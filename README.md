# DevTrack

A full-stack task & time tracking application, built as a reference implementation of **Hexagonal Architecture** (Ports & Adapters) on the backend and a **feature-based, signal-driven** structure on the frontend.

Three domain entities, wired end-to-end: **Project** → **Task** → **Time Entry**.

- **Backend**: Java 21, Spring Boot 3, Maven, PostgreSQL (H2 for tests)
- **Frontend**: Angular 17 (standalone components, native Signals, no NgRx)
- **Everything free and open-source** — no paid licenses, no cloud dependencies to run it

---

## Table of contents

- [Architecture](#architecture)
- [Tech stack](#tech-stack)
- [Project structure](#project-structure)
- [Getting started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [1. Start the database](#1-start-the-database)
  - [2. Run the backend](#2-run-the-backend)
  - [3. Run the frontend](#3-run-the-frontend)
- [Running the backend from Eclipse](#running-the-backend-from-eclipse)
- [API reference](#api-reference)
- [Testing](#testing)
- [Configuration](#configuration)
- [Known limitations & roadmap](#known-limitations--roadmap)

---

## Architecture

### Backend: Hexagonal Architecture (Ports & Adapters)

The backend is organized so that **business logic never depends on frameworks or infrastructure** — only the other way around. Each of the three entities (`Project`, `Task`, `TimeEntry`) follows the exact same layout:

```
┌─────────────────────────────────────────────────────────────────┐
│  infrastructure/web              infrastructure/persistence      │
│  (REST controllers, DTOs,        (JPA entities, Spring Data       │
│   validation, error mapping)      repositories)                   │
│         │  implements                    │  implements            │
│         ▼  inbound ports                 ▼  outbound port         │
│  ┌───────────────────────────────────────────────────────────┐   │
│  │                      application                          │   │
│  │  port/in/   → one interface per use case (Create/Get/...) │   │
│  │  port/out/  → outbound contract (e.g. ProjectRepositoryPort)│  │
│  │  service/   → one class per use case, implements a port/in │  │
│  └───────────────────────────────────────────────────────────┘   │
│                              │ uses                                │
│                              ▼                                     │
│                          domain                                    │
│              (plain Java, zero framework annotations)              │
└─────────────────────────────────────────────────────────────────┘
```

**Why this matters:**

- `domain/Project.java`, `Task.java`, `TimeEntry.java` are **plain Java objects**. No `@Entity`, no Spring annotation — they can be unit-tested with zero framework bootstrap (see `ProjectTest`, `TaskTest`, `TimeEntryTest`), and business invariants (e.g. "a task title can't be blank", "hours must be positive") are enforced right there, not scattered across controllers.
- Every use case (`CreateProjectUseCase`, `DeleteTaskUseCase`, ...) is its **own interface**, implemented by its **own service class**. There is no single bloated `ProjectService` doing everything — each class has one reason to change.
- The **outbound port** (`ProjectRepositoryPort`, `TaskRepositoryPort`, `TimeEntryRepositoryPort`) is an interface owned by the application layer. `infrastructure/persistence` provides the real implementation (`ProjectPersistenceAdapter`, backed by Spring Data JPA). Swapping PostgreSQL for something else would mean writing a new adapter — the domain and application layers wouldn't change at all.
- **Cross-aggregate references are by ID, not by object graph.** A `Task` holds a `projectId: Long`, not a `Project` reference; a `TimeEntry` holds a `taskId: Long`. This keeps the three aggregates independently loadable and testable, and mirrors how the JPA entities are mapped (plain `project_id`/`task_id` foreign-key columns, no `@ManyToOne`).
- `CreateTaskService` and `CreateTimeEntryService` validate that the parent (`Project`, `Task`) actually exists **before** creating the child — via the parent's outbound port — so referential integrity is enforced at the application layer, not left to the database to reject.

### Frontend: feature-based, signal-driven

```
src/app/
├── core/                    → singletons: HTTP error interceptor, notification service
├── shared/                  → reserved for reusable, logic-free UI (currently empty)
└── features/
    ├── projects/
    │   ├── project.model.ts        → TypeScript interfaces mirroring the backend DTOs
    │   ├── project.service.ts      → HttpClient calls, nothing else
    │   └── components/
    │       ├── project-list/       → SMART: owns state (Signals), talks to the service
    │       ├── project-card/       → PRESENTATIONAL: @Input/@Output only, no HTTP
    │       └── project-form/       → PRESENTATIONAL: reactive form, emits on submit
    ├── tasks/          (same shape)
    └── time-entries/   (same shape)
```

**Why this matters:**

- **Smart vs. presentational** is a strict split: only the `*-list` components inject services or hold state; `*-card` and `*-form` components are pure — they render `@Input()`s and emit `@Output()`s. This makes the presentational components trivially unit-testable (see the `*.component.spec.ts` files) and reusable.
- **State is native Angular Signals** (`signal()`, `.set()`, `.update()`) — no NgRx, no RxJS state management library. For an app this size, Signals give the same reactivity with far less boilerplate.
- **Routing is lazy** (`loadComponent`) per feature, and each list component doubles as both a "scoped" view (e.g. `/projects/:projectId/tasks`) and a "global" view (`/tasks`, reading every task via the unfiltered backend endpoint) — same component, branching on whether a route param is present.
- The **HTTP error interceptor** (`core/interceptors/error.interceptor.ts`) catches every failed request and surfaces it through a `NotificationService` signal, rendered as a dismissible banner in `app.component.ts` — so a backend error is never silently swallowed.

---

## Tech stack

| Layer | Technology | Notes |
|---|---|---|
| Backend language/runtime | Java 21 (LTS) | |
| Backend framework | Spring Boot 3.3 | Web, Data JPA, Validation |
| Build tool | Maven | |
| Database (runtime) | PostgreSQL 16 | via Docker |
| Database (tests) | H2 (in-memory) | swapped in automatically for the test classpath, no Docker needed to run `mvn test` |
| Boilerplate reduction | Lombok | JPA entities only — the domain model is hand-written |
| Backend testing | JUnit 5, Mockito, AssertJ, Spring MockMvc | |
| Frontend framework | Angular 17 | standalone components, no NgModules |
| Frontend state | Angular Signals | no NgRx |
| Frontend testing | Jasmine, Karma | |
| Containerization | Docker Compose | database only — backend and frontend run natively for fast reload |

---

## Project structure

```
devTrack/
├── docker-compose.yml          # PostgreSQL for local development
├── backend/                    # Spring Boot API (hexagonal architecture)
│   ├── pom.xml
│   ├── DevtrackApplication.launch   # ready-made Eclipse run configuration
│   └── src/
│       ├── main/java/com/devtrack/
│       │   ├── domain/                    # Project, Task, TimeEntry + their enums
│       │   ├── application/
│       │   │   ├── port/in/               # one interface per use case
│       │   │   ├── port/out/              # outbound repository contracts
│       │   │   ├── service/               # use case implementations
│       │   │   └── exception/             # domain-level "not found" exceptions
│       │   └── infrastructure/
│       │       ├── web/                   # REST controllers, DTOs, mappers, CORS, error handling
│       │       └── persistence/           # JPA entities, Spring Data repositories, mappers
│       ├── main/resources/application.yml # runtime config (PostgreSQL, port 8081)
│       └── test/                          # mirrors main/, plus src/test/resources/application.yml (H2)
└── frontend/                   # Angular SPA
    ├── angular.json / package.json
    └── src/app/
        ├── core/                          # HTTP error interceptor, notification service
        ├── shared/                        # reserved for reusable UI (empty for now)
        └── features/{projects,tasks,time-entries}/
```

---

## Getting started

### Prerequisites

| Tool | Version | Required for |
|---|---|---|
| JDK **with `javac`** | 21 | backend (a JRE-only install will not compile it) |
| Maven | 3.9+ | backend |
| Node.js / npm | 20+ / 10+ | frontend |
| Docker + Docker Compose | any recent version | the PostgreSQL database |

> If any of these aren't installed system-wide, they can be installed user-locally without root access — see [Configuration](#configuration) for the pattern used during development of this project.

### 1. Start the database

```bash
docker compose up -d db
```

Starts a single `postgres:16-alpine` container (`devtrack-postgres`) with:

- database `devtrack`, user `devtrack`, password `devtrack` (matches `application.yml` — local dev only, see the comments in `docker-compose.yml`)
- data persisted in the named volume `devtrack-postgres-data`
- a healthcheck, so `docker compose up -d --wait db` blocks until Postgres is actually ready

```bash
docker compose down       # stop, keep data
docker compose down -v    # stop and wipe the volume (fresh DB next time)
```

Hibernate is configured with `ddl-auto: update`, so the schema (`projects`, `tasks`, `time_entries` tables) is created/updated automatically on backend startup — no manual migration step for this project's scope.

### 2. Run the backend

```bash
cd backend
mvn spring-boot:run
```

The API starts on **`http://localhost:8081`**, base path `/api`.

Run the test suite (does **not** need Docker/Postgres — it runs against an in-memory H2 database instead, see [Testing](#testing)):

```bash
mvn test
```

### 3. Run the frontend

```bash
cd frontend
npm install
npm start
```

Opens on **`http://localhost:4200`**, calling the backend at `http://localhost:8081/api` (configured in `src/environments/environment.ts`). CORS for any `http://localhost:*` origin is enabled on the backend (`WebConfig.java`), so the Angular dev server can call it directly — no proxy needed.

Run the frontend tests:

```bash
npm test
```

(Headless Chrome is required; on a machine without one installed, see `frontend/karma.conf.ci.js` for a working setup using Puppeteer's bundled Chromium.)

---

## Running the backend from Eclipse

This project was developed importing the backend as an **existing Maven project** (`File → Import → Maven → Existing Maven Projects`, pointing at `backend/`). A ready-made run configuration is committed at `backend/DevtrackApplication.launch` — after importing, it shows up directly under **Run → Run Configurations…**, or can be run by double-clicking the file in the Project Explorer.

Two Eclipse-specific gotchas this project's setup already works around:

1. **Lombok in the editor.** Eclipse's own compiler (`ecj`) needs the Lombok Java agent to see the generated getters/setters, which a plain `-javaagent` isn't enough for inside Eclipse's OSGi runtime — it also needs `-Xbootclasspath/a`. If importing on a machine where this isn't set up yet, launch Eclipse with:
   ```bash
   LOMBOK_JAR="$(find ~/.m2 -name 'lombok-*.jar' | grep -v sources | head -1)"
   JAVA_TOOL_OPTIONS="-javaagent:$LOMBOK_JAR -Xbootclasspath/a:$LOMBOK_JAR" eclipse &
   ```
2. **That same `JAVA_TOOL_OPTIONS` must *not* leak into the app's own run.** Lombok is compile-time only — the running app doesn't need the agent, and it isn't relevant at runtime. `DevtrackApplication.launch` already overrides `JAVA_TOOL_OPTIONS` to empty for its own process (see its **Environment** tab), so this is a non-issue once the launch config is used as committed.

---

## API reference

Base path: `http://localhost:8081/api`. All request/response bodies are JSON.

### Projects

| Method | Path | Body | Description |
|---|---|---|---|
| `POST` | `/projects` | `{ name, description }` | Create a project (starts in `PLANNED`) |
| `GET` | `/projects` | — | List all projects |
| `GET` | `/projects/{id}` | — | Get one project |
| `PUT` | `/projects/{id}` | `{ name, description, status }` | Update a project |
| `DELETE` | `/projects/{id}` | — | Delete a project |

`status`: `PLANNED` · `ACTIVE` · `ON_HOLD` · `COMPLETED`

### Tasks

| Method | Path | Body | Description |
|---|---|---|---|
| `POST` | `/tasks` | `{ title, description, priority, projectId }` | Create a task (starts in `TODO`) — 404 if `projectId` doesn't exist |
| `GET` | `/tasks` | — | List **all** tasks, across every project |
| `GET` | `/tasks/{id}` | — | Get one task |
| `GET` | `/projects/{projectId}/tasks` | — | List tasks for **one** project |
| `PUT` | `/tasks/{id}` | `{ title, description, status, priority }` | Update a task (project can't be changed) |
| `DELETE` | `/tasks/{id}` | — | Delete a task |

`status`: `TODO` · `IN_PROGRESS` · `DONE`
`priority`: `LOW` · `MEDIUM` · `HIGH` · `URGENT`

### Time entries

| Method | Path | Body | Description |
|---|---|---|---|
| `POST` | `/time-entries` | `{ taskId, date, hours, notes }` | Log time (404 if `taskId` doesn't exist, 400 if `hours <= 0`) |
| `GET` | `/time-entries` | — | List **all** entries, across every task |
| `GET` | `/time-entries/{id}` | — | Get one entry |
| `GET` | `/tasks/{taskId}/time-entries` | — | List entries for **one** task |
| `PUT` | `/time-entries/{id}` | `{ date, hours, notes }` | Update an entry (task can't be changed) |
| `DELETE` | `/time-entries/{id}` | — | Delete an entry |

### Errors

Every error response has the same shape (`ApiError`), produced by a single `@RestControllerAdvice`:

```json
{
  "timestamp": "2026-09-24T19:00:21.4Z",
  "status": 404,
  "error": "Not Found",
  "message": "Task not found with id=999",
  "details": []
}
```

`400` for Bean Validation failures fills `details` with one message per invalid field; unexpected server errors return a generic `500` without leaking internal exception messages.

---

## Testing

| | Count | What's covered |
|---|---|---|
| **Backend** | 51 tests | Domain invariants (pure unit tests, no Spring); application services (unit, Mockito-mocked ports, including not-found error paths); REST controllers (Spring `MockMvc` + real H2, full CRUD + validation + cross-entity 404s); centralized exception handling |
| **Frontend** | 35 tests | HTTP services (`HttpClientTestingModule`); smart components (mocked services, both "scoped" and "global" list modes); presentational form components (validation, reset behavior); presentational card components (event emission) |

Backend tests never touch Docker: `src/test/resources/application.yml` overrides the datasource to an in-memory H2 instance, picked up automatically by Spring Boot because the test classpath takes priority over `src/main/resources` for same-named files.

```bash
cd backend && mvn test    # 51 tests, a few seconds, no external dependencies
cd frontend && npm test   # 35 tests, headless Chrome
```

---

## Configuration

All of the following were installed **user-locally** (no `sudo`) during development, since the target environment didn't allow root access — the pattern works on any Linux machine without package-manager privileges:

```bash
# JDK 21 (a full JDK, not just a JRE — the JRE alone lacks javac)
curl -fsSL -o jdk.tar.gz "https://api.adoptium.net/v3/binary/latest/21/ga/linux/x64/jdk/hotspot/normal/eclipse?project=jdk"
mkdir -p ~/.local/opt/jdk21 && tar xzf jdk.tar.gz -C ~/.local/opt/jdk21 --strip-components=1

# Maven
curl -fsSL -o maven.tar.gz "https://dlcdn.apache.org/maven/maven-3/3.9.16/binaries/apache-maven-3.9.16-bin.tar.gz"
mkdir -p ~/.local/opt/maven && tar xzf maven.tar.gz -C ~/.local/opt/maven --strip-components=1

# Node.js
curl -fsSL -o node.tar.xz "https://nodejs.org/dist/v22.23.2/node-v22.23.2-linux-x64.tar.xz"
mkdir -p ~/.local/opt/node && tar xJf node.tar.xz -C ~/.local/opt/node --strip-components=1

export JAVA_HOME="$HOME/.local/opt/jdk21"
export PATH="$JAVA_HOME/bin:$HOME/.local/opt/maven/bin:$HOME/.local/opt/node/bin:$PATH"
```

Add the `export` lines to `~/.bashrc` to persist them across terminal sessions.

**Ports** used by default — change if any conflict on your machine:

| Service | Port | Where to change it |
|---|---|---|
| Backend | `8081` | `backend/src/main/resources/application.yml` → `server.port` |
| Frontend | `4200` | `ng serve --port <n>`, or `frontend/angular.json` |
| PostgreSQL | `5432` | `docker-compose.yml` (`ports:`) — also update `application.yml`'s `spring.datasource.url` to match |

If you change the backend port, also update `frontend/src/environments/environment.ts` (`apiBaseUrl`).

---

## Known limitations & roadmap

This is a portfolio/learning project; scope was deliberately kept focused. Explicitly out of scope for now:

- **No update UI on the frontend** — the backend supports `PUT` on all three entities, but the Angular UI currently only wires up create/delete. Editing would reuse the existing form components with pre-filled values.
- **No CI pipeline** — tests are run locally only; a GitHub Actions workflow running `mvn test` and `ng test` on push would be a natural next step.
- **No auth** — every endpoint is unauthenticated, appropriate for a local-only demo.
- **`ddl-auto: update`** generates the schema automatically from the JPA entities. Fine for this project's scope, but not how a production system should manage schema changes — a real deployment would use Flyway or Liquibase migrations instead (with `ddl-auto: validate`).
- **No pagination** — list endpoints return every row. Fine at demo scale, would need pagination (`Pageable`) before real data volume.

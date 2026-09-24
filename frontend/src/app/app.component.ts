import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { NotificationService } from './core/notification.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <header class="app-header">
      <h1>DevTrack</h1>
      <nav>
        <a routerLink="/projects" routerLinkActive="active">Projects</a>
        <a routerLink="/tasks" routerLinkActive="active">Tasks</a>
        <a routerLink="/time-entries" routerLinkActive="active">Time Entries</a>
      </nav>
    </header>
    @if (notificationService.message(); as message) {
      <div class="error-banner">
        <span>{{ message }}</span>
        <button type="button" (click)="notificationService.clear()">&times;</button>
      </div>
    }
    <main>
      <router-outlet />
    </main>
  `,
  styles: [
    `
      .app-header {
        padding: 1rem 1.5rem;
        background: #1f2937;
        color: white;
        display: flex;
        align-items: center;
        gap: 2rem;
      }

      .app-header h1 {
        margin: 0;
        font-size: 1.25rem;
      }

      nav {
        display: flex;
        gap: 1.25rem;
      }

      nav a {
        color: #d1d5db;
        text-decoration: none;
        font-size: 0.9rem;
        padding: 0.25rem 0;
        border-bottom: 2px solid transparent;
      }

      nav a:hover {
        color: white;
      }

      nav a.active {
        color: white;
        border-bottom-color: #60a5fa;
      }

      .error-banner {
        background: #fef2f2;
        border-bottom: 1px solid #fecaca;
        color: #991b1b;
        padding: 0.75rem 1.5rem;
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-size: 0.9rem;
      }

      .error-banner button {
        background: none;
        border: none;
        color: #991b1b;
        font-size: 1.1rem;
        cursor: pointer;
        line-height: 1;
      }

      main {
        padding: 1.5rem;
        max-width: 960px;
        margin: 0 auto;
      }
    `
  ]
})
export class AppComponent {
  protected readonly notificationService = inject(NotificationService);
}

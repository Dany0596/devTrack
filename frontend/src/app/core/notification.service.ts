import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly _message = signal<string | null>(null);
  readonly message = this._message.asReadonly();

  showError(message: string): void {
    this._message.set(message);
  }

  clear(): void {
    this._message.set(null);
  }
}

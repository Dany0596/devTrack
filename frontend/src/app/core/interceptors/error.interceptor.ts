import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { NotificationService } from '../notification.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notificationService = inject(NotificationService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const message = error.error?.message ?? 'Unexpected error while contacting the server';
      notificationService.showError(message);
      return throwError(() => error);
    })
  );
};

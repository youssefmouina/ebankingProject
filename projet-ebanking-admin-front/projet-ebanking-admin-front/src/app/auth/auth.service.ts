import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly BASE_URL = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: { email: string, password: string }): Observable<boolean> {
    return this.http.post<any>(`${this.BASE_URL}/authenticate`, credentials, { withCredentials: true }).pipe(
      map(res => !!res.accessToken),
      catchError(() => of(false))
    );
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.BASE_URL}/register`, user, { withCredentials: true });
  }

  logout(): void {
    this.http.post(`${this.BASE_URL}/logout`, {}, { withCredentials: true }).subscribe({
      next: () => this.router.navigate(['/login']),
      error: () => this.router.navigate(['/login']) // même si erreur, on redirige
    });
  }

  verifySession(): Observable<boolean> {
    return this.http.post(`${this.BASE_URL}/refresh-token`, {}, { withCredentials: true }).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }
}

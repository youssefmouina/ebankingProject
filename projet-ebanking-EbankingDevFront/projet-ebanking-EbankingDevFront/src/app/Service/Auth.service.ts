import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { UserLoginDTO } from '../model/dto/UserLoginDTO';
import { UserResDTO } from '../model/dto/UserResDTO';
import { UserRegisterDTO } from '../model/dto/UserRegistryDTO';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private isAuthenticatedFlag = false;

  constructor(private http: HttpClient, private router: Router) {}

  setAuthenticated(status: boolean) {
    this.isAuthenticatedFlag = status;
  }

  isAuthenticated(): boolean {
    // return this.isAuthenticatedFlag || !!sessionStorage.getItem('userid');
      return this.isAuthenticatedFlag || !!localStorage.getItem('userid');
  }

  // Method to get user info (since user_id is also in HttpOnly cookie)
  register(dto: Partial<UserRegisterDTO>): Observable<UserResDTO> {
    return this.http.post<any>(
      'http://localhost:8080/api/auth/register',
      dto,
      { withCredentials: true }
     );
  }

  login(dto: Partial<UserLoginDTO>): Observable<UserResDTO> {
    return this.http.post<any>(
      'http://localhost:8080/api/auth/authenticate',
      dto,
      { withCredentials: true }
    );
  }
  logout(): void {
    // Call backend logout endpoint to clear HttpOnly cookies
    this.http.post(
      'http://localhost:8080/api/auth/logout',
      {},
      { withCredentials: true }
    ).subscribe({
      next: () => {
        this.setAuthenticated(false);
        // sessionStorage.removeItem('userid');
        localStorage.removeItem('userid');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        // Even if logout fails, clear local state
        this.setAuthenticated(false);
        // sessionStorage.removeItem('userid');
        localStorage.removeItem('userid');
        this.router.navigate(['/login']);
      }
    });
  }
}

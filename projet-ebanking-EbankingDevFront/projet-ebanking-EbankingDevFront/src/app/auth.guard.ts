import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './Service/Auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.authService.isAuthenticated()) {
      return true;
    }
    // sessionStorage.removeItem('userid');
    localStorage.removeItem('userid')
    this.router.navigate(['/login'], {
      queryParams: { redirect: state.url }  // <-- preserve attempted URL
  });
    return false;
  }
}

import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../Service/Auth.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  dto = {email: '', password: ''};
  error = '';
  onForgotPassword(event: Event){

  };
  private redirectUrl: string = '/dashboard';  // default redirect

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute, private authService: AuthService) {
    this.route.queryParams.subscribe(params => {
      this.redirectUrl = params['redirect'] || '/dashboard';
    });
  }

  handleLogin(event: Event) {
    event.preventDefault();
    this.error = '';


    this.authService.login(this.dto).subscribe({
      next: (authData) => {
        // sessionStorage.setItem('userid', authData.userId)
        localStorage.setItem('userid', authData.userId);
        this.authService.setAuthenticated(true);
        this.router.navigateByUrl(this.redirectUrl);
      },
      error: (err) => {
        this.error = "Email ou Password incorrect"
      }
    });
  }
}

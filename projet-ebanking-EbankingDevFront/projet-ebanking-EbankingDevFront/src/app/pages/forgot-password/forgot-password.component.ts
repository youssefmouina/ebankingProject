import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../Service/Auth.service';
import { MaybeClientDTO } from '../../model/dto/MaybeClientDTO';
import { MaybeClientWithEmailTokenDTO } from '../../model/dto/MaybeClientWithEmailTokenDTO';
import { UserRegisterDTO } from '../../model/dto/UserRegistryDTO';
import { CheckRecoveryTokenDTO } from '../../model/dto/CheckRecoveryTokenDTO';
import { ChangePasswordDTO } from '../../model/dto/ChangePasswordDTO';
import {ForgotPassword} from '../../Service/ForgotPassword.service';

@Component({
  selector: 'app-forgot-password',
  standalone: false,
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {
  step = 1;
  emailTokenSent = false;
  phoneTokenSent = false;
  emailToken = '';
  phoneToken = '';
  isEmailCodeValid = false;
  isPhoneCodeValid = false;
  error = '';

  dto : CheckRecoveryTokenDTO = {
    email: '',
    recoveryPasswordToken: ''
  };
  login = {
    email: '',
    password: ''
  }
  password = {
    newPassword: '',
    confirmPassword: ''
  }
  changePassword : ChangePasswordDTO = {
    email: '',
    newPassword: '',
    currentToken: '',
  };
  constructor(private forgotPassword: ForgotPassword, private authService: AuthService, private router: Router) {}

  sendEmailToken(): void {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!this.dto.email.trim()) {
      this.error = 'Veuillez saisir votre email';
      return;
    }
    if (!emailRegex.test(this.dto.email.trim())) {
      this.error = 'Veuillez saisir un email valide';
      return;
    }
    this.forgotPassword.generateRecoveryPasswordToken(this.dto.email).subscribe({
      next: (res) => {
        if(res === true){
          this.emailTokenSent = true;
          return;
        } else{
          this.error = "Failed to generate token. Please check the email or try again";
          return;
        }
      },
      error: (err) => {
        this.error = "An error occurred while generating the token:, " + err;
        return;
      }
    });
    this.error = '';
    this.step = 2;
  }

  onEmailCodeComplete(code: string): void {
    this.emailToken = code;
    this.isEmailCodeValid = true;
    console.log('Email verification code completed:', code);

    // Auto-verify when code is complete
    this.verifyEmailToken();
  }

  onEmailCodeChanged(code: string): void {
    this.emailToken = code;
    this.isEmailCodeValid = code.length === 6;

    // Clear error when user starts typing
    if (this.error) {
      this.error = '';
    }
  }

  verifyEmailToken(): void {
    if (!this.emailToken.trim() || this.emailToken.trim().length !== 6) {
      this.error = 'Veuillez saisir le code de vérification complet';
      return;
    }
    let checkRecoveryTokenDTO = new CheckRecoveryTokenDTO(this.dto.email, this.dto.recoveryPasswordToken);

    // Simulate email verification
    this.forgotPassword.checkRecoveryToken(checkRecoveryTokenDTO).subscribe({
      next: (res) => {
        if(res === true){
          this.step = 3;
          return;
        } else{
          this.step = 2;
          this.error = "Token incorrect";
          return;
        }
      },
      error: (err) => {
        this.step = 2;
        this.error = "An error occurred while verifying the token:, " + err;
        return;
      }
    });

    // For demo purposes, accept any 6-digit code
    this.error = '';
  }

  handleChangePassword(): void {
    if (!this.password.confirmPassword || !this.password.newPassword) {
      if(!this.password.newPassword){
        this.error = 'Veuillez saisir votre nouveau mot de passe';
      }
      else if(!this.password.confirmPassword){
        this.error = 'Veuillez confirmer votre mot de passe';
      }
      return;
    }
    if(this.password.confirmPassword !== this.password.newPassword){
      this.error = 'le mot de passe n\'est pas compatible';
      return
    }
    if(this.password.newPassword.length < 8){
      this.error = 'Password must contain at least 8 characters.';
      return;
    }
    this.changePassword.email = this.dto.email;
    this.changePassword.newPassword = this.password.newPassword;
    this.changePassword.currentToken = this.dto.recoveryPasswordToken;

    // Simulate sending phone token
    this.forgotPassword.changePassword(this.changePassword).subscribe({
      next: (res) => {
        this.login.email = this.dto.email;
        this.login.password = this.changePassword.newPassword;
        if(res == true){
            this.authService.login(this.login).subscribe({
            next: (authData) => {
              // sessionStorage.setItem('userid', authData.userId)
              localStorage.setItem('userid', authData.userId);
              this.authService.setAuthenticated(true);
              this.router.navigateByUrl('/dashboard');
            },
            error: (err) => {
              this.error = "Email ou Password incorrect"
            }
          });
        } else{
          this.error = "echoue de changement de mot de passe"
          return;
        }
      },
      error: (err) => {
        this.error = "echoue de changement de mot de passe"
        return;
      }
    });
    this.error = '';
  }

  goBackToLogin(): void {
    this.router.navigate(['/login']);
  }
  handleRetourStep2(){
    this.step = 2;
    console.log(this.emailToken);
  }
}

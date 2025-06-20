import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../Service/Auth.service';
import { MaybeClientDTO } from '../../model/dto/MaybeClientDTO';
import { MaybeClientWithEmailTokenDTO } from '../../model/dto/MaybeClientWithEmailTokenDTO';
import { UserRegisterDTO } from '../../model/dto/UserRegistryDTO';
import {RegisterVerification} from '../../Service/RegisterVerification.service';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  step = 1;
  emailTokenSent = false;
  phoneTokenSent = false;
  emailToken = '';
  phoneToken = '';
  isEmailCodeValid = false;
  isPhoneCodeValid = false;
  error = '';

  dto : UserRegisterDTO = {
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    phone: '',
    job: '',
    password: ''
  };
  constructor(private registerVerification: RegisterVerification, private authService: AuthService, private router: Router) {}

  sendEmailToken(): void {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!this.dto.firstName.trim() || !this.dto.lastName.trim() || !this.dto.email.trim()) {
      if (!this.dto.firstName.trim()) {
        this.error = 'Veuillez saisir votre prénom';
      }else if(!this.dto.lastName.trim()){
        this.error = 'Veuillez saisir votre nom';
      }
      else{
        this.error = 'Veuillez saisir votre email';
      }
      return;
    }
    if (!emailRegex.test(this.dto.email.trim())) {
      this.error = 'Veuillez saisir un email valide';
      return;
    }
    let maybeClientDTO = new MaybeClientDTO(this.dto.firstName, this.dto.lastName, this.dto.email);
    this.registerVerification.generateTokenByEmail(maybeClientDTO).subscribe({
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
    let maybeClientWithEmailTokenDTO = new MaybeClientWithEmailTokenDTO(this.dto.firstName, this.dto.lastName, this.dto.email, this.emailToken);

    // Simulate email verification
    this.registerVerification.checkEmailToken(maybeClientWithEmailTokenDTO).subscribe({
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

  sendPhoneToken(): void {
    if (!this.dto.username.trim() || !this.dto.phone.trim()) {
      if(!this.dto.username.trim()){
        this.error = 'Veuillez saisir votre nom d\'utilisateur';
      }
      else{
        this.error = 'Veuillez saisir votre numéro de téléphone';
      }
      return;
    }

    // Simulate sending phone token
    console.log('Sending phone token to:', this.dto.phone);
    this.phoneTokenSent = true;
    this.error = '';
    this.step = 4;
  }
  continueToLastStep(): void{
    if (!this.dto.password) {
      this.error = 'Veuillez saisir votre mot de passe';
      return;
    }

    if (this.dto.password.length < 8) {
      this.error = 'Password must contain at least 8 characters.';
      return;
    }

    this.error = '';
    this.step = 6;
  }

  onPhoneCodeComplete(code: string): void {
    this.phoneToken = code;
    this.isPhoneCodeValid = true;
    console.log('Phone verification code completed:', code);

    // Auto-verify when code is complete
    this.verifyPhoneToken();
  }

  onPhoneCodeChanged(code: string): void {
    this.phoneToken = code;
    this.isPhoneCodeValid = code.length === 6;

    // Clear error when user starts typing
    if (this.error) {
      this.error = '';
    }
  }

  verifyPhoneToken(): void {
    if (!this.phoneToken.trim() || this.phoneToken.trim().length !== 6) {
      this.error = 'Veuillez saisir le code de vérification complet';
      return;
    }

    // Simulate phone verification
    console.log('Verifying phone token:', this.phoneToken);

    // For demo purposes, accept any 6-digit code
    this.error = '';
    this.step = 5;
  }

  handleRegister(event: Event): void {
    event.preventDefault();

    // Validate all required fields
    if (!this.dto.firstName.trim() || !this.dto.lastName.trim() || !this.dto.email.trim() ||
    !this.dto.username.trim() || !this.dto.phone.trim() || !this.dto.password) {
      this.error = 'Veuillez remplir tous les champs obligatoires';
      return;
    }

    // Simulate registration
    this.authService.register(this.dto).subscribe({
      next: (res) => {
        this.authService.setAuthenticated(true);
        this.router.navigateByUrl("/dashboard");
        localStorage.setItem('userid', res.userId)
      },
      error: (err) => {
        this.error = "An error occurred while registrating, " + err;
        return;
      }
    });
  }
  // Navigation helpers
  goBackToLogin(): void {
    this.router.navigate(['/login']);
  }
  handleRetourStep2(){
    this.step = 2;
    console.log(this.emailToken);
  }
}

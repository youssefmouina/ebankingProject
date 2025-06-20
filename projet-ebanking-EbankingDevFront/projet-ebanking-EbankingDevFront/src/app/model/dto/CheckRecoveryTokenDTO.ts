export class CheckRecoveryTokenDTO {
  email!: string;
  recoveryPasswordToken!: string;
  constructor(email: string, recoveryPasswordToken: string) {
    this.email = email;
    this.recoveryPasswordToken = recoveryPasswordToken;
  }
}

export class MaybeClientWithEmailTokenDTO {
    firstName!: string;
    lastName!: string;
    email!: string;
    emailToken!: string;

    constructor(firstName: string, lastName: string, email: string, emailToken: string) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.emailToken = emailToken;
  }
  }

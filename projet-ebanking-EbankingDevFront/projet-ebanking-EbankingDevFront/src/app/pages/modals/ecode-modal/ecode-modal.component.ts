import { Component, EventEmitter, Input, Output, ViewChildren , ElementRef, QueryList } from '@angular/core';
import { ClientService } from '../../../Service/client.service';
import { EcodeDTO } from '../../../model/dto/EcodeDTO';
import {AuthService} from '../../../Service/Auth.service';


@Component({
  selector: 'ecode-modal',
  standalone: false,
  templateUrl: './ecode-modal.component.html',
  styleUrl: './ecode-modal.component.css'
})
export class EcodeModalComponent {


  constructor(
    private clientService : ClientService,
    private authService : AuthService
  ){}

  @Output() onCancel = new EventEmitter<boolean>();


  ecode = '';
  confirmEcode='';
  isLoading : boolean =false;
  display=true;
  generateVerificationCodeModal : boolean = false;
  ecodeNotSecure: string = '';
  verificationCode = '';
  clientId :number = Number(localStorage.getItem('userid'));
  ecodeDTO: EcodeDTO ={
    clientId:this.clientId,
    code:''
  }
  invalidTokenError=''
  showSuccessModal : boolean=false;
  showFailureModal : boolean=false;
  errorMessage : string ='';




  allowOnlyNumbers(event: KeyboardEvent): void {
    const charCode = event.key.charCodeAt(0);
    if (charCode < 48 || charCode > 57) {
      event.preventDefault(); // block non-digit input
    }
  }


  cancel() {
    this.onCancel.emit(false);
  }

  onEcodeChange() {
    if (this.ecodeNotSecure) {
      this.ecodeNotSecure = '';
    }
  }



  onVerificationCodeChange(){
    if(this.invalidTokenError){
      this.invalidTokenError='';
    }
  }

  closeSuccessModal() {
    this.showSuccessModal = false;
    this.display=true;
    this.generateVerificationCodeModal=false;

  }

  closeFailureModal() {
    this.showFailureModal = false;
    this.generateVerificationCodeModal=true;
  }


  verificationCodeDigits: string[] = ['', '', '', '', '', ''];
  codeDigits = new Array(6);

  @ViewChildren('codeInput') inputs!: QueryList<ElementRef>;

  onDigitInput(event: any, index: number) {
    const input = event.target;
    const value = input.value;

    if (/^\d$/.test(value) && index < 5) {
      this.inputs.toArray()[index + 1].nativeElement.focus();
    }

    this.updateFullCode();
  }

  onKeyDown(event: KeyboardEvent, index: number) {
    if (event.key === 'Backspace' && !this.verificationCodeDigits[index] && index > 0) {
      this.inputs.toArray()[index - 1].nativeElement.focus();
    }
  }

  handlePaste(event: ClipboardEvent) {
    const pastedText = event.clipboardData?.getData('text') ?? '';
    const digits = pastedText.replace(/\D/g, '').slice(0, 6).split('');
    digits.forEach((d, i) => {
      this.verificationCodeDigits[i] = d;
    });
    this.updateFullCode();

    setTimeout(() => {
      const inputsArray = this.inputs.toArray();
      inputsArray[Math.min(digits.length, 5)].nativeElement.focus();
    });
  }

  updateFullCode() {
    this.verificationCode = this.verificationCodeDigits.join('');
  }





  validate() {
    this.isLoading = true;

    this.clientService.checkEcodeSecurity(this.ecode).subscribe({
      next: (result: boolean) => {
        setTimeout(() => {
          this.isLoading = false;

          if (result) {
            this.generateVerificationCodeModal=true;
            this.display=false;

             // Appel de la méthode sendEcodeTokenForVerification si result est true
            this.clientService.sendEcodeTokenForVerification(this.clientId).subscribe({
              next: (sendResult: boolean) => {
                if (sendResult) {
                  console.log("Token envoyé avec succès !");
                } else {
                  console.error("Erreur lors de l'envoi du token.");
                }
              },
              error: (err) => {
                if (err.status === 401 || err.status === 403) {
                  this.authService.logout();
                }
                else{
                  console.error("Erreur lors de l'appel à sendEcodeTokenForVerification :", err);
                }
              }
            });

          } else {
            this.ecodeNotSecure = "Le code entré est invalide ou non sécurisé.";
          }
        }, 2000);
      },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.authService.logout();
        }
        else{
          setTimeout(() => {
            this.isLoading = false;
            console.error("Erreur lors de la vérification du code :", err);
          }, 2000);
        }
      }
    });
  }

  verifyEmailCode() {

    this.clientService.verififyTokenEcodeSubmittedByClient(this.verificationCode, this.clientId).subscribe({
      next: (sendResult: boolean) => {
        if (sendResult) {
          this.ecodeDTO = {
            clientId: this.clientId,
            code: this.ecode
          };

          this.clientService.saveEcode(this.ecodeDTO).subscribe({
            next: (result: boolean) => {
              if (result) {
                setTimeout(()=>{
                  this.showSuccessModal = true;
                  this.generateVerificationCodeModal = false;
                },500)
              } else {
                this.errorMessage="erreur lors du changement du ecode"
                setTimeout(()=>{
                  this.showFailureModal = true;
                  this.generateVerificationCodeModal=false;

                },500)
              }
            },
            error: (err) => {
              if (err.status === 401 || err.status === 403) {
                this.authService.logout();
              }
              else{
                console.error("erreur lors de sauvegarde du ecode :", err);
              }
            }
          });
        } else {

          this.errorMessage="token saisi par client est invalide !"
          setTimeout(()=>{
            this.generateVerificationCodeModal=false;
            this.showFailureModal = true;
          },500)
        }
      },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.authService.logout();
        }
        else{
          console.error("Erreur lors de l'appel à verififyTokenEcodeSubmittedByClient :", err);
        }
      }
    });
  }
























}

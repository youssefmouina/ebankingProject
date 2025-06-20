import { Component, Input, Output, EventEmitter, } from '@angular/core';

import { InvoicesService } from '../../../Service/invoices.service'; // adjust path
import { InvoiceResDTO } from '../../../model/dto/InvoiceResDTO'; // adjust path
import { ComptesService } from '../../../Service/comptes.service'; // adjust path
import { StatusCompte } from '../../../model/dto/StatusCompte';
import { CompteResDTO } from '../../../model/dto/CompteResDTO';
import { InvoicePayDTO } from '../../../model/dto/InvoicePayDTO';
import { AuthService } from '../../../Service/Auth.service';
import { ClientService } from '../../../Service/client.service';
import { EcodeDTO } from '../../../model/dto/EcodeDTO';

@Component({
  selector: 'invoice-modal',
  standalone: false,
  templateUrl: './invoice-modal.component.html',
  styleUrl: './invoice-modal.component.css'
})
export class InvoiceModalComponent {

  constructor(private invoiceService: InvoicesService, private comptesService: ComptesService, private authService: AuthService, private clientService: ClientService) { }

  @Input() showModal: boolean = false;
  @Input() provider: string = "";
  @Output() close = new EventEmitter<void>();

  // clientId = Number(sessionStorage.getItem('userid')); //TODO : bring it from the token
  clientId = Number(localStorage.getItem('userid'));
  reference: string = "";
  showInvoiceToPay: boolean = false;
  invoiceResDto!: InvoiceResDTO
  selectedCompte: any = null;
  isPayButtonEnabled = false;
  invoiceNotFound: boolean = false;
  CCourantActifComptes: CompteResDTO[] = [];
  code='';
  isLoadingForEcodeVeriFication=false;
  showEcodeModal=false;
  ecodeDTO : EcodeDTO= {
      clientId: this.clientId,
      code: ''
    };

    @Output() onCancel = new EventEmitter<boolean>();



  showSuccessModal = false;
  showFailureModal = false;


  closeModal() {
    this.showInvoiceToPay = false;
    this.close.emit();
  }
  openSuccessModal() {
    this.showSuccessModal = true;
  }

  closeSuccessModal() {
    this.showSuccessModal = false;
  }

  openFailureModal() {
    this.showFailureModal = true;
  }

  closeFailureModal() {
    this.showFailureModal = false;
  }

  allowOnlyNumbers(event: KeyboardEvent): void {
    const charCode = event.key.charCodeAt(0);
    if (charCode < 48 || charCode > 57) {
      event.preventDefault(); // block non-digit input
    }
  }

  cancel(){

    this.showEcodeModal = false;
    this.code = '';
    this.isLoadingForEcodeVeriFication = false;
    this.onCancel.emit(false);

  }






  onSelectCompte(compte: any) {
    this.selectedCompte = compte;

    // Enable the button only if the account has enough balance
    if (this.invoiceResDto && compte.solde > this.invoiceResDto.amount && compte.autorisePaiementEnLigne) {
      this.isPayButtonEnabled = true;
    } else {
      this.isPayButtonEnabled = false;
    }
  }



  getCompteCouranActiftByClientId() { // to get compte courant actif du client
    this.comptesService.getCompte(this.clientId, "ccourant", StatusCompte.ACTIF)
      .subscribe({
        next: (response) => { // store the data returned from the service in a variable called response
          console.log("geting Comptes Courant Actif By ClientId method");
          this.CCourantActifComptes = response;
          console.log("compte courant actif : ", this.CCourantActifComptes)
        },
        error: (err) => {
          if (err.status === 401 || err.status === 403) {
            this.authService.logout();
          } else {
              console.error("Error fetching le compte courant de l'utilisateur :", this.clientId, err);
          }
        }
      });
  }


  searchReference() {
    this.invoiceService.getInvoice(this.clientId, this.provider, this.reference)
      .subscribe({
        next: (response) => { // store the data returned from the service in a variable called response
          this.invoiceResDto = response;
          this.showInvoiceToPay = true;
          this.getCompteCouranActiftByClientId();
          this.isPayButtonEnabled = false; // reset the button state for new invoice
          this.invoiceNotFound = false;
          console.log("invoice not found ? ", this.invoiceNotFound);
        },
        error: (err) => {
          if (err.status === 401 || err.status === 403) {
            this.authService.logout();
          } else if (err.status === 404) {
            this.invoiceNotFound = true;
            this.showInvoiceToPay = false;
          } else {
            console.error("Unexpected error:", err);
          }
        }

      });
  }
  payInvoice() {
    const invoiceDTO: InvoicePayDTO = {
      clientId: this.clientId,
      provider: this.invoiceResDto.provider,
      referenceNumber: this.invoiceResDto.referenceNumber,
      compteId: this.selectedCompte.id
    };
    this.invoiceService.payInvoice(invoiceDTO).subscribe({
      next: (response) => {
        console.log('Invoice paid successfully:', response);
        this.openSuccessModal();
        this.closeModal();
      },
      error: (error) => {
        if (error.status === 401 || error.status === 403) {
          this.authService.logout();
        } else {
          console.error('Error paying invoice:', error);
          this.openFailureModal();
          this.closeModal();
        }
      }
    });
  }
  // afficher form pour saisir ecode et valider l'operation
  DisplayEcodeModal(){
    this.showEcodeModal=true;
    this.showInvoiceToPay=false;
  }
  confirmInvoice() {
    this.ecodeDTO = {
      clientId: this.clientId,
      code: this.code
    };
    // verifier ecode saisie par client pour valider operation(virement,paiement)
    this.clientService.checkEcodeForOperations(this.ecodeDTO).subscribe({
      next: (sendResult: boolean) => {

        console.log(sendResult);
        if (sendResult) {


          const invoiceDTO: InvoicePayDTO = {
            clientId: this.clientId,
            provider: this.invoiceResDto.provider,
            referenceNumber: this.invoiceResDto.referenceNumber,
            compteId: this.selectedCompte.id
          };
          this.invoiceService.payInvoice(invoiceDTO).subscribe({
            next: (response) => {
              this.isLoadingForEcodeVeriFication=true;
              setTimeout(()=>{
                this.showSuccessModal=true;
                this.showEcodeModal = false;
                this.code='';
              },2000)
              console.log('Invoice paid successfully:', response);
            },
            error: (error) => {
              if (error.status === 401 || error.status === 403) {
                this.authService.logout();
              }
              console.error('Error paying invoice:', error);
            }
          });
        } else {
          this.isLoadingForEcodeVeriFication=true;
          setTimeout(()=>{
            this.isLoadingForEcodeVeriFication=false;
            this.showEcodeModal = false;
            this.showFailureModal=true;
            this.code='';
          },2000)
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
  }
}

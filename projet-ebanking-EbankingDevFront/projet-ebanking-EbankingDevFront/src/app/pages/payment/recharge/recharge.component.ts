import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RechargeService } from '../../../Service/recharge.service'; // adapte le chemin si besoin
import { RechargeDTO } from '../../../model/dto/RechargeDTO';
import { RechargeResDTO } from '../../../model/dto/RechargeResDTO';
import { ComptesService } from '../../../Service/comptes.service';
import { StatusCompte } from '../../../model/dto/StatusCompte';
import { CompteResDTO } from '../../../model/dto/CompteResDTO';
import { EcodeDTO } from '../../../model/dto/EcodeDTO';
import { EventEmitter , Output , ViewChildren, QueryList, ElementRef } from '@angular/core';
import { ClientService } from '../../../Service/client.service';
import { AuthService } from '../../../Service/Auth.service';

@Component({
  selector: 'recharge',
  standalone: false,
  templateUrl: './recharge.component.html',
  styleUrl: './recharge.component.css'
})
export class RechargeComponent {
  rechargeForm: FormGroup;
  operators = [
    {
      name: 'Inwi',
      logo: '../assets/recharge/inwi.png',
      rechargeDescription: 'operator.inwi.description'
    },
    {
      name: 'Orange',
      logo: '../assets/recharge/orange.png',
      rechargeDescription: 'operator.orange.description'
    },
    {
      name: 'IAM',
      logo: '../assets/recharge/iam.png',
      rechargeDescription: 'operator.iam.description'
    }
  ];

  showRechargeForm = false;
  selectedOperator: string = '';
  // clientId: number = Number(sessionStorage.getItem('userid'));
  clientId: number = Number(localStorage.getItem('userid'));
  selectedRib: string = '';
  selectedCompte: CompteResDTO | null = null;
  selectedMontant: string = '';
  comptesCourants: CompteResDTO[] = [];
  showSuccessMessage = false;
  successMessage = '';
  isProcessing = false;
  ecodeDTO : EcodeDTO= {
    clientId: this.clientId,
    code: ''
  };
  showEcodeModal : boolean=false
  verificationCode='';
  showSuccessModal : boolean=false;
  showFailureModal : boolean=false;

  code = '';
  isLoadingForEcodeVeriFication: boolean=false;




  constructor(
    private rechargeService: RechargeService,
    private fb: FormBuilder,
    private comptesService: ComptesService,
    private authService: AuthService,
    private clinetService: ClientService
  ) {
    this.rechargeForm = this.fb.group({
      phoneNumber: ['', [Validators.required, Validators.pattern('^(06|07)[0-9]{8}$')]],
      montant: ['', Validators.required]
    });
  }
  @Output() onCancel = new EventEmitter<boolean>();

  ngOnInit(): void {
    this.comptesService.getCompte(this.clientId, "ccourant", StatusCompte.ACTIF)
          .subscribe({
            next: (response) => { // store the data returned from the service in a variable called response
              this.comptesCourants=response;
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

  onCompteChange(event: any) {
    this.selectedRib = event.target.value;
    this.selectedCompte = this.comptesCourants.find(c => c.rib === this.selectedRib) || null;
  }

  onMontantChange(event: any) {
    this.selectedMontant = event.target.value;
  }

  isFormValid(): boolean {
    return this.selectedCompte !== null &&
           this.rechargeForm.valid &&
           this.selectedMontant !== '' &&
           this.selectedCompte.autorisePaiementEnLigne &&
           this.handleVerifiedSolde(this.selectedCompte.solde, parseFloat(this.selectedMontant));
  }

  handleRecharge(operatorName: string){
    this.selectedOperator = operatorName;
    this.showRechargeForm = true;
    this.selectedCompte = null;
    this.rechargeForm.reset();
    this.selectedMontant = '';
  }

  public handleVerifiedSolde(solde:number, montant:number){
    return solde > montant;
  }

  public parseFloatValue(value: string): number {
    return parseFloat(value);
  }




  cancel() {
    this.showEcodeModal = false;
    this.code = '';
    this.isLoadingForEcodeVeriFication = false;
    this.onCancel.emit(false);
  }
  allowOnlyNumbers(event: KeyboardEvent): void {
    const charCode = event.key.charCodeAt(0);
    if (charCode < 48 || charCode > 57) {
      event.preventDefault(); // block non-digit input
    }
  }

  closeSuccessModal() {
    this.showSuccessModal = false;
  }

  closeFailureModal() {
    this.showFailureModal = false;
  }


  // afficher form pour saisir ecode et valider l'operation
  DisplayEcodeModal(){
    if (this.isFormValid()) {
      this.isProcessing = true;
      setTimeout(()=>{
        this.showEcodeModal=true;
        this.isProcessing = false;
        this.showRechargeForm=false;
      },2000)
    }
  }

  /*confirmRecharge() {
    if (this.isFormValid()) {
      this.isProcessing = true;
      const rechargeDTO: RechargeDTO = {
        operateur: this.selectedOperator,
        rib: this.selectedRib,
        phoneNumber: this.rechargeForm.get('phoneNumber')?.value,
        montant: this.parseFloatValue(this.selectedMontant)
      };

      this.rechargeService.effectuerRecharge(rechargeDTO).subscribe({
        next: (response: RechargeResDTO) => {
          console.log('Recharge effectuée avec succès', response);
          this.showSuccessMessage = true;
          this.successMessage = `Recharge de ${this.selectedMontant} DH effectuée avec succès pour le numéro ${this.rechargeForm.get('phoneNumber')?.value}`;

          this.showRechargeForm = false;
          this.selectedOperator = '';
          this.selectedCompte = null;
          this.rechargeForm.reset();
          this.selectedMontant = '';

          setTimeout(() => {
            this.showSuccessMessage = false;
            this.successMessage = '';
          }, 8000);
        },
        error: (error) => {
          if (error.status === 401 || error.status === 403) {
            this.authService.logout();
          } else {
            console.error('Erreur lors de la recharge', error);
            this.showSuccessMessage = true;
            this.successMessage = 'Une erreur est survenue lors de la recharge. Veuillez réessayer.';
            setTimeout(() => {
              this.showSuccessMessage = false;
              this.successMessage = '';
            }, 8000);
          }
        },
        complete: () => {
          this.isProcessing = false;
        }
      });
    }
  }*/

  confirmRecharge() {
    if (this.isFormValid()) {


      this.ecodeDTO = {
        clientId: this.clientId,
        code: this.code
      };


      // verifier ecode saisie par client pour valider operation(virement,paiement)
      this.clinetService.checkEcodeForOperations(this.ecodeDTO).subscribe({
        next: (sendResult: boolean) => {

          if (sendResult) {
            const rechargeDTO: RechargeDTO = {
              operateur: this.selectedOperator,
              rib: this.selectedRib,
              phoneNumber: this.rechargeForm.get('phoneNumber')?.value,
              montant: this.parseFloatValue(this.selectedMontant)
            };
            this.rechargeService.effectuerRecharge(rechargeDTO).subscribe({
              next: (response: RechargeResDTO) => {
                this.isLoadingForEcodeVeriFication=true;
                setTimeout(()=>{
                  this.showSuccessModal=true;
                  this.showEcodeModal = false;
                  this.selectedOperator = '';
                  this.selectedCompte = null;
                  this.rechargeForm.reset();
                  this.selectedMontant = '';
                },2000)
              },
              error: (error) => {
                if (error.status === 401 || error.status === 403) {
                  this.authService.logout();
                }
                console.error('Erreur lors de la recharge', error);
              }
            });
          } else {
            this.isLoadingForEcodeVeriFication=true;
            setTimeout(()=>{
              this.showEcodeModal = false;
              this.showFailureModal=true;
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
}

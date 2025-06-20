import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CompteService } from '../../Service/CompteService';
import { CCourantResDTO } from '../../model/dto/CCouurantResDTO';
import { VirementService } from '../../Service/VirementService';
import {TypeTransaction} from '../../model/dto/TypeTransaction';
import {VirementDTO} from '../../model/dto/VirementDTO';
import 'sweetalert2';
import Swal from 'sweetalert2';
import {VirementResDTO} from '../../model/dto/VirementResDTO';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';
import { AuthService } from '../../Service/Auth.service';

@Component({
  selector: 'app-transfers',
  standalone: false,
  templateUrl: './transfers.component.html',
  styleUrl: './transfers.component.css'
})
export class TransfersComponent implements OnInit {

  transfer = new FormGroup({
    compteEmetteur: new FormControl<string | null>('', Validators.required),
    compteRecepteur: new FormControl<string | null>(null, Validators.required),
    montant: new FormControl<number | null>(null, [Validators.required, Validators.min(0.01)]),
    type: new FormControl<TypeTransaction>(TypeTransaction.INSTANTANEE)
  });
  transferResp = new FormGroup({
    compteEmetteur: new FormControl('', Validators.required),
    compteRecepteur: new FormControl('', Validators.required),
    montant: new FormControl(null, [Validators.required, Validators.min(0.01)]),
    type: new FormControl('')
  });
  pdfUrl:SafeResourceUrl | null = null
  idVirement:number|null = null;
  constructor(private compteService: CompteService, private virementService: VirementService,private sanitizer: DomSanitizer, private authService: AuthService ) {
  }
  fromComptes : CCourantResDTO[] = [];
  // clientId : string = sessionStorage.getItem('userid') ?? '';
  clientId : string = localStorage.getItem('userid') ?? '';
  viewReceipt(): void {
    if(this.idVirement){
      this.virementService.getReceiptByVirementId(this.idVirement).subscribe({
        next: blob => {
        const url = URL.createObjectURL(blob);
        this.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
        const modal = new (window as any).bootstrap.Modal(document.getElementById('receiptModal'));
        modal.show();
        }, error: (err) =>{
          if (err.status === 401 || err.status === 403) {
            this.authService.logout();
          } else {
            console.error('Error loading informations', err);
          }
        }
      });
    }

  }
  ngOnInit(): void {
    this.compteService.getCCourantByClientId(this.clientId).subscribe(
      {
        next: (response) => { // store the data returned from the service in a variable called response
          console.log("geting Comptes Courant Actif By ClientId method");
          this.fromComptes = response;
          console.log("compte courant actif : ", this.fromComptes)
        },
        error: (err) => {
          if (err.status === 401 || err.status === 403) {
            this.authService.logout();
          } else {
            console.error("Error fetching le compte courant de l'utilisateur :", this.clientId, err);
          }
        }
      }
    );
  }
  get isFormReady(): boolean {
    return this.transfer.valid &&
      this.transfer.get('compteEmetteur')?.value !== null &&
      this.transfer.get('compteRecepteur')?.value !== null &&
      this.transfer.get('compteRecepteur')?.value?.length===24 &&
      this.transfer.get('montant')?.value !== null;
  }
  onSubmit(): void {
    if (this.transfer.valid) {
      const formValue = this.transfer.value;

      // Vérifie que tous les champs obligatoires sont bien présents
      if (
        formValue.compteEmetteur != null &&
        formValue.compteRecepteur != null &&
        formValue.montant != null
      ) {
        const payload: VirementDTO = {
          compteEmetteur: formValue.compteEmetteur,
          compteRecepteur: formValue.compteRecepteur,
          montant: formValue.montant,
          type: formValue.type ?? TypeTransaction.INSTANTANEE
        };
        console.log(payload);

        this.virementService.executeVirement(payload).subscribe({
          next: (response) => {
            console.log("Virement réussi :", response);
            Swal.fire({
              icon: 'success',
              title: 'Succès',
              text: 'Le virement a été effectué avec succès !'
            });
            this.idVirement=response.data.id;
          },
          error: (err) => {
            if (err.status === 401 || err.status === 403) {
              this.authService.logout();
            } else {
              console.error("Erreur virement :", err);
              Swal.fire({
                icon: 'error',
                title: 'Erreur',
                text: 'Une erreur est survenue lors du virement. Veuillez réessayer.'
            });
          }
        }});
      } else {
        console.error("Certains champs sont vides ou null !");
      }
    }
  }

}

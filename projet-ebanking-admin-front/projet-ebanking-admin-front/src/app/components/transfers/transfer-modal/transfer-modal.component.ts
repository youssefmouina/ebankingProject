import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TransfersService } from '../../../services/transfers.service';
import { VirementDTO } from '../../../model/VirementDTO';
import { TypeTransaction } from '../../../model/TypeTransaction';
import swal from 'sweetalert2';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-virement-modal',
  templateUrl: './transfer-modal.component.html',
  styleUrls: ['./transfer-modal.component.css'],
  standalone: false
})
export class TransferModalComponent {
  @Output() close = new EventEmitter<void>();

  virementForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private transferService: TransfersService,
    private translate: TranslateService // 💬 Ajout du service de traduction
  ) {
    this.virementForm = this.fb.group({
      compteEmetteur: ['', [Validators.required, Validators.pattern(/^\d{24}$/)]],
      compteRecepteur: ['', [Validators.required, Validators.pattern(/^\d{24}$/)]],
      montant: ['', [Validators.required, Validators.min(0.01)]],
      type: [TypeTransaction.NORMAL, Validators.required]
    });
  }

  get emetteur() { return this.virementForm.get('compteEmetteur')!; }
  get rib() { return this.virementForm.get('compteRecepteur')!; }
  get montant() { return this.virementForm.get('montant')!; }

  closeModal() {
    this.close.emit();
  }

  submitTransfer() {
    if (this.virementForm.invalid) {
      this.virementForm.markAllAsTouched();
      return;
    }

    const request: VirementDTO = this.virementForm.value;

    this.transferService.executeVirement(request).subscribe({
      next: () => {
        this.translate.get([
          'TRANSFER.SUCCESS_TITLE',
          'TRANSFER.SUCCESS_TEXT',
          'TRANSFER.CONFIRM_BUTTON'
        ]).subscribe((translations: { [x: string]: any; }) => {
          swal.fire({
            icon: 'success',
            title: translations['TRANSFER.SUCCESS_TITLE'],
            text: translations['TRANSFER.SUCCESS_TEXT'],
            confirmButtonText: translations['TRANSFER.CONFIRM_BUTTON']
          });
          this.closeModal();
        });
      },
      error: (err) => {
        this.translate.get([
          'TRANSFER.ERROR_TEXT',
          'TRANSFER.CONFIRM_BUTTON'
        ]).subscribe(translations => {
          swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: translations['TRANSFER.ERROR_TEXT'],
            confirmButtonText: translations['TRANSFER.CONFIRM_BUTTON']
          });
        });
        console.error(err);
        this.errorMessage = err.error?.error || 'Erreur lors du virement.';
      }
    });
  }
}

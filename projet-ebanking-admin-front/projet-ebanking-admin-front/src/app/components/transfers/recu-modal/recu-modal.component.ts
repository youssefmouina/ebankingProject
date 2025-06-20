import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { TransfersService } from '../../../services/transfers.service';

@Component({
  selector: 'app-recu-modal',
  templateUrl: './recu-modal.component.html',
  styleUrls: ['./recu-modal.component.css'],
  standalone: false
})
export class RecuModalComponent implements OnInit {
  @Input() virementId!: number;
  @Output() close = new EventEmitter<void>();

  pdfUrl: SafeResourceUrl | null = null;
  errorMessage: string = '';

  constructor(
    private transferService: TransfersService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    if (this.virementId) {
      this.transferService.getReceiptPdf(this.virementId).subscribe({
        next: (blob) => {
          const fileURL = URL.createObjectURL(blob);
          this.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(fileURL);
        },
        error: (err) => {
          this.errorMessage = 'Impossible de charger le reçu.';
          console.error(err);
        }
      });
    }
  }

  closeModal() {
    this.close.emit();
  }
}

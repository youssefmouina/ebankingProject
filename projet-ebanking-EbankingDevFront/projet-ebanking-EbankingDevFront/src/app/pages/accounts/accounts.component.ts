import { Component, OnInit } from '@angular/core';
import { NgClass, NgForOf, NgIf } from '@angular/common';
import { TranslatePipe } from '@ngx-translate/core';
import { CompteService } from '../../Service/CompteService';
import { VirementService } from '../../Service/VirementService';
import { CompteResDTO } from '../../model/dto/CompteResDTO';
import { VirementResDTO } from '../../model/dto/VirementResDTO';
import { FormsModule } from '@angular/forms';
import { RechargeResDTO } from '../../model/dto/RechargeResDTO';
import { InvoiceResDTO } from '../../model/dto/InvoiceResDTO';
import { RechargeService } from '../../Service/RechargeService';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { AuthService } from '../../Service/Auth.service';
import { InvoicesService } from '../../Service/invoices.service';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  imports: [
    NgIf,
    NgForOf,
    NgClass,
    TranslatePipe,
    FormsModule
  ],
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {
  selectedOperation: string = 'virement';
  accounts: CompteResDTO[] = [];
  virements: VirementResDTO[] = [];
  recharges: RechargeResDTO[] = [];
  factures: InvoiceResDTO[] = [];
  selectedAccount: CompteResDTO | null = null;
  page = 1;
  maxItems = 3;
  totalPages: number = 1;
  totalPagesR: number = 1;
  totalPagesI: number = 1;
  pdfUrl: SafeResourceUrl | null = null;
  clientId=localStorage.getItem('userid');

  constructor(
    private compteService: CompteService,
    private virementService: VirementService,
    private rechargeService: RechargeService,
    private sanitizer: DomSanitizer,
    private authService: AuthService,
    private invoicesService: InvoicesService,
  ) {}

  ngOnInit() {
    // const userId = sessionStorage.getItem('userid') ?? '';
    const userId = localStorage.getItem('userid')  ?? '';

    this.compteService.getByClientId(userId, "compte", "tout").subscribe({
      next: (comptes: CompteResDTO[]) => {
        this.accounts.push(...comptes);
        if (!this.selectedAccount && this.accounts.length > 0) {
          const courantAccount = this.accounts.find(acc => acc.accountType === "CCourant");
          this.selectAccount(courantAccount || this.accounts[0]);
        }
      },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.authService.logout();
        } else {
          console.error('Error loading contents', err);
        }
      }
    }
  );
}

  viewReceipt(virement: VirementResDTO): void {
    this.virementService.getReceiptByVirementId(virement.id).subscribe(
      {
        next: blob => {
          const url = URL.createObjectURL(blob);
          this.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
          const modal = new (window as any).bootstrap.Modal(document.getElementById('receiptModal'));
          modal.show();
        },
        error: (err) => {
          if (err.status === 401 || err.status === 403) {
            this.authService.logout();
          } else {
            console.error('Error loading contents', err);
          }
        }
      }
    )
  }

  onOperationTypeChange(type: string) {
    if (!this.selectedAccount) return;

    switch (type) { //this.selectedOperation
      case 'virement':
        this.loadVirements();
        break;
      case 'recharge':
        this.loadRecharges();
        break;
      case 'facture':
        this.loadFactures();
        break;
    }
  }

  selectOperation(type: string) {
    this.selectedOperation = type;
    this.onOperationTypeChange(type);
  }

  getAccountsByStatus(status: string): CompteResDTO[] {
    return this.accounts.filter(a => a.status === status);
  }

  loadVirements() {
    if (this.selectedAccount) {
      this.virements = [];
      this.virementService.getVirementsByCompteId(this.selectedAccount.id, this.page, this.maxItems)
        .subscribe(
          {
            next:
            response => {
            this.virements = response.content;
            this.totalPages = response.totalPages;
            },
            error: (err) => {
              if (err.status === 401 || err.status === 403) {
                this.authService.logout();
              } else {
                console.error('Error loading contents', err);
              }
            }
          }
        )
      }
    }

  loadRecharges() {
    if (this.selectedAccount) {
      this.recharges = [];
      this.rechargeService.getRechargesByCompteId(this.selectedAccount.id, this.page, this.maxItems)
        .subscribe(
          {
            next:
            response => {
            this.recharges = response.content;
            this.totalPagesR = response.totalPages;},
            error: (err) => {
              if (err.status === 401 || err.status === 403) {
                this.authService.logout();
              } else {
                console.error('Error loading contents', err);
              }
            }
          }
        )
      }
    }
    loadFactures() {
    if (this.selectedAccount) {
      this.factures = [];
      this.invoicesService.getInvoicesByCompteId(this.selectedAccount.id, this.page, this.maxItems)
        .subscribe(
          {
            next:
            response => {
            this.factures = response.content;
            this.totalPagesR = response.totalPages;},
            error: (err) => {
              if (err.status === 401 || err.status === 403) {
                this.authService.logout();
              } else {
                console.error('Error loading contents', err);
              }
            }
          }
        )
      }
    }

  setPage(p: number) {
    if (p < 1 || p > this.totalPages) return;
    this.page = p;
    this.loadVirements();
  }

  setPageR(p: number) {
    if (p < 1 || p > this.totalPagesR) return;
    this.page = p;
    this.loadRecharges();
  }

  setPageI(p: number) {
    if (p < 1 || p > this.totalPagesI) return;
    this.page = p;
    this.loadRecharges();
  }

  selectAccount(account: CompteResDTO) {
    this.selectedOperation = "virement";
    this.selectedAccount = account;
    this.loadVirements();
  }

  getVisibleStatuses(): string[] {
    return ['ACTIF', 'BLOQUE', 'FERME'].filter(
      status => this.getAccountsByStatus(status).length > 0
    );
  }
  onDotationToggle(event: Event, account: any): void {
    const checked = (event.target as HTMLInputElement).checked;
    this.toggleDotation(account.id, checked);
  }



  toggleDotation(accountId: number, autorisePaiementEnLigne: boolean): void {
    console.log('New value:', autorisePaiementEnLigne);
    this.compteService.changeAutorisedPaymentEnLigne(accountId, autorisePaiementEnLigne).subscribe({
      next: (result: boolean) => {
        console.log('changed successfully');
      },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.authService.logout();
        }
        else{
          console.error('problème pour changement de dotation status de compte :', err);
        }
      }
    });
  }

}

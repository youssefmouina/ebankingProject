import { Component, OnInit, OnDestroy, ChangeDetectionStrategy, ChangeDetectorRef, Inject, PLATFORM_ID, Input, OnChanges, SimpleChanges } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { BinanceWebSocketService } from '../../../Service/binance-api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CryptoModel } from '../../../model/dto/CryptoModel';
import { CryptoserviceService } from '../../../Service/cryptoservice.service';
import { AuthService } from '../../../Service/Auth.service';
import { CompteResDTO } from '../../../model/dto/CompteResDTO';
import { StatusCompte } from '../../../model/dto/StatusCompte';
import { ComptesService } from '../../../Service/comptes.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-crypto-dashboard',
  standalone: false,
  templateUrl: './crypto-dashboard.component.html',
  styleUrls: ['./crypto-dashboard.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CryptoDashboardComponent implements OnInit, OnDestroy, OnChanges {
  @Input() valueboolean!: boolean;
  value: boolean = true;

  selectedRib: string = '';
  cryptoData: CryptoModel[] = [];
  hasBought: { [symbol: string]: boolean } = {};
  quantities: { [symbol: string]: number } = {};
  ribs: { [symbol: string]: string } = {};
  private activeInputSymbol: string | null = null;
  private shouldRefocusInput: boolean = false;
  private isDropdownOpen: boolean = false;
  selectedCompte: CompteResDTO | null = null;
  comptesCourants: CompteResDTO[] = [];

  clientId: number = Number(localStorage.getItem('userid'));

  constructor(
    private comptesService: ComptesService,
    private wsService: BinanceWebSocketService,
    private cdr: ChangeDetectorRef,
    private http: CryptoserviceService,
    private authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  onCompteChange(event: any) {
    this.selectedRib = event.target.value;
    this.selectedCompte = this.comptesCourants.find(c => c.rib === this.selectedRib) || null;
    this.isDropdownOpen = false; // Reset dropdown state
    this.cdr.markForCheck();
  }

  // Add method to handle dropdown focus events
  onDropdownFocus() {
    this.isDropdownOpen = true;
    this.shouldRefocusInput = false; // Prevent input refocus while dropdown is open
  }

  onDropdownBlur() {
    // Use setTimeout to allow click events to complete before resetting
    setTimeout(() => {
      this.isDropdownOpen = false;
    }, 150);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['valueboolean']) {
      this.value = this.valueboolean;
      this.cdr.markForCheck();
    }
  }

  ngOnInit() {
    this.wsService.connect();
    this.wsService.cryptoData$.subscribe({
      next: data => {
        this.cryptoData = this.updateCryptoData(data);

        // Only refocus if not interacting with dropdown and input was previously focused
        if (isPlatformBrowser(this.platformId) && this.shouldRefocusInput && !this.isDropdownOpen) {
          setTimeout(() => {
            if (this.activeInputSymbol && !this.isDropdownOpen) {
              const input = document.querySelector(`input[data-symbol="${this.activeInputSymbol}"]`) as HTMLInputElement;
              if (input && document.activeElement !== input) {
                input.focus();
              }
            }
          }, 0);
        }
        this.cdr.markForCheck();
      },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.authService.logout();
        } else {
          console.error('Error in WebSocket subscription', err);
        }
      }
    });

    if (isPlatformBrowser(this.platformId)) {
      document.addEventListener('focusin', this.handleFocusIn.bind(this));
      document.addEventListener('focusout', this.handleFocusOut.bind(this));
    }

    this.loadComptesCourants();
  }

  private loadComptesCourants() {
    this.comptesService.getCompte(this.clientId, "ccourant", StatusCompte.ACTIF)
      .subscribe({
        next: (response: CompteResDTO[]) => {
          this.comptesCourants = [...response];
          this.cdr.markForCheck();
        },
        error: (err: any) => {
          if (err.status === 401 || err.status === 403) {
            this.authService.logout();
          } else {
            console.error("Error fetching comptes courants:", err);
          }
        }
      });
  }

  ngOnDestroy() {
    this.wsService.disconnect();
    if (isPlatformBrowser(this.platformId)) {
      document.removeEventListener('focusin', this.handleFocusIn.bind(this));
      document.removeEventListener('focusout', this.handleFocusOut.bind(this));
    }
  }

  private handleFocusIn(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target.classList.contains('quantity-input')) {
      this.activeInputSymbol = target.getAttribute('data-symbol');
      this.shouldRefocusInput = true;
      this.isDropdownOpen = false;
    } else if (target.tagName === 'SELECT' || target.closest('select')) {
      // User is interacting with dropdown
      this.shouldRefocusInput = false;
      this.isDropdownOpen = true;
    }
  }

  private handleFocusOut(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target.classList.contains('quantity-input')) {
      // Delay clearing to allow for quick refocus
      setTimeout(() => {
        if (document.activeElement !== target) {
          this.shouldRefocusInput = false;
        }
      }, 100);
    }
  }

  private updateCryptoData(newData: CryptoModel[]): CryptoModel[] {
    const updatedData = [...this.cryptoData];

    newData.forEach(newCrypto => {
      const existingIndex = updatedData.findIndex(c => c.symbol === newCrypto.symbol);
      if (existingIndex >= 0) {
        updatedData[existingIndex] = { ...updatedData[existingIndex], ...newCrypto };
      } else {
        updatedData.push({ ...newCrypto });
      }
    });

    return updatedData.filter(c => newData.some(n => n.symbol === c.symbol));
  }

  formatPrice(price: string | undefined): string {
    return price ? parseFloat(price).toFixed(2) : 'N/A';
  }

  isPositive(value: string | undefined): boolean {
    if (!value) return false;
    const num = parseFloat(value);
    return !isNaN(num) && num >= 0;
  }

  buy(symbol: string, currentPrice: string) {
    const quantity = this.quantities[symbol] || 0;
    const rib = this.selectedRib || '';

    if (!rib) {
      console.error('No RIB selected');
      return;
    }

    const transaction = {
      name: symbol,
      montant: quantity,
      actuealprisecurrency: currentPrice
    };

    this.http.post(transaction, rib).subscribe({
      next: (ref: any) => {
        if(ref===true){
          console.log('Purchase successful', ref);
          this.hasBought[symbol] = true;
          this.quantities[symbol] = 0;
          this.shouldRefocusInput = false; // Clear focus state after transaction
          this.activeInputSymbol = null;
          this.cdr.markForCheck();
          Swal.fire({
            title: 'Success!',
            text: 'Your transaction was successful',
            icon: 'success'
          });
        }
        else{
          Swal.fire({
            title: 'error!',
            text: 'Your transaction was failed',
            icon: "error"
          });
        }

      },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.authService.logout();
        } else {
          console.error('Error in purchase', err);
        }
      }
    });
  }

  sell(symbol: string, currentPrice: string) {
    const quantity = this.quantities[symbol] || 0;
    const rib = this.ribs[symbol] || '';

    if (!rib) {
      console.error('No RIB selected');
      return;
    }

    const transaction = {
      name: symbol,
      montant: quantity,
      actuealprisecurrency: currentPrice
    };

    this.http.postvendre(transaction, rib).subscribe({
      next: (ref: any) => {
        console.log('Sale successful', ref);
        this.hasBought[symbol] = false;
        this.quantities[symbol] = 0;
        this.shouldRefocusInput = false; // Clear focus state after transaction
        this.activeInputSymbol = null;
        this.cdr.markForCheck();
      },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.authService.logout();
        } else {
          console.error('Error in sale', err);
        }
      }
    });
  }

  trackBySymbol(index: number, crypto: CryptoModel): string {
    return crypto.symbol;
  }
}

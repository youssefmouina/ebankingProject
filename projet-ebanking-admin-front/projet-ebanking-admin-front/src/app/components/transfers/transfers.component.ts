import { Component, OnInit } from '@angular/core';
import { VirementResDTO } from '../../model/VirementResDTO';
import {TransfersService} from '../../services/transfers.service';
import {TypeTransaction} from '../../model/TypeTransaction';

@Component({
  selector: 'app-transfers',
  templateUrl: './transfers.component.html',
  styleUrls: ['./transfers.component.css'],
  standalone: false
})
export class TransfersComponent implements OnInit {
  virements: VirementResDTO[] = [];
  currentPage = 0;
  pageSize = 10;
  hasMore = false;

  showVirementModal = false;
  selectedRecuId: number | null = null;

  constructor(private transfersService: TransfersService) {}

  ngOnInit() {
    this.fetchVirements();
  }

  fetchVirements() {
    this.transfersService.getVirements(this.currentPage, this.pageSize)
      .subscribe((page) => {
        this.virements = page.content;
        this.hasMore = !page.last;
      });
  }

  nextPage() {
    if (this.hasMore) {
      this.currentPage++;
      this.fetchVirements();
    }
  }

  previousPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.fetchVirements();
    }
  }

  openVirementModal() {
    this.showVirementModal = true;
  }

  closeVirementModal() {
    this.showVirementModal = false;
    this.fetchVirements();
  }

  openRecuModal(id: number) {
    this.selectedRecuId = id;
  }

  closeRecuModal() {
    this.selectedRecuId = null;
  }

  protected readonly TypeTransaction = TypeTransaction;
}

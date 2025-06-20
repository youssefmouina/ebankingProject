import {Component, OnInit} from '@angular/core';
import {CompteResDTO} from '../../model/dto/CompteResDTO';
import {CompteService} from '../../Service/CompteService';
import {VirementResDTO} from '../../model/dto/VirementResDTO';
import {VirementService} from '../../Service/VirementService';
import { AuthService } from '../../Service/Auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {

  courantAccount: CompteResDTO | null = null;
  epargneAccount: CompteResDTO | null = null;
  recentVirements: VirementResDTO[] = [];

  constructor(
    private compteService: CompteService,
    private virementService: VirementService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // const userId = sessionStorage.getItem('userid') ?? '';
    const userId = localStorage.getItem('userid')  ?? '';
    this.compteService.getByClientId(userId, "compte", "tout").subscribe({
      next:(comptes: CompteResDTO[]) => {
        this.courantAccount = comptes.find(acc => acc.accountType === "CCourant") || null;
        this.epargneAccount = comptes.find(acc => acc.accountType === "CEpargne") || null;
        if (this.courantAccount) {
          this.virementService.getVirementsByCompteId(this.courantAccount.id, 1, 5).subscribe({
            next:response => {
            this.recentVirements = response.content;
            console.log(this.recentVirements);
          },error: (err) => {
            if (err.status === 401 || err.status === 403) {
              this.authService.logout();
            } else {
              console.error('Error loading contents', err);
              }
            }
          });
        }
      },error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.authService.logout();
        } else {
          console.error('Error loading contents', err);}
        }
      }
    );
  }
}

import { Component, OnInit } from '@angular/core';
import {DashboardStatsResDTO} from '../../model/DashStatsResDTO';
import {StatsService} from '../../services/stats.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  standalone: false
})
export class DashboardComponent implements OnInit {
  dashboardData?: DashboardStatsResDTO;  // <-- hold the actual data here
  dashboardSubscription?: Subscription;
  showClientStats = true;
  showCurrentStats = false;
  showSavingsStats = false;

  constructor(private statsSevice:StatsService) {

  }

  ngOnInit(): void {
    this.dashboardSubscription = this.statsSevice.getDashboardStats().subscribe({
      next: (data) => {
        this.dashboardData = data;  // <-- assign received data here
      },
      error: (err) => {
        console.error('Error fetching dashboard stats', err);
      }
    });
  }

  ngOnDestroy(): void {
    this.dashboardSubscription?.unsubscribe();  // good practice to avoid memory leaks
  }

  toggleClientStats(): void {
    this.showClientStats = true;
    this.showCurrentStats = false;
    this.showSavingsStats = false;
  }

  toggleCurrentStats(): void {
    this.showCurrentStats = true;
    this.showClientStats = false;
    this.showSavingsStats = false;
  }

  toggleSavingsStats(): void {
    this.showSavingsStats = true;
    this.showClientStats = false;
    this.showCurrentStats = false;
  }
}

  import { Component, OnInit } from '@angular/core';
  import { ChartType, ChartConfiguration, ChartOptions } from 'chart.js';
  import { StatsService } from '../../../services/stats.service';

  @Component({
    selector: 'app-current-account-stats',
    templateUrl: './current-account-stats.component.html',
    styleUrls: ['./current-account-stats.component.css'],
    standalone: false
  })
  export class CurrentAccountStatsComponent implements OnInit {
    totalAccounts = 0;
    totalBalance = 0;
    averageBalance = 0;
    newAccountsThisMonth = 0;

    filter: 'quarter' | 'halfYear' = 'halfYear';
    limit: number = 6;
    availableLimits: number[] = [];

    fullMonthlyLabels: string[] = [];
    fullMonthlyData: number[] = [];

    accountsChartData: ChartConfiguration<'bar'>['data'] = {
      labels: [],
      datasets: [{
        label: 'Opened Accounts',
        data: [],
        backgroundColor: '#0d6efd',
        borderRadius: 6,
        barThickness: 30
      }]
    };

    accountsChartOptions: ChartOptions<'bar'> = {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true
        }
      },
      plugins: {
        legend: { display: false }
      }
    };

    balancePieData: ChartConfiguration<'doughnut'>['data'] = {
      labels: ['< 1k DA', '1k–10k DA', '10k–50k DA', '> 50k DA'],
      datasets: [{
        data: [120, 300, 200, 80],
        backgroundColor: ['#6c757d', '#0d6efd', '#ffc107', '#198754']
      }]
    };

    balancePieOptions: ChartOptions<'doughnut'> = {
      responsive: true,
      plugins: {
        legend: {
          position: 'bottom'
        }
      }
    };
    private loadAccountSummary(): void {
      this.statsService.getCurrentAccountsSummaryStats().subscribe(summary => {
        this.totalAccounts = summary.totalAccounts;
        this.totalBalance = summary.totalBalance;
        this.averageBalance = summary.averageBalance;
        this.newAccountsThisMonth = summary.newThisMonth;
      });
    }
    private loadBalanceDistribution(): void {
      this.statsService.getBalanceDistribution().subscribe(data => {
        this.balancePieData = {
          labels: ['< 1k DA', '1k–10k DA', '10k–50k DA', '> 50k DA'],
          datasets: [{
            data: [
              data.lessThan1k,
              data.between1kAnd10k,
              data.between10kAnd50k,
              data.greaterThan50k
            ],
            backgroundColor: ['#6c757d', '#0d6efd', '#ffc107', '#198754']
          }]
        };
      });
    }



    constructor(private statsService: StatsService) {}
    ngOnInit(): void {
      this.updateAvailableLimits();
      this.loadAccountsChartData();
      this.loadGlobalStats();
      this.loadBalanceDistribution();
      this.loadAccountSummary();
    }

    private loadAccountsChartData(): void {
      this.statsService.getMonthlyAccountStats(this.filter, this.limit).subscribe(data => {
        const now = new Date();
        const labels: string[] = [];
        const values: number[] = [];

        // Get the last `limit` months as expected labels (e.g. ['Jan', 'Feb', ...])
        for (let i = this.limit - 1; i >= 0; i--) {
          const date = new Date(now.getFullYear(), now.getMonth() - i, 1);
          const label = date.toLocaleString('en-US', { month: 'short' }); // e.g. "Jan"
          labels.push(label);
          const value = data[label] ?? 0; // Fill missing months with 0
          values.push(value);
        }

        this.accountsChartData = {
          labels,
          datasets: [{
            label: 'Opened Accounts',
            data: values,
            backgroundColor: '#0d6efd',
            borderRadius: 6,
            barThickness: 30
          }]
        };

        this.newAccountsThisMonth = values.at(-1) ?? 0;
      });
    }


    private loadGlobalStats(): void {
      this.statsService.getGeneralAccountStats().subscribe(stats => {
        this.totalAccounts = stats.totalAccountCurrent;
        this.averageBalance = 0; // unless you fetch it from somewhere
        this.totalBalance = 0;   // optional
      });
    }


    onFilterChange(): void {
      this.updateAvailableLimits();

      this.limit = Number(this.limit);
      if (!this.availableLimits.includes(this.limit)) {
        this.limit = this.availableLimits[this.availableLimits.length - 1];
      }

      this.loadAccountsChartData();
    }

    private updateAvailableLimits(): void {
      const max = this.filter === 'quarter' ? 3 : 6;
      this.availableLimits = [2, 3, 4, 5, 6].filter(n => n <= max);
    }


  }

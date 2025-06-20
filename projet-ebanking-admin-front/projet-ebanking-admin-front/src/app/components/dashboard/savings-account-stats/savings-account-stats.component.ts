import { Component, OnInit } from '@angular/core';
import {
  ChartConfiguration,
  ChartType,
  ChartOptions
} from 'chart.js';
import {StatsService} from '../../../services/stats.service';

@Component({
  selector: 'app-savings-account-stats',
  templateUrl: './savings-account-stats.component.html',
  styleUrls: ['./savings-account-stats.component.css'],
  standalone: false
})
export class SavingsAccountStatsComponent implements OnInit {
  filter: 'month' | 'year' = 'month';
  lastNOptions = [2, 3, 4, 5, 6];
  lastN: number = 6;

  barChartType: 'bar' = 'bar';
  averageDeposit: number = 1520.75;
  growthRate: number = 4.3;
  newAccounts: number = 57;

  barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [
      {
        label: 'Savings Accounts',
        data: [],
        backgroundColor: '#198754',
        borderRadius: 8,
        barThickness: 30
      }
    ]
  };

  barChartOptions: ChartOptions<'bar'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: {
        backgroundColor: '#343a40',
        titleColor: '#fff',
        bodyColor: '#f8f9fa',
        borderColor: '#adb5bd',
        borderWidth: 1,
        padding: 10,
        cornerRadius: 6
      }
    },
    scales: {
      x: {
        grid: { display: false },
        ticks: { color: '#6c757d', font: { size: 12 } }
      },
      y: {
        beginAtZero: true,
        grid: { color: '#f1f3f5' },
        ticks: { color: '#6c757d', font: { size: 12 } }
      }
    }
  };

  constructor(private statsService: StatsService) {}

  ngOnInit(): void {
    this.loadChartData();
    this.loadSummaryStats();

  }

  onFilterChange(): void {
    this.loadChartData();
  }

  loadChartData(): void {
    this.statsService.getSavingsAccountStats(this.filter, this.lastN)
      .subscribe((stats: { [label: string]: number }) => {
        const fullLabels: string[] = [];

        if (this.filter === 'month') {
          const allMonths = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
            'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
          const currentMonthIndex = new Date().getMonth();

          for (let i = this.lastN - 1; i >= 0; i--) {
            const monthIndex = (currentMonthIndex - i + 12) % 12;
            fullLabels.push(allMonths[monthIndex]);
          }
        } else {
          const currentYear = new Date().getFullYear();
          for (let i = this.lastN - 1; i >= 0; i--) {
            fullLabels.push((currentYear - i).toString());
          }
        }

        const fullData = fullLabels.map(label => stats[label] ?? 0);

        this.barChartData = {
          labels: fullLabels,
          datasets: [
            {
              label: 'Savings Accounts',
              data: fullData, // fullData is already number[]
              backgroundColor: '#198754',
              borderRadius: 8,
              barThickness: 30
            }
          ]
        };

      });
  }
  loadSummaryStats(): void {
    this.statsService.getSavingsAccountSummaryStats().subscribe(summary => {
      this.averageDeposit = summary.averageDeposit;
      this.growthRate = summary.growthRate;
      this.newAccounts = summary.newAccounts;
    });
  }

}

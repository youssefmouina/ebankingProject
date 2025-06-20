import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import {
  ChartConfiguration,
  ChartType,
  ChartOptions
} from 'chart.js';
import {StatsService} from '../../../services/stats.service';

@Component({
  selector: 'app-client-stats',
  templateUrl: './client-stats.component.html',
  styleUrls: ['./client-stats.component.css'],
  standalone: false
})
export class ClientStatsComponent implements OnInit {
  filter: 'month' | 'year' = 'month';
  limit: number = 6;
  availableLimits: number[] = [];

  barChartType: ChartType = 'bar';

  fullMonthlyLabels = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

  fullYearlyLabels: string[] = [];

  barChartData: ChartConfiguration['data'] = {
    labels: [],
    datasets: []
  };

  barChartOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    animation: {
      duration: 800,
      easing: 'easeOutQuart'
    },
    plugins: {
      legend: { display: false },
      tooltip: {
        backgroundColor: '#212529',
        titleColor: '#ffffff',
        bodyColor: '#e9ecef',
        borderColor: '#dee2e6',
        borderWidth: 1,
        padding: 12,
        cornerRadius: 8
      }
    },
    scales: {
      x: {
        grid: { drawOnChartArea: false },
        ticks: { color: '#6c757d', font: { size: 12 } }
      },
      y: {
        beginAtZero: true,
        grid: { color: '#f1f3f5' },
        ticks: { color: '#6c757d', font: { size: 12 } }
      }
    }
  };

  constructor(private statsService: StatsService, private translate: TranslateService) {}

  ngOnInit(): void {
    this.updateAvailableLimits();
    this.loadChartData();
  }

  onFilterChange(): void {
    this.updateAvailableLimits();
    if (!this.availableLimits.includes(this.limit)) {
      this.limit = this.availableLimits[this.availableLimits.length - 1];
    }
    this.loadChartData();
  }

  private updateAvailableLimits(): void {
    const max = this.filter === 'month' ? 12 : (this.fullYearlyLabels.length || 6);
    this.availableLimits = [2, 3, 4, 5, 6].filter(n => n <= max);
  }

  private loadChartData(): void {
    if (this.filter === 'month') {
      this.statsService.getMonthlyClientStats().subscribe(data => {
        const rawKeys = Object.keys(data).sort(); // e.g. ['2025-05', '2025-06']
        const countsMap = new Map<string, number>();

        rawKeys.forEach(key => countsMap.set(key, data[key]));

        const latestKey = rawKeys.length ? rawKeys[rawKeys.length - 1] : this.getCurrentYearMonth();
        const [latestYear, latestMonthStr] = latestKey.split('-');
        let latestMonth = parseInt(latestMonthStr, 10);
        let year = parseInt(latestYear, 10);

        const labels: string[] = [];
        const counts: number[] = [];

        for (let i = 5; i >= 0; i--) {
          let month = latestMonth - i;
          let y = year;
          if (month <= 0) {
            month += 12;
            y -= 1;
          }
          const key = `${y.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}`;
          labels.push(this.fullMonthlyLabels[month - 1]);
          counts.push(countsMap.get(key) ?? 0);
        }

        this.setChartData(labels, counts);
      });
    } else {
      this.statsService.getYearlyClientStats().subscribe((data: any) => {
        const rawYears = Object.keys(data).sort();
        const countsMap = new Map<string, number>();

        rawYears.forEach(year => countsMap.set(year, data[year]));

        const latestYear = rawYears.length ? parseInt(rawYears[rawYears.length - 1], 10) : new Date().getFullYear();

        const labels: string[] = [];
        const counts: number[] = [];

        for (let i = 5; i >= 0; i--) {
          const y = latestYear - i;
          labels.push(y.toString());
          counts.push(countsMap.get(y.toString()) ?? 0);
        }

        this.fullYearlyLabels = labels;
        this.setChartData(labels, counts);
      });
    }
  }

  private getCurrentYearMonth(): string {
    const now = new Date();
    return `${now.getFullYear()}-${(now.getMonth() + 1).toString().padStart(2, '0')}`;
  }


  private setChartData(labels: string[], data: number[]): void {
    const sliceStart = Math.max(labels.length - this.limit, 0);

    this.barChartData = {
      labels: labels.slice(sliceStart),
      datasets: [
        {
          label: 'Inscriptions',
          data: data.slice(sliceStart),
          backgroundColor: '#0d6efd',
          borderRadius: 6,
          barThickness: 32
        }
      ]
    };
  }
  getLastPeriodsLabel(count: number): string {
    const periodKey = this.filter === 'month' ? 'clientRegistrations.month' : 'clientRegistrations.year';
    // Use pluralized form of period if your i18n supports it
    const period = this.translate.instant(periodKey + (count > 1 ? '_plural' : ''));

    return this.translate.instant('clientRegistrations.lastPeriods', { count, period });
  }


}

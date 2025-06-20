// src/app/services/stats.service.ts
import {HttpClient, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import {DashboardStatsResDTO} from '../model/DashStatsResDTO';
import {
  CurrentAccountStatsComponent
} from '../components/dashboard/current-account-stats/current-account-stats.component';
import {CurrentAccountsSummaryStats} from '../model/CurrentAccountsSummaryStats';

@Injectable({
  providedIn: 'root'
})
export class StatsService {

  private apiUrl = `http://localhost:8080/api/stats`;

  constructor(private http: HttpClient) {}

  getDashboardStats(): Observable<DashboardStatsResDTO> {
    return this.http.get<DashboardStatsResDTO>(`${this.apiUrl}`, { withCredentials: true });
  }
  getMonthlyClientStats() {
    return this.http.get<Record<string, number>>(`${this.apiUrl}/clients/monthly`, { withCredentials: true });
  }

  getYearlyClientStats() {
    return this.http.get<Record<number, number>>(`${this.apiUrl}/clients/yearly`, { withCredentials: true });
  }



  getMonthlyAccountStats(filter: 'quarter' | 'halfYear', limit: number): Observable<{ [label: string]: number }> {
    const params = new HttpParams()
      .set('filter', filter)
      .set('limit', limit.toString());

    return this.http.get<{ [label: string]: number }>(
      `${this.apiUrl}/ccourant/monthly`,
      { params, withCredentials: true }
    );
  }

  getYearlyAccountStats(filter: 'halfYear', limit: number): Observable<{ [label: string]: number }> {
    const params = new HttpParams()
      .set('filter', filter)
      .set('limit', limit.toString());

    return this.http.get<{ [label: string]: number }>(
      `${this.apiUrl}/ccourant/yearly`,
      { params, withCredentials: true }
    );
  }
  getGeneralAccountStats(): Observable<{
    totalClients: number;
    totalAccountCurrent: number;
    totalAccountSavings: number;
  }> {
    return this.http.get<any>(
      `${this.apiUrl}/ccourant/stats`,
      { withCredentials: true }
    );
  }
  getBalanceDistribution(): Observable<{
    lessThan1k: number,
    between1kAnd10k: number,
    between10kAnd50k: number,
    greaterThan50k: number
  }> {
    return this.http.get<any>(`${this.apiUrl}/balance-distribution`, { withCredentials: true });
  }
  getCurrentAccountsSummaryStats(): Observable<CurrentAccountsSummaryStats> {
    return this.http.get<CurrentAccountsSummaryStats>(`${this.apiUrl}/summary`,{ withCredentials: true });
  }
  getSavingsAccountStats(filter: 'month' | 'year', lastN: number): Observable<{ [label: string]: number }> {
    const params = new HttpParams()
      .set('filter', filter)
      .set('lastN', lastN.toString());

    return this.http.get<{ [label: string]: number }>(
      `${this.apiUrl}/cepargne`,
      { params, withCredentials: true }
    );
  }
  getSavingsAccountSummaryStats(): Observable<{
    averageDeposit: number;
    growthRate: number;
    newAccounts: number;
  }> {
    return this.http.get<{
      averageDeposit: number;
      growthRate: number;
      newAccounts: number;
    }>(
      `${this.apiUrl}/cepargne/summary`,
      { withCredentials: true }
    );
  }


}

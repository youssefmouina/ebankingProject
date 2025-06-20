export class DashboardStatsResDTO {
  private _totalClients: number=0;
  private _totalAccountCurrent: number=0;
  private _totalAccountSavings: number=0;

  get totalClients(): number {
    return this._totalClients;
  }

  set totalClients(value: number) {
    this._totalClients = value;
  }

  get totalAccountCurrent(): number {
    return this._totalAccountCurrent;
  }

  set totalAccountCurrent(value: number) {
    this._totalAccountCurrent = value;
  }

  get totalAccountSavings(): number {
    return this._totalAccountSavings;
  }

  set totalAccountSavings(value: number) {
    this._totalAccountSavings = value;
  }


}

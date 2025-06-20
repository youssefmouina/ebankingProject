export class CurrentAccountsSummaryStats {
  private _totalAccounts?: number;
  private _totalBalance?: number;
  private _averageBalance?: number;
  private _newThisMonth?: number;

  constructor(totalAccounts: number, totalBalance: number, averageBalance: number, newThisMonth: number) {
    this._totalAccounts = totalAccounts;
    this._totalBalance = totalBalance;
    this._averageBalance = averageBalance;
    this._newThisMonth = newThisMonth;
  }

  get totalAccounts(): number {
    return <number>this._totalAccounts;
  }

  set totalAccounts(value: number) {
    this._totalAccounts = value;
  }

  get totalBalance(): number {
    return <number>this._totalBalance;
  }

  set totalBalance(value: number) {
    this._totalBalance = value;
  }

  get averageBalance(): number {
    return <number>this._averageBalance;
  }

  set averageBalance(value: number) {
    this._averageBalance = value;
  }

  get newThisMonth(): number {
    return <number>this._newThisMonth;
  }

  set newThisMonth(value: number) {
    this._newThisMonth = value;
  }

}

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { TransfersComponent } from './components/transfers/transfers.component';
import { DepositComponent } from './components/deposit/deposit.component';
import { ApplayoutComponent } from './applayout/applayout.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import { ClientStatsComponent } from './components/dashboard/client-stats/client-stats.component';
import { CurrentAccountStatsComponent } from './components/dashboard/current-account-stats/current-account-stats.component';
import { SavingsAccountStatsComponent } from './components/dashboard/savings-account-stats/savings-account-stats.component';
import {NgChartsModule} from 'ng2-charts';
import { ClientsComponent } from './components/clients/clients.component';
import { TransferModalComponent } from './components/transfers/transfer-modal/transfer-modal.component';
import { RecuModalComponent } from './components/transfers/recu-modal/recu-modal.component';
import { EmployeesComponent } from './components/employees/employees.component';
import { AddEmployeeModalComponent } from './components/employees/add-employee-modal/add-employee-modal.component';
import { UnauthorizedComponent } from './components/unauthorized/unauthorized.component';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    ClientsComponent,
    TransfersComponent,
    DepositComponent,
    ApplayoutComponent,
    ClientStatsComponent,
    CurrentAccountStatsComponent,
    SavingsAccountStatsComponent,
    ClientsComponent,
    TransferModalComponent,
    RecuModalComponent,
    EmployeesComponent,
    AddEmployeeModalComponent,
    UnauthorizedComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      }
    }),
    NgChartsModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

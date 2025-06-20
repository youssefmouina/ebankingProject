import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LayoutComponent } from './layout/layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AccountsComponent } from './pages/accounts/accounts.component';
import { TransfersComponent } from './pages/transfers/transfers.component';
import {TradingComponent} from './pages/trading/trading.component';
import {InvoiceModalComponent} from './pages/modals/invoice-modal/invoice-modal.component';
import {InvoicesComponent} from './pages/payment/invoices/invoices.component';
import {RechargeComponent} from './pages/payment/recharge/recharge.component';
import {PaymentsComponent} from './pages/payment/payments.component';
import { LoginComponent } from './pages/login/login.component';
import { AuthGuard } from './auth.guard';
import { LoginGuard } from './login.guard';
import { RegisterComponent } from './pages/register/register.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { SettingsComponent } from './pages/settings/settings.component';

const routes: Routes = [
  {path: 'register', component: RegisterComponent, canActivate: [LoginGuard]},
  { path: 'forgot-password', component: ForgotPasswordComponent, canActivate: [LoginGuard]},
  {path: 'login', component: LoginComponent, canActivate: [LoginGuard]},
  {
    path: '',
    component: LayoutComponent,
    canActivate: [AuthGuard], // <--- Protect the entire layout group
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'accounts', component: AccountsComponent },
      { path: 'transfers', component: TransfersComponent },
      { path: 'crypto', component: TradingComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'settings', component: SettingsComponent },

      { path: 'payments', component: PaymentsComponent,
        children:[
          {path:'recharges', component:RechargeComponent},
          {path:'invoices', component:InvoicesComponent}
        ]
      },
    ]
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}

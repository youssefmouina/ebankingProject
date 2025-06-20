// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { TransfersComponent } from './components/transfers/transfers.component';
import { DepositComponent } from './components/deposit/deposit.component';

import { ApplayoutComponent } from './applayout/applayout.component';
import { AuthGuard } from './auth/auth.guard';
import {ClientsComponent} from './components/clients/clients.component';
import {EmployeesComponent} from './components/employees/employees.component';
import {UnauthorizedComponent} from './components/unauthorized/unauthorized.component';

const routes: Routes = [
  // Login route (unprotected)
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: ApplayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'unauthorized', component: UnauthorizedComponent },
      { path: 'employees', component: EmployeesComponent },
      { path: 'clients', component: ClientsComponent },
      { path: 'transfers', component: TransfersComponent },
      { path: 'deposits', component: DepositComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

  // Fallback route
  { path: '**', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}

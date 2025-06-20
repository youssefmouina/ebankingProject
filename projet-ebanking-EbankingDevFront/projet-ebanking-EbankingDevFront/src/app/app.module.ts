import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AccountsComponent } from './pages/accounts/accounts.component';
import { TransfersComponent } from './pages/transfers/transfers.component';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule, provideHttpClient} from '@angular/common/http';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {CommonModule} from '@angular/common';
import {FormsModule, NgModel, ReactiveFormsModule} from '@angular/forms';
import {CryptoDashboardComponent} from './pages/trading/crypto-dashboard/crypto-dashboard.component';
import {TradingViewWidgetComponent} from './pages/trading/tradingbtc/tradingview-widget.component';
import {TradingComponent} from './pages/trading/trading.component';
import {TradingethComponent} from './pages/trading/tradingeth/tradingeth.component';
import {InvoicesComponent} from './pages/payment/invoices/invoices.component';
import {RechargeComponent} from './pages/payment/recharge/recharge.component';
import {PaymentsComponent} from './pages/payment/payments.component';
import {InvoiceModalComponent} from './pages/modals/invoice-modal/invoice-modal.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { EcodeModalComponent } from './pages/modals/ecode-modal/ecode-modal.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { VerificationCodeComponent } from './pages/verification-code/verification-code.component';
// import {ChatbotComponent} from './pages/payment/chatbot/chatbot.component';
import {ChatbotComponent} from './pages/chatbot/chatbot.component';
// import { AuthInterceptor } from './Service/AuthInterceptor';
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
@NgModule({
  declarations: [
    AppComponent,
    LayoutComponent,
    DashboardComponent,
    TransfersComponent,
    CryptoDashboardComponent,
    TradingViewWidgetComponent,
    TradingComponent,
    InvoicesComponent,
    RechargeComponent,
    PaymentsComponent,
    InvoiceModalComponent,
    LoginComponent,
    RegisterComponent,
    ForgotPasswordComponent,
    VerificationCodeComponent,
    SettingsComponent,
    EcodeModalComponent,
    ChatbotComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    RouterModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      }
    }),
    AccountsComponent,
    ReactiveFormsModule,
    TradingethComponent,
    FormsModule,

  ],
  providers: [
    // { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
function provideFirebaseApp(arg0: () => any): any[] | import("@angular/core").Type<any> | import("@angular/core").ModuleWithProviders<{}> {
  throw new Error('Function not implemented.');
}


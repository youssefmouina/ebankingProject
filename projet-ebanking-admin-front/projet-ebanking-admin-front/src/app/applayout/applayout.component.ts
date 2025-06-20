import { Component } from '@angular/core';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import { AuthService } from '../auth/auth.service';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-layout',
  templateUrl: './applayout.component.html',
  styleUrls: ['./applayout.component.css'],
  standalone: false
})
export class ApplayoutComponent {
  constructor(private auth: AuthService, private router: Router,private translateService:TranslateService) {}

  logout() {
    this.auth.logout();
  }
  switchLang(event: Event) {
    const select = event.target as HTMLSelectElement;
    const lang = select.value;
    console.log('Switching language to:', lang);
    localStorage.setItem('lang', lang);
    this.translateService.setDefaultLang(lang);
    this.translateService.use(lang);
  }

  protected readonly localStorage = localStorage;
}

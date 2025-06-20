import { Component } from '@angular/core';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'admin-front';
  constructor(private translateService: TranslateService) {
    const savedLang = localStorage.getItem('lang');
    const browserLang = navigator.language.split('-')[0]; // Use 'en', 'fr', etc.

    const lang = savedLang || (browserLang === 'fr' ? 'fr' : 'en');

    // Save chosen language if not already saved
    if (!savedLang) {
      localStorage.setItem('lang', lang);
    }

    this.translateService.setDefaultLang('en');
    this.translateService.use(lang);
  }
}

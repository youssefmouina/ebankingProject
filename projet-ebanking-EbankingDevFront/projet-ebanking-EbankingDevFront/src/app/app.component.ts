import { Component, OnInit } from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
// import { AuthInitService } from './Service/authInitService';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'ebankingClientFrontEnd';
  constructor(private translateService: TranslateService/*, private authInitService: AuthInitService*/) {
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
  ngOnInit() {
    // this.authInitService.init();
  }
}

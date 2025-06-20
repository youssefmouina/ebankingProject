import {Component, OnInit} from '@angular/core';
import { Location } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from '../Service/Auth.service';
import {ClientService} from '../Service/client.service';

@Component({
  selector: 'app-layout',
  standalone: false,
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent implements OnInit{

  constructor(private location: Location, private translateService: TranslateService, private authService: AuthService, private clientService : ClientService) {}
  username: string = '';
  ngOnInit() {
    const userId = Number(localStorage.getItem('userid'));
    this.clientService.getClient(userId).subscribe({
      next: (res) => {
        this.username = res.username;
      },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.authService.logout();
        }
        else{
          console.error('Error fetching client:', err);
        }
      }
    });
  }
  switchLang(event: Event) {
    const select = event.target as HTMLSelectElement;
    const lang = select.value;
    console.log('Switching language to:', lang);
    localStorage.setItem('lang', lang);
    this.translateService.setDefaultLang(lang);
    this.translateService.use(lang);
  }

  logout() {
    this.authService.logout();
  }

  protected readonly localStorage = localStorage;
}

import { Component } from '@angular/core';
import { ClientService } from '../../Service/client.service';
import { ClientResDTO } from '../../model/dto/ClientResDTO';
import {AuthService} from '../../Service/Auth.service';

@Component({
  selector: 'app-settings',
  standalone: false,
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {

  constructor(
      private clientService : ClientService,
      private authService: AuthService
    ){}
  activeTab: 'profile' | 'security' = 'profile';
  showEcodeModal: boolean = false;
  clientId:number=Number(localStorage.getItem('userid'));
  client: ClientResDTO | null = null;

  editBankinoPassword() {
    console.log('Edit Bankino password');
  }

  editECode() {
    this.showEcodeModal = true;
  }

  closeEcodeModal() {
    this.showEcodeModal = false;
  }

  ngOnInit(): void {
    this.clientService.getClient(this.clientId).subscribe({
      next: (result: ClientResDTO) => {
         this.client=result
      },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.authService.logout();
        }
        else{
          console.error("Erreur lors de la récupération du client :", err);
        }
      }
    });
  }


}

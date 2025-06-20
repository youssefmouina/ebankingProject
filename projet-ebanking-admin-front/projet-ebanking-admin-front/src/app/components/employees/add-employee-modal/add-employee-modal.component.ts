import { Component, EventEmitter, Output } from '@angular/core';
import { EmployeesService } from '../../../services/Employee.service';
import { EmployeeDTO } from '../../../model/EmployeeDTO';
import { Role } from '../../../model/Role';
import Swal from 'sweetalert2';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-add-employee-modal',
  templateUrl: './add-employee-modal.component.html',
  styleUrls: ['./add-employee-modal.component.css'],
  standalone: false
})
export class AddEmployeeModalComponent {
  @Output() close = new EventEmitter<void>();

  employee: Partial<EmployeeDTO> = {
    lastName: '',
    firstName: '',
    email: '',
    phone: '',
    role: Role.ADMIN,
    password: ''
  };

  roles = Object.values(Role);
  loading = false;
  errorMessage = '';

  constructor(
    private employeesService: EmployeesService,
    private translate: TranslateService
  ) {}

  submit() {
    if (this.loading) return;

    this.loading = true;
    this.errorMessage = '';

    this.employeesService.saveEmployee(this.employee as EmployeeDTO).subscribe({
      next: () => {
        this.loading = false;
        this.translate.get(['employee.addTitle', 'employee.addSuccess']).subscribe(translations => {
          Swal.fire({
            icon: 'success',
            title: translations['employee.addTitle'],
            text: translations['employee.addSuccess']
          });
        });
        this.close.emit();
      },
      error: (error) => {
        this.loading = false;
        const errorMsg = error?.error?.message || 'Erreur lors de l’ajout.';
        this.errorMessage = errorMsg;
        this.translate.get(['employee.addErrorTitle', 'employee.addErrorMessage']).subscribe(translations => {
          Swal.fire({
            icon: 'error',
            title: translations['employee.addErrorTitle'] || 'Erreur',
            text:  translations['employee.addErrorMessage'] || 'Erreur inconnue'
          });
        });
      }
    });
  }

  closeModal() {
    this.close.emit();
  }
}

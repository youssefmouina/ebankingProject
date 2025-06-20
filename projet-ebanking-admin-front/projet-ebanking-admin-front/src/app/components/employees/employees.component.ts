import { Component, OnInit } from '@angular/core';
import { EmployeeResDTO } from '../../model/EmployeeResDTO';
import { EmployeesService } from '../../services/Employee.service';
import { Role } from '../../model/Role';
import {Router} from '@angular/router';

@Component({
  selector: 'app-employees',
  templateUrl: './employees.component.html',
  styleUrls: ['./employees.component.css'],
  standalone: false
})
export class EmployeesComponent implements OnInit {
  employees: EmployeeResDTO[] = [];
  currentPage = 0;
  pageSize = 10;
  hasMore = false;

  showAddModal = false;

  constructor(private employeesService: EmployeesService,private router: Router) {}

  ngOnInit(): void {
    this.fetchEmployees();
  }

  fetchEmployees() {
    this.employeesService.getEmployees(this.currentPage, this.pageSize).subscribe({
      next: (page) => {
        this.employees = page.content;
        this.hasMore = !page.last;
      },
      error: (err) => {
        if (err.status === 401) {
          this.router.navigate(['/unauthorized']);
        }
      }
    });
  }

  nextPage() {
    if (this.hasMore) {
      this.currentPage++;
      this.fetchEmployees();
    }
  }

  previousPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.fetchEmployees();
    }
  }

  openAddModal() {
    this.showAddModal = true;
  }

  closeAddModal() {
    this.showAddModal = false;
    this.fetchEmployees();
  }

  protected readonly Role = Role;
}

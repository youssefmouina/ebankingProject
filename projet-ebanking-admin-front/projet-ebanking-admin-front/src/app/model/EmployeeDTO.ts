import {Role} from './Role';

export class EmployeeDTO {
  firstName?: string;
  lastName?: string;
  username?: string;
  password?: string;
  email?: string;
  phone?: string;
  role?: Role;
}

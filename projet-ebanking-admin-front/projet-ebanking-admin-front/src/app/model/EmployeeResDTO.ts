import {Role} from './Role';

export class EmployeeResDTO {
  id?: number;
  firstName?: string;
  lastName?: string;
  username?: string;
  email?: string;
  phone?: string;
  createdAt?: string;
  updatedAt?: string;
  role?: Role;
}

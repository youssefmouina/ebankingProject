import { CompteResDTO } from '../dto/CompteResDTO';
import { InvoiceResDTO } from '../dto/InvoiceResDTO'; // Assume you already have this

export class ClientResDTO {
  id!: number;
  firstName!: string;
  lastName!: string;
  username!: string;
  email!: string;
  phone!: string;
  job!: string;
  comptes!: CompteResDTO[];
  invoices!: InvoiceResDTO[];
}

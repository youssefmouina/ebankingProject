import { ClientResDTO } from '../dto/ClientResDTO';
import { VirementResDTO } from '../dto/VirementResDTO';
import { RechargeResDTO } from '../dto/RechargeResDTO';
import { StatusCompte } from '../dto/StatusCompte'; // or .model if it's a class

export class CEpargneResDTO {
  id!: number;
  client!: ClientResDTO;
  rib!: string;
  solde!: number;
  status!: StatusCompte;
  createdAt!: Date;
  updatedAt!: Date;
  tauxInterets!: number;
  dateInterets!:Date;
  accountType!: string;
}

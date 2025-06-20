import { ClientResDTO } from '../dto/ClientResDTO';
import { VirementResDTO } from '../dto/VirementResDTO';
import { RechargeResDTO } from '../dto/RechargeResDTO';
import { StatusCompte } from '../dto/StatusCompte'; // or .model if it's a class

export class CCourantResDTO {
  id!: number;
  client!: ClientResDTO;
  rib!: string;
  solde!: number;
  status!: StatusCompte;
  createdAt!: Date;
  updatedAt!: Date;
  autorisePaiementEnLigne!: boolean;
  virementsEmis!: VirementResDTO[];
  virementsRecu!: VirementResDTO[];
  recharges!: RechargeResDTO[];
  accountType!: string;
}

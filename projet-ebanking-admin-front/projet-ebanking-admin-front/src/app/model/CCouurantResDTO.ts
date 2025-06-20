import { ClientResDTO } from './ClientResDTO';
import { VirementResDTO } from './VirementResDTO';
import { RechargeResDTO } from './RechargeResDTO';
import { StatusCompte } from './StatusCompte'; // or .model if it's a class

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

import { StatusCompte } from './StatusCompte';
import { VirementResDTO } from '../dto/VirementResDTO';
import { RechargeResDTO } from '../dto/RechargeResDTO';

export class CompteResDTO {
  id!: number;
  rib!: string;
  solde!: number;
  status!: StatusCompte;
  createdAt!: Date;
  updatedAt!: Date;
  tauxInterets!: number;
  dateInterets!: Date;
  autorisePaiementEnLigne!: boolean;
  virementsEmis!: VirementResDTO[];
  virementsRecu!: VirementResDTO[];
  recharges!: RechargeResDTO[];
  accountType!: string;
}

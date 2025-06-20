import { CompteResDTO } from './CompteResDTO';
import { TypeTransaction } from './TypeTransaction';

export class VirementResDTO {
  id!: number;
  compteEmetteur!: CompteResDTO;
  compteRecepteur!: CompteResDTO;
  montant!: number;
  createdAt!: Date;
  type!: TypeTransaction;
  recu!: string;
}

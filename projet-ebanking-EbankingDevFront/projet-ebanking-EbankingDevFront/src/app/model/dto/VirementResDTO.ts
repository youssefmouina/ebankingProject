import { CompteResDTO } from '../dto/CompteResDTO';
import { TypeTransaction } from '../dto/TypeTransaction';

export class VirementResDTO {
  id!: number;
  compteEmetteur!: CompteResDTO;
  compteRecepteur!: CompteResDTO;
  montant!: number;
  createdAt!: Date;
  type!: TypeTransaction;
  recu!: string;
}

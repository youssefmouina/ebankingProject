import { TypeTransaction } from '../dto/TypeTransaction';

export class VirementDTO {
  compteEmetteur!: string;
  compteRecepteur!: string;
  montant!: number;
  type: TypeTransaction = TypeTransaction.INSTANTANEE;
}

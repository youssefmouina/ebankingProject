import { ClientResDTO } from './ClientResDTO';

import { StatusCompte } from './StatusCompte';

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

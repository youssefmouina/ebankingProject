import { CCourantResDTO } from './CCouurantResDTO';

export class RechargeResDTO {
  id!: number;
  operateur!: string;
  phoneNumber!: string;
  montant!: number;
  createdAt!: Date;
  compte!: CCourantResDTO;
}

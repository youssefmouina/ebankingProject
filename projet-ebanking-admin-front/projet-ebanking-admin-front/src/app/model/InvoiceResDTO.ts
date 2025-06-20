export class InvoiceResDTO {
    id!: number;
    provider!: string;
    amount!: number;
    dueDate!: Date;
    referenceNumber!: string;

    paid!: boolean;
    paidDate!: Date | null;

    userId!: number;
}

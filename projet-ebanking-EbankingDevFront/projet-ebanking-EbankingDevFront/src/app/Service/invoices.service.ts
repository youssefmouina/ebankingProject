import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InvoiceResDTO } from '../model/dto/InvoiceResDTO';
import { InvoicePayDTO } from '../model/dto/InvoicePayDTO';

@Injectable({
  providedIn: 'root'
})
export class InvoicesService {
  private invoiceUrl = 'http://localhost:8080/api/invoices';

  constructor(private http: HttpClient) {}

  getInvoice(clientId: number, provider: string, reference: string): Observable<InvoiceResDTO> {
    const url = `${this.invoiceUrl}/invoice/${clientId}/${provider}/${reference}`;
    return this.http.get<InvoiceResDTO>(url, { withCredentials: true }); //this.http.get<RetourDuBackend>(url);
  }

  payInvoice(invoicePayDTO:InvoicePayDTO): Observable<InvoiceResDTO> {
    const url = `${this.invoiceUrl}/invoice/pay`;
    return this.http.put<InvoiceResDTO>(url, invoicePayDTO, { withCredentials: true });
  }
  getInvoicesByCompteId(compteId: number,  page: number, size: number): Observable<any> {
    let offset = page - 1;
    const url = `${this.invoiceUrl}/${compteId}`;
    const params = new HttpParams().set('size', size.toString()).set('offset', offset.toString());;
    return this.http.get<InvoiceResDTO[]>(url, {
      params,
      withCredentials: true
    });
}
}

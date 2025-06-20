import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Page} from '../model/Page';
import {VirementResDTO} from '../model/VirementResDTO';
import {VirementDTO} from '../model/VirementDTO';


@Injectable({
  providedIn: 'root'
})
export class TransfersService {

  private apiUrl = `http://localhost:8080/api/virements`;

  constructor(private http: HttpClient) {}

  getVirements(offset: number, size: number): Observable<Page<VirementResDTO>> {
    return this.http.get<Page<VirementResDTO>>(`${this.apiUrl}`, {
      params: new HttpParams()
        .set('offset', offset.toString())
        .set('size', size.toString()),
      withCredentials: true
    });
  }
  getReceiptPdf(virementId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${virementId}/recu`, {
      responseType: 'blob',
      withCredentials: true
    });
  }

  // New: Execute a virement (transfer) with request payload
  executeVirement(virementRequest: VirementDTO): Observable<VirementResDTO> {
    return this.http.post<VirementResDTO>(`${this.apiUrl}/virement`, virementRequest, {
      withCredentials: true
    });
  }



}

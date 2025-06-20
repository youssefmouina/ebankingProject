import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import {VirementResDTO} from '../model/dto/VirementResDTO';
import {VirementDTO} from '../model/dto/VirementDTO';

@Injectable({
  providedIn: 'root'
})
export class VirementService {
  private readonly apiUrl = 'http://localhost:8080/api/virements';

  constructor(private http: HttpClient) {

  }
  getVirementsByCompteId(id: Number, page: number, size: number): Observable<any> {
    let offset = page - 1;
    const params = new HttpParams()
      .set('size', size.toString())
      .set('offset', offset.toString());

    return this.http.get<any>(`${this.apiUrl}/${id}`, { params, withCredentials: true });
  }
  getReceiptByVirementId(virementId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${virementId}/recu`, {
      responseType: 'blob', withCredentials: true
    });
  }
  executeVirement(request: VirementDTO): Observable<{ message: string, data: VirementResDTO }> {
    return this.http.post<{ message: string, data: VirementResDTO }>(this.apiUrl+"/virement", request, { withCredentials: true });
  }
}

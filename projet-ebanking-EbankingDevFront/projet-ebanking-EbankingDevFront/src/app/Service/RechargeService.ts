import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {map, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RechargeService {
  private readonly apiUrl = 'http://localhost:8080/api/recharges';

  constructor(private http: HttpClient) {

  }
  getRechargesByCompteId(id: Number, page: number, size: number): Observable<any> {
    let offset = page - 1;
    const params = new HttpParams()
      .set('size', size.toString())
      .set('offset', offset.toString());
    let observ= this.http.get<any>(`${this.apiUrl}/${id}`, { params, withCredentials: true });
    console.log(observ);
    return observ;

  }

}

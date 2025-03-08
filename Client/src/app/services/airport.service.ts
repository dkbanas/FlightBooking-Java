import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {AirportRequest} from '../models/AirportRequest';
import {AirportResponse} from '../models/AirportResponse';
import {Observable, tap} from 'rxjs';
import {PageableAirportResponse} from '../models/PageableAirportResponse';

@Injectable({
  providedIn: 'root'
})
export class AirportService {
  private apiUrl = 'http://localhost:8080/airport';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth_token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  createAirport(airport: AirportRequest): Observable<AirportResponse> {
    return this.http.post<AirportResponse>(this.apiUrl, airport, { headers: this.getAuthHeaders() });
  }

  uploadImage(file: File, airportCode: string): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(`${this.apiUrl}/${airportCode}/upload-image`, formData, {
      responseType: 'text',
    });
  }

  getAirports(): Observable<AirportResponse[]> {
    return this.http.get<AirportResponse[]>(this.apiUrl);
  }

  getPageableAirports(page: number, size: number, sortBy: string, sortDirection: string,continent: string): Observable<PageableAirportResponse> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection)
      .set('continent', continent);

    return this.http.get<PageableAirportResponse>(`${this.apiUrl}/paginated`, { params });
  }

  searchAirports(query: string): Observable<AirportResponse[]> {
    return this.http.get<AirportResponse[]>(`${this.apiUrl}/search?query=${query}`);
  }

  deleteAirport(code: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${code}`, { headers: this.getAuthHeaders() })
      .pipe(
        tap(() => console.log(`Deleted airport with code: ${code}`))
      );
  }

  updateAirport(code: string, airport: AirportRequest): Observable<AirportResponse> {
    return this.http.put<AirportResponse>(`${this.apiUrl}/${code}`, airport, { headers: this.getAuthHeaders() })
      .pipe(
        tap(response => console.log('Airport updated:', response))
      );
  }

  getAirportByCode(code: string): Observable<AirportResponse> {
    return this.http.get<AirportResponse>(`${this.apiUrl}/${code}`)
      .pipe(
        tap(airport => console.log('Fetched airport:', airport))
      );
  }
}

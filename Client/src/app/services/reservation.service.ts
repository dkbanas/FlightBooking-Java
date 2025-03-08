import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {ReservationResponse} from '../models/ReservationResponse';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private apiUrl = 'http://localhost:8080/reservation';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth_token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  createReservation(reservation: { flightId: number; userId: number; seatNumbers: string[] }) {
    const params = new HttpParams()
      .set('flightId', reservation.flightId.toString())
      .set('userId', reservation.userId.toString())
      .set('seatNumbers', reservation.seatNumbers.join(','));

    return this.http.post<ReservationResponse>(`${this.apiUrl}`, null, {
      headers: this.getAuthHeaders(),
      params: params,
    });
  }

  getReservationsByUser(userId: number) {
    return this.http.get<any[]>(`${this.apiUrl}/user/${userId}`);
  }

  cancelReservation(reservationId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${reservationId}`);
  }
}

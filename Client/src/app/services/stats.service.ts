import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {BusiestAirportResponse} from '../models/BusiestAirportResponse';
import {MostPopularRouteResponse} from '../models/MostPopularRouteResponse';
import {FlightResponse} from '../models/FlightResponse';

@Injectable({
  providedIn: 'root'
})
export class StatsService {
  private baseUrl = 'http://localhost:8080';
  constructor(private http: HttpClient) { }

  getTotalProfit() {
    return this.http.get<number>(`${this.baseUrl}/reservation/total-profit`);
  }

  getReservationsCount() {
    return this.http.get<number>(`${this.baseUrl}/reservation/count`);
  }

  getUserCount() {
    return this.http.get<number>(`${this.baseUrl}/auth/user-count`);
  }

  getAirportCount() {
    return this.http.get<number>(`${this.baseUrl}/airport/airport-count`);
  }

  getFlightCount() {
    return this.http.get<number>(`${this.baseUrl}/flight/flight-count`);
  }

  getBusiestAirport(): Observable<BusiestAirportResponse> {
    return this.http.get<BusiestAirportResponse>(`${this.baseUrl}/airport/busiest`);
  }

  getMostPopularRoute(): Observable<MostPopularRouteResponse> {
    return this.http.get<MostPopularRouteResponse>(`${this.baseUrl}/reservation/most-popular-route`);
  }

  getMostExpensiveFlight(): Observable<FlightResponse>{
    return this.http.get<FlightResponse>(`${this.baseUrl}/flight/most-expensive`);
  }

  getCheapestFlight(): Observable<FlightResponse>{
    return this.http.get<FlightResponse>(`${this.baseUrl}/flight/cheapest`);
  }



}

import { Injectable } from '@angular/core';
import {RegisterRequest} from '../models/RegisterRequest';
import {BehaviorSubject, map, mapTo, Observable, of, switchMap, tap} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {LoginResponse} from '../models/LoginResponse';
import {LoginRequest} from '../models/LoginRequest';
import {AccountDetailsResponse} from '../models/AccountDetailsResponse';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';
  private tokenKey: string = 'auth_token';
  private expirationKey = 'auth_expiration';
  private logoutTimer: any;

  private loggedInSubject = new BehaviorSubject<boolean>(this.hasToken());
  isLoggedIn$ = this.loggedInSubject.asObservable();

  private accountDetailsSubject = new BehaviorSubject<AccountDetailsResponse | null>(null);
  accountDetails$ = this.accountDetailsSubject.asObservable();

  constructor(private http: HttpClient) {
    this.autoLogoutOnInit();
  }

  private hasToken(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  register(request:RegisterRequest): Observable<any>{
    return this.http.post(`${this.apiUrl}/register`,request);
  }

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, request).pipe(
      tap((response) => {
        this.setSession(response.token, response.expiresIn);
        this.getAccountDetails().subscribe();
      }),
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey!);
    this.loggedInSubject.next(false);
  }

   getAccountDetails(): Observable<AccountDetailsResponse> {
    const token = this.getToken();
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<AccountDetailsResponse>(`${this.apiUrl}/account-details`, { headers }).pipe(
      tap(details => {
        this.accountDetailsSubject.next(details);
      })
    );
  }

  private setSession(token: string, expiresIn: number): void {
    const expirationDate = new Date().getTime() + expiresIn * 1000;
    localStorage.setItem(this.tokenKey, token);
    localStorage.setItem(this.expirationKey, expirationDate.toString());

    this.loggedInSubject.next(true);

    this.setLogoutTimer(expiresIn * 1000);
  }

  private setLogoutTimer(duration: number): void {
    if (this.logoutTimer) {
      clearTimeout(this.logoutTimer);
    }
    this.logoutTimer = setTimeout(() => this.logout(), duration);
  }

  private autoLogoutOnInit(): void {
    const expiration = localStorage.getItem(this.expirationKey);
    if (expiration) {
      const expiresIn = +expiration - new Date().getTime();
      if (expiresIn > 0) {
        this.setLogoutTimer(expiresIn);
      } else {
        this.logout();
      }
    }
  }
}

import {Component, ViewChild} from '@angular/core';
import {NgbCollapse, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {LoginModalComponent} from '../../modals/login-modal/login-modal.component';
import {RegisterModalComponent} from '../../modals/register-modal/register-modal.component';
import {AuthService} from '../../../services/auth.service';
import {NgIf} from '@angular/common';
import {AccountDetailsResponse} from '../../../models/AccountDetailsResponse';
import {RouterLink, RouterLinkActive} from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    NgbCollapse,
    LoginModalComponent,
    RegisterModalComponent,
    NgIf,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  isNavbarCollapsed = true;
  isLoggedIn: boolean = false;
  isAdmin: boolean = false;
  accountDetails: AccountDetailsResponse | null = null;

  constructor(private authService: AuthService) {
    this.authService.isLoggedIn$.subscribe(loggedIn => {
      this.isLoggedIn = loggedIn;
      if(loggedIn){
        this.authService.getAccountDetails().subscribe();
      }
    });

    this.authService.accountDetails$.subscribe(details => {
      this.accountDetails = details;
      this.isAdmin = details?.roles.includes('ROLE_ADMIN') || false;
    });
  }


  logout(): void {
    this.authService.logout();
  }
}

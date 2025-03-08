import {Component, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {CurrencyPipe, DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {ReservationService} from '../../../services/reservation.service';
import {AuthService} from '../../../services/auth.service';
import {LoginModalComponent} from '../../modals/login-modal/login-modal.component';

@Component({
  selector: 'app-flight-details-page',
  standalone: true,
  imports: [
    NgClass,
    NgForOf,
    NgIf,
    CurrencyPipe,
    DatePipe,
    LoginModalComponent
  ],
  templateUrl: './flight-details-page.component.html',
  styleUrl: './flight-details-page.component.scss'
})

/**
 * FlightDetailsPageComponent handles the logic for displaying flight details, seat selection, and payment processing.
 * It includes functionality to display the outbound and return flights, handle seat selection, and process the payment after reservations are created.
 */
export class FlightDetailsPageComponent {
  flight: any; // Holds the flight data, including outbound and optional return flight details.
  selectedSeats: number[] = []; // Stores the selected seat numbers for the outbound flight.
  returnSelectedSeats: number[] = []; // Stores the selected seat numbers for the return flight.
  numberOfPassengers!: number; // Number of passengers for the booking (used for seat selection validation).
  isOutboundSeatsOpen = false;
  isReturnSeatsOpen = false;
  isLoggedIn: boolean = false;
  @ViewChild('loginModal') loginModal!: LoginModalComponent;
  /**
   * Constructor for initializing FlightDetailsPageComponent.
   * It fetches the flight data and the number of passengers from the route state.
   * @param router Angular Router used for navigation.
   * @param reservationService Service responsible for handling reservation creation.
   * @param authService Service used to fetch account details for the logged-in user.
   * @param paymentService Service responsible for handling payment-related actions.
   */
  constructor(
    private router: Router,
    private reservationService: ReservationService,
    private authService: AuthService,
  ) {
    // Fetch the flight data and number of passengers from route state (passed from previous page).
    const state = this.router.getCurrentNavigation()?.extras.state;
    this.flight = state?.['flight'];
    this.numberOfPassengers = state?.['numberOfPassengers'] || 1; // Default to 1 passenger if not provided.

    // Prepare the list of occupied seats for outbound and return flights (if any).
    if (this.flight) {
      this.prepareOccupiedSeats();
    }

    this.authService.isLoggedIn$.subscribe((loggedIn) => {
      this.isLoggedIn = loggedIn;
    });
  }

  /**
   * Prepares the list of occupied seats for the outbound and return flights.
   * It uses the available seat list to determine which seats are occupied.
   */
  private prepareOccupiedSeats(): void {
    const totalSeatsEconomy = this.flight.outboundFlight.totalSeatsEconomy;
    const availableSeatsEconomy = this.flight.outboundFlight.availableSeatsEconomyList.map(Number);

    this.flight.outboundFlight.occupiedSeats = Array.from({ length: totalSeatsEconomy }, (_, index) => index + 1)
      .filter(seat => !availableSeatsEconomy.includes(seat));

    if (this.flight.returnFlight) {
      const returnTotalSeats = this.flight.returnFlight.totalSeatsEconomy;
      const returnAvailableSeats = this.flight.returnFlight.availableSeatsEconomyList.map(Number);

      this.flight.returnFlight.occupiedSeats = Array.from({ length: returnTotalSeats }, (_, index) => index + 1)
        .filter(seat => !returnAvailableSeats.includes(seat));
    }
  }

  /**
   * Toggles the selection of a seat. If the seat is available and within the limit, it is selected or deselected.
   */
  toggleSeat(seatNumber: number, isReturnFlight: boolean = false): void {
    const seatList = isReturnFlight ? this.returnSelectedSeats : this.selectedSeats;
    const index = seatList.indexOf(seatNumber);

    if (index > -1) {
      // If the seat is already selected, remove it from the list (deselect).
      seatList.splice(index, 1);
    } else {
      // If the seat is not selected, check if the user has exceeded the number of passengers.
      if (seatList.length < this.numberOfPassengers) {
        seatList.push(seatNumber);
      } else {
        const flightType = isReturnFlight ? 'return flight' : 'outbound flight';
        alert(`You can only select ${this.numberOfPassengers} seat(s) for the ${flightType}.`);
      }
    }
  }

  toggleSeats(flightType: 'outbound' | 'return') {
    if (flightType === 'outbound') {
      this.isOutboundSeatsOpen = !this.isOutboundSeatsOpen;
    } else if (flightType === 'return') {
      this.isReturnSeatsOpen = !this.isReturnSeatsOpen;
    }
  }

  getTotalPrice(): number {
    let totalPrice = 0;

    if (this.flight?.outboundFlight) {
      totalPrice += this.flight.outboundFlight.priceEconomy * this.numberOfPassengers;
    }

    if (this.flight?.returnFlight) {
      totalPrice += this.flight.returnFlight.priceEconomy * this.numberOfPassengers;
    }

    return totalPrice;
  }


  toPayment(): void {
    if (
      this.selectedSeats.length < this.numberOfPassengers ||
      (this.flight.returnFlight && this.returnSelectedSeats.length < this.numberOfPassengers)
    ) {
      alert(`Please select ${this.numberOfPassengers} seat(s) for both outbound and return flights.`);
      return;
    }

    if (!this.isLoggedIn) {
      this.loginModal.openModal();
      return;
    }

    /**
     * Handles the navigation to the payment page after the reservation is successfully created.
     * Validates if the user has selected the correct number of seats and then processes the reservation.
     */
    this.authService.getAccountDetails().subscribe({
      next: (accountDetails) => {
        const userId = accountDetails.userId;

        const outboundReservation = {
          flightId: this.flight.outboundFlight.id,
          userId: userId,
          seatNumbers: this.selectedSeats.map(String),
        };

        const returnReservation = this.flight.returnFlight
          ? {
            flightId: this.flight.returnFlight.id,
            userId: userId,
            seatNumbers: this.returnSelectedSeats.map(String),
          }
          : null;

        this.reservationService.createReservation(outboundReservation).subscribe({
          next: (outboundResponse) => {
            console.log('Reservation for outbound flight created:', outboundResponse);

            if (returnReservation) {
              this.reservationService.createReservation(returnReservation).subscribe({
                next: (returnResponse) => {
                  console.log('Reservation for return flight created:', returnResponse);
                  this.router.navigate(['complete-order'], {
                    state: { reservations: [outboundResponse, returnResponse] },
                  });
                },
                error: (err) =>
                  console.error('Error creating return flight reservation:', err),
              });
            } else {
              this.router.navigate(['complete-order'], { state: { reservations: [outboundResponse] } });
            }
          },
          error: (err) => {
            console.error('Error creating outbound flight reservation:', err);
            alert('An error occurred while creating your reservation. Please try again.');
          },
        });
      },
      error: (err) => {
        console.error('Error fetching account details:', err);
        alert('An error occurred while fetching user details. Please log in again.');
      },
    });
  }
}

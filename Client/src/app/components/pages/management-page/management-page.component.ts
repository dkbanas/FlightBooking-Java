import { Component } from '@angular/core';
import {AirportResponse} from '../../../models/AirportResponse';
import {AirportService} from '../../../services/airport.service';
import {FormsModule} from '@angular/forms';
import {CurrencyPipe, DatePipe, NgForOf, NgIf} from '@angular/common';
import {NgbAccordionBody, NgbAccordionHeader, NgbPagination} from '@ng-bootstrap/ng-bootstrap';
import {AddAirportModalComponent} from '../../modals/add-airport-modal/add-airport-modal.component';
import {AddFlightModalComponent} from '../../modals/add-flight-modal/add-flight-modal.component';
import {FlightResponse} from '../../../models/FlightResponse';
import {FlightService} from '../../../services/flight.service';
import {PageableAirportResponse} from '../../../models/PageableAirportResponse';
import {PageableFlightResponse} from '../../../models/PageableFlightResponse';
import {StatsService} from '../../../services/stats.service';
import {BusiestAirportResponse} from '../../../models/BusiestAirportResponse';
import {MostPopularRouteResponse} from '../../../models/MostPopularRouteResponse';

@Component({
  selector: 'app-management-page',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    DatePipe,
    NgbAccordionHeader,
    NgbAccordionBody,
    NgIf,
    AddAirportModalComponent,
    AddFlightModalComponent,
    CurrencyPipe,
    NgbPagination
  ],
  templateUrl: './management-page.component.html',
  styleUrl: './management-page.component.scss'
})
export class ManagementPageComponent {
  /////////////////////////////////////
  airports: AirportResponse[] = [];
  currentPage = 1;
  pageSize = 6;
  totalPages = 0;
  totalElements = 0;
  sort = 'name,asc';
  pageSizeOptions = [6, 12, 24, 48];

  flights: FlightResponse[] = [];
  currentFlightPage = 1;
  flightPageSize = 6;
  totalFlightPages = 0;
  totalFlightElements = 0;
  flightPageSizeOptions = [6, 12, 24, 48];
  flightSort = 'flightNumber,asc';

  //STATS
  totalProfit: number = 0;
  reservationsCount: number = 0;
  userCount: number = 0;
  airportCount: number = 0;
  flightCount: number = 0;
  busiestAirport: BusiestAirportResponse | null = null;
  mostPopularRoute: MostPopularRouteResponse | null = null;
  cheapestFlight: FlightResponse | null = null;
  mostExpensiveFlight: FlightResponse | null = null;
  /////////////////////////////////////
  constructor(private airportService: AirportService,private flightService:FlightService,private statsService: StatsService) {}

  ngOnInit(): void {
    this.loadAirports();
    this.loadFlights();
    this.loadStats();
  }
  /////////////////////////////////////
  get currentStartIndex(): number {
    return (this.currentPage - 1) * this.pageSize + 1;
  }

  get currentEndIndex(): number {
    const endIndex = this.currentPage * this.pageSize;
    return Math.min(endIndex, this.totalElements);
  }

  get flightStartIndex(): number {
    return (this.currentFlightPage - 1) * this.flightPageSize + 1;
  }

  get flightEndIndex(): number {
    const endIndex = this.currentFlightPage * this.flightPageSize;
    return Math.min(endIndex, this.totalFlightElements);
  }
  /////////////////////////////////////

  loadAirports(): void {
    const [sortBy, sortDirection] = this.sort.split(',');
    const continentParam = '';

    this.airportService.getPageableAirports(this.currentPage - 1, this.pageSize, sortBy, sortDirection,continentParam).subscribe({
      next: (response: PageableAirportResponse) => {
        this.airports = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
      },
      error: (error) => {
        console.error('Error loading airports:', error);
      }
    });
  }

  onPageSizeChange(): void {
    this.currentPage = 1;
    this.loadAirports();
  }

  onSortChange(): void {
    this.currentPage = 1;
    this.loadAirports();
  }

  onPageChange(newPage: number): void {
    this.currentPage = newPage;
    this.loadAirports();
  }
  /////////////////////////////////////
  loadFlights(): void {
    const [sortBy, sortDirection] = this.flightSort.split(',');

    this.flightService.getPageableFlights(this.currentFlightPage - 1, this.flightPageSize, sortBy, sortDirection).subscribe({
      next: (response: PageableFlightResponse) => {
        this.flights = response.content;
        this.totalFlightPages = response.totalPages;
        this.totalFlightElements = response.totalElements;
      },
      error: (error) => {
        console.error('Error loading flights:', error);
      }
    });
  }

  onFlightPageSizeChange(): void {
    this.currentFlightPage = 1;
    this.loadFlights();
  }

  onFlightSortChange(): void {
    this.currentFlightPage = 1;
    this.loadFlights();
  }

  onFlightPageChange(newPage: number): void {
    this.currentFlightPage = newPage;
    this.loadFlights();
  }
  /////////////////////////////////////
  loadStats(): void {
    this.statsService.getTotalProfit().subscribe((profit) => this.totalProfit = profit);
    this.statsService.getReservationsCount().subscribe((count) => this.reservationsCount = count);
    this.statsService.getUserCount().subscribe((count) => this.userCount = count);
    this.statsService.getAirportCount().subscribe((count) => this.airportCount = count);
    this.statsService.getFlightCount().subscribe((count) => this.flightCount = count);

    this.statsService.getCheapestFlight().subscribe({
      next: (response: FlightResponse) => {
        if (response) {
          this.cheapestFlight = response;
        }
      },
      error: (error) => {
        console.error('Error loading cheapest flight:', error);
      }
    });

    this.statsService.getMostExpensiveFlight().subscribe({
      next: (response: FlightResponse) => {
        if (response) {
          this.mostExpensiveFlight = response;
        }
      },
      error: (error) => {
        console.error('Error loading most expensive flight:', error);
      }
    });

    this.statsService.getBusiestAirport().subscribe({
      next: (response: BusiestAirportResponse) => {
        console.log('Busiest Airport:', response);
        if (response) {
          this.busiestAirport = response;
        }
      },
      error: (error) => {
        console.error('Error loading busiest airport:', error);
      }
    });

    this.statsService.getMostPopularRoute().subscribe({
      next: (response: MostPopularRouteResponse) => {
        console.log('Most Popular Route:', response);
        this.mostPopularRoute = response;
      },
      error: (error) => {
        console.error('Error loading most popular route:', error);
      }
    });
  }

  ///////////////////////////////////
  deleteAirport(code: string): void {
    if (confirm('Are you sure you want to delete this airport?')) {
      this.airportService.deleteAirport(code).subscribe({
        next: () => {

          this.airports = this.airports.filter(airport => airport.code !== code);
          console.log('Airport deleted:', code);
          this.loadAirports();
        },
        error: (error) => {
          console.error('Error deleting airport:', error);
        }
      });
    }
  }

  deleteFlight(flightNumber: string): void {
    if (confirm('Are you sure you want to delete this flight?')) {
      this.flightService.deleteFlight(flightNumber).subscribe({
        next: () => {

          this.flights = this.flights.filter(flight => flight.flightNumber !== flightNumber);
          console.log('Flight deleted:', flightNumber);
          this.loadFlights();
        },
        error: (error) => {
          console.error('Error deleting flight:', error);
        }
      });
    }
  }
}

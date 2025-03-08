import {Component, OnInit} from '@angular/core';
import {AirportService} from '../../../services/airport.service';
import {FormsModule} from '@angular/forms';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {NgbPagination} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-airport-page',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    DatePipe,
    NgbPagination
  ],
  templateUrl: './airport-page.component.html',
  styleUrl: './airport-page.component.scss'
})
export class AirportPageComponent implements OnInit {
  airports: any[] = [];
  currentPage = 1;
  pageSize = 6;
  totalPages = 0;
  totalElements = 0;
  sort = 'name,asc';
  pageSizeOptions = [6, 12, 24, 48];

  continents: string[] = ['World', 'North America', 'South America', 'Europe', 'Asia', 'Australia', 'Africa'];
  selectedContinent: string = 'World';

  constructor(private airportService: AirportService) {}

  ngOnInit(): void {
    this.loadAirports();
  }

  get currentStartIndex(): number {
    return (this.currentPage - 1) * this.pageSize + 1;
  }

  get currentEndIndex(): number {
    const endIndex = this.currentPage * this.pageSize;
    return Math.min(endIndex, this.totalElements);
  }

  onCategoryChange(continent: string): void {
    this.selectedContinent = continent;
    this.currentPage = 1; // Reset to the first page when changing categories
    this.loadAirports();
  }

  loadAirports(): void {
    const [sortBy, sortDirection] = this.sort.split(',');
    const continentParam = this.selectedContinent === 'World' ? '' : this.selectedContinent;

    this.airportService.getPageableAirports(
      this.currentPage - 1,
      this.pageSize,
      sortBy,
      sortDirection,
      continentParam
    ).subscribe({
      next: (response: any) => {  // Adjust type according to your response structure
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
}

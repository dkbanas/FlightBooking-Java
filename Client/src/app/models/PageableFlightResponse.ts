import {Pageable} from './PageableAirportResponse';
import {FlightResponse} from './FlightResponse';

export interface PageableFlightResponse {
  content: FlightResponse[];
  pageable: Pageable;
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

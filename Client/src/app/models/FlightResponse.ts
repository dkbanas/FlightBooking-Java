import {AirportResponse} from './AirportResponse';

export interface FlightResponse{
  id: number
  flightNumber: string
  departureLocation: AirportResponse
  arrivalLocation: AirportResponse
  departureTime: string
  arrivalTime: string
  priceEconomy: number
  priceBusiness: number
  totalSeatsEconomy: number
  duration: string
  airline: string
  availableSeatsEconomy: number
  createdAt: string
}

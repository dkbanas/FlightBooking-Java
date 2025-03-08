
export interface FlightRequest {
  flightNumber: string;
  departureLocationId: number;
  arrivalLocationId: number;
  departureTime: Date;
  arrivalTime: Date;
  priceEconomy: number;
  priceBusiness: number;
  totalSeatsEconomy: number;
  totalSeatsBusiness: number;
  airline: string;
}

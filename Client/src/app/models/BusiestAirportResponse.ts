export interface BusiestAirportResponse {
  airport: {
    id: number;
    name: string;
    code: string;
    city: string;
    country: string;
    createdAt: string;
    cityPhotoUrl: string;
    continent: string;
  };
  flightCount: number;
}

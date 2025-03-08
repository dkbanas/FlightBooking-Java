export interface MostPopularRouteResponse {
  departureAirport: {
    id: number;
    name: string;
    code: string;
    city: string;
    country: string;
    createdAt: string;
    cityPhotoUrl: string;
    continent: string;
  };
  arrivalAirport: {
    id: number;
    name: string;
    code: string;
    city: string;
    country: string;
    createdAt: string;
    cityPhotoUrl: string;
    continent: string;
  };
  reservationCount: number;
}

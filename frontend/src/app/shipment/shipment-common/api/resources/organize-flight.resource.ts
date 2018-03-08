export class OrganizeFlightResource {

  constructor(flightNumber: string, airline: string, price: number,
              departureAirport: string, departureDate: string,
              destinationAirport: string, destinationDate: string) {
    this.flightNumber = flightNumber;
    this.airline = airline;
    this.price = price;

    this.departureAirport = departureAirport;
    this.departureTime = departureDate;

    this.destinationAirport = destinationAirport;
    this.destinationTime = destinationDate;

  }

  public flightNumber: string;
  public airline: string;
  public departureAirport: string;
  public departureTime: string;

  public destinationAirport: string;
  public destinationTime: string;
  public price: number;
}

package org.educama.shipment.api.resource;

import org.educama.shipment.api.datastructure.FlightDS;
import org.educama.shipment.model.Flight;

import java.time.Instant;

/**
 * REST-Resource for create or put Methods for flights.
 */
public class SaveFlightResource {

    public FlightDS shipmentFlight;

    /**
     * Convert this instance of API-Model (Resource) to the internal data model.
     *
     * @return the converted instance
     */
    public Flight toFlight() {
        Flight toConvert = new Flight();
        toConvert.airline = shipmentFlight.airline;
        toConvert.departureAirport = shipmentFlight.departureAirport;
        toConvert.destinationAirport = shipmentFlight.destinationAirport;
        toConvert.departureTime = Instant.parse(shipmentFlight.departureTime);
        toConvert.destinationTime = Instant.parse(shipmentFlight.destinationTime);
        toConvert.flightNumber = shipmentFlight.flightNumber;
        toConvert.price = shipmentFlight.price;
        return toConvert;
    }
}

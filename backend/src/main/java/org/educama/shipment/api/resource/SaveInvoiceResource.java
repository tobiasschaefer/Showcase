package org.educama.shipment.api.resource;

import org.educama.shipment.model.Invoice;

import java.time.Instant;

/**
 * REST-Resource for create or put Methods for invoices.
 */
public class SaveInvoiceResource {

    public String invoiceCreationDate;
    public double preCarriage;
    public double exportInsurance;
    public double exportCustomsClearance;
    public double flightPrice;
    public double importInsurance;
    public double importCustomsClearance;
    public double onCarriage;
    public double managementFee;
    public double serviceFee;
    public double discount;

    /**
     * Convert this instance of API-Model (Resource) to the internal data model.
     *
     * @return the converted instance
     */
    public Invoice toInvoice() {
        Invoice toConvert = new Invoice();
        toConvert.invoiceCreationDate = Instant.parse(invoiceCreationDate);
        toConvert.preCarriage = preCarriage;
        toConvert.exportInsurance = exportInsurance;
        toConvert.exportCustomsClearance = exportCustomsClearance;
        toConvert.flightPrice = flightPrice;
        toConvert.importInsurance = importInsurance;
        toConvert.importCustomsClearance = importCustomsClearance;
        toConvert.onCarriage = onCarriage;
        toConvert.managementFee = managementFee;
        toConvert.serviceFee = serviceFee;
        toConvert.discount = discount;

        return toConvert;
    }
}

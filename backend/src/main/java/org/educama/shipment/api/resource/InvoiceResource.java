package org.educama.shipment.api.resource;

import org.educama.shipment.model.Invoice;

/**
 * REST-Resource for single invoice.
 */
public class InvoiceResource {

    public String invoiceNumber;
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
     * Create a API-Model (Resource) instance from the internal data model.
     *
     * @param invoiceModel instance of the internal-data model
     * @return a converted InvoiceResource
     */

    public InvoiceResource fromInvoice(Invoice invoiceModel) {
        this.invoiceNumber = invoiceModel.invoiceNumber;
        this.invoiceCreationDate = invoiceModel.invoiceCreationDate.toString();
        this.preCarriage = invoiceModel.preCarriage;
        this.exportInsurance = invoiceModel.exportInsurance;
        this.exportCustomsClearance = invoiceModel.exportCustomsClearance;
        this.flightPrice = invoiceModel.flightPrice;
        this.importInsurance = invoiceModel.importInsurance;
        this.importCustomsClearance = invoiceModel.importCustomsClearance;
        this.onCarriage = invoiceModel.onCarriage;
        this.managementFee = invoiceModel.managementFee;
        this.serviceFee = invoiceModel.serviceFee;
        this.discount = invoiceModel.discount;
        return this;
    }
}

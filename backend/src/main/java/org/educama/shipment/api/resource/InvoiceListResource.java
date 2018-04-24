package org.educama.shipment.api.resource;

import org.educama.shipment.model.Invoice;

import java.util.ArrayList;
import java.util.Collection;

/**
 * REST-Resource for a list of invoices.
 */
public class InvoiceListResource {

    public Collection<InvoiceResource> invoices;

    public InvoiceListResource fromInvoiceCollection(Collection<Invoice> invoicesList) {
        this.invoices = new ArrayList<>();

        for (Invoice currentInvoice : invoicesList) {
            invoices.add(new InvoiceResource().fromInvoice(currentInvoice));
        }

        return this;
    }
}


package org.educama.shipment.process.tasks;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.CaseExecution;
import org.camunda.bpm.engine.task.Task;
import org.educama.shipment.control.ShipmentControlService;
import org.educama.shipment.process.ShipmentCaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Class representing an interface to the Create Invoice Task.
 */
@Component
public class CreateInvoiceTask {

    private ProcessEngine processEngine;
    private ShipmentControlService shipmentControlService;

    @Autowired
    public CreateInvoiceTask(ProcessEngine processEngine, ShipmentControlService shipmentControlService) {
        this.processEngine = processEngine;
        this.shipmentControlService = shipmentControlService;
    }

    public boolean isActive(String trackingId) {
        return processEngine.getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_HUMAN_TASK_CREATE_INVOICE)
                .caseInstanceBusinessKey(trackingId)
                .active()
                .singleResult() != null;
    }

    public boolean canBeCompleted(String trackingId) {
        return shipmentControlService.isShipmentOrderComplete(trackingId);
    }

    public void complete(String trackingId) {
        CaseExecution createInvoiceCaseExecution = processEngine.getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_HUMAN_TASK_CREATE_INVOICE)
                .caseInstanceBusinessKey(trackingId)
                .active()
                .singleResult();

        Assert.notNull(createInvoiceCaseExecution);

        Task task = processEngine.getTaskService().createTaskQuery()
                .caseExecutionId(createInvoiceCaseExecution.getId())
                .singleResult();

        Assert.notNull(task);

        processEngine.getTaskService().complete(task.getId());
    }

}

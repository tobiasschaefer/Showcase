package org.educama.shipment.process;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.runtime.CaseExecution;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.educama.BeanTestConfiguration;
import org.educama.EducamaApplication;
import org.educama.customer.model.Address;
import org.educama.customer.model.Customer;
import org.educama.customer.repository.CustomerRepository;
import org.educama.enums.ClientType;
import org.educama.shipment.model.Cargo;
import org.educama.shipment.model.Services;
import org.educama.shipment.model.Shipment;
import org.educama.shipment.repository.ShipmentRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.processEngine;
import static org.junit.Assert.*;

/**
 * Tests the CMMN model.
 */
@SuppressWarnings("checkstyle:magicnumber")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { EducamaApplication.class, BeanTestConfiguration.class })
@Deployment(resources = "cmmn/ShipmentCase.cmmn")
public class ShipmentCaseModelTest extends AbstractProcessEngineRuleTest {

    String caseInstanceId;
    Long shipmentId;

    @Autowired
    ShipmentRepository shipmentRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ShipmentCaseEvaluator caseModelHandler;

    @Before
    public void setup() {
        Shipment shipment = new Shipment();
        shipment.trackingId = "INCOMPLETE_SHIPMENT_ID";
        shipment.customerTypeEnum = ClientType.SENDER;

        shipment.receiver = new Customer("Arthur Dent", new Address("Arthursstreet", "42", "00042", "Earth"));
        customerRepository.save(shipment.receiver);

        shipment.sender = new Customer("Zaphood Beeblebrox", new Address("Zaphodsstreet", "42", "12342", "Beitegeuze"));
        customerRepository.save(shipment.sender);

        shipment.shipmentCargo = new Cargo(null, 2.0, 123.0, "Don't Panic!", true);
        shipment.shipmentServices = new Services(false, false, false, true, false, false, true);
        shipmentRepository.save(shipment);

        this.shipmentId = shipment.getId();
    }

    @After
    public void cleanup() {
        shipmentRepository.deleteAll();
        customerRepository.deleteAll();

        // cleanup
        processEngine().getCaseService().terminateCaseExecution(caseInstanceId);
        processEngine().getCaseService().closeCaseInstance(caseInstanceId);
    }

    @Test
    public void testCaseInitializationWithIncompleteShipmentData() {
        Shipment shipment = shipmentRepository.findOne(this.shipmentId);
        shipment.trackingId = UUID.randomUUID().toString();
        shipmentRepository.save(shipment);

        CaseInstance caseInstance = processEngine().getCaseService()
                .createCaseInstanceByKey(ShipmentCaseConstants.SHIPMENTCASEKEY, shipment.trackingId);
        this.caseInstanceId = caseInstance.getId();

        showCaseOverview(caseInstance);

        // Case Instance active?
        assertTrue(caseInstance.isActive());

        // Milestone 'Shipment order completed' not reached?
        assertTrue(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_MILESTONE_SHIPMENT_ORDER_COMPLETED)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult().isAvailable());

        // Task 'Complete shipment order' active?
        assertTrue(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_HUMAN_TASK_COMPLETE_SHIPMENT_ORDER)
                .caseInstanceBusinessKey(shipment.trackingId).active().singleResult().isActive());

        // Stage 'Process shipment order' is not enabled?
        assertFalse(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_STAGE_PROCESS_SHIPMENT_ORDER)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult().isEnabled());
    }

    @Test
    public void testCaseInitializationWithIncompleteShipmentDataWithModelUpdate() {
        Shipment shipment = shipmentRepository.findOne(this.shipmentId);
        shipment.trackingId = UUID.randomUUID().toString();
        shipmentRepository.save(shipment);

        CaseInstance caseInstance = processEngine().getCaseService()
                .createCaseInstanceByKey(ShipmentCaseConstants.SHIPMENTCASEKEY, shipment.trackingId);
        this.caseInstanceId = caseInstance.getId();

        showCaseOverview(caseInstance);

        // Case Instance active?
        assertTrue(caseInstance.isActive());

        // Milestone 'Shipment order completed' not reached?
        assertTrue(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_MILESTONE_SHIPMENT_ORDER_COMPLETED)
                .caseInstanceBusinessKey(shipment.trackingId)
                .singleResult()
                .isAvailable());

        // Task 'Complete shipment order' active?
        CaseExecution completeShipmentOrderCaseExecution = processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_HUMAN_TASK_COMPLETE_SHIPMENT_ORDER)
                .caseInstanceBusinessKey(shipment.trackingId)
                .active()
                .singleResult();
        assertTrue(completeShipmentOrderCaseExecution.isActive());

        // Stage 'Process shipment order' is not enabled?
        assertFalse(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_STAGE_PROCESS_SHIPMENT_ORDER)
                .caseInstanceBusinessKey(shipment.trackingId)
                .singleResult()
                .isEnabled());

        Shipment shipment2 = shipmentRepository.findOne(this.shipmentId);
        shipment2.shipmentCargo = new Cargo(1, 2.0, 123.0, "Don't Panic!", true);
        shipmentRepository.save(shipment2);

        // Complete task 'Complete shipment order'
        Task task = processEngine().getTaskService().createTaskQuery()
                .caseExecutionId(completeShipmentOrderCaseExecution.getId())
                .singleResult();
        processEngine().getTaskService().complete(task.getId());

        // evaluate
        System.out.println("CREATING...");
        caseModelHandler.reevaluateCase(shipment.trackingId);

        // print
        showCaseOverview(caseInstance);

        // Task 'Complete shipment order' completed?
        assertNull(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_HUMAN_TASK_COMPLETE_SHIPMENT_ORDER)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult());

        // Stage 'Process shipment order' is active?
        assertTrue(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_STAGE_PROCESS_SHIPMENT_ORDER)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult().isActive());
    }

    @Test
    public void testCaseAfterCreation() {
        Shipment shipment = shipmentRepository.findOne(this.shipmentId);
        shipment.shipmentCargo.numberPackages = 2;
        shipment.trackingId = UUID.randomUUID().toString();
        shipmentRepository.save(shipment);

        CaseInstance caseInstance = processEngine().getCaseService()
                .createCaseInstanceByKey(ShipmentCaseConstants.SHIPMENTCASEKEY, shipment.trackingId);
        this.caseInstanceId = caseInstance.getId();

        // Case Instance active?
        assertTrue(caseInstance.isActive());

        showCaseOverview(caseInstance);

        // Milestone reached and stage activated?
        assertNull(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_MILESTONE_SHIPMENT_ORDER_COMPLETED)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult());

        // Stage "PlanItem_Stage_ProcessShipmentOrder" automatically active with
        // the input data?
        assertTrue(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_STAGE_PROCESS_SHIPMENT_ORDER)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult().isActive());

        // Task 'PlanItem_HumanTask_ChangeShipmentOrder' enabled? -> Manual Task
        assertTrue(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_HUMAN_TASK_CHANGE_SHIPMENT_ORDER)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult().isEnabled());

        // Stage 'PlanItem_HumanTask_CreateInvoice' is available?
        assertTrue(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_HUMAN_TASK_CREATE_INVOICE)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult().isAvailable());

        // Stage 'PlanItem_HumanTask_OrganizeFlight' is active?
        assertTrue(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_HUMAN_TASK_ORGANIZE_FLIGHT)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult().isActive());
    }

    @Test
    public void testNullValueDataAfterCreation() {
        Shipment shipment = shipmentRepository.findOne(this.shipmentId);
        shipment.trackingId = UUID.randomUUID().toString();
        shipmentRepository.save(shipment);

        CaseInstance caseInstance = processEngine().getCaseService()
                .createCaseInstanceByKey(ShipmentCaseConstants.SHIPMENTCASEKEY, shipment.trackingId);
        this.caseInstanceId = caseInstance.getId();

        showCaseOverview(caseInstance);

        // Milestone reached and stage activated?
        assertTrue(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_MILESTONE_SHIPMENT_ORDER_COMPLETED)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult().isAvailable());

        // Stage "PlanItem_Stage_ProcessShipmentOrder" automatically active with
        // the input data?
        assertTrue(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_STAGE_PROCESS_SHIPMENT_ORDER)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult().isAvailable());

        // Task 'PlanItem_HumanTask_ChangeShipmentOrder' enabled? -> Manual Task
        assertNull(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_HUMAN_TASK_CHANGE_SHIPMENT_ORDER)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult());

        // Stage 'PlanItem_HumanTask_CreateInvoice' is available?
        assertNull(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_HUMAN_TASK_CREATE_INVOICE)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult());

        // Stage 'PlanItem_HumanTask_OrganizeFlight' is available?
        assertNull(processEngine().getCaseService().createCaseExecutionQuery()
                .activityId(ShipmentCaseConstants.PLAN_ITEM_HUMAN_TASK_ORGANIZE_FLIGHT)
                .caseInstanceBusinessKey(shipment.trackingId).singleResult());
    }

    /**
     * Helper method because it is not provided by
     * {@link org.camunda.bpm.engine.test.assertions.ProcessEngineTests}.
     *
     * @return current CaseService
     */
    private CaseService caseService() {
        return processEngine().getCaseService();
    }

    // method to display an overview of executions
    private void showCaseOverview(CaseInstance caseInstance) {
        List<CaseExecution> caseExecutionList = caseService().createCaseExecutionQuery()
                .caseInstanceId(caseInstance.getId()).list();
        System.out.println("------ Current List of Case Executions ------");

        caseExecutionList.stream().filter(caseExecution -> caseExecution.getId() == caseInstance.getId())
                .forEach(caseExecution -> System.out.println("Case Instance : " + caseExecution.getActivityName() + " ["
                        + caseExecution.getActivityType() + ", " + caseExecution.getActivityId() + "]"
                        + " - CaseExecutionId: " + caseExecution.getId()));

        caseExecutionList.stream().filter(caseExecution -> caseExecution.isActive())
                .forEach(caseExecution -> System.out.println("Running ('active'): " + caseExecution.getActivityName()
                        + " [" + caseExecution.getActivityType() + ", " + caseExecution.getActivityId() + "]"
                        + " - CaseExecutionId: " + caseExecution.getId()));

        caseExecutionList.stream().filter(caseExecution -> caseExecution.isEnabled())
                .forEach(caseExecution -> System.out.println("Possible to start ('enabled'): "
                        + caseExecution.getActivityName() + " [" + caseExecution.getActivityType() + ", "
                        + caseExecution.getActivityId() + "]" + " - CaseExecutionId: " + caseExecution.getId()));

        caseExecutionList.stream().filter(c -> c.isAvailable())
                .filter(c -> c.getActivityType().compareTo("milestone") != 0)
                .forEach(caseExecution -> System.out.println("Impossible to start ('available'): "
                        + caseExecution.getActivityName() + " [" + caseExecution.getActivityType() + ", "
                        + caseExecution.getActivityId() + "]" + " - CaseExecutionId: " + caseExecution.getId()));

        caseExecutionList.stream().filter(c -> c.isAvailable())
                .filter(c -> c.getActivityType().compareTo("milestone") == 0)
                .forEach(caseExecution -> System.out.println("Milestone not reached yet: "
                        + caseExecution.getActivityName() + " - CaseExecutionId: " + caseExecution.getId()));

        System.out.println("---------------------------------------------");
        System.out.println();
    }
}

package org.educama.shipment.api;

import org.educama.shipment.api.datastructure.EnabledTaskDS;
import org.educama.shipment.api.resource.EnabledTaskListResource;
import org.educama.shipment.api.resource.ShipmentTaskListResource;
import org.educama.shipment.api.resource.CompletedTaskListResource;
import org.educama.shipment.boundary.ShipmentTaskBoundaryService;
import org.educama.shipment.api.datastructure.ShipmentTaskDS;
import org.educama.shipment.api.datastructure.CompletedTaskDS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-Service to access shipment task resources.
 */
@RestController
@RequestMapping(path = ShipmentTaskController.SHIPMENTTASK_RESOURCE_PATH, produces = {MediaType.APPLICATION_JSON_VALUE})
public class ShipmentTaskController {

    public static final String SHIPMENTTASK_RESOURCE_PATH = "/educama/v1/tasks";

    @Autowired
    private ShipmentTaskBoundaryService shipmentTaskBoundaryService;

    /**
     * @return a Tasklist assigned to user "educama"
     */
    @RequestMapping(path = "/active", method = RequestMethod.GET)
    public ShipmentTaskListResource getTasks() {
        List<ShipmentTaskDS> tasks = shipmentTaskBoundaryService.findAllActive();
        ShipmentTaskListResource taskList = new ShipmentTaskListResource().fromTaskCollection(tasks);
        return taskList;
    }

    /**
     * @return a task list of active tasks for a specific shipment
     */
    @RequestMapping(path = "/active/{trackingId}", method = RequestMethod.GET)
    public ShipmentTaskListResource getActiveTasksForShipment(@PathVariable("trackingId") String trackingId) {
        List<ShipmentTaskDS> tasks = shipmentTaskBoundaryService.findAllActiveForShipment(trackingId);
        ShipmentTaskListResource taskList = new ShipmentTaskListResource().fromTaskCollection(tasks);
        return taskList;
    }

    /**
     * @return a Completed Tasklist for a Sipment"
     */
    @RequestMapping(value = "/completed/{trackingId}", method = RequestMethod.GET)
    public CompletedTaskListResource getCompletedTasks(@PathVariable("trackingId") String trackingId) {
        List<CompletedTaskDS> completedTask = shipmentTaskBoundaryService.findAllCompletedTasksForShipment(trackingId);
        CompletedTaskListResource completedTaskListResource = new CompletedTaskListResource().fromTaskCollection(completedTask);
        return completedTaskListResource;
    }

    /**
     * @return a Tasklist with enabled tasks for a specific shipment
     */
    @RequestMapping(value = "/enabled/{trackingId}", method = RequestMethod.GET)
    public EnabledTaskListResource getEnabledTasks(@PathVariable("trackingId") String trackingId) {
        List<EnabledTaskDS> enabledTask = shipmentTaskBoundaryService.findAllEnabledTasksForShipment(trackingId);
        EnabledTaskListResource enabledTaskListResource = new EnabledTaskListResource().fromTaskCollection(enabledTask);
        return enabledTaskListResource;
    }

    /**
     * API call to manually start an enabled task by trackingId and name.
     */
    @RequestMapping(value = "/enabled/start/{trackingId}/{name}", method = RequestMethod.POST)
    public EnabledTaskListResource manuallyStartEnabledTask(@PathVariable("trackingId") String trackingId, @PathVariable("name") String name) {
        shipmentTaskBoundaryService.manuallyStartEnabledTask(trackingId, name);
        List<EnabledTaskDS> enabledTask = shipmentTaskBoundaryService.findAllEnabledTasksForShipment(trackingId);
        EnabledTaskListResource enabledTaskListResource = new EnabledTaskListResource().fromTaskCollection(enabledTask);
        return enabledTaskListResource;
    }

    /**
     * API call to complete an active task by trackingId and name.
     */
    @RequestMapping(value = "/active/{trackingId}/{name}", method = RequestMethod.POST)
    public ShipmentTaskListResource completeTask(@PathVariable("trackingId") String trackingId, @PathVariable("name") String name) {
        shipmentTaskBoundaryService.completeTask(trackingId, name);
        List<ShipmentTaskDS> completeTask = shipmentTaskBoundaryService.findAllActiveForShipment(trackingId);
        ShipmentTaskListResource shipmentTaskListResource = new ShipmentTaskListResource().fromTaskCollection(completeTask);
        return shipmentTaskListResource;
    }

}


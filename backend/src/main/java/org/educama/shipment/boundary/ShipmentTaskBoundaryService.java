package org.educama.shipment.boundary;

import org.educama.shipment.api.datastructure.EnabledTaskDS;
import org.educama.shipment.api.datastructure.ShipmentTaskDS;
import org.educama.shipment.api.datastructure.CompletedTaskDS;
import java.util.List;


/**
 * Boundary service for shipment tasks.
 */
public interface ShipmentTaskBoundaryService {
    /**
     * Retrieves all tasks.
     *
     * @return a collection of all tasks
     */
     List<ShipmentTaskDS> findAllActive();

    /**
     * Retrieves all active tasks of a shipment.
     *
     * @return a collection of all active tasks to shipment
     */

    List<ShipmentTaskDS> findAllActiveForShipment(String trackingId);

    /**
     * Retrieves all enabled tasks for one shipment.
     *
     * @return a collection of all enabled tasks
     */
     List <EnabledTaskDS> findAllEnabledTasksForShipment(String trackingId);

     /**
      * @return a collection of completed tasks for a Shipment.
      */
     List<CompletedTaskDS> findAllCompletedTasksForShipment(String trackingId);

    /**
     * manually start a enabled task in camunda.
     */
     void manuallyStartEnabledTask(String trackingId, String name);

    /**
     * complete a task in camunda.
     */
    void completeTask(String trackingId, String name);

}

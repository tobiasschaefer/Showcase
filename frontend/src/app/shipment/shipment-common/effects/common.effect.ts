import {Injectable} from "@angular/core";
import {Actions, Effect} from "@ngrx/effects";
import * as shipmentCaptureActions from "../store/shipments/shipment-capture-page/shipment-capture-page.actions";
import * as organizeFlightactions from "../store/shipments/organize-flight-page/organize-flight-page.actions";
import {
  RequestTasksForShipmentAction
} from "../store/tasks/task-list-page.actions";
import {
  RequestEnabledTasksForShipmentAction
} from "../store/enbaled-tasks/enabled-task-list-page.actions";
import {
  RequestCompletedTaskForShipmentAction
} from "../store/completed-tasks/completed-task-list-page.actions";
import {RequestSingleShipment} from "../store/shipments/shipment-list-page/shipment-list-page.actions";


@Injectable()
export class CommonEffect {
  constructor(private _actions: Actions) {
  }

  @Effect()
  reloadShipment = this._actions
    .ofType("RELOAD_STORE_ACTION")
    .map((action: shipmentCaptureActions.ReloadStoreAction) =>
      new RequestSingleShipment(action.trackingId)
    );

  @Effect()
  reloadActiveTasks = this._actions
    .ofType("RELOAD_STORE_ACTION")
    .map((action: shipmentCaptureActions.ReloadStoreAction) =>
      new RequestTasksForShipmentAction(action.trackingId));

  @Effect()
  reloadEnabledTasks = this._actions
    .ofType("RELOAD_STORE_ACTION")
    .map((action: shipmentCaptureActions.ReloadStoreAction) =>
      new RequestEnabledTasksForShipmentAction(action.trackingId));

  @Effect()
  reloadCompletedTasks = this._actions
    .ofType("RELOAD_STORE_ACTION")
    .map((action: shipmentCaptureActions.ReloadStoreAction) =>
      new RequestCompletedTaskForShipmentAction(action.trackingId));


  @Effect()
  reloadCompletedTaskForOrganizeFlight = this._actions
    .ofType("SAVE_FLIGHT_SUCCESSFUL_ACTION")
    .map((action: organizeFlightactions.SaveFlightSuccessfultAction) =>
      new RequestCompletedTaskForShipmentAction(action.trackingId));

  @Effect()
  reloadShipmentForOrganizeFlight = this._actions
    .ofType("SAVE_FLIGHT_SUCCESSFUL_ACTION")
    .map((action: organizeFlightactions.SaveFlightSuccessfultAction) =>
      new RequestSingleShipment(action.trackingId));

  @Effect()
  reloadActiveTasksForOrganizeFlight = this._actions
    .ofType("SAVE_FLIGHT_SUCCESSFUL_ACTION")
    .map((action: organizeFlightactions.SaveFlightSuccessfultAction) =>
      new RequestTasksForShipmentAction(action.trackingId));

  @Effect()
  reloadEnabledTasksTasksForOrganizeFlight = this._actions
    .ofType("SAVE_FLIGHT_SUCCESSFUL_ACTION")
    .map((action: organizeFlightactions.SaveFlightSuccessfultAction) =>
      new RequestEnabledTasksForShipmentAction(action.trackingId));

}

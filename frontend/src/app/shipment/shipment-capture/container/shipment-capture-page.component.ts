import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {State} from "../../../app.reducers";
import {Store} from "@ngrx/store";
import {SaveShipmentEvent} from "../presentationals/events/save-shipment.event";
import {ShipmentResource} from "../../shipment-common/api/resources/shipment.resource";
import {Subscription, Observable} from "rxjs";
import {ShipmentService} from "../../shipment-common/api/shipment.service";
import {ShipmentCapturePageModel} from "./shipment-capture-page.model";
import * as _ from "lodash";
import {CompleteActiveTask} from "../../shipment-common/store/tasks/task-list-page.actions";
import {ShipmentCaptureSlice} from "../../shipment-common/store/shipments/shipment-capture-page/shipment-capture-page.slice";
import {RequestSingleShipment} from "../../shipment-common/store/shipments/shipment-list-page/shipment-list-page.actions";
import {
  ResetShipmentCaptureSliceAction,
  ReloadStoreAction
} from "../../shipment-common/store/shipments/shipment-capture-page/shipment-capture-page.actions";
import {OrganizeFlightResource} from "../../shipment-common/api/resources/organize-flight.resource";
import {CompleteTaskEvent} from "../presentationals/events/complete-task.event";

@Component({
  selector: "educama-shipment-capture-page",
  templateUrl: "./shipment-capture-page.component.html",
})
export class ShipmentCapturePageComponent implements OnInit, OnDestroy {

  // relevant slice of store and subscription for this slice
  public shipmentCaptureSlice: Observable<ShipmentCaptureSlice>;
  public shipmentCaptureSliceSubscription: Subscription;

  // model for the page
  public shipmentCaptureModel: ShipmentCapturePageModel = new ShipmentCapturePageModel();

  constructor(private _activatedRoute: ActivatedRoute,
              private _shipmentService: ShipmentService,
              private _router: Router,
              private _store: Store<State>) {

    this.shipmentCaptureSlice = this._store.select(state => state.shipmentCaptureSlice);

    this.shipmentCaptureSliceSubscription = this.shipmentCaptureSlice.subscribe(
      shipmentCaptureSlice => this.updateShipmentCaptureModel(shipmentCaptureSlice)
    );
  }

  public ngOnInit() {
    this._activatedRoute.parent.params.subscribe(params => {
      if (this._router.url.includes("caseui")) {
        this._store.dispatch(new RequestSingleShipment(params["id"]));
      }
      console.log(params["id"]);
    });
  }

  public ngOnDestroy() {
    this._store.dispatch(new ResetShipmentCaptureSliceAction(""));
    this.shipmentCaptureSliceSubscription.unsubscribe();
  }

  // ***************************************************
  // Event Handler
  // ***************************************************

  /*
   * Handles the save event for a shipment
   */
  public onSaveShipmentEvent(saveShipmentEvent: SaveShipmentEvent) {
    if (_.isUndefined(this.shipmentCaptureModel.shipment)) {
      this._shipmentService.createShipment(this.mapShipmentFromSaveShipmentEvent(saveShipmentEvent))
        .subscribe((shipment) => {
          this._router.navigate(["/caseui/" + shipment.trackingId]);
        });
    } else {
      this._activatedRoute.parent.params.subscribe(params => {
        saveShipmentEvent.trackingId = params["id"];
        this._shipmentService.updateShipment(params["id"], this.mapShipmentFromSaveShipmentEvent(saveShipmentEvent))
          .subscribe(shipment => {
            const trackingId = shipment.trackingId;
            this._store.dispatch(new ReloadStoreAction(trackingId));
            this._router.navigate(["/caseui/" + trackingId]);
          });
      });
    }
  }

  /*
   * Handles the cancellation of a new shipment creation
   */
  public onSaveShipmentCancellationEvent(saveShipmentEvent: SaveShipmentEvent) {
    this._router.navigate(["/shipments"]);
  }

  /*
   * Handles the complete task event for a shipment
   */
  public oncompleteTaskEvent(completeTaskEvent: CompleteTaskEvent) {
    this._store.dispatch(new CompleteActiveTask(completeTaskEvent.trackingId, completeTaskEvent.taskName));
  }

  // ***************************************************
  // Data Retrieval
  // ***************************************************

  private updateShipmentCaptureModel(shipmentCaptureSlice: ShipmentCaptureSlice) {
    this.shipmentCaptureModel.shipment = shipmentCaptureSlice.shipment;
  }

  private mapShipmentFromSaveShipmentEvent(saveShipmentEvent: SaveShipmentEvent): ShipmentResource {
    const shipment = new ShipmentResource();
    shipment.uuidSender = saveShipmentEvent.uuidSender;
    shipment.uuidReceiver = saveShipmentEvent.uuidReceiver;
    shipment.customerTypeEnum = saveShipmentEvent.customerTypeEnum;
    shipment.shipmentCargo = saveShipmentEvent.shipmentCargo;
    shipment.shipmentServices = saveShipmentEvent.shipmentServices;
    shipment.trackingId = saveShipmentEvent.trackingId;
    shipment.shipmentFlight = null;

    return shipment;

  }

}

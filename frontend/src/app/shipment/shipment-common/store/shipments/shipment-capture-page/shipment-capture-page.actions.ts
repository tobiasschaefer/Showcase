import {Action} from "@ngrx/store";
import {ShipmentResource} from "../../../api/resources/shipment.resource";
export const LOAD_SHIPMENT_SUCCESSFULL = "LOAD_SHIPMENT_SUCCESSFULL";
export const RESET_SHIPMENT_CAPTURE_SLICE = "type = RESET_SHIPMENT_CAPTURE_SLICE";
export const UPDATE_SHIPMENT = "UPDATE_SHIPMENT";
export const UPDATE_SHIPMENT_SUCCESSFULL = "UPDATE_SHIPMENT_SUCCESSFULL";

export class LoadShipmentSuccessfullAction implements Action {
  type = LOAD_SHIPMENT_SUCCESSFULL;

  constructor(public payload: ShipmentResource) {

  }
}

export class ResetShipmentCaptureSliceAction implements Action {
  type = RESET_SHIPMENT_CAPTURE_SLICE;

  constructor(public payload: any) {

  }
}

export class UpdateShipmentAction implements Action {
  type = UPDATE_SHIPMENT;

  constructor(public payload: ShipmentResource) {

  }
}

export class UpdateShipmentSucessfullAction implements Action {
  type = UPDATE_SHIPMENT_SUCCESSFULL;

  constructor(public payload: ShipmentResource) {

  }
}


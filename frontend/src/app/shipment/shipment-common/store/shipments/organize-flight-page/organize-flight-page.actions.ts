import {Action} from "@ngrx/store";
import {OrganizeFlightResource} from "../../../api/resources/organize-flight.resource";

export const SAVE_FLIGHT_ACTION = "SAVE_FLIGHT_ACTION";
export const SAVE_FLIGHT_SUCCESSFUL_ACTION = "SAVE_FLIGHT_SUCCESSFUL_ACTION";

export class SaveFlightAction implements Action {
    type = SAVE_FLIGHT_ACTION;

    constructor(public trackingId: string, public payload: OrganizeFlightResource) {

    }
}

export class SaveFlightSuccessfultAction implements Action {
  type = SAVE_FLIGHT_SUCCESSFUL_ACTION;

  constructor(public payload: OrganizeFlightResource) {

  }
}


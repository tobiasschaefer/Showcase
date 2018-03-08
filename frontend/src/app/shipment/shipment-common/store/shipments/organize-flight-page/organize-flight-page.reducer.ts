import {ActionReducer, Action} from "@ngrx/store";
import * as actions from "./organize-flight-page.actions";
import {
  ORGANIZE_FLIGHT_SLICE_INITIAL_STATE
} from "./organize-flight-page.initial-state";
import {OrganizeFlightSlice} from "./organize-flight-page.slice";

export function organizeFlightPageReducer(state: OrganizeFlightSlice = ORGANIZE_FLIGHT_SLICE_INITIAL_STATE,
                                           action: Action): OrganizeFlightSlice {
    switch (action.type) {
        case actions.SAVE_FLIGHT_SUCCESSFUL_ACTION:
            const saveFlightSuccessfultAction = action as actions.SaveFlightSuccessfultAction;
            return Object.assign({}, state, {
                flight: saveFlightSuccessfultAction.payload
            });
        default:
            return state;
    }
}

export const ORGANIZE_FLIGHT_PAGE_REDUCER: ActionReducer<OrganizeFlightSlice> = organizeFlightPageReducer;

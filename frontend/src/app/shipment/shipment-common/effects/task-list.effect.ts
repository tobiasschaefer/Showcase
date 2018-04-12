import {Injectable} from "@angular/core";
import {Actions, Effect} from "@ngrx/effects";
import {Store} from "@ngrx/store";
import {State} from "../../../app.reducers";
import * as actions from "../store/tasks/task-list-page.actions";
import {TaskListSlice} from "../store/tasks/task-list-page.slice";
import {Observable} from "rxjs/Observable";
import {TaskService} from "../api/task.service";
import {
  CompleteActiveTaskSuccessful, RequestTasksFailedAction,
  RequestTasksSuccessfulAction
} from "../store/tasks/task-list-page.actions";

@Injectable()
export class TaskListEffect {
  private id: string;
  constructor(private _actions: Actions,
              private _taskService: TaskService,
              private _store: Store<State>) {
  }

  @Effect() loadTasks = this._actions
    .ofType(actions.REQUEST_TASKS)
    .withLatestFrom(this._store, (action, state) => state.taskListSlice)
    .switchMap((taskListSlice: TaskListSlice) => {
      return this._taskService.findTasks();
    })
    .map(taskListSlice => new RequestTasksSuccessfulAction(taskListSlice))
    .catch(() => Observable.of(new RequestTasksFailedAction()));

  @Effect() loadTasksForShipment = this._actions
    .ofType(actions.REQUEST_TASKS_FOR_SHIPMENT)
    .map((action: actions.RequestTasksForShipmentAction) => this.id = action.trackingId)
    .withLatestFrom(this._store, (action, state) => state.taskListSlice)
    .switchMap((taskListSlice: TaskListSlice) => {
      return this._taskService.findTasksForShipment(this.id);
    })
    .map(taskListSlice => new RequestTasksSuccessfulAction(taskListSlice))
    .catch(() => Observable.of(new RequestTasksFailedAction()));

  @Effect()
  completeActiveTask = this._actions
    .ofType(actions.COMPLETE_ACTIVE_TASK)
    .switchMap((action: actions.CompleteActiveTask) =>
      this._taskService.manualyCompleteTaskByName(action.trackingId, action.taskName)
    )
    .map((payload) => new CompleteActiveTaskSuccessful(payload));
}

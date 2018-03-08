import {Component, Input, Output} from "@angular/core";
import {CompletedTaskListRowModel} from "../container/completed-task-list-page.model";
import {TaskResource} from "../../shipment-common/api/resources/task.resource";

@Component({
  selector: "educama-completed-task-list",
  templateUrl: "./completed-task-list.component.html"
})
export class CompletedTaskListComponent {

  @Input()
  public completedTaskList: CompletedTaskListRowModel[];

  @Output()
  public selectedCompletedTask: TaskResource = new TaskResource();
}

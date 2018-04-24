import {NgModule} from "@angular/core";
import {SharedModule} from "../../shared/shared.module";
import {ShipmentService} from "./api/shipment.service";
import {TaskService} from "./api/task.service";
import {EffectsModule} from "@ngrx/effects";
import {ShipmentListEffect} from "./effects/shipment-list.effect";
import {TaskListEffect} from "./effects/task-list.effect";
import {EnabledTaskListEffect} from "./effects/enabled-task-list.effect";
import {CompletedTaskListEffect} from "./effects/completed-task-list.effect";
import {CommonEffect} from "./effects/common.effect";

@NgModule({
  imports: [
    EffectsModule.forFeature([
      ShipmentListEffect,
      TaskListEffect,
      EnabledTaskListEffect,
      CompletedTaskListEffect,
      CommonEffect
    ]),
    SharedModule,
  ],
  providers: [
    ShipmentService,
    TaskService
  ]
})
export class ShipmentCommonModule {
}

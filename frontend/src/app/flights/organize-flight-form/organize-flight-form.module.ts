import {SharedModule} from "../../shared/shared.module";
import {OrganizeFlightFormComponent} from "./presentationals/organize-flight-form.component";
import {NgModule} from "@angular/core";
import {OrganizeFlightFormPageComponent} from "./container/organize-flight-form-page.component";


@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    OrganizeFlightFormComponent,
    OrganizeFlightFormPageComponent
  ],
  exports: [
    OrganizeFlightFormComponent
  ]
})
export class OrganizeFlightFormModule {
}

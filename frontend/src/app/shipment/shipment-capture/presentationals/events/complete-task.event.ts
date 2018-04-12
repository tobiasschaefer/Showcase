export class CompleteTaskEvent {
  trackingId: string;
  taskName: string;


  constructor(trackingId: string, taskName: string) {
    this.trackingId = trackingId;
    this.taskName = taskName;
  }
}

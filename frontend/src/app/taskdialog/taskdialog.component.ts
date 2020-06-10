import { Component, OnInit, Inject, Input } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { Task } from '../model/Task';

@Component({
  selector: 'app-taskdialog',
  templateUrl: './taskdialog.component.html',
  styleUrls: ['./taskdialog.component.css']
})
export class TaskdialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<TaskdialogComponent>,
    @Inject(MAT_DIALOG_DATA) public task: Task) {
      console.log("open");
      console.log(task);
      console.log(this.task);
    }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit() {
  }

}

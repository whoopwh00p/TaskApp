import { Component, OnInit, Inject, Input } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { Task } from '../model/Task';
import { State } from '../model/State';

@Component({
  selector: 'app-taskdialog',
  templateUrl: './taskdialog.component.html',
  styleUrls: ['./taskdialog.component.css']
})
export class TaskdialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<TaskdialogComponent>,
    @Inject(MAT_DIALOG_DATA) public task: Task) {
    }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit() {
    this.task.state = State.IN_PROGRESS;
  }
}

import { Component, OnInit, Input, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import {State} from '../model/State';
import { TaskService } from '../task.service';
import { Task } from '../model/Task';
import {MAT_DIALOG_DATA,MatDialogRef} from "@angular/material/dialog";

import { ConfirmDialogModel, ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';

@Component({
  selector: 'app-edit-task',
  templateUrl: './edit-task.component.html',
  styleUrls: ['./edit-task.component.css']
})
export class EditTaskComponent implements OnInit {

  dialogTypeEnum = DialogType;
  form: FormGroup;
  dialogType: DialogType;
  task: Task;
  states:State[] = [State.TODO,State.IN_PROGRESS,State.DONE];

  constructor(private formBuilder: FormBuilder, private dialogRef: MatDialogRef<EditTaskComponent>, @Inject(MAT_DIALOG_DATA) data, private taskService: TaskService, public dialog: MatDialog) { 
    if(data == null) {
      this.task = {
        'id': 0,
        'name': '',
        'description': '',
        'state': State.TODO
      };
      this.dialogType = DialogType.NEW;
    }
    else {
      this.task = data;
      //TODO: fix this workaround
      switch(data.state) {
        case "TODO":
          this.task.state = State.TODO;
          break;
        case "IN_PROGRESS":
          this.task.state = State.IN_PROGRESS;
          break;
        case "DONE":
          this.task.state = State.DONE;
          break;
      }
      this.dialogType = DialogType.EDIT;
    }
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group(this.task);
  }

  submit(): void {
    if(this.dialogType == DialogType.NEW) {
      this.taskService.createTask(this.form.value);
    } else {
      this.taskService.updateTask(this.form.value);
    }
    this.close();
  }

  close(): void {
    this.dialogRef.close();
  }

  delete(): void {
    const message = "Are you sure you want to delete task #"+this.task.id+"?";
 
    const dialogData = new ConfirmDialogModel("Confirm Action", message);
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      id: "confirm1",
      maxWidth: "400px",
      data: dialogData
    });
    dialogRef.afterClosed().subscribe(shouldDelete => {
      if(shouldDelete) {
        console.log("delete task");
        this.taskService.deleteTask(this.form.value);
        this.close();
      } 
    });

  }

}
export enum DialogType {
  NEW, EDIT
}
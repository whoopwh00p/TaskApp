import { Component, OnInit, Input, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import {State} from '../model/State';
import { TaskService } from '../task.service';
import { Task } from '../model/Task';
import { User } from '../model/User';
import {MAT_DIALOG_DATA,MatDialogRef} from "@angular/material/dialog";

import { ConfirmDialogModel, ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import { UserService } from '../user.service';

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
  userNames: String[] = [];
  users: User[] = [];
  constructor(private formBuilder: FormBuilder, 
      private dialogRef: MatDialogRef<EditTaskComponent>,
      @Inject(MAT_DIALOG_DATA) data, 
      private taskService: TaskService, 
      private userService: UserService,
      public dialog: MatDialog) { 
    if(data == null) {
      this.task = {
        'id': 0,
        'name': '',
        'description': '',
        'state': State.TODO,
        'assigneeId': null,
        'assigneeName': null
      };
      this.dialogType = DialogType.NEW;
    }
    else {
      this.task = data;
      if(this.task.assigneeId === undefined) {
        this.task.assigneeId = null;
      }
      if(this.task.assigneeName === undefined) {
        this.task.assigneeName = null;
      }
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
    this.userService.getUsers().subscribe(users =>  {
      for(let user of users) {
        this.userNames.push(user.name);
      }
      this.users = users;
    })

  }

  submit(): void {
    if(this.dialogType == DialogType.NEW) {
      this.taskService.createTask(this.form.value);
    } else {
      let updatedTask:Task  = this.form.value;
      console.log(updatedTask.assigneeName);
      if(!updatedTask.assigneeName === null) {
        updatedTask.assigneeId = this.users.find(u => u.name == updatedTask.assigneeName).id;
      } else {
        updatedTask.assigneeId = null;
      }
      this.taskService.updateTask(updatedTask);
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
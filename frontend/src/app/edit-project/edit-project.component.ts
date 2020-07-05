import { Component, OnInit, Input, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import {State} from '../model/State';
import { Task } from '../model/Task';
import { User } from '../model/User';
import {MAT_DIALOG_DATA,MatDialogRef} from "@angular/material/dialog";

import { ConfirmDialogModel, ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import { UserService } from '../user.service';
import { ProjectService } from '../project.service';
import { Project } from '../model/Project';

@Component({
  selector: 'app-edit-project',
  templateUrl: './edit-project.component.html',
  styleUrls: ['./edit-project.component.css']
})
export class EditProjectComponent implements OnInit {
  dialogTypeEnum = DialogType;
  form: FormGroup;
  dialogType: DialogType;
  project: Project;
  userNames: String[] = [];
  users: User[] = [];
  constructor(private formBuilder: FormBuilder, 
      private dialogRef: MatDialogRef<EditProjectComponent>,
      @Inject(MAT_DIALOG_DATA) data, 
      private projectService: ProjectService, 
      private userService: UserService,
      public dialog: MatDialog) { 
    if(data == null) {
      this.project = {
        'id': 0,
        'name': '',
        'shortName': ''
      };
      this.dialogType = DialogType.NEW;
    }
    else {
      this.project = data;
      this.dialogType = DialogType.EDIT;
    }
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group(this.project);
    this.userService.getUsers().subscribe(users =>  {
      for(let user of users) {
        this.userNames.push(user.name);
      }
      this.users = users;
    })

  }

  submit(): void {
    if(this.dialogType == DialogType.NEW) {
      this.projectService.createProject(this.form.value);
    } else {
      let updatedProject:Project  = this.form.value;
      this.projectService.updateProject(updatedProject);
    }
    this.close();
  }

  close(): void {
    this.dialogRef.close();
  }

  delete(): void {
    const message = "Are you sure you want to delete project "+this.project.name+"?";
 
    const dialogData = new ConfirmDialogModel("Confirm Action", message);
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      id: "confirm1",
      maxWidth: "400px",
      data: dialogData
    });
    dialogRef.afterClosed().subscribe(shouldDelete => {
      if(shouldDelete) {
        this.projectService.deleteProject(this.form.value);
        this.close();
      } 
    });

  }
}
export enum DialogType {
  NEW, EDIT
}
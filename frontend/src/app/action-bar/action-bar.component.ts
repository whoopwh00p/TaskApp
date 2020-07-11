import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ProjectService } from '../project.service';
import { Project } from '../model/Project';
import { CookieService } from 'ngx-cookie-service';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import { TaskdialogComponent } from '../taskdialog/taskdialog.component';
import { EditTaskComponent } from '../edit-task/edit-task.component';
import { EditProjectComponent } from '../edit-project/edit-project.component';
@Component({
  selector: 'app-action-bar',
  templateUrl: './action-bar.component.html',
  styleUrls: ['./action-bar.component.css']
})
export class ActionBarComponent implements OnInit {

  public projects: Project[];
  public selectedProject : Project;

  constructor(private projectService: ProjectService, private cookieService : CookieService,public dialog: MatDialog) { }

  ngOnInit(): void {
    this.projectService.getProjects().subscribe(result => {
      this.projects = result;
      this.setDefaultSelectedProject();
    });
  }

  selectProject(): void {
    this.projectService.setSelectedProject(this.selectedProject);
  }

  setDefaultSelectedProject() : void {
    let selectedProjectId : Number = parseInt(this.cookieService.get('selected-project'));
    this.selectedProject = this.findProjectById(selectedProjectId);
    this.selectProject();
  }

  openCreateTaskDialog() {
    const config = new MatDialogConfig();
    config.width = "80%";
    config.disableClose = true;
    const dialogRef = this.dialog.open(EditTaskComponent, config);
  }

  openCreateProjectDialog() {
    const config = new MatDialogConfig();
    config.width = "80%";
    config.disableClose = true;
    const dialogRef = this.dialog.open(EditProjectComponent, config);
  }

  openEditProjectDialog() {
    const config = new MatDialogConfig();
    config.width = "80%";
    config.disableClose = true;
    config.data = this.selectedProject;
    const dialogRef = this.dialog.open(EditProjectComponent, config);
  }

  private findProjectById(id:Number) : Project {
    for(let project of this.projects) {
      if(project.id == id) {
        return project;
      }
    }
    return this.projects[0];
  }
}

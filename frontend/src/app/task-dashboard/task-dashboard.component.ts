import { Component, OnInit } from '@angular/core';
import { Task } from '../model/Task';
import { TaskService } from '../task.service';
import { State } from '../model/State';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import { TaskdialogComponent } from '../taskdialog/taskdialog.component';
import { EditTaskComponent } from '../edit-task/edit-task.component';
import { ProjectService } from '../project.service';
import { Project } from '../model/Project';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-task-dashboard',
  templateUrl: './task-dashboard.component.html',
  styleUrls: ['./task-dashboard.component.css']
})
export class TaskDashboardComponent implements OnInit {

  public tasks: Task[];
  public states:State[] = [State.TODO,State.IN_PROGRESS,State.DONE];
  public displayedRows:number;
  public task: Task;
  public projectShortName: String;
  
  constructor(private taskService: TaskService,private projectService: ProjectService,public dialog: MatDialog) { }

  ngOnInit(): void {
    this.taskService.refreshNeeded$.subscribe(() => {
      this.getTasks();
    });
    this.projectService.refreshNeeded$.subscribe(() => {
      this.projectShortName = this.projectService.getSelectedProject().shortName;
    });
    this.displayedRows = Math.floor(12/this.states.length);
  }

  getTasks(): void {
    console.log("getTasks");
    this.taskService.getTasks()
      .subscribe(tasks =>{
        this.tasks = tasks;
      });
  }

  drop(event: CdkDragDrop<State>) {
    let draggedTask: Task = event.item.data;
    let targetState: State = event.container.data;
    if(draggedTask.state != targetState) {
      draggedTask.state = targetState;
      this.taskService.updateTask(draggedTask);
    }
  }

  open(task: Task) {
    const config = new MatDialogConfig();
    config.width = "80%";
    config.data = task;
    const dialogRef = this.dialog.open(EditTaskComponent, config);
  }
}

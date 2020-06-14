import { Component, OnInit } from '@angular/core';
import { Task } from '../model/Task';
import { TaskService } from '../task.service';
import { State } from '../model/State';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import { TaskdialogComponent } from '../taskdialog/taskdialog.component';
import { EditTaskComponent } from '../edit-task/edit-task.component';
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

  
  constructor(private taskService: TaskService,public dialog: MatDialog) { }

  ngOnInit(): void {
    this.taskService.refreshNeeded$.subscribe(() => {
      this.getTasks();
    });
    this.getTasks();
    this.displayedRows = Math.floor(12/this.states.length);
    console.log("opjaisd");
    console.log(this.states);
  }

  getTasks(): void {
    console.log("getTasks");
    this.taskService.getTasks()
      .subscribe(tasks =>{
        this.tasks = tasks;
      });
  }

  open(task: Task) {
    const config = new MatDialogConfig();
    config.width = "80%";
    config.data = task;
    const dialogRef = this.dialog.open(EditTaskComponent, config);
  }

  openCreateTaskDialog() {
    const config = new MatDialogConfig();
    config.width = "80%";
    config.disableClose = true;
    const dialogRef = this.dialog.open(EditTaskComponent, config);
  }
}

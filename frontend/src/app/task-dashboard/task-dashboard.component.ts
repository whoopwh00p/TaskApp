import { Component, OnInit } from '@angular/core';
import { Task } from '../model/Task';
import { TaskService } from '../task.service';
import { State } from '../model/State';
import {MatDialog} from '@angular/material/dialog';
import { TaskdialogComponent } from '../taskdialog/taskdialog.component';
@Component({
  selector: 'app-task-dashboard',
  templateUrl: './task-dashboard.component.html',
  styleUrls: ['./task-dashboard.component.css']
})
export class TaskDashboardComponent implements OnInit {

  public tasks: Task[];
  public states: State[];
  public displayedRows:number;

  constructor(private taskService: TaskService,public dialog: MatDialog) { }

  ngOnInit(): void {
    this.states = new Array<State>();
    this.tasks = this.taskService.getTasks();

    for(let task of this.tasks) {
      if(!this.states.includes(task.state)) {
        this.states.push(task.state);
      }
    }
    this.states.sort((a,b) => a.getId()-b.getId());
    this.displayedRows = Math.floor(12/this.states.length);
  }

  filterTask(task: Task, state:State){
    return task.state == state;
  }

  open(task: Task) {
    const dialogRef = this.dialog.open(TaskdialogComponent, {
      width: '800px',
      data: task
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }
}

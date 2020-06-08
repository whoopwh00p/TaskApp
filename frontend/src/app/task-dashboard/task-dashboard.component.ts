import { Component, OnInit } from '@angular/core';
import { Task } from '../model/Task';
import { TaskService } from '../task.service';

@Component({
  selector: 'app-task-dashboard',
  templateUrl: './task-dashboard.component.html',
  styleUrls: ['./task-dashboard.component.css']
})
export class TaskDashboardComponent implements OnInit {

  public tasks: Task[][];

  constructor(private taskService: TaskService) { }

  ngOnInit(): void {
    for(let i in this.taskService.getTasks())
    {
      console.log(i);
      let task = this.taskService.getTasks()[i];
      this.tasks[task.state][i] = task;
    }
  }

}

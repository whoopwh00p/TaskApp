import { Component, OnInit } from '@angular/core';
import { Task } from '../model/Task';
import { TaskService } from '../task.service';

@Component({
  selector: 'app-task-dashboard',
  templateUrl: './task-dashboard.component.html',
  styleUrls: ['./task-dashboard.component.css']
})
export class TaskDashboardComponent implements OnInit {

  public tasks: Task[];
  public states: number[];

  constructor(private taskService: TaskService) { }

  ngOnInit(): void {
    this.states = new Array<number>();
    this.tasks = this.taskService.getTasks();

    for(let task of this.tasks) {
      if(!this.states.includes(task.state)) {
        this.states.push(task.state);
      }
    }
    this.states.sort((a,b) => a-b);
  }

  filterTask(task: Task, state:number){
    return task.state == state;
  }
}

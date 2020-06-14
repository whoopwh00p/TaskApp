import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import {State} from '../model/State';
import { TaskService } from '../task.service';
import { Task } from '../model/Task';

@Component({
  selector: 'app-create-task',
  templateUrl: './create-task.component.html',
  styleUrls: ['./create-task.component.css']
})
export class CreateTaskComponent implements OnInit {

  form: FormGroup;

  defaultTask: Task;

  states:State[] = [State.TODO,State.IN_PROGRESS,State.DONE];

  constructor(private formBuilder: FormBuilder, private taskService: TaskService) { 
    this.defaultTask = {
      'id': 0,
      'name': '',
      'description': '',
      'state': State.TODO
    }
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group(this.defaultTask);
  }

  createTask(): void {
    this.taskService.createTask(this.form.value);
    this.form.reset(this.defaultTask);
  }

}

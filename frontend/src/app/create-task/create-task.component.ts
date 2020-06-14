import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import {State} from '../model/State';

@Component({
  selector: 'app-create-task',
  templateUrl: './create-task.component.html',
  styleUrls: ['./create-task.component.css']
})
export class CreateTaskComponent implements OnInit {

  form: FormGroup;

  states:State[] = [State.TODO,State.IN_PROGRESS,State.DONE];

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group( {
      'taskName': '',
      'taskDescription': '',
      'taskState': State.TODO
    })
  }

}

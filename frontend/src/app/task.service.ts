import { Injectable } from '@angular/core';
import { Task } from './model/Task';
import { State } from './model/State';

@Injectable({
  providedIn: 'root'
})

export class TaskService {
  TASKS: Task[] = [
    { id: 1, name: 'Task 1' , description: 'Description 1', state: State.TODO}, 
    { id: 2, name: 'Task 2' , description: 'Description 2', state: State.DONE}, 
    { id: 3, name: 'Task 3' , description: 'Description 3', state: State.IN_PROGRESS}, 
    { id: 4, name: 'Task 4' , description: 'Description 4', state: State.TODO}, 
    { id: 5, name: 'Task 5' , description: 'Description 5', state: State.TODO},
    { id: 6, name: 'Task 6' , description: 'Description 6', state: State.TODO},
    { id: 6, name: 'Task 6' , description: 'Description 6', state: State.DONE}
  ];
  constructor() { }

  getTasks(): Task[] {
    return this.TASKS;
  }
}

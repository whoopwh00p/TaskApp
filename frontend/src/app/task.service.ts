import { Injectable } from '@angular/core';
import { Task } from './model/Task';

@Injectable({
  providedIn: 'root'
})

export class TaskService {
  // 0 = todo, 1=in progress, 2=done
  TASKS: Task[] = [
    { id: 1, name: 'Task 1' , description: 'Description 1', state: 2}, 
    { id: 2, name: 'Task 2' , description: 'Description 2', state: 0}, 
    { id: 3, name: 'Task 3' , description: 'Description 3', state: 1}, 
    { id: 4, name: 'Task 4' , description: 'Description 4', state: 4}, 
    { id: 5, name: 'Task 5' , description: 'Description 5', state: 4},
    { id: 6, name: 'Task 6' , description: 'Description 6', state: 0},
    { id: 6, name: 'Task 6' , description: 'Description 6', state: 4}
  ];
  constructor() { }

  getTasks(): Task[] {
    return this.TASKS;
  }
}

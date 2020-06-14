import { Pipe, PipeTransform } from '@angular/core';
import { Task } from './model/Task';
import { State } from './model/State';

@Pipe({
  name: 'taskfilter'
})
export class TaskfilterPipe implements PipeTransform {

  transform(tasks: Task[], state: State): any {
    if(tasks != null) {
      return tasks.filter(task => task.state == state);
    }
    return;
  }

}

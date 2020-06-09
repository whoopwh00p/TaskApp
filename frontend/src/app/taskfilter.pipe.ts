import { Pipe, PipeTransform } from '@angular/core';
import { Task } from './model/Task';

@Pipe({
  name: 'taskfilter'
})
export class TaskfilterPipe implements PipeTransform {

  transform(tasks: Task[], state: number): any {
    return tasks.filter(task => task.state == state);
  }

}

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Observable, of, Subject } from 'rxjs';
import { Task } from './model/Task';
import { ProjectService } from './project.service';
import { Project } from './model/Project';

@Injectable({
  providedIn: 'root'
})

export class TaskService {

  private baseUrl = 'http://localhost:8080/projects/';  
  private path = '/tasks/'
  private project: Project;

  private _refreshNeeded$ = new Subject<void>();

  constructor(private http: HttpClient, private projectService: ProjectService) { 
    this.projectService.refreshNeeded$.subscribe(result => {
      this.project = result;
      this._refreshNeeded$.next();
    })
  }

  get refreshNeeded$() {
    return this._refreshNeeded$;
  }

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.baseUrl+this.project.id+this.path)
          .pipe(
            tap(_ => this.log('fetched tasks')),
            catchError(this.handleError<Task[]>('getTasks', []))
          );
  }

  createTask(task:Task) {
    this.http.post<Task>(this.baseUrl+this.project.id+this.path, {
      'name': task.name,
      'description': task.description,
      'state': task.state.toString()
    }).pipe(
      tap(_ => {
        this.log('create task');
        this._refreshNeeded$.next();
      }),
      catchError(this.handleError<Task>('create task'))
    ).subscribe();
  }

  updateTask(task:Task) {
    this.http.put<Task>(this.baseUrl+this.project.id+this.path+task.id, {
      'name': task.name,
      'description': task.description,
      'state': task.state.toString()
    }).pipe(
      tap(_ => {
        this.log('update task');
        this._refreshNeeded$.next();
      }),
      catchError(this.handleError<Task>('update task'))
    ).subscribe();
  }

  deleteTask(task:Task) {
    this.http.delete<Task>(this.baseUrl+this.project.id+this.path+task.id).pipe(
      tap(_ => {
        this.log('delete task');
        this._refreshNeeded$.next();
      }),
      catchError(this.handleError<Task>('delete task'))
    ).subscribe();
  }

  /**
 * Handle Http operation that failed.
 * Let the app continue.
 * @param operation - name of the operation that failed
 * @param result - optional value to return as the observable result
 */
private handleError<T>(operation = 'operation', result?: T) {
  return (error: any): Observable<T> => {

    // TODO: send the error to remote logging infrastructure
    console.error(error); // log to console instead

    // TODO: better job of transforming error for user consumption
    this.log(`${operation} failed: ${error.message}`);

    // Let the app keep running by returning an empty result.
    return of(result as T);
  }
}

  private log(message: string) {
    console.log(message);
  }
}

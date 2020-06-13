import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import { Task } from './model/Task';
import { State } from './model/State';

@Injectable({
  providedIn: 'root'
})

export class TaskService {

  private baseUrl = 'http://localhost:8080';  

  constructor(private http: HttpClient) { }

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.baseUrl+"/projects/1/tasks/")
          .pipe(
            tap(_ => this.log('fetched tasks')),
            catchError(this.handleError<Task[]>('getTasks', []))
          );
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

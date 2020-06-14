import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Observable, of, Subject } from 'rxjs';
import { Project } from './model/Project';


@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private projectUrl = 'http://localhost:8080/projects/';  

  private _refreshNeeded$ = new Subject<Project>();
  private selectedProject: Project;

  constructor(private http: HttpClient) { }

  get refreshNeeded$() {
    return this._refreshNeeded$;
  }

  getProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(this.projectUrl)
        .pipe(
          tap(_ => this.log('fetched projects')),
          catchError(this.handleError<Project[]>('getProjects', []))
        );
  }

  setSelectedProject(project: Project): void {
    this.selectedProject = project;
    this._refreshNeeded$.next(project);
  }

  getSelectedProject() : Project {
    return this.selectedProject;
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

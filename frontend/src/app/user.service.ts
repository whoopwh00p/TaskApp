import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Observable, of, Subject } from 'rxjs';
import { User } from './model/User';
import { Project } from './model/Project';
import { AuthService } from './auth/auth.service';

@Injectable({
  providedIn: 'root'
})

export class UserService {

  private baseUrl = 'http://localhost:8080';  
  private path = '/users'
  private project: Project;

  private _refreshNeeded$ = new Subject<void>();

  constructor(private http: HttpClient, private authService: AuthService) { 
  }

  getUser(): Observable<User[]> {
    return this.http.get<User[]>(this.baseUrl+this.path,
      {
        headers: new HttpHeaders().set('Authorization', `Bearer ${this.authService.accessToken}`)
      })
          .pipe(
            tap(_ => this.log('fetched tasks')),
            catchError(this.handleError<User[]>('getUser', []))
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

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import { User } from './model/User';

@Injectable({
  providedIn: 'root'
})

export class UserService {

  private baseUrl = 'http://localhost:8080';  
  private path = '/users'
  private accessToken: String;
  private userName: String;
  private users: User[];

  constructor(private http: HttpClient) { 
    
  }

  public setUserInformation(userName: String, userId: String, accessToken: String) {
    this.userName = userName;
    this.accessToken = accessToken;
    this.getUsers().subscribe(users => {
      if(this.users == null || this.users == undefined) {
        this.users = users;
        if(!this.userExists(userName)) {
          console.log(userName+" does not exist");
          this.createUser(userName,userId).subscribe(result => console.log(userName+" created"));
        }
      }
    });

  }

  private userExists(userName: String):boolean {
    return this.users.find(u => u.name == userName) != null;
  }

  private createUser(userName: String, userId: String) {
    let newUser: User = {
      'id': userId,
      'name': userName
    } 
    return this.http.post<User>(this.baseUrl+this.path, newUser,
      {
        headers: new HttpHeaders().set('Authorization', `Bearer ${this.accessToken}`)
      })
        .pipe(
          tap(_ => this.log('fetched tasks')),
          catchError(this.handleError<User[]>('getUser', []))
      );
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.baseUrl+this.path,
      {
        headers: new HttpHeaders().set('Authorization', `Bearer ${this.accessToken}`)
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

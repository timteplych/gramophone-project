import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../_models';
import {BehaviorSubject, Observable} from 'rxjs';
import {API_URL} from '../../environments/environment';

@Injectable({providedIn: 'root'})
export class UsersService {
  currentUsersSubj: BehaviorSubject<User[]>;
  public currentUsers: Observable<User[]>;


  constructor(private http: HttpClient) {
    this.currentUsersSubj = new BehaviorSubject<User[]>([]);
    this.currentUsers = this.currentUsersSubj.asObservable();
    this.getListOfUsers();
  }

  getListOfUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${API_URL}/users`);
  }

}

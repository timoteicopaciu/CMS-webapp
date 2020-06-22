
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from './user.model';
import {Observable} from 'rxjs';
import {Conference} from './conference.model';
import {Permission} from "./permission.model";


@Injectable()
export class UserService {
  private userUrl = 'http://localhost:8080/user';

  constructor(private httpClient: HttpClient) {
  }

  login(username: string, password: string): Observable<User>{
    return this.httpClient
      .post<User>(this.userUrl + '/checkUser', [username, password]);
  }

  register(name: string, username: string, password: string, email: string, affiliation: string): Observable<User>{
    let user: User = new User(null, username, name, email, affiliation, password, null, false, false, false);

    return this.httpClient
      .post<User>(this.userUrl + '/saveUser', user);
  }

  getUserByEmailAddress(emailAddress: string): Observable<User>{
    return this.httpClient.post<User>(this.userUrl + '/getUserByEmail', emailAddress);
  }

  getPermissionForUserInConference(userID: number, conferenceID: number): Observable<Permission>{
    return this.httpClient.post<Permission>(this.userUrl + '/getPermissionForUserInConference', [userID, conferenceID]);
  }

  getAllNonSCUsersAndNonPCMembers(conferenceID: number): Observable<User[]>{
    return this.httpClient.post<User[]>(this.userUrl + "/getNonSCMembersAndNonPCMembers", conferenceID);
  }

  checkIfIsPCMemberInAnyConference(userID:number): Observable<boolean>{
    return this.httpClient
      .post<boolean>(this.userUrl + "/checkIfIsPCMemberInAnyConference", userID);
  }

  updateUser(user: User): Observable<User>{
    return this.httpClient
      .post<User>(this.userUrl + "/updateUser",user);
  }

  getSCMembers(): Observable<User[]>{
    return this.httpClient.get<User[]>(this.userUrl + "/getSCMembers");
  }
}

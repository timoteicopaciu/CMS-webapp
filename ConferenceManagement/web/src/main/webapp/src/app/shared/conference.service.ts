// @ts-ignore
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from './user.model';
import {Observable} from 'rxjs';
import {Conference} from './conference.model';
import {map} from "rxjs/operators";
import {Permission} from "./permission.model";
import {UserService} from "./user.service";


@Injectable()
export class ConferenceService {
  private conferenceUrl = 'http://localhost:8080/conference';

  constructor(private httpClient: HttpClient,
              private userService: UserService) {
  }

  saveConference(conference: Conference): Observable<Conference>{
    console.log(conference);
    return this.httpClient.post<Conference>(this.conferenceUrl + '/saveConference', conference);
  }

  getConferences(): Observable<Conference[]>{
    return this.httpClient
      .get<Conference[]>(this.conferenceUrl + '/getConferences');
  }

  getConference(conferenceID: number): Observable<Conference>{
    return this.getConferences().pipe(map(conferences => conferences.find(conference => conference.id === conferenceID)));
  }

  updateConference(conference: Conference): Observable<Conference>{
    return this.httpClient.post<Conference>(this.conferenceUrl + '/updateConference', conference);
  }

  setCurrentConference(conference:Conference):void{
    sessionStorage.setItem("conference", JSON.stringify(conference));

    const user: User = JSON.parse(sessionStorage.getItem("user"));

    if(user != null)
      this.userService.getPermissionForUserInConference(user.id, conference.id).subscribe(permission => {
        sessionStorage.setItem("permission", JSON.stringify(permission));
      });
  }

  addPermission(permission: Permission) :Observable<Permission>{
    return this.httpClient
      .post<Permission>(this.conferenceUrl + "/saveOrUpdatePermission", permission);
  }
}

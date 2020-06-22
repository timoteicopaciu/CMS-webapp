
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Section} from "./section.model";
import {Observable} from "rxjs";
import {User} from "./user.model";
import {Proposal} from "./proposal.model";

@Injectable()
export class SectionService {
  private sectionUrl = 'http://localhost:8080/section';

  constructor(private httpClient: HttpClient) {
  }

  addSection(section: Section): Observable<Section>{
    console.log(section);
    return this.httpClient.post<Section>(this.sectionUrl + "/saveSection", section);
  }

  getSectionsForConference(conferenceID: number): Observable<Section[]> {
    return this.httpClient
      .post<Section[]>(this.sectionUrl + "/getSectionsForConference", conferenceID);
  }

  getSectionByID(sectionID: number): Observable<Section> {
    return this.httpClient
      .post<Section>(this.sectionUrl + "/getSectionByID", sectionID);
  }

  getCandidatesForSectionChair(conferenceID: number): Observable<User[]>{
    return this.httpClient.post<User[]>(this.sectionUrl + "/getCandidatesForSectionChair", conferenceID);
  }

  updateSectionProposals(section: Section): Observable<Section>{
    return this.httpClient.post<Section>(this.sectionUrl + "/updateSectionProposals", section);
  }

  getUnassignedAndAcceptedProposals(): Observable<Proposal[]>{
    return this.httpClient.get<Proposal[]>(this.sectionUrl + "/getUnassignedAndAcceptedProposals");
  }

  addParticipantInSection(sectionID: number, userID: number): Observable<void>{
    return this.httpClient.post<void>(this.sectionUrl + "/addParticipantInSection", [sectionID, userID]);
  }

  checkParticipantInSection(sectionID: number, userID: number): Observable<boolean>{
    return this.httpClient.post<boolean>(this.sectionUrl + "/checkParticipantInSection",[sectionID, userID]);
  }
}

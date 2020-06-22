import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {Conference} from "./conference.model";
import {Proposal} from "./proposal.model";
import {User} from "./user.model";
import {ProposalAuthor} from "./proposalAuthor.model";
import {catchError, map} from "rxjs/operators";
import {Bidding} from "./bidding.model";
import {patchTimer} from "zone.js/lib/common/timers";
import {Review} from "./review.model";

@Injectable()
export class ProposalService{
  private proposalUrl = 'http://localhost:8080/proposal';

  constructor(private httpClient: HttpClient) {
  }

  getProposalsForConference(conferenceID: number): Observable<Proposal[]>{
    return this.httpClient
      .post<Proposal[]>(this.proposalUrl + '/getProposalsForConference', conferenceID);
  }

  getProposalForConference(conferenceID: number, proposalID: number){
    return this.getProposalsForConference(conferenceID).pipe(map(proposals => proposals.find(proposal => proposal.id === proposalID)));
  }

  addProposal(name: string, keywords:string, topics: string, conference:Conference):
    Observable<Proposal>{
    let proposal : Proposal = new Proposal(null, name, keywords, topics, "", "", "", "pending review", conference);
    return this.httpClient
      .post<Proposal>(this.proposalUrl + "/saveProposal", proposal);
  }

  addAuthor(proposalAuthor: ProposalAuthor) {
    this.httpClient
      .post(this.proposalUrl + "/addAuthorForProposal", proposalAuthor)
      .subscribe(x => console.log(x));
  }

  updateProposal(proposal: Proposal): Observable<Proposal>{
    return this.httpClient.post<Proposal>(this.proposalUrl + "/updateProposal", proposal);
  }

  getProposalsIDsForUser(userID:number):Observable<number[]>{
    return this.httpClient
      .post<number[]>(this.proposalUrl + "/getProposalsIDsForUser", userID);
  }

  addBidding(bidding: Bidding): Observable<Bidding>{
    return this.httpClient
      .post<Bidding>(this.proposalUrl + "/bidProposal", bidding);
  }

  addReview(review: Review): Observable<Review>{
    console.log(review);
    return this.httpClient
      .post<Review>(this.proposalUrl + "/reviewProposal", review);
  }

  getUnbiddenProposalIDs(userID: number): Observable<number[]>{
    return this.httpClient.post<number[]>(this.proposalUrl + "/getUnbiddenProposalsIDs", userID);
  }

  getUsersForReviewingAProposal(proposalID:number):Observable<User[]>{
    return this.httpClient
      .post<User[]>(this.proposalUrl + "/getUsersForReviewingAProposal", proposalID);

  }

  getReviewersForProposal(proposalID: number): Observable<User[]>{
    return this.httpClient.post<User[]>(this.proposalUrl + "/getReviewersForProposal", proposalID);
  }

  updateReview(review: Review): Observable<Review>{
    return this.httpClient
      .post<Review>(this.proposalUrl + "/updateReview", review);
  }

  getReviewByUserAndProposal(userID: number, proposalID: number): Observable<Review>{
    return this.httpClient.post<Review>(this.proposalUrl + "/getReviewByUserAndProposal", [userID, proposalID]);
  }

  getReviewsForProposal(proposalID: number): Observable<Review[]>{
    return this.httpClient.post<Review[]>(this.proposalUrl + "/getReviewsForProposal", proposalID);
  }

  checkProposalStatus(proposalID: number, conferenceLevel: number) : Observable<string>{
    return this.httpClient
      .post<string>(this.proposalUrl + "/checkProposalStatus", [proposalID, conferenceLevel]);
  }

  resetReviewsForProposal(proposalID: number): Observable<string>{
    return this.httpClient.post<string>(this.proposalUrl + "/resetReviewsForProposal", proposalID);
  }

  getProposalsForIDs(proposalIDs: number[]): Observable<Proposal[]>{
    return this.httpClient.post<Proposal[]>(this.proposalUrl + "/getProposalsForIDs", proposalIDs);
  }

  getAuthorsForProposal(proposalID: number): Observable<User[]>{
    return this.httpClient.post<User[]>(this.proposalUrl + "/getAuthorsForProposal", proposalID);
  }

  uploadAbstract(proposalID: number, abstract: File): Observable<boolean> {
    const formData = new FormData();
    formData.append('file', abstract);
    return this.httpClient.put<boolean>(this.proposalUrl + '/uploadAbstractPaper/'+ `${proposalID}`, formData)
      .pipe(
        map(result => Boolean(result))
      );
  }

  uploadFull(proposalID: number, full: File): Observable<boolean> {
    const formData = new FormData();
    formData.append('file', full);
    return this.httpClient.put<boolean>(this.proposalUrl + '/uploadFullPaper/'+ `${proposalID}`, formData)
      .pipe(
        map(result => Boolean(result))
      );
  }

  uploadPresentation(proposalID: number, presentationFile: File): Observable<boolean> {
    const formData = new FormData();
    formData.append('file', presentationFile);
    return this.httpClient.put<boolean>(this.proposalUrl + '/uploadPresentation/'+ `${proposalID}`, formData)
      .pipe(
        map(result => Boolean(result))
      );
  }
}

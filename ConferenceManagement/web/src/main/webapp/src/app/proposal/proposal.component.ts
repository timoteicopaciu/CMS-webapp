import {Component, Input, OnInit} from '@angular/core';
import {Proposal} from "../shared/proposal.model";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {switchMap} from "rxjs/operators";
import {User} from "../shared/user.model";
import {Conference} from "../shared/conference.model";
import {ProposalService} from "../shared/proposal.service";
import {Bidding} from "../shared/bidding.model";
import {Permission} from "../shared/permission.model";
import {Review} from "../shared/review.model";
import {Location} from "@angular/common";
import {URL} from "url";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-proposal',
  templateUrl: './proposal.component.html',
  styleUrls: ['./proposal.component.css']
})
export class ProposalComponent implements OnInit {

  @Input() proposal: Proposal = null;
  user : User = JSON.parse(sessionStorage.getItem("user"));
  conference : Conference = JSON.parse(sessionStorage.getItem("conference"))
  currentDate = new Date();
  unbiddenIDs: number[] = JSON.parse(sessionStorage.getItem("biddingIDs"));
  permission: Permission = JSON.parse(sessionStorage.getItem("permission"));
  usersToReview: User[] = null;
  showForReview: boolean = false;
  qualifier: string = "";
  review: Review = null;
  reviewsByAllUsers: Review[] = null;
  writtenProposals :number[] = JSON.parse(sessionStorage.getItem("proposalsIDs"));
  reviewersForProposal : User[] = null;
  wantToSeReviewers: boolean = false;
  chairToViewReviews: boolean = false;
  authorToViewReviews: boolean = false;
  authorsForProposal: User[] = null;
  abstractUrl : URL;
  fullUrl : URL;
  presentationURL: URL;

  constructor(private route:ActivatedRoute,
              private proposalService: ProposalService,
              private location: Location,
              private router: Router,
              private sanitizer: DomSanitizer) { }

  ngOnInit(): void {

    this.route.params.subscribe(params => {
      if(+params["conferenceID"] != this.conference.id) {
        this.router.navigate(["conference", this.conference.id, "proposals", +params['proposalID'], "details"]);
      }
    });
    this.route.params.pipe(switchMap((params: Params) =>
      this.proposalService.getProposalForConference(+params['conferenceID'], +params['proposalID'])))
      .subscribe(proposal => {this.proposal = proposal;

        if(this.proposal.abstractPaperURL !== "") {
          this.abstractUrl =
            this.sanitizer.bypassSecurityTrustResourceUrl('http://localhost:8080/proposal/getAbstractPaper/' + this.proposal.id);

        }
        if(this.proposal.fullPaperURL !== "")
          this.fullUrl =
            this.sanitizer.bypassSecurityTrustResourceUrl('http://localhost:8080/proposal/getFullPaper/' + this.proposal.id);

        if(this.proposal.presentationURL !== "")
          this.presentationURL =
            this.sanitizer.bypassSecurityTrustResourceUrl('http://localhost:8080/proposal/getPresentation/' + this.proposal.id);

        this.proposalService.getAuthorsForProposal(this.proposal.id)
          .subscribe(authors => this.authorsForProposal = authors);

        if(this.user !== null) {
          this.proposalService.getReviewByUserAndProposal(this.user.id, this.proposal.id).subscribe(
            review => {
              this.review = review;
            }
          );
          this.proposalService.getReviewsForProposal(this.proposal.id).subscribe(reviewers =>
            this.reviewsByAllUsers = reviewers);
          this.proposalService.getReviewersForProposal(this.proposal.id).subscribe(reviewers => {
            this.reviewersForProposal = reviewers;
          })
        }
      });
  }

  bidProposal(proposal: Proposal, accepted: boolean): void{
    const bidding: Bidding = new Bidding(null, accepted, this.user, proposal);
    this.proposalService.addBidding(bidding).subscribe();
    const index = this.unbiddenIDs.indexOf(proposal.id);
    if (index > -1) {
      this.unbiddenIDs.splice(index, 1);
    }
    sessionStorage.setItem("biddingIDs", JSON.stringify(this.unbiddenIDs));
  }

  canDoBidding(proposalID: number):boolean{
    if(this.permission === null || this.writtenProposals.indexOf(proposalID) !== -1){
      return false;
    }
    return this.permission.isPCMember === true &&
      this.unbiddenIDs.indexOf(this.proposal.id) !== -1 && this.currentDate < this.conference.biddingDeadline
  }

  showList() {
    this.proposalService.getUsersForReviewingAProposal(this.proposal.id)
      .subscribe(users => {this.usersToReview = users;});
  }

  setAsReviewer(user: User) {
    this.proposalService.
    addReview(new Review(null, user, this.proposal, '', ''))
      .subscribe();
    this.usersToReview.splice(this.usersToReview.indexOf(user), 1);
    this.reviewersForProposal.push(user);
  }

  reviewProposal(recommendation: string) {
    if(this.review.qualifier === ""){
      this.review.qualifier = this.qualifier;
    }

    this.review.recommendation = recommendation;
    this.proposalService.updateReview(this.review)
      .subscribe(review =>{

        let isPresent = false;
        this.reviewsByAllUsers.forEach(currentReview =>
        {
          if(currentReview.id == review.id){
            isPresent = true;
            currentReview.recommendation = review.recommendation;
          }
        })

        if(!isPresent) {
          this.reviewsByAllUsers.push(review);
          this.proposalService.checkProposalStatus(this.proposal.id, this.conference.level)
            .subscribe();
        }
      });
    this.showForReview = false;


  }

  goBack() {
    this.location.back();
  }

  userInReviewers(userId:number):boolean{
    let wasThere : boolean = false;
    this.reviewersForProposal.forEach(user => {
      if(user.id == userId)
        wasThere = true;
    });
    return wasThere;
  }

  updateStatusByChair(status: string): void{
    this.proposal.status = status;
    this.proposalService.updateProposal(this.proposal).subscribe();
  }

  resetProposalReviews() {
    this.proposal.status = "pending review";
    this.reviewsByAllUsers = null;
    this.proposalService.resetReviewsForProposal(this.proposal.id).subscribe();
  }

  authorCanSeeReviews() {
    return this.proposal.status === 'Accepted' && this.writtenProposals.indexOf(this.proposal.id) !== -1
  }
}

import { Component, OnInit } from '@angular/core';
import {ProposalService} from "../shared/proposal.service";
import {ConferenceService} from "../shared/conference.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../shared/user.service";
import {ProposalAuthor} from "../shared/proposalAuthor.model";
import {Permission} from "../shared/permission.model";
import {Conference} from "../shared/conference.model";
import {User} from "../shared/user.model";
import {Proposal} from "../shared/proposal.model";

@Component({
  selector: 'app-proposal-new',
  templateUrl: './proposal-new.component.html',
  styleUrls: ['./proposal-new.component.css']
})
export class ProposalNewComponent implements OnInit {
  user : User = JSON.parse(sessionStorage.getItem("user"));
  conference : Conference = JSON.parse(sessionStorage.getItem("conference"));
  wantToAddAuthor :Boolean = false;
  proposal: Proposal;

  constructor(private conferenceService: ConferenceService,
              private proposalService: ProposalService,
              private userService : UserService,
              private router: Router,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if(+params["conferenceID"] != this.conference.id) {
        this.router.navigate(["conference/", this.conference.id, "newProposal"]);
      }
    });
  }

  addProposal(name: string, keywords: string, topics: string) {
    if(name === '' || keywords === '' || topics === ''){
      alert("Please enter the name, keywords and topics");
      return;
    }
    this.proposalService.addProposal(name, keywords, topics,
      this.conference)
      .subscribe(proposal => {
        alert("Proposal " + proposal.name + " was successfully added!")
        this.proposal = proposal;
        this.proposalService.addAuthor(new ProposalAuthor(null, this.user, proposal));
        this.conferenceService.addPermission(new Permission(null, this.conference ,
          this.user, true, null, null))
          .subscribe();

        let writtenProposals :number[] = JSON.parse(sessionStorage.getItem("proposalsIDs"));
        writtenProposals.push(proposal.id);
        sessionStorage.setItem("proposalsIDs", JSON.stringify(writtenProposals));
      });
  }

  addAuthor(emailAddress: string) {
    this.wantToAddAuthor = false;
    this.userService.getUserByEmailAddress(emailAddress).subscribe(user => {
      if (user === null || user.isChair === true || user.isCoChair === true || user.isSCMember === true){
        alert("Invalid email address! Author must have an account and mustn't be a Chair/Co-Chair or a Steering Committee Member!");
      }
      else {
        this.proposalService.addAuthor(new ProposalAuthor(null, user, this.proposal));
        this.conferenceService.addPermission(new Permission(null, this.conference,
          user, true, null, null))
          .subscribe();
      }
    });
  }

  goBack() {
    this.router.navigate(["conference/", this.conference.id]);
  }
}

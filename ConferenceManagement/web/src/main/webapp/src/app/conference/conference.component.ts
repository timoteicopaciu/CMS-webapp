import {Component, Input, OnInit} from '@angular/core';
import {Conference} from "../shared/conference.model";
import {ConferenceService} from "../shared/conference.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {Location} from "@angular/common";
import {switchMap} from "rxjs/operators";
import {UserService} from "../shared/user.service";
import {User} from "../shared/user.model";
import {Observable} from "rxjs";
import {ProposalService} from "../shared/proposal.service";
import {Permission} from "../shared/permission.model";

@Component({
  selector: 'app-conference',
  templateUrl: './conference.component.html',
  styleUrls: ['./conference.component.css']
})
export class ConferenceComponent implements OnInit {

  @Input() conference: Conference;
  user: User = JSON.parse(sessionStorage.getItem("user"));
  users: User[] = null;
  storedConference: Conference = JSON.parse(sessionStorage.getItem("conference"));

  constructor(private conferenceService: ConferenceService,
              private route: ActivatedRoute,
              private router: Router,
              private location: Location,
              private userService: UserService) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if(+params["conferenceID"] != this.storedConference.id) {
        this.router.navigate(["conference", this.storedConference.id]);
      }
    });
    this.route.params.pipe(switchMap((params: Params) => this.conferenceService.getConference(+params['conferenceID'])))
      .subscribe(conference => this.conference = conference);
  }

  goBack(): void{
    this.location.back();
  }

  allowAsPCMember(user: User): void{
    this.conferenceService.addPermission(new Permission(null, this.conference,
      user, null, true, null))
      .subscribe();
    const index = this.users.indexOf(user);
    if (index > -1) {
      this.users.splice(index, 1);
    }
  }

  goToCreateProposal(): void{
    this.router.navigate(["conference", this.conference.id,"newProposal"]);
  }

  goToProposals(): void{
    this.router.navigate(["conference", this.conference.id,"proposals"]);
  }

  updateConference(): void{
    var date1 = (<HTMLInputElement>document.getElementById("startDate")).value;
    var date2 = (<HTMLInputElement>document.getElementById("endDate")).value;
    var date3 = (<HTMLInputElement>document.getElementById("biddingDeadline")).value;
    this.conference.startDate = new Date(date1);
    this.conference.endDate = new Date(date2);
    this.conference.biddingDeadline = new Date(date3)
    this.conferenceService.updateConference(this.conference).subscribe(
      conference => {
        sessionStorage.setItem("conference", JSON.stringify(conference));
      }
    );
  }

  getAllUsers(): void{
    this.userService.getAllNonSCUsersAndNonPCMembers(this.conference.id).subscribe(users => this.users = users);
  }

  isChairOrCoChair():boolean{
    return this.user.isChair === true || this.user.isCoChair === true;
  }

  goToSections() {
    this.router.navigate(["conference", this.conference.id,"sections"]);
  }

  goToCreateSection() {
    this.router.navigate(["conference", this.conference.id,"newSection"]);
  }
}

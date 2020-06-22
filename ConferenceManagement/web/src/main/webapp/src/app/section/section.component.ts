import {Component, Input, OnInit} from '@angular/core';
import {SectionService} from "../shared/section.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {Location} from "@angular/common";
import {Section} from "../shared/section.model";
import {switchMap} from "rxjs/operators";
import {Proposal} from "../shared/proposal.model";
import {ProposalService} from "../shared/proposal.service";
import {User} from "../shared/user.model";
import {Conference} from "../shared/conference.model";

@Component({
  selector: 'app-section',
  templateUrl: './section.component.html',
  styleUrls: ['./section.component.css']
})
export class SectionComponent implements OnInit {

  @Input() section: Section;
  proposalsToBePresented: Proposal[] = null;
  proposalsToBeAssigned: Proposal[] = null;
  wantToAssignProposals: boolean = false;
  user: User = JSON.parse(sessionStorage.getItem("user"));
  proposalIDs: number[] = [];
  conference: Conference = JSON.parse(sessionStorage.getItem("conference"));
  alreadyParticipated: boolean = null;

  constructor(private sectionService: SectionService,
              private route: ActivatedRoute,
              private location: Location,
              private proposalService: ProposalService,
              private router: Router) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if(+params["conferenceID"] != this.conference.id) {
        this.router.navigate(["conference", this.conference.id, "sections", +params['sectionID']]);
      }
    });
    this.route.params.pipe(switchMap((params: Params) =>
      this.sectionService.getSectionByID(+params['sectionID'])))
      .subscribe(section => {
        this.section = section;
        this.sectionService.getUnassignedAndAcceptedProposals()
          .subscribe( proposals => this.proposalsToBeAssigned = proposals);
        if(this.user !== null)
          this.sectionService.checkParticipantInSection(this.section.id, this.user.id)
            .subscribe( participated => {this.alreadyParticipated = participated;});
        this.proposalService.getProposalsForIDs(section.proposalIDs)
          .subscribe(proposals => {this.proposalsToBePresented = proposals;})
      });
  }

  goBack(): void{
    this.location.back();
  }

  isChairOrCoChair():boolean{
    return this.user.isChair === true || this.user.isCoChair === true;
  }

  assignProposal(proposal: Proposal): void {
    this.proposalIDs.push(proposal.id);
    this.proposalsToBePresented.push(proposal);
    const index = this.proposalsToBeAssigned.indexOf(proposal);
    if (index > -1) {
      this.proposalsToBeAssigned.splice(index, 1);
    }
  }

  assignAllProposals(): void {
    this.section.proposalIDs = this.proposalIDs;
    this.sectionService.updateSectionProposals(this.section).subscribe();
    this.router.navigate(["conference", this.conference.id, "sections"]);
  }

  participateInSection() {
    this.alreadyParticipated = true;
    this.sectionService.addParticipantInSection(this.section.id, this.user.id).subscribe();
  }
}

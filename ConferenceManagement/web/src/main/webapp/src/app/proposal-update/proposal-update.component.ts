import {Component, ElementRef, Input, OnInit, Sanitizer, ViewChild} from '@angular/core';
import {ProposalService} from "../shared/proposal.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {Proposal} from "../shared/proposal.model";
import {switchMap} from "rxjs/operators";
import {Location} from "@angular/common";
import {User} from "../shared/user.model";
import {Conference} from "../shared/conference.model";
import {ProposalAuthor} from "../shared/proposalAuthor.model";
import {Permission} from "../shared/permission.model";
import {UserService} from "../shared/user.service";
import {ConferenceService} from "../shared/conference.service";
import {DomSanitizer} from "@angular/platform-browser";
import {HttpClient} from "@angular/common/http";
import {URL} from "url";

@Component({
  selector: 'app-proposal-update',
  templateUrl: './proposal-update.component.html',
  styleUrls: ['./proposal-update.component.css']
})
export class ProposalUpdateComponent implements OnInit {

  @ViewChild('abstractPaperUpload', {static: false}) abstractPaperUpload: ElementRef;
  abstractFile: File = null;
  abstractFileName: string = null;

  @ViewChild('fullPaperUpload', {static: false}) fullPaperUpload: ElementRef;
  fullFile: File = null;
  fullFileName: string = null;

  @ViewChild('presentationUpload', {static: false}) presentationUpload: ElementRef;
  presentationFile: File = null;
  presentationFileName: string = null;

  @Input() proposal: Proposal;
  user : User = JSON.parse(sessionStorage.getItem("user"));
  conference : Conference = JSON.parse(sessionStorage.getItem("conference"));
  wantToAddAuthor :Boolean = false;

  currentDate = new Date();

  constructor(private proposalService: ProposalService,
              private route: ActivatedRoute,
              private location: Location,
              private userService: UserService,
              private conferenceService: ConferenceService,
              private router: Router,
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if(+params["conferenceID"] != this.conference.id) {
        this.router.navigate(["conference", this.conference.id, "proposals", +params['proposalID']]);
      }

      this.proposalService.getProposalForConference(+params['conferenceID'], +params['proposalID'])
        .subscribe(proposal => {
          this.proposal = proposal;
        })
    });
  }

  uploadAbstractButtonPressed() {
    const fileUpload = this.abstractPaperUpload.nativeElement;
      fileUpload.onchange = () => {
      this.abstractFile = fileUpload.files[0];
      this.abstractFileName = this.abstractFile.name;
      this.abstractPaperUpload.nativeElement.value = '';
    };
    fileUpload.click();
  }

  uploadFullButtonPressed() {
    const fileUpload = this.fullPaperUpload.nativeElement;
    fileUpload.onchange = () => {
      this.fullFile = fileUpload.files[0];
      this.fullFileName = this.fullFile.name;
      this.fullPaperUpload.nativeElement.value = '';
    };
    fileUpload.click();
  }

  uploadPresentationButtonPressed() {
    const fileUpload = this.presentationUpload.nativeElement;
    fileUpload.onchange = () => {
      this.presentationFile = fileUpload.files[0];
      this.presentationFileName = this.presentationFile.name;
      this.presentationUpload.nativeElement.value = '';
    };
    fileUpload.click();
  }

  updateProposal(): void {
    if(this.abstractFileName !== null )
      this.proposal.abstractPaperURL = "Abstract" + String(this.proposal.id) + ".pdf";
    if(this.fullFileName !== null)
      this.proposal.fullPaperURL = "FullPaper" + String(this.proposal.id) + ".pdf";
    if(this.presentationFileName !== null)
      this.proposal.presentationURL = "Presentation" + String(this.proposal.id) + ".pdf";

    this.proposalService.uploadAbstract(this.proposal.id, this.abstractFile).subscribe(result => console.log(result))
    this.proposalService.uploadFull(this.proposal.id, this.fullFile).subscribe(result => console.log(result))
    this.proposalService.uploadPresentation(this.proposal.id, this.presentationFile).subscribe(result => console.log(result))
    this.proposalService.updateProposal(this.proposal).subscribe();
  }

  goBack() {
    this.location.back();
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



}

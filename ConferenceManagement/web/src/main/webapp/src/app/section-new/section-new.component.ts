import { Component, OnInit } from '@angular/core';
import {SectionService} from "../shared/section.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Conference} from "../shared/conference.model";
import {User} from "../shared/user.model";
import {Section} from "../shared/section.model";
import {Location} from "@angular/common";
import {ConferenceService} from "../shared/conference.service";
import {Permission} from "../shared/permission.model";

@Component({
  selector: 'app-section-new',
  templateUrl: './section-new.component.html',
  styleUrls: ['./section-new.component.css']
})
export class SectionNewComponent implements OnInit {

  wantsToAddSectionChair: boolean = false;
  conference: Conference = JSON.parse(sessionStorage.getItem("conference"));
  candidatesForSectionChair: User[] = null;
  selectedCandidate: User = null;

  constructor(private sectionService: SectionService,
              private router: Router,
              private route: ActivatedRoute,
              private location: Location,
              private conferenceService: ConferenceService) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if(+params["conferenceID"] != this.conference.id) {
        this.router.navigate(["conference", this.conference.id, "newSection"]);
      }
    });
    this.sectionService.getCandidatesForSectionChair(this.conference.id)
      .subscribe( users => this.candidatesForSectionChair = users);
  }

  saveSection(sectionName: string, user: User) {
    if(sectionName === '' || this.selectedCandidate === null){
      alert("Please enter a section name and assign a section chair!");
      return;
    }
    this.wantsToAddSectionChair = false;
    let section: Section = new Section(null, sectionName, user, null, this.conference);
    this.sectionService.addSection(section).subscribe();
    this.conferenceService.addPermission(new Permission(null, this.conference, user, null, null, true)).subscribe();
    this.router.navigate(["conference", this.conference.id]);
  }

  setAsSectionChair(user: User) {
    this.selectedCandidate = user;
  }

  goBack() {
    this.location.back();
  }
}

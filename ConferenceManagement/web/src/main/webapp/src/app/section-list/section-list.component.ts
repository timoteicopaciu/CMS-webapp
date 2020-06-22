import { Component, OnInit } from '@angular/core';
import {Section} from "../shared/section.model";
import {ActivatedRoute, Router} from "@angular/router";
import {Conference} from "../shared/conference.model";
import {SectionService} from "../shared/section.service";
import {ProposalService} from "../shared/proposal.service";
import {Proposal} from "../shared/proposal.model";
import {Location} from "@angular/common";

@Component({
  selector: 'app-section-list',
  templateUrl: './section-list.component.html',
  styleUrls: ['./section-list.component.css']
})
export class SectionListComponent implements OnInit {

  sectionsForConference: Section[] = null;
  conference: Conference = JSON.parse(sessionStorage.getItem("conference"));

  constructor(private route: ActivatedRoute,
              private router: Router,
              private sectionService: SectionService,
              private location: Location) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if(+params["conferenceID"] != this.conference.id) {
        this.router.navigate(["conference/", this.conference.id, "sections"]);
      }
    });
    this.sectionService.getSectionsForConference(this.conference.id)
      .subscribe(sections => this.sectionsForConference = sections);
  }

  goToDetails(id: number) {
    this.router.navigate(["conference/", this.conference.id, "sections", id]);
  }

  goBack() {
    this.location.back();
  }
}

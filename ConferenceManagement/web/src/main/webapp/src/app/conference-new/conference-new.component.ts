import { Component, OnInit } from '@angular/core';
import {ConferenceService} from "../shared/conference.service";
import {Conference} from "../shared/conference.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-conference-new',
  templateUrl: './conference-new.component.html',
  styleUrls: ['./conference-new.component.css']
})
export class ConferenceNewComponent implements OnInit {

  level: string ="2";
  constructor(private conferenceService: ConferenceService,
              private router: Router) { }

  ngOnInit(): void {
  }

  addConference(name: string, callForPapers: string, startDate: string, endDate: string, bidding: string, abstract: string, full: string){
    if(name === '' || callForPapers === ''){
      alert("Please insert a name and the call for papers!");
      return;
    }

    let emptyList: number[] = [];
    let conference: Conference =
      new Conference(null, name, +this.level, new Date(startDate), new Date(endDate), callForPapers, new Date(abstract), new Date(full), new Date(bidding), emptyList);

    this.conferenceService.saveConference(conference).subscribe();
    this.router.navigate(["conference-list-page"]);
  }

  goBack() {
    this.router.navigate([""]);
  }
}

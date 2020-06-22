import { Component, OnInit } from '@angular/core';
import {ConferenceService} from '../shared/conference.service';
import {Conference} from '../shared/conference.model';
import {Router} from "@angular/router";

@Component({
  selector: 'app-conference-list',
  templateUrl: './conference-list.component.html',
  styleUrls: ['./conference-list.component.css']
})
export class ConferenceListComponent implements OnInit {
  conferences: Conference[];

  constructor(private conferenceService: ConferenceService,
              private router: Router) { }

  ngOnInit(): void {
    this.conferenceService.getConferences()
      .subscribe(conferences => this.conferences = conferences);
  }

  goToConferenceDetails(conference: Conference): void{
    this.conferenceService.setCurrentConference(conference);
    this.router.navigate(["conference/", conference.id]);
  }

}

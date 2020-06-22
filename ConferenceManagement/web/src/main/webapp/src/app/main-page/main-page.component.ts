import { Component, OnInit } from '@angular/core';
import {User} from "../shared/user.model";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

  isPCMemberInSomeConference: string = sessionStorage.getItem("isPCMemberInSomeConference")
  user : User = JSON.parse(sessionStorage.getItem("user"));
  constructor() { }

  ngOnInit(): void {
    if(this.user && this.user.personalWebsite == null && this.isPCMemberInSomeConference == "true"){
      alert("You are PC Member. Please update you personal data!")
    }
  }

}

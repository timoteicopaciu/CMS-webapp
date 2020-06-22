import { Component, OnInit } from '@angular/core';
import {User} from "../shared/user.model";
import {UserService} from "../shared/user.service";
import {Location} from "@angular/common";

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {

  user : User = JSON.parse(sessionStorage.getItem("user"));
  isPCMemberInSomeConference: string = sessionStorage.getItem("isPCMemberInSomeConference");
  constructor(private userService: UserService,
              private location: Location) { }

  ngOnInit(): void {
  }

  updateInfoForPCMember(personalWebsite: string) {
    this.userService.updateUser(new User(this.user.id, null, null, null, null, null, personalWebsite, null, null, null))
      .subscribe(user => {
        sessionStorage.setItem('user', JSON.stringify(user))
        this.user = user;
      })
  }

  goBack() {
    this.location.back();
  }
}

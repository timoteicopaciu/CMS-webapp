import { Component, OnInit } from '@angular/core';
import {UserService} from "../shared/user.service";
import {User} from "../shared/user.model";

@Component({
  selector: 'app-assign-cochair',
  templateUrl: './assign-cochair.component.html',
  styleUrls: ['./assign-cochair.component.css']
})
export class AssignCochairComponent implements OnInit {

  CoChairsCandidates: User[] = []
  CoChairs: User[] = [];
  user: User = JSON.parse(sessionStorage.getItem("user"))
  received: boolean = null;
  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getSCMembers()
      .subscribe( users => {
        users.forEach(user => {
          if(user.isCoChair)
            this.CoChairs.push(user);

          if(!user.isChair && !user.isCoChair)
            this.CoChairsCandidates.push(user);
        })
        this.received = true;
      });
  }

  assignAsCoChair(user: User) {
    user.isCoChair = true;
    this.CoChairs.push(user);
    this.CoChairsCandidates.splice(this.CoChairsCandidates.indexOf(user), 1);
    this.userService.updateUser(user).subscribe();
  }
}

import { Component, OnInit } from '@angular/core';
import {LoginPageComponent} from "../login-page/login-page.component";
import {UserService} from "../shared/user.service";
import {User} from "../shared/user.model";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {
  user: User;

  constructor() { }

  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('user'));

  }

  logout(): void{
    sessionStorage.clear()
    this.ngOnInit();
  }



}

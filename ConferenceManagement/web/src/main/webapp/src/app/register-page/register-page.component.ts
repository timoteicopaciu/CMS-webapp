import { Component, OnInit } from '@angular/core';
import {UserService} from "../shared/user.service";
import {Router} from "@angular/router";
import {User} from "../shared/user.model";
import {Observable} from "rxjs";


@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.css']
})
export class RegisterPageComponent implements OnInit {
  user: User;
  constructor(private userService: UserService,
              private router: Router) { }

  ngOnInit(): void {
  }

  register(name:string, username: string, password: string, email: string, affiliation: string) {
    if (username === '' || password === '' || name === '' || email === '' || affiliation === '') {
      alert('All fields must be filled!');
      return;
    }

    this.userService.register(name, username, password, email, affiliation).subscribe(user => this.user = user);

    if(this.user === null){
      alert("Username and email must be unique!");
      return;
    }

    this.router.navigate(["login-page"]);
  }

}

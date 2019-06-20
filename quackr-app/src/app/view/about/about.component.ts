import { Component, OnInit } from '@angular/core';
import {User} from "../../model/user";
import {Event} from "../../model/event";
import {UNAUTHORIZED} from "http-status-codes";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.scss']
})
export class AboutComponent implements OnInit {

  public username = "";
  userId;

  constructor(private userService: UserService) { }

  ngOnInit() {
    if(UserService.getLoggedInUser() !== null) {
      this.userId = UserService.getLoggedInUser().id;
      this.username = UserService.getLoggedInUser().username;
    } else {
      this.userId = null;
    }
  }

}

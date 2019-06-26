import {Component, Input, OnInit} from '@angular/core';
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private userService: UserService) { }

  public userId;
  public username = "";

  public userRoleIsAdmin = false;

  ngOnInit() {
    this.userRoleIsAdmin = UserService.getLoggedInUser().role === "ADMIN";
    this.username = UserService.getLoggedInUser().username;
    this.userId = UserService.getLoggedInUser().id;
  }

  logout() {
    this.userService.logout();
  }
}

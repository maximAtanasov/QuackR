import {Component, Input, OnInit} from '@angular/core';
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private userService: UserService) { }

  @Input() public userId;
  @Input() public username;

  public userRoleIsAdmin = false;

  ngOnInit() {
    this.userRoleIsAdmin = UserService.getLoggedInUser().role === "ADMIN";
  }

  logout() {
    this.userService.logout();
  }
}

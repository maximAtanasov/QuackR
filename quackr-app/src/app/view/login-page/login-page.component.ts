import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Title} from "@angular/platform-browser";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";
import {NOT_FOUND, UNAUTHORIZED} from 'http-status-codes';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnInit {

  username: string;
  password: string;

  invalidUser = false;
  invalidPassword = false;

  constructor(private router: Router, private userService: UserService, private titleService: Title) {
    titleService.setTitle('quackR - Login');
  }


  /**
   * Called when the form is submitted, upon successful login, redirects
   * to the homepage.
   */
  submitForm(): void {
    this.invalidUser = false;
    this.invalidPassword = false;
    this.userService.login(this.username, this.password)
      .then(() => {
        this.router.navigate(['/home']);
      })
      .catch(e => {
        if (e.status === NOT_FOUND) {
          this.invalidUser = true;
        } else if (e.status === UNAUTHORIZED) {
          this.invalidPassword = true;
        }
      });
  }

  ngOnInit() {
    if(UserService.getLoggedInUser() && UserService.getLoggedInUser().username !== null
    && UserService.getLoggedInUser().username !== undefined){
      this.router.navigate(['/home']);
    }
  }
}


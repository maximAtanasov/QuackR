import { Component, OnInit } from '@angular/core';
import {Title} from "@angular/platform-browser";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";
import {CONFLICT} from "http-status-codes";

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.scss']
})
export class RegisterPageComponent implements OnInit {

  username: string;
  password: string;
  confirmPassword: string;

  invalidUser = false;
  invalidPassword = false;
  passwordsDoNotMatch = false;

  constructor(private router: Router, private userService: UserService, private titleService: Title) {
    titleService.setTitle('quackR - Sign up');
  }

  ngOnInit() {
  }

  /**
   * Is called upon registration form submit.
   * Validates users input and sends the appropriate requests to the server
   * using the UserService.
   */
  submitForm(): void {
    this.invalidUser = false;
    this.passwordsDoNotMatch = this.password !== this.confirmPassword;
    this.invalidPassword = UserService.validatePassword(this.password);

    if (!this.invalidPassword && !this.passwordsDoNotMatch) {
      this.userService.register(this.username, this.password)
        .then(e => {
          this.userService.login(this.username, this.password)
            .then(
              () => this.router.navigate(['/home']));
        })
        .catch(e => {
          if (e.error && e.status === CONFLICT &&
            e.error.errorMessage === 'A user with the username ' + this.username + ' already exists!') {
            this.invalidUser = true;
          }
        });
    }
  }
}

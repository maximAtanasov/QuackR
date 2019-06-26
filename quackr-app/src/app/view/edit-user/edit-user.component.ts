import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../service/user.service';
import {User} from '../../model/user';
import {CONFLICT, UNAUTHORIZED} from 'http-status-codes';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss']
})
export class EditUserComponent implements OnInit {

  userId: number;
  user: User = new User();
  invalidOldPassword: boolean;
  invalidPassword: boolean;
  passwordsDoNotMatch: boolean;
  confirmPassword: string;
  password: string;
  oldPassword: string;
  invalidUser: boolean;
  success: boolean;
  private oldUsername: string;

  constructor(private titleService: Title, private router: Router, private route: ActivatedRoute, private userService: UserService) {
    titleService.setTitle('quackR - Edit account');
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.userId = params.id;
      if (this.userId != UserService.getLoggedInUser().id) {
        this.router.navigate(['/home']);
      }
      this.userService.getUser(this.userId)
        .then(value => {
          this.user = value;
          this.oldUsername = this.user.username;
        })
        .catch(e => {
          if (e.status === UNAUTHORIZED) {
            this.userService.logout();
          }
        });
    });
  }

  submitForm() {
    this.success = false;
    this.invalidUser = false;
    this.invalidOldPassword = false;
    this.passwordsDoNotMatch = this.password !== this.confirmPassword;
    this.invalidPassword = UserService.validatePassword(this.password);

    if (!this.invalidPassword && !this.passwordsDoNotMatch) {

      this.userService.login(this.oldUsername, this.oldPassword)
        .then(
          () => {
            this.userService.editUser(this.user.username, this.password, 'USER', this.userId)
              .then(e => {
                this.userService.login(this.user.username, this.password)
                  .then(() => this.success = true);
              })
              .catch(e => {
                if (e.error && e.status === CONFLICT &&
                  e.error.errorMessage === 'A user with the username ' + this.user.username + ' already exists!') {
                  this.invalidUser = true;
                }
              });
          })
        .catch(() => this.invalidOldPassword = true);


    }
  }

  deleteAccount() {
    this.userService.deleteUser(this.userId)
      .then(() => {
        this.router.navigate(['/login']);
      });
  }
}

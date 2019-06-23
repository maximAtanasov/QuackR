import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import {UNAUTHORIZED} from "http-status-codes";
import {User} from "../../model/user";
import {Event} from "../../model/event";
import {EventService} from "../../service/event.service";

@Component({
  selector: 'app-admin-menu',
  templateUrl: './admin-menu.component.html',
  styleUrls: ['./admin-menu.component.scss']
})
export class AdminMenuComponent implements OnInit {

  public users: User[] = [];
  public events: Event[] = [];

  userId: number;
  user: User = new User();

  constructor(private router: Router, private eventService: EventService,
              private route: ActivatedRoute, private userService: UserService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.userId = params.id;
      if (this.userId != UserService.getLoggedInUser().id) {
        this.router.navigate(['/home']);
      }
      this.userService.getUser(this.userId)
        .then(value => {
          this.user = value;
          if(this.user.role === "USER"){
            this.router.navigate(['/home']);
          }
        })
        .catch(e => {
          if (e.status === UNAUTHORIZED) {
            this.userService.logout();
          }
        });
      this.userService.getAllUsers()
        .then(result => {
          this.users = result;
          this.users.forEach(user => {
            this.eventService.getAllEvents(user.id)
              .then(result => {
                result.forEach(r => this.events.push(r));
                this.events.forEach(value => value.date = new Date(value.date).toISOString().split('T')[0]);
                this.events.sort((a, b) => {
                  if (new Date(a.date).getTime() === new Date(b.date).getTime()) {
                    return 0;
                  } else if (new Date(a.date).getTime() > new Date(b.date).getTime()) {
                    return -1;
                  } else {
                    return 1;
                  }
                });
              })
              .catch(e => {
                if(e.status === UNAUTHORIZED) {
                  this.userService.logout();
                }
              });
          });
        }).catch(e => {
        if(e.status === UNAUTHORIZED) {
          this.userService.logout();
        }
      });
    });
  }

  deleteAccount(id: number) {
    this.userService.deleteUser(id)
      .then(value => this.users = this.users.filter(value1 => value1.id !== id))
      .catch(e => {
        if(e.status === UNAUTHORIZED) {
          this.userService.logout();
        }
      })
  }


  getUserWithId(id: number): User {
    return this.users.find(value => value.id === id);
  }

  deleteEvent(event: Event) {
    this.eventService.deleteEvent(event.id)
      .then(value => this.events = this.events.filter(value1 => value1.id !== event.id))
      .catch(e => {
        if(e.status === UNAUTHORIZED) {
          this.userService.logout();
        }
      })
  }
}

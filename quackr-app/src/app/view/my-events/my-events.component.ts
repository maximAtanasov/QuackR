import { Component, OnInit } from '@angular/core';
import {UserService} from "../../service/user.service";
import {Event} from "../../model/event";
import {EventService} from "../../service/event.service";
import {UNAUTHORIZED} from "http-status-codes";
import {User} from "../../model/user";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-my-events',
  templateUrl: './my-events.component.html',
  styleUrls: ['./my-events.component.scss']
})
export class MyEventsComponent implements OnInit {

  constructor(private route: ActivatedRoute, private userService: UserService, private eventService: EventService) {}

  private event: Event = new Event();

  public users: User[] = [];
  public events: Event[] = [];
  userId: number;

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.userId = params.id;
      this.eventService.getAllEvents(this.userId)
        .then(result => {
          console.log(this.events);
          result.forEach(r => this.events.push(r));
        })
        .catch(e => {
          if(e.status === UNAUTHORIZED) {
            this.logout();
          }
        });
    });
  }

  logout() {
    this.userService.logout();
  }

  createEvent() {
    this.eventService.createEvent(this.event, UserService.getLoggedInUser().id)
      .then()
      .catch(e => {
        console.log(e);
        if(e.status === UNAUTHORIZED) {
          this.logout();
        }
      })
  }

  getUserWithId(id: number): User {
    return this.users.find(value => value.id === id);
  }
}

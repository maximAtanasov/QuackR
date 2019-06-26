import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {UserService} from '../../service/user.service';
import {Event} from '../../model/event';
import {EventService} from '../../service/event.service';
import {UNAUTHORIZED} from 'http-status-codes';
import {User} from '../../model/user';
import { ModalDirective } from 'ngx-bootstrap/modal';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {

  constructor(private titleService: Title, private userService: UserService, private eventService: EventService) {
    titleService.setTitle('quackR - Home');
  }

  @ViewChild('modal')
  public modal: ElementRef;

  event: Event = new Event();

  public username = '';
  public users: User[] = [];
  public events: Event[] = [];
  userId: number;

  ngOnInit() {
    this.events = [];
    this.users = [];
    if (UserService.getLoggedInUser() !== null) {
      this.userId = UserService.getLoggedInUser().id;
      this.username = UserService.getLoggedInUser().username;
    }
    this.userService.getAllUsers()
      .then(result => {
        this.users = result;
        this.users.forEach(user => {
          if (user.id === this.userId) {
            return;
          }
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
              if (e.status === UNAUTHORIZED) {
                this.logout();
              }
            });
        });
      }).catch(e => {
      if (e.status === UNAUTHORIZED) {
        this.logout();
      }
    });

  }

  logout() {
    this.userService.logout();
  }

  createEvent() {
    this.eventService.createEvent(this.event, UserService.getLoggedInUser().id)
      .then(() => this.modal.nativeElement.click())
      .catch(e => {
        if (e.status === UNAUTHORIZED) {
          this.logout();
        }
      });
  }

  getUserWithId(id: number): User {
    return this.users.find(value => value.id === id);
  }

  attendEvent(userId: number, eventId: number) {
    this.eventService.addAttendeeToEvent(eventId, this.getUserWithId(userId))
      .catch(e => {
        if (e.status === UNAUTHORIZED) {
          this.logout();
        }
      });
    this.events.find(value => value.id === eventId).attendees.push(this.getUserWithId(userId));
  }

  unattendEvent(userId: number, eventId: number) {
    this.eventService.removeAttendeeFromEvent(eventId, this.getUserWithId(userId))
      .catch(e => {
        if (e.status === UNAUTHORIZED) {
          this.logout();
        }
      });
    const event = this.events.find(value => value.id === eventId);
     event.attendees = event.attendees.filter(value => value.id !== userId);
  }

  checkIsAttending(event: Event, user: User) {
    return event.attendees.filter(value => value.id === user.id).length > 0;
  }

  now() {
    const date = new Date();
    date.setDate(new Date().getDate() + 1);
    return date.toISOString().split('T')[0];
  }

  isPastDate(date: any) {
    return date < this.now();
  }
}

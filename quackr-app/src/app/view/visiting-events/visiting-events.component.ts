import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {UserService} from '../../service/user.service';
import {EventService} from '../../service/event.service';
import {Event} from '../../model/event';
import {User} from '../../model/user';
import {UNAUTHORIZED} from 'http-status-codes';
import {ActivatedRoute, Router} from '@angular/router';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-visiting-events',
  templateUrl: './visiting-events.component.html',
  styleUrls: ['./visiting-events.component.scss']
})
export class VisitingEventsComponent implements OnInit {

  constructor(private titleService: Title, private router: Router, private route: ActivatedRoute, private userService: UserService, private eventService: EventService) {
    titleService.setTitle('quackR - Events I\'m visiting');
  }

  @ViewChild('modal')
  public modal: ElementRef;

  public username = '';
  public users: User[] = [];
  public events: Event[] = [];
  userId: number;

  ngOnInit() {
    this.events = [];
    this.users = [];
    this.route.params.subscribe(params => {
      this.userId = params.id;
      if (this.userId != UserService.getLoggedInUser().id) {
        this.router.navigate(['/home']);
      }
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
                result.forEach(r => {
                  if (this.checkIsAttending(r, this.getUserWithId(this.userId))) {
                    this.events.push(r);
                  }
                });
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
    });
  }

  logout() {
    this.userService.logout();
  }

  getUserWithId(id: number): User {
    return this.users.find(value => value.id === id);
  }

  unattendEvent(userId: number, eventId: number) {
    this.eventService.removeAttendeeFromEvent(eventId, this.getUserWithId(userId))
      .then(value => this.events = this.events.filter(value1 => value1.id !== eventId))
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
}

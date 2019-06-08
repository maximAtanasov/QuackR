import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {UserService} from "../../service/user.service";
import {Event} from "../../model/event";
import {EventService} from "../../service/event.service";
import {UNAUTHORIZED} from "http-status-codes";
import {User} from "../../model/user";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-my-events',
  templateUrl: './my-events.component.html',
  styleUrls: ['./my-events.component.scss']
})
export class MyEventsComponent implements OnInit {

  constructor(private router: Router, private route: ActivatedRoute, private userService: UserService, private eventService: EventService) {}

  @ViewChild('modal')
  public modal:ElementRef;

  private eventToEdit: Event = new Event();

  public user: User = new User();
  public events: Event[] = [];
  userId: number;

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.userId = params.id;
      if(this.userId != UserService.getLoggedInUser().id){
        this.router.navigate(['/home']);
      }
      this.userService.getUser(this.userId)
        .then(value => this.user = value)
        .catch(e => {
          if(e.status === UNAUTHORIZED) {
            this.logout();
          }
        });
      this.eventService.getAllEvents(this.userId)
        .then(result => {
          result.forEach(r => this.events.push(r));
          this.events.forEach(value => value.date = new Date(value.date).toISOString().split('T')[0]);
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

  deleteEvent(event: Event) {
    this.eventService.deleteEvent(event.id)
      .then(value => this.events = this.events.filter(value1 => value1.id !== event.id))
      .catch(e => {
        if(e.status === UNAUTHORIZED) {
          this.logout();
        }
      })
  }

  editEvent() {
    this.eventService.editEvent(this.eventToEdit, this.eventToEdit.id)
      .then(() => this.modal.nativeElement.click())
      .catch(e => {
        if(e.status === UNAUTHORIZED) {
          this.logout();
        }
      })
  }
}

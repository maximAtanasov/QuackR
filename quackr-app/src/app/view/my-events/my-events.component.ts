import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {UserService} from '../../service/user.service';
import {Event} from '../../model/event';
import {EventService} from '../../service/event.service';
import {UNAUTHORIZED} from 'http-status-codes';
import {User} from '../../model/user';
import {ActivatedRoute, Router} from '@angular/router';
import {Title} from '@angular/platform-browser';
import {Comment} from "../../model/comment";
import {CommentService} from "../../service/comment.service";

@Component({
  selector: 'app-my-events',
  templateUrl: './my-events.component.html',
  styleUrls: ['./my-events.component.scss']
})
export class MyEventsComponent implements OnInit {

  constructor(private commentService: CommentService,
              private titleService: Title,
              private router: Router,
              private route: ActivatedRoute,
              private userService: UserService,
              private eventService: EventService) {
    titleService.setTitle('quackR - My events');
  }

  @ViewChild('modal')
  public modal: ElementRef;

  eventToEdit: Event = new Event();

  public user: User = new User();
  public events: Event[] = [];
  public commentInput: string;
  private commenters: User[] = [];
  userId: number;

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.userId = params.id;
      if (this.userId != UserService.getLoggedInUser().id) {
        this.router.navigate(['/home']);
      }
      this.userService.getUser(this.userId)
        .then(value => this.user = value)
        .catch(e => {
          if (e.status === UNAUTHORIZED) {
            this.logout();
          }
        });
      this.eventService.getAllEvents(this.userId)
        .then(result => {
          result.forEach(r => this.events.push(r));
          result.forEach(result => result.comments.forEach(comment => {
            this.userService.getUser(comment.posterId).then(result => {
              if(this.commenters.find(val => val.id === result.id) === undefined){
                this.commenters.push(result);
              }
            }).catch(e => {
                if (e.status === UNAUTHORIZED) {
                  this.logout();
                }
              });
          }));
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
  }

  logout() {
    this.userService.logout();
  }

  deleteEvent(event: Event) {
    this.eventService.deleteEvent(event.id)
      .then(value => this.events = this.events.filter(value1 => value1.id !== event.id))
      .catch(e => {
        if (e.status === UNAUTHORIZED) {
          this.logout();
        }
      });
  }

  editEvent() {
    this.eventService.editEvent(this.eventToEdit, this.eventToEdit.id)
      .then(() => this.modal.nativeElement.click())
      .catch(e => {
        console.log(e);
        if (e.status === UNAUTHORIZED) {
          this.logout();
        }
      });
  }

  now() {
    const date = new Date();
    date.setDate(new Date().getDate() + 1);
    return date.toISOString().split('T')[0];
  }


  postComment(eventId: number) {
    const comment = new Comment();
    comment.text = this.commentInput;
    comment.posterId = this.userId;
    comment.eventId = eventId;
    this.commentService.createComment(comment)
      .then(result => {
        result.datePosted = new Date(result.datePosted).toUTCString();
        this.events.find(value => value.id === eventId).comments.push(result);
        this.commenters.push(this.user);
      })
      .catch(e => {
        if (e.status === UNAUTHORIZED) {
          this.logout();
        }
      });
    this.commentInput = "";
  }

  deleteComment(id: number, eventId: number) {
    this.commentService.deleteComment(id)
      .then(() => {
        const event = this.events.find(value => value.id === eventId);
        event.comments = event.comments.filter(value => value.id !== id);
      })
      .catch(e => {
        console.log(e);
        if (e.status === UNAUTHORIZED) {
          this.logout();
        }
      });
  }

  getUserWithId(id: number): User {
    const result = this.commenters.find(value => value.id === id);
    if(result === undefined){
      return  new User();
    }else {
      return result;
    }
  }
}

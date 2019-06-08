import { Injectable } from '@angular/core';
import {User} from "../model/user";
import {Event} from "../model/event";
import {AppComponent} from "../app.component";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private apiURL = AppComponent.getApiUrl();

  constructor(private httpClient: HttpClient) {
  }

  public getAllEvents(userId: number): Promise<Event[]> {
    return this.httpClient.get<Event[]>(this.apiURL + 'events/user/' + userId).toPromise();
  }


  public getEvent(event: Event): Promise<Event> {
    return this.httpClient.post<Event>(this.apiURL + 'events', {
      title: event.title,
      date: event.date,
      location: event.location,
      description: event.description,
      attendeeLimit: event.attendeeLimit,
      public: true
    }).toPromise();
  }

  public createEvent(event: Event, userId: number): Promise<Event> {
    return this.httpClient.post<Event>(this.apiURL + 'events/user/' + userId, {
      title: event.title,
      date: event.date,
      location: event.location,
      description: event.description,
      attendeeLimit: event.attendeeLimit,
      public: true
    }).toPromise();
  }

  public editEvent(event: Event, eventId: number): Promise<Event> {
    return this.httpClient.post<Event>(this.apiURL + 'events/'+eventId, {
      title: event.title,
      date: event.date,
      location: event.location,
      description: event.description,
      attendeeLimit: event.attendeeLimit,
      public: true
    }).toPromise();
  }

  public deleteEvent(eventId: number) {
    return this.httpClient.delete<any>(this.apiURL + 'events/' + eventId).toPromise();
  }


  public addAttendeeToEvent(eventId: number, attendee: User) {
    return this.httpClient.post<any>(this.apiURL + 'events/' + eventId + '/add', JSON.stringify([attendee])).toPromise();
  }

  public removeAttendeeFromEvent(eventId: number, attendee: User) {
    return this.httpClient.post<any>(this.apiURL + 'events/' + eventId + '/remove', JSON.stringify([attendee])).toPromise();
  }
}

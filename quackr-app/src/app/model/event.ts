import {User} from "./user";

export class Event {
  public id: number;
  public organizerId: number;
  public title: string;
  public location: string;
  public date: any;
  public description: string;
  public attendeeLimit: number;
  public attendees: User[];
  public comments: Comment[];
  public public: true
}

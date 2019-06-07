import {User} from "./user";

export class Event {
  public id: number;
  public title: string;
  public location: string;
  public date: string;
  public description: string;
  public attendeeLimit: number;
  public attendees: User[];
  public comments: Comment[];
  public public: true
}

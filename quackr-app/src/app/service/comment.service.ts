import {Injectable} from '@angular/core';
import {AppComponent} from "../app.component";
import {HttpClient} from "@angular/common/http";
import {Comment} from "../model/comment";

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private apiURL = AppComponent.getApiUrl();

  constructor(private httpClient: HttpClient) { }

  public createComment(comment: Comment): Promise<Comment> {
    return this.httpClient.post<Comment>(this.apiURL + 'comments/event/' + comment.eventId, {
      text: comment.text,
      posterId: comment.posterId
    }).toPromise();
  }

  public deleteComment(commentId: number): Promise<any> {
    return this.httpClient.delete<Comment>(this.apiURL + 'comments/' + commentId).toPromise();
  }
}

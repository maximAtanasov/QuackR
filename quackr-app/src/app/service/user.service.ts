import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {AppComponent} from "../app.component";
import {User} from "../model/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiURL = AppComponent.getApiUrl();

  constructor(private router: Router, private httpClient: HttpClient) {
  }

  /**
   * Returns true if the given string is at least 8 symbols long
   * and contains both a digit and a character.
   * @param password the string to check.
   */
  public static validatePassword(password: string): boolean {
    return password.length < 8 || !/\d/g.test(password) || !/\d+/g.test(password);
  }

  /**
   * returns the currently logged in user or null if they don't exist.
   */
  public static getLoggedInUser() {
    return JSON.parse(localStorage.getItem('currentUser'));
  }

  /**
   * Send a POST request to /users with the username and password
   * @param usernameValue The username
   * @param passwordValue The password in plaintext
   */
  public register(usernameValue: string, passwordValue: string) {
    return this.httpClient.post(this.apiURL + 'users',
      {username: usernameValue, password: passwordValue, rating: 0, role: "USER"},
      {observe: 'response'}).toPromise();
  }

  /**
   * Sends a POST request to /users/login with the given username and password.
   * Upon receiving a successful response from the server. The received access and
   * refresh tokens are saved to the localStorage with the key "currentUser".
   * @param usernameValue The username
   * @param passwordValue The password in plaintext
   */
  public login(usernameValue: string, passwordValue: string) {
    return this.httpClient.post<any>(this.apiURL + 'users/login',
      {username: usernameValue, password: passwordValue}).toPromise()
      .then(user => {
        if (user && user.accessToken) {
          user.username = usernameValue;
          localStorage.setItem('currentUser', JSON.stringify(user));
        }
      });
  }

  public editUser(usernameValue: string, passwordValue: string, roleValue: string, userId: number) {
    return this.httpClient.post<any>(this.apiURL + 'users/' + userId,
      {username: usernameValue, password: passwordValue, rating: 0, role: roleValue}).toPromise();
  }

  public getUser(userId: number): Promise<User> {
    return this.httpClient.get<User>(this.apiURL + 'users/' + userId).toPromise();
  }


  public getAllUsers(): Promise<User[]> {
    return this.httpClient.get<User[]>(this.apiURL + 'users').toPromise();
  }


  public deleteUser(userId: number) {
    return this.httpClient.delete<any>(this.apiURL + 'users/' + userId).toPromise();
  }

  /**
   * Deletes the access token from the localStorage and
   * redirects to /login
   */
  public logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }
}

import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginPageComponent } from './view/login-page/login-page.component';
import {HomepageComponent} from "./view/homepage/homepage.component";
import { HeaderComponent } from './view/header/header.component';
import { RegisterPageComponent } from './view/register-page/register-page.component';
import {RouterModule, Routes} from "@angular/router";
import { FooterComponent } from './view/footer/footer.component';
import {FormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AuthInterceptor} from "./auth.interceptor";
import { MyEventsComponent } from './view/my-events/my-events.component';

const appRoutes: Routes = [
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: 'home', component: HomepageComponent },
  {path: 'events/user/:id', component: MyEventsComponent},
  { path: '', redirectTo: '/home', pathMatch: 'full'}
];

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    HomepageComponent,
    HeaderComponent,
    RegisterPageComponent,
    FooterComponent,
    MyEventsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

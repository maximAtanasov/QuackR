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
import { VisitingEventsComponent } from './view/visiting-events/visiting-events.component';
import { EditUserComponent } from './view/edit-user/edit-user.component';
import { AboutComponent } from './view/about/about.component';
import {TrimValueAccessorModule} from "ng-trim-value-accessor";
import { AdminMenuComponent } from './view/admin-menu/admin-menu.component';

const appRoutes: Routes = [
  { path: 'login', component: LoginPageComponent },
  { path: 'about', component: AboutComponent },
  { path: 'admin-menu/:id', component: AdminMenuComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: 'home', component: HomepageComponent },
  {path: 'events/user/:id', component: MyEventsComponent},
  {path: 'visiting/:id', component: VisitingEventsComponent},
  {path: 'edit-user/:id', component: EditUserComponent},
  { path: '', redirectTo: 'home', pathMatch: 'full'}
];

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    HomepageComponent,
    HeaderComponent,
    RegisterPageComponent,
    FooterComponent,
    MyEventsComponent,
    VisitingEventsComponent,
    EditUserComponent,
    AboutComponent,
    AdminMenuComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    TrimValueAccessorModule,
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

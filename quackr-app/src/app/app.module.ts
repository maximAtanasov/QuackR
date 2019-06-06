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

const appRoutes: Routes = [
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: 'home', component: HomepageComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    HomepageComponent,
    HeaderComponent,
    RegisterPageComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

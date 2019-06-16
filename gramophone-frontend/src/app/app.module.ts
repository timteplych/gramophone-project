import {BrowserModule} from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {SidebarComponent} from './sidebar/sidebar.component';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {library} from '@fortawesome/fontawesome-svg-core';
import {fas} from '@fortawesome/free-solid-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';
import {faPlayCircle} from '@fortawesome/free-solid-svg-icons/faPlayCircle';
import {fab} from '@fortawesome/free-brands-svg-icons';
import { NavbarComponent } from './navbar/navbar.component';
import { LoginComponent } from './login/login.component';
import { MainPageComponent } from './main-page/main-page.component';
import {HttpClientModule} from '@angular/common/http';
import { TrackPageComponent } from './track-page/track-page.component';
import { MusicPlayerComponent } from './music-player/music-player.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {
  MatProgressBarModule,
  MatRadioModule,
  MatSliderModule,
  MatFormFieldModule, MatInputModule
} from '@angular/material';
import { RegisterComponent } from './register/register.component';
import { UserAccountComponent } from './user-account/user-account.component';
import {ScrollToModule} from '@nicky-lenaers/ngx-scroll-to';



@NgModule({
  declarations: [
    AppComponent,
    SidebarComponent,
    NavbarComponent,
    LoginComponent,
    MainPageComponent,
    TrackPageComponent,
    MusicPlayerComponent,
    RegisterComponent,
    UserAccountComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ScrollToModule.forRoot(),
    FontAwesomeModule,
    BrowserAnimationsModule,
    MatProgressBarModule, MatRadioModule, MatSliderModule,
    FormsModule,
    ReactiveFormsModule, MatFormFieldModule, MatInputModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ]
})
export class AppModule {
  constructor() {
    library.add(fas, far, fab, faPlayCircle);

  }
}

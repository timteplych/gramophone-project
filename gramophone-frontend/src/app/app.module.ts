import {BrowserModule} from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, LOCALE_ID, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {SidebarComponent} from './sidebar/sidebar.component';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {library} from '@fortawesome/fontawesome-svg-core';
import {fas} from '@fortawesome/free-solid-svg-icons';
import {far} from '@fortawesome/free-regular-svg-icons';
import {faPlayCircle} from '@fortawesome/free-solid-svg-icons/faPlayCircle';
import {fab} from '@fortawesome/free-brands-svg-icons';
import {NavbarComponent} from './navbar/navbar.component';
import {LoginComponent} from './login/login.component';
import {MainPageComponent} from './main-page/main-page.component';
import {HttpClientModule} from '@angular/common/http';
import {TrackPageComponent} from './track-page/track-page.component';
import {MusicPlayerComponent} from './music-player/music-player.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {
  MatProgressBarModule,
  MatRadioModule,
  MatSliderModule,
  MatFormFieldModule, MatInputModule, MatDialogModule, MAT_DATE_LOCALE, DateAdapter, MAT_DATE_FORMATS, MatTabsModule
} from '@angular/material';
import {MomentDateAdapter} from '@angular/material-moment-adapter';
import {RegisterComponent} from './register/register.component';
import {UserAccountComponent} from './user-account/user-account.component';
import {ScrollToModule} from '@nicky-lenaers/ngx-scroll-to';
import {NumberSpacesPipe} from './_utils/numberSpaces.pipe';
import {ButtonSubFormatPipe} from './_utils/buttonSubFormat.pipe';
import {MusicUploadDialogComponent} from './music-upload-dialog/music-upload-dialog.component';
import {NgxFileDropModule} from 'ngx-file-drop';
import {DATE_FORMAT} from '../environments/environment';
import { registerLocaleData } from '@angular/common';
import localeRu from '@angular/common/locales/ru';
import {ListeningAmountPipe} from './_utils/listeningAmount.pipe';
import { PlaylistComponent } from './playlist/playlist.component';

registerLocaleData(localeRu);

@NgModule({
  declarations: [
    AppComponent,
    SidebarComponent,
    NavbarComponent,
    LoginComponent,
    ListeningAmountPipe,
    MainPageComponent,
    NumberSpacesPipe,
    ButtonSubFormatPipe,
    TrackPageComponent,
    MusicPlayerComponent,
    RegisterComponent,
    UserAccountComponent,
    MusicUploadDialogComponent,
    ButtonSubFormatPipe,
    PlaylistComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    MatTabsModule,
    NgxFileDropModule,
    ScrollToModule.forRoot(),
    FontAwesomeModule,
    MatDialogModule,
    BrowserAnimationsModule,
    MatProgressBarModule, MatRadioModule, MatSliderModule,
    FormsModule,
    ReactiveFormsModule, MatFormFieldModule, MatInputModule
  ],
  providers: [{provide: LOCALE_ID, useValue: 'ru'},
    {provide: MAT_DATE_LOCALE, useValue: 'ru-RU'},
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
    {provide: MAT_DATE_FORMATS, useValue: DATE_FORMAT}],
  entryComponents: [MusicUploadDialogComponent],
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

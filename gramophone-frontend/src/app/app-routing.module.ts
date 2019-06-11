import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {MainPageComponent} from './main-page/main-page.component';
import {TrackPageComponent} from './track-page/track-page.component';
import {UserAccountComponent} from './user-account/user-account.component';

const routes: Routes = [
  {path: '', component: MainPageComponent},
  {path: 'tracks', component: TrackPageComponent},
  {path: 'account', component: UserAccountComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {MainPageComponent} from './main-page/main-page.component';
import {TrackPageComponent} from './track-page/track-page.component';
import {UserAccountComponent} from './user-account/user-account.component';

const routes: Routes = [
  {path: '', component: MainPageComponent, data: {animation: 'Home'}},
  {path: 'tracks/:id', component: TrackPageComponent, data: {animation: 'Track'}},
  {path: 'account', component: UserAccountComponent, data: {animation: 'Account'}},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainPageComponent} from './main-page/main-page.component';
import {TrackPageComponent} from './track-page/track-page.component';
import {UserAccountComponent} from './user-account/user-account.component';
import {RoleEnum} from './_models';
import {AuthGuard} from './_guard';

const routes: Routes = [
  {path: '', component: MainPageComponent, data: {animation: 'Home'}},
  {path: 'tracks/:id', component: TrackPageComponent, data: {animation: 'Track'}},
  {
    path: 'account',
    component: UserAccountComponent,
    canActivate: [AuthGuard],
    data: {animation: 'Account', roles: [RoleEnum.User]}
  },
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

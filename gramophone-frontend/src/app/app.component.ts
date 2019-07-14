import {Component} from '@angular/core';
import {animate, group, query, stagger, style, transition, trigger} from '@angular/animations';
import {Router} from '@angular/router';
import {AuthenticationService} from './_services';
import {RoleEnum, User} from './_models';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [
    trigger('routeAnimations', [
      transition('Account => *', [
        query(':enter, :leave',
          style({
            opacity: 0
          }),
          {optional: true}),
        group([
          query(':enter', [
            style({
              opacity: 0,
              height: 0
            }),
            animate('1s ease-in-out',
              style({
                opacity: 1,
                height: '*'
              }))
          ], {optional: true}),
          query(':leave', [
            style({opacity: 0}),
            animate('1s ease-in-out',
              style({opacity: 0}))
          ], {optional: true}),
        ])
      ]),
      transition('Home => *', [
        query(':enter, :leave',
          style({opacity: 0}),
          {optional: true}),
        group([
          query(':enter', [
            style({opacity: 0}),
            animate('1s ease-in-out',
              style({opacity: 1}))
          ], {optional: true}),
          query(':leave', [
            style({opacity: 1}),
            animate('1s ease-in-out',
              style({opacity: 0}))
          ], {optional: true}),
        ])
      ]),
      transition('Track => *', [
        query(':enter, :leave',
          style({opacity: 0}),
          {optional: true}),
        group([
          query(':enter', [
            style({
              opacity: 0,
              height: 0
            }),
            animate('1s ease-in-out',
              style({
                opacity: 1,
                height: '*'
              }))
          ], {optional: true}),
          query(':leave', [
            style({opacity: 0}),
            animate('1s ease-in-out',
              style({opacity: 0}))
          ], {optional: true}),
        ])
      ]),
      transition('Track => Home', [
        query(':enter, :leave',
          style({opacity: 1}),
          {optional: true}),
        group([
          query(':enter', [
            style({opacity: 0}),
            animate('1s ease-in-out',
              style({opacity: 1}))
          ], {optional: true}),
          query(':leave', [
              style({opacity: 1}),
              animate('1s ease-in-out',
                style({opacity: 0}))
            ], {optional: true}
          )]
        )]
      )]
    )]
})


export class AppComponent {
  title = 'gramophone-frontend';
  currentUser: User;

  constructor(private router: Router,
              private authenticationService: AuthenticationService) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);

  }

  get isAdmin() {
    let variant = false;
    if (this.currentUser) {
      this.currentUser.roles.forEach(obg => {
        if (obg.name === RoleEnum.Admin) {
          variant = true;
        }
      });
    }
    return variant;
  }

}

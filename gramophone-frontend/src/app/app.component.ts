import {Component} from '@angular/core';
import {animate, group, query, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [
    trigger('routeAnimations', [
    transition('Account => *', [
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
          style({opacity: 0}),
          animate('1s ease-in-out',
            style({opacity: 0}))
        ], {optional: true}),
      ])
    ]),
    transition('Home => *', [
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
          style({opacity: 0}),
          animate('1s ease-in-out',
            style({opacity: 1}))
        ], {optional: true}),
      ])
    ]),
    transition('Track => *', [
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
}

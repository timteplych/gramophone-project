import {Component, OnInit} from '@angular/core';

declare var $: any;

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})

export class NavbarComponent implements OnInit {



  constructor() {
  }

  ngOnInit() {
  }

  onClick() {
    $('.login.sidebar')
      .sidebar({
        dimPage: false
      })
      .sidebar('setting', 'transition', 'overlay')
      .sidebar('toggle');
  }

}

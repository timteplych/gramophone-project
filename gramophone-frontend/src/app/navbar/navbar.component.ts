import {Component, OnInit} from '@angular/core';
import {User} from '../_models';
import {AuthenticationService} from '../_services';
import {RoleEnum} from '../_models';

declare var $: any;

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})

export class NavbarComponent implements OnInit {

  currentUser: User;
  users: User[] = [];
  updatePage;


  constructor(private authenticationService: AuthenticationService
  ) {
    this.currentUser = this.authenticationService.currentUserValue;
    console.log(this.currentUser);
  }

  ngOnInit() {
  }

  onClick() {
    $('.login.sidebar')
      .sidebar({
        dimPage: false,
        context: $('body')
      })
      .sidebar('setting', 'transition', 'overlay')
      .sidebar('toggle');

  }


  isAdmin(): boolean {
    let variant = false;
    this.currentUser.roles.forEach(obg => {
      console.log(obg.name === RoleEnum.Admin);
      if (obg.name === RoleEnum.Admin) {
        variant = true;
      }
    });
    return variant;
  }

  isUser(): boolean {
    let variant = false;
    this.currentUser.roles.forEach(obg => {
      if (obg.name === RoleEnum.User) {
        variant = true;
      }
    });
    return variant;
  }

}

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
  isLoggedIn;
  isFilterOn = true;


  constructor(private authenticationService: AuthenticationService
  ) {
    this.currentUser = this.authenticationService.currentUserValue;
    this.authenticationService.currentUser.subscribe(value => {
      this.currentUser = value;
      this.isLoggedIn = !!value;
      this.isUser();
      this.isAdmin();
    });
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

  filterSidebar() {
    if (this.isFilterOn) {
      $('.filter.inner').removeClass('slide');
      this.isFilterOn = false;
    } else {
      $('.filter.inner').addClass('slide');
      this.isFilterOn = true;
    }
  }


  isAdmin(): boolean {
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

  isUser(): boolean {
    let variant = false;
    if (this.currentUser) {
      this.currentUser.roles.forEach(obg => {
        if (obg.name === RoleEnum.User) {
          variant = true;
        }
      });
    }
    return variant;
  }

  logout() {
    this.authenticationService.logout();

  }
}

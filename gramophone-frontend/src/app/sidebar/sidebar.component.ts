import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog} from "@angular/material";
import {AuthenticationService} from "../_services";
import {RoleEnum, User} from "../_models";

declare var $: any;

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  // faPlayCircle = faPlayCircle;
  currentUser: User;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private authenticationService: AuthenticationService) {

    this.authenticationService.currentUser.subscribe(value => {
      this.currentUser = value;
      this.isUser();
      this.isAdmin();
    });
  }

  ngOnInit() {
    $('div.sidebar a.item.accordion')
      .accordion({
        selector: {
          trigger: '.title'
        }
      });
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

}

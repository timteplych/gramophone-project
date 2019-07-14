import {Component, OnInit} from '@angular/core';
import {User} from '../_models';
import {AuthenticationService, TracksService} from '../_services';
import {RoleEnum} from '../_models';
import {ActivatedRoute, Router} from '@angular/router';
import {MusicUploadDialogComponent} from '../music-upload-dialog/music-upload-dialog.component';
import {MatDialog} from '@angular/material';

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
  searchTerm = '';


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog,
    private authenticationService: AuthenticationService,
    private tracksService: TracksService
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


  uploadMusic(): void {
    const dialogRef = this.dialog.open(MusicUploadDialogComponent, {
      width: '800px',
      height: '745px'
    });
  }

  openPlaylist() {
    $('.playlist.sidebar')
      .sidebar({
        dimPage: false
      }).sidebar('setting', 'transition', 'push')
      .sidebar('toggle');
  }

  search() {
    this.tracksService.getTrackListWithSearchAndFilter(1,
      this.searchTerm.charAt(0).toUpperCase() + this.searchTerm.slice(1), '')
      .subscribe(res => {
        console.log(res);
      });
  }
}

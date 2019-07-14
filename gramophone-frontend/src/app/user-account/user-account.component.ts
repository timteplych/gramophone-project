import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Track, User} from '../_models';
import {AuthenticationService, TracksService} from '../_services';
import {Subscription} from 'rxjs';


declare var $: any;

@Component({
  selector: 'app-user-account',
  templateUrl: './user-account.component.html',
  styleUrls: ['./user-account.component.scss']
})
export class UserAccountComponent implements OnInit {
  currentUser: User;
  userForm: FormGroup;
  trackListSub: Subscription;
  trackList: Track[];

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private tracksService: TracksService
  ) {

  }

  ngOnInit() {
    $('.tabs-menu .tabular.menu .item').tab();

    this.authenticationService.currentUser.subscribe(value => {
      this.currentUser = value;
    });

    this.trackListSub = this.tracksService.getUserTracks(this.currentUser.id).subscribe(res => {
      this.trackList = res;
    });

    if (this.currentUser.infoSinger) {
      this.userForm = this.formBuilder.group({
        username: [this.currentUser.username, Validators.required],
        avatar: [this.currentUser.avatar || null],
        firstName: [this.currentUser.infoSinger.firstName, Validators.required],
        lastName: [this.currentUser.infoSinger.lastName, Validators.required],
        email: [this.currentUser.email, Validators.required],
        phone: [this.currentUser.infoSinger.phone, Validators.required],
        oldPassword: [''],
        password: [''],
        password2: ['']
      });
    } else {
      this.userForm = this.formBuilder.group({
        username: [this.currentUser.username, Validators.required],
        avatar: [null],
        firstName: [''],
        lastName: [''],
        email: [this.currentUser.email, Validators.required],
        phone: [''],
        oldPassword: [''],
        password: [''],
        password2: ['']
      });
    }
  }

  get f() {
    return this.userForm.controls;
  }


}

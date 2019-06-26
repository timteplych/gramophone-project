import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AuthenticationService, TracksService} from '../_services';
import {Track, User} from '../_models';
import {BehaviorSubject, Subscription} from 'rxjs';
import {FormBuilder, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-track-page',
  templateUrl: './track-page.component.html',
  styleUrls: ['./track-page.component.scss']
})
export class TrackPageComponent implements OnInit {

  track: Track;
  trackSub: Subscription;
  userSub: Subscription;
  commentForm: FormGroup;
  myFocusVar;
  currentUser: User;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private trackService: TracksService,
    private authenticationService: AuthenticationService
  ) {
    this.trackSub = this.trackService.getTrackById(this.route.snapshot.params.id).subscribe(res => {
      this.track = res;
      localStorage.removeItem('track');
      localStorage.setItem('track', JSON.stringify(this.track));
      this.trackService.currentTrackSubj.next(res);
    });
    this.userSub = this.authenticationService.currentUser.subscribe(res => {
      this.currentUser = res;
    });
  }


  ngOnInit() {
    this.commentForm = this.formBuilder.group({
      comment: ['']
    });

  }

  get f() {
    return this.commentForm.controls;
  }

  delay(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}

import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AuthenticationService, CommentsService, TracksService} from '../_services';
import {CommentModel, Track, User} from '../_models';
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
  commentList: CommentModel[];

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private trackService: TracksService,
    private authenticationService: AuthenticationService,
    private commentServices: CommentsService
  ) {
    this.trackSub = this.trackService.getTrackById(this.route.snapshot.params.id).subscribe(res => {
      this.track = res;
      localStorage.removeItem('track');
      localStorage.setItem('track', JSON.stringify(this.track));
      this.trackService.currentTrackSubj.next(res);
      console.log(res);
    });

    this.userSub = this.authenticationService.currentUser.subscribe(res => {
      this.currentUser = res;
    });
  }


  ngOnInit() {
    if (this.track) {
      console.log(this.track.comments);
      this.commentServices.getAllCommnets(this.track.id).subscribe(comments => {
        console.log(comments);
        this.commentList = comments;
        this.commentServices.currentCommentListSubj.next(comments);
      });
    }

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

  sendComment() {
    console.log(this.track.id, this.f.comment.value, this.currentUser.id);
    this.commentServices.addComment(this.track.id, this.f.comment.value, this.currentUser.id).subscribe(res => {
      this.commentServices.getAllCommnets(this.track.id).subscribe(comments => {
        console.log(comments);
        this.commentList = comments;
        this.track.comments = comments;
        this.commentServices.currentCommentListSubj.next(comments);
      });
    });
    this.f.comment.setValue('');
  }
}

import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Genre, Track, User} from '../_models';
import {TracksService, UsersService} from '../_services';
import {Observable, Subscription, timer} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss']
})
export class MainPageComponent implements OnInit {

  trackListSub: Subscription;
  trackList: Track[];
  genreList: Genre[];
  genreListSub: Subscription;
  usersList: User[];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private tracksService: TracksService,
    private usersService: UsersService
  ) {

  }

  ngOnInit() {
    this.usersService.getListOfUsers().subscribe(res => {
      this.usersList = res;
    });

    this.tracksService.getTrackList(1).subscribe(res => {
      this.trackList = res;
    });
    this.trackListSub = this.tracksService.currentTrackList.subscribe(res => {
      this.trackList = res;
    });

    this.genreListSub = this.tracksService.getGenreList().subscribe(res => {
      this.genreList = res;
    });
  }

}

import {Component, OnInit} from '@angular/core';
import {Genre, Track} from '../_models';
import {TracksService} from '../_services';
import {Subscription} from 'rxjs';
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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private tracksService: TracksService
  ) {
  }

  ngOnInit() {

    this.trackListSub = this.tracksService.getTrackList().subscribe(res => {
        this.trackList = res;
      },
      console.error);

    this.genreListSub = this.tracksService.getGenreList().subscribe(res => {
      this.genreList = res;
    });

  }

}

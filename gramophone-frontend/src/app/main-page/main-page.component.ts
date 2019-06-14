import {Component, OnInit} from '@angular/core';
import {Track} from '../_models';
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

  }

}

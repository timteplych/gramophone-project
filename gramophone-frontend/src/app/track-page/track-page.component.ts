import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {TracksService} from '../_services';
import {Track} from '../_models';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-track-page',
  templateUrl: './track-page.component.html',
  styleUrls: ['./track-page.component.scss']
})
export class TrackPageComponent implements OnInit {

  track: Track;
  trackSub: Subscription;

  constructor(
    private route: ActivatedRoute,
    private trackService: TracksService
  ) {
  }

  ngOnInit() {
    this.trackSub = this.trackService.getTrackById(this.route.snapshot.params.id).subscribe(res => {
      this.track = res;
      localStorage.setItem('track', JSON.stringify(this.track));
      console.log(this.track);
    });
  }

  delay(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

}

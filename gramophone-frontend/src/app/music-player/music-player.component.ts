import {Component, OnInit} from '@angular/core';
import * as moment from 'moment';
import {Duration} from 'moment';
import {Subscription, timer} from 'rxjs';
import {TracksService} from '../_services';
import {Track} from '../_models';


@Component({
  selector: 'app-music-player',
  templateUrl: './music-player.component.html',
  styleUrls: ['./music-player.component.scss']
})
export class MusicPlayerComponent implements OnInit {

  private audio: any;
  private durationTime = 100;
  isPlaying = false;
  volumeValue = 1;
  trackDuration: Duration = moment.duration(0, 'seconds');
  value;
  cachedVolumn;
  trackTime;
  subscribe = null;
  source;
  audioList;
  trackTitle;
  trackPerformer;
  currentTrack: Track;
  currentTrackSub: Subscription;
  isClosed = false;

  constructor(
    private trackService: TracksService
  ) {
  }

  ngOnInit() {
    this.audio = new Audio();
    this.currentTrackSub = this.trackService.currentTrack.subscribe(res => {
      this.isClosed = false;
      this.currentTrack = res;
      this.audio.src = this.currentTrack.downloadUrl;
      this.trackPerformer = this.currentTrack.wordAuthor;
      this.trackTitle = this.currentTrack.title;
      this.audio.load();
      this.source = timer(this.audio.duration, 1000);
      this.audio.addEventListener('timeupdate', event => {
        this.trackTime = this.audio.currentTime;
      });
    });
  }

  // Activate the playback
  play() {
    if (!this.isPlaying) {
      this.isPlaying = true;
      this.durationTime = Math.round(this.audio.duration);
      this.audio.play();
      if (this.subscribe === null || this.subscribe.closed) {
        this.subscribe = this.source.subscribe(() => {
          if (this.trackDuration.milliseconds() !== 0) {
            if (this.audio.ended || this.trackDuration.seconds() === 0 && this.trackDuration.minutes() === 0) {
              this.subscribe.unsubscribe();
              this.isPlaying = false;
              this.audio.load();
              this.currentTrack.listeningAmount++;
              this.trackService.updateTrackListeningAmount(this.currentTrack.id, this.currentTrack.listeningAmount).subscribe((res) => {
                console.log(res);
              });
              this.trackDuration = moment.duration(0, 'seconds');
            } else {
              if (this.audio.paused) {
                this.isPlaying = false;
                this.trackDuration = moment.duration(this.trackDuration, 'seconds');
              } else {
                this.isPlaying = true;
                this.trackDuration = moment.duration(this.trackDuration, 'seconds').subtract(1, 'seconds');
              }
            }
          } else {
            this.trackDuration = moment.duration(this.audio.duration, 'seconds');
          }
        });
      }
    } else {
      this.subscribe.unsubscribe();
      this.audio.pause();
      this.trackDuration = moment.duration(this.trackDuration, 'seconds');
      this.isPlaying = false;
    }
  }

  // Turn the sound off
  mute() {
    if (!this.audio.muted) {
      this.audio.muted = true;
      this.cachedVolumn = this.volumeValue;
      this.volumeValue = 0;
      this.audio.volume = this.volumeValue;
    } else {
      this.audio.muted = false;
      this.audio.volume = this.cachedVolumn;
      this.volumeValue = this.cachedVolumn;
    }
  }

  // Stop the playback
  stop() {
    // this.audio.load();
    this.audio.pause();
  }

  // Activate the previous track
  prev(value: any) {
    if (this.audioList.length > 1) {
      const inc = this.audioList.indexOf(value) - 1;
      if (inc <= this.audioList.length - 1) {
        const currentTrack = this.audioList[inc];
        this.audio.src = currentTrack.src;
        this.audio.load();
        this.audio.play();
      }
    }
  }

  // Activate the next track
  next(value: any) {
    if (this.audioList.length > 1) {
      const inc = this.audioList.indexOf(value) + 1;
      if (inc <= this.audioList.length - 1) {
        const currentTrack = this.audioList[inc];
        this.audio.src = currentTrack.src;
        this.audio.load();
        this.audio.play();
      }
    }
  }


  // Change of music volume
  volumeChange() {
    this.audio.volume = this.volumeValue;
  }


  // Sets the dots on the timeline with which the track starts
  setTime() {
    this.stop();
    this.audio.currentTime = this.trackTime;
    this.trackDuration = moment.duration(this.audio.duration, 'seconds').subtract(this.trackTime, 'seconds');
    this.audio.play();
    this.isPlaying = true;
  }
}

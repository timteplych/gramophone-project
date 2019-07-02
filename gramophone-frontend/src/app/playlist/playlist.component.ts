import {Component, OnInit} from '@angular/core';

declare var $: any;

@Component({
  selector: 'app-playlist',
  templateUrl: './playlist.component.html',
  styleUrls: ['./playlist.component.scss']
})
export class PlaylistComponent implements OnInit {

  isPlaying = false;

  constructor() {
  }

  ngOnInit() {
    $('.accordion')
      .accordion();
  }


  play() {

  }
}

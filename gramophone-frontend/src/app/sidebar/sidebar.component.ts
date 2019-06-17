import { Component, OnInit } from '@angular/core';

declare var $: any;

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  // faPlayCircle = faPlayCircle;

  constructor() { }

  ngOnInit() {
    $('div.sidebar a.item.accordion')
      .accordion({
        selector: {
          trigger: '.title'
        }
      });
  }

}

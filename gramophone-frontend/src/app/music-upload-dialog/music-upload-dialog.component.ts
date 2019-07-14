import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {FileSystemFileEntry, NgxFileDropEntry} from 'ngx-file-drop';
import {Genre, User} from '../_models';
import {Subscription} from 'rxjs';
import {AuthenticationService, TracksService} from '../_services';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {formDataCreator} from '../_utils/formDataCreator';
import {ActivatedRoute, Router} from '@angular/router';

declare var $: any;

@Component({
  selector: 'app-music-upload-dialog',
  templateUrl: './music-upload-dialog.component.html',
  styleUrls: ['./music-upload-dialog.component.scss'],
  animations: [trigger('slideLeft', [
    state('out', style({
      transform: 'translateX(0)',
      opacity: 1
    })),
    state('in', style({
      transform: 'translateX(-40%)',
      opacity: 0
    })),
    transition('in <=> out', animate('.5s ease'))
  ])]
})
export class MusicUploadDialogComponent implements OnInit {

  public file: NgxFileDropEntry;
  formData: FormData;
  trackForm: FormGroup;
  genreList: Genre[];
  genreListSub: Subscription;
  isPlaying = false;
  audio;
  trackTime;
  currentUser: User;
  fileTypes = ['audio/mp3', 'audio/mpeg', 'audio/wav'];

  public circumference: number;
  public strokeDashoffset = 0;

  constructor(public dialogRef: MatDialogRef<MusicUploadDialogComponent>,
              public dialog: MatDialog,
              private formBuilder: FormBuilder,
              private tracksService: TracksService,
              private authenticationService: AuthenticationService,
              private route: ActivatedRoute,
              private router: Router) {

    this.authenticationService.currentUser.subscribe(value => {
      this.currentUser = value;
    });
  }


  ngOnInit() {
    this.trackForm = this.formBuilder.group({
      title: [null, Validators.required],
      wordAuthor: [null, Validators.required],
      musicAuthor: [null, Validators.required],
      genreId: [null, Validators.required],
      file: [null, Validators.required],
      performerId: [this.currentUser.id, Validators.required],
    });

    this.audio = new Audio();
    $('.ui.dropdown')
      .dropdown({
        clearable: true
      });

    this.genreListSub = this.tracksService.getGenreList().subscribe(res => {
      this.genreList = res;
    });

    this.audio.addEventListener('timeupdate', event => {
      this.trackTime = this.audio.currentTime;
      if (this.audio.ended) {
        this.audio.pause();
        this.audio.load();
        this.strokeDashoffset = this.circumference - this.audio.duration * this.circumference;
        this.isPlaying = false;
      }
    });
  }

  public dropped(files: NgxFileDropEntry[]) {
    this.file = files[0];
    if (files[0].fileEntry.isFile) {
      const fileEntry = files[0].fileEntry as FileSystemFileEntry;
      fileEntry.file((innerFile: File) => {
        console.log(files[0].relativePath, innerFile);
        if (this.fileTypes.includes(innerFile.type)) {
          this.trackForm.controls.file.setValue(innerFile);
        } else {
          console.log(innerFile.type);
        }
      });
    }
    // for (const droppedFile of files) {
    //
    //   // Is it a file?
    //   if (droppedFile.fileEntry.isFile) {
    //     const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
    //     fileEntry.file((file: File) => {
    //       console.log(droppedFile.relativePath, file);
    //       this.trackForm.controls.file.setValue(file);
    //     });
    //   } else {
    //     const fileEntry = droppedFile.fileEntry as FileSystemDirectoryEntry;
    //     console.log(droppedFile.relativePath, fileEntry);
    //   }
    // }
  }

  get f() {
    return this.trackForm.controls;
  }

  public fileOver(event) {
    console.log(event);
  }

  public fileLeave(event) {
    console.log(event);
  }

  onSubmit() {
    if (!this.trackForm.valid) {
      return;
    }
    const formData = formDataCreator(this.trackForm.value);

    this.tracksService.createNewTrack(formData).subscribe(
      res => {
        this.tracksService.getTrackList().subscribe(list => {
          this.tracksService.currentTrackListSubj.next(list);
        });
      },
      err => console.log(err)
    );
    this.dialogRef.close();

    // this.tracksService
  }


  play() {
    if (!this.isPlaying && this.f.file.value) {
      this.isPlaying = true;
      this.audio.src = URL.createObjectURL(this.f.file.value);
      this.audio.load();
      this.circumference = 2 * Math.PI * 40;
      this.strokeDashoffset = this.trackTime;
      // this.durationTime = Math.round(this.audio.duration);
      this.audio.play();

      //   if (this.subscribe === null || this.subscribe.closed) {
      //     this.subscribe = this.source.subscribe(() => {
      //       if (this.trackDuration.milliseconds() !== 0) {
      //         if (this.audio.ended || this.trackDuration.seconds() === 0 && this.trackDuration.minutes() === 0) {
      //           this.subscribe.unsubscribe();
      //           this.isPlaying = false;
      //           this.audio.load();
      //           this.trackDuration = moment.duration(0, 'seconds');
      //         } else {
      //           if (this.audio.paused) {
      //             this.isPlaying = false;
      //             this.trackDuration = moment.duration(this.trackDuration, 'seconds');
      //           } else {
      //             this.isPlaying = true;
      //             this.trackDuration = moment.duration(this.trackDuration, 'seconds').subtract(1, 'seconds');
      //           }
      //         }
      //       } else {
      //         this.trackDuration = moment.duration(this.audio.duration, 'seconds');
      //       }
      //     });
      //   }
    } else {
      // this.subscribe.unsubscribe();
      this.audio.pause();
      // this.trackDuration = moment.duration(this.trackDuration, 'seconds');
      this.isPlaying = false;
    }

  }

}

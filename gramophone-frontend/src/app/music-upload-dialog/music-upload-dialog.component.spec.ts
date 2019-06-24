import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MusicUploadDialogComponent } from './music-upload-dialog.component';

describe('MusicUploadDialogComponent', () => {
  let component: MusicUploadDialogComponent;
  let fixture: ComponentFixture<MusicUploadDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MusicUploadDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MusicUploadDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

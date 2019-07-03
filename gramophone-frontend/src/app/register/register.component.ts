import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../_services';
import {ScrollToEvent} from '@nicky-lenaers/ngx-scroll-to/lib/scroll-to-event.interface';
import {
  ScrollToAnimationEasing,
  ScrollToConfigOptions,
  ScrollToOffsetMap,
  ScrollToService
} from '@nicky-lenaers/ngx-scroll-to';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;
  public ngxScrollToDestination: string;
  public ngxScrollToEvent: ScrollToEvent;
  public ngxScrollToDuration: number;
  public ngxScrollToEasing: ScrollToAnimationEasing;
  public ngxScrollToOffset: number;
  public ngxScrollToOffsetMap: ScrollToOffsetMap;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthenticationService,
    private scrollToService: ScrollToService
  ) {
  }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      password2: ['', Validators.required]
    });
  }


  get f() {
    return this.registerForm.controls;
  }


  onSubmited() {
    if (this.registerForm.invalid) {
      return;
    }

    this.authService.register(this.f.username.value, this.f.email.value, this.f.password.value, this.f.password2.value).subscribe(resp => {
      console.log(resp);
    });
    this.registerForm.reset({});

    const config: ScrollToConfigOptions = {
      container: 'custom-container',
      target: 'destination-1',
      duration: 3000,
      easing: 'easeOutElastic',
      offset: 20
    };

    this.scrollToService.scrollTo(config);

  }
}

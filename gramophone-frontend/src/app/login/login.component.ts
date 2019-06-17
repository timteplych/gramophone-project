import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../_services';
import {ActivatedRoute, Router} from '@angular/router';

declare var $: any;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loading = false;
  submitted = false;
  returnUrl: string;
  loginForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private authService: AuthenticationService
  ) {
    this.authService.currentUser.subscribe(value => {
      this.loading = !!value;
    });
  }

  ngOnInit() {

    this.loginForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  get f() {
    return this.loginForm.controls;
  }

  // onClick() {
  //
  //     .sidebar({
  //       dimPage: false,
  //       context: $('body')
  //     })
  //     .sidebar('setting', 'transition', 'overlay')
  //     .sidebar('toggle');
  // }

  onSubmited() {
    if (this.loginForm.invalid) {
      return;
    }
    this.authService.login(this.f.email.value, this.f.password.value).subscribe(data => {
      // this.loading = true;
      },
      error => {
        this.loading = false;
      });
    $('.login.sidebar').sidebar('toggle');
  }

}

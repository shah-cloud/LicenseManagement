/**
 * @description
 * Component for login form
 */
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import swal from 'sweetalert';

/**
 * @description
 * Component for login form
 */
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  /**form containing username and password form controls */
  loginForm: FormGroup;
  /**to enable/disable the login button */
  logging = false;
  /**to show error msg */
  errorMsg = '';


  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,

  ) { }

  ngOnInit() {
    this.createForm();
  }

  /**getter for easier access to form controls in html */
  get username() { return this.loginForm.get('username'); }
  get password() { return this.loginForm.get('password'); }

  /**creates an instance of @class FormGroup */
  createForm = () => {
    this.loginForm = this.fb.group({
      username: ['ajay', Validators.required],
      password: ['12345', Validators.required]
    });
  }

  /**called on click of form submit button*/
  onSubmit() {

    if (!this.loginForm.valid) {
      this.showError();
      return;
    }
    this.login();
    swal({
      icon: "success",
      title: "Login Successfully",
      text: 'Redirecting...',
      timer: 500,
      buttons: [false],
    });


  }

  /**
   * shows error message passed by @method onSubmit 
   * if @param {string} msg is defined, use this error msg i.e server side error,
   * else check and show input validation errors
   */
  showError(msg?: string) {

    if (msg) { this.errorMsg = msg; return; }
    if (this.loginForm.controls.username.invalid) {
      this.errorMsg = 'Please enter username';
      return;
    }
    if (this.loginForm.controls.password.invalid) {
      this.errorMsg = 'Please enter password';
      return;
    }
  }

  /**
   * sends login request, when successfull, sends userFetch request and save user data, handles error
   */
  login() {

    this.logging = true;
    this.errorMsg = '';

    this.authService.login(this.loginForm.value)
      .subscribe((res: any) => {
        this.authService.saveToken(res.access_token);
        this.authService.fetchUserDetails()
          .subscribe((resp: any) => {
            this.authService.saveUserDetails(resp);
            this.navigate();
          }, (err: any) => {
            localStorage.clear();
            this.logging = false;
            this.showError(err.msg);
          });
      }, (err: any) => {
        this.logging = false;
        if (err.status === 400) {
          this.showError('Username or Password is incorrect');
        }
      });
  }

  /**navigate to control-room interface after successfull login */
  navigate() {
    this.router.navigate(['/projects']);
  }
}

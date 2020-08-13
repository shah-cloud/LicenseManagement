import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private api: ApiService,
  ) { }

  login(data: any) {
    return this.api.post(`oauth/token?grant_type=password&username=${data.username}&password=${data.password}`, {});
  }

  isLoggedIn() { return localStorage.getItem('access_token') ? true : false; }

  saveToken(token: string) {
    localStorage.setItem('access_token', token);
  }

  fetchUserDetails() {
    return this.api.get('api/me');
  }

  saveUserDetails(userInfo: any) {
    localStorage.setItem('userInfo', JSON.stringify(userInfo));
    localStorage.setItem('userAuthorities', JSON.stringify(userInfo.authorities));
  }
  /**indicates server to send the OTP to user*/
  forgotPassword(username: string) {
    return this.api.get(`forgot-password/${username}`);
  }

  resetPassword(payLoad: any) {
    return this.api.post(`forgot-password`, payLoad);
  }
}

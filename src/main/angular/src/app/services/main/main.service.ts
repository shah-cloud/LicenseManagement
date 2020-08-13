import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';

@Injectable({
  providedIn: 'root'
})
export class MainService {

  constructor(private api:ApiService ) { }
  getLoginUser(){
    return this.api.get('api/me');
  }
}

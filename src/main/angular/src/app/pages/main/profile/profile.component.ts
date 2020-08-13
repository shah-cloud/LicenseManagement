import { MainService } from './../../../services/main/main.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  UserDetail: any;
  UserAuthorities: any;
  UserRole
  UserName
  Name
  constructor(private mainService:MainService) { }

  ngOnInit() {
    this.mainService.getLoginUser().subscribe(data=>{
      console.log(data)
      this.UserDetail = data
      this.Name = data.name
      this.UserName = data.username
      this.UserAuthorities =data.authorities
      this.UserRole = data.roles
    })
  }

}

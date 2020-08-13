import { StorageService } from './../../../services/storage/storage.service';
import { AdminService } from './../../../services/admin/admin.service';
import { Component, OnInit } from '@angular/core';
import { _keyValueDiffersFactory } from '@angular/core/src/application_module';
import { hasLifecycleHook } from '@angular/compiler/src/lifecycle_reflector';
import { Router }  from '@angular/router';
import swal from 'sweetalert';
@Component({
  selector: 'app-role',
  templateUrl: './role.component.html',
  styleUrls: ['./role.component.scss']
})
export class RoleComponent implements OnInit {
  public roles = [];
  isloader:boolean=true
  authorities: any;
  AuthoritiesName: any;
 
  constructor(private _adminService: AdminService,
    private route:Router,
    private _storageService:StorageService ) { }

  ngOnInit() {
    this._adminService.getRoles()
      .subscribe(data => {
        this.roles = data
        console.log(this.roles)
        this.isloader=false
      },error => {
        console.log(error);
      })  
  }

  hasAuthority(authority){
    const authorities:any[] = this._storageService.getData('userAuthorities').map(a=>a.name);
    return authorities.includes(authority);
  }

  
  editrole(item) {
    this._adminService.selecetedRole.next(item);
    this.route.navigate(['roles',item.id])
  }
  
  deleterole(item) {
    swal({
      title: "You sure?",
      text: "You want to go ahead with deletion?",
      icon: "warning",
      closeOnClickOutside:false,
      buttons:["Yes","No"],
      dangerMode: true,
    })
    .then(willDelete => {
      if (willDelete) {    
      }
      else {
        this._adminService.deleteRole(item.id).subscribe(data => {
          item.active = false;
          swal("Delete successfully!");
        },
          error => {
            console.log(error);
    
          })
        
      }
    });
   

  }
  activaterole(item)
  {
    swal({
      title: "Are you sure?",
      text: "Are you sure that you want to activate this?",
      icon: "warning",
      closeOnClickOutside:false,
      buttons:["Yes","No"],
      dangerMode: true,
    })
    .then(willDelete => {
      if (willDelete) {    
      }
      else {
    this._adminService.deleteRole(item.id).subscribe(data => {
    item.active = true;
    swal("Activate successfully!");
  },
    error => {
      console.log(error);

    })
    
  }
  })
  }
   
  createRole(){
   this.route.navigate(['roles/create'])
  }
  
}

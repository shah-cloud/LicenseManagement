import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormArray, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Component, OnInit, OnChanges } from '@angular/core';
import { AdminService } from './../../../../services/admin/admin.service';
import swal from 'sweetalert';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss'],
})
export class CreateUserComponent implements OnInit {
  createUserForm: FormGroup
  dropdownList = [];
  selectedItems = [];
  dropdownSettings = {};
  userId
  loaderbutton: boolean = false;
  constructor(private fb: FormBuilder,
    private _adminService: AdminService,
    private route: Router,
    private activate: ActivatedRoute) { }


  ngOnInit() {
    this.createUserForm = this.fb.group({
      name: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      contactNo: ['', [Validators.required, Validators.pattern("^[0-9]*$")
        , Validators.minLength(10), Validators.maxLength(10)]],
      roleIds: this.fb.array([], [Validators.required])
    })

    this._adminService.getRoles().subscribe
      (data => {
        this.dropdownList = data
      }, error => {
      }
      )


    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'name',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: true
    };


    this.activate.params.subscribe(params => {
      this.userId = params['id']
    })

    this.patchavalue()
  }


  patchavalue() {
    if (this.userId) {
    this._adminService.selecetedUser.subscribe(data => {
      if(Object.keys(data).length){
        this.createUserForm.patchValue(data)
        this.selectedItems = data.roles;
        const roleIds = <FormArray>this.createUserForm.controls['roleIds'];
        if (data.roles) {
          data.roles.forEach(role => {
            roleIds.push(new FormControl(role.id))
          });
        }
      }
      else{
       this._adminService.getUserById(this.userId).subscribe(data=>{
        this.createUserForm.patchValue(data)
        this.selectedItems = data.roles;
        const roleIds = <FormArray>this.createUserForm.controls['roleIds'];
        if (data.roles) {
          data.roles.forEach(role => {
            roleIds.push(new FormControl(role.id))
          });
        }
       })
      }   
    })
  }}


  onItemSelect(item: any) {
    const roleIds = <FormArray>this.createUserForm.controls['roleIds'];
    roleIds.push(new FormControl(item.id));
  }

  onItemDeSelect(item: any) {
    const roleIds = <FormArray>this.createUserForm.controls['roleIds'];
    roleIds.removeAt(roleIds.value.indexOf(item.id));
  }
  onSelectAll(items: any[]) {
    const roleIds = <FormArray>this.createUserForm.controls['roleIds'];
    items.forEach(e => {
      roleIds.push(new FormControl(e.id));
    });
  }
  onDeSelect(items: any[]) {
    const roleIds = <FormArray>this.createUserForm.controls['roleIds'];
    items.forEach(e => {
      roleIds.removeAt(roleIds.value.indexOf(e.id));
    });
  }



  onSubmit() {

    this.loaderbutton = true
    if (this.userId) {
      this._adminService.updateUser(this.userId, this.createUserForm.value)
        .subscribe(data => {
          this.route.navigate(['users'])
          swal("Update User Successfully!");

        },
          error => {
          })
    }
    else {
      this._adminService.addUser(this.createUserForm.value).
        subscribe(data => {
          this.route.navigate(['users'])
          swal("New User Added Successfully!");
        },
          error => {
          })
    }
  }
  goback() {
    this.route.navigate(['users'])
  }
  cancel() {
    this.patchavalue()
  }

}

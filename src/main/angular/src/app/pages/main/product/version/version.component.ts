import { StorageService } from './../../../../services/storage/storage.service';
import { ProductService } from './../../../../services/product/product.service';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import swal from 'sweetalert';

@Component({
  selector: 'app-version',
  templateUrl: './version.component.html',
  styleUrls: ['./version.component.scss']
})
export class VersionComponent implements OnInit {
  Versions = [];
  versionId;
  createVersionForm: FormGroup;
  isCreateVersion: boolean = false
  isloader:boolean= true
  loaderbutton:boolean=false;
  formDetail: any;
  //isVersionTable: boolean = true
  constructor(
    private _productservice: ProductService,
    private fb: FormBuilder,
    private activate: ActivatedRoute,
    private _storageService:StorageService) { }

  ngOnInit() {
    this.getversions();
    this.createVersionForm = this.initProjectForm();
  }
  hasAuthority(authority){
    const authorities:any[] = this._storageService.getData('userAuthorities').map(a=>a.name);
    return authorities.includes(authority);
  }
  getversions() {
    this._productservice.getVersions().subscribe(data => {
      console.log(data)
      this.Versions = data
      this.isloader = false

    })
  }
  initProjectForm() {
    return this.fb.group({
      "version": ["", [Validators.required]],
    });
  }
  onSubmit() {
    this.loaderbutton=true
    if (this.versionId) {
      this._productservice.updateVersions(this.versionId, this.createVersionForm.value)
        .subscribe(data => {
          swal("Version updated successfully!");
          this.getversions()
          this.createVersionForm.reset()
          this.isCreateVersion = false
          this.versionId = ''
          this.loaderbutton=false
        }, error => {
          this.loaderbutton=false
        })
    }
    else {
      this._productservice.addVersions(this.createVersionForm.value).
        subscribe(data => {
          swal("Version added successfully!");
          this.getversions()
          this.createVersionForm.reset()
          this.isCreateVersion = false
          this.loaderbutton=false
        },
          error => {
            this.loaderbutton=false
          })
    }
  }
  deleteVersion(version) {
    swal({
      title: "You sure?",
      text: "You want to go ahead with deletion?",
      icon: "warning",
      closeOnClickOutside: false,
      buttons: ["Yes", "No"],
      dangerMode: true,
    })
      .then(willDelete => {
        if (willDelete) {
        }
        else {
          this._productservice.deleteVersions(version.id).subscribe(data => {

            this.getversions()
            swal("Version deleted successfully");
          },
            error => {
            })

        }
      });
  }
  editVersion(version) {
    this.isCreateVersion = true
    //console.log(version.version)
    this.versionId = version.id
    this.formDetail=version
    this.createVersionForm.patchValue({
      version: version.version
    })
  }
  showVersionForm(){
    this.isCreateVersion = true
    this.createVersionForm.reset()
  }
  Reset(){
    this.createVersionForm.patchValue(this.formDetail);
  }
  close(){
    this.isCreateVersion = false
  }
}

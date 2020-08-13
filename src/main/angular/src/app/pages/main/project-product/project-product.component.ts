import { StorageService } from './../../../services/storage/storage.service';
import { MainService } from './../../../services/main/main.service';
import { ProjectService } from 'src/app/services/project/project.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import swal from 'sweetalert';

@Component({
  selector: 'app-project-product',
  templateUrl: './project-product.component.html',
  styleUrls: ['./project-product.component.scss']
})
export class ProjectProductComponent implements OnInit {
  projectProduct
  isloader: boolean = true
  showModal: boolean = false
  showCommentModal: boolean = false
  popUpForm: FormGroup;
  commentValue: any;
  commentSubmitButton: string;
  selectedProduct: any;
  comments: any;
  userId: any;
  constructor(private _projectService:ProjectService,
    private _storageService:StorageService,
    private mainService:MainService,
    private route: Router,
    private fb: FormBuilder) { }

  ngOnInit() {
    this.getProjectProducts()
    this.mainService.getLoginUser().subscribe(data=>{
      console.log(data)
      this.userId = data.id
    })
    this.popUpForm = this.initpopUpForm();
  }
  getProjectProducts()
  {
    this._projectService.getProjectProducts().subscribe(data=>{
      console.log(data)
      this.projectProduct = data
      this.isloader = false
    })
  }
  hasAuthority(authority){
    const authorities:any[] = this._storageService.getData('userAuthorities').map(a=>a.name);
    return authorities.includes(authority);
  }
  initpopUpForm() {
    return this.fb.group({
      "comment": [""]
    })
  }
  deleteProduct(project){
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
          this._projectService.deleteProduct(project.id).subscribe(data => {
            console.log(data)
            swal("Delete Successfully!")
            this.getProjectProducts()
          },
            error => {
              console.log("error");
            })
        }
      });
  }

  editProduct(product) {
    this._projectService.selecetedProduct.next(product);
    this.route.navigate([`projects/${product.projectId}/product/${product.id}`])
  }

  onSubmitComment() {
    this.showModal = false;
    switch (this.commentSubmitButton) {
      case 'Submit':
        swal({
          title: "Are you sure?",
          text: "You want to Submit this?",
          icon: "warning",
          closeOnClickOutside: false,
          buttons: ["Yes", "No"],
          dangerMode: true,
        })
          .then(willDelete => {
            if (willDelete) {
            }
            else {
              this._projectService.submitProductStatus(this.selectedProduct.id, this.popUpForm.value).subscribe(
                data => {
                  console.log(data)
                  console.log("submit")
                  this.selectedProduct.status = 'SUBMIT'
                   this.getProjectProducts()
                  swal("Project Submit successfully!");
                }
              )
            }
          });
        break;
      case 'Reject':
        swal({
          title: "Are you sure?",
          text: "You want to Reject this?",
          icon: "warning",
          closeOnClickOutside: false,
          buttons: ["Yes", "No"],
          dangerMode: true,
        })
          .then(willDelete => {
            if (willDelete) {
            }
            else {
              this._projectService.rejectProductStatus(this.selectedProduct.id, this.popUpForm.value).subscribe(
                data => {
                  console.log("rejected")
                  this.selectedProduct.status = 'REJECTED'
                  this.getProjectProducts()
                  swal("Project Reject successfully!");
                }
              )
            }
          });
        break;
      case 'Review':
        swal({
          title: "Are you sure?",
          text: "You want to Review this?",
          icon: "warning",
          closeOnClickOutside: false,
          buttons: ["Yes", "No"],
          dangerMode: true,
        })
          .then(willDelete => {
            if (willDelete) {
            }
            else {
              this._projectService.reviewProductStatus(this.selectedProduct.id, this.popUpForm.value).subscribe(
                data => {
                  console.log("review")
                  this.selectedProduct.status = 'REVIEWED'
                  this.getProjectProducts()
                  swal("Project Review successfully!");
                }
              )
            }
          });
        break;
      case 'Approved':
        swal({
          title: "Are you sure?",
          text: "You want to Approve this?",
          icon: "warning",
          closeOnClickOutside: false,
          buttons: ["Yes", "No"],
          dangerMode: true,
        })
          .then(willDelete => {
            if (willDelete) {
            }
            else {
              this._projectService.approveProductStatus(this.selectedProduct.id, this.popUpForm.value).subscribe(
                data => {
                  console.log("approved")
                  this.selectedProduct.status = 'APPROVED'
                  this.getProjectProducts()
                  swal("Project Approve successfully!");
                }
              )
            }
          });
        break;
    }
  }
  submitProductStatus(project) {
    this.showModal = true;
    this.popUpForm.reset();
    this.commentSubmitButton = 'Submit'
    this.selectedProduct = project
  }
  reviewProductStatus(project) {
    this.showModal = true;
    this.popUpForm.reset();
    this.commentSubmitButton = 'Review'
    this.selectedProduct = project
  }
  approveProductStatus(project) {
    this.showModal = true;
    this.popUpForm.reset();
    this.commentSubmitButton = 'Approved'
    this.selectedProduct = project
  }
  rejectProductStatus(project) {
    this.showModal = true;
    this.popUpForm.controls['comment'].setValidators(Validators.required);
    this.commentSubmitButton = 'Reject'
    this.selectedProduct = project;
    this.popUpForm.reset();
  }
  hide() {
    this.showModal = false;
  }




  hideCommentModel(){
    this.showCommentModal=false;
  }
  showComments(project){
    this.comments = project.comments
    console.log(this.comments) 
    if(this.comments.length > 0){
     this.showCommentModal=true
    }
    else{
      swal("No Comments Found")
    }
  }

  hasUserId(c){
  if(c.commentedById ==this.userId)
  {
    return true
  }
  else{
    return false
  }
}
}

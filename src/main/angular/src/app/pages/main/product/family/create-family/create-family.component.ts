import { Router, ActivatedRoute } from '@angular/router';
import { ProductService } from './../../../../../services/product/product.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder, FormControl, FormArray } from '@angular/forms';

@Component({
  selector: 'app-create-family',
  templateUrl: './create-family.component.html',
  styleUrls: ['./create-family.component.scss']
})
export class CreateFamilyComponent implements OnInit {
  createFamilyForm: FormGroup;
  productCodes: FormArray;
  familyId
  constructor(private fb:FormBuilder,
    private _productService:ProductService,
    private route:Router,
    private activateroute:ActivatedRoute) { }

  ngOnInit() {
    this.createFamilyForm =this.initCreateFamilyForm()
    this.activateroute.params.subscribe(
      params => {
        this.familyId = params['id'] 
      })
      this.patchavalue()
  }
  initCreateFamilyForm() {
    return this.fb.group({
      "name": ["", [Validators.required]],
      "code": ["", [Validators.required]],
      "description": ["", [Validators.required]],
      "productCodes": this.fb.array([
       this.productcode()
      ])
    });
  }
  productcode(){
    return this.fb.group({
      "id": ["",[]],
      "name":["",[Validators.required]]
    });
  }
  onAddName(): void {
    this.productCodes = this.createFamilyForm.get('productCodes') as FormArray;
    this.productCodes.push(this.productcode());
  }

  onSubmit() {
    if(this.familyId){
      console.log(this.createFamilyForm.value)
      this._productService.updateProductFamily(this.familyId,this.createFamilyForm.value)
      .subscribe(data=>{
        console.log()
        this.route.navigate(['products/family'])
      })
    }else{
   console.log(this.createFamilyForm.value)
    this._productService.addProductFamily(this.createFamilyForm.value)
    .subscribe(data=>{
      console.log()
      this.route.navigate(['products/family'])
    })
  }}

  patchavalue(){
    if(this.familyId){
  this._productService.selecetedFamily.subscribe(data => {
    console.log(data)
    this.createFamilyForm.patchValue(data)
    //this.createFamilyForm.controls['productCodes'].patchValue(data.productCodes);

  })

}
}
}
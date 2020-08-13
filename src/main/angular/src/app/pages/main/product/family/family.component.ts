import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { ProductService } from './../../../../services/product/product.service';

@Component({
  selector: 'app-family',
  templateUrl: './family.component.html',
  styleUrls: ['./family.component.scss']
})
export class FamilyComponent implements OnInit {
  Family= []
  isloader:boolean= true
  constructor(private _productService :ProductService,
    private route:Router) { }

  ngOnInit() {
    this._productService.getProductFamilies().subscribe(
      data=>{
        this.Family= data
        console.log(data)
        this.isloader = false
      }
    )
  }
  addProductFamily(){
this.route.navigate(['products/family/create'])
  }
  editFamilyDescription(family){
    console.log(family.id)
    this._productService.selecetedFamily.next(family);
    this.route.navigate(['products/family',family.id])
  }
  deleteFamilyDescription(family){
    this._productService.deleteProductFamily(family.id).subscribe(data=>{
       this.Family.splice(this.Family.findIndex(pd => pd.id == family.id))
    })
  }
}

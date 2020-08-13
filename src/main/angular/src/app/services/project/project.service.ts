import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  selecetedProduct : BehaviorSubject<any> = new BehaviorSubject<any>({});
  constructor(private api:ApiService ) { }
  getProjects(){
    return this.api.get('api/projects');
  }
  addProject(data){
    return this.api.post('api/project', data)
  }
  getProduct(){
    return this.api.get('api/project/product')
  }
  addProduct(data){
    return this.api.post('api/project/product',data)
  }
  deleteProduct(Id){
    return this.api.delete(`api/project/product/${Id}`);
  }
  updateProduct(Id,data){
    return this.api.put(`api/project/product/${Id}`,data)
  }
  submitProductStatus(Id, data){
    return this.api.put(`api/project/product/${Id}/submit`,data)
  }
  reviewProductStatus(Id,data){
    return this.api.put(`api/project/product/${Id}/review`,data)
  }
  approveProductStatus(Id, data){
    return this.api.put(`api/project/product/${Id}/approve`,data)
  }
  rejectProductStatus(Id, data){
    return this.api.put(`api/project/product/${Id}/reject`,data)
  }
  getProjectTypes(){
    return this.api.get('api/project/types');
  }
  getProjectManager(){
     return this.api.get('api/users/project-manager')
 }
  getCustomer(){
     return this.api.get('api/users/customer')
 }
 getProductsByProjectId(projectId){
  return this.api.get(`api/project/${projectId}/product`)
 }
 getProjectProducts(){
  return this.api.get('api/project/product')
 }
 getLicenseType(){
   return this.api.get('api/license/types')
 }

}

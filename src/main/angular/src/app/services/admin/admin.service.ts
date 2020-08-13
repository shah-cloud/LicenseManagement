import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';
import { BehaviorSubject } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private api:ApiService) { }
  selecetedRole : BehaviorSubject<any> = new BehaviorSubject<any>({})
  selecetedUser : BehaviorSubject<any> = new BehaviorSubject<any>({});
  getRoles(){
    return this.api.get('api/roles');
  }
   
  getUsers(){
   return this.api.get('api/users');
  }

  updateRole(roleId, data){
    return this.api.put(`api/role/${roleId}`, data);
  }
   
  updateUser(Id, data)
  {
    return this.api.put(`api/user/${Id}`, data)
  }

  addRole(data){
    return this.api.post('api/role', data)
  }
  
  addUser(data){
    return this.api.post('api/user',data)
  }

  deleteRole(roleId){
    return this.api.delete(`api/role/${roleId}`);
  }
  
  deleteUser(userId){
   return this.api.delete(`api/user/${userId}`);
  }


  getauthorities(){
    return this.api.get('api/authorities');
  }
  
  activateRole(roleId){
    return this.api.put(`api/role/${roleId}/activate`,{});
  }

  activateUser(userId){
    return this.api.put(`api/user/${userId}/activate`,{});
  }

  logout(){
    return this.api.get('api/me/logout')
  }
   
  getRoleById(roleId){
    return this.api.get(`api/role/${roleId}`)
  }

  getUserById(userId){
    return this.api.get(`api/user/${userId}`)
  }
}

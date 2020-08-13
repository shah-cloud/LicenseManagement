import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainComponent } from './main.component';
import { MainRoutingModule } from './main-routing.module';
import { UserComponent } from './user/user.component';
import { CreateUserComponent } from './user/create-user/create-user.component';
import { RoleComponent } from './role/role.component';
import { CreateRoleComponent } from './role/create-role/create-role.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { ProjectComponent } from './project/project.component';
import { CreateProjectComponent } from './project/create-project/create-project.component';
import { ProductComponent } from './product/product.component';
import { VersionComponent } from './product/version/version.component';
import { FamilyComponent } from './product/family/family.component';
import { DetailComponent } from './product/detail/detail.component';
import { AddProductComponent } from './project/add-product/add-product.component';
import { ProfileComponent } from './profile/profile.component';
import { ProjectProductComponent } from './project-product/project-product.component';
import { CreateFamilyComponent } from './product/family/create-family/create-family.component';


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    MainRoutingModule,ReactiveFormsModule,
    NgMultiSelectDropDownModule.forRoot(),
  ],
  declarations: [MainComponent, UserComponent,
     CreateUserComponent, RoleComponent, CreateRoleComponent, ProjectComponent, CreateProjectComponent, ProductComponent, VersionComponent, FamilyComponent, DetailComponent, AddProductComponent, ProfileComponent, ProjectProductComponent, CreateFamilyComponent,
     ]
})
export class MainModule { }

import { VersionComponent } from './product/version/version.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './main.component';
import { UserComponent } from './user/user.component';
import { CreateUserComponent } from './user/create-user/create-user.component';
import { RoleComponent } from './role/role.component';
import { CreateRoleComponent } from './role/create-role/create-role.component';
import { ProjectComponent } from './project/project.component';
import { CreateProjectComponent } from './project/create-project/create-project.component';
import { ProductComponent } from './product/product.component';
import { FamilyComponent } from './product/family/family.component';
import { DetailComponent } from './product/detail/detail.component';
import { AddProductComponent } from './project/add-product/add-product.component';
import { ProfileComponent } from './profile/profile.component';
import { ProjectProductComponent } from './project-product/project-product.component';
import { CreateFamilyComponent } from './product/family/create-family/create-family.component';

const routes: Routes = [
  {
    path: '', component: MainComponent,
    children: [
      { path: 'roles', component: RoleComponent },
      { path: 'roles/create', component: CreateRoleComponent,
       //canDeactivate:[DeactivateGuard]
      },
      { path: 'roles/:id', component: CreateRoleComponent,
       //canDeactivate:[DeactivateGuard]
      },
      { path: 'users', component: UserComponent },
      { path: 'users/create', component: CreateUserComponent},
      { path: 'users/:id', component: CreateUserComponent},
      { path: 'projects', component: ProjectComponent },
      { path: 'projects/create', component: CreateProjectComponent },
      { path: 'projects/:id/product', component: AddProductComponent },
      { path: 'projects/:id/product/:productId', component: AddProductComponent },
      { path: 'products', component: ProductComponent,
        children:[
          {path: 'version', component: VersionComponent},
          {path: 'family', component: FamilyComponent},
          {path:'family/create', component:CreateFamilyComponent},
          {path:'family/:id', component:CreateFamilyComponent},
          {path: 'detail', component:DetailComponent }
        ]
      },
      {path:'profile', component:ProfileComponent},
      {path:'projectproduct', component:ProjectProductComponent},
     

    ]
  }
]
@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [RouterModule]
})
export class MainRoutingModule { }

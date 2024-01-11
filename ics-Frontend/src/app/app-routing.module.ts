import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {GalleryComponent} from "./analyse/components/gallery/gallery.component";
import {ImageAnalyzerComponent} from "./analyse/components/image-analyzer/image-analyzer.component";
import {ImageContainerComponent} from "./analyse/components/image-container/image-container.component";
import {LoginComponent} from "./analyse/components/login/login.component";
import {RegisterComponent} from "./analyse/components/register/register.component";

const routes: Routes = [
  {path: "gallery", component: GalleryComponent},
  {path: "index", component: ImageAnalyzerComponent},
    { path: "login", component: LoginComponent },
    { path: "register", component: RegisterComponent },
    { path: "", redirectTo: '/login', pathMatch: 'full' }
  //{path: "tags", component: ImageContainerComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const RoutingComponents = [GalleryComponent, ImageAnalyzerComponent, ImageContainerComponent];

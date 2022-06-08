import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageContainerComponent } from './components/image-container/image-container.component';
import { ImageAnalyzerComponent } from './components/image-analyzer/image-analyzer.component';
import { ReactiveFormsModule} from "@angular/forms";
import { GalleryComponent } from './components/gallery/gallery.component';
import {AppRoutingModule, RoutingComponents} from "../app-routing.module";
import {RouterModule} from "@angular/router";
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { ScrollingModule } from '@angular/cdk/scrolling';
import {OverlayModule} from '@angular/cdk/overlay';
import { MatSliderModule } from '@angular/material/slider';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@NgModule({
  declarations: [
    ImageContainerComponent,
    ImageAnalyzerComponent,
    GalleryComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AppRoutingModule,
    MatAutocompleteModule,
    FontAwesomeModule,
    MatFormFieldModule,
  ],
  exports:[

  ]
})
export class AnalyseModule { }

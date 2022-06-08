import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageAnalyzerComponent } from './image-analyzer.component';

describe('ImageAnalyzerComponent', () => {
  let component: ImageAnalyzerComponent;
  let fixture: ComponentFixture<ImageAnalyzerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImageAnalyzerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImageAnalyzerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

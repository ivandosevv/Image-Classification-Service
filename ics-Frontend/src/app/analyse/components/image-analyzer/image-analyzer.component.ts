import {
    Component,
    ElementRef,
    EventEmitter,
    OnInit,
    Output,
    ViewChild,
    ViewEncapsulation
} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StorageService} from "../../services/storage.service";
import {DatePipe} from "@angular/common";
import {Tag} from "../../models/tag-model.models";
import {Image} from "../../models/image-model.models";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {ImageContainerComponent} from "../image-container/image-container.component";
import {GalleryComponent} from "../gallery/gallery.component";
import {takeUntil} from "rxjs/operators";
import {Subject} from "rxjs";
import {AuthService} from "../../services/auth.service";

@Component({
    selector: 'app-image-analyzer',
    templateUrl: './image-analyzer.component.html',
    styleUrls: ['./image-analyzer.component.css'],
    encapsulation: ViewEncapsulation.None,
})
export class ImageAnalyzerComponent implements OnInit {

    private _imgFormGroup: FormGroup;
    private _loadPopUp: boolean;
    private _tags$!: Tag[];
    private _images: Image[] = [];
    private _image?: Image;

    constructor(private storageService: StorageService,
                private authService: AuthService,
                private formBuilder: FormBuilder,
                private _elementRef: ElementRef) {
        this._loadPopUp = false;
        this._imgFormGroup = this.formBuilder.group({
            inputURL: ['', [Validators.required]],
        });

        if (!localStorage.getItem("connectGallery")!) {
            localStorage.removeItem("connectGallery");
        }
    }

    ngOnInit(): void {
    }

    public validURL(str: string): boolean {
        var pattern = new RegExp('^(https?:\\/\\/)?' + // protocol
            '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // domain name
            '((\\d{1,3}\\.){3}\\d{1,3}))' + // OR ip (v4) address
            '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // port and path
            '(\\?[;&a-z\\d%_.~+=-]*)?' + // query string
            '(\\#[-a-z\\d_]*)?$', 'i'); // fragment locator
        return pattern.test(str);
    }

    public addURL(event: Event): void {
        event.preventDefault();
        const currUser = this.authService.getCurrentUser();
        if (!currUser) {
            console.error('No current user found');
            return;
        }

        if (!this._imgFormGroup.valid) {
            alert('Current form is invalid');
            return;
        }

        const actualURL = this._imgFormGroup.get('inputURL')?.value;
        console.log(actualURL);

        if (!this.validURL(actualURL)) {
            alert("URL is not valid!");
            return;
        }

        const currentUser = this.authService.getCurrentUser();
        if (currentUser) {
            this.storageService.add(actualURL, currentUser.username, currentUser.password).subscribe(() => {
                // Call backend service to analyze the image and fetch tags
                this.storageService.analyzeImage(actualURL).subscribe(
                    (response: Tag[]) => {
                        this._tags$ = response;
                        this._loadPopUp = true; // Assuming this controls the display of tags
                    },
                    error => {
                        console.error('Error:', error);
                        console.log('Sent credentials:', currentUser.username, currentUser.password);
                    }
                );
            });
        }

        return;
    }


    public getImages(): Image[] {
        return this._images;
    }

    public exitPopUp(exit: boolean) {
        this._loadPopUp = false;

    }

    public get imgFormGroup(): FormGroup {
        return this._imgFormGroup;
    }

    public set imgFormGroup(value: FormGroup) {
        this._imgFormGroup = value;
    }

    public get loadPopUp(): boolean {
        return this._loadPopUp;
    }

    public set loadPopUp(value: boolean) {
        this._loadPopUp = value;
    }

    public get tags$(): Tag[] {
        return this._tags$;
    }

    public set tags$(value: Tag[]) {
        this._tags$ = value;
    }

    public get images(): Image[] {
        return this._images;
    }

    public set images(value: Image[]) {
        this._images = value;
    }

    public get image(): Image {
        return <Image>this._image;
    }

    set image(value: Image) {
        this._image = value;
    }
}

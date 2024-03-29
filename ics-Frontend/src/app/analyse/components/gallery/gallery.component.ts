import {
    Component,
    ElementRef,
    OnInit,
    SimpleChanges,
    ViewChild,
    ViewEncapsulation
} from '@angular/core';
import {Image} from "../../models/image-model.models";
import {StorageService} from "../../services/storage.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Form, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Tag} from "../../models/tag-model.models";
import {filter, map, Observable, startWith, timer} from "rxjs";
import {faSearch} from '@fortawesome/free-solid-svg-icons';
import {faExpand} from '@fortawesome/free-solid-svg-icons';
import {faSignOut} from '@fortawesome/free-solid-svg-icons';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-gallery',
    templateUrl: './gallery.component.html',
    styleUrls: ['./gallery.component.css'],
    encapsulation: ViewEncapsulation.None,
})
export class GalleryComponent implements OnInit {

    @ViewChild('inputTag') public inputTag: ElementRef<HTMLInputElement> | undefined;
    //const actualURL = this._imgFormGroup.get('inputURL')?.value;

    tagControl: FormControl = new FormControl();
    private _tagFormGroup: FormGroup;
    private _loadPopUp: boolean;
    private _image?: Image;
    private _images: Image[] = [];
    private _options: Tag[] = [];
    correctTags?: Tag[] = [];
    faSearch = faSearch;
    faExpand = faExpand;
    faSignOut = faSignOut;

    constructor(private storageService: StorageService, private authService: AuthService,
                private formBuilder: FormBuilder,
                private router: Router) {
        this._loadPopUp = false;
        this._tagFormGroup = this.formBuilder.group({
            inputTag: ['', [Validators.required]],
        });
    }

    public initialized = false;

    ngOnInit(): void {
        const currentUser = this.authService.getCurrentUser();
        // this.setImages();
        this.loadUserSpecificImages();

        this.storageService.getAllTags(currentUser.username, currentUser.password).subscribe(
            (response: Tag[]) => {
                this._options = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );

        this.tagControl.valueChanges.pipe(
            startWith(''),
            map(value => (typeof value === 'string' ? value : value.name)),
            map(name => (name ? this.filterProper(name) : this.options.slice()))
        ).subscribe((myTags: Tag[]) => this.correctTags = myTags);
    }

    private loadUserSpecificImages(): void {
        const currentUser = this.authService.getCurrentUser();
        console.log(currentUser);
        if (currentUser) {
            this.storageService.getImagesByUser(currentUser.username, currentUser.password).subscribe(
                (response: Image[]) => {
                    this._images = response;
                },
                (error: HttpErrorResponse) => {
                    if (error.status == 404) {
                        this._images = [];
                    } else {
                        console.error(error);
                    }
                }
            );
        } else {
            // Handle case when no user is logged in or redirect to login
        }
    }

    private filterProper(name: string): Tag[] {
        const filterValue = name.toLowerCase();
        return this.options.filter(option => option.en.toLowerCase().startsWith(filterValue));
    }

    displayProper(tag: Tag): string {
        return tag && tag.en ? tag.en : '';
    }

    public loadImagesByTag(): void {
        const currentUser = this.authService.getCurrentUser();
        this.storageService.getAll(currentUser.username, currentUser.password).subscribe(
            (response: Image[]) => {
                this.images = response;
                if (!this.inputTag?.nativeElement.value) {
                    alert("The form is invalid!");
                    return;
                }
                const currTag = this.inputTag.nativeElement.value;
                let curr = [];
                for (let img of this.images) {
                    for (let tag of img.connections) {
                        if (tag.name == currTag) {
                            curr.push(img);
                        }
                    }
                }

                this.images = curr;
            },
            (error: HttpErrorResponse) => {
                if (error.status == 404) {
                    this._images = [];
                } else {
                    console.error(error);
                }
            }
        );
    }

    public searchByTagParameter(actualTag: string): void {
        const currentUser = this.authService.getCurrentUser();
        //this.ngOnInit();

        if (actualTag == '') {
            this.ngOnInit();
            return;
        }

        this.storageService.getImagesByTag(actualTag, currentUser.username, currentUser.password).subscribe(
            (response: Image[]) => {
                this._images = response;
                console.log(this._images);
            },
            (error: HttpErrorResponse) => {
                if (error.status == 404) {
                    this._images = [];
                } else {
                    console.error(error);
                }
            });
    }

    public searchByTag(): void {
        const actualTag = this._tagFormGroup.get('inputTag')?.value;

        this.searchByTagParameter(actualTag);
    }

    public getImageDetails(url: string): void {
        for (let img of this._images) {
            if (img.url == url) {
                this.image = img;
                this._loadPopUp = true;
                break;
            }
        }
    }

    public exitPopUp(exit: boolean) {
        this._loadPopUp = exit;
        this.setImages();
    }

    get loadPopUp(): boolean {
        return this._loadPopUp;
    }

    set loadPopUp(value: boolean) {
        this._loadPopUp = value;
    }

    get image(): Image {
        return <Image>this._image;
    }

    set image(value: Image) {
        this._image = value;
    }

    get images(): Image[] {
        return this._images;
    }

    set images(value: Image[]) {
        this._images = value;
    }

    get tagFormGroup(): FormGroup {
        return this._tagFormGroup;
    }

    set tagFormGroup(value: FormGroup) {
        this._tagFormGroup = value;
    }

    get options(): Tag[] {
        return this._options;
    }

    set options(value: Tag[]) {
        this._options = value;
    }

    logout() {
        this.authService.logout();
        this.router.navigate(['/login']);
    }

    private setImages() {
        const currentUser = this.authService.getCurrentUser();
        if (!localStorage.getItem("connectGallery")!) {
            this.storageService.getImagesByUser(currentUser.username, currentUser.password).subscribe(
                (response: Image[]) => {
                    this._images = response;
                },
                (error: HttpErrorResponse) => {
                    if (error.status == 404) {
                        this._images = [];
                    } else {
                        console.error(error);
                    }
                }
            );
        } else {
            console.log(localStorage.getItem("connectGallery")!);
            this.storageService.getImagesByTag(localStorage.getItem("connectGallery")!, currentUser.username, currentUser.password).subscribe(
                (response: Image[]) => {
                    this._images = response;
                    localStorage.removeItem("connectGallery");
                },
                (error: HttpErrorResponse) => {
                    if (error.status == 404) {
                        this._images = [];
                    } else {
                        console.error(error);
                    }
                }
            );
        }
    }
}

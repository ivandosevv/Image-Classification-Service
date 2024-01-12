import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ImageService {
    private apiUrl = 'http://localhost:7067';

    constructor(private http: HttpClient) { }

    uploadImage(imageData: FormData): Observable<any> {
        const token = localStorage.getItem('token');
        const headers = new HttpHeaders({
        });

        return this.http.post(`${this.apiUrl}/upload`, imageData, { headers });
    }

}

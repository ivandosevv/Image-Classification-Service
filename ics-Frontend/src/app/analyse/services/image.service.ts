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

    addAndAnalyzeImage(imageUrl: string): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}/images`, imageUrl);
    }

    getTagsByImageId(id: number, username: string, password: string): Observable<Map<string, number>> {
        const headers = new HttpHeaders({
            'Content-Type':  'application/json',
            'Authorization': this.createBasicAuthToken(username, password)
        });

        return this.http.get<Map<string, number>>(`${this.apiUrl}/images/id/${id}/tags`, { headers });
    }

    private createBasicAuthToken(username: string, password: string): string {
        return 'Basic ' + btoa(username + ':' + password);
    }

}

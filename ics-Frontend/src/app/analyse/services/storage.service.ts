import { Injectable } from '@angular/core';
import {
    HttpClient,
    HttpErrorResponse,
    HttpHeaders,
    HttpParams, HttpResponse
} from "@angular/common/http";
import {catchError, map, Observable, tap, throwError} from "rxjs";
import { Image } from '../models/image-model.models'
import {Tag} from "../models/tag-model.models";

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private apiServerUrl = '';

  constructor(private httpClient: HttpClient) { }
  private readonly api = "http://localhost:7067";

    private getAuthHeaders(username: string, password: string): HttpHeaders {
        return new HttpHeaders({
            Authorization: this.createBasicAuthToken(username, password)
        });
    }

    public add(itemToAdd: string, username: string, password: string) {
        const headers = new HttpHeaders({ Authorization: this.createBasicAuthToken(username, password) });
        return this.httpClient.post(`${this.api}/images/analyze`, itemToAdd, { headers, observe: 'response' })
            .pipe(
                tap(response => console.log('Raw Response:', response)),
                catchError(error => {
                    console.error('Error in request:', error);
                    return throwError(() => new Error(error.message));
                })
            );
    }

    public get(url: string) {
    return this.httpClient.get<Image>(`${this.api}/images/url?url=` + url);
  }

  public getAll() {
    console.log(this.httpClient.get<Image>(`${this.api}/images`).subscribe());
    return this.httpClient.get<Image[]>(`${this.api}/images`);
  }

  public getAllTags() {
    return this.httpClient.get<Tag[]>(`${this.api}/tags`);
  }

  public getTags(url: string) {
    return this.httpClient.get<Tag[]>(`${this.api}/images/url/` + url + '/tags');
  }

  public getImagesByTag(tag: string) {
    return this.httpClient.get<Image[]>(`${this.api}/tags/` + tag);
  }

    getImagesByUser(userId: number, username: string, password: string): Observable<Image[]> {
        const headers = this.getAuthHeaders(username, password);
        return this.httpClient.get<Image[]>(`${this.api}/images/user/${userId}`, { headers });
    }

    analyzeImage(imageUrl: string): Observable<Tag[]> {
        return this.httpClient.post<Tag[]>(`${this.api}/analyze`, { url: imageUrl });
    }

    getImages(username: string, password: string) {
        const headers = new HttpHeaders({
            Authorization: this.createBasicAuthToken(username, password)
        });
        return this.httpClient.get<Image[]>(`${this.api}/images`, { headers });
    }

    private createBasicAuthToken(username: string, password: string): string {
        return 'Basic ' + btoa(username + ':' + password);
    }
}

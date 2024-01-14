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

    private createBasicAuthToken(username: string, password: string): string {
        return 'Basic ' + btoa(username + ':' + password);
    }

    private getAuthHeaders(username: string, password: string): HttpHeaders {
        return new HttpHeaders({
            Authorization: this.createBasicAuthToken(username, password)
        });
    }

    add(imageUrl: string, username: string, password: string): Observable<any> {
        const headers = new HttpHeaders({
            'Content-Type':  'application/json',
            'Authorization': this.createBasicAuthToken(username, password)
        });

        const requestBody = {
            imageUrl,
            username,
            password
        };
        return this.httpClient.post(`${this.api}/images`, requestBody, { headers });
    }

    public get(url: string, username: string, password: string) {
        const headers = new HttpHeaders({
            'Content-Type':  'application/json',
            'Authorization': this.createBasicAuthToken(username, password)
        });
        return this.httpClient.get<Image>(`${this.api}/images/url?url=` + url, { headers });
  }

  public getAll(username: string, password: string) {
      const headers = new HttpHeaders({
          'Content-Type':  'application/json',
          'Authorization': this.createBasicAuthToken(username, password)
      });
    console.log(this.httpClient.get<Image>(`${this.api}/images`).subscribe());
    return this.httpClient.get<Image[]>(`${this.api}/images`, { headers });
  }

  public getAllTags(username: string, password: string) {
      const headers = new HttpHeaders({
          'Content-Type':  'application/json',
          'Authorization': this.createBasicAuthToken(username, password)
      });
    return this.httpClient.get<Tag[]>(`${this.api}/tags`, { headers });
  }

  public getTags(imageId: number | undefined, username: string, password: string) {
      const headers = new HttpHeaders({
          'Content-Type':  'application/json',
          'Authorization': this.createBasicAuthToken(username, password)
      });
    return this.httpClient.get<Tag[]>(`${this.api}/images/id/` + imageId + '/tags', { headers });
  }

  public getImagesByTag(tag: string, username: string, password: string) {
      const headers = new HttpHeaders({
          'Content-Type':  'application/json',
          'Authorization': this.createBasicAuthToken(username, password)
      });
    return this.httpClient.get<Image[]>(`${this.api}/tags/` + tag, { headers });
  }

    getImagesByUser(username: string, password: string): Observable<Image[]> {
        const headers = this.getAuthHeaders(username, password);
        return this.httpClient.get<Image[]>(`${this.api}/images/user/${username}`, { headers });
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
}

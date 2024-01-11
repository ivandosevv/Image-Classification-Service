import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import { Observable } from "rxjs";
import { Image } from '../models/image-model.models'
import {Tag} from "../models/tag-model.models";

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private apiServerUrl = '';

  constructor(private httpClient: HttpClient) { }
  private readonly api = "http://localhost:7067";

  public add(itemToAdd: string): Observable<Image> {
    console.log("add");
    return this.httpClient.post<Image>(`${this.api}/images`, itemToAdd);
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

    public getImagesByUser(userId: number): Observable<Image[]> {
        return this.httpClient.get<Image[]>(`${this.api}/images/user/${userId}`);
    }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {Router} from "@angular/router";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:7067';

    constructor(private http: HttpClient, private router: Router) { }

    login(username: string, password: string): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}/login`, { username, password }).pipe(
            tap(response => {
                localStorage.setItem('user', JSON.stringify(response.user));
                localStorage.setItem('token', response.token); // Assuming the response includes a JWT token
            })
        );
    }

    register(username: string, password: string): Observable<any> {
        return this.http.post(`${this.apiUrl}/register`, { username, password });
    }

    getCurrentUser() {
        const userData = localStorage.getItem('user');
        return userData ? JSON.parse(userData) : null;
    }


    logout() {
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        this.router.navigate(['/login']); // Redirect to login page
    }

}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, Observable, tap, throwError} from 'rxjs';
import {Router} from "@angular/router";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:7067/users';

    constructor(private http: HttpClient, private router: Router) { }

    login(username: string, password: string): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}/login`, { username, password }).pipe(
            tap(response => {
                if (response.username) {
                    localStorage.setItem('user', JSON.stringify({ username: response.username, password: password }));
                    console.log('User data saved:', response.username);
                } else {
                    console.error('Login response missing username:', response);
                }
            }),
            catchError(error => {
                // Handle error
                console.error('Login error:', error);
                return throwError(error);
            })
        );
    }

    getCurrentUser() {
        const userData = localStorage.getItem('user');
        if (userData) {
            try {
                return JSON.parse(userData);
            } catch (e) {
                console.error('Error parsing user data:', e);
            }
        }
        return null;
    }


    register(username: string, password: string): Observable<any> {
        return this.http.post(`${this.apiUrl}/register`, { username, password });
    }

    logout() {
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        this.router.navigate(['/login']); // Redirect to login page
    }

}

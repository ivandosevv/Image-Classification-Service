import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {
    username!: string;
    password!: string;

    constructor(private authService: AuthService, private router: Router,
                private snackBar: MatSnackBar) { }

    onSubmit() {
        this.authService.login(this.username, this.password).subscribe(
            (response: any) => {
                console.log('Logged in successfully!', response);
                this.router.navigate(['/dashboard']); // Navigate to dashboard or home page after successful login
            },
            (error: any) => {
                this.snackBar.open('Login failed: ' + error.message, 'Close', {
                    duration: 3000,
                });
            }
        );
    }
}

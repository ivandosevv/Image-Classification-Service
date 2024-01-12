import {Component, ViewEncapsulation} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css'],
    encapsulation: ViewEncapsulation.None,
})
export class RegisterComponent {
    username!: string;
    password!: string;

    constructor(private authService: AuthService, private router: Router,
                private snackBar: MatSnackBar) { }

    onSubmit() {
        this.authService.register(this.username, this.password).subscribe(
            (response: any) => {
                console.log('Registration successful!', response);
                this.router.navigate(['/login']);
                this.snackBar.open('Registration successful', 'Close', {
                    duration: 3000,
                });
            },
            (error: any) => {
                this.snackBar.open('Registration failed: ' + error.message, 'Close', {
                    duration: 3000,
                });
            }
        );
    }
}

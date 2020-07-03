import { Injectable } from '@angular/core';
import * as auth0 from 'auth0-js';
import { environment } from './../../environments/environment';
import { Router } from '@angular/router';

(window as any).global = window;

@Injectable()
export class AuthService {
  // Create Auth0 web auth instance
  auth0 = new auth0.WebAuth({
    clientID: environment.auth.clientID,
    domain: environment.auth.domain,
    responseType: 'token',
    redirectUri: environment.auth.redirect,
    audience: environment.auth.audience,
    scope: environment.auth.scope
  });
  // Store authentication data
  expiresAt: number;
  userProfile: any;
  accessToken: string;
  authenticated: boolean;

  constructor(private router: Router) {
    this.checkSession();
  }

  getAccessToken() {
    console.log("getAccessToken");
    console.log(this.accessToken);
    return "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImtoZDZONnpubkxGN0E5MmN5NFk2RCJ9.eyJpc3MiOiJodHRwczovL2Rldi1jZDNjZWJiay5ldS5hdXRoMC5jb20vIiwic3ViIjoiYXV0aDB8NWVkZDAwYTMzNDQxMzcwMDE0OThlNDg5IiwiYXVkIjpbImh0dHA6Ly9sb2NhbGhvc3Q6NDIwMCIsImh0dHBzOi8vZGV2LWNkM2NlYmJrLmV1LmF1dGgwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE1OTM3OTg4MzIsImV4cCI6MTU5MzgwNjAzMiwiYXpwIjoiUFNLOW5hcjdEREpDaDkwcWlWUHJmMW84c2Q5WVBxMWQiLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIn0.vKx9BZm_FZ-5VZeSlftvSelI235wMIzWvQB1MGVgQdOrbr7NxYJE-5Qhfof9ak_WEDeyrEgwYe8ebu2pfmPhKz12Kr1fHoKZcRud7kk3vJlqNMI88xzD8uM1rAMnxLDv3Z6VN3uoBDtTI0EeUoh3t3Etuy9XTIRw0-LIMcNRoBeX5yDSmqb6_RzmCzdUb4Q7PWbEfs6_dDHFToUSRPJzKelv_2J6PRwdkbXh3PDsISh8wg_ezSuHtyuy3c8a8bA9xhgMbT6u2T8Oh6hRMEtfBBgP84KgF89jMcLjjkg_iTXDTJOCJCKm_85DQDu4mdebWNkRQEuAqt_YFadrqvaPYQ";
  }

  getUserName() {
    return this.userProfile.email;
  }

  login() {
    // Auth0 authorize request
    this.auth0.authorize();
  }

  handleLoginCallback() {
    // When Auth0 hash parsed, get profile
    this.auth0.parseHash((err, authResult) => {
      if (authResult && authResult.accessToken) {
        window.location.hash = '';
        this.getUserInfo(authResult);
        this.router.navigate(['dashboard']);
      } else if (err) {
        console.error(`Error: ${err.error}`);
      }
    });
  }

  checkSession() {

    this.auth0.checkSession({}, (err, authResult) => {
      if (authResult && authResult.accessToken) {
        this.getUserInfo(authResult);
      }
    });
  }

  getUserInfo(authResult) {
    // Use access token to retrieve user's profile and set session
    this.auth0.client.userInfo(authResult.accessToken, (err, profile) => {
      if (profile) {
        this._setSession(authResult, profile);
      }
    });
  }

  private _setSession(authResult, profile) {
    // Save authentication data and update login status subject
    console.log("setSession");
    console.log(authResult.accessToken);
    this.expiresAt = authResult.expiresIn * 1000 + Date.now();
    this.accessToken = authResult.accessToken;
    this.userProfile = profile;
    this.authenticated = true;
  }

  logout() {
    // Log out of Auth0 session
    // Ensure that returnTo URL is specified in Auth0
    // Application settings for Allowed Logout URLs
    this.auth0.logout({
      clientID: environment.auth.clientID
    });
  }

  get isLoggedIn(): boolean {
    // Check if current date is before token
    // expiration and user is signed in locally
    if(this.expiresAt ===  undefined || this.authenticated === undefined)
    {
      return false;
    }
    return Date.now() < this.expiresAt && this.authenticated;
  }

}

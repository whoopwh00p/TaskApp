import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';

import { AuthService } from './auth/auth.service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CallbackComponent } from './callback.component';
import { TaskDashboardComponent } from './task-dashboard/task-dashboard.component';
import { TaskfilterPipe } from './taskfilter.pipe';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { TaskdialogComponent } from './taskdialog/taskdialog.component';
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from '@angular/material/form-field';
import { EditTaskComponent } from './edit-task/edit-task.component';
import { GreetingComponent } from './greeting/greeting.component';
import { ActionBarComponent } from './action-bar/action-bar.component';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
@NgModule({
  declarations: [
    AppComponent,
    CallbackComponent,
    TaskDashboardComponent,
    TaskfilterPipe,
    TaskdialogComponent,
    EditTaskComponent,
    GreetingComponent,
    ActionBarComponent,
    ConfirmDialogComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    MaterialModule,
    BrowserAnimationsModule
  ],
  providers: [
    AuthService,
    { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'fill' } },
    CookieService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

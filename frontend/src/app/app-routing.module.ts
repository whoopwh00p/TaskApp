import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth/auth.guard';
import { CallbackComponent } from './callback.component';
import { LoginComponent } from './login/login.component';
import { TaskDashboardComponent } from './task-dashboard/task-dashboard.component';
import { ContentComponent } from './content/content.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: ContentComponent},
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'callback', component: CallbackComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AuthGuard]
})
export class AppRoutingModule { }

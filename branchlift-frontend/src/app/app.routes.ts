import { Routes } from '@angular/router';
import { EnvironmentListComponent } from './components/environment-list/environment-list.component';
import { ProjectListComponent } from './components/project-list/project-list.component';

export const routes: Routes = [
  { path: 'environments', component: EnvironmentListComponent },
  { path: 'projects', component: ProjectListComponent },
  { path: '', redirectTo: '/environments', pathMatch: 'full' }
];

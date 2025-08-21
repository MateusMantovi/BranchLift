import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription, timer } from 'rxjs';
import { switchMap } from 'rxjs/operators';

// Imports do Angular Material
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';

// Imports dos seus Models e Services
import { Project } from '../../models/project';
import { ProjectService } from '../../services/project.service';
import { Environment } from '../../models/environment.model';
import { EnvironmentService } from '../../services/environment.service';

@Component({
  selector: 'app-environment-list',
  standalone: true,
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule, MatButtonModule, MatCardModule,
    MatChipsModule, MatFormFieldModule, MatIconModule, MatInputModule,
    MatProgressSpinnerModule, MatSelectModule, MatTableModule
  ],
  templateUrl: './environment-list.component.html',
  styleUrls: ['./environment-list.component.css'],
})
export class EnvironmentListComponent implements OnInit, OnDestroy {
  displayedColumns: string[] = ['status', 'projectName', 'gitBranch', 'accessUrl', 'createdBy', 'createdAt', 'actions'];
  environments: Environment[] = [];
  projects: Project[] = [];
  isLoading = true;
  provisionForm!: FormGroup;
  private subscription = new Subscription();

  constructor(
    private readonly environmentService: EnvironmentService,
    private readonly projectService: ProjectService,
    private readonly fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.provisionForm = this.fb.group({
      projectId: ['', Validators.required],
      gitBranch: ['', Validators.required],
      createdBy: ['frontend-user', Validators.required],
    });

    this.loadProjects();

    const polling$ = timer(0, 5000).pipe(
      switchMap(() => this.environmentService.getAllEnvironments())
    );

    this.subscription.add(
      polling$.subscribe({
        next: (data) => { this.environments = data; this.isLoading = false; },
        error: (err: any) => { console.error('Erro ao buscar ambientes', err); this.isLoading = false; },
      })
    );
  }

  loadProjects(): void {
    this.projectService.getAllProjects().subscribe(data => {
      this.projects = data;
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  onProvisionSubmit(): void {
    if (this.provisionForm.invalid) return;
    const { projectId, gitBranch, createdBy } = this.provisionForm.value;
    this.isLoading = true;

    this.subscription.add(
      this.environmentService
        .requestProvisioning(projectId!, gitBranch!, createdBy!)
        .subscribe({
          next: () => {
            this.provisionForm.reset({ createdBy: 'frontend-user', projectId: '', gitBranch: '' });
          },
          error: (err: any) => { console.error('Erro ao provisionar ambiente', err); this.isLoading = false; },
        })
    );
  }

  deleteEnvironment(id: number): void {
    this.isLoading = true; // Mostra o spinner enquanto processa
    this.subscription.add(
      this.environmentService.deleteEnvironment(id).subscribe({
        next: () => {
          // SUCESSO: Remove o item da lista localmente para a UI atualizar na hora.
          this.environments = this.environments.filter(env => env.id !== id);
          this.isLoading = false; // Esconde o spinner
        },
        error: (err: any) => {
          console.error('Erro ao excluir ambiente', err);
          // Em um app real, mostraríamos uma notificação de erro aqui.
          this.isLoading = false; // Esconde o spinner
        },
      })
    );
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'RUNNING': return 'primary';
      case 'PROVISIONING': return 'accent';
      case 'ERROR': return 'warn';
      default: return '';
    }
  }
}

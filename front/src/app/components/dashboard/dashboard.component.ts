import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ProjectService } from '../../services/project.service';
import { Project } from '../../models/project.model';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {

  projects: Project[] = [];
  isLoading = true;
  showCreateModal = false;
  isCreating = false;

  newProject = {
    name: '',
    description: ''
  };

  errorMessage = '';
  successMessage = '';

  constructor(
    public authService: AuthService,
    private projectService: ProjectService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login'], { replaceUrl: true });
      return;
    }
    this.loadProjects();
  }

  loadProjects(): void {
    this.isLoading = true;
    this.projectService.getProjects().subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.success) {
          this.projects = response.data;
        }
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Error al cargar proyectos:', error);
        if (error.status === 401) {
          this.authService.logout();
          this.router.navigate(['/login'], { replaceUrl: true });
        }
      }
    });
  }

  openCreateModal(): void {
    this.newProject = { name: '', description: '' };
    this.errorMessage = '';
    this.showCreateModal = true;
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
  }

  createProject(): void {
    if (!this.newProject.name.trim()) {
      this.errorMessage = 'El nombre del proyecto es requerido';
      return;
    }

    this.isCreating = true;
    this.errorMessage = '';

    this.projectService.createProject(this.newProject).subscribe({
      next: (response) => {
        this.isCreating = false;
        if (response.success) {
          this.showCreateModal = false;
          this.successMessage = 'Proyecto creado exitosamente';
          this.loadProjects();
          setTimeout(() => this.successMessage = '', 3000);
        }
      },
      error: (error) => {
        this.isCreating = false;
        this.errorMessage = error.error?.message || 'Error al crear el proyecto';
      }
    });
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}

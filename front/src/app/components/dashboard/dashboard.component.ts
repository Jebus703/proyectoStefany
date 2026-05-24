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

  // Variables para editar
  showEditModal = false;
  isEditing = false;
  editProject = {
    id: 0,
    name: '',
    description: ''
  };

  // Variables para eliminar
  showDeleteModal = false;
  isDeleting = false;
  projectToDelete: Project | null = null;

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

  // === Funciones para Editar ===
  openEditModal(project: Project): void {
    this.editProject = {
      id: project.id,
      name: project.name,
      description: project.description || ''
    };
    this.errorMessage = '';
    this.showEditModal = true;
  }

  closeEditModal(): void {
    this.showEditModal = false;
  }

  updateProject(): void {
    if (!this.editProject.name.trim()) {
      this.errorMessage = 'El nombre del proyecto es requerido';
      return;
    }

    this.isEditing = true;
    this.errorMessage = '';

    this.projectService.updateProject(this.editProject.id, {
      name: this.editProject.name,
      description: this.editProject.description
    }).subscribe({
      next: (response) => {
        this.isEditing = false;
        if (response.success) {
          this.showEditModal = false;
          this.successMessage = 'Proyecto actualizado exitosamente';
          this.loadProjects();
          setTimeout(() => this.successMessage = '', 3000);
        }
      },
      error: (error) => {
        this.isEditing = false;
        this.errorMessage = error.error?.message || 'Error al actualizar el proyecto';
      }
    });
  }

  // === Funciones para Eliminar ===
  openDeleteModal(project: Project): void {
    this.projectToDelete = project;
    this.showDeleteModal = true;
  }

  closeDeleteModal(): void {
    this.showDeleteModal = false;
    this.projectToDelete = null;
  }

  deleteProject(): void {
    if (!this.projectToDelete) return;

    this.isDeleting = true;

    this.projectService.deleteProject(this.projectToDelete.id).subscribe({
      next: (response) => {
        this.isDeleting = false;
        if (response.success) {
          this.showDeleteModal = false;
          this.projectToDelete = null;
          this.successMessage = 'Proyecto eliminado exitosamente';
          this.loadProjects();
          setTimeout(() => this.successMessage = '', 3000);
        }
      },
      error: (error) => {
        this.isDeleting = false;
        this.errorMessage = error.error?.message || 'Error al eliminar el proyecto';
      }
    });
  }
}

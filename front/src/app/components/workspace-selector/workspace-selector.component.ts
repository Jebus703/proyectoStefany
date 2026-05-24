import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Workspace } from '../../models/workspace.model';

@Component({
  selector: 'app-workspace-selector',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './workspace-selector.component.html',
  styleUrl: './workspace-selector.component.css'
})
export class WorkspaceSelectorComponent implements OnInit {

  workspaces: Workspace[] = [];
  userName = '';
  isLoading = false;
  selectedWorkspaceId: number | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.workspaces = this.authService.getWorkspaces();
    this.userName = this.authService.getCurrentUser()?.fullName || '';

    if (this.workspaces.length === 0) {
      this.router.navigate(['/login']);
    }
  }

  selectWorkspace(workspace: Workspace): void {
    this.isLoading = true;
    this.selectedWorkspaceId = workspace.id;

    this.authService.generateToken(workspace.id).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.success) {
          this.router.navigate(['/dashboard'], { replaceUrl: true });
        }
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Error al generar token:', error);
      }
    });
  }

  getRoleClass(role: string | undefined): string {
    if (!role) return '';
    return 'role-' + role.toLowerCase();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login'], { replaceUrl: true });
  }
}

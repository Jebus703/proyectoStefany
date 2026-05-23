import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User } from '../models/user.model';
import { Workspace } from '../models/workspace.model';
import { Role } from '../models/role.model';
import { ApiResponse } from '../models/api-response.model';

interface LoginRequest {
  username: string;
  password: string;
}

interface LoginResponse {
  user: User;
  workspaces: Workspace[];
}

interface TokenRequest {
  userId: number;
  workspaceId: number;
}

interface TokenResponse {
  token: string;
  workspace: Workspace;
  role: Role;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';

  private currentUserSubject = new BehaviorSubject<User | null>(null);
  private currentWorkspaceSubject = new BehaviorSubject<Workspace | null>(null);
  private currentRoleSubject = new BehaviorSubject<Role | null>(null);
  private workspacesSubject = new BehaviorSubject<Workspace[]>([]);

  currentUser$ = this.currentUserSubject.asObservable();
  currentWorkspace$ = this.currentWorkspaceSubject.asObservable();
  currentRole$ = this.currentRoleSubject.asObservable();
  workspaces$ = this.workspacesSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadFromStorage();
  }

  private loadFromStorage(): void {
    const user = localStorage.getItem('user');
    const workspace = localStorage.getItem('workspace');
    const role = localStorage.getItem('role');
    const workspaces = localStorage.getItem('workspaces');

    if (user) this.currentUserSubject.next(JSON.parse(user));
    if (workspace) this.currentWorkspaceSubject.next(JSON.parse(workspace));
    if (role) this.currentRoleSubject.next(JSON.parse(role));
    if (workspaces) this.workspacesSubject.next(JSON.parse(workspaces));
  }

  login(username: string, password: string): Observable<ApiResponse<LoginResponse>> {
    return this.http.post<ApiResponse<LoginResponse>>(`${this.apiUrl}/login`, { username, password })
      .pipe(
        tap(response => {
          if (response.success) {
            this.currentUserSubject.next(response.data.user);
            this.workspacesSubject.next(response.data.workspaces);
            localStorage.setItem('user', JSON.stringify(response.data.user));
            localStorage.setItem('workspaces', JSON.stringify(response.data.workspaces));
          }
        })
      );
  }

  generateToken(workspaceId: number): Observable<ApiResponse<TokenResponse>> {
    const userId = this.currentUserSubject.value?.id;
    return this.http.post<ApiResponse<TokenResponse>>(`${this.apiUrl}/token`, { userId, workspaceId })
      .pipe(
        tap(response => {
          if (response.success) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('workspace', JSON.stringify(response.data.workspace));
            localStorage.setItem('role', JSON.stringify(response.data.role));
            this.currentWorkspaceSubject.next(response.data.workspace);
            this.currentRoleSubject.next(response.data.role);
          }
        })
      );
  }

  logout(): void {
    localStorage.clear();
    this.currentUserSubject.next(null);
    this.currentWorkspaceSubject.next(null);
    this.currentRoleSubject.next(null);
    this.workspacesSubject.next([]);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  getCurrentRole(): Role | null {
    return this.currentRoleSubject.value;
  }

  getCurrentWorkspace(): Workspace | null {
    return this.currentWorkspaceSubject.value;
  }

  getWorkspaces(): Workspace[] {
    return this.workspacesSubject.value;
  }

  canCreate(): boolean {
    return this.currentRoleSubject.value?.canCreate ?? false;
  }

  canEdit(): boolean {
    return this.currentRoleSubject.value?.canEdit ?? false;
  }

  canDelete(): boolean {
    return this.currentRoleSubject.value?.canDelete ?? false;
  }
}

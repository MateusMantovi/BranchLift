import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Environment } from '../models/environment.model';

@Injectable({
  providedIn: 'root'
})
export class EnvironmentService {
  private apiUrl = '/api/environments';

  constructor(private http: HttpClient) { }

  getAllEnvironments(): Observable<Environment[]> {
    return this.http.get<Environment[]>(this.apiUrl);
  }

  requestProvisioning(projectId: number, gitBranch: string, createdBy: string): Observable<Environment> {
    const params = new HttpParams()
      .set('projectId', projectId.toString())
      .set('gitBranch', gitBranch)
      .set('createdBy', createdBy);

    return this.http.post<Environment>(`${this.apiUrl}/request-provisioning`, null, { params });
  }

  // --- MÉTODO QUE ESTAVA FALTANDO ---
  deleteEnvironment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  // ------------------------------------
}

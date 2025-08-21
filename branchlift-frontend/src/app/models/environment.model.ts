import { Project } from "./project";

export interface Environment {
  id: number;
  project: Project;
  gitBranch: string;
  status: 'PROVISIONING' | 'RUNNING' | 'ERROR' | 'DESTROYED';
  accessUrl?: string;
  allocatedPort?: number;
  createdBy: string;
  createdAt: string;
  buildLog?: string;
}

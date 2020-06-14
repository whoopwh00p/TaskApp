import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ProjectService } from '../project.service';
import { Project } from '../model/Project';

@Component({
  selector: 'app-project-dashboard',
  templateUrl: './project-dashboard.component.html',
  styleUrls: ['./project-dashboard.component.css']
})
export class ProjectDashboardComponent implements OnInit {

  public projects: Project[];
  public selectedProject;

  constructor(private projectService: ProjectService) { }

  ngOnInit(): void {
    this.projectService.getProjects().subscribe(result => {
      this.projects = result
      this.selectedProject = this.projects[0];
    });
  }

  submit(): void {
    this.projectService.setSelectedProject(this.selectedProject);
  }
}

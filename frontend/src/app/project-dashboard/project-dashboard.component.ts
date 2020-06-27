import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ProjectService } from '../project.service';
import { Project } from '../model/Project';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-project-dashboard',
  templateUrl: './project-dashboard.component.html',
  styleUrls: ['./project-dashboard.component.css']
})
export class ProjectDashboardComponent implements OnInit {

  public projects: Project[];
  public selectedProject : Project;

  constructor(private projectService: ProjectService, private cookieService : CookieService) { }

  ngOnInit(): void {
    this.projectService.getProjects().subscribe(result => {
      this.projects = result;
      this.setDefaultSelectedProject();
    });
  }

  selectProject(): void {
    this.projectService.setSelectedProject(this.selectedProject);
  }

  setDefaultSelectedProject() : void {
    let selectedProjectId : Number = parseInt(this.cookieService.get('selected-project'));
    this.selectedProject = this.findProjectById(selectedProjectId);
    this.selectProject();
  }

  private findProjectById(id:Number) : Project {
    for(let project of this.projects) {
      if(project.id == id) {
        return project;
      }
    }
    return this.projects[0];
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment as env } from 'src/environments/environment';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.scss']
})
export class GridComponent implements OnInit {
  grid: string[][];
  steps: number;
  timer: NodeJS.Timeout;

  constructor (
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.getGrid();
    this.getSteps();
  }

  play(): void {
    this.timer = setInterval(() => {
      this.getGrid();
      this.getSteps();
    }, 100);
  }

  stop(): void {
    clearInterval(this.timer);
  }

  step(): void {
    this.getGrid();
    this.getSteps();
  }

  getGrid(): void {
    this.http.get<string[][]>(`${env.baseUrl}/api/grid`) 
          .subscribe(r => {
              this.grid = r;
          });
  }

  getSteps(): void {
    this.http.get<number>(`${env.baseUrl}/api/grid/steps`) 
          .subscribe(r => {
              this.steps = r;
          });
  }
}

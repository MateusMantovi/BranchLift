import { Component } from '@angular/core';
// IMPORTANTE: Adiciona RouterLink, RouterLinkActive e RouterOutlet
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  // Garante que os imports necessários para o roteamento estão aqui
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'branchlift-frontend';
}

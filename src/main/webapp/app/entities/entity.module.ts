import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'video',
        loadChildren: () => import('./video/video.module').then(m => m.WhatsappstatusVideoModule),
      },
      {
        path: 'profile',
        loadChildren: () => import('./profile/profile.module').then(m => m.WhatsappstatusProfileModule),
      },
      {
        path: 'comment',
        loadChildren: () => import('./comment/comment.module').then(m => m.WhatsappstatusCommentModule),
      },
      {
        path: 'video-category',
        loadChildren: () => import('./video-category/video-category.module').then(m => m.WhatsappstatusVideoCategoryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class WhatsappstatusEntityModule {}

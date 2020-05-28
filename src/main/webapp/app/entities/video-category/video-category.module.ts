import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WhatsappstatusSharedModule } from 'app/shared/shared.module';
import { VideoCategoryComponent } from './video-category.component';
import { VideoCategoryDetailComponent } from './video-category-detail.component';
import { VideoCategoryUpdateComponent } from './video-category-update.component';
import { VideoCategoryDeleteDialogComponent } from './video-category-delete-dialog.component';
import { videoCategoryRoute } from './video-category.route';

@NgModule({
  imports: [WhatsappstatusSharedModule, RouterModule.forChild(videoCategoryRoute)],
  declarations: [VideoCategoryComponent, VideoCategoryDetailComponent, VideoCategoryUpdateComponent, VideoCategoryDeleteDialogComponent],
  entryComponents: [VideoCategoryDeleteDialogComponent],
})
export class WhatsappstatusVideoCategoryModule {}

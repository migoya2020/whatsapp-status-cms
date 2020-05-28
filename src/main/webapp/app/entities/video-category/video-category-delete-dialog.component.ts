import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVideoCategory } from 'app/shared/model/video-category.model';
import { VideoCategoryService } from './video-category.service';

@Component({
  templateUrl: './video-category-delete-dialog.component.html',
})
export class VideoCategoryDeleteDialogComponent {
  videoCategory?: IVideoCategory;

  constructor(
    protected videoCategoryService: VideoCategoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.videoCategoryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('videoCategoryListModification');
      this.activeModal.close();
    });
  }
}

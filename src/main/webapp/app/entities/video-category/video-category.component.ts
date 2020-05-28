import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVideoCategory } from 'app/shared/model/video-category.model';
import { VideoCategoryService } from './video-category.service';
import { VideoCategoryDeleteDialogComponent } from './video-category-delete-dialog.component';

@Component({
  selector: 'jhi-video-category',
  templateUrl: './video-category.component.html',
})
export class VideoCategoryComponent implements OnInit, OnDestroy {
  videoCategories?: IVideoCategory[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected videoCategoryService: VideoCategoryService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.videoCategoryService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IVideoCategory[]>) => (this.videoCategories = res.body || []));
      return;
    }

    this.videoCategoryService.query().subscribe((res: HttpResponse<IVideoCategory[]>) => (this.videoCategories = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInVideoCategories();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVideoCategory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVideoCategories(): void {
    this.eventSubscriber = this.eventManager.subscribe('videoCategoryListModification', () => this.loadAll());
  }

  delete(videoCategory: IVideoCategory): void {
    const modalRef = this.modalService.open(VideoCategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.videoCategory = videoCategory;
  }
}

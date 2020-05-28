import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVideoCategory } from 'app/shared/model/video-category.model';

@Component({
  selector: 'jhi-video-category-detail',
  templateUrl: './video-category-detail.component.html',
})
export class VideoCategoryDetailComponent implements OnInit {
  videoCategory: IVideoCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ videoCategory }) => (this.videoCategory = videoCategory));
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IVideoCategory, VideoCategory } from 'app/shared/model/video-category.model';
import { VideoCategoryService } from './video-category.service';

@Component({
  selector: 'jhi-video-category-update',
  templateUrl: './video-category-update.component.html',
})
export class VideoCategoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    category: [null, [Validators.required, Validators.maxLength(12)]],
    description: [null, [Validators.maxLength(25)]],
  });

  constructor(protected videoCategoryService: VideoCategoryService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ videoCategory }) => {
      this.updateForm(videoCategory);
    });
  }

  updateForm(videoCategory: IVideoCategory): void {
    this.editForm.patchValue({
      id: videoCategory.id,
      category: videoCategory.category,
      description: videoCategory.description,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const videoCategory = this.createFromForm();
    if (videoCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.videoCategoryService.update(videoCategory));
    } else {
      this.subscribeToSaveResponse(this.videoCategoryService.create(videoCategory));
    }
  }

  private createFromForm(): IVideoCategory {
    return {
      ...new VideoCategory(),
      id: this.editForm.get(['id'])!.value,
      category: this.editForm.get(['category'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVideoCategory>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}

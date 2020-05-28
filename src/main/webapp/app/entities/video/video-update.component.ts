import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IVideo, Video } from 'app/shared/model/video.model';
import { VideoService } from './video.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-video-update',
  templateUrl: './video-update.component.html',
})
export class VideoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
    videoUrl: [null, [Validators.required]],
    videoUrlContentType: [],
    description: [null, [Validators.minLength(5), Validators.maxLength(100)]],
    date: [null, [Validators.required]],
    tags: [],
    views: [null, [Validators.min(0)]],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected videoService: VideoService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ video }) => {
      if (!video.id) {
        const today = moment().startOf('day');
        video.date = today;
      }

      this.updateForm(video);
    });
  }

  updateForm(video: IVideo): void {
    this.editForm.patchValue({
      id: video.id,
      title: video.title,
      videoUrl: video.videoUrl,
      videoUrlContentType: video.videoUrlContentType,
      description: video.description,
      date: video.date ? video.date.format(DATE_TIME_FORMAT) : null,
      tags: video.tags,
      views: video.views,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('whatsappstatusApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const video = this.createFromForm();
    if (video.id !== undefined) {
      this.subscribeToSaveResponse(this.videoService.update(video));
    } else {
      this.subscribeToSaveResponse(this.videoService.create(video));
    }
  }

  private createFromForm(): IVideo {
    return {
      ...new Video(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      videoUrlContentType: this.editForm.get(['videoUrlContentType'])!.value,
      videoUrl: this.editForm.get(['videoUrl'])!.value,
      description: this.editForm.get(['description'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      tags: this.editForm.get(['tags'])!.value,
      views: this.editForm.get(['views'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVideo>>): void {
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

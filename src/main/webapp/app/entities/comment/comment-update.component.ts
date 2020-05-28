import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IComment, Comment } from 'app/shared/model/comment.model';
import { CommentService } from './comment.service';

@Component({
  selector: 'jhi-comment-update',
  templateUrl: './comment-update.component.html',
})
export class CommentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    comment: [null, [Validators.required, Validators.maxLength(45)]],
    commentDate: [null, [Validators.required]],
  });

  constructor(protected commentService: CommentService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comment }) => {
      if (!comment.id) {
        const today = moment().startOf('day');
        comment.commentDate = today;
      }

      this.updateForm(comment);
    });
  }

  updateForm(comment: IComment): void {
    this.editForm.patchValue({
      id: comment.id,
      comment: comment.comment,
      commentDate: comment.commentDate ? comment.commentDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comment = this.createFromForm();
    if (comment.id !== undefined) {
      this.subscribeToSaveResponse(this.commentService.update(comment));
    } else {
      this.subscribeToSaveResponse(this.commentService.create(comment));
    }
  }

  private createFromForm(): IComment {
    return {
      ...new Comment(),
      id: this.editForm.get(['id'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      commentDate: this.editForm.get(['commentDate'])!.value
        ? moment(this.editForm.get(['commentDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComment>>): void {
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

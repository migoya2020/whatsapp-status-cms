import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVideoCategory, VideoCategory } from 'app/shared/model/video-category.model';
import { VideoCategoryService } from './video-category.service';
import { VideoCategoryComponent } from './video-category.component';
import { VideoCategoryDetailComponent } from './video-category-detail.component';
import { VideoCategoryUpdateComponent } from './video-category-update.component';

@Injectable({ providedIn: 'root' })
export class VideoCategoryResolve implements Resolve<IVideoCategory> {
  constructor(private service: VideoCategoryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVideoCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((videoCategory: HttpResponse<VideoCategory>) => {
          if (videoCategory.body) {
            return of(videoCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new VideoCategory());
  }
}

export const videoCategoryRoute: Routes = [
  {
    path: '',
    component: VideoCategoryComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'whatsappstatusApp.videoCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VideoCategoryDetailComponent,
    resolve: {
      videoCategory: VideoCategoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'whatsappstatusApp.videoCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VideoCategoryUpdateComponent,
    resolve: {
      videoCategory: VideoCategoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'whatsappstatusApp.videoCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VideoCategoryUpdateComponent,
    resolve: {
      videoCategory: VideoCategoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'whatsappstatusApp.videoCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

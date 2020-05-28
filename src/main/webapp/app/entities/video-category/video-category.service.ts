import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IVideoCategory } from 'app/shared/model/video-category.model';

type EntityResponseType = HttpResponse<IVideoCategory>;
type EntityArrayResponseType = HttpResponse<IVideoCategory[]>;

@Injectable({ providedIn: 'root' })
export class VideoCategoryService {
  public resourceUrl = SERVER_API_URL + 'api/video-categories';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/video-categories';

  constructor(protected http: HttpClient) {}

  create(videoCategory: IVideoCategory): Observable<EntityResponseType> {
    return this.http.post<IVideoCategory>(this.resourceUrl, videoCategory, { observe: 'response' });
  }

  update(videoCategory: IVideoCategory): Observable<EntityResponseType> {
    return this.http.put<IVideoCategory>(this.resourceUrl, videoCategory, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVideoCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVideoCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVideoCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

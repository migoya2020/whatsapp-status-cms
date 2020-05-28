import { Moment } from 'moment';
import { Tags } from 'app/shared/model/enumerations/tags.model';

export interface IVideo {
  id?: number;
  title?: string;
  videoUrlContentType?: string;
  videoUrl?: any;
  description?: string;
  date?: Moment;
  tags?: Tags;
  views?: number;
}

export class Video implements IVideo {
  constructor(
    public id?: number,
    public title?: string,
    public videoUrlContentType?: string,
    public videoUrl?: any,
    public description?: string,
    public date?: Moment,
    public tags?: Tags,
    public views?: number
  ) {}
}

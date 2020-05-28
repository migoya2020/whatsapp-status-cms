import { Moment } from 'moment';

export interface IComment {
  id?: number;
  comment?: string;
  commentDate?: Moment;
}

export class Comment implements IComment {
  constructor(public id?: number, public comment?: string, public commentDate?: Moment) {}
}

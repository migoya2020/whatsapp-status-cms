export interface IVideoCategory {
  id?: number;
  category?: string;
  description?: string;
}

export class VideoCategory implements IVideoCategory {
  constructor(public id?: number, public category?: string, public description?: string) {}
}

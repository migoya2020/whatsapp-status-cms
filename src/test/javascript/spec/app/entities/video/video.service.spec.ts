import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { VideoService } from 'app/entities/video/video.service';
import { IVideo, Video } from 'app/shared/model/video.model';
import { Tags } from 'app/shared/model/enumerations/tags.model';

describe('Service Tests', () => {
  describe('Video Service', () => {
    let injector: TestBed;
    let service: VideoService;
    let httpMock: HttpTestingController;
    let elemDefault: IVideo;
    let expectedResult: IVideo | IVideo[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(VideoService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Video(0, 'AAAAAAA', 'image/png', 'AAAAAAA', 'AAAAAAA', currentDate, Tags.COMIC, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Video', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new Video()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Video', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            videoUrl: 'BBBBBB',
            description: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
            tags: 'BBBBBB',
            views: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Video', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            videoUrl: 'BBBBBB',
            description: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
            tags: 'BBBBBB',
            views: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Video', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

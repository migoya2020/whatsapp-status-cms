import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { WhatsappstatusTestModule } from '../../../test.module';
import { VideoCategoryComponent } from 'app/entities/video-category/video-category.component';
import { VideoCategoryService } from 'app/entities/video-category/video-category.service';
import { VideoCategory } from 'app/shared/model/video-category.model';

describe('Component Tests', () => {
  describe('VideoCategory Management Component', () => {
    let comp: VideoCategoryComponent;
    let fixture: ComponentFixture<VideoCategoryComponent>;
    let service: VideoCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WhatsappstatusTestModule],
        declarations: [VideoCategoryComponent],
      })
        .overrideTemplate(VideoCategoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VideoCategoryComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VideoCategoryService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new VideoCategory(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.videoCategories && comp.videoCategories[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});

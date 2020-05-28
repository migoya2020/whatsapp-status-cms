import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WhatsappstatusTestModule } from '../../../test.module';
import { VideoCategoryDetailComponent } from 'app/entities/video-category/video-category-detail.component';
import { VideoCategory } from 'app/shared/model/video-category.model';

describe('Component Tests', () => {
  describe('VideoCategory Management Detail Component', () => {
    let comp: VideoCategoryDetailComponent;
    let fixture: ComponentFixture<VideoCategoryDetailComponent>;
    const route = ({ data: of({ videoCategory: new VideoCategory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WhatsappstatusTestModule],
        declarations: [VideoCategoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(VideoCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VideoCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load videoCategory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.videoCategory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WhatsappstatusTestModule } from '../../../test.module';
import { VideoCategoryUpdateComponent } from 'app/entities/video-category/video-category-update.component';
import { VideoCategoryService } from 'app/entities/video-category/video-category.service';
import { VideoCategory } from 'app/shared/model/video-category.model';

describe('Component Tests', () => {
  describe('VideoCategory Management Update Component', () => {
    let comp: VideoCategoryUpdateComponent;
    let fixture: ComponentFixture<VideoCategoryUpdateComponent>;
    let service: VideoCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WhatsappstatusTestModule],
        declarations: [VideoCategoryUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(VideoCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VideoCategoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VideoCategoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new VideoCategory(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new VideoCategory();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});

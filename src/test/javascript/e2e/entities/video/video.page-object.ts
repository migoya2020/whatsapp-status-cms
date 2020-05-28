import { element, by, ElementFinder } from 'protractor';

export class VideoComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-video div table .btn-danger'));
  title = element.all(by.css('jhi-video div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class VideoUpdatePage {
  pageTitle = element(by.id('jhi-video-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  titleInput = element(by.id('field_title'));
  videoUrlInput = element(by.id('file_videoUrl'));
  descriptionInput = element(by.id('field_description'));
  dateInput = element(by.id('field_date'));
  tagsSelect = element(by.id('field_tags'));
  viewsInput = element(by.id('field_views'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setTitleInput(title: string): Promise<void> {
    await this.titleInput.sendKeys(title);
  }

  async getTitleInput(): Promise<string> {
    return await this.titleInput.getAttribute('value');
  }

  async setVideoUrlInput(videoUrl: string): Promise<void> {
    await this.videoUrlInput.sendKeys(videoUrl);
  }

  async getVideoUrlInput(): Promise<string> {
    return await this.videoUrlInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setDateInput(date: string): Promise<void> {
    await this.dateInput.sendKeys(date);
  }

  async getDateInput(): Promise<string> {
    return await this.dateInput.getAttribute('value');
  }

  async setTagsSelect(tags: string): Promise<void> {
    await this.tagsSelect.sendKeys(tags);
  }

  async getTagsSelect(): Promise<string> {
    return await this.tagsSelect.element(by.css('option:checked')).getText();
  }

  async tagsSelectLastOption(): Promise<void> {
    await this.tagsSelect.all(by.tagName('option')).last().click();
  }

  async setViewsInput(views: string): Promise<void> {
    await this.viewsInput.sendKeys(views);
  }

  async getViewsInput(): Promise<string> {
    return await this.viewsInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class VideoDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-video-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-video'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}

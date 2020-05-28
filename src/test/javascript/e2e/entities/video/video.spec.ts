import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { VideoComponentsPage, VideoDeleteDialog, VideoUpdatePage } from './video.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Video e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let videoComponentsPage: VideoComponentsPage;
  let videoUpdatePage: VideoUpdatePage;
  let videoDeleteDialog: VideoDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Videos', async () => {
    await navBarPage.goToEntity('video');
    videoComponentsPage = new VideoComponentsPage();
    await browser.wait(ec.visibilityOf(videoComponentsPage.title), 5000);
    expect(await videoComponentsPage.getTitle()).to.eq('whatsappstatusApp.video.home.title');
    await browser.wait(ec.or(ec.visibilityOf(videoComponentsPage.entities), ec.visibilityOf(videoComponentsPage.noResult)), 1000);
  });

  it('should load create Video page', async () => {
    await videoComponentsPage.clickOnCreateButton();
    videoUpdatePage = new VideoUpdatePage();
    expect(await videoUpdatePage.getPageTitle()).to.eq('whatsappstatusApp.video.home.createOrEditLabel');
    await videoUpdatePage.cancel();
  });

  it('should create and save Videos', async () => {
    const nbButtonsBeforeCreate = await videoComponentsPage.countDeleteButtons();

    await videoComponentsPage.clickOnCreateButton();

    await promise.all([
      videoUpdatePage.setTitleInput('title'),
      videoUpdatePage.setVideoUrlInput(absolutePath),
      videoUpdatePage.setDescriptionInput('description'),
      videoUpdatePage.setDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      videoUpdatePage.tagsSelectLastOption(),
      videoUpdatePage.setViewsInput('5'),
    ]);

    expect(await videoUpdatePage.getTitleInput()).to.eq('title', 'Expected Title value to be equals to title');
    expect(await videoUpdatePage.getVideoUrlInput()).to.endsWith(
      fileNameToUpload,
      'Expected VideoUrl value to be end with ' + fileNameToUpload
    );
    expect(await videoUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
    expect(await videoUpdatePage.getDateInput()).to.contain('2001-01-01T02:30', 'Expected date value to be equals to 2000-12-31');
    expect(await videoUpdatePage.getViewsInput()).to.eq('5', 'Expected views value to be equals to 5');

    await videoUpdatePage.save();
    expect(await videoUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await videoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Video', async () => {
    const nbButtonsBeforeDelete = await videoComponentsPage.countDeleteButtons();
    await videoComponentsPage.clickOnLastDeleteButton();

    videoDeleteDialog = new VideoDeleteDialog();
    expect(await videoDeleteDialog.getDialogTitle()).to.eq('whatsappstatusApp.video.delete.question');
    await videoDeleteDialog.clickOnConfirmButton();

    expect(await videoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

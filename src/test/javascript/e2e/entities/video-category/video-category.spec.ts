import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { VideoCategoryComponentsPage, VideoCategoryDeleteDialog, VideoCategoryUpdatePage } from './video-category.page-object';

const expect = chai.expect;

describe('VideoCategory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let videoCategoryComponentsPage: VideoCategoryComponentsPage;
  let videoCategoryUpdatePage: VideoCategoryUpdatePage;
  let videoCategoryDeleteDialog: VideoCategoryDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load VideoCategories', async () => {
    await navBarPage.goToEntity('video-category');
    videoCategoryComponentsPage = new VideoCategoryComponentsPage();
    await browser.wait(ec.visibilityOf(videoCategoryComponentsPage.title), 5000);
    expect(await videoCategoryComponentsPage.getTitle()).to.eq('whatsappstatusApp.videoCategory.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(videoCategoryComponentsPage.entities), ec.visibilityOf(videoCategoryComponentsPage.noResult)),
      1000
    );
  });

  it('should load create VideoCategory page', async () => {
    await videoCategoryComponentsPage.clickOnCreateButton();
    videoCategoryUpdatePage = new VideoCategoryUpdatePage();
    expect(await videoCategoryUpdatePage.getPageTitle()).to.eq('whatsappstatusApp.videoCategory.home.createOrEditLabel');
    await videoCategoryUpdatePage.cancel();
  });

  it('should create and save VideoCategories', async () => {
    const nbButtonsBeforeCreate = await videoCategoryComponentsPage.countDeleteButtons();

    await videoCategoryComponentsPage.clickOnCreateButton();

    await promise.all([videoCategoryUpdatePage.setCategoryInput('category'), videoCategoryUpdatePage.setDescriptionInput('description')]);

    expect(await videoCategoryUpdatePage.getCategoryInput()).to.eq('category', 'Expected Category value to be equals to category');
    expect(await videoCategoryUpdatePage.getDescriptionInput()).to.eq(
      'description',
      'Expected Description value to be equals to description'
    );

    await videoCategoryUpdatePage.save();
    expect(await videoCategoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await videoCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last VideoCategory', async () => {
    const nbButtonsBeforeDelete = await videoCategoryComponentsPage.countDeleteButtons();
    await videoCategoryComponentsPage.clickOnLastDeleteButton();

    videoCategoryDeleteDialog = new VideoCategoryDeleteDialog();
    expect(await videoCategoryDeleteDialog.getDialogTitle()).to.eq('whatsappstatusApp.videoCategory.delete.question');
    await videoCategoryDeleteDialog.clickOnConfirmButton();

    expect(await videoCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

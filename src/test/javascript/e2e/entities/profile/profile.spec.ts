import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ProfileComponentsPage, ProfileDeleteDialog, ProfileUpdatePage } from './profile.page-object';

const expect = chai.expect;

describe('Profile e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let profileComponentsPage: ProfileComponentsPage;
  let profileUpdatePage: ProfileUpdatePage;
  let profileDeleteDialog: ProfileDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Profiles', async () => {
    await navBarPage.goToEntity('profile');
    profileComponentsPage = new ProfileComponentsPage();
    await browser.wait(ec.visibilityOf(profileComponentsPage.title), 5000);
    expect(await profileComponentsPage.getTitle()).to.eq('whatsappstatusApp.profile.home.title');
    await browser.wait(ec.or(ec.visibilityOf(profileComponentsPage.entities), ec.visibilityOf(profileComponentsPage.noResult)), 1000);
  });

  it('should load create Profile page', async () => {
    await profileComponentsPage.clickOnCreateButton();
    profileUpdatePage = new ProfileUpdatePage();
    expect(await profileUpdatePage.getPageTitle()).to.eq('whatsappstatusApp.profile.home.createOrEditLabel');
    await profileUpdatePage.cancel();
  });

  it('should create and save Profiles', async () => {
    const nbButtonsBeforeCreate = await profileComponentsPage.countDeleteButtons();

    await profileComponentsPage.clickOnCreateButton();

    await promise.all([
      profileUpdatePage.setFirstNameInput('firstName'),
      profileUpdatePage.setLastNameInput('lastName'),
      profileUpdatePage.setEmailInput('nRRe@}6.g'),
      profileUpdatePage.setPhoneInput('phone'),
      profileUpdatePage.setAddressLine1Input('addressLine1'),
      profileUpdatePage.setAddressLine2Input('addressLine2'),
      profileUpdatePage.setCityInput('city'),
      profileUpdatePage.setCountryInput('country'),
    ]);

    expect(await profileUpdatePage.getFirstNameInput()).to.eq('firstName', 'Expected FirstName value to be equals to firstName');
    expect(await profileUpdatePage.getLastNameInput()).to.eq('lastName', 'Expected LastName value to be equals to lastName');
    expect(await profileUpdatePage.getEmailInput()).to.eq('nRRe@}6.g', 'Expected Email value to be equals to nRRe@}6.g');
    expect(await profileUpdatePage.getPhoneInput()).to.eq('phone', 'Expected Phone value to be equals to phone');
    expect(await profileUpdatePage.getAddressLine1Input()).to.eq(
      'addressLine1',
      'Expected AddressLine1 value to be equals to addressLine1'
    );
    expect(await profileUpdatePage.getAddressLine2Input()).to.eq(
      'addressLine2',
      'Expected AddressLine2 value to be equals to addressLine2'
    );
    expect(await profileUpdatePage.getCityInput()).to.eq('city', 'Expected City value to be equals to city');
    expect(await profileUpdatePage.getCountryInput()).to.eq('country', 'Expected Country value to be equals to country');

    await profileUpdatePage.save();
    expect(await profileUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await profileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Profile', async () => {
    const nbButtonsBeforeDelete = await profileComponentsPage.countDeleteButtons();
    await profileComponentsPage.clickOnLastDeleteButton();

    profileDeleteDialog = new ProfileDeleteDialog();
    expect(await profileDeleteDialog.getDialogTitle()).to.eq('whatsappstatusApp.profile.delete.question');
    await profileDeleteDialog.clickOnConfirmButton();

    expect(await profileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

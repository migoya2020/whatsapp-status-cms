import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { WhatsappstatusSharedModule } from 'app/shared/shared.module';

import { LogsComponent } from './logs.component';

import { logsRoute } from './logs.route';

@NgModule({
  imports: [WhatsappstatusSharedModule, RouterModule.forChild([logsRoute])],
  declarations: [LogsComponent],
})
export class LogsModule {}

import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MenuComponent } from './menu/menu.component';
import { MainPageComponent } from './main-page/main-page.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { RegisterPageComponent } from './register-page/register-page.component';
import {UserService} from './shared/user.service';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import { ConferenceComponent } from './conference/conference.component';
import { ConferenceListComponent } from './conference-list/conference-list.component';
import {ConferenceService} from './shared/conference.service';
import { ProposalListComponent } from './proposal-list/proposal-list.component';
import {ProposalService} from "./shared/proposal.service";
import { ProposalNewComponent } from './proposal-new/proposal-new.component';
import { ConferenceNewComponent } from './conference-new/conference-new.component';
import { ProposalUpdateComponent } from './proposal-update/proposal-update.component';
import { ProposalComponent } from './proposal/proposal.component';
import { SectionNewComponent } from './section-new/section-new.component';
import { SectionListComponent } from './section-list/section-list.component';
import {SectionService} from "./shared/section.service";
import { SectionComponent } from './section/section.component';
import { UserInfoComponent } from './user-info/user-info.component';
import { AssignCochairComponent } from './assign-cochair/assign-cochair.component';
import { HelpComponent } from './help/help.component';

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    MainPageComponent,
    LoginPageComponent,
    RegisterPageComponent,
    ConferenceComponent,
    ConferenceListComponent,
    ProposalListComponent,
    ProposalNewComponent,
    ConferenceNewComponent,
    ProposalUpdateComponent,
    ProposalComponent,
    SectionNewComponent,
    SectionListComponent,
    SectionComponent,
    UserInfoComponent,
    AssignCochairComponent,
    HelpComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
  ],
  providers: [UserService, ConferenceService, ProposalService, SectionService],
  bootstrap: [AppComponent]
})
export class AppModule { }

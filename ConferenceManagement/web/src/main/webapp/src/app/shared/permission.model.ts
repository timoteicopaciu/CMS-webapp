import {User} from "./user.model";
import {Conference} from "./conference.model";

export class Permission {
  id:number;
  conference:Conference;
  user: User;
  isAuthor:boolean;
  isPCMember:boolean;
  isSectionChair:boolean;

  constructor(id, conference, user, isAuthor, isPCMember, isSectionChair) {
    this.id = id;
    this.conference = conference;
    this.user = user;
    this.isAuthor = isAuthor;
    this.isPCMember = isPCMember;
    this.isSectionChair = isSectionChair;
  }

}

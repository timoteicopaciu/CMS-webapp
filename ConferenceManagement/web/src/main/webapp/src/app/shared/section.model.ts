import {User} from "./user.model";
import {Conference} from "./conference.model";

export class Section {
  id:number;
  name: string;
  chair:User;
  proposalIDs:number[];
  conference:Conference;


  constructor(id: number, name: string, chair: User, proposalIDs: number[], conference: Conference) {
    this.id = id;
    this.name = name;
    this.chair = chair;
    this.proposalIDs = proposalIDs;
    this.conference = conference;
  }
}


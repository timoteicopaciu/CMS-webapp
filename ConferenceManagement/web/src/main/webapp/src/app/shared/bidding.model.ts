import {User} from "./user.model";
import {Proposal} from "./proposal.model";

export class Bidding {
  id: number;
  accepted: boolean;
  user: User;
  proposal: Proposal;

  constructor(id, accepted, user, proposal) {
    this.id=id;
    this.accepted = accepted;
    this.user = user;
    this.proposal = proposal;
  }
}

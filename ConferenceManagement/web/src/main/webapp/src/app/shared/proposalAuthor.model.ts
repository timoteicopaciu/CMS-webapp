import {User} from "./user.model";
import {Proposal} from "./proposal.model";

export class ProposalAuthor {
  id: number;
  author: User;
  proposal: Proposal;

  constructor(id, author, proposal) {
    this.id = id;
    this.author = author;
    this.proposal = proposal;
  }
}

import {User} from "./user.model";
import {Proposal} from "./proposal.model";

export class Review {
  id: number;
  user: User;
  proposal: Proposal;
  qualifier: string;
  recommendation: string;

  constructor(id, user, proposal, qualifier, recommendation) {
    this.id = id;
    this.user = user;
    this.proposal = proposal;
    this.qualifier = qualifier;
    this.recommendation = recommendation;
  }

}

import {Conference} from "./conference.model";

export class Proposal {
  id:number;
  name:string;
  keywords:string;
  topics:string;
  abstractPaperURL:string;
  fullPaperURL: string;
  presentationURL: string;
  status: string;
  conference:Conference;

  constructor(id, name, keywords, topics, abstractPaperURL, fullPaperURL, presentationURL, status, conference) {
    this.id = id;
    this.name = name;
    this.keywords = keywords;
    this.topics = topics;
    this.abstractPaperURL = abstractPaperURL;
    this.fullPaperURL = fullPaperURL;
    this.presentationURL = presentationURL;
    this.status = status;
    this.conference = conference;
  }
}

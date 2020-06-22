
export class Conference {
  id: number;
  name: string;
  level: number;
  startDate: Date;
  endDate: Date;
  callForPapers: string;
  abstractPaperDeadline: Date;
  fullPaperDeadline: Date;
  biddingDeadline: Date;
  sections: number[];

  constructor(id, name, level, startDate, endDate, callForPapers, abstractPaperDeadline, fullPaperDeadline, biddingDeadline, sections) {
    this.id = id;
    this.name = name;
    this.level = level;
    this.startDate = startDate;
    this.endDate = endDate;
    this.callForPapers = callForPapers;
    this.abstractPaperDeadline = abstractPaperDeadline;
    this.fullPaperDeadline = fullPaperDeadline;
    this.biddingDeadline = biddingDeadline;
    this.sections = sections;
  }
}

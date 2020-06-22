export class User{
  id: number;
  username: string;
  name: string;
  emailAddress: string;
  affiliation: string;
  password: string;
  personalWebsite: string;
  isChair: boolean;
  isCoChair: boolean;
  isSCMember: boolean;

  constructor(id, username, name, email, affiliation, password, personalWebsite, isChair, isCoChair, isSCMember) {
    this.id = id;
    this.username = username;
    this.name = name;
    this.emailAddress = email;
    this.affiliation = affiliation;
    this.password = password;
    this.personalWebsite = personalWebsite;
    this.isChair = isChair;
    this.isCoChair = isCoChair;
    this.isSCMember = isSCMember;
  }
}

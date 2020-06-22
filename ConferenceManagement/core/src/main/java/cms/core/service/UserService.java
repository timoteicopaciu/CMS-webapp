package cms.core.service;


import cms.core.domain.CMSUser;
import cms.core.domain.Conference;
import cms.core.domain.Permission;
import cms.core.domain.ProposalAuthor;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<CMSUser> getUserByUsername(String username);

    Optional<CMSUser> getUserByEmailAddress(String emailAddress);

    List<CMSUser> getUsersByIDs(List<Long> usersIDs);

    List<Conference> getConferencesForPCMember(String username);

    List<CMSUser> getAllUsers();

    CMSUser save(CMSUser CMSUser);

    Optional<CMSUser> getChair();

    List<CMSUser> getSCMembers();

    List<CMSUser> getNonSCMembers();

    Optional<Permission> getPermissionForUserInConference(long userID, long conferenceID);

    Optional<ProposalAuthor> getUserCanBeAuthorInProposal(long userID, long proposalID);

    List<CMSUser> getCandidatesForSectionChair(Long conferenceID);

    boolean isPCMemberInAnyConference(Long userID);

    CMSUser updateUser(CMSUser user);

    List<CMSUser> getUsersWithoutChairs();

}

package cms.core.service;

import cms.core.domain.CMSUser;
import cms.core.domain.Conference;
import cms.core.domain.Permission;
import cms.core.domain.ProposalAuthor;
import cms.core.repo.PermissionRepository;
import cms.core.repo.ProposalAuthorRepository;
import cms.core.repo.SectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cms.core.repo.UserRepository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ProposalAuthorRepository proposalAuthorRepository;

    @Override
    public Optional<CMSUser> getUserByUsername(String username) {
        return userRepository.findAll().stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    @Override
    public Optional<CMSUser> getUserByEmailAddress(String emailAddress) {
        return userRepository.findAll().stream().filter(user -> user.getEmailAddress().equals(emailAddress)).findFirst();
    }

    @Override
    public List<CMSUser> getUsersByIDs(List<Long> usersIDs) {
        return userRepository.findAllById(usersIDs);
    }

    @Override
    public List<Conference> getConferencesForPCMember(String username) {
        System.out.println("innnn getConferencesForPCMember" + username);
        return permissionRepository.findAll()
                .stream()
                .filter(permission -> permission.getCmsUser().getUsername().equals(username))
                .filter(Permission::getIsPCMember)
                .map(Permission::getConference)
                .collect(Collectors.toList());
    }

    @Override
    public List<CMSUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public CMSUser save(CMSUser CMSUser) {
        logger.trace("save Service - method entered CMSUser={}", CMSUser);
        return userRepository.save(CMSUser);
    }


    @Override
    public Optional<CMSUser> getChair() {
        return userRepository.findAll().stream().filter(CMSUser::getIsChair).findFirst();
    }

    @Override
    public List<CMSUser> getSCMembers() {
        return userRepository.findAll().stream()
               .filter(CMSUser::getIsSCMember)
                .collect(Collectors.toList());
    }

    @Override
    public List<CMSUser> getNonSCMembers() {
        return userRepository.findAll().stream().filter(user -> !user.getIsSCMember()).collect(Collectors.toList());
    }

    @Override
    public Optional<Permission> getPermissionForUserInConference(long userID, long conferenceID) {
        return permissionRepository.findAll().stream().filter(permission -> permission.getCmsUser().getId() == userID
        && permission.getConference().getId() == conferenceID).findFirst();
    }

    @Override
    public Optional<ProposalAuthor> getUserCanBeAuthorInProposal(long userID, long proposalID) {
        return proposalAuthorRepository.findAll().stream().filter(proposalAuthor -> proposalAuthor.getUser().getId() == userID
        && proposalAuthor.getProposal().getId() == proposalID).findFirst();
    }

    @Override
    public List<CMSUser> getCandidatesForSectionChair(Long conferenceID) {

        List<Long> sectionChairsIDs =
                permissionRepository.findAll().stream()
                .filter(permission -> permission.getConference().getId().equals(conferenceID))
                .filter(Permission::getIsSectionChair)
                .map(permission -> permission.getCmsUser().getId())
                .collect(Collectors.toList());

        List<CMSUser> SCMembers = userRepository.findAll().stream()
                        .filter(user -> !sectionChairsIDs.contains(user.getId()))
                        .filter(CMSUser::getIsSCMember)
                        .collect(Collectors.toList());

        List<CMSUser> PCMembers = permissionRepository.findAll().stream()
                .filter(Permission::getIsPCMember)
                .filter(permission -> !permission.getIsSectionChair())
                .map(Permission::getCmsUser)
                .collect(Collectors.toList());

        return Stream.concat(SCMembers.stream(), PCMembers.stream())
                .collect(Collectors.toList());

    }

    @Override
    public boolean isPCMemberInAnyConference(Long userID) {
        List<Long> PCMembersIDs = permissionRepository.findAll().stream()
                .filter(permission -> permission.getCmsUser().getId().equals(userID))
                .filter(Permission::getIsPCMember)
                .map(permission -> permission.getCmsUser().getId())
                .collect(Collectors.toList());

        return PCMembersIDs.contains(userID);
    }

    @Override
    @Transactional
    public CMSUser updateUser(CMSUser user) {
        Optional<CMSUser> cmsUserOptional = userRepository.findById(user.getId());
        if(cmsUserOptional.isEmpty())
            return null;

        CMSUser cmsUser = cmsUserOptional.get();

        if(user.getName() != null)
            cmsUser.setName(user.getName());
        if(user.getUsername() != null)
            cmsUser.setUsername(user.getUsername());
        if(user.getEmailAddress() != null)
            cmsUser.setEmailAddress(user.getEmailAddress());
        if(user.getAffiliation() != null)
            cmsUser.setAffiliation(user.getAffiliation());
        if(user.getPersonalWebsite() != null)
            cmsUser.setPersonalWebsite(user.getPersonalWebsite());
        if(user.getIsChair() != null)
            cmsUser.setIsChair(user.getIsChair());
        if(user.getIsCoChair() != null)
            cmsUser.setIsCoChair(user.getIsCoChair());
        if(user.getIsSCMember() != null)
            cmsUser.setIsSCMember(user.getIsSCMember());
        return cmsUser;
    }

    @Override
    public List<CMSUser> getUsersWithoutChairs() {
        return userRepository.findAll().stream()
                .filter(user ->!user.getIsChair().equals(true) && !user.getIsCoChair().equals(true)
                && !user.getIsSCMember().equals(true))
                .collect(Collectors.toList());
    }


}

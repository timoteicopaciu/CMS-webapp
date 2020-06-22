package cms.core.service;

import cms.core.domain.CMSUser;
import cms.core.domain.Conference;
import cms.core.domain.Permission;
import cms.core.domain.Section;
import cms.core.repo.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cms.core.repo.ConferenceRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConferenceServiceImpl implements ConferenceService {
    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Optional<Conference> getConferenceById(Long conferenceId) {
        return conferenceRepository.findById(conferenceId);
    }

    @Override
    public List<Conference> getAll() {
        return conferenceRepository.findAll();
    }

    @Override
    public List<CMSUser> getPCMembersForConference(Long conferenceId) {
        return permissionRepository.findAll().stream()
                .filter(permission -> permission.getConference().getId().equals(conferenceId))
                .filter(Permission::getIsPCMember)
                .map(Permission::getCmsUser)
                .collect(Collectors.toList());
    }

    @Override
    public Conference save(Conference conference) {
        return conferenceRepository.save(conference);
    }


    @Override
    @Transactional
    public Optional<Conference> postponeConference(Long conferenceId, Date newStartDate, Date newEndDate) {
        Conference conference = conferenceRepository.findById(conferenceId).orElse(null);
        if(conference == null)
            return Optional.empty();

        conference.setStartDate(newStartDate);
        conference.setEndDate(newEndDate);
        return Optional.of(conference);
    }

    @Override
    @Transactional
    public Permission saveOrUpdatePermission(Permission permission) {
        Permission newPermission = permissionRepository.findAll().stream().
                filter(permission1 -> permission1.getConference().getId().equals(permission.getConference().getId()) &&
                        permission1.getCmsUser().getId().equals(permission.getCmsUser().getId()))
                .findFirst().orElse(null);

        if(newPermission == null){
            if(permission.getIsAuthor() == null)
                permission.setIsAuthor(false);
            if(permission.getIsPCMember() == null)
                permission.setIsPCMember(false);
            if(permission.getIsSectionChair() == null)
                permission.setIsSectionChair(false);

            permissionRepository.save(permission);
            return permission;
        }

        if(permission.getIsPCMember() != null){
            newPermission.setIsPCMember(permission.getIsPCMember());
        }
        if(permission.getIsAuthor() != null){
            newPermission.setIsAuthor(permission.getIsAuthor());
        }
        if(permission.getIsSectionChair() != null){
            newPermission.setIsSectionChair(permission.getIsSectionChair());
        }
        return newPermission;
    }

    @Override
    @Transactional
    public Optional<Conference> updateConference(Conference conference) {
        Conference updatedConference = conferenceRepository.findById(conference.getId()).orElse(null);
        if(updatedConference == null)
            return Optional.empty();

        if(conference.getCallForPapers() != null)
            updatedConference.setCallForPapers(conference.getCallForPapers());
        if(conference.getStartDate() != null)
            updatedConference.setStartDate(conference.getStartDate());
        if(conference.getEndDate() != null)
            updatedConference.setEndDate(conference.getEndDate());
        if(conference.getBiddingDeadline() != null)
            updatedConference.setBiddingDeadline(conference.getBiddingDeadline());
        if(conference.getAbstractPaperDeadline() != null)
            updatedConference.setAbstractPaperDeadline(conference.getAbstractPaperDeadline());
        if(conference.getFullPaperDeadline() != null)
            updatedConference.setFullPaperDeadline(conference.getFullPaperDeadline());

        return Optional.of(updatedConference);
    }
}

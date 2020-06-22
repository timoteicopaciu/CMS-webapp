package cms.core.service;

import cms.core.domain.CMSUser;
import cms.core.domain.Conference;
import cms.core.domain.Permission;
import cms.core.domain.Section;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ConferenceService {

    Optional<Conference> getConferenceById(Long conferenceId);

    List<Conference> getAll();

    List<CMSUser> getPCMembersForConference(Long conferenceId);

    Conference save(Conference conference);

    Optional<Conference> postponeConference(Long conferenceId, Date newStartDate, Date newEndDate);

    Permission saveOrUpdatePermission(Permission permission);

    Optional<Conference> updateConference(Conference conference);
}

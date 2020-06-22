package cms.core.service;

import cms.core.domain.CMSUser;
import cms.core.domain.Proposal;
import cms.core.domain.Section;

import java.util.List;
import java.util.Optional;

public interface SectionService {

    Optional<Section> getSectionById(Long sectionId);

    List<Section> getSectionsByIDs(List<Long> sectionsIDs);

    List<Section> getAllByConferenceId(Long conferenceId);

    Section save(Section section);


    Optional<Section> updateSectionChair(Long sectionID, CMSUser futureChair);

    List<Proposal> getUnassignedAndAcceptedProposals();

    Section updateSectionProposals(Long sectionID, List<Long> proposalIDs);

    void addParticipantInSection(Long sectionID, Long userID);

    boolean checkParticipantInSection(Long sectionID, Long userID);

}

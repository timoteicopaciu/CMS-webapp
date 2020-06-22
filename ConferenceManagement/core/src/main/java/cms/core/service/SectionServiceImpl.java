package cms.core.service;

import cms.core.domain.CMSUser;
import cms.core.domain.Proposal;
import cms.core.domain.Section;
import cms.core.repo.ProposalRepository;
import cms.core.repo.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cms.core.repo.SectionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class SectionServiceImpl implements SectionService{

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<Section> getSectionById(Long sectionId) {
        return sectionRepository.findById(sectionId);
    }

    @Override
    public List<Section> getSectionsByIDs(List<Long> sectionsIDs) {
        return sectionRepository.findAllById(sectionsIDs);
    }

    @Override
    public List<Section> getAllByConferenceId(Long conferenceId) {
        return sectionRepository.findAll().stream().filter(section -> section.getConference().getId() == conferenceId)
                .collect(Collectors.toList());
    }

    @Override
    public Section save(Section section) {
        section.setParticipants(new ArrayList<>());
        return sectionRepository.save(section);
    }

    @Override
    @Transactional
    public Optional<Section> updateSectionChair(Long sectionID, CMSUser futureChair) {
        Optional<Section> section = sectionRepository.findById(sectionID);
        if(section.isEmpty()){
            return Optional.empty();
        }

        section.get().setSectionChair(futureChair);
        return section;
    }

    @Override
    public List<Proposal> getUnassignedAndAcceptedProposals() {
        List<Long> assignedProposalIDs = new ArrayList<>();
        sectionRepository.findAll().stream()
                .map(Section::getProposals)
                .forEach(proposals -> proposals.forEach(proposal -> assignedProposalIDs.add(proposal.getId())));

        return proposalRepository.findAll().stream()
                .filter(proposal -> proposal.getStatus().equals("Accepted"))
                .filter(proposal -> !assignedProposalIDs.contains(proposal.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Section updateSectionProposals(Long sectionID, List<Long> proposalIDs) {
        List<Proposal> proposals = proposalRepository.findAllById(proposalIDs);
        Optional<Section> updatedSection =
                sectionRepository.findById(sectionID);
        if(updatedSection.isEmpty())
            return null;

        updatedSection.ifPresent(section -> section.setProposals(proposals));

        return updatedSection.get();
    }

    @Override
    @Transactional
    public void addParticipantInSection(Long sectionID, Long userID) {
        userRepository.findById(userID).ifPresent(user -> {
            sectionRepository.findById(sectionID).ifPresent(section -> {
                Hibernate.initialize(section.getParticipants());
                List<CMSUser> participants = section.getParticipants();
                participants.add(user);
                section.setParticipants(participants);
            });
        });
    }

    @Override
    @Transactional
    public boolean checkParticipantInSection(Long sectionID, Long userID) {
        Optional<Section> section = sectionRepository.findById(sectionID);
        if(section.isEmpty())
            return false;

        Section section1 = section.get();
        Hibernate.initialize(section1.getParticipants());
        List<CMSUser> users = section1.getParticipants();
        if(users == null || users.size() == 0)
            return false;
        List<Long> userIDs = users.stream()
                .filter(user -> user.getId().equals(userID))
                .map(CMSUser::getId)
                .collect(Collectors.toList());

        return userIDs.size() > 0;
    }


}

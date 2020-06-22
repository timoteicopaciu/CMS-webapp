package cms.web.converter;

import cms.core.domain.CMSUser;
import cms.core.domain.Proposal;
import cms.core.service.UserService;
import cms.web.dto.ProposalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProposalConverter extends BaseConverter<Proposal, ProposalDTO>{
    @Autowired
    private ConferenceConverter conferenceConverter;

    @Override
    public Proposal convertDtoToModel(ProposalDTO proposalDTO) {
        if(proposalDTO == null)
            return null;
        return Proposal.builder()
                .id(proposalDTO.getId())
                .name(proposalDTO.getName())
                .topics(proposalDTO.getTopics())
                .keywords(proposalDTO.getKeywords())
                .abstractPaperURL(proposalDTO.getAbstractPaperURL())
                .fullPaperURL(proposalDTO.getFullPaperURL())
                .presentationURL(proposalDTO.getPresentationURL())
                .status(proposalDTO.getStatus())
                .conference((proposalDTO.getConference() == null)? null : conferenceConverter.convertDtoToModel(proposalDTO.getConference()))
                .build();
    }

    @Override
    public ProposalDTO convertModelToDto(Proposal proposal) {
        if(proposal == null)
            return null;
        return ProposalDTO.builder()
                .id(proposal.getId())
                .name(proposal.getName())
                .topics(proposal.getTopics())
                .keywords(proposal.getKeywords())
                .abstractPaperURL(proposal.getAbstractPaperURL())
                .fullPaperURL(proposal.getFullPaperURL())
                .presentationURL(proposal.getPresentationURL())
                .status(proposal.getStatus())
                .conference((proposal.getConference() == null)? null : conferenceConverter.convertModelToDto(proposal.getConference()))
                .build();
    }

    public List<Long> convertModelsToIDs(List<Proposal> models) {
        return models.stream()
                .map(Proposal::getId)
                .collect(Collectors.toList());
    }
}

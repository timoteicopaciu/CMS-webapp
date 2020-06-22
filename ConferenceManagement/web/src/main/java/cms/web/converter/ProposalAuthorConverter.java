package cms.web.converter;

import cms.core.domain.ProposalAuthor;
import cms.web.dto.ProposalAuthorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProposalAuthorConverter extends BaseConverter<ProposalAuthor, ProposalAuthorDTO>{

    @Autowired
    private ProposalConverter proposalConverter;

    @Autowired
    private UserConverter userConverter;

    @Override
    public ProposalAuthor convertDtoToModel(ProposalAuthorDTO proposalAuthorDTO) {
        return ProposalAuthor.builder()
                .id(proposalAuthorDTO.getId())
                .proposal((proposalAuthorDTO.getProposal() == null)? null : proposalConverter.convertDtoToModel(proposalAuthorDTO.getProposal()))
                .user((proposalAuthorDTO.getAuthor() == null)? null : userConverter.convertDtoToModel(proposalAuthorDTO.getAuthor()))
                .build();
    }

    @Override
    public ProposalAuthorDTO convertModelToDto(ProposalAuthor proposalAuthor) {
        return ProposalAuthorDTO.builder()
                .id(proposalAuthor.getId())
                .proposal((proposalAuthor.getProposal() == null)? null : proposalConverter.convertModelToDto(proposalAuthor.getProposal()))
                .author((proposalAuthor.getUser() == null)? null : userConverter.convertModelToDto(proposalAuthor.getUser()))
                .build();
    }
}

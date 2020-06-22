package cms.web.converter;

import cms.core.domain.Review;
import cms.web.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter extends BaseConverter<Review, ReviewDTO>{

    @Autowired
    private UserConverter userConverter;

    @Autowired ProposalConverter proposalConverter;

    @Override
    public Review convertDtoToModel(ReviewDTO reviewDTO) {
        if(reviewDTO == null)
            return null;
        return Review.builder()
                .id(reviewDTO.getId())
                .CMSUser((reviewDTO.getUser() == null)? null : userConverter.convertDtoToModel(reviewDTO.getUser()))
                .proposal((reviewDTO.getProposal() == null)? null : proposalConverter.convertDtoToModel(reviewDTO.getProposal()))
                .qualifier(reviewDTO.getQualifier())
                .recommendation(reviewDTO.getRecommendation())
                .build();
    }

    @Override
    public ReviewDTO convertModelToDto(Review review) {
        if(review == null)
            return null;
        return ReviewDTO.builder()
                .id(review.getId())
                .user((review.getCMSUser() == null)? null : userConverter.convertModelToDto(review.getCMSUser()))
                .proposal((review.getProposal() == null)? null : proposalConverter.convertModelToDto(review.getProposal()))
                .qualifier(review.getQualifier())
                .recommendation(review.getRecommendation())
                .build();
    }
}

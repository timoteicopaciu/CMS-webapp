package cms.core.service;

import cms.core.domain.*;

import java.util.List;
import java.util.Optional;

public interface ProposalService {


    List<Proposal> getProposalsByIDs(List<Long> proposalIDs);

    List<Proposal> getAll();

    List<Proposal> getAllByConferenceId(Long conferenceId);

    List<Long> getProposalsIDsForUser(Long userID);

    List<Long> getUnbiddenIDs(Long userID);

    Proposal save(Proposal proposal);


    Proposal update(Proposal newProposal);

    void updateStatus(Long proposalID, String status);

    Bidding bidProposal(Bidding bidding);

    Review reviewProposal(Review review);

    ProposalAuthor addAuthorForProposal(ProposalAuthor proposalAuthor);

    List<CMSUser> getUsersForReviewingAProposal(Long proposalID);

    List<CMSUser> getReviewersForProposal(Long proposalID);

    List<Review> getReviewsForProposal(Long proposalID);

    Optional<Review> updateReview(Review review);

    Optional<Review> getReviewByUserAndProposal(Long userID, Long proposalID);

    boolean checkAuthorWroteAProposal(Long proposalID, Long userID);

    String checkProposalStatus(Long proposalID, Long conferenceLevel);

    void resetReviewsForProposal(Long proposalID);

    List<CMSUser> getAuthorsForProposal(Long proposalID);
}

package cms.core.service;

import cms.core.domain.*;
import cms.core.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProposalServiceImpl implements ProposalService{
    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private BiddingRepository biddingRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProposalAuthorRepository proposalAuthorRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Proposal> getProposalsByIDs(List<Long> proposalIDs) {
        return proposalRepository.findAllById(proposalIDs);
    }

    @Override
    public List<Proposal> getAll() {
        return proposalRepository.findAll();
    }

    @Override
    public List<Proposal> getAllByConferenceId(Long conferenceId) {
        return proposalRepository.findAll().stream().filter(proposal ->
            proposal.getConference().getId().equals(conferenceId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getProposalsIDsForUser(Long userID) {
        return proposalAuthorRepository.findAll().stream().filter(x -> x.getUser().getId().equals(userID))
                .map(x -> x.getProposal().getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getUnbiddenIDs(Long userID) {
        List<Long> allProposalsIDs = proposalRepository.findAll().stream()
                .map(Proposal::getId)
                .collect(Collectors.toList());
        List<Long> biddenIDs = biddingRepository.findAll().stream()
                .filter(bidding -> bidding.getCMSUser().getId().equals(userID))
                .map(bidding -> bidding.getProposal().getId())
                .collect(Collectors.toList());
        return allProposalsIDs.stream().filter(id -> !biddenIDs.contains(id))
                .collect(Collectors.toList());
    }

    @Override
    public Proposal save(Proposal proposal) {
        return proposalRepository.save(proposal);
    }


    @Override
    @Transactional
    public Proposal update(Proposal newProposal) {
        Proposal proposal = proposalRepository.findById(newProposal.getId()).orElse(newProposal);

        proposal.setKeywords(newProposal.getKeywords());
        proposal.setName(newProposal.getName());
        proposal.setTopics(newProposal.getTopics());
        proposal.setAbstractPaperURL(newProposal.getAbstractPaperURL());
        proposal.setFullPaperURL(newProposal.getFullPaperURL());
        proposal.setPresentationURL(newProposal.getPresentationURL());
        proposal.setStatus(newProposal.getStatus());
        proposal.setConference(newProposal.getConference());

        return proposal;
    }

    @Override
    @Transactional
    public void updateStatus(Long proposalID, String status) {
        this.proposalRepository.findById(proposalID).ifPresent(proposal -> proposal.setStatus(status));
    }

    @Override
    public Bidding bidProposal(Bidding bidding) {
        return biddingRepository.save(bidding);
    }

    @Override
    public Review reviewProposal(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public ProposalAuthor addAuthorForProposal(ProposalAuthor proposalAuthor) {
        return proposalAuthorRepository.save(proposalAuthor);
    }

    @Override
    public List<CMSUser> getUsersForReviewingAProposal(Long proposalID) {
        List<Long> userIDs = biddingRepository.findAll().stream()
                .filter(bidding -> bidding.getProposal().getId().equals(proposalID))
                .filter(Bidding::getAccepted)
                .map(bidding -> bidding.getCMSUser().getId())
                .collect(Collectors.toList());
        List<Long> reviewersIDs = reviewRepository.findAll().stream()
                .filter(review -> review.getProposal().getId().equals(proposalID))
                .map(review -> review.getCMSUser().getId())
                .collect(Collectors.toList());
        List<Long> usersToReviewIDs = userIDs.stream().filter(id -> !reviewersIDs.contains(id))
                .collect(Collectors.toList());

        return userRepository.findAllById(usersToReviewIDs);
    }

    @Override
    public List<CMSUser> getReviewersForProposal(Long proposalID) {
        return reviewRepository.findAll().stream()
                .filter(review -> review.getProposal().getId().equals(proposalID))
                .map(Review::getCMSUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> getReviewsForProposal(Long proposalID) {
        return reviewRepository.findAll().stream()
                .filter(review -> review.getProposal().getId().equals(proposalID))
                .filter(review -> !review.getQualifier().equals(""))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<Review> updateReview(Review review) {
        Review newReview = reviewRepository.findById(review.getId()).orElse(null);

        if(newReview == null)
            return Optional.empty();

        newReview.setQualifier(review.getQualifier());
        newReview.setRecommendation(review.getRecommendation());

        return Optional.of(newReview);
    }

    @Override
    public Optional<Review> getReviewByUserAndProposal(Long userID, Long proposalID) {
        return reviewRepository.findAll().stream().filter(review -> review.getCMSUser().getId().equals(userID)
            && review.getProposal().getId().equals(proposalID)).findFirst();
    }

    @Override
    public boolean checkAuthorWroteAProposal(Long proposalID, Long userID) {
        Optional<ProposalAuthor> found = proposalAuthorRepository.findAll().stream()
                .filter(proposalAuthor -> proposalAuthor.getProposal().getId().equals(proposalID)
                && proposalAuthor.getUser().getId().equals(userID))
                .findFirst();

        return found.isPresent();

    }

    @Override
    public String checkProposalStatus(Long proposalID, Long conferenceLevel) {
        List<String> qualifiersForProposal = reviewRepository.findAll().stream()
                .filter(review -> !review.getQualifier().equals(""))
                .filter(review -> review.getProposal().getId().equals(proposalID))
                .map(Review::getQualifier)
                .collect(Collectors.toList());

        if(qualifiersForProposal.size() != conferenceLevel)
            return "pending review";

        int accepted = 0;
        int rejected = 0;
        for(String qualifier: qualifiersForProposal){
            if (qualifier.contains("Accept"))
                accepted++;
            else if(qualifier.contains("Reject"))
                rejected++;
        }

        if(conferenceLevel == 2){
            if(accepted == 2)
                return "Accepted";
            else if(rejected == 2)
                return "Rejected";
            else
                return "Undecided";
        }
        else if(conferenceLevel == 3){
            if(accepted >= 2)
                return "Accepted";
            else if(rejected >= 2)
                return "Rejected";
            else
                return "Undecided";
        }
        else{
            if(accepted >= 3)
                return "Accepted";
            else if(rejected >= 3)
                return "Rejected";
            else
                return "Undecided";
        }
    }

    @Override
    @Transactional
    public void resetReviewsForProposal(Long proposalID) {
        this.reviewRepository.findAll().stream()
                .filter(review -> review.getProposal().getId().equals(proposalID))
                .forEach(review -> {review.setQualifier(""); review.setRecommendation("");});

        this.proposalRepository.findById(proposalID).ifPresent(proposal -> proposal.setStatus("pending review"));
    }

    @Override
    public List<CMSUser> getAuthorsForProposal(Long proposalID) {
        return proposalAuthorRepository.findAll().stream()
                .filter(proposalAuthor -> proposalAuthor.getProposal().getId().equals(proposalID))
                .map(ProposalAuthor::getUser)
                .collect(Collectors.toList());
    }

}

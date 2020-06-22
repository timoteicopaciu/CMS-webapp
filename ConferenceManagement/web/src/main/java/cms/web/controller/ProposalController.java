package cms.web.controller;

import cms.core.domain.*;
import cms.core.repo.ProposalRepository;
import cms.core.service.ProposalService;
import cms.web.FileHelper;
import cms.web.converter.*;
import cms.web.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ProposalController{
    public static final Logger logger = LoggerFactory.getLogger(ConferenceController.class);

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private ProposalConverter proposalConverter;

    @Autowired
    private ProposalAuthorConverter proposalAuthorConverter;

    @Autowired
    private BiddingConverter biddingConverter;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private ReviewConverter reviewConverter;

    @RequestMapping(value = "/proposal/getProposals", method = RequestMethod.GET)
    List<ProposalDTO> getProposals(){
        List<Proposal> proposals = proposalService.getAll();
        return new ArrayList<>(proposalConverter.convertModelsToDtos(proposals));
    }

    @RequestMapping(value = "/proposal/getProposalsForIDs", method = RequestMethod.POST)
    List<ProposalDTO> getProposalsForIDs(@RequestBody List<Long> proposalsIDs){
        List<Proposal> proposals = proposalService.getProposalsByIDs(proposalsIDs);
        return new ArrayList<>(proposalConverter.convertModelsToDtos(proposals));
    }

    @RequestMapping(value = "/proposal/getProposalsForConference", method = RequestMethod.POST)
    List<ProposalDTO> getProposalsForConference(@RequestBody Long conferenceID){
        List<Proposal> proposals = proposalService.getAllByConferenceId(conferenceID);
        return new ArrayList<>(proposalConverter.convertModelsToDtos(proposals));
    }

    @RequestMapping(value = "/proposal/saveProposal", method = RequestMethod.POST)
    ProposalDTO saveProposal(@RequestBody ProposalDTO proposalDTO){
        return proposalConverter.convertModelToDto(proposalService.save(proposalConverter.convertDtoToModel(proposalDTO)));
    }

    @RequestMapping(value = "/proposal/addAuthorForProposal", method = RequestMethod.POST)
    ProposalAuthorDTO addAuthorForProposal(@RequestBody ProposalAuthorDTO proposalAuthorDTO){
        ProposalAuthor proposalAuthor = proposalAuthorConverter
                .convertDtoToModel(proposalAuthorDTO);
        return proposalAuthorConverter.convertModelToDto(
                proposalService.addAuthorForProposal(proposalAuthor));
    }

    @RequestMapping(value = "/proposal/updateProposal", method = RequestMethod.POST)
    ProposalDTO updateProposal(@RequestBody ProposalDTO newProposalDTO){
        return proposalConverter.convertModelToDto(proposalService.update(proposalConverter.convertDtoToModel(newProposalDTO)));
    }

    @RequestMapping(value = "/proposal/getProposalsIDsForUser", method = RequestMethod.POST)
    List<Long> getProposalsIDsForUser(@RequestBody Long userID){
        return proposalService.getProposalsIDsForUser(userID);
    }

    @RequestMapping(value ="/proposal/bidProposal", method = RequestMethod.POST)
    BiddingDTO bidProposal(@RequestBody BiddingDTO biddingDTO){
        Bidding bidding = proposalService.bidProposal(biddingConverter.convertDtoToModel(biddingDTO));
        return biddingConverter.convertModelToDto(bidding);
    }

    @RequestMapping(value = "/proposal/reviewProposal", method = RequestMethod.POST)
    ReviewDTO reviewProposal(@RequestBody ReviewDTO reviewDTO){
        logger.trace("proposalController, review proposal, review = {}", reviewDTO);
        Review review = proposalService.reviewProposal(reviewConverter.convertDtoToModel(reviewDTO));
        logger.trace("proposalController, review proposal, saved review = {}", review);
        return reviewConverter.convertModelToDto(review);
    }

    @RequestMapping(value = "/proposal/getUnbiddenProposalsIDs", method = RequestMethod.POST)
    List<Long> getUnbiddenProposalsIDs(@RequestBody Long userID) {
        return proposalService.getUnbiddenIDs(userID);
    }

    @RequestMapping(value = "/proposal/getUsersForReviewingAProposal", method = RequestMethod.POST)
    List<UserDTO> getUsersForReviewingAProposal(@RequestBody Long proposalID){
        List<CMSUser> users = proposalService.getUsersForReviewingAProposal(proposalID);
        return new ArrayList<>(userConverter.convertModelsToDtos(users));
    }

    @RequestMapping(value = "/proposal/getReviewersIDsForProposal", method = RequestMethod.POST)
    List<Long> getReviewersIDsForProposal(@RequestBody Long proposalID){
        return proposalService.getReviewersForProposal(proposalID).stream()
                .map(CMSUser::getId)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/proposal/getReviewersForProposal", method = RequestMethod.POST)
    List<CMSUser> getReviewersForProposal(@RequestBody Long proposalID){
        return proposalService.getReviewersForProposal(proposalID);
    }

    @RequestMapping(value = "/proposal/updateReview", method = RequestMethod.POST)
    ReviewDTO updateReview(@RequestBody ReviewDTO reviewDTO){
        Optional<Review> updatedReview = proposalService.updateReview(reviewConverter.convertDtoToModel(reviewDTO));

        if(updatedReview.isEmpty()){
            return null;
        }

        return reviewConverter.convertModelToDto(updatedReview.get());
    }

    @RequestMapping(value = "/proposal/getReviewByUserAndProposal", method = RequestMethod.POST)
    ReviewDTO getReviewByUserAndProposal(@RequestBody Long[] IDs){
        Optional<Review> review = proposalService.getReviewByUserAndProposal(IDs[0], IDs[1]);

        if(review.isEmpty()){
            return null;
        }
        return reviewConverter.convertModelToDto(review.get());
    }

    @RequestMapping(value = "/proposal/getReviewsForProposal", method = RequestMethod.POST)
    List<ReviewDTO> getReviewsForProposal(@RequestBody Long proposalID) {
        logger.trace("in proposalController, getReviewsForProposal, proposalID = {}", proposalID);
        List<Review> reviewList = proposalService.getReviewsForProposal(proposalID);
        logger.trace("in proposalController, getReviewsForProposal, reviewList = {}", reviewList);
        List<ReviewDTO> reviewDTOList = reviewConverter.convertModelsToDtos(reviewList);
        logger.trace("in proposalController, getReviewsForProposal, reviewDTOList = {}", reviewDTOList);
        return reviewDTOList;
    }

    @RequestMapping(value = "/proposal/checkAuthorWroteAProposal", method = RequestMethod.POST)
    boolean checkAuthorWroteAProposal(@RequestBody Long[] IDs){
        return proposalService.checkAuthorWroteAProposal(IDs[0], IDs[1]);
    }

    @RequestMapping(value = "/proposal/checkProposalStatus", method = RequestMethod.POST)
    String checkProposalStatus(@RequestBody Long[] params){
        logger.trace("in ProposalController, checkProposalStatus, proposalID = {}, conferenceLevel = {}", params[0], params[1]);
        String status = proposalService.checkProposalStatus(params[0], params[1]);
        logger.trace("in ProposalController, checkProposalStatus, status = {}", status);
        proposalService.updateStatus(params[0], status);
        logger.trace("in ProposalController, checkProposalStatus, status updated");
        return status;
    }

    @RequestMapping(value = "/proposal/resetReviewsForProposal", method = RequestMethod.POST)
    String resetReviewsForProposal(@RequestBody Long proposalID){
        logger.trace("in ProposalController, restReviewsForProposal, proposalID = {}", proposalID);

        this.proposalService.resetReviewsForProposal(proposalID);
        return "OK";
    }

    @RequestMapping(value = "/proposal/uploadAbstractPaper/{id}", method = RequestMethod.PUT)
    boolean uploadAbstract(@PathVariable Long id, @RequestParam("file") MultipartFile content) {
        logger.trace("in ProposalController, uploadAbstract");
        String abstractUrl = FileHelper.storeFile(content, "D:\\Faculty\\MyProjects\\CMS-webapp\\ConferenceManagement\\web\\src\\main\\webapp\\src\\assets\\files\\" + "Abstract"+id+".pdf");
        logger.trace("in ProposalController, uploadAbstract, abstractUrl = {}", abstractUrl);
        return true;
    }

    @RequestMapping(value = "/proposal/getAbstractPaper/{id}", method = RequestMethod.GET)
    ResponseEntity<Resource> getAbstract(@PathVariable Long id) {
        String url = "D:\\Faculty\\MyProjects\\CMS-webapp\\ConferenceManagement\\web\\src\\main\\webapp\\src\\assets\\files\\Abstract"+id+".pdf";
        Resource file = FileHelper.loadFileAsResource(url);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename())
                .body(file);
    }

    @RequestMapping(value = "/proposal/uploadFullPaper/{id}", method = RequestMethod.PUT)
    boolean uploadFullPaper(@PathVariable Long id, @RequestParam("file") MultipartFile content) {
        logger.trace("in ProposalController, uploadFullPaper");
        String url = FileHelper.storeFile(content, "D:\\Faculty\\MyProjects\\CMS-webapp\\ConferenceManagement\\web\\src\\main\\webapp\\src\\assets\\files\\" + "FullPaper"+id+".pdf");
        logger.trace("in ProposalController, uploadFullPaper, abstractUrl = {}", url);
        return true;
    }

    @RequestMapping(value = "/proposal/getFullPaper/{id}", method = RequestMethod.GET)
    ResponseEntity<Resource> getFullPaper(@PathVariable Long id) {
        String url = "D:\\Faculty\\MyProjects\\CMS-webapp\\ConferenceManagement\\web\\src\\main\\webapp\\src\\assets\\files\\FullPaper"+id+".pdf";
        Resource file = FileHelper.loadFileAsResource(url);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename())
                .body(file);
    }

    @RequestMapping(value = "/proposal/uploadPresentation/{id}", method = RequestMethod.PUT)
    boolean uploadPresentation(@PathVariable Long id, @RequestParam("file") MultipartFile content) {
        logger.trace("in ProposalController, uploadPresentation");
        String url = FileHelper.storeFile(content, "D:\\Faculty\\MyProjects\\CMS-webapp\\ConferenceManagement\\web\\src\\main\\webapp\\src\\assets\\files\\" + "Presentation"+id+".pdf");
        logger.trace("in ProposalController, uploadPresentation, abstractUrl = {}", url);
        return true;
    }

    @RequestMapping(value = "/proposal/getPresentation/{id}", method = RequestMethod.GET)
    ResponseEntity<Resource> getPresentation(@PathVariable Long id) {
        String url = "D:\\Faculty\\MyProjects\\CMS-webapp\\ConferenceManagement\\web\\src\\main\\webapp\\src\\assets\\files\\Presentation"+id+".pdf";
        Resource file = FileHelper.loadFileAsResource(url);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename())
                .body(file);
    }

    @RequestMapping(value = "/proposal/getAuthorsForProposal", method = RequestMethod.POST)
    List<UserDTO> getAuthorsForProposal(@RequestBody Long proposalID){
        List<CMSUser> authors = proposalService.getAuthorsForProposal(proposalID);
        return new ArrayList<>(userConverter.convertModelsToDtos(authors));
    }
}

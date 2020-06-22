package cms.web.controller;

import cms.core.domain.CMSUser;
import cms.core.domain.Permission;
import cms.core.domain.ProposalAuthor;
import cms.core.service.ConferenceService;
import cms.core.service.UserService;
import cms.web.converter.ConferenceConverter;
import cms.web.converter.PermissionConverter;
import cms.web.converter.ProposalAuthorConverter;
import cms.web.converter.UserConverter;
import cms.web.dto.ConferenceDTO;
import cms.web.dto.PermissionDTO;
import cms.web.dto.ProposalAuthorDTO;
import cms.web.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private ConferenceConverter conferenceConverter;

    @Autowired
    private PermissionConverter permissionConverter;

    @RequestMapping(value = "/user/getUsers", method = RequestMethod.GET)
    List<UserDTO> getUsers(){
        List<CMSUser> users = userService.getAllUsers();
        return new ArrayList<>(userConverter.convertModelsToDtos(users));
    }

    @RequestMapping(value = "/user/getNonSCMembersAndNonPCMembers", method = RequestMethod.POST)
    List<UserDTO> getNonSCMembersAndNonPCMembers(@RequestBody Long conferenceId){
        List<Long> PCMembersIDs = conferenceService.getPCMembersForConference(conferenceId)
                .stream().map(CMSUser::getId)
                .collect(Collectors.toList());

        List<CMSUser> result = userService.getNonSCMembers().stream()
                .filter(user -> !PCMembersIDs.contains(user.getId()))
                .collect(Collectors.toList());
        return new ArrayList<>(userConverter.convertModelsToDtos(result));
    }

    @RequestMapping(value = "/user/getSCMembers", method = RequestMethod.GET)
    List<UserDTO> getSCMembers(){
        List<CMSUser> users = userService.getSCMembers();
        return new ArrayList<>(userConverter.convertModelsToDtos(users));
    }

    @RequestMapping(value = "/user/getUsersByIDs", method = RequestMethod.POST)
    List<UserDTO> getUsersByIDs(@RequestBody List<Long> usersIDs){
        List<CMSUser> users = userService.getUsersByIDs(usersIDs);
        return new ArrayList<>(userConverter.convertModelsToDtos(users));
    }
    
    @RequestMapping(value = "/user/saveUser", method = RequestMethod.POST)
    UserDTO saveUser(@RequestBody UserDTO userDTO){
        logger.trace("saveUser Controller -method entered userDTO{}", userDTO);
        Optional<CMSUser> user1 = userService.getUserByUsername(userDTO.getUsername());
        Optional<CMSUser> user2 = userService.getUserByEmailAddress(userDTO.getEmailAddress());
        if(user1.isPresent() || user2.isPresent()){
            return null;
        }
        return userConverter.convertModelToDto(userService.save(userConverter.convertDtoToModel(userDTO)));
    }

    @RequestMapping(value = "/user/checkUser", method = RequestMethod.POST)
    UserDTO checkUser(@RequestBody String[] args){
        String username = args[0];
        String password = args[1];
        Optional<CMSUser> cmsUser = userService.getUserByUsername(username);
        if(cmsUser.isPresent() && cmsUser.get().getPassword().equals(password)){
            return userConverter.convertModelToDto(cmsUser.get());
        }
        return null;
    }

    @RequestMapping(value = "/user/getConferencesForPCMember", method = RequestMethod.POST)
    List<ConferenceDTO> getConferencesForPCMember(@RequestBody String username){
        return conferenceConverter.convertModelsToDtos(userService.getConferencesForPCMember(username));
    }

    @RequestMapping(value = "/user/getUserByUsername", method = RequestMethod.POST)
    UserDTO getUserByUsername(@RequestBody String username){
        Optional<CMSUser> user = userService.getUserByUsername(username);
        if(user.isEmpty())
            return null;
        return userConverter.convertModelToDto(user.get());
    }

    @RequestMapping(value = "/user/getUserByEmail", method = RequestMethod.POST)
    UserDTO getUserByEmail(@RequestBody String email){
        logger.trace("in getUserByEmail, controller, email = {}",  email);
        Optional<CMSUser> user = userService.getUserByEmailAddress(email);
        logger.trace("user = {}", user);
        if(user.isEmpty())
            return null;
        return userConverter.convertModelToDto(user.get());
    }

    @RequestMapping(value = "user/getPermissionForUserInConference", method = RequestMethod.POST)
    PermissionDTO getPermissionForUserInConference(@RequestBody Long[] IDs){
        Long userID = IDs[0];
        Long conferenceID = IDs[1];
        Optional<Permission> permission = userService.getPermissionForUserInConference(userID, conferenceID);

        if(permission.isEmpty()){
            return null;
        }
        return permissionConverter.convertModelToDto(permission.get());
    }

    @RequestMapping(value = "user/getUserCanBeAuthorInProposal", method = RequestMethod.POST)
    boolean getUserCanBeAuthorInProposal(@RequestBody Long[] IDs){
        Long userID = IDs[0];
        Long proposalID = IDs[1];
        Optional<ProposalAuthor> proposalAuthor = userService.getUserCanBeAuthorInProposal(userID, proposalID);

        return proposalAuthor.isPresent();
    }

    @RequestMapping(value = "user/checkIfIsPCMemberInAnyConference", method = RequestMethod.POST)
    boolean checkIfIsPCMemberInAnyConference(@RequestBody Long userID){
        logger.trace("in UserController, checkIfIsPCMemberInAnyConference, userID = {}", userID);
        boolean status = userService.isPCMemberInAnyConference(userID);
        logger.trace("in UserController, checkIfIsPCMemberInAnyConference, status = {}", status);
        return status;
    }

    @RequestMapping(value = "user/updateUser", method = RequestMethod.POST)
    UserDTO updateUser(@RequestBody UserDTO userDTO){
        logger.trace("in UserController, updateUser, userDTO = {}", userDTO);
        CMSUser user = userService.updateUser(userConverter.convertDtoToModel(userDTO));
        logger.trace("in UserController, updateUser, updated user = {}", user);
        return userConverter.convertModelToDto(user);
    }

    @RequestMapping(value = "user/getUsersWithoutChairs", method = RequestMethod.GET)
    List<UserDTO> getUsersWithoutChairs(){
        List<CMSUser> users = userService.getUsersWithoutChairs();
        return new ArrayList<>(userConverter.convertModelsToDtos(users));
    }

}

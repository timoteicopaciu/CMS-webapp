package cms.web.converter;

import cms.core.domain.CMSUser;
import cms.core.domain.Section;
import cms.web.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter extends BaseConverter<CMSUser, UserDTO> {
    @Override
    public CMSUser convertDtoToModel(UserDTO userDTO) {
        if(userDTO == null)
            return null;
        return CMSUser.builder()
                .id(userDTO.getId())
                .affiliation(userDTO.getAffiliation())
                .emailAddress(userDTO.getEmailAddress())
                .isChair(userDTO.getIsChair())
                .isCoChair(userDTO.getIsCoChair())
                .isSCMember(userDTO.getIsSCMember())
                .name(userDTO.getName())
                .password(userDTO.getPassword())
                .personalWebsite(userDTO.getPersonalWebsite())
                .username(userDTO.getUsername())
                .build();
    }

    @Override
    public UserDTO convertModelToDto(CMSUser user) {
        if(user == null)
            return null;

        return UserDTO.builder()
                .id(user.getId())
                .affiliation(user.getAffiliation())
                .emailAddress(user.getEmailAddress())
                .isChair(user.getIsChair())
                .isCoChair(user.getIsCoChair())
                .isSCMember(user.getIsSCMember())
                .name(user.getName())
                .password(user.getPassword())
                .personalWebsite(user.getPersonalWebsite())
                .username(user.getUsername())
                .build();
    }

}

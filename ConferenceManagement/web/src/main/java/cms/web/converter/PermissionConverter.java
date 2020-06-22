package cms.web.converter;

import cms.core.domain.Permission;
import cms.web.dto.PermissionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PermissionConverter extends BaseConverter<Permission, PermissionDTO>{

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private ConferenceConverter conferenceConverter;

    @Override
    public Permission convertDtoToModel(PermissionDTO permissionDTO) {
        if(permissionDTO == null)
            return null;
        return Permission.builder()
                .id(permissionDTO.getId())
                .cmsUser((permissionDTO.getUser() == null)? null : userConverter.convertDtoToModel(permissionDTO.getUser()))
                .conference((permissionDTO.getConference() == null)? null : conferenceConverter.convertDtoToModel(permissionDTO.getConference()))
                .isAuthor(permissionDTO.getIsAuthor())
                .isPCMember(permissionDTO.getIsPCMember())
                .isSectionChair(permissionDTO.getIsSectionChair())
                .build();
    }

    @Override
    public PermissionDTO convertModelToDto(Permission permission) {
        if(permission == null)
            return null;
        return PermissionDTO.builder()
                .id(permission.getId())
                .user((permission.getCmsUser() == null)? null : userConverter.convertModelToDto(permission.getCmsUser()))
                .conference((permission.getConference() == null)? null : conferenceConverter.convertModelToDto(permission.getConference()))
                .isAuthor(permission.getIsAuthor())
                .isPCMember(permission.getIsPCMember())
                .isSectionChair(permission.getIsSectionChair())
                .build();
    }
}

package cms.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PermissionDTO implements Serializable {
    private Long id;
    private ConferenceDTO conference;
    private UserDTO user;
    private Boolean isAuthor;
    private Boolean isPCMember;
    private Boolean isSectionChair;
}

package cms.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Permission implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Conference conference;

    @ManyToOne
    private CMSUser cmsUser;

    private Boolean isAuthor;
    private Boolean isPCMember;
    private Boolean isSectionChair;

}

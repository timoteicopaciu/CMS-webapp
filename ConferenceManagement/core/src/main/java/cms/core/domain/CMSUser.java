package cms.core.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "emailAddress"),
        @UniqueConstraint(columnNames = "username")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CMSUser implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String name;
    private String emailAddress;
    private String affiliation;
    private String personalWebsite;
    private Boolean isChair;
    private Boolean isCoChair;
    private Boolean isSCMember;

}

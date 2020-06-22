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
public class Review implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Proposal proposal;

    @ManyToOne
    private CMSUser CMSUser;

    private String qualifier;
    @Column(length = 3000)
    private String recommendation;

}

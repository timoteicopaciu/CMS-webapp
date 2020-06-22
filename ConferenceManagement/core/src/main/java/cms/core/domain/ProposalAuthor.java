package cms.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProposalAuthor implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private CMSUser user;

    @ManyToOne
    private Proposal proposal;
}

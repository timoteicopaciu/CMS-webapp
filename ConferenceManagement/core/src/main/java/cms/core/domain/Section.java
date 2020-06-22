package cms.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Section implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToOne
    private CMSUser sectionChair;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Proposal> proposals = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    private List<CMSUser> participants = new ArrayList<>();

    @ManyToOne
    private Conference conference;
}

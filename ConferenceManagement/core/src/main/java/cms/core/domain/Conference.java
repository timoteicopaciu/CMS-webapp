package cms.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Conference implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 500)
    private String name;
    private Date startDate;
    private Date endDate;
    @Column(length = 3000)
    private String callForPapers;
    private Date abstractPaperDeadline;
    private Date fullPaperDeadline;
    private Date biddingDeadline;
    private Integer level;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "conference")
    private List<Section> sections = new ArrayList<>();
}

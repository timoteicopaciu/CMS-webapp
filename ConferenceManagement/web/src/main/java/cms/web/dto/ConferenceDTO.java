package cms.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ConferenceDTO implements Serializable {
    private Long id;

    private String name;
    private Date startDate;
    private Date endDate;
    private String callForPapers;
    private Date abstractPaperDeadline;
    private Date fullPaperDeadline;
    private Date biddingDeadline;
    private Integer level;
    private List<Long> sectionsIDs;

}

package cms.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SectionDTO implements Serializable {
    private Long id;
    private String name;
    private UserDTO chair;
    private List<Long> proposalIDs;
    private ConferenceDTO conference;
}

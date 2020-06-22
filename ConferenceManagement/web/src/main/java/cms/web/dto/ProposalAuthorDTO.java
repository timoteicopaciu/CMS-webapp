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
public class ProposalAuthorDTO implements Serializable {
    private Long id;
    private UserDTO author;
    private ProposalDTO proposal;
}

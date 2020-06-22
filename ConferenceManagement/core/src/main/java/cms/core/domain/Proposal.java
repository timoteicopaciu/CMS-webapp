package cms.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Proposal implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 500)
    private String name;
    @Column(length = 1000)
    private String keywords;
    @Column(length = 1000)
    private String topics;
    private String abstractPaperURL;
    private String fullPaperURL;
    private String presentationURL;
    private String status;

    @ManyToOne
    private Conference conference;
}

package service;

import cms.core.domain.Conference;
import cms.core.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Component
public class ConferenceServiceTest {

    @Autowired
    private ConferenceService conferenceService;

    void save_Test(){
        Conference conference =
                new Conference(null, "conf1", null, null, "call", null,null, null, 1, new ArrayList<>());
        assert (conferenceService.save(conference) == conference);
    }

    void update_Test(){
        Conference conference =
                new Conference(1l, "conf2", null, null, "call", null,null, null, 1, new ArrayList<>());

        Optional<Conference> conferenceOptional = conferenceService.updateConference(conference);
        assert (conferenceOptional.isPresent());
        assert (conferenceOptional.get() == conference);
    }

    void postponeConference_Test(){
        Date date1 = new Date();
        Date date2 = new Date();
        Conference conference =
                new Conference(1l, "conf1",date1 , date2, "call", null,null, null, 1, new ArrayList<>());
        Optional<Conference> conference1 = conferenceService.postponeConference(1l, date1, date2);
        assert (conference1.isPresent());
        assert (conference1.get().equals(conference));
    }

    public static void main(String[] args) {
        ConferenceServiceTest conferenceServiceTest = new ConferenceServiceTest();

        conferenceServiceTest.save_Test();
        conferenceServiceTest.postponeConference_Test();
    }
}

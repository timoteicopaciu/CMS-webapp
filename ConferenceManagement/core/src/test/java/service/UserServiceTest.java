package service;

import cms.core.domain.CMSUser;
import cms.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceTest {

    @Autowired
    private UserService userService;

    void getUserByUsername_Test(){
        assert (userService.getUserByUsername("test").isPresent());
        assert (userService.getUserByUsername("test1").isEmpty());
    }

    void getUserByEmailAddress_Test(){
        assert (userService.getUserByEmailAddress("test@gmail.com").isPresent());
        assert (userService.getUserByEmailAddress("test@sdfdsfs").isEmpty());
    }

    void save_Test(){
        CMSUser cmsUser =
                new CMSUser(null, "username", "pass", "name", "emailAddress", "aff", null, false, false, false);
        assert (userService.save(cmsUser) == cmsUser);
    }

    void update_Test(){
        CMSUser cmsUser =
                new CMSUser(null, "username1", "pass", "name", "emailAddress", "aff", null, false, false, false);
        assert (userService.updateUser(cmsUser) == cmsUser);
    }

    void getAllUsers_Test(){
        assert (userService.getAllUsers().size() != 0);
    }

    public static void main(String[] args) {
        UserServiceTest userServiceTest = new UserServiceTest();

        userServiceTest.getUserByUsername_Test();
        userServiceTest.getUserByEmailAddress_Test();
        userServiceTest.save_Test();
        userServiceTest.update_Test();
        userServiceTest.getAllUsers_Test();

    }
}

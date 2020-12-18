package rpc.server.service;

import org.springframework.stereotype.Service;
import rpc.api.UserService;
import rpc.api.domain.User;

@Service
public class UserServiceImpl implements UserService {
    public User findByName(String username) {
        final User user = new User();
        user.setUsername("KK-" + username + "-" + System.currentTimeMillis());
        user.setAge(18);
        return user;
    }
}

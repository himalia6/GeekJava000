package rpc.api;

import rpc.api.domain.User;

public interface UserService {
    User findByName(String username);
}

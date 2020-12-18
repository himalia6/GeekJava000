package rpc.client;

import lombok.extern.slf4j.Slf4j;
import rpc.api.RpcStubHelper;
import rpc.api.UserService;
import rpc.api.domain.User;

@Slf4j
public class RpcClientApplication {
    public static void main(String[] args) {
        UserService userService = RpcStubHelper.getService(UserService.class, "http://localhost:8080");
        final User tom = userService.findByName("tom");
        log.info("user {}", tom);
    }
}

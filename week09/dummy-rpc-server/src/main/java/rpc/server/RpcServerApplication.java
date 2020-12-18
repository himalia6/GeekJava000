package rpc.server;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rpc.api.DummyRpcRequest;
import rpc.api.DummyRpcResponse;
import rpc.api.UserService;
import rpc.api.domain.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@RestController
@SpringBootApplication
public class RpcServerApplication implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(RpcServerApplication.class, args);
    }

    @PostMapping("/")
    public DummyRpcResponse call(@RequestBody DummyRpcRequest request)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Class<?> serviceClass = request.getServiceClass();
        final Object service = this.applicationContext.getBean(serviceClass);

        final Class<?>[] parameterTypes = new Class<?>[request.getParameters().length];
        for (int i = 0; i < request.getParameters().length; i++) {
            parameterTypes[i] = request.getParameters()[i].getClass();
        }

        final Method method = serviceClass.getMethod(request.getMethod(), parameterTypes);

        final Object result = method.invoke(service, request.getParameters());
        final DummyRpcResponse<Object> response = new DummyRpcResponse<>();
        response.setResult(result);
        response.setStatus(true);
        return response;
    }
//
//    @Bean
//    public UserService userService() {
//        return new UserServiceImpl();
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

package rpc.api;

import lombok.Data;

@Data
public class DummyRpcRequest {
    private Class<?> serviceClass;

    private String method;

    private Object[] parameters;
}

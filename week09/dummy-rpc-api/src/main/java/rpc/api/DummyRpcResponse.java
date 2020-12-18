package rpc.api;

import lombok.Data;

@Data
public class DummyRpcResponse<T> {
    private T result;

    private boolean status;
}

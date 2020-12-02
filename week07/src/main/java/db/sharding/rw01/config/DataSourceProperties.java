package db.sharding.rw01.config;

import lombok.Data;


@Data
public class DataSourceProperties {

    private String poolName;

    private String url;

    private String username;

    private String password;

    private String driver;

    private boolean readOnly;
}

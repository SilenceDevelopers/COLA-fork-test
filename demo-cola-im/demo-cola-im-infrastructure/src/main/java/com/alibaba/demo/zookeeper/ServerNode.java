package com.alibaba.demo.zookeeper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerNode implements Serializable {

    private Long id;

    private String host;

    private Integer port;

    private Integer weight = 0;

    public ServerNode(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getAddress() {
        return host + ":" + port;
    }

    @Override
    public String toString() {
        return host + ":" + port + ":" + id;
    }
}

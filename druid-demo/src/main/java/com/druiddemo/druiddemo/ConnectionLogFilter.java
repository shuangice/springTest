package com.druiddemo.druiddemo;

import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class ConnectionLogFilter extends FilterEventAdapter {
    @Override
    public void connection_connectBefore(FilterChain chain, Properties info) {
        System.out.println("链接数据库之前的操作");
    }

    @Override
    public void connection_connectAfter(ConnectionProxy connection) {
        System.out.println("链接数据库之后的操作");
    }
}

package com.omvoid.jmqsc;

import com.ibm.mq.constants.MQConstants;

import java.util.Hashtable;

public class Configuration {

    private String host;
    private String channel;
    private int port;
    private String userID;
    private String password;

    public static Configuration defaultConfiguration() {
        Configuration conf = new Configuration();
        conf.channel = "DEV.ADMIN.SVRCONN";
        conf.host = "127.0.0.1";
        conf.port = 1414;
        conf.userID = "admin";
        conf.password = "passw0rd";
        return conf;
    }

    public Hashtable<String, Object> getProperties() {
        Hashtable<String, Object> properties = new Hashtable<>();
        properties.put(MQConstants.HOST_NAME_PROPERTY, host);
        properties.put(MQConstants.PORT_PROPERTY, port);
        properties.put(MQConstants.CHANNEL_PROPERTY, channel);
        boolean needAuth = (userID != null && !userID.isEmpty() && password != null && !password.isEmpty());
        properties.put(MQConstants.USE_MQCSP_AUTHENTICATION_PROPERTY, needAuth);
        if(needAuth) {
            properties.put(MQConstants.USER_ID_PROPERTY, userID);
            properties.put(MQConstants.PASSWORD_PROPERTY, password);
        }
        return properties;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

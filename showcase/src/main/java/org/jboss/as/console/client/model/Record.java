package org.jboss.as.console.client.model;

/**
 * @author Heiko Braun
 * @date 7/12/11
 */
public interface Record {


    String getKey();
    void setKey(String key);

    String getValue();
    void setValue(String value);
}

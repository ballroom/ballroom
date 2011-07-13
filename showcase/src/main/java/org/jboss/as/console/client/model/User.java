package org.jboss.as.console.client.model;

/**
 * @author Heiko Braun
 * @date 7/13/11
 */
public interface User {
    
    String getFirstName();
    void setFirstName(String name);
    
    String getLastName();
    void setLastName(String name);

    String getEmail();
    void setEmail(String email);

    boolean isPlainText();
    void setPlainText(boolean b);
    
}

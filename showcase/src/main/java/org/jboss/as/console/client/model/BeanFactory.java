package org.jboss.as.console.client.model;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * @author Heiko Braun
 * @date 7/12/11
 */
public interface BeanFactory extends AutoBeanFactory {

    AutoBean<Record> record();
    AutoBean<User> user();
}

package org.jboss.as.console.client.model;

import com.google.gwt.autobean.shared.AutoBean;
import com.google.gwt.autobean.shared.AutoBeanFactory;

/**
 * @author Heiko Braun
 * @date 7/12/11
 */
public interface BeanFactory extends AutoBeanFactory {

    AutoBean<Record> record();
}

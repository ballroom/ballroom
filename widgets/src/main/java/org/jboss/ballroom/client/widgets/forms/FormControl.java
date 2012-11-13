package org.jboss.ballroom.client.widgets.forms;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author Heiko Braun
 * @date 11/12/12
 */
public interface FormControl extends IsWidget {

    /**
     * Reset the form back to its unedited state.
     */
    void cancel();

    /**
     * Enable/disable this form.
     *
     * @param b
     */
    void setEnabled(boolean b);

    /**
     * Validate the form.
     * @return The errors (if any).
     */
    FormValidation validate();


    /**
     * Clear form values
     */
    void clearValues();
}

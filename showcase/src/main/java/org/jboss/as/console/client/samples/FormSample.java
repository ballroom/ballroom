package org.jboss.as.console.client.samples;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.as.console.client.model.BeanFactory;
import org.jboss.as.console.client.model.User;
import org.jboss.as.console.client.spi.Framework;
import org.jboss.as.console.client.widgets.ContentGroupLabel;
import org.jboss.as.console.client.widgets.common.DefaultButton;
import org.jboss.as.console.client.widgets.forms.CheckBoxItem;
import org.jboss.as.console.client.widgets.forms.DisclosureGroupRenderer;
import org.jboss.as.console.client.widgets.forms.Form;
import org.jboss.as.console.client.widgets.forms.TextBoxItem;

import java.util.Map;


/**
 * @author Heiko Braun
 * @date 7/13/11
 */
public class FormSample implements Sample{

    private Framework framework = GWT.create(Framework.class);
    private BeanFactory factory = (BeanFactory)framework.getBeanFactory();

    private Form<User> form;

    @Override
    public String getId() {
        return "forms";
    }

    @Override
    public String getName() {
        return "Forms";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Widget asWidget() {

        VerticalPanel layout = new VerticalPanel();
        layout.setStyleName("rhs-content-panel");

        HTML desc = new HTML("<p>Form editing and data binding. click on the 'Enable' button to start editing</p>");
        layout.add(desc);

        // enable
        DefaultButton enableBtn = new DefaultButton("Enable", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                form.setEnabled(true);
            }
        });
        layout.add(enableBtn);

        // changeset
        DefaultButton diffBtn = new DefaultButton("Changed Values", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Map<String,Object> changedValues = form.getChangedValues();
                StringBuilder sb = new StringBuilder();
                for(String key : changedValues.keySet())
                {
                    Object value = changedValues.get(key);
                    sb.append("- ").append(key).append(": ").append(value).append("\n");
                }
                Window.alert(sb.toString());
            }
        });
        layout.add(diffBtn);

        ContentGroupLabel delim = new ContentGroupLabel("Databinding Example");
        layout.add(delim);

        form = new Form<User>(User.class);
        TextBoxItem firstName = new TextBoxItem("firstName", "First Name");
        TextBoxItem lastName = new TextBoxItem("lastName", "Last Name");
        TextBoxItem email = new TextBoxItem("email", "Email Address");
        CheckBoxItem likes = new CheckBoxItem("plainText", "Prefers Plain Text Email?");


        form.setFields(firstName, lastName, email);
        form.setFieldsInGroup("Preferences", new DisclosureGroupRenderer(), likes);

        layout.add(form.asWidget());


        // add some data
        User user = factory.user().as();
        user.setFirstName("Peter");
        user.setLastName("Post");
        user.setEmail("me@somewhere.com");
        user.setPlainText(true);

        // edit through form
        form.edit(user);

        // disable by default
        form.setEnabled(false);

        return layout;
    }
}

package org.jboss.ballroom.client.widgets.forms;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ResolveExpressionEvent extends GwtEvent<ResolveExpressionEvent.ExpressionResolveListener> {

    public static final Type TYPE = new Type<ExpressionResolveListener>();

    private String expressionValue;

    public ResolveExpressionEvent(String expressionValue) {
        this.expressionValue = expressionValue;
    }

    public Type<ExpressionResolveListener> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ExpressionResolveListener listener) {
        listener.onResolveExpressionEvent(expressionValue);
    }

    public String getExpressionValue() {
        return expressionValue;
    }

    public interface ExpressionResolveListener extends EventHandler {
        void onResolveExpressionEvent(String expr);
    }
}



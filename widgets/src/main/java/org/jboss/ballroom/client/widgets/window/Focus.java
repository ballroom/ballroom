package org.jboss.ballroom.client.widgets.window;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

import java.util.LinkedList;
import java.util.List;

/**
 * Used to trap focus in modal windows.
 *
 * @author Heiko Braun
 * @date 3/12/12
 */
public class Focus {

    private static final String[] INPUT_TYPES = new String[]{"INPUT", "TEXTAREA"};
    private static final String[] BUTTON_TYPES = new String[]{"BUTTON"};
    private static final String[] SUPPORTED_TYPES = new String[]{"INPUT", "TEXTAREA", "SELECT", "CHECKBOX", "A", "BUTTON"};

    private List<Element> focusable = new LinkedList<Element>();
    private int currentFocus = 0;
    private boolean includeChildren = false;

    private Element rootElement;

    public Focus(Element root) {
        reset(root);
    }

    public void reset(Element root)
    {
        rootElement = root;
        reset();
    }

    public void reset() {
        focusable = new LinkedList<Element>();
        currentFocus = 0;
        includeChildren = false;
        findFocusable(rootElement, focusable, includeChildren);

        //System.out.println("num focusable: " + focusable.size());
    }

    public void next() {
        if(focusable.isEmpty()) return;

        int next = currentFocus+1;
        if(next>focusable.size()-1)
            next = 0;

        setFocus(focusable.get(next));
        currentFocus = next;
    }

    private void setFocus(Element element) {

        //System.out.println("on: "+element.getTagName());
        element.focus();
    }

    public void prev() {
        if(focusable.isEmpty()) return;

        int prev = currentFocus-1;
        if(prev<0)
            prev = focusable.size()-1;

        setFocus(focusable.get(prev));

        currentFocus = prev;
    }

    /*public static void flagFocusable(Element element, boolean isFocusable)
    {
        element.setAttribute("focusable", String.valueOf(isFocusable));
    } */

    private void findFocusable(Element root, List<Element> focusable, boolean include)
    {
        NodeList<Node> children = root.getChildNodes();

        for(int i=0; i<children.getLength(); i++)
        {
            Node child = children.getItem(i);
            if(Node.ELEMENT_NODE == child.getNodeType())
            {
                Element childElement = (Element)child;

                //if(childElement.hasAttribute("focusable"))
                //    includeChildren = childElement.getAttribute("focusable").equals("true");

                if(childElement.getTabIndex()>=0)
                {
                    String tagName = childElement.getTagName();
                    //System.out.println(tagName);

                    for(String supported : SUPPORTED_TYPES)
                    {
                        if(tagName.equalsIgnoreCase(supported))
                        {
                            focusable.add(childElement);
                            break;
                        }

                    }

                }

                findFocusable(childElement, focusable, include);

            }
        }

    }

    public void onFirstInput() {
        currentFocus = focusOn(focusable, INPUT_TYPES);
    }

    public void onFirstButton() {
        currentFocus = focusOn(focusable, BUTTON_TYPES);
    }

    private static int focusOn(List<Element> focusable, String[] types)
    {
        int index = 0;
        for(int i=0; i<focusable.size(); i++)
        {
            Element element = focusable.get(i);
            String tagName = element.getTagName();

            Element match = null;

            for(String type : types)
            {
                if(tagName.equalsIgnoreCase(type))
                {
                    match = element;
                    break;
                }
            }

            if(match!=null)
            {
                match.focus();
                index =i;
                break;
            }
        }

        return index;
    }

    public static native Element getActiveElement()
        /*-{
            if($wnd.document.activeElement)
                return $wnd.document.activeElement;
            else
                return null;
        }-*/;
}


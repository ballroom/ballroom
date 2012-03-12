package org.jboss.ballroom.client.widgets.window;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

import java.util.LinkedList;
import java.util.List;

/**
 * Used to trap focus in model windows.
 *
 * @author Heiko Braun
 * @date 3/12/12
 */
public class Focus {

    private List<Element> focusable = new LinkedList<Element>();
    private int currentFocus = 0;
    private boolean includeChildren = false;

    private Element rootElement;

    public Focus(Element root) {
        this.rootElement = root;

        findFocusable(root, focusable, includeChildren);
    }

    public void reset() {
        focusable = new LinkedList<Element>();
        currentFocus = 0;
        includeChildren = false;
        findFocusable(rootElement, focusable, includeChildren);
    }

    public void next() {
        if(focusable.isEmpty()) return;

        // forward
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
        // backward
        if(focusable.isEmpty()) return;

        // forward
        int prev = currentFocus-1;
        if(prev<0)
            prev = focusable.size()-1;

        setFocus(focusable.get(prev));

        currentFocus = prev;
    }


    private void findFocusable(Element root, List<Element> focusable, boolean include)
    {
        NodeList<Node> children = root.getChildNodes();

        for(int i=0; i<children.getLength(); i++)
        {
            Node child = children.getItem(i);
            if(Node.ELEMENT_NODE == child.getNodeType())
            {
                Element childElement = (Element)child;

                if(childElement.hasAttribute("focusable"))
                    includeChildren = childElement.getAttribute("focusable").equals("true");

                if(includeChildren && childElement.getTabIndex()>=0)
                {
                    String tagName = childElement.getTagName();
                    //System.out.println(tagName);
                    if(tagName.equals("INPUT")
                            || tagName.equals("TEXTAREA")
                            || tagName.equals("BUTTON")
                            || tagName.equals("A"))
                    {
                        focusable.add(childElement);
                    }

                }

                findFocusable(childElement, focusable, include);

            }
        }

    }

    public void setDefault() {
        currentFocus = focusOnInput(focusable);
    }

    private static int focusOnInput(List<Element> focusable)
    {
        int index = 0;
        for(int i=0; i<focusable.size(); i++)
        {
            Element element = focusable.get(i);
            String tagName = element.getTagName();
            if(tagName.equals("INPUT")
                    || tagName.equals("TEXTAREA"))
            {
                element.focus();
                index =i;
                break;
            }
        }

        return index;
    }
}


package ca.datamagic.noaa.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by Greg on 1/1/2016.
 */
public final class XmlUtils {
    public static final String replaceEntities(String text) {
        StringBuffer xml = new StringBuffer();
        for (int index = 0; index < text.length(); index++) {
            char current = text.charAt(index);
            switch (current) {
                case '\'': xml.append("&apos;"); break;
                case '\"': xml.append("&quot;"); break;
                case '&': xml.append("&amp;"); break;
                case '<': xml.append("&lt;"); break;
                case '>': xml.append("&gt;"); break;
                default: xml.append(current); break;
            }
        }
        return xml.toString();
    }

    public static final String getText(Node node) {
        if (node.hasChildNodes()) {
            NodeList childNodes = node.getChildNodes();
            if (childNodes.getLength() > 0) {
                Node child = childNodes.item(0);
                if ((child.getNodeType() == Node.CDATA_SECTION_NODE) ||
                        (child.getNodeType() == Node.TEXT_NODE)) {
                    return child.getNodeValue();
                }
            }
        }
        return null;
    }
}

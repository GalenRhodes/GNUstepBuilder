//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2019.04.04 at 01:41:58 PM EDT
//

package com.galenrhodes.gnustep.options.data;

import javax.xml.bind.annotation.*;

/**
 * <p>Java class for CustomLibNameType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CustomLibNameType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="selected" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomLibNameType", propOrder = {
        "value"
})
public class CustomLibNameType {

    @XmlValue
    protected String  value;
    @XmlAttribute(name = "selected")
    protected Boolean selected;

    /**
     * Gets the value of the value property.
     *
     * @return possible object is {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value allowed object is {@link String }
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the selected property.
     *
     * @return possible object is {@link Boolean }
     */
    public boolean isSelected() {
        if(selected == null) {
            return true;
        }
        else {
            return selected;
        }
    }

    /**
     * Sets the value of the selected property.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSelected(Boolean value) {
        this.selected = value;
    }

}

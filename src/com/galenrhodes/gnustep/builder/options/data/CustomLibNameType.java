//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2019.04.17 at 10:44:00 AM EDT
//


package com.galenrhodes.gnustep.builder.options.data;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for CustomLibNameType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CustomLibNameType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *       &lt;attribute name="selected" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomLibNameType", namespace = "http://www.projectgalen.com/gnustepbuilder", propOrder = {
    "value"
})
public class CustomLibNameType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "selected")
    protected Boolean selected;

    /**
     * Gets the value of the value property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public synchronized String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public synchronized void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the selected property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public synchronized boolean isSelected() {
        if (selected == null) {
            return true;
        } else {
            return selected;
        }
    }

    /**
     * Sets the value of the selected property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public synchronized void setSelected(Boolean value) {
        this.selected = value;
    }

}

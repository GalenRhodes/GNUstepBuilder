//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2019.04.18 at 12:19:28 PM EDT
//


package com.galenrhodes.gnustep.builder.options.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for Modules complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Modules"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SourceLocations" type="{http://www.projectgalen.com/gnustepbuilder}SourceLocation" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Modules", namespace = "http://www.projectgalen.com/gnustepbuilder", propOrder = {
        "sourceLocations"
})
public class Modules {

    @XmlElement(name = "SourceLocations", namespace = "http://www.projectgalen.com/gnustepbuilder", required = true)
    protected List<SourceLocation> sourceLocations;

    /**
     * Gets the value of the sourceLocations property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sourceLocations property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSourceLocations().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SourceLocation }
     *
     *
     */
    public synchronized List<SourceLocation> getSourceLocations() {
        if(sourceLocations == null) {
            sourceLocations = new ArrayList<SourceLocation>();
        }
        return this.sourceLocations;
    }

}

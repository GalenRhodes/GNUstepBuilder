//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.04.14 at 12:02:31 PM EDT 
//


package com.galenrhodes.gnustep.builder.options.data;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LibraryComboEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LibraryComboEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ng-gnu-gnu"/&gt;
 *     &lt;enumeration value="gnu-gnu-gnu"/&gt;
 *     &lt;enumeration value="apple-apple-apple"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LibraryComboEnum", namespace = "http://www.projectgalen.com/gnustepbuilder")
@XmlEnum
public enum LibraryComboEnum {

    @XmlEnumValue("ng-gnu-gnu")
    NG_GNU_GNU("ng-gnu-gnu"),
    @XmlEnumValue("gnu-gnu-gnu")
    GNU_GNU_GNU("gnu-gnu-gnu"),
    @XmlEnumValue("apple-apple-apple")
    APPLE_APPLE_APPLE("apple-apple-apple");
    private final String value;

    LibraryComboEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LibraryComboEnum fromValue(String v) {
        for (LibraryComboEnum c: LibraryComboEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

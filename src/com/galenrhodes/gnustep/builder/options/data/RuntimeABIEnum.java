//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.04.17 at 10:44:00 AM EDT 
//


package com.galenrhodes.gnustep.builder.options.data;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RuntimeABIEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RuntimeABIEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="GNU"/&gt;
 *     &lt;enumeration value="1.9"/&gt;
 *     &lt;enumeration value="2.0"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RuntimeABIEnum", namespace = "http://www.projectgalen.com/gnustepbuilder")
@XmlEnum
public enum RuntimeABIEnum {

    GNU("GNU"),
    @XmlEnumValue("1.9")
    V1_9("1.9"),
    @XmlEnumValue("2.0")
    V2_0("2.0");
    private final String value;

    RuntimeABIEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RuntimeABIEnum fromValue(String v) {
        for (RuntimeABIEnum c: RuntimeABIEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

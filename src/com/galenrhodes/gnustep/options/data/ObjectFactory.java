//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2019.04.04 at 01:41:58 PM EDT
//

package com.galenrhodes.gnustep.options.data;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated in the com.galenrhodes.gnustep.options.data package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation for XML content. The Java representation of XML content can consist of schema derived interfaces and classes representing the
 * binding of schema type definitions, element declarations and model groups.  Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GNUstepBuildOptions_QNAME = new QName("http://www.projectgalen.com/gnustepbuilder/options", "GNUstepBuildOptions");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.galenrhodes.gnustep.options.data
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ClangTargetsType }
     */
    public ClangTargetsType createClangTargetsType() {
        return new ClangTargetsType();
    }

    /**
     * Create an instance of {@link CustomLibNameType }
     */
    public CustomLibNameType createCustomLibNameType() {
        return new CustomLibNameType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GNUstepOptions }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.projectgalen.com/gnustepbuilder/options", name = "GNUstepBuildOptions")
    public JAXBElement<GNUstepOptions> createGNUstepBuildOptions(GNUstepOptions value) {
        return new JAXBElement<GNUstepOptions>(_GNUstepBuildOptions_QNAME, GNUstepOptions.class, null, value);
    }

    /**
     * Create an instance of {@link GNUstepOptions }
     */
    public GNUstepOptions createGNUstepOptions() {
        return new GNUstepOptions();
    }

    /**
     * Create an instance of {@link ListValuesType }
     */
    public ListValuesType createListValuesType() {
        return new ListValuesType();
    }

    /**
     * Create an instance of {@link ValuesArrayType }
     */
    public ValuesArrayType createValuesArrayType() {
        return new ValuesArrayType();
    }

}

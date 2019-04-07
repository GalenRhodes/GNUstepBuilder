//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2019.04.05 at 12:54:41 PM EDT
//


package com.galenrhodes.gnustep.options.data;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for GNUstepOptions complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="GNUstepOptions"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="InstallPrerequisiteSoftware" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="InstallLibkqueue" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="CreateEntriesIn_ld_so_conf_d" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="BuildClang" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="ClangTargets" type="{http://www.projectgalen.com/gnustepbuilder}ClangTargetsType"/&gt;
 *         &lt;element name="BuildGoogleTest" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="UseSwiftLibDispatch" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="BuildLibDispatchFirst" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="OldABICompat" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="RuntimeABI" type="{http://www.projectgalen.com/gnustepbuilder}RuntimeABIEnum"/&gt;
 *         &lt;element name="CustomLibName" type="{http://www.projectgalen.com/gnustepbuilder}CustomLibNameType" minOccurs="0"/&gt;
 *         &lt;element name="BuildMakeTwice" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="NoMixedABI" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="ObjectiveCArc" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="DebugByDefault" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="NativeObjectiveCExceptions" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="LibraryCombo" type="{http://www.w3.org/2001/XMLSchema}anyType"/&gt;
 *         &lt;element name="Layout" type="{http://www.w3.org/2001/XMLSchema}anyType"/&gt;
 *         &lt;element name="WorkingDirectory" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
 *         &lt;element name="InstallDirectory" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
 *         &lt;element name="ListValues" type="{http://www.projectgalen.com/gnustepbuilder}ListValuesType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GNUstepOptions", propOrder = {
        "installPrerequisiteSoftware",
        "installLibkqueue",
        "createEntriesInLdSoConfD",
        "buildClang",
        "clangTargets",
        "buildGoogleTest",
        "useSwiftLibDispatch",
        "buildLibDispatchFirst",
        "oldABICompat",
        "runtimeABI",
        "customLibName",
        "buildMakeTwice",
        "noMixedABI",
        "objectiveCArc",
        "debugByDefault",
        "nativeObjectiveCExceptions",
        "libraryCombo",
        "layout",
        "workingDirectory",
        "installDirectory",
        "listValues"
})
public class GNUstepOptions {

    @XmlElement(name = "InstallPrerequisiteSoftware", defaultValue = "false")
    protected Boolean           installPrerequisiteSoftware;
    @XmlElement(name = "InstallLibkqueue", defaultValue = "false")
    protected Boolean           installLibkqueue;
    @XmlElement(name = "CreateEntriesIn_ld_so_conf_d", defaultValue = "false")
    protected Boolean           createEntriesInLdSoConfD;
    @XmlElement(name = "BuildClang", defaultValue = "false")
    protected Boolean           buildClang;
    @XmlElement(name = "ClangTargets", required = true)
    protected ClangTargetsType  clangTargets;
    @XmlElement(name = "BuildGoogleTest", defaultValue = "false")
    protected Boolean           buildGoogleTest;
    @XmlElement(name = "UseSwiftLibDispatch", defaultValue = "true")
    protected Boolean           useSwiftLibDispatch;
    @XmlElement(name = "BuildLibDispatchFirst", defaultValue = "true")
    protected Boolean           buildLibDispatchFirst;
    @XmlElement(name = "OldABICompat", defaultValue = "false")
    protected Boolean           oldABICompat;
    @XmlElement(name = "RuntimeABI", required = true)
    @XmlSchemaType(name = "string")
    protected RuntimeABIEnum    runtimeABI;
    @XmlElement(name = "CustomLibName")
    protected CustomLibNameType customLibName;
    @XmlElement(name = "BuildMakeTwice", defaultValue = "false")
    protected Boolean           buildMakeTwice;
    @XmlElement(name = "NoMixedABI", defaultValue = "true")
    protected Boolean           noMixedABI;
    @XmlElement(name = "ObjectiveCArc", defaultValue = "true")
    protected Boolean           objectiveCArc;
    @XmlElement(name = "DebugByDefault", defaultValue = "true")
    protected Boolean           debugByDefault;
    @XmlElement(name = "NativeObjectiveCExceptions", defaultValue = "true")
    protected Boolean           nativeObjectiveCExceptions;
    @XmlElement(name = "LibraryCombo", required = true)
    protected Object            libraryCombo;
    @XmlElement(name = "Layout", required = true)
    protected Object            layout;
    @XmlElement(name = "WorkingDirectory")
    protected Object            workingDirectory;
    @XmlElement(name = "InstallDirectory")
    protected Object            installDirectory;
    @XmlElement(name = "ListValues", required = true)
    protected ListValuesType    listValues;

    /**
     * Gets the value of the clangTargets property.
     *
     * @return
     *     possible object is
     *     {@link ClangTargetsType }
     *
     */
    public ClangTargetsType getClangTargets() {
        return clangTargets;
    }

    /**
     * Sets the value of the clangTargets property.
     *
     * @param value
     *     allowed object is
     *     {@link ClangTargetsType }
     *
     */
    public void setClangTargets(ClangTargetsType value) {
        this.clangTargets = value;
    }

    /**
     * Gets the value of the customLibName property.
     *
     * @return
     *     possible object is
     *     {@link CustomLibNameType }
     *
     */
    public CustomLibNameType getCustomLibName() {
        return customLibName;
    }

    /**
     * Sets the value of the customLibName property.
     *
     * @param value allowed object is {@link CustomLibNameType }
     */
    public void setCustomLibName(CustomLibNameType value) {
        this.customLibName = value;
    }

    /**
     * Gets the value of the installDirectory property.
     *
     * @return possible object is {@link Object }
     */
    public Object getInstallDirectory() {
        return installDirectory;
    }

    /**
     * Sets the value of the installDirectory property.
     *
     * @param value allowed object is {@link Object }
     */
    public void setInstallDirectory(Object value) {
        this.installDirectory = value;
    }

    /**
     * Gets the value of the layout property.
     *
     * @return
     *     possible object is
     *     {@link Object }
     *
     */
    public Object getLayout() {
        return layout;
    }

    /**
     * Sets the value of the layout property.
     *
     * @param value allowed object is {@link Object }
     */
    public void setLayout(Object value) {
        this.layout = value;
    }

    /**
     * Gets the value of the libraryCombo property.
     *
     * @return possible object is {@link Object }
     */
    public Object getLibraryCombo() {
        return libraryCombo;
    }

    /**
     * Sets the value of the libraryCombo property.
     *
     * @param value
     *     allowed object is
     *     {@link Object }
     *
     */
    public void setLibraryCombo(Object value) {
        this.libraryCombo = value;
    }

    /**
     * Gets the value of the listValues property.
     *
     * @return possible object is {@link ListValuesType }
     */
    public ListValuesType getListValues() {
        return listValues;
    }

    /**
     * Sets the value of the listValues property.
     *
     * @param value allowed object is {@link ListValuesType }
     */
    public void setListValues(ListValuesType value) {
        this.listValues = value;
    }

    /**
     * Gets the value of the runtimeABI property.
     *
     * @return possible object is {@link RuntimeABIEnum }
     */
    public RuntimeABIEnum getRuntimeABI() {
        return runtimeABI;
    }

    /**
     * Sets the value of the runtimeABI property.
     *
     * @param value allowed object is {@link RuntimeABIEnum }
     */
    public void setRuntimeABI(RuntimeABIEnum value) {
        this.runtimeABI = value;
    }

    /**
     * Gets the value of the workingDirectory property.
     *
     * @return possible object is {@link Object }
     */
    public Object getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Sets the value of the workingDirectory property.
     *
     * @param value allowed object is {@link Object }
     */
    public void setWorkingDirectory(Object value) {
        this.workingDirectory = value;
    }

    /**
     * Gets the value of the buildClang property.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isBuildClang() {
        return buildClang;
    }

    /**
     * Gets the value of the buildGoogleTest property.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isBuildGoogleTest() {
        return buildGoogleTest;
    }

    /**
     * Gets the value of the buildLibDispatchFirst property.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isBuildLibDispatchFirst() {
        return buildLibDispatchFirst;
    }

    /**
     * Gets the value of the buildMakeTwice property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isBuildMakeTwice() {
        return buildMakeTwice;
    }

    /**
     * Gets the value of the createEntriesInLdSoConfD property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCreateEntriesInLdSoConfD() {
        return createEntriesInLdSoConfD;
    }

    /**
     * Gets the value of the debugByDefault property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isDebugByDefault() {
        return debugByDefault;
    }

    /**
     * Gets the value of the installLibkqueue property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isInstallLibkqueue() {
        return installLibkqueue;
    }

    /**
     * Gets the value of the installPrerequisiteSoftware property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isInstallPrerequisiteSoftware() {
        return installPrerequisiteSoftware;
    }

    /**
     * Gets the value of the nativeObjectiveCExceptions property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isNativeObjectiveCExceptions() {
        return nativeObjectiveCExceptions;
    }

    /**
     * Gets the value of the noMixedABI property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isNoMixedABI() {
        return noMixedABI;
    }

    /**
     * Gets the value of the objectiveCArc property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isObjectiveCArc() {
        return objectiveCArc;
    }

    /**
     * Gets the value of the oldABICompat property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isOldABICompat() {
        return oldABICompat;
    }

    /**
     * Gets the value of the useSwiftLibDispatch property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isUseSwiftLibDispatch() {
        return useSwiftLibDispatch;
    }

    /**
     * Sets the value of the buildClang property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setBuildClang(Boolean value) {
        this.buildClang = value;
    }

    /**
     * Sets the value of the buildGoogleTest property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setBuildGoogleTest(Boolean value) {
        this.buildGoogleTest = value;
    }

    /**
     * Sets the value of the buildLibDispatchFirst property.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setBuildLibDispatchFirst(Boolean value) {
        this.buildLibDispatchFirst = value;
    }

    /**
     * Sets the value of the buildMakeTwice property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setBuildMakeTwice(Boolean value) {
        this.buildMakeTwice = value;
    }

    /**
     * Sets the value of the createEntriesInLdSoConfD property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCreateEntriesInLdSoConfD(Boolean value) {
        this.createEntriesInLdSoConfD = value;
    }

    /**
     * Sets the value of the debugByDefault property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setDebugByDefault(Boolean value) {
        this.debugByDefault = value;
    }

    /**
     * Sets the value of the installLibkqueue property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setInstallLibkqueue(Boolean value) {
        this.installLibkqueue = value;
    }

    /**
     * Sets the value of the installPrerequisiteSoftware property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setInstallPrerequisiteSoftware(Boolean value) {
        this.installPrerequisiteSoftware = value;
    }

    /**
     * Sets the value of the nativeObjectiveCExceptions property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setNativeObjectiveCExceptions(Boolean value) {
        this.nativeObjectiveCExceptions = value;
    }

    /**
     * Sets the value of the noMixedABI property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setNoMixedABI(Boolean value) {
        this.noMixedABI = value;
    }

    /**
     * Sets the value of the objectiveCArc property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setObjectiveCArc(Boolean value) {
        this.objectiveCArc = value;
    }

    /**
     * Sets the value of the oldABICompat property.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setOldABICompat(Boolean value) {
        this.oldABICompat = value;
    }

    /**
     * Sets the value of the useSwiftLibDispatch property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setUseSwiftLibDispatch(Boolean value) {
        this.useSwiftLibDispatch = value;
    }

}

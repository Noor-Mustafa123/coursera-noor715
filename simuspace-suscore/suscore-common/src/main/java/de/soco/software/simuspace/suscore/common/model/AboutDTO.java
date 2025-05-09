package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class AboutDTO is to show about menu and support menu in simuspace.
 *
 * @author Noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class AboutDTO {

    /**
     * The id.
     */
    private String id;

    /**
     * The version.
     */
    @UIColumn( data = "version", name = "version", filter = "text", renderer = "text", title = "3000100x4", isShow = true, orderNum = 1 )
    private String version;

    /**
     * The type.
     */
    @UIColumn( data = "type", name = "type", filter = "text", renderer = "text", title = "3000051x4", isShow = true, orderNum = 2 )
    private String type;

    /**
     * The build fe simuspace.
     */
    @UIColumn( data = "buildFe", name = "buildFe", filter = "text", renderer = "text", title = "3000076x4", isShow = true, orderNum = 3 )
    private String buildFe;

    /**
     * The build be simuspace.
     */
    @UIColumn( data = "buildBe", name = "buildBe", filter = "text", renderer = "text", title = "3000077x4", isShow = true, orderNum = 4 )
    private String buildBe;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the builds the fe.
     *
     * @return the builds the fe
     */
    public String getBuildFe() {
        return buildFe;
    }

    /**
     * Sets the builds the fe.
     *
     * @param buildFe
     *         the new builds the fe
     */
    public void setBuildFe( String buildFe ) {
        this.buildFe = buildFe;
    }

    /**
     * Gets the builds the be.
     *
     * @return the builds the be
     */
    public String getBuildBe() {
        return buildBe;
    }

    /**
     * Sets the builds the be.
     *
     * @param buildBe
     *         the new builds the be
     */
    public void setBuildBe( String buildBe ) {
        this.buildBe = buildBe;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *         the new version
     */
    public void setVersion( String version ) {
        this.version = version;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

}

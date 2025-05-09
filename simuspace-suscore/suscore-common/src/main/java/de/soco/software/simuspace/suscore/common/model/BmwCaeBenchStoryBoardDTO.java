package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class BmwCaeBenchStoryBoardDTO.
 *
 * @author Ali Haider
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class BmwCaeBenchStoryBoardDTO extends BmwCaeBenchDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -836148646414915797L;

    /**
     * The type.
     */
    @UIFormField( name = "type", title = "3000173x4", orderNum = 3 )
    @UIColumn( data = "type", filter = "text", renderer = "text", title = "3000173x4", name = "type", orderNum = 3 )
    private String type;

    /**
     * The discipline context.
     */
    @UIFormField( name = "disciplineContext", title = "3000185x4", orderNum = 4 )
    @UIColumn( data = "disciplineContext", filter = "text", renderer = "text", title = "3000185x4", name = "disciplineContext", orderNum = 4 )
    private String disciplineContext;

    /**
     * The project.
     */
    @UIFormField( name = "project", title = "3000166x4", orderNum = 8 )
    @UIColumn( data = "project", filter = "text", renderer = "text", title = "3000166x4", name = "project", orderNum = 8 )
    private String project;

    /**
     * Gets the project.
     *
     * @return the project
     */
    public String getProject() {
        return project;
    }

    /**
     * Sets the project.
     *
     * @param project
     *         the new project
     */
    public void setProject( String project ) {
        this.project = project;
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

    /**
     * Gets the discipline context.
     *
     * @return the discipline context
     */
    public String getDisciplineContext() {
        return disciplineContext;
    }

    /**
     * Sets the discipline context.
     *
     * @param disciplineContext
     *         the new discipline context
     */
    public void setDisciplineContext( String disciplineContext ) {
        this.disciplineContext = disciplineContext;
    }

}

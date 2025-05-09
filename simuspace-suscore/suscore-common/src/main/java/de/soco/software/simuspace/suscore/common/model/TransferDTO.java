package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A pojo class for mapping transfer object data to json and back to class.
 *
 * @author Huzaifah.Mubashir
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class TransferDTO {

    /**
     * The name.
     */
    @UIFormField( name = "name", title = "3000032x4", orderNum = 2 )
    @UIColumn( data = "name", name = "name", filter = "", renderer = "text", title = "3000032x4", orderNum = 2, isSortable = false, width = 0 )
    private String name;

    /**
     * The filePathandName.
     */
    @UIFormField( name = "filePathandName", title = "3000114x4", isAsk = false )
    @UIColumn( data = "filePathandName", name = "", filter = "text", renderer = "hidden", title = "3000114x4", type = "hidden", isSortable = false )
    private String filePathandName;

    /**
     * The action
     */
    @UIFormField( name = "action", title = "3000073x4", type = "hidden" )
    @UIColumn( data = "action", name = "action", filter = "", renderer = "hidden", title = "3000073x4", type = "hidden", isSortable = false )
    private String action;

    /**
     * Theprogress.
     */
    @UIFormField( name = "progress", title = "3000072x4", orderNum = 3, isAsk = false, type = "progress")
    @UIColumn( data = "progress", name = "type", filter = "", renderer = "progress", title = "3000072x4", orderNum = 3, isSortable = false )
    private ProgressBar progress;

    /**
     * Gets the file pathand name.
     *
     * @return the file pathand name
     */
    public String getFilePathandName() {
        return filePathandName;
    }

    /**
     * Sets the file pathand name.
     *
     * @param filePathandName
     *         the new file pathand name
     */
    public void setFilePathandName( String filePathandName ) {
        this.filePathandName = filePathandName;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the action.
     *
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the action.
     *
     * @param action
     *         the new action
     */
    public void setAction( String action ) {
        this.action = action;
    }

    /**
     * Gets the progress.
     *
     * @return the progress
     */
    public ProgressBar getProgress() {
        return progress;
    }

    /**
     * Sets the progress.
     *
     * @param progress
     *         the new progress
     */
    public void setProgress( ProgressBar progress ) {
        this.progress = progress;
    }

    /**
     * Instantiates a new transfer DTO.
     */
    public TransferDTO() {
        super();
    }

    /**
     * Instantiates a new transfer DTO.
     *
     * @param name
     *         the name
     * @param action
     *         the action
     * @param progress
     *         the progress
     */
    public TransferDTO( String name, String action, ProgressBar progress ) {
        super();
        this.name = name;
        this.action = action;
        this.progress = progress;
    }

    /**
     * Instantiates a new transfer DTO.
     *
     * @param name
     *         the name
     * @param filePathandName
     *         the file pathand name
     * @param action
     *         the action
     * @param progress
     *         the progress
     */
    public TransferDTO( String name, String action, ProgressBar progress, String filePathandName ) {
        super();
        this.name = name;
        this.filePathandName = filePathandName;
        this.action = action;
        this.progress = progress;
    }

}

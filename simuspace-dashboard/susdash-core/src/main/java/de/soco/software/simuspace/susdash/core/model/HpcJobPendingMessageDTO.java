package de.soco.software.simuspace.susdash.core.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.UIColumn;

/**
 * The Class HpcJobPendingMessageDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class HpcJobPendingMessageDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -3357737778601273216L;

    /**
     * The pending message.
     */
    @UIColumn( data = "pendingMessage", name = "PendingMessage", filter = "text", renderer = "text", title = "3000209x4", orderNum = 0 )
    private String pendingMessage;

    /**
     * Instantiates a new hpc job pending message DTO.
     */
    public HpcJobPendingMessageDTO() {
        super();
    }

    /**
     * Instantiates a new Hpc pending message dto.
     *
     * @param pendingMessage
     *         the pending message
     */
    public HpcJobPendingMessageDTO( String pendingMessage ) {
        super();
        this.pendingMessage = pendingMessage;
    }

    /**
     * Gets the pending message.
     *
     * @return the pendingMessage
     */
    public String getPendingMessage() {
        return pendingMessage;
    }

    /**
     * Sets the pending message.
     *
     * @param pendingMessage
     *         the pendingMessage to set
     */
    public void setPendingMessage( String pendingMessage ) {
        this.pendingMessage = pendingMessage;
    }

}
package de.soco.software.simuspace.workflow.dto;

/**
 * The Class EmailDTO.
 */
public class EmailDTO {

    /**
     * The from email.
     */
    private String fromEmail;

    /**
     * The password.
     */
    private String password;

    /**
     * The host.
     */
    private String host;

    /**
     * The port.
     */
    private String port;

    /**
     * The start tls enable.
     */
    private String startTlsEnable;

    /**
     * The ssl protocols.
     */
    private String sslProtocols;

    /**
     * The smtp auth.
     */
    private String smtpAuth;

    /**
     * The mail protocol.
     */
    private String mailProtocol;

    /**
     *
     */
    public EmailDTO() {
        super();
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getFromEmail() {
        return fromEmail;
    }

    /**
     * Sets the email.
     *
     * @param email
     *         the email to set
     */
    public void setFromEmail( String fromEmail ) {
        this.fromEmail = fromEmail;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password
     *         the password to set
     */
    public void setPassword( String password ) {
        this.password = password;
    }

    /**
     * Gets the host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host.
     *
     * @param host
     *         the host to set
     */
    public void setHost( String host ) {
        this.host = host;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the port.
     *
     * @param port
     *         the port to set
     */
    public void setPort( String port ) {
        this.port = port;
    }

    /**
     * Gets the start tls enable.
     *
     * @return the startTlsEnable
     */
    public String getStartTlsEnable() {
        return startTlsEnable;
    }

    /**
     * Sets the start tls enable.
     *
     * @param startTlsEnable
     *         the startTlsEnable to set
     */
    public void setStartTlsEnable( String startTlsEnable ) {
        this.startTlsEnable = startTlsEnable;
    }

    /**
     * Gets the ssl protocols.
     *
     * @return the sslProtocols
     */
    public String getSslProtocols() {
        return sslProtocols;
    }

    /**
     * Sets the ssl protocols.
     *
     * @param sslProtocols
     *         the sslProtocols to set
     */
    public void setSslProtocols( String sslProtocols ) {
        this.sslProtocols = sslProtocols;
    }

    /**
     * Gets the smtp auth.
     *
     * @return the smtpAuth
     */
    public String getSmtpAuth() {
        return smtpAuth;
    }

    /**
     * Sets the smtp auth.
     *
     * @param smtpAuth
     *         the smtpAuth to set
     */
    public void setSmtpAuth( String smtpAuth ) {
        this.smtpAuth = smtpAuth;
    }

    /**
     * Gets the mail protocol.
     *
     * @return the mailProtocol
     */
    public String getMailProtocol() {
        return mailProtocol;
    }

    /**
     * Sets the mail protocol.
     *
     * @param mailProtocol
     *         the mailProtocol to set
     */
    public void setMailProtocol( String mailProtocol ) {
        this.mailProtocol = mailProtocol;
    }

}

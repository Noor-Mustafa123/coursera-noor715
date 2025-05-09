package de.soco.software.simuspace.suscore.notification.service.rest;

/**
 * The Interface WSService.
 */
public interface WSService {

    /**
     * Post update.
     *
     * @param json
     *         the json
     *
     * @return the string
     */
    String postUpdate( String json );

    /**
     * Unmonitor client.
     *
     * @param keyToken
     *         the key token
     *
     * @return true, if successful
     */
    boolean unmonitorClient( String keyToken );

    /**
     * Active SSE.
     *
     * @return the int
     */
    int activeSSE();

}

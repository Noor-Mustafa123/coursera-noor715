package de.soco.software.simuspace.suscore.data.model;

import java.io.Serializable;

/**
 * Json Mapping Dto File For Movie
 *
 * @author Nosheen.Sharif
 */
public class MovieSources implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The webm.
     */
    private String webm;

    /**
     * The mp 4.
     */
    private String mp4;

    /**
     * @return the webm
     */
    public String getWebm() {
        return webm;
    }

    /**
     * @param webm
     *         the webm to set
     */
    public void setWebm( String webm ) {
        this.webm = webm;
    }

    /**
     * @return the mp4
     */
    public String getMp4() {
        return mp4;
    }

    /**
     * @param mp4
     *         the mp4 to set
     */
    public void setMp4( String mp4 ) {
        this.mp4 = mp4;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MovieSources [webm=" + webm + ", mp4=" + mp4 + "]";
    }

}

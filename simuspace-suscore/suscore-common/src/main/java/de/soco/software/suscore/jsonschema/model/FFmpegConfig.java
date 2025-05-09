package de.soco.software.suscore.jsonschema.model;

import java.util.ArrayList;

/**
 * The Class FFmpegConfig.
 *
 * @author Noman Arshad
 */
public class FFmpegConfig {

    /**
     * The ffmpeg.
     */
    private ArrayList< FfmpegExtension > ffmpeg;

    /**
     * Gets the ffmpeg.
     *
     * @return the ffmpeg
     */
    public ArrayList< FfmpegExtension > getFfmpeg() {
        return ffmpeg;
    }

    /**
     * Sets the ffmpeg.
     *
     * @param ffmpeg
     *         the new ffmpeg
     */
    public void setFfmpeg( ArrayList< FfmpegExtension > ffmpeg ) {
        this.ffmpeg = ffmpeg;
    }

}

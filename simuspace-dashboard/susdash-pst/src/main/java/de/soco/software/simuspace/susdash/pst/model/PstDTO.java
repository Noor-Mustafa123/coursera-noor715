package de.soco.software.simuspace.susdash.pst.model;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.soco.software.simuspace.susdash.pst.constants.PstConstants;

/**
 * The type Pst dto.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class PstDTO implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -1392875836717344168L;

    /**
     * The Status.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.STATUS )
    private String status;

    /**
     * The Id.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.ID )
    private String id;

    /**
     * The Verkn.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.VERKN )
    private String verkn;

    /**
     * The Motor nr.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.MOTOR_NR )
    private String motorNr;

    /**
     * The Motor type.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.MOTOR_TYPE )
    private String motorType;

    /**
     * The Motor bg.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.MOTOR_BG )
    private String motorBG;

    /**
     * The Program.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.PROGRAM )
    private String program;

    /**
     * The Total cycles.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.TOTAL_CYCLES )
    private String totalCycles;

    /**
     * The Current cycles.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.CURRENT_CYCLES )
    private String currentCycles;

    /**
     * The Progress.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.PROGRESS )
    private String progress;

    /**
     * The Pst ort.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.PST_ORT )
    private String pstOrt;

    /**
     * The Actual start date.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.ACTUAL_START_DATE )
    private String actualStartDate;

    /**
     * The Actual end date.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.ACTUAL_END_DATE )
    private String actualEndDate;

    /**
     * The Planned start date.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.PLANNED_START_DATE )
    private String plannedStartDate;

    /**
     * The Planned end date.
     */
    @JsonProperty( PstConstants.JSON_PROPERTIES.PLANNED_END_DATE )
    private String plannedEndDate;

    public String getMotorType() {
        return motorType;
    }

    /**
     * Gets planned end date.
     *
     * @return the planned end date
     */
    public String getPlannedEndDate() {
        return plannedEndDate;
    }

    /**
     * Gets planned start date.
     *
     * @return the planned start date
     */
    public String getPlannedStartDate() {
        return plannedStartDate;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status
     *         the status
     */
    public void setStatus( String status ) {
        this.status = status;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id
     *         the id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Gets pst ort.
     *
     * @return the pst ort
     */
    public String getPstOrt() {
        return pstOrt;
    }

    /**
     * Sets pst ort.
     *
     * @param pstOrt
     *         the pst ort
     */
    public void setPstOrt( String pstOrt ) {
        this.pstOrt = pstOrt;
    }

    /**
     * Gets actual start date.
     *
     * @return the actual start date
     */
    public String getActualStartDate() {
        return actualStartDate;
    }

    /**
     * Sets actual start date.
     *
     * @param actualStartDate
     *         the actual start date
     */
    public void setActualStartDate( String actualStartDate ) {
        this.actualStartDate = actualStartDate;
    }

    /**
     * Gets actual end date.
     *
     * @return the actual end date
     */
    public String getActualEndDate() {
        return actualEndDate;
    }

    /**
     * Sets actual end date.
     *
     * @param actualEndDate
     *         the actual end date
     */
    public void setActualEndDate( String actualEndDate ) {
        this.actualEndDate = actualEndDate;
    }

    /**
     * Sets planned start date.
     *
     * @param plannedStartDate
     *         the planned start date
     */
    public void setPlannedStartDate( String plannedStartDate ) {
        this.plannedStartDate = plannedStartDate;
    }

    /**
     * Sets planned end date.
     *
     * @param plannedEndDate
     *         the planned end date
     */
    public void setPlannedEndDate( String plannedEndDate ) {
        this.plannedEndDate = plannedEndDate;
    }

    /**
     * Gets verkn.
     *
     * @return the verkn
     */
    public String getVerkn() {
        return verkn;
    }

    /**
     * Sets verkn.
     *
     * @param verkn
     *         the verkn
     */
    public void setVerkn( String verkn ) {
        this.verkn = verkn;
    }

    /**
     * Gets motor nr.
     *
     * @return the motor nr
     */
    public String getMotorNr() {
        return motorNr;
    }

    /**
     * Sets motor nr.
     *
     * @param motorNr
     *         the motor nr
     */
    public void setMotorNr( String motorNr ) {
        this.motorNr = motorNr;
    }

    /**
     * Gets motor bg.
     *
     * @return the motor bg
     */
    public String getMotorBG() {
        return motorBG;
    }

    /**
     * Sets motor bg.
     *
     * @param motorBG
     *         the motor bg
     */
    public void setMotorBG( String motorBG ) {
        this.motorBG = motorBG;
    }

    /**
     * Gets program.
     *
     * @return the program
     */
    public String getProgram() {
        return program;
    }

    /**
     * Sets program.
     *
     * @param program
     *         the program
     */
    public void setProgram( String program ) {
        this.program = program;
    }

    /**
     * Gets total cycles.
     *
     * @return the total cycles
     */
    public String getTotalCycles() {
        return totalCycles;
    }

    /**
     * Sets total cycles.
     *
     * @param totalCycles
     *         the total cycles
     */
    public void setTotalCycles( String totalCycles ) {
        this.totalCycles = totalCycles;
    }

    /**
     * Gets current cycles.
     *
     * @return the current cycles
     */
    public String getCurrentCycles() {
        return currentCycles;
    }

    /**
     * Sets current cycles.
     *
     * @param currentCycles
     *         the current cycles
     */
    public void setCurrentCycles( String currentCycles ) {
        this.currentCycles = currentCycles;
    }

    /**
     * Gets progress.
     *
     * @return the progress
     */
    public String getProgress() {
        return progress;
    }

    /**
     * Sets progress.
     *
     * @param progress
     *         the progress
     */
    public void setProgress( String progress ) {
        this.progress = progress;
    }

    public void setMotorType( String motorType ) {
        this.motorType = motorType;
    }

}

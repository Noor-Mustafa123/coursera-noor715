package de.soco.software.simuspace.wizards.model.QADyna;

import de.soco.software.simuspace.suscore.common.model.UIColumn;

/**
 * The type Dyna combination dto.
 *
 * @author Ali Haider
 */
public class QADynaCombinationDTO {

    /**
     * The Run.
     */
    @UIColumn( data = "run", name = "Run", filter = "text", renderer = "checkbox", title = "4100058x4", orderNum = 0 )
    private int run = 1;

    /**
     * The Name.
     */
    @UIColumn( data = "inputDeck.name", name = "inputDeck", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1 )
    private QADynaCombinationDeckDTO inputDeck;

    /**
     * The Cpu.
     */
    @UIColumn( data = "cpu", name = "cpu", filter = "text", renderer = "text", title = "4100147x4", orderNum = 2 )
    private String cpu;

    /**
     * The Ez.
     */
    @UIColumn( data = "ez", name = "ez", filter = "text", renderer = "text", title = "4100148x4", orderNum = 3 )
    private String ez;

    /**
     * The Dyna version.
     */
    @UIColumn( data = "dynaVersion.name", name = "dynaVersion", filter = "text", renderer = "text", title = "4100149x4", orderNum = 4 )
    private QADynaCombinationObjectDTO dynaVersion;

    /**
     * The Master control.
     */
    @UIColumn( data = "mcfile.name", name = "mcfile", filter = "text", renderer = "text", title = "4100150x4", orderNum = 5 )
    private QADynaCombinationObjectDTO mcfile;

    /**
     * The Mat db.
     */
    @UIColumn( data = "matdb.name", name = "matdb", filter = "text", renderer = "text", title = "4100151x4", orderNum = 6 )
    private QADynaCombinationObjectDTO matdb;

    /**
     * The Mat dbqs.
     */
    @UIColumn( data = "matdbqs.name", name = "matdbqs", filter = "text", renderer = "text", title = "4100152x4", orderNum = 7 )
    private QADynaCombinationObjectDTO matdbqs;

    /**
     * Gets run.
     *
     * @return the run
     */
    public int getRun() {
        return run;
    }

    /**
     * Sets run.
     *
     * @param run
     *         the run
     */
    public void setRun( int run ) {
        this.run = run;
    }

    /**
     * Gets input deck.
     *
     * @return the input deck
     */
    public QADynaCombinationDeckDTO getInputDeck() {
        return inputDeck;
    }

    /**
     * Sets input deck.
     *
     * @param inputDeck
     *         the input deck
     */
    public void setInputDeck( QADynaCombinationDeckDTO inputDeck ) {
        this.inputDeck = inputDeck;
    }

    /**
     * Gets cpu.
     *
     * @return the cpu
     */
    public String getCpu() {
        return cpu;
    }

    /**
     * Sets cpu.
     *
     * @param cpu
     *         the cpu
     */
    public void setCpu( String cpu ) {
        this.cpu = cpu;
    }

    /**
     * Gets ez.
     *
     * @return the ez
     */
    public String getEz() {
        return ez;
    }

    /**
     * Sets ez.
     *
     * @param ez
     *         the ez
     */
    public void setEz( String ez ) {
        this.ez = ez;
    }

    /**
     * Gets dyna version.
     *
     * @return the dyna version
     */
    public QADynaCombinationObjectDTO getDynaVersion() {
        return dynaVersion;
    }

    /**
     * Sets dyna version.
     *
     * @param dynaVersion
     *         the dyna version
     */
    public void setDynaVersion( QADynaCombinationObjectDTO dynaVersion ) {
        this.dynaVersion = dynaVersion;
    }

    /**
     * Gets master control.
     *
     * @return the master control
     */
    public QADynaCombinationObjectDTO getMcfile() {
        return mcfile;
    }

    /**
     * Sets master control.
     *
     * @param mcfile
     *         the master control
     */
    public void setMcfile( QADynaCombinationObjectDTO mcfile ) {
        this.mcfile = mcfile;
    }

    /**
     * Gets matdb.
     *
     * @return the matdb
     */
    public QADynaCombinationObjectDTO getMatdb() {
        return matdb;
    }

    /**
     * Sets matdb.
     *
     * @param matdb
     *         the matdb
     */
    public void setMatdb( QADynaCombinationObjectDTO matdb ) {
        this.matdb = matdb;
    }

    /**
     * Gets matdbqs.
     *
     * @return the matdbqs
     */
    public QADynaCombinationObjectDTO getMatdbqs() {
        return matdbqs;
    }

    /**
     * Sets matdbqs.
     *
     * @param matdbqs
     *         the matdbqs
     */
    public void setMatdbqs( QADynaCombinationObjectDTO matdbqs ) {
        this.matdbqs = matdbqs;
    }

}

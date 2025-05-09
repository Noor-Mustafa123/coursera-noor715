package de.soco.software.simuspace.wizards.model.QADyna;

import java.util.List;

import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchInputDeckDTO;

/**
 * The type Qa dyna base form.
 */
public class QADynaBaseForm {

    /**
     * The Selection id.
     */
    private String selectionId;

    /**
     * The Inputdeck.
     */
    private List< BmwCaeBenchInputDeckDTO > inputDeck;

    /**
     * Instantiates a new Qa dyna base form.
     */
    public QADynaBaseForm() {

    }

    /**
     * Gets inputdeck.
     *
     * @return the inputdeck
     */
    public List< BmwCaeBenchInputDeckDTO > getInputDeck() {
        return inputDeck;
    }

    /**
     * Sets inputdeck.
     *
     * @param inputDeck
     *         the inputdeck
     */
    public void setInputDeck( List< BmwCaeBenchInputDeckDTO > inputDeck ) {
        this.inputDeck = inputDeck;
    }

    /**
     * Gets selection id.
     *
     * @return the selection id
     */
    public String getSelectionId() {
        return selectionId;
    }

    /**
     * Sets selection id.
     *
     * @param selectionId
     *         the selection id
     */
    public void setSelectionId( String selectionId ) {
        this.selectionId = selectionId;
    }

}

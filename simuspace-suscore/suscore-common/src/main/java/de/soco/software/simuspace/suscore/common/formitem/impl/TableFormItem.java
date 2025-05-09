package de.soco.software.simuspace.suscore.common.formitem.impl;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.model.BindTo;
import de.soco.software.suscore.jsonschema.model.BindFrom;

/**
 * To Make Selections on Tables of UI Forms.
 *
 * @author Noman Arshad
 */
@Getter
@Setter
public class TableFormItem extends UIFormItem {

    /**
     * The multiple.
     */
    private boolean multiple;

    /**
     * The connected.
     */
    private String connected;

    /**
     * The Connected table label.
     */
    private String connectedTableLabel;

    /**
     * The row reorder.
     */
    private boolean rowReorder;

    /**
     * The bind from.
     */
    private BindFrom bindFrom;

    /**
     * The bind to.
     */
    private BindTo bindTo;

    /**
     * The trigger modified on init.
     */
    private boolean triggerModifiedOnInit;

    /**
     * The Button label.
     */
    private String buttonLabel;

    /**
     * Instantiates a new select UI.
     */
    TableFormItem() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMultiple() {
        return multiple;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMultiple( boolean multiple ) {
        this.multiple = multiple;
    }

    /**
     * Sets the bind from.
     *
     * @param bindUrl
     *         the new bind from
     */
    public void setBindFrom( String bindUrl ) {
        this.bindFrom = new BindFrom( bindUrl );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( multiple ? 1231 : 1237 );
        result = prime * result + ( ( connected == null ) ? 0 : connected.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( !super.equals( obj ) ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        TableFormItem other = ( TableFormItem ) obj;
        return ( multiple != other.multiple );
    }
}

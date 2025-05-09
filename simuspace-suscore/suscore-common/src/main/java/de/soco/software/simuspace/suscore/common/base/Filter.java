package de.soco.software.simuspace.suscore.common.base;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The Class Filter which is mapped to filters json to filter table records
 *
 * @author Zeeshan jamal
 */
public class Filter implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The operator.
     */
    private String operator;

    /**
     * The value.
     */
    private String value = "";

    /**
     * The from.
     */
    private Date from;

    /**
     * The to.
     */
    private Date to;

    /**
     * The condition.
     */
    private String condition;// AND, OR

    /**
     * The selection.
     */
    private List< String > selection;

    /**
     * Instantiates a new filter.
     */
    public Filter() {
        // default constructor
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the value to set
     */
    public void setValue( String value ) {
        this.value = value;
    }

    /**
     * Gets the from.
     *
     * @return the from
     */
    public Date getFrom() {
        return from;
    }

    /**
     * Sets the from.
     *
     * @param fromTime
     *         the new from
     */
    public void setFrom( Date fromTime ) {
        this.from = fromTime;
    }

    /**
     * Gets the to.
     *
     * @return the to
     */
    public Date getTo() {
        return to;
    }

    /**
     * Sets the to.
     *
     * @param toTime
     *         the new to
     */
    public void setTo( Date toTime ) {
        this.to = toTime;
    }

    /**
     * Gets the selection.
     *
     * @return the selection
     */
    public List< String > getSelection() {
        return selection;
    }

    /**
     * Sets the selection.
     *
     * @param selection
     *         the new selection
     */
    public void setSelection( List< String > selection ) {
        this.selection = selection;
    }

    /**
     * Gets the operator.
     *
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Sets the operator.
     *
     * @param operator
     *         the new operator
     */
    public void setOperator( String operator ) {
        this.operator = operator;
    }

    /**
     * Gets the condition.
     *
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Sets the condition.
     *
     * @param condition
     *         the new condition
     */
    public void setCondition( String condition ) {
        this.condition = condition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( condition == null ) ? 0 : condition.hashCode() );
        result = prime * result + ( ( from == null ) ? 0 : from.hashCode() );
        result = prime * result + ( ( operator == null ) ? 0 : operator.hashCode() );
        result = prime * result + ( ( selection == null ) ? 0 : selection.hashCode() );
        result = prime * result + ( ( to == null ) ? 0 : to.hashCode() );
        result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
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
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        Filter other = ( Filter ) obj;
        if ( condition == null ) {
            if ( other.condition != null ) {
                return false;
            }
        } else if ( !condition.equals( other.condition ) ) {
            return false;
        }
        if ( from == null ) {
            if ( other.from != null ) {
                return false;
            }
        } else if ( !from.equals( other.from ) ) {
            return false;
        }
        if ( operator == null ) {
            if ( other.operator != null ) {
                return false;
            }
        } else if ( !operator.equals( other.operator ) ) {
            return false;
        }
        if ( selection == null ) {
            if ( other.selection != null ) {
                return false;
            }
        } else if ( !selection.equals( other.selection ) ) {
            return false;
        }
        if ( to == null ) {
            if ( other.to != null ) {
                return false;
            }
        } else if ( !to.equals( other.to ) ) {
            return false;
        }
        if ( value == null ) {
            if ( other.value != null ) {
                return false;
            }
        } else if ( !value.equals( other.value ) ) {
            return false;
        }
        return true;
    }

}

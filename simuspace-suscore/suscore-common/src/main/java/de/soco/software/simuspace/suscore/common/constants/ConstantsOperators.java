package de.soco.software.simuspace.suscore.common.constants;

import lombok.Getter;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;

/**
 * A class that would hold the operators which are sent by front end in filters {@link FiltersDTO}
 *
 * @author Zeeshan Jamal
 */
@Getter
public enum ConstantsOperators {

    /**
     * The equals.
     */
    EQUALS( "Equals", "='$'", "Gleich" ),
    /**
     * The not equals.
     */
    NOT_EQUALS( "Does Not Equal", "!='$'", "Nicht Gleich" ),
    /**
     * The is null.
     */
    IS_NULL( "Is Null", "IS NULL", "Ist Null" ),
    /**
     * The is not null.
     */
    IS_NOT_NULL( "Is Not Null", "IS NOT NULL", "Ist Nicht Null" ),
    /**
     * The is in.
     */
    IS_IN( "Contains", "like('%$%')", "Enthält" ),
    /**
     * The is not in.
     */
    IS_NOT_IN( "Does Not Contain", "not like('%$%')", "Beinhaltet nicht" ),
    /**
     * The begins with.
     */
    BEGINS_WITH( "Begins With", "like('$%')", "Beginnt mit" ),
    /**
     * The ends with.
     */
    ENDS_WITH( "Ends With", "like('%$')", "Endet mit" ),
    /**
     * The and.
     */
    AND( "And", "AND", "Und" ),
    /**
     * The or.
     */
    OR( "Or", "OR", "Oder" ),
    /**
     * The order by.
     */
    ORDER_BY( "Order By", "ORDER BY", "Sortieren Nach" ),
    /**
     * The asc.
     */
    ASC( "Asc", "ASC", "Aufsteigend" ),
    /**
     * The desc.
     */
    DESC( "Desc", "DESC", "Absteigend" ),
    /**
     * The where.
     */
    WHERE( "Where", "WHERE", "Wo" ),
    /**
     * The from.
     */
    FROM( "From", "FROM", "Von" ),
    /**
     * The selection.
     */
    SELECTION( "Contains", "IN ($)", "Enthält" ),
    /**
     * The before.
     */
    BEFORE( "Before", "<'$'", "Vor" ),
    /**
     * The after.
     */
    AFTER( "After", ">'$'", "Nach" ),
    /**
     * The after or equal to.
     */
    AFTER_OR_EQUAL_TO( "After Or Equal To", ">='$'", "Nach oder gleich" ),
    /**
     * The before or equal to.
     */
    BEFORE_OR_EQUAL_TO( "Before Or Equal To", "<='$'", "Vor oder gleich" );

    /**
     * The Name.
     */
    final String name;

    /**
     * The Operator.
     */
    final String operatorTemplate;

    /**
     * The De name.
     */
    final String deName;

    /**
     * Instantiates a new operators.
     *
     * @param name
     *         the name
     * @param operatorTemplate
     *         the operatorTemplate
     * @param deName
     *         the German translation of the name
     */
    ConstantsOperators( String name, String operatorTemplate, String deName ) {
        this.name = name;
        this.operatorTemplate = operatorTemplate.trim();
        this.deName = deName;
    }
}

package de.soco.software.simuspace.suscore.common.enums;

import java.util.Arrays;

import lombok.Getter;

/**
 * The enum Selection origins.
 */
@Getter
public enum SelectionOrigins {

    /**
     * Context selection origins.
     */
    CONTEXT( "context" ),

    /**
     * The Regex selection.
     */
    REGEX_SELECTION( "Regex selection" ),

    /**
     * The Template selection.
     */
    TEMPLATE_SELECTION( "Template selection" ),

    /**
     * Qa dyna selection origins.
     */
    QA_DYNA( "qaDyna" ),

    /**
     * Permission selection origins.
     */
    PERMISSION( "permissions" ),

    /**
     * The Run dummy.
     */
    RUN_DUMMY( "Run Dummy" ),

    /**
     * User selection origins.
     */
    USER( "user" ),

    /**
     * Group selection origins.
     */
    GROUP( "group" ),

    /**
     * The Report section.
     */
    REPORT_SECTION( "Objects selection for section" ),

    /**
     * The Workflow location.
     */
    WORKFLOW_LOCATION( "Workflow Location" ),

    /**
     * The Variant copy.
     */
    VARIANT_COPY( "Variant Copy" ),

    /**
     * Ssfe selection origins.
     */
    SSFE( "ssfe" ),

    /**
     * Unknown selection origins.
     */
    UNKNOWN( "unknown" );

    /**
     * Instantiates a new Selection origins.
     *
     * @param origin
     *         the origin
     */
    SelectionOrigins( String origin ) {
        this.origin = origin;
    }

    /**
     * The Origin.
     */
    private final String origin;

    /**
     * Gets by origin.
     *
     * @param origin
     *         the origin
     *
     * @return the by origin
     */
    public static SelectionOrigins getByOrigin( String origin ) {
        return Arrays.stream( SelectionOrigins.values() ).filter( enumItem -> enumItem.getOrigin().equals( origin ) ).findFirst()
                .orElse( UNKNOWN );
    }

}

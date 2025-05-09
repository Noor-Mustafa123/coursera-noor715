package de.soco.software.simuspace.suscore.common.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface UI FieldForm for showing/rendering Forms at front end side.
 *
 * @author Noman Arshad
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD ) // can use in method only.
public @interface UIFormField {

    /**
     * Data.
     *
     * @return the string
     */
    String name();

    /**
     * Title.
     *
     * @return the string
     */
    String title();

    /**
     * Type class.
     *
     * @return the string[]
     */
    String[] typeClass() default {};

    /**
     * Type.
     *
     * @return the string
     */
    String type() default "text";

    /**
     * Checks if is ask.
     *
     * @return true, if is ask
     */
    boolean isAsk() default true;

    /**
     * Readonly.
     *
     * @return the boolean
     */
    boolean readonly() default false;

    /**
     * Multiple.
     *
     * @return true, if successful
     */
    boolean multiple() default false;

    /**
     * Selectable.
     *
     * @return the string
     */
    String selectable() default "status";

    /**
     * Duplicate.
     *
     * @return true, if successful
     */
    boolean duplicate() default false;

    /**
     * OrderNum.
     *
     * @return the string
     */
    int orderNum() default 0;

    /**
     * Accepted files.
     *
     * @return the string
     */
    String acceptedFiles() default "";

    /**
     * Max files.
     *
     * @return the int
     */
    int maxFiles() default 1;

    /**
     * Section.
     *
     * @return the string
     */
    String section() default "default";

    /**
     * Show boolean.
     *
     * @return the boolean
     */
    boolean show() default true;

    /**
     * Filter options string [ ].
     *
     * @return the string [ ]
     */
    String[] filterOptions() default {};

    /**
     * Required boolean.
     *
     * @return the boolean
     */
    boolean required() default false;

}
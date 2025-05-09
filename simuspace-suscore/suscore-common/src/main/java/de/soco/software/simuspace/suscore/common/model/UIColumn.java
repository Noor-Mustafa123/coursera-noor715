package de.soco.software.simuspace.suscore.common.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;

/**
 * The interface UI Column for showing/rendering columns at front end side.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD ) // can use in method only.
public @interface UIColumn {

    /**
     * Data.
     *
     * @return the string
     */
    String data();

    /**
     * Title.
     *
     * @return the string
     */
    String title();

    /**
     * Filter.
     *
     * @return the string
     */
    String filter();

    /**
     * Type.
     *
     * @return the string
     */
    String type() default "text";

    /**
     * Options.
     *
     * @return the string
     */
    String options() default "options";

    /**
     * Name.
     *
     * @return the string
     */
    String name();

    /**
     * Checks if is show.
     *
     * @return true, if is show
     */
    boolean isShow() default true;

    /**
     * Checks if is sortable.
     *
     * @return true, if is sortable
     */
    boolean isSortable() default true;

    /**
     * Renderer.
     *
     * @return the string
     */
    String renderer();

    /**
     * Manage.
     *
     * @return the string
     */
    String manage() default "true";

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
     * Tooltip.
     *
     * @return the string
     */
    String tooltip() default "";

    /**
     * Url.
     *
     * @return the string
     */
    String url() default "";

    /**
     * Filter options string [ ].
     *
     * @return the string [ ]
     */
    String[] filterOptions() default {};

    /**
     * Truncate at int.
     *
     * @return the int
     */
    int width() default ConstantsInteger.DEFAULT_COLUMN_WIDTH;

}
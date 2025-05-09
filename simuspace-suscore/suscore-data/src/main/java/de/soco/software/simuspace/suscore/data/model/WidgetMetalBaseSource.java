package de.soco.software.simuspace.suscore.data.model;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.METAL_BASE_FIELDS;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The type Widget material inspector source.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = true )
public class WidgetMetalBaseSource extends WidgetOtherSource {

    /**
     * The Mat db source.
     */
    @UIFormField( name = METAL_BASE_FIELDS.MAT_DB_SOURCE, title = "3000268x4", type = "select", required = true, orderNum = 11 )
    private String matDbSource;

    /**
     * The Mat db schema.
     */
    @UIFormField( name = METAL_BASE_FIELDS.MAT_DB_SCHEMA, title = "3000269x4", type = "select", required = true, orderNum = 12 )
    private String matDbSchema;

    /**
     * The Gs material name.
     */
    @UIFormField( name = METAL_BASE_FIELDS.GS_MATERIAL_NAME, title = "3000264x4", type = "select", section = "Statistical Material Analysis", required = true, orderNum = 20 )
    private String gs_material_name;

    /**
     * The Surface finish.
     */
    @UIFormField( name = METAL_BASE_FIELDS.SURFACE_FINISH, title = "3000285x4", type = "select", section = "Statistical Material Analysis", orderNum = 21, multiple = true )
    private List< String > surface_finish;

    /**
     * The Supplier name.
     */
    @UIFormField( name = METAL_BASE_FIELDS.SUPPLIER_NAME, title = "3000288x4", type = "select", section = "Statistical Material Analysis", orderNum = 22, multiple = true )
    private List< String > supplier_name;

    /**
     * The Angle.
     */
    @UIFormField( name = METAL_BASE_FIELDS.ANGLE, title = "3000272x4", type = "select", section = "Statistical Material Analysis", orderNum = 23, required = true )
    private String angle;

    /**
     * The Test created between.
     */
    @UIFormField( name = METAL_BASE_FIELDS.TEST_CREATED_BETWEEN, title = "3000363x4", type = "dateRange", section = "Statistical Material Analysis", orderNum = 23 )
    private DateRange testCreatedBetween;

    /**
     * The Min thickness.
     */
    @UIFormField( name = METAL_BASE_FIELDS.MIN_THICKNESS, title = "3000364x4", type = "integer", section = "Statistical Material Analysis", orderNum = 24 )
    private String minThickness;

    /**
     * The Max thickness.
     */
    @UIFormField( name = METAL_BASE_FIELDS.MAX_THICKNESS, title = "3000365x4", type = "integer", section = "Statistical Material Analysis", orderNum = 25 )
    private String maxThickness;

    /**
     * The Status.
     */
    @UIFormField( name = METAL_BASE_FIELDS.IO_STATUS, title = "3000370x4", type = "boolean", section = "Statistical Material Analysis", orderNum = 26 )
    private String ioStatus;

    /**
     * The Flow curves.
     */
    @UIFormField( name = METAL_BASE_FIELDS.FLOW_CURVE, title = "3000366x4", type = "boolean", section = "Statistical Material Analysis", orderNum = 27 )
    private String flowCurves;

    /**
     * The Bulge.
     */
    @UIFormField( name = METAL_BASE_FIELDS.BULGE, title = "3000367x4", type = "boolean", section = "Statistical Material Analysis", orderNum = 28 )
    private String bulge;

    /**
     * The Show only in review.
     */
    @UIFormField( name = METAL_BASE_FIELDS.SHOW_ONLY_IN_REVIEW, title = "3000371x4", type = "boolean", section = "Statistical Material Analysis", orderNum = 29 )
    private String showOnlyInReview;

    /**
     * The Quantile limit.
     */
    @UIFormField( name = METAL_BASE_FIELDS.QUANTILE_LIMIT, title = "3000368x4", type = "float", section = "Statistical Material Analysis", orderNum = 30 )
    private String quantileLimit;

    /**
     * The Order nr.
     */
    @UIFormField( name = METAL_BASE_FIELDS.ORDER_NR, title = "3000266x4", type = "select", section = "Statistical Material Analysis", required = true, orderNum = 31, multiple = true )
    private List< String > order_nr;

    /**
     * The Order nr.
     */
    @UIFormField( name = METAL_BASE_FIELDS.ALL_TEST_IDS, title = "3000391x4", type = "hidden", section = "Statistical Material Analysis", orderNum = 31, multiple = true )
    private List< String > allTestIds;

    /**
     * The Indicator.
     */
    @UIFormField( name = METAL_BASE_FIELDS.CHARACTERISTICS, title = "3000372x4", type = "select", section = "Statistical Material Analysis", required = true, orderNum = 32, multiple = true )
    private List< String > characteristics;

    /**
     * The Lab data plot data.
     */
    @UIFormField( name = METAL_BASE_FIELDS.PLOT_DATA, title = "3000290x4", type = "button", section = "Statistical Material Analysis", orderNum = 51 )
    private String plotData;

}

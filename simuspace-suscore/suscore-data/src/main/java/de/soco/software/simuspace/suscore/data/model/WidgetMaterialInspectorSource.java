package de.soco.software.simuspace.suscore.data.model;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.MATERIAL_INSPECTOR_FIELDS;

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
public class WidgetMaterialInspectorSource extends WidgetOtherSource {

    /**
     * The Mat db source.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.MAT_DB_SOURCE, title = "3000268x4", type = "select", required = true, orderNum = 11 )
    private String matDbSource;

    /**
     * The Mat db schema.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.MAT_DB_SCHEMA, title = "3000269x4", type = "select", required = true, orderNum = 12 )
    private String matDbSchema;

    /**
     * The Gs material name.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.GS_MATERIAL_NAME, title = "3000264x4", type = "select", section = "Read Lab Data", required = true, orderNum = 23 )
    private String gs_material_name;

    /**
     * The Thickness.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.THICKNESS, title = "3000265x4", type = "select", section = "Read Lab Data", required = true, orderNum = 24 )
    private List< String > thickness;

    /**
     * The Surface finish.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.SURFACE_FINISH, title = "3000285x4", type = "select", section = "Read Lab Data", orderNum = 25 )
    private List< String > surface_finish;

    /**
     * The Charge.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.CHARGE, title = "3000286x4", type = "select", section = "Read Lab Data", orderNum = 26 )
    private List< String > charge;

    /**
     * The Laboratory name.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.LABORATORY_NAME, title = "3000287x4", type = "select", section = "Read Lab Data", orderNum = 27 )
    private List< String > laboratory_name;

    /**
     * The Supplier name.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.SUPPLIER_NAME, title = "3000288x4", type = "select", section = "Read Lab Data", orderNum = 28 )
    private List< String > supplier_name;

    /**
     * The Status.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.STATUS, title = "3000283x4", type = "select", section = "Read Lab Data", orderNum = 29 )
    private List< String > status;

    /**
     * The Order nr.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.ORDER_NR, title = "3000266x4", type = "select", section = "Read Lab Data", required = true, orderNum = 30 )
    private String order_nr;

    /**
     * The Probe.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.PROBE, title = "3000314x4", type = "select", section = "Read Lab Data", required = false, orderNum = 31 )
    private String probe;

    /**
     * The Investigated strain state.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.INVESTIGATED_STRAIN_STATE, type = "float", title = "3000291x4", section = "Read Lab Data", required = false, orderNum = 32 )
    private Double investigatedStrainState;

    /**
     * The Lab data plot data.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.LAB_DATA_PLOT_DATA, title = "3000290x4", type = "button", section = "Read Lab Data", required = false, orderNum = 33 )
    private String labDataPlotData;

    /**
     * The Ft 0.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.FT0, title = "3000292x4", type = "float", section = "Optimization Yield Locus", required = false, orderNum = 40 )
    private Double ft0;

    /**
     * The Ft 45.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.FT45, title = "3000293x4", type = "float", section = "Optimization Yield Locus", required = false, orderNum = 41 )
    private Double ft45;

    /**
     * The Ft 90.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.FT90, title = "3000294x4", type = "float", section = "Optimization Yield Locus", required = false, orderNum = 42 )
    private Double ft90;

    /**
     * The Fb.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.FB, title = "3000295x4", type = "float", section = "Optimization Yield Locus", required = false, orderNum = 43 )
    private Double fb;

    /**
     * The F tau.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.F_TAU, title = "3000296x4", type = "float", section = "Optimization Yield Locus", required = false, orderNum = 44 )
    private Double f_tau;

    /**
     * The Exp.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.EXP, title = "3000297x4", type = "integer", section = "Optimization Yield Locus", required = false, orderNum = 45 )
    private Integer exp;

    /**
     * The Weight s.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.WEIGHT_S, title = "3000298x4", type = "integer", section = "Optimization Yield Locus", required = false, orderNum = 46 )
    private Integer weightS;

    /**
     * The Weight r.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.WEIGHT_R, title = "3000299x4", type = "integer", section = "Optimization Yield Locus", required = false, orderNum = 47 )
    private Integer weightR;

    /**
     * The Cal yield locus b 89.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.CAL_YIELD_LOCUS_B89, title = "3000300x4", type = "button", section = "Optimization Yield Locus", required = false, orderNum = 48 )
    private String calYieldLocusB89;

    /**
     * The Cal yield locus b 2000.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.CAL_YIELD_LOCUS_B2000, title = "3000301x4", type = "button", section = "Optimization Yield Locus", required = false, orderNum = 49 )
    private String calYieldLocusB2000;

    /**
     * The Define yield locus.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.DEFINE_YIELD_LOCUS, title = "3000302x4", type = "button", section = "Optimization Yield Locus", required = false, orderNum = 50 )
    private String defineYieldLocus;

    /**
     * The Gen yield locus.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.GEN_YIELD_LOCUS, title = "3000303x4", type = "button", section = "Optimization Yield Locus", required = false, orderNum = 51 )
    private String genYieldLocus;

    /**
     * The Weight n.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.WEIGHT_N, title = "3000304x4", type = "float", section = "Optimization Flow Curve", required = false, orderNum = 60 )
    private Double weightN;

    /**
     * The Discretization.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.DISCRETIZATION, title = "3000305x4", type = "float", section = "Optimization Flow Curve", required = false, orderNum = 61 )
    private Double discretization;

    /**
     * The Generations.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.GENERATIONS, title = "3000306x4", type = "float", section = "Optimization Flow Curve", required = false, orderNum = 62 )
    private Double generations;

    /**
     * The Interval n.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.INTERVAL_N, title = "3000307x4", type = "float", section = "Optimization Flow Curve", required = false, orderNum = 63 )
    private Double intervalN;

    /**
     * The Cpus.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.CPUS, title = "3000308x4", type = "select", section = "Optimization Flow Curve", required = false, orderNum = 64 )
    private String cpus;

    /**
     * The Pop size.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.POP_SIZE, title = "3000309x4", type = "float", section = "Optimization Flow Curve", required = false, orderNum = 65 )
    private Double popSize;

    /**
     * The Initial value fc optimization.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.INITIAL_VALUE_FC_OPTIMIZATION, title = "3000310x4", type = "float", section = "Optimization Flow Curve", required = false, orderNum = 66 )
    private Double initialValueFCOptimization;

    /**
     * The Opt flow curve models.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.OPT_FLOW_CURVE_MODELS, title = "3000311x4", type = "button", section = "Optimization Flow Curve", required = false, orderNum = 67 )
    private String optFlowCurveModels;

    /**
     * The Load opt results.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.LOAD_OPT_RESULTS, title = "3000312x4", type = "button", section = "Optimization Flow Curve", required = false, orderNum = 68 )
    private String loadOptResults;

    /**
     * The Export flow curve data.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.EXPORT_FLOW_CURVE_DATA, title = "3000313x4", type = "button", section = "Optimization Flow Curve", required = false, orderNum = 69 )
    private String exportFlowCurveData;

    /**
     * The Flow curve plot data.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.FLOW_CURVE_PLOT_DATA, title = "3000290x4", type = "button", section = "Optimization Flow Curve", required = false, orderNum = 70 )
    private String flowCurvePlotData;

    /**
     * The Material card name.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.MATERIAL_CARD_NAME, title = "3000317x4", section = "Export Results", required = false, orderNum = 80 )
    private String materialCardName;

    /**
     * The Export measurement data.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.EXPORT_MEASUREMENT_DATA, title = "3000318x4", type = "button", section = "Export Results", required = false, orderNum = 81 )
    private String exportMeasurementData;

    /**
     * The Export yield locus.
     */
    @UIFormField( name = MATERIAL_INSPECTOR_FIELDS.EXPORT_YIELD_LOCUS, title = "3000319x4", type = "button", section = "Export Results", required = false, orderNum = 82 )
    private String exportYieldLocus;

}

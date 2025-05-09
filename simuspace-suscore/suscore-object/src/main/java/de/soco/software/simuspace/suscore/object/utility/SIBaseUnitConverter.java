package de.soco.software.simuspace.suscore.object.utility;

import javax.measure.converter.RationalConverter;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import java.math.BigInteger;
import java.util.List;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.suscore.jsonschema.model.UnitsFamily;
import de.soco.software.suscore.jsonschema.model.UnitsList;

/**
 * The Class SIBaseUnitConverter for the conversion of quantity types to SI units.
 *
 * @author Noman Arshad , Zeeshan jamal
 */
public class SIBaseUnitConverter {

    /**
     * The Constant DIMENSIONLESS_QUANTITY.
     */
    private static final String DIMENSIONLESS_QUANTITY = "Dimensionless";

    /**
     * Instantiates a new SI base unit converter.
     */
    private SIBaseUnitConverter() {

    }

    /**
     * Convert.
     *
     * @param quantity
     *         the quantity
     * @param fromUnit
     *         the from unit
     * @param toUnit
     *         the to unit
     * @param value
     *         the value
     *
     * @return the double
     */
    public static double convert( String quantity, String fromUnit, String toUnit, double value ) {
        double fromUnitFactor = ConstantsInteger.INTEGER_VALUE_ZERO;
        double toUnitFactor = fromUnitFactor;

        /*
         * no longer need for this code since it gives wrong conversion if (
         * quantity.equalsIgnoreCase( LENGTH_QUANTITY ) ) { fromUnitFactor =
         * convertToLenghBaseScalingFactor( fromUnit ); toUnitFactor =
         * convertToLenghBaseScalingFactor( toUnit ); return applyArithmeticOperation(
         * value, fromUnitFactor, toUnitFactor ); } else if ( quantity.equalsIgnoreCase(
         * TIME_QUANTITY ) ) { fromUnitFactor = convertToTimeBaseScalingFactor( fromUnit
         * ); toUnitFactor = convertToTimeBaseScalingFactor( toUnit ); return
         * applyArithmeticOperation( value, fromUnitFactor, toUnitFactor ); } else if (
         * quantity.equalsIgnoreCase( MASS_QUANTITY ) ) { fromUnitFactor =
         * convertToMassBaseScalingFactor( fromUnit ); toUnitFactor =
         * convertToMassBaseScalingFactor( toUnit ); return applyArithmeticOperation(
         * value, fromUnitFactor, toUnitFactor ); } else if ( quantity.equalsIgnoreCase(
         * SPEED_QUANTITY ) ) { fromUnitFactor = convertToSpeedBaseScalingFactor(
         * fromUnit ); toUnitFactor = convertToSpeedBaseScalingFactor( toUnit ); return
         * applyArithmeticOperation( value, fromUnitFactor, toUnitFactor ); } else if (
         * quantity.equalsIgnoreCase( TEMPERATURE_QUANTITY ) ) { return
         * convertToTemperatureBaseScalingFactor( fromUnit, toUnit, value ); } else if (
         * quantity.equalsIgnoreCase( FORCE_QUANTITY ) ) { fromUnitFactor =
         * convertToForceBaseScalingFactor( fromUnit ); toUnitFactor =
         * convertToForceBaseScalingFactor( toUnit ); return applyArithmeticOperation(
         * value, fromUnitFactor, toUnitFactor ); } else
         */

        if ( quantity.equalsIgnoreCase( DIMENSIONLESS_QUANTITY ) ) {
            return value;
        }
        // this code works only id scale and multiplier is given else it will fail
        fromUnitFactor = getDividendFromConfig( fromUnit, quantity );
        toUnitFactor = getDividendFromConfig( toUnit, quantity );
        return applyArithmeticOperation( value, fromUnitFactor, toUnitFactor );
    }

    /**
     * Apply arithmetic operation.
     *
     * @param value
     *         the value
     * @param fromUnitFactor
     *         the from unit factor
     * @param toUnitFactor
     *         the to unit factor
     *
     * @return the double
     */
    private static double applyArithmeticOperation( double value, double fromUnitFactor, double toUnitFactor ) {
        try {
            return fromUnit( toUnit( value, toUnitFactor ), fromUnitFactor );
        } catch ( ArithmeticException e ) {
            ExceptionLogger.logException( e, e.getClass() );
            return value;
        }
    }

    /**
     * Gets the dividend from config.
     *
     * @param unitVal
     *         the unit val
     * @param quantity
     *         the quantity
     *
     * @return the dividend from config
     */
    private static double getDividendFromConfig( String unitVal, String quantity ) {
        List< UnitsFamily > unitFamilylist = PropertiesManager.getConvertionUnits();
        for ( UnitsFamily unitFamily : unitFamilylist ) {
            if ( unitFamily.getUnitsFamily().equalsIgnoreCase( quantity ) ) {
                for ( UnitsList unit : unitFamily.getUnits() ) {
                    if ( unit.getName().equalsIgnoreCase( unitVal ) || unit.getScale().equalsIgnoreCase( unitVal )
                            || unit.getLabel().equalsIgnoreCase( unitVal ) ) {
                        return Double.parseDouble( unit.getMultiplier() );
                    }
                }
            }
        }
        return 1;
    }

    /**
     * Convert to lengh base value.
     *
     * @param unit
     *         the unit
     *
     * @return the double scaling factor
     */
    private static double convertToLenghBaseScalingFactor( String unit ) {
        BigInteger dividend;
        BigInteger divisor;
        switch ( unit.toLowerCase() ) {
            case "kilometer", "km" -> {
                dividend = ( ( RationalConverter ) SI.KILOMETRE.getConverterTo( SI.METRE ) ).getDividend();
                divisor = ( ( RationalConverter ) SI.KILOMETRE.getConverterTo( SI.METRE ) ).getDivisor();
            }
            case "centimeter", "cm" -> {
                dividend = ( ( RationalConverter ) SI.CENTI( SI.METRE ).getConverterTo( SI.METRE ) ).getDividend();
                divisor = ( ( RationalConverter ) SI.CENTI( SI.METRE ).getConverterTo( SI.METRE ) ).getDivisor();
            }
            case "millimeter", "mm" -> {
                dividend = ( ( RationalConverter ) SI.MILLI( SI.METRE ).getConverterTo( SI.METRE ) ).getDividend();
                divisor = ( ( RationalConverter ) SI.MILLI( SI.METRE ).getConverterTo( SI.METRE ) ).getDivisor();
            }
            case "micrometer", "μm" -> {
                dividend = ( ( RationalConverter ) SI.MICRO( SI.METRE ).getConverterTo( SI.METRE ) ).getDividend();
                divisor = ( ( RationalConverter ) SI.MICRO( SI.METRE ).getConverterTo( SI.METRE ) ).getDivisor();
            }
            case "nanometer", "nm" -> {
                dividend = ( ( RationalConverter ) SI.NANO( SI.METRE ).getConverterTo( SI.METRE ) ).getDividend();
                divisor = ( ( RationalConverter ) SI.NANO( SI.METRE ).getConverterTo( SI.METRE ) ).getDivisor();
            }
            case "yard", "yd" -> {
                dividend = ( ( RationalConverter ) NonSI.YARD.getConverterTo( SI.METRE ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.YARD.getConverterTo( SI.METRE ) ).getDivisor();
            }
            case "foot", "ft" -> {
                dividend = ( ( RationalConverter ) NonSI.FOOT.getConverterTo( SI.METRE ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.FOOT.getConverterTo( SI.METRE ) ).getDivisor();
            }
            case "inches", "in" -> {
                dividend = ( ( RationalConverter ) NonSI.INCH.getConverterTo( SI.METRE ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.INCH.getConverterTo( SI.METRE ) ).getDivisor();
            }
            case "angstorm", "ao" -> {
                dividend = ( ( RationalConverter ) NonSI.ANGSTROM.getConverterTo( SI.METRE ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.ANGSTROM.getConverterTo( SI.METRE ) ).getDivisor();
            }
            default -> {
                dividend = BigInteger.valueOf( 1L );
                divisor = BigInteger.valueOf( 1L );
            }
        }

        return divisor.doubleValue() / dividend.doubleValue();
    }

    /**
     * Convert to mass base scaling factor.
     *
     * @param unit
     *         the unit
     *
     * @return the double
     */
    private static double convertToMassBaseScalingFactor( String unit ) {
        BigInteger dividend;
        BigInteger divisor;
        switch ( unit.toLowerCase() ) {
            case "ton", "t" -> {
                dividend = ( ( RationalConverter ) NonSI.TON_US.getConverterTo( SI.KILOGRAM ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.TON_US.getConverterTo( SI.KILOGRAM ) ).getDivisor();
            }
            case "gram", "g" -> {
                dividend = ( ( RationalConverter ) SI.GRAM.getConverterTo( SI.KILOGRAM ) ).getDividend();
                divisor = ( ( RationalConverter ) SI.GRAM.getConverterTo( SI.KILOGRAM ) ).getDivisor();
            }
            case "centigram", "cg" -> {
                dividend = ( ( RationalConverter ) SI.CENTI( SI.GRAM ).getConverterTo( SI.KILOGRAM ) ).getDividend();
                divisor = ( ( RationalConverter ) SI.CENTI( SI.GRAM ).getConverterTo( SI.KILOGRAM ) ).getDivisor();
            }
            case "milligram", "mg" -> {
                dividend = ( ( RationalConverter ) SI.MILLI( SI.GRAM ).getConverterTo( SI.KILOGRAM ) ).getDividend();
                divisor = ( ( RationalConverter ) SI.MILLI( SI.GRAM ).getConverterTo( SI.KILOGRAM ) ).getDivisor();
            }
            case "microgram", "μg" -> {
                dividend = ( ( RationalConverter ) SI.MICRO( SI.GRAM ).getConverterTo( SI.KILOGRAM ) ).getDividend();
                divisor = ( ( RationalConverter ) SI.MICRO( SI.GRAM ).getConverterTo( SI.KILOGRAM ) ).getDivisor();
            }
            case "pound", "lb" -> {
                dividend = ( ( RationalConverter ) NonSI.POUND.getConverterTo( SI.KILOGRAM ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.POUND.getConverterTo( SI.KILOGRAM ) ).getDivisor();
            }
            case "ounce", "oz" -> {
                dividend = ( ( RationalConverter ) NonSI.OUNCE.getConverterTo( SI.KILOGRAM ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.OUNCE.getConverterTo( SI.KILOGRAM ) ).getDivisor();
            }
            default -> {
                dividend = BigInteger.valueOf( 1L );
                divisor = BigInteger.valueOf( 1L );
            }
        }

        return divisor.doubleValue() / dividend.doubleValue();
    }

    /**
     * Convert to Time base scaling factor.
     *
     * @param unit
     *         the unit
     *
     * @return the double
     */
    private static double convertToTimeBaseScalingFactor( String unit ) {
        BigInteger dividend;
        BigInteger divisor;
        switch ( unit.toLowerCase() ) {
            case "nanosecond", "ns" -> {
                dividend = ( ( RationalConverter ) SI.NANO( SI.SECOND ).getConverterTo( SI.SECOND ) ).getDividend();
                divisor = ( ( RationalConverter ) SI.NANO( SI.SECOND ).getConverterTo( SI.SECOND ) ).getDivisor();
            }
            case "microsecond", "μs" -> {
                dividend = ( ( RationalConverter ) SI.MICRO( SI.SECOND ).getConverterTo( SI.SECOND ) ).getDividend();
                divisor = ( ( RationalConverter ) SI.MICRO( SI.SECOND ).getConverterTo( SI.SECOND ) ).getDivisor();
            }
            case "millisecond", "ms" -> {
                dividend = ( ( RationalConverter ) SI.MILLI( SI.SECOND ).getConverterTo( SI.SECOND ) ).getDividend();
                divisor = ( ( RationalConverter ) SI.MILLI( SI.SECOND ).getConverterTo( SI.SECOND ) ).getDivisor();
            }
            case "minute", "m" -> {
                dividend = ( ( RationalConverter ) NonSI.MINUTE.getConverterTo( SI.SECOND ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.MINUTE.getConverterTo( SI.SECOND ) ).getDivisor();
            }
            case "hour", "hr" -> {
                dividend = ( ( RationalConverter ) NonSI.HOUR.getConverterTo( SI.SECOND ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.HOUR.getConverterTo( SI.SECOND ) ).getDivisor();
            }
            case "day", "d" -> {
                dividend = ( ( RationalConverter ) NonSI.DAY.getConverterTo( SI.SECOND ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.DAY.getConverterTo( SI.SECOND ) ).getDivisor();
            }
            case "week", "w" -> {
                dividend = ( ( RationalConverter ) NonSI.WEEK.getConverterTo( SI.SECOND ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.WEEK.getConverterTo( SI.SECOND ) ).getDivisor();
            }
            default -> {
                dividend = BigInteger.valueOf( 1L );
                divisor = BigInteger.valueOf( 1L );
            }
        }

        return divisor.doubleValue() / dividend.doubleValue();
    }

    /**
     * Convert to speed base scaling factor.
     *
     * @param unit
     *         the unit
     *
     * @return the double
     */
    private static double convertToSpeedBaseScalingFactor( String unit ) {
        BigInteger dividend;
        BigInteger divisor;
        switch ( unit.toLowerCase() ) {
            case "knot", "kn" -> {
                dividend = ( ( RationalConverter ) NonSI.KNOT.getConverterTo( SI.METRES_PER_SECOND ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.KNOT.getConverterTo( SI.METRES_PER_SECOND ) ).getDivisor();
            }
            case "kilometerperhour", "km/h" -> {
                dividend = ( ( RationalConverter ) NonSI.KILOMETRES_PER_HOUR.getConverterTo( SI.METRES_PER_SECOND ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.KILOMETRES_PER_HOUR.getConverterTo( SI.METRES_PER_SECOND ) ).getDivisor();
            }
            case "milesperhour", "mi/h" -> {
                dividend = ( ( RationalConverter ) NonSI.MILES_PER_HOUR.getConverterTo( SI.METRES_PER_SECOND ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.MILES_PER_HOUR.getConverterTo( SI.METRES_PER_SECOND ) ).getDivisor();
            }
            default -> {
                dividend = BigInteger.valueOf( 1L );
                divisor = BigInteger.valueOf( 1L );
            }
        }

        return divisor.doubleValue() / dividend.doubleValue();
    }

    /**
     * Convert to force base scaling factor.
     *
     * @param unit
     *         the unit
     *
     * @return the double
     */
    private static double convertToForceBaseScalingFactor( String unit ) {
        BigInteger dividend;
        BigInteger divisor;
        switch ( unit.toLowerCase() ) {
            case "dyne", "dy" -> {
                dividend = ( ( RationalConverter ) NonSI.DYNE.getConverterTo( SI.NEWTON ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.DYNE.getConverterTo( SI.NEWTON ) ).getDivisor();
            }
            case "kilopound", "kp" -> {
                dividend = ( ( RationalConverter ) NonSI.KILOGRAM_FORCE.getConverterTo( SI.NEWTON ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.KILOGRAM_FORCE.getConverterTo( SI.NEWTON ) ).getDivisor();
            }
            case "poundforce", "pf" -> {
                dividend = ( ( RationalConverter ) NonSI.POUND_FORCE.getConverterTo( SI.NEWTON ) ).getDividend();
                divisor = ( ( RationalConverter ) NonSI.POUND_FORCE.getConverterTo( SI.NEWTON ) ).getDivisor();
            }
            default -> {
                dividend = BigInteger.valueOf( 1L );
                divisor = BigInteger.valueOf( 1L );
            }
        }

        return divisor.doubleValue() / dividend.doubleValue();
    }

    /**
     * To unit.
     *
     * @param value
     *         the value
     * @param factor
     *         the factor
     *
     * @return the double
     */
    private static double toUnit( double value, double factor ) {
        return ( value * factor );
    }

    /**
     * From unit.
     *
     * @param value
     *         the value
     * @param factor
     *         the factor
     *
     * @return the double
     */
    private static double fromUnit( double value, double factor ) {
        return ( value / factor );
    }

}
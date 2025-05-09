package de.soco.software.simuspace.workflow.processing.impl;

import javax.swing.*;

import java.io.File;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.workflow.exceptions.SusExpressionException;
import de.soco.software.simuspace.workflow.util.SuSConditionalExpressionEvaluator;

/**
 * This class is to try (and also to test) the possibility to use Spring EL to implement the conditional workflow element.
 *
 * @author Matthias Rummel
 * @see Spring EL documentation at https://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html
 */
@Log4j2
public class SuSConditionalExpressionEvaluatorTest {

    /**
     * The expected exception from message.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Adding should work in expression.
     */
    @Test
    public void addingShouldWorkInExpression() {

        final String expression1 = "1+2 == 3";
        boolean result = evaluateExpression( expression1 );
        Assert.assertEquals( true, result );

        final String expression2 = "3+4 == 7";
        result = evaluateExpression( expression2 );
        Assert.assertEquals( true, result );

        final String expression3 = "(2+(3+(1+1+1))) == 8";
        result = evaluateExpression( expression3 );
        Assert.assertEquals( true, result );

        log.info( "tested plus." );
    }

    /**
     * All mathematical operators should work in expression.
     */
    @Test
    public void allMathematicalOperatorsShouldWorkInExpression() {

        final String expression1 = "2 * 2 == 4";
        boolean result = evaluateExpression( expression1 );
        Assert.assertEquals( true, result );

        final String expression2 = "9 / 3 == 3";
        result = evaluateExpression( expression2 );
        Assert.assertEquals( true, result );

        final String expression3 = "5 - 4 == 1";
        result = evaluateExpression( expression3 );
        Assert.assertEquals( true, result );

        final String expression4 = "5 - 3 == 1";
        result = evaluateExpression( expression4 );
        Assert.assertEquals( false, result );

        final String expression5 = "(1+2)*3 == 9";
        result = evaluateExpression( expression5 );
        Assert.assertEquals( true, result );

        final String expression6 = "(1+2)*(3+4) == 21";
        result = evaluateExpression( expression6 );
        Assert.assertEquals( true, result );

        final String expression7 = "4*(2+(3+(1+1+1))) == 32";
        result = evaluateExpression( expression7 );
        Assert.assertEquals( true, result );

        log.info( "tested other mathematical operators." );
    }

    /**
     * Comparators should work with strings.
     */
    @Test
    public void comparatorsShouldWorkWithStrings() {

        final String expression1 = "('abbbbc' == 'b')";
        boolean result = evaluateExpression( expression1 );
        Assert.assertEquals( false, result );

        final String expression2 = "('a' < 'b') || ('b' < 'c')";
        result = evaluateExpression( expression2 );
        Assert.assertEquals( true, result );

        log.info( "tested calculation mix." );
    }

    /**
     * Equals should work on ints.
     */
    @Test
    public void equalsShouldWorkOnInts() {

        final String expression1 = "1 == 1";
        boolean result = evaluateExpression( expression1 );
        Assert.assertEquals( true, result );

        final String expression2 = "1+1 == 2";
        result = evaluateExpression( expression2 );
        Assert.assertEquals( true, result );

        final String expression3 = "1+1 == 3";
        result = evaluateExpression( expression3 );
        Assert.assertEquals( false, result );

        log.info( "tested equals for integer." );
    }

    /**
     * Verifies that the passed expression string evaluated and return the result of it.
     *
     * @param expression
     *         The expression to evaluate.
     * @param expected
     *         The expected result.
     * @param context
     *         the context holding the values of the parameters.
     */
    private boolean evaluateExpression( String expression ) {

        final SuSConditionalExpressionEvaluator susConditionEvaluator = new SuSConditionalExpressionEvaluator();
        final boolean result = susConditionEvaluator.evaluate( expression );
        log.debug( "Result of '" + expression + "': " + result );
        return result;

    }

    /**
     * Invalid expression should throw exception.
     */
    @Test
    public void invalidExpressionShouldThrowException() {

        final String expression1 = "1 === 1";

        thrown.expect( SusExpressionException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.MESSAGE_PROVIDED_EXPRESSION_IS_INVALID, expression1 ) );

        final boolean result = evaluateExpression( expression1 );
        Assert.assertEquals( true, result );
        log.info( "tested in valid expressing throws custom exception." );
    }

    /**
     * Invalid function used in expression should throw exception.
     */
    @Test
    public void invalidFunctionUsedInExpressionShouldThrowException() {

        final String isExistanceExpression = "inValidFunction('src/test/resources/conditional_test.txt')";

        thrown.expect( SusExpressionException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.MESSAGE_IN_VALID_CUSTOM_METHOD_SUFIX ) );
        log.info( "tested in valid function used in expression throws custom exception." );

        final boolean result = evaluateExpression( isExistanceExpression );
        Assert.assertEquals( true, result );

    }

    /**
     * Should throw exception when invalid expression invalid numeric is passed.
     */
    @Test
    public void shouldThrowExceptionWhenInvalidExpressionInvalidNumericIsPassed() {

        final String expression1 = "1";
        thrown.expect( SusExpressionException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.MESSAGE_PROVIDED_EXPRESSION_IS_INVALID, expression1 ) );

        final boolean result = evaluateExpression( expression1 );
        Assert.assertEquals( false, result );
        log.info( "tested in valid expressing throws custom exception." );
    }

    /**
     * Invalid expression should throw exception.
     */
    @Test
    public void shouldThrowExceptionWhenInvalidExpressionStringPassed() {

        final String expression1 = "asdf";
        thrown.expect( SusExpressionException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.MESSAGE_PROVIDED_EXPRESSION_IS_INVALID, expression1 ) );

        final boolean result = evaluateExpression( expression1 );
        Assert.assertEquals( false, result );
        log.info( "tested in valid expressing throws custom exception." );
    }

    /**
     * Tests the "AND" condition for booleans.
     */
    @Test
    public void shouldWorkANDWithBooleans() {

        // Different caps letters in expression
        final String expression1 = "True AND False";
        boolean result = evaluateExpression( expression1 );
        Assert.assertEquals( false, result );

        final String expression2 = "true AND false";
        result = evaluateExpression( expression2 );
        Assert.assertEquals( false, result );

        final String expression4 = "TRUE AND FALSE";
        result = evaluateExpression( expression4 );
        Assert.assertEquals( false, result );

        final String expression3 = "true and false";
        result = evaluateExpression( expression3 );
        Assert.assertEquals( false, result );

        // logic
        final String expression5 = "True AND True";
        result = evaluateExpression( expression5 );
        Assert.assertEquals( true, result );

        final String expression6 = "False AND True";
        result = evaluateExpression( expression6 );
        Assert.assertEquals( false, result );

        final String expression7 = "False AND False";
        result = evaluateExpression( expression7 );
        Assert.assertEquals( false, result );

        log.info( "tested AND expression for booleans." );
    }

    /**
     * Test custom expression function contains text in files.
     */
    @Test
    public void shouldWorkCustomFunctionToCheckTextConatinsInFile() {

        final File file = new File( "src/test/resources/conditional_test.txt" );
        Assert.assertTrue( file.exists() );

        final String isExistanceExpression = "2>1 && contains('src/test/resources/conditional_test.txt','soco')";
        final boolean result = evaluateExpression( isExistanceExpression );
        Assert.assertEquals( true, result );

        log.info( "tested file 'src/test/resources/conditional_test.txt' contains 'soco'." );
    }

    /**
     * Should work mix bools with all comparators.
     */
    @Test
    public void shouldWorkMixBoolsWithAllComparators() {

        final String expression1 = "True AND (1<2)";
        boolean result = evaluateExpression( expression1 );
        Assert.assertEquals( true, result );

        final String expression2 = "True AND (1>2)";
        result = evaluateExpression( expression2 );
        Assert.assertEquals( false, result );

        log.info( "tested calculation mix." );
    }

    /**
     * Test the other logical operators: OR, NOT
     */
    @Test
    public void shouldWorkORWithBooleanOperators() {

        // OR
        final String expression1 = "True or False";
        boolean result = evaluateExpression( expression1 );
        Assert.assertEquals( true, result );

        final String expression2 = "True or True";
        result = evaluateExpression( expression2 );
        Assert.assertEquals( true, result );

        final String expression3 = "False or True";
        result = evaluateExpression( expression3 );
        Assert.assertEquals( true, result );

        final String expression4 = "False or False";
        result = evaluateExpression( expression4 );
        Assert.assertEquals( false, result );
        // NOT
        final String expression5 = "False or not True";
        result = evaluateExpression( expression5 );
        Assert.assertEquals( false, result );
        log.info( "tested other logical operators." );
    }

    /**
     * Test custom expression function exists, isFile and isDirectory.
     *
     * @throws SusException
     */
    @Test
    public void shouldWorkWithCustomFunctionsInExpressionForFile() throws SusException {

        final File file = new File( "src/test/resources/conditional_test.txt" );
        Assert.assertTrue( file.exists() );

        final String isExistanceExpression = "exists('src/test/resources/conditional_test.txt')";
        boolean result = evaluateExpression( isExistanceExpression );
        Assert.assertEquals( true, result );

        final String isFileExpression = "isFile('src/test/resources/conditional_test.txt')";
        result = evaluateExpression( isFileExpression );
        Assert.assertEquals( true, result );

        final String isDirectoryExpression = "!isDirectory('src/test/resources/conditional_test.txt')";
        result = evaluateExpression( isDirectoryExpression );
        Assert.assertEquals( true, result );

        final String grupedExistsFileAndDirectoryExpression = "exists('src/test/resources/conditional_test.txt') && isFile('src/test/resources/conditional_test.txt') && !isDirectory('src/test/resources/conditional_test.txt') AND 1==1";
        result = evaluateExpression( grupedExistsFileAndDirectoryExpression );
        Assert.assertEquals( true, result );

        log.info( "tested path existance is file and is directory through expression." );
    }

    /**
     * Test the other comparison operators: <, <=, >=, !=
     */
    @Test
    public void whenComparisonOperatorsUsedExpressionShuldWork() {

        final String expression1 = "1 < 2";
        boolean result = evaluateExpression( expression1 );
        Assert.assertEquals( true, result );

        final String expression2 = "2 < 1";
        result = evaluateExpression( expression2 );
        Assert.assertEquals( false, result );

        final String expression3 = "1 <= 2";
        result = evaluateExpression( expression3 );
        Assert.assertEquals( true, result );

        final String expression4 = "2 <= 1";
        result = evaluateExpression( expression4 );
        Assert.assertEquals( false, result );

        final String expression5 = "1 >= 2";
        result = evaluateExpression( expression5 );
        Assert.assertEquals( false, result );

        final String expression6 = "2 >= 1";
        result = evaluateExpression( expression6 );
        Assert.assertEquals( true, result );

        final String expression7 = "1 != 1";
        result = evaluateExpression( expression7 );
        Assert.assertEquals( false, result );

        final String expression8 = "1 != 2";
        result = evaluateExpression( expression8 );
        Assert.assertEquals( true, result );

        log.info( "tested other comparators." );

        final String expression9 = "9 > 10";
        result = evaluateExpression( expression9 );
        Assert.assertEquals( false, result );

        final String expression10 = "9 > 7";
        result = evaluateExpression( expression10 );
        Assert.assertEquals( true, result );

        final String expression11 = "21 > 32";
        result = evaluateExpression( expression11 );
        Assert.assertEquals( false, result );

        log.info( "tested greater than." );
    }

}

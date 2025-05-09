package de.soco.software.simuspace.workflow.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.workflow.exceptions.SusExpressionException;

/**
 * This class is a SimuSpace conditional evaluator this parses the SimuSpace expression uses Spring Expression Language as third party and
 * customize it according to SuS Expression.
 *
 * @author Nasir.Farooq
 */
public class SuSConditionalExpressionEvaluator {

    /**
     * The Constant JAVA_LANG.
     */
    private static final String MESSAGE_PART_TO_REMOVE = "java.lang.";

    /**
     * The Constant EXCEPTION_MESSAGE_PART_FOR_CUSTOMIZATION_NOT_FOUND.
     */
    private static final String EXCEPTION_MESSAGE_PART_FOR_CUSTOMIZATION_NOT_FOUND = "cannot be found";

    /**
     * The Constant EXCEPTION_MESSAGE_PART_FOR_CUSTOMIZATION_METHOD_CALL.
     */
    private static final String EXCEPTION_MESSAGE_PART_FOR_CUSTOMIZATION_METHOD_CALL = "Method call:";

    /**
     * The Constant REGEX_TO_READ_EXCEPTION_MESSAGE.
     */
    private static final String REGEX_TO_READ_EXCEPTION_MESSAGE = "^.*Method .*? cannot be found.*";

    /**
     * Instantiates a new SuS conditional expression evaluator.
     */
    public SuSConditionalExpressionEvaluator() {
        super();
    }

    /**
     * Checks either file contains specific text or not. This function name will be registered in {@link StandardEvaluationContext} and then
     * usable in SuS expression and expression can be parse able by {@link SpelExpressionParser}
     *
     * @param filePath
     *         the file path to read.
     * @param text
     *         the text to find in file
     *
     * @return true, if successful
     */
    public boolean contains( String filePath, String text ) {
        try ( BufferedReader bufferedReader = new BufferedReader( new FileReader( filePath ) ) ) {

            String line;
            while ( ( line = bufferedReader.readLine() ) != null ) {
                if ( line.contains( text ) ) {
                    return true;
                }
            }
        } catch ( final IOException e ) {
            ExceptionLogger.logException( e, getClass() );
            return false;
        }

        return false;
    }

    /**
     * Evaluates written in SuS expression language.
     *
     * @param expression
     *         the expression
     *
     * @return true, if successful
     */
    public boolean evaluate( String expression ) {
        Boolean result;
        try {
            final SuSConditionalExpressionEvaluator wfeConditionalAction = new SuSConditionalExpressionEvaluator();
            final EvaluationContext context = new StandardEvaluationContext( wfeConditionalAction );

            final ExpressionParser parser = new SpelExpressionParser();

            final Expression exp = parser.parseExpression( expression );
            result = ( Boolean ) exp.getValue( context );
            return result;
        } catch ( final EvaluationException e ) {
            String message = e.getMessage();
            if ( message.matches( REGEX_TO_READ_EXCEPTION_MESSAGE ) ) {
                message = message.substring(
                        message.indexOf( EXCEPTION_MESSAGE_PART_FOR_CUSTOMIZATION_METHOD_CALL )
                                + EXCEPTION_MESSAGE_PART_FOR_CUSTOMIZATION_METHOD_CALL.length(),
                        message.lastIndexOf( EXCEPTION_MESSAGE_PART_FOR_CUSTOMIZATION_NOT_FOUND ) );
                throw new SusExpressionException( message.replace( MESSAGE_PART_TO_REMOVE, "" )
                        + MessagesUtil.getMessage( WFEMessages.MESSAGE_IN_VALID_CUSTOM_METHOD_SUFIX ) );
            }
            ExceptionLogger.logException( e, getClass() );
            throw new SusExpressionException( MessagesUtil.getMessage( WFEMessages.MESSAGE_PROVIDED_EXPRESSION_IS_INVALID, expression ) );
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );

            throw new SusExpressionException( MessagesUtil.getMessage( WFEMessages.MESSAGE_PROVIDED_EXPRESSION_IS_INVALID, expression ) );
        }
    }

    /**
     * Checks the existence of the specified path. This function name will be registered in {@link StandardEvaluationContext} and then
     * usable in SuS expression and expression can be parse able by {@link SpelExpressionParser}
     *
     * @param path
     *         the expression for existence java.lang.RuntimeException: EL1004E: Method call: Method
     *         containsa(java.lang.String,java.lang.String)
     *
     * @return true, if successful
     */
    public boolean exists( String path ) {

        if ( StringUtils.isNullOrEmpty( path ) ) {
            return false;
        }

        return new File( path ).exists();
    }

    /**
     * Checks if the specified path is directory. This function name will be registered in {@link StandardEvaluationContext} and then usable
     * in SuS expression and expression can be parse able by {@link SpelExpressionParser}
     *
     * @param path
     *         the expression for existence
     *
     * @return true, if successful
     */
    public boolean isDirectory( String path ) {

        if ( StringUtils.isNullOrEmpty( path ) ) {
            return false;
        }

        return new File( path ).isDirectory();
    }

    /**
     * Checks if the specified path is file. This function name will be registered in {@link StandardEvaluationContext} and then usable in
     * SuS expression and expression can be parse able by {@link SpelExpressionParser}
     *
     * @param path
     *         the expression for existence
     *
     * @return true, if successful
     */
    public boolean isFile( String path ) {

        if ( StringUtils.isNullOrEmpty( path ) ) {
            return false;
        }

        return new File( path ).isFile();
    }

}

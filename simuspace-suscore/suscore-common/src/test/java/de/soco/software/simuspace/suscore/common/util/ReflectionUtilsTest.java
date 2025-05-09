package de.soco.software.simuspace.suscore.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UserDTO;

/**
 * The Class is responsible to test public methods of {@link ReflectionUtils}.
 */
public class ReflectionUtilsTest {

    /**
     * The Constant GET_NAME.
     */
    private static final String METHOD_NAME_GET_NAME = "getName";

    /**
     * The Constant NEW_STATIC_FIELD_VALUE.
     */
    private static final String NEW_STATIC_FIELD_VALUE = "newValue";

    private static final String USER_ID = UUID.randomUUID().toString();

    /**
     * The Constant EXISTING_FIELD_ID.
     */
    private static final String EXISTING_FIELD_ID = "id";

    /**
     * The Constant NON_EXISTING_FIELD_NAME.
     */
    private static final String NON_EXISTING_FIELD_NAME = "name";

    /**
     * The Constant METHOD_NAME_SET_CUSTOM_ATTRIB.
     */
    private static final String METHOD_NAME_SET_CUSTOM_ATTRIB = "setCustomAttrib";

    /**
     * The library DTO.
     */
    private static LibraryDTOTestHelper libraryDTO;

    /**
     * The user DTO.
     */
    private static UserDTO userDTO;

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * Sets the up.
     */
    @BeforeClass
    public static void setUp() {
        libraryDTO = fillLibraryDTOTest();
        userDTO = fillUserDTO();
    }

    /**
     * Should return field of when valid field name is passed.
     *
     * @throws NoSuchFieldException
     *         the no such field exception
     * @throws SecurityException
     *         the security exception
     */
    @Test
    public void shouldReturnFieldOfWhenValidFieldNameIsPassed() throws NoSuchFieldException, SecurityException {
        Field expectedFieldId = ReflectionUtils.getFieldByName( UserDTO.class, EXISTING_FIELD_ID );
        Assert.assertEquals( UserDTO.class.getDeclaredField( EXISTING_FIELD_ID ), expectedFieldId );

    }

    /**
     * Should throw exception when non existing field name is passed to get field by name.
     */
    @Test
    public void shouldThrowExceptionWhenNonExistingFieldNameIsPassedToGetFieldByName() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.FIELD_NOT_FOUND_IN_CLASS.getKey(), NON_EXISTING_FIELD_NAME,
                UserDTO.class.getName() ) );
        ReflectionUtils.getFieldByName( UserDTO.class, NON_EXISTING_FIELD_NAME );
    }

    /**
     * Should set final static field of A class.
     *
     * @throws NoSuchFieldException
     *         the no such field exception
     * @throws IllegalAccessException
     *         the illegal access exception
     */
    @Test
    public void shouldSetFinalStaticFieldOfAClass() throws NoSuchFieldException, IllegalAccessException {
        String oldValue = ConstantsKaraf.KARAF_CONF_PATH;
        ReflectionUtils.setFinalStaticField( ConstantsKaraf.class, ConstantsKaraf.STATIC_CONSTATN_FIELD_KARAF_CONF_PATH,
                NEW_STATIC_FIELD_VALUE ); // For a String
        Assert.assertNotEquals( oldValue, ConstantsKaraf.KARAF_CONF_PATH );
        Assert.assertEquals( NEW_STATIC_FIELD_VALUE, ConstantsKaraf.KARAF_CONF_PATH );
    }

    /**
     * Should throw exception when non existing field set final static field of A class.
     *
     * @throws NoSuchFieldException
     *         the no such field exception
     * @throws IllegalAccessException
     *         the illegal access exception
     */
    @Test
    public void shouldThrowExceptionWhenNonExistingFieldSetFinalStaticFieldOfAClass() throws NoSuchFieldException, IllegalAccessException {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.FIELD_NOT_FOUND_IN_CLASS.getKey(), NON_EXISTING_FIELD_NAME,
                ConstantsKaraf.class.getName() ) );
        ReflectionUtils.setFinalStaticField( ConstantsKaraf.class, NON_EXISTING_FIELD_NAME, NEW_STATIC_FIELD_VALUE ); // For a String
    }

    /**
     * Should throw exception when invoke prepare entity is called and method does not exist.
     */
    @Test
    public void shouldThrowExceptionWhenInvokePrepareEntityIsCalledAndMethodDoesNotExist() {
        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessageBundleFactory.getMessage( Messages.UNABLE_TO_PREPARE_ENTITY_OF_DTO.getKey(), userDTO.getClass().getSimpleName() ) );
        ReflectionUtils.invokePrepareEntity( userDTO, USER_ID );
    }

    /**
     * Should successfully invoke prepare entity and return expected value.
     */
    @Test
    public void shouldSuccessfullyInvokePrepareEntityAndReturnExpectedValue() {
        Object actual = ReflectionUtils.invokePrepareEntity( libraryDTO, USER_ID );
        Assert.assertEquals( NEW_STATIC_FIELD_VALUE, actual );
    }

    /**
     * Should throw exception when invoke method is called and method does not exist.
     */
    @Test
    public void shouldThrowExceptionWhenInvokeMethodIsCalledAndMethodDoesNotExist() {
        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessageBundleFactory.getMessage( Messages.UNABLE_TO_PREPARE_ENTITY_OF_DTO.getKey(), userDTO.getClass().getSimpleName() ) );
        ReflectionUtils.invokeMethod( METHOD_NAME_GET_NAME, userDTO );
    }

    /**
     * Should successfully invoke method and return expected value.
     */
    @Test
    public void shouldSuccessfullyInvokeMethodAndReturnExpectedValue() {
        Object actual = ReflectionUtils.invokeMethod( METHOD_NAME_GET_NAME, libraryDTO );
        Assert.assertEquals( NEW_STATIC_FIELD_VALUE, actual );
    }

    /**
     * Should throw exception when invoke method with list param is called and method does not exist.
     */
    @Test
    public void shouldThrowExceptionWhenInvokeMethodWithListParamIsCalledAndMethodDoesNotExist() {
        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessageBundleFactory.getMessage( Messages.UNABLE_TO_PREPARE_ENTITY_OF_DTO.getKey(), userDTO.getClass().getSimpleName() ) );
        List< String > customAttrib = new ArrayList<>();
        ReflectionUtils.invokeMethodWithListParam( METHOD_NAME_GET_NAME, userDTO, customAttrib );
    }

    /**
     * Should successfully invoke method with list param and return expected value.
     */
    @Test
    public void shouldSuccessfullyInvokeMethodWithListParamAndReturnExpectedValue() {
        List< String > expectedObject = new ArrayList<>();
        ReflectionUtils.invokeMethodWithListParam( METHOD_NAME_SET_CUSTOM_ATTRIB, libraryDTO, expectedObject );
        Assert.assertEquals( expectedObject, libraryDTO.getCustomAttrib() );
    }

    /**
     * Fill library DTO test.
     *
     * @return the library DTO test
     */
    private static LibraryDTOTestHelper fillLibraryDTOTest() {
        libraryDTO = new LibraryDTOTestHelper();
        libraryDTO.setName( NEW_STATIC_FIELD_VALUE );
        return libraryDTO;
    }

    /**
     * Fill user DTO.
     *
     * @return the user DTO
     */
    private static UserDTO fillUserDTO() {
        userDTO = new UserDTO();
        userDTO.setFirstName( NEW_STATIC_FIELD_VALUE );
        return userDTO;
    }

}

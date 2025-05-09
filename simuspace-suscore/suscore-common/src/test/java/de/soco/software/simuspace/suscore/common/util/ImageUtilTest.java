package de.soco.software.simuspace.suscore.common.util;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.EncryptionDecryptionDTO;

/**
 * @author Ahmar Nadeem
 *
 * A JUnit test class to test the functionality of @link ImageUtil.java
 */
public class ImageUtilTest {

    /**
     * Expected width of the resized image
     */
    private static final int EXPECTED_WIDTH = 40;

    /**
     * Expected height of the resized image
     */
    private static final int EXPECTED_HEIGHT = 60;

    /**
     * temp image under test
     */
    private static final String TEST_IMAGE = "user.png";

    /**
     * The Constant INVALID_IMAGE.
     */
    private static final String INVALID_IMAGE = "user234.png";

    /**
     * temp image under test with size 1920 * 1080
     */
    private static final String TEST_IMAGE_500x550 = "500x550.jpg";

    /**
     * temp image under test with size 1920 * 1080
     */
    private static final String TEST_IMAGE_1920x1080 = "1920x1080.png";

    /**
     * temp name of the profile image
     */
    private static final String USER_ID = "userProfileImageId";

    /**
     * The Constant TEST_IMAGE_200x187.
     */
    private static final String TEST_IMAGE_200x187 = "200x187.jpg";

    /**
     * The Constant SRC_FILE.
     */
    private static final File SRC_FILE = new File( "src/test/resources/user.png" );

    /**
     * Generic Rule for the expected exception
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * remove files at the end of all the tests
     */
    @AfterClass
    public static void tearDown() {
        File dir = new File( ConstantsString.TEST_RESOURCE_PATH );
        for ( File file : dir.listFiles() ) {
            if ( !file.isDirectory() && file.getName().contains( USER_ID ) ) {
                file.delete();
            }
        }
    }

    /**
     * Test if the image is properly resized
     *
     * @throws IOException
     */
    @Test
    public void shouldResizeAndCropTheImageWhenValidImageAndCoordinatesProvided() throws IOException {

        File file = new File( ConstantsString.TEST_RESOURCE_PATH + TEST_IMAGE );
        BufferedImage originalImage = ImageIO.read( file );

        int expectedHeight = EXPECTED_HEIGHT;
        int expectedWidth = EXPECTED_WIDTH;

        BufferedImage actualImage = ImageUtil.resizeAndCrop( file, expectedWidth, expectedHeight );
        Assert.assertNotNull( actualImage );
        Assert.assertNotSame( originalImage, actualImage );
        Assert.assertEquals( expectedHeight, actualImage.getHeight() );
        Assert.assertEquals( expectedWidth, actualImage.getWidth() );

        // Test if the image is converted to byte array
        byte[] binaryData = ImageUtil.toByteArray( actualImage,
                TEST_IMAGE.substring( TEST_IMAGE.lastIndexOf( ConstantsString.DOT ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
        Assert.assertNotNull( binaryData );
        Assert.assertTrue( binaryData.length > ConstantsInteger.INTEGER_VALUE_ZERO );
    }

    /**
     * Test if the image is properly resized
     *
     * @throws IOException
     */
    @Test
    public void shouldResizeAndCropTheImageWhenSameHeightAndWidthValuesProvided() throws IOException {

        File file = new File( ConstantsString.TEST_RESOURCE_PATH + TEST_IMAGE );
        BufferedImage originalImage = ImageIO.read( file );

        int expectedHeight = EXPECTED_HEIGHT;
        int expectedWidth = EXPECTED_HEIGHT;

        BufferedImage actualImage = ImageUtil.resizeAndCrop( file, expectedWidth, expectedHeight );
        Assert.assertNotNull( actualImage );
        Assert.assertNotSame( originalImage, actualImage );
        Assert.assertEquals( expectedHeight, actualImage.getHeight() );
        Assert.assertEquals( expectedWidth, actualImage.getWidth() );

        byte[] binaryData = ImageUtil.toByteArray( actualImage,
                TEST_IMAGE.substring( TEST_IMAGE.lastIndexOf( ConstantsString.DOT ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
        Assert.assertNotNull( binaryData );
        Assert.assertTrue( binaryData.length > ConstantsInteger.INTEGER_VALUE_ZERO );
    }

    /**
     * Test if the image is properly resized when the original image has greater height than its width
     *
     * @throws IOException
     */
    @Test
    public void shoudlResizeAndCropTheImageWhenHeightGreaterThanWidthOfOriginalImage() throws IOException {

        File file = new File( ConstantsString.TEST_RESOURCE_PATH + TEST_IMAGE_500x550 );
        BufferedImage originalImage = ImageIO.read( file );

        int expectedHeight = EXPECTED_HEIGHT;
        int expectedWidth = EXPECTED_HEIGHT;

        BufferedImage actualImage = ImageUtil.resizeAndCrop( file, expectedWidth, expectedHeight );
        Assert.assertNotNull( actualImage );
        Assert.assertNotSame( originalImage, actualImage );
        Assert.assertEquals( expectedHeight, actualImage.getHeight() );

        byte[] binaryData = ImageUtil.toByteArray( actualImage,
                TEST_IMAGE.substring( TEST_IMAGE.lastIndexOf( ConstantsString.DOT ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
        Assert.assertNotNull( binaryData );
        Assert.assertTrue( binaryData.length > ConstantsInteger.INTEGER_VALUE_ZERO );
    }

    /**
     * Test if the image is properly resized when the original image has greater width than its height
     *
     * @throws IOException
     */
    @Test
    public void shoudlResizeAndCropTheImageWhenWidthGreaterThanHeightOfOriginalImage() throws IOException {

        File file = new File( ConstantsString.TEST_RESOURCE_PATH + TEST_IMAGE_1920x1080 );
        BufferedImage originalImage = ImageIO.read( file );

        int expectedHeight = EXPECTED_HEIGHT;
        int expectedWidth = EXPECTED_HEIGHT;

        BufferedImage actualImage = ImageUtil.resizeAndCrop( file, expectedWidth, expectedHeight );
        Assert.assertNotNull( actualImage );
        Assert.assertNotSame( originalImage, actualImage );
        Assert.assertEquals( expectedHeight, actualImage.getHeight() );

        byte[] binaryData = ImageUtil.toByteArray( actualImage,
                TEST_IMAGE.substring( TEST_IMAGE.lastIndexOf( ConstantsString.DOT ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
        Assert.assertNotNull( binaryData );
        Assert.assertTrue( binaryData.length > ConstantsInteger.INTEGER_VALUE_ZERO );
    }

    /**
     * Test if the image is not provided, then the system should throw exception
     *
     * @throws IOException
     */
    @Test
    public void shouldThrowExcpetionWhenImageNotProvidedForResizing() {

        int expectedHeight = EXPECTED_HEIGHT;
        int expectedWidth = EXPECTED_WIDTH;

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_IMAGE.getKey() ) );
        ImageUtil.resizeAndCrop( null, expectedWidth, expectedHeight );
    }

    /**
     * Test if the image is resized to default when no values provided for height and width
     */
    @Test
    public void shouldResizeToDefaultWhenNoHeightAndWidthProvided() {

        File file = new File( ConstantsString.TEST_RESOURCE_PATH + TEST_IMAGE );

        ImageUtil.resizeAndCrop( file, ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.INTEGER_VALUE_ZERO );
    }

    /**
     * Test if the image is properly resized
     *
     * @throws IOException
     */
    @Test
    public void shouldThrowExcpetionWhenInvalidImageTypeIsProvided() {

        File file = new File( ConstantsString.TEST_RESOURCE_PATH + "config.properties" );

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.IMAGE_NOT_PROVIDED_TO_RESIZE.getKey() ) );
        ImageUtil.resizeAndCrop( file, EXPECTED_HEIGHT, EXPECTED_HEIGHT );
    }

    /**
     * Test if the image is properly resized
     *
     * @throws IOException
     */
    @Test
    public void shouldThrowExcpetionWhenNullBufferedImageProvidedToConvertToByteArray() {

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_IMAGE.getKey() ) );
        ImageUtil.imageToByteArray( null,
                TEST_IMAGE.substring( TEST_IMAGE.lastIndexOf( ConstantsString.DOT ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
    }

    /**
     * This tests the function that creates the image form byte array
     */
    @Test
    public void shouldWriteByteArrayToFileAndCreateImage() {
        File file = new File( ConstantsString.TEST_RESOURCE_PATH + TEST_IMAGE );

        int expectedHeight = EXPECTED_HEIGHT;
        int expectedWidth = EXPECTED_HEIGHT;

        String expectedPath = ConstantsString.TEST_RESOURCE_PATH + USER_ID + ConstantsString.DOT
                + CommonUtils.getFileExtension( TEST_IMAGE );

        BufferedImage actualImage = ImageUtil.resizeAndCrop( file, expectedWidth, expectedHeight );
        byte[] binaryData = ImageUtil.toByteArray( actualImage,
                TEST_IMAGE.substring( TEST_IMAGE.lastIndexOf( ConstantsString.DOT ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
        String actualPath = ImageUtil.writeImageBytes( binaryData, USER_ID, ConstantsString.TEST_RESOURCE_PATH,
                CommonUtils.getFileExtension( TEST_IMAGE ) );
        Assert.assertNotNull( actualPath );
        Assert.assertEquals( expectedPath, actualPath );

    }

    /**
     * This tests the function that creates the image form byte array with default extension if image extansion is null
     */
    @Test
    public void shouldWriteByteArrayToFileAndCreateImageWithDefaultExtensionIfNoExtensionProvided() {
        File file = new File( ConstantsString.TEST_RESOURCE_PATH + TEST_IMAGE );

        int expectedHeight = EXPECTED_HEIGHT;
        int expectedWidth = EXPECTED_HEIGHT;

        String expectedPath = ConstantsString.TEST_RESOURCE_PATH + USER_ID + ConstantsString.DOT + ConstantsString.DEFAULT_IMAGE_TYPE;
        BufferedImage actualImage = ImageUtil.resizeAndCrop( file, expectedWidth, expectedHeight );
        byte[] binaryData = ImageUtil.toByteArray( actualImage,
                TEST_IMAGE.substring( TEST_IMAGE.lastIndexOf( ConstantsString.DOT ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
        String actualPath = ImageUtil.writeImageBytes( binaryData, USER_ID, ConstantsString.TEST_RESOURCE_PATH,
                ConstantsString.EMPTY_STRING );
        Assert.assertNotNull( actualPath );
        Assert.assertEquals( expectedPath, actualPath );

    }

    /**
     * Tests that if the byte array is null then the system should not create the image
     */
    @Test
    public void shouldNotCreateImageAndThrowExceptionWhenBytesAreNull() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_IMAGE.getKey() ) );
        ImageUtil.writeImageBytes( null, USER_ID, ConstantsString.TEST_RESOURCE_PATH, CommonUtils.getFileExtension( TEST_IMAGE ) );
    }

    /**
     * Test if the image is properly converted to the byte array
     */
    @Test
    public void shouldConvertImageToByteArrayWhenValidImageProvided() {

        File file = new File( ConstantsString.TEST_RESOURCE_PATH + TEST_IMAGE );

        // Test if the image is converted to byte array
        byte[] binaryData = ImageUtil.imageToByteArray( file,
                TEST_IMAGE.substring( TEST_IMAGE.lastIndexOf( ConstantsString.DOT ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
        Assert.assertNotNull( binaryData );
        Assert.assertTrue( binaryData.length > ConstantsInteger.INTEGER_VALUE_ZERO );
    }

    /**
     * Should resize the image file for thumb nail conversion and return thumb nail file when valid files are provided.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldResizeTheImageFileForThumbNailConversionAndReturnThumbNailFileWhenValidFilesAreProvided() throws IOException {
        File srcFile = new File( ConstantsString.TEST_RESOURCE_PATH + TEST_IMAGE_500x550 );
        File expectedFile = new File( ConstantsString.TEST_RESOURCE_PATH + TEST_IMAGE_200x187 );
        String actual = ImageUtil.createThumbNail( srcFile, expectedFile, new EncryptionDecryptionDTO() );
        Assert.assertEquals( expectedFile.getPath(), actual );
    }

    /**
     * Should not resize the image file for thumb nail and return empty path when src file does not exists.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shoudNotResizeTheImageFileForThumbNailAndReturnEmptyPathWhenSrcFileDoesNotExists() throws IOException {
        File file = new File( ConstantsString.EMPTY_STRING );
        String actual = ImageUtil.createThumbNail( file, file, new EncryptionDecryptionDTO() );
        Assert.assertEquals( ConstantsString.EMPTY_STRING, actual );
    }

    /**
     * Should get empty string if src file does not exist.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldGetEmptyStringIfSrcFileDoesNotExist() throws IOException {

        String actual = ImageUtil.resizeImageForPreview( new File( INVALID_IMAGE ), new File( TEST_IMAGE ) );
        Assert.assertEquals( ConstantsString.EMPTY_STRING, actual );
    }

    /**
     * Should get thumb file name as string if src file exist.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldGetThumbFileNameAsStringIfSrcFileExist() throws IOException {

        String actual = ImageUtil.resizeImageForPreview( SRC_FILE, new File( TEST_IMAGE ) );
        Assert.assertTrue( actual.contains( TEST_IMAGE ) );
    }

}

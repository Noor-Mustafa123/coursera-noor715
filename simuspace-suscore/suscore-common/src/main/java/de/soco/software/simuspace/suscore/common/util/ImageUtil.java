package de.soco.software.simuspace.suscore.common.util;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileExtension;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.EncryptionDecryptionDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The Class ImageUtil.
 *
 * @author Ahmar Hashmi
 *
 * A utility class to handle image operations
 */
@Log4j2
public final class ImageUtil {

    /**
     * Instantiates a new image util.
     */
    private ImageUtil() {
        super();
    }

    /**
     * A utility function to resize the image to a provided width and height.
     *
     * @param file
     *         the file
     * @param requiredWidth
     *         the required width
     * @param requiredHeight
     *         the required height
     *
     * @return the buffered image
     */
    public static BufferedImage resizeAndCrop( File file, int requiredWidth, int requiredHeight ) {

        if ( requiredWidth < ConstantsInteger.INTEGER_VALUE_ONE || requiredHeight < ConstantsInteger.INTEGER_VALUE_ONE ) {
            requiredWidth = ConstantsInteger.DEFAULT_IMAGE_WIDTH;
            requiredHeight = ConstantsInteger.DEFAULT_IMAGE_HEIGHT;
        }

        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read( file );
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_IMAGE.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_IMAGE.getKey() ) );
        }

        if ( inputImage == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.IMAGE_NOT_PROVIDED_TO_RESIZE.getKey() ) );
        }

        // first get the width and the height of the image
        int originWidth = inputImage.getWidth();
        int originHeight = inputImage.getHeight();

        // let us check if we have to scale the image
        if ( originWidth <= requiredWidth && originHeight <= requiredHeight ) {
            // we don't have to scale the image, just return the origin
            return inputImage;
        }

        // Scale in respect to width or height?
        Scalr.Mode scaleMode = Scalr.Mode.AUTOMATIC;

        // find out which side is the shortest
        int maxSize = ConstantsInteger.INTEGER_VALUE_ZERO;
        if ( originHeight > originWidth ) {
            // scale to width
            scaleMode = Scalr.Mode.FIT_TO_WIDTH;
            maxSize = requiredWidth;
        } else if ( originWidth >= originHeight ) {
            scaleMode = Scalr.Mode.FIT_TO_HEIGHT;
            maxSize = requiredHeight;
        }

        // Scale the image to given size
        BufferedImage outputImage = Scalr.resize( inputImage, Scalr.Method.QUALITY, scaleMode, maxSize );

        // okay, now let us check that both sides are fitting to our result scaling
        if ( scaleMode.equals( Scalr.Mode.FIT_TO_WIDTH ) && outputImage.getHeight() > requiredHeight ) {
            // the height is too large, resize again
            outputImage = Scalr.resize( outputImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, requiredHeight );
        } else if ( scaleMode.equals( Scalr.Mode.FIT_TO_HEIGHT ) && outputImage.getWidth() > requiredWidth ) {
            // the width is too large, resize again
            outputImage = Scalr.resize( outputImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, requiredWidth );
        }

        // now we have an image that is definitely equal or smaller to the given size
        // Now let us check, which side needs black lines
        int paddingSize = ConstantsInteger.INTEGER_VALUE_ZERO;
        if ( outputImage.getWidth() != requiredWidth ) {
            // we need padding on the width axis
            paddingSize = ( requiredWidth - outputImage.getWidth() ) / ConstantsInteger.INTEGER_VALUE_TWO;
        } else if ( outputImage.getHeight() != requiredHeight ) {
            // we need padding on the height axis
            paddingSize = ( requiredHeight - outputImage.getHeight() ) / ConstantsInteger.INTEGER_VALUE_TWO;
        }

        // we need padding?
        if ( paddingSize > ConstantsInteger.INTEGER_VALUE_ZERO ) {
            // add the padding to the image
            outputImage = Scalr.pad( outputImage, paddingSize, Color.white );

            // now we have to crop the image because the padding was added to all sides
            int x = ConstantsInteger.INTEGER_VALUE_ZERO;
            int y = ConstantsInteger.INTEGER_VALUE_ZERO;
            int width = ConstantsInteger.INTEGER_VALUE_ZERO;
            int height = ConstantsInteger.INTEGER_VALUE_ZERO;
            if ( outputImage.getWidth() > requiredWidth ) {
                // set the correct range
                x = paddingSize;
                y = ConstantsInteger.INTEGER_VALUE_ZERO;
                width = outputImage.getWidth() - ( ConstantsInteger.INTEGER_VALUE_TWO * paddingSize );
                height = outputImage.getHeight();
            } else if ( outputImage.getHeight() > requiredHeight ) {
                // set the correct range
                x = ConstantsInteger.INTEGER_VALUE_ZERO;
                y = paddingSize;
                width = outputImage.getWidth();
                height = outputImage.getHeight() - ( ConstantsInteger.INTEGER_VALUE_TWO * paddingSize );
            }

            // Crop the image
            if ( width > ConstantsInteger.INTEGER_VALUE_ZERO && height > ConstantsInteger.INTEGER_VALUE_ZERO ) {
                outputImage = Scalr.crop( outputImage, x, y, width, height );
            }
        }

        // flush both images
        inputImage.flush();
        // return the final image
        return outputImage;
    }

    /**
     * A utility function to convert the buffered image to the byte array.
     *
     * @param originalImage
     *         the original image
     * @param fileExtension
     *         the file extension
     *
     * @return the byte[]
     */
    public static byte[] toByteArray( BufferedImage originalImage, String fileExtension ) {
        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            ImageIO.write( originalImage, fileExtension, baos );
            return baos.toByteArray();
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_IMAGE.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_IMAGE.getKey() ) );
        }
    }

    /**
     * A utility function to convert the byte array to the image and return the URL of newly created image.
     *
     * @param profilePhoto
     *         the profile photo
     * @param userId
     *         the user id
     * @param filePath
     *         the file path
     * @param fileExtension
     *         the file extension
     *
     * @return the string
     */
    public static String writeImageBytes( byte[] profilePhoto, String userId, String filePath, String fileExtension ) {

        if ( StringUtils.isEmpty( fileExtension ) ) {
            fileExtension = ConstantsString.DEFAULT_IMAGE_TYPE;
        }

        File fileNameAndPath = new File( filePath + userId + ConstantsString.DOT + fileExtension );
        if ( !fileNameAndPath.exists() ) {
            fileNameAndPath.mkdir();
        }

        try ( InputStream in = new ByteArrayInputStream( profilePhoto ) ) {
            BufferedImage bImageFromConvert = ImageIO.read( in );
            ImageIO.write( bImageFromConvert, fileExtension, fileNameAndPath );
            return fileNameAndPath.toString();
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_IMAGE.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_IMAGE.getKey() ) );
        }
    }

    /**
     * A utility function to read the image from file and convert it to byte stream.
     *
     * @param file
     *         the file
     * @param fileExtension
     *         the file extension
     *
     * @return the byte[]
     */
    public static byte[] imageToByteArray( File file, String fileExtension ) {
        BufferedImage originalImage;
        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            originalImage = ImageIO.read( file );
            ImageIO.write( originalImage, fileExtension, baos );
            byte[] imageInByte = baos.toByteArray();
            originalImage.flush();
            return imageInByte;
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_IMAGE.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_IMAGE.getKey() ) );
        }
    }

    /**
     * Creates the thumb nail.
     *
     * @param srcFile
     *         the src file
     * @param thumbFile
     *         the thumb file
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public static String createThumbNail( File srcFile, File thumbFile, EncryptionDecryptionDTO encDec ) throws IOException {
        int width = 200;
        int height = 200;
        if ( srcFile.exists() ) {

            try ( InputStream decriptedStreamFromVault = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream( srcFile, encDec );
                    OutputStream outputStream = Files.newOutputStream( Paths.get( thumbFile.toURI() ) ) ) {

                createBasicThumbnail( srcFile, encDec, width, height, decriptedStreamFromVault, outputStream );
            }
        }
        return thumbFile.getPath();
    }

    private static void createBasicThumbnail( File srcFile, EncryptionDecryptionDTO encDec, int width, int height,
            InputStream decriptedStreamFromVault, OutputStream outputStream ) throws IOException {
        BufferedImage in = ImageIO.read( decriptedStreamFromVault );
        double outputAspect = 1.0 * width / height;
        double inputAspect = 1.0 * in.getWidth() / in.getHeight();
        if ( outputAspect < inputAspect ) {
            // width is limiting factor; adjust height to keep aspect
            height = ( int ) ( width / inputAspect );
        } else {
            // height is limiting factor; adjust width to keep aspect
            width = ( int ) ( height * inputAspect );
        }
        BufferedImage bi = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
        g2.drawImage( in, 0, 0, width, height, null );
        g2.dispose();
        ImageIO.write( bi, getExtensionFromObjectFile( EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream( srcFile, encDec ) ),
                outputStream );
    }

    /**
     * Creates the thumb nail.
     *
     * @param srcFile
     *         the src file
     * @param thumbFile
     *         the thumb file
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public static String createEncryptedThumbNail( File srcFile, File thumbFile, EncryptionDecryptionDTO endDec ) throws IOException {
        int width = 200;
        int height = 200;
        if ( srcFile.exists() ) {

            try ( InputStream decriptedStreamFromVault = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream( srcFile, endDec );
                    OutputStream outputStream = Files.newOutputStream(
                            Paths.get( PropertiesManager.getDefaultServerTempPath() + File.separator + thumbFile.getName() ) ) ) {

                createBasicThumbnail( srcFile, endDec, width, height, decriptedStreamFromVault, outputStream );
            }

            EncryptAndDecryptUtils.encryptStreamIfEncpEnabledAndSave(
                    new FileInputStream( PropertiesManager.getDefaultServerTempPath() + File.separator + thumbFile.getName() ), thumbFile );
        }
        return thumbFile.getPath();
    }

    public static String createThumbNailForProfilePhoto( File srcFile, File thumbFile ) throws IOException {
        int width = 200;
        int height = 200;
        if ( srcFile.exists() ) {
            BufferedImage in = ImageIO.read( Files.newInputStream( Paths.get( srcFile.toURI() ) ) );
            double outputAspect = 1.0 * width / height;
            double inputAspect = 1.0 * in.getWidth() / in.getHeight();
            if ( outputAspect < inputAspect ) {
                // width is limiting factor; adjust height to keep aspect
                height = ( int ) ( width / inputAspect );
            } else {
                // height is limiting factor; adjust width to keep aspect
                width = ( int ) ( height * inputAspect );
            }
            BufferedImage bi = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
            Graphics2D g2 = bi.createGraphics();
            g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
            g2.drawImage( in, 0, 0, width, height, null );
            g2.dispose();
            ImageIO.write( bi, getExtensionFromObjectFile( srcFile ), Files.newOutputStream( Paths.get( thumbFile.toURI() ) ) );
        }
        return thumbFile.getPath();
    }

    /**
     * Re size image for preview.
     *
     * @param srcFile
     *         the src file
     * @param thumbFile
     *         the thumb file
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public static String resizeImageForPreview( File srcFile, File thumbFile ) throws IOException {

        if ( srcFile.exists() ) {

            BufferedImage srcImg = ImageIO.read( srcFile );
            BufferedImage thumbImg = Scalr.resize( srcImg, Method.QUALITY, Mode.FIT_TO_WIDTH, 200, 187, Scalr.OP_ANTIALIAS );
            String extention = FilenameUtils.getExtension( srcFile.getPath() );
            List< String > extensionsMap = Arrays.asList( ConstantsFileExtension.JPG, ConstantsFileExtension.JPEG,
                    ConstantsFileExtension.PNG, ConstantsFileExtension.GIF );
            if ( !extensionsMap.contains( extention ) ) {
                extention = ConstantsFileExtension.JPG;
            }

            ImageIO.write( thumbImg, extention, thumbFile );

            srcImg.flush();
            return thumbFile.getPath();

        }
        return ConstantsString.EMPTY_STRING;
    }

    /**
     * Gets the extension from object file.
     *
     * @param file
     *         the file
     *
     * @return the extension from object file
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private static String getExtensionFromObjectFile( File file ) throws IOException {
        Tika tike = new Tika();
        String type = tike.detect( file );
        if ( type.contains( ConstantsFileExtension.JPEG ) || type.contains( ConstantsFileExtension.JPG ) ) {
            return ConstantsFileExtension.JPEG;
        } else if ( type.contains( ConstantsFileExtension.GIF ) ) {
            return ConstantsFileExtension.GIF;
        } else if ( type.contains( ConstantsFileExtension.PNG ) ) {
            return ConstantsFileExtension.PNG;
        }
        return ConstantsFileExtension.JPEG;
    }

    /**
     * Gets the extension from object file.
     *
     * @param file
     *         the file
     *
     * @return the extension from object file
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private static String getExtensionFromObjectFile( InputStream decriptedStream ) throws IOException {
        Tika tike = new Tika();
        String type = tike.detect( decriptedStream );
        if ( type.contains( ConstantsFileExtension.JPEG ) || type.contains( ConstantsFileExtension.JPG ) ) {
            return ConstantsFileExtension.JPEG;
        } else if ( type.contains( ConstantsFileExtension.GIF ) ) {
            return ConstantsFileExtension.GIF;
        } else if ( type.contains( ConstantsFileExtension.PNG ) ) {
            return ConstantsFileExtension.PNG;
        }
        return ConstantsFileExtension.JPEG;
    }

    /**
     * Creates the image by xy charts.
     *
     * @param strChartLabels
     *         the str chart labels
     * @param strFileFormate
     *         the str file formate
     * @param heigth
     *         the heigth
     * @param width
     *         the width
     * @param xyCoordinates
     *         the xy coordinates
     *
     * @return the byte[]
     */
    public static byte[] createImageByXyCharts( List< String > strChartLabels, String strFileFormate, Integer heigth, Integer width,
            List< List< double[] > > xyCoordinates ) {
        JFreeChart chart = null;
        byte[] bytes = null;
        XYDataset dataset = null;
        dataset = createMultiLinesDataset( xyCoordinates, strChartLabels );
        chart = prepareJFreeChart( "Objects Movement Chart", "X", "Y", dataset );
        try ( ByteArrayOutputStream baos1 = prepareByteArrayOutPutStream( strFileFormate, heigth, width, chart ) ) {
            if ( heigth == null || heigth < 1920 ) {
                return prepareThumbNailChart( heigth, width, dataset );
            }
            bytes = baos1.toByteArray();
        } catch ( IOException e ) {
            log.error( "Preparing thumbnail issue", e );
            throw new SusException( "Preparing thumbnail issue" );
        }
        return bytes;
    }

    /**
     * Creates the image by xy chart.
     *
     * @param strChartLabel
     *         the str_chart label
     * @param strxAxisLabel
     *         the str_x axis label
     * @param stryAxisLabel
     *         the str_y axis label
     * @param strFileFormat
     *         the str_file format
     * @param iHeight
     *         the i_height
     * @param iWidth
     *         the i_width
     * @param coordinates
     *         the co_ordinates
     *
     * @return the byte[]
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public static byte[] createImageByXyChart( String strChartLabel, String strxAxisLabel, String stryAxisLabel, String strFileFormat,
            Integer iHeight, Integer iWidth, List< double[] > coordinates ) throws IOException {
        XYDataset dataset = createDataset( coordinates, strChartLabel );
        if ( iHeight == null || iHeight < 1920 ) {
            return prepareThumbNailChart( iHeight, iWidth, dataset );
        }
        JFreeChart chart = prepareJFreeChart( strChartLabel, strxAxisLabel, stryAxisLabel, dataset );
        try ( ByteArrayOutputStream baos1 = prepareByteArrayOutPutStream( strFileFormat, iHeight, iWidth, chart ) ) {
            return baos1.toByteArray();
        }

    }

    /**
     * Prepare byte array out put stream.
     *
     * @param strFileFormat
     *         the str file format
     * @param iHeight
     *         the i height
     * @param iWidth
     *         the i width
     * @param chart
     *         the chart
     *
     * @return the byte array output stream
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private static ByteArrayOutputStream prepareByteArrayOutPutStream( String strFileFormat, Integer iHeight, Integer iWidth,
            JFreeChart chart ) throws IOException {

        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            if ( iHeight != null && iWidth != null ) {
                ChartUtils.writeChartAsJPEG( baos, chart, iHeight, iWidth );
            } else {
                ChartUtils.writeChartAsJPEG( baos, chart, 500, 300 );
            }

            /* set the quality level **/
            float quality = 0.5f;
            Iterator< ImageWriter > iter = ImageIO.getImageWritersByFormatName( strFileFormat );
            ImageWriter writer = iter.next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            iwp.setCompressionMode( ImageWriteParam.MODE_EXPLICIT );
            iwp.setCompressionQuality( quality );

            try ( ByteArrayInputStream inputStream = new ByteArrayInputStream( baos.toByteArray() );
                    MemoryCacheImageOutputStream output = new MemoryCacheImageOutputStream( baos1 ) ) {
                writer.setOutput( output );
                BufferedImage originalImage = ImageIO.read( inputStream );
                IIOImage image = new IIOImage( originalImage, null, null );
                writer.write( null, image, iwp );
                originalImage.flush();
            }
            writer.dispose();
        }

        return baos1;
    }

    /**
     * Prepare J free chart.
     *
     * @param strChartLabel
     *         the str chart label
     * @param strxAxisLabel
     *         the strx axis label
     * @param stryAxisLabel
     *         the stry axis label
     * @param dataset
     *         the dataset
     *
     * @return the j free chart
     */
    private static JFreeChart prepareJFreeChart( String strChartLabel, String strxAxisLabel, String stryAxisLabel, XYDataset dataset ) {
        JFreeChart chart = ChartFactory.createXYLineChart( strChartLabel, strxAxisLabel, stryAxisLabel, dataset );
        chart.getLegend().setVisible( false );
        chart.setBackgroundPaint( Color.WHITE );
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint( Color.white );
        plot.setRangePannable( true );
        plot.setRangeGridlinesVisible( true );
        plot.setDomainGridlinesVisible( true );
        plot.setDomainGridlineStroke( new BasicStroke( 1.0f ) );
        plot.setRangeGridlineStroke( new BasicStroke( 1.0f ) );
        plot.setDomainGridlinePaint( Color.GRAY.brighter() );
        plot.setRangeGridlinePaint( Color.GRAY.brighter() );
        plot.getRenderer().setSeriesPaint( 0, Color.GREEN );

        plot.setAxisOffset( new org.jfree.chart.ui.RectangleInsets( 4.0, 30.0, 30.0, 4.0 ) );
        // Sets Axis Font
        plot.getRangeAxis().setTickLabelFont( new Font( "Dialog", Font.PLAIN, 18 ) );
        plot.getDomainAxis().setTickLabelFont( new Font( "Dialog", Font.PLAIN, 18 ) );
        // Sets Label Font
        plot.getDomainAxis().setLabelFont( new Font( "Dialog", Font.PLAIN, 30 ) );
        plot.getRangeAxis().setLabelFont( new Font( "Dialog", Font.PLAIN, 30 ) );
        return chart;
    }

    /**
     * Prepare thumb nail chart.
     *
     * @param iHeight
     *         the i height
     * @param iWidth
     *         the i width
     * @param dataset
     *         the dataset
     *
     * @return the byte[]
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private static byte[] prepareThumbNailChart( Integer iHeight, Integer iWidth, XYDataset dataset ) throws IOException {
        byte[] bytes = null;
        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            JFreeChart thumbnailChart = chartForThumbnail( dataset );
            if ( iHeight != null && iWidth != null ) {
                ChartUtils.writeChartAsJPEG( baos, thumbnailChart, iHeight, iWidth );
            } else {
                ChartUtils.writeChartAsJPEG( baos, thumbnailChart, 500, 300 );
            }
            bytes = baos.toByteArray();
        }
        return bytes;
    }

    /**
     * Creates the dataset.
     *
     * @param coordinates
     *         the co_ordinates
     * @param strChartLabel
     *         the str_chart label
     *
     * @return the XY dataset
     */
    private static XYDataset createDataset( List< double[] > coordinates, String strChartLabel ) {

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = prepareXYSeries( coordinates, strChartLabel );
        dataset.addSeries( series1 );
        return dataset;
    }

    /**
     * Prepare XY series.
     *
     * @param coordinates
     *         the coordinates
     * @param strChartLabel
     *         the str chart label
     *
     * @return the XY series
     */
    private static XYSeries prepareXYSeries( List< double[] > coordinates, String strChartLabel ) {
        XYSeries series1 = new XYSeries( strChartLabel, false );

        for ( double[] xyCoOrdinates : coordinates ) {
            double xCoordiate = xyCoOrdinates[ 0 ];
            double yCoordiate = xyCoOrdinates[ 1 ];
            series1.add( xCoordiate, yCoordiate );
        }
        return series1;
    }

    /**
     * Prepare XY series with differnet comparable.
     *
     * @param coordinates
     *         the coordinates
     * @param strChartLabel
     *         the str chart label
     * @param j
     *         the j
     *
     * @return the XY series
     */
    private static XYSeries prepareXYSeriesWithDiffernetComparable( List< double[] > coordinates, String strChartLabel, int j ) {
        XYSeries series1 = new XYSeries( strChartLabel + Integer.toString( j ), false );

        for ( double[] xyCoOrdinates : coordinates ) {
            double xCoordiate = xyCoOrdinates[ 0 ];
            double yCoordiate = xyCoOrdinates[ 1 ];
            series1.add( xCoordiate, yCoordiate );
        }
        return series1;
    }

    /**
     * Creates the multi lines dataset.
     *
     * @param xyCoordinates
     *         the xy coordinates
     * @param strChartLabels
     *         the str chart labels
     *
     * @return the XY dataset
     */
    private static XYDataset createMultiLinesDataset( List< List< double[] > > xyCoordinates, List< String > strChartLabels ) {
        try {
            XYSeriesCollection dataset = new XYSeriesCollection();
            for ( int i = 0; i < xyCoordinates.size(); i++ ) {
                XYSeries series = prepareXYSeriesWithDiffernetComparable( xyCoordinates.get( i ), strChartLabels.get( i ), i );
                dataset.addSeries( series );
            }
            return dataset;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getLocalizedMessage() );
        }
    }

    /**
     * Chart for thumbnail.
     *
     * @param dataset
     *         the dataset
     *
     * @return the j free chart
     */
    private static JFreeChart chartForThumbnail( XYDataset dataset ) {
        JFreeChart chart = ChartFactory.createXYLineChart( "", "", "", dataset );
        XYPlot plot = chart.getXYPlot();
        ValueAxis range = plot.getRangeAxis();
        range.setVisible( false );
        ValueAxis domain = plot.getDomainAxis();
        domain.setVisible( false );
        chart.getLegend().setVisible( false );
        chart.setBackgroundPaint( Color.WHITE );
        plot.setBackgroundPaint( Color.white );
        plot.setRangePannable( true );
        plot.setRangeGridlinesVisible( true );
        plot.setDomainGridlinesVisible( true );
        plot.setDomainGridlineStroke( new BasicStroke( 1.0f ) );
        plot.setRangeGridlineStroke( new BasicStroke( 1.0f ) );
        plot.setDomainGridlinePaint( Color.GRAY.brighter() );
        plot.setRangeGridlinePaint( Color.GRAY.brighter() );
        plot.getRenderer().setSeriesPaint( 0, Color.GREEN );

        return chart;

    }

}
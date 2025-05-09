package de.soco.software.simuspace.suscore.data.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.data.entity.DataObjectTraceEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObjectTraceDTO.
 *
 * @author noman arshad
 */
@Log4j2
@JsonIgnoreProperties( ignoreUnknown = true )
public class DataObjectTraceDTO extends DataObjectFileDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -1123048392666515662L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final DataObjectTraceEntity ENTITY_CLASS = new DataObjectTraceEntity();

    /**
     * The layout.
     */
    private Object layout;

    /**
     * The data.
     */
    private ArrayList< Object > data = new ArrayList<>();

    /**
     * The plot type.
     */
    private String plotType;

    /**
     * The plot.
     */
    private String plot;

    /**
     * The file.
     */
    @UIFormField( name = "templateFile", title = "0300073x4", type = "file-upload", multiple = false, orderNum = 8 )
    private DocumentDTO templateFile;

    /**
     * Instantiates a new data object trace DTO.
     *
     * @param layout
     *         the layout
     */
    public DataObjectTraceDTO( Object layout ) {
        super();
        this.layout = layout;
    }

    /**
     * Instantiates a new data object trace DTO.
     */
    public DataObjectTraceDTO() {
        super();
    }

    /**
     * Gets the layout.
     *
     * @return the layout
     */
    public Object getLayout() {
        return layout;
    }

    /**
     * Sets the layout.
     *
     * @param layout
     *         the new layout
     */
    public void setLayout( Object layout ) {
        this.layout = layout;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public ArrayList< Object > getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the new data
     */
    public void setData( ArrayList< Object > data ) {
        this.data = data;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "[layout=" + layout + ", data=" + data + "]";
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( data == null ) ? 0 : data.hashCode() );
        result = prime * result + ( ( layout == null ) ? 0 : layout.hashCode() );
        return result;
    }

    /**
     * Equals.
     *
     * @param obj
     *         the obj
     *
     * @return true, if successful
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( !super.equals( obj ) ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        DataObjectTraceDTO other = ( DataObjectTraceDTO ) obj;
        if ( data == null ) {
            if ( other.data != null ) {
                return false;
            }
        } else if ( !data.equals( other.data ) ) {
            return false;
        }
        if ( layout == null ) {
            if ( other.layout != null ) {
                return false;
            }
        } else if ( !layout.equals( other.layout ) ) {
            return false;
        }
        return true;
    }

    /**
     * Prepare entity.
     *
     * @param userId
     *         the user id
     *
     * @return the data object trace entity
     */
    @Override
    public DataObjectTraceEntity prepareEntity( String userId ) {

        DataObjectTraceEntity dataObjectEntity = new DataObjectTraceEntity();

        dataObjectEntity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        Date now = new Date();
        dataObjectEntity.setCreatedOn( now );
        dataObjectEntity.setModifiedOn( now );
        dataObjectEntity.setName( getName() );
        dataObjectEntity.setDescription( getDescription() );
        dataObjectEntity.setTypeId( getTypeId() );
        dataObjectEntity.setPlot( ByteUtil.convertStringToByte( getPlot() ) );
        dataObjectEntity.setPlotType( getPlotType() );
        dataObjectEntity
                .setCustomAttributes( prepareCustomAttributes( dataObjectEntity, getCustomAttributes(), getCustomAttributesDTO() ) );

        // set Status From lifeCycle
        dataObjectEntity.setLifeCycleStatus( ( getLifeCycleStatus() != null ) ? getLifeCycleStatus().getId() : null );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( userId ) );
        dataObjectEntity.setOwner( userEntity );

        if ( getFile() != null ) {
            DocumentEntity documentEntity = prepareDocumentEntity();
            dataObjectEntity.setFile( documentEntity );
            dataObjectEntity.setSize( documentEntity.getSize() );

            if ( getTemplateFile() != null && getTemplateFile().getPath() != null ) {
                dataObjectEntity.setPlot( ByteUtil.convertStringToByte( getTraceSettingsFromSusttFile() ) );
            }
        } else {
            dataObjectEntity.setSize( null );
        }

        dataObjectEntity.setJobId( getJobId() );
        return dataObjectEntity;

    }

    /**
     * Get trace Settings from sustt file.
     *
     * @return the trace settings
     */
    private String getTraceSettingsFromSusttFile() {

        File susttFile = new File( PropertiesManager.getVaultPath() + getTemplateFile().getPath() );
        if ( susttFile.exists() ) {
            try ( InputStream decriptStream = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream( susttFile,
                    getTemplateFile().getEncryptionDecryption() ) ) {

                String[] headersCsv = getCsvHeaderArrayByPath();

                JSONParser parser = new JSONParser();
                Object obj = parser.parse( new InputStreamReader( decriptStream ) );
                JSONObject jsonObject = ( JSONObject ) obj;
                if ( jsonObject.containsKey( "data" ) ) {
                    JSONArray dataArray = ( JSONArray ) jsonObject.get( "data" );
                    for ( Object object : dataArray ) {
                        JSONObject dataIteration = ( JSONObject ) object;
                        if ( dataIteration.containsKey( "x" ) ) {
                            String x = ( String ) dataIteration.get( "x" );
                            if ( !findHeaderInArray( headersCsv, x ) ) {
                                jsonObject.put( "x", "" );
                            }
                        }
                        if ( dataIteration.containsKey( "y" ) ) {
                            String y = ( String ) dataIteration.get( "y" );
                            if ( !findHeaderInArray( headersCsv, y ) ) {
                                jsonObject.put( "y", "" );
                            }
                        }
                    }
                }

                if ( susttFile.exists() ) {
                    susttFile.delete();
                }

                return jsonObject.toString();

            } catch ( Exception e ) {
                log.error( "Trace object sustt file plot error :" + e.getMessage(), e );
            }
        }

        return null;
    }

    /**
     * Find header in array.
     *
     * @param headersCsv
     *         the headers csv
     * @param x
     *         the x
     *
     * @return true, if successful
     */
    private boolean findHeaderInArray( String[] headersCsv, String x ) {
        for ( String header : headersCsv ) {
            if ( header.equalsIgnoreCase( x.replace( "__", "" ) ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the csv header array by path.
     *
     * @param csvFile
     *         the csv file
     *
     * @return the csv header array by path
     *
     * @throws FileNotFoundException
     *         the file not found exception
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private String[] getCsvHeaderArrayByPath() throws IOException {
        File csvFile = new File( PropertiesManager.getVaultPath() + getFile().getPath() );
        try ( BufferedReader br = new BufferedReader( new InputStreamReader(
                EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream( csvFile, getFile().getEncryptionDecryption() ) ) ) ) {

            String header = br.readLine();
            if ( header != null ) {
                return header.split( "," );
            }
        }

        return null;

    }

    /**
     * Gets the plot type.
     *
     * @return the plot type
     */
    public String getPlotType() {
        return plotType;
    }

    /**
     * Sets the plot type.
     *
     * @param plotType
     *         the new plot type
     */
    public void setPlotType( String plotType ) {
        this.plotType = plotType;
    }

    /**
     * Gets the plot.
     *
     * @return the plot
     */
    public String getPlot() {
        return plot;
    }

    /**
     * Sets the plot.
     *
     * @param plot
     *         the new plot
     */
    public void setPlot( String plot ) {
        this.plot = plot;
    }

    /**
     * Gets the template file.
     *
     * @return the template file
     */
    public DocumentDTO getTemplateFile() {
        return templateFile;
    }

    /**
     * Sets the template file.
     *
     * @param template
     *         file the template file
     */
    public void setTemplateFile( DocumentDTO templateFile ) {
        this.templateFile = templateFile;
    }

}

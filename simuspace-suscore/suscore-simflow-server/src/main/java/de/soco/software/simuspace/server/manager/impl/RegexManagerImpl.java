package de.soco.software.simuspace.server.manager.impl;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.dao.RegexDAO;
import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.manager.RegexManager;
import de.soco.software.simuspace.server.manager.WFSchemeManager;
import de.soco.software.simuspace.server.model.RegexScanDTO;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.BmwCaeBenchEnums;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.VariableDropDownEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.formitem.impl.TableFormItem;
import de.soco.software.simuspace.suscore.common.model.ObjectVariableDTO;
import de.soco.software.simuspace.suscore.common.model.ScanFileDTO;
import de.soco.software.simuspace.suscore.common.model.ScanResponseDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.BmwCaeBenchDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.RegexEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.document.dao.DocumentDAO;

/**
 * The Class RegexManagerImpl.
 */
@Log4j2
public class RegexManagerImpl extends BaseManager implements RegexManager {

    /**
     * The regex DAO.
     */
    private RegexDAO regexDAO;

    /**
     * The wf scheme manager.
     */
    private WFSchemeManager wfSchemeManager;

    /**
     * The bmw cae bench DAO.
     */
    private BmwCaeBenchDAO bmwCaeBenchDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Constant REQUIRED.
     */
    private static final String REQUIRED = "required";

    /**
     * The Constant MUST_CHOSE_OPTION.
     */
    private static final String MUST_CHOSE_OPTION = "Must Chose Option";

    /**
     * The Constant REGEX_OBJECT_VALUE.
     */
    private static final String REGEX_OBJECT_VALUE = "regexobject.value";

    /**
     * The Constant VALUE.
     */
    private static final String VALUE = "value";

    /**
     * The Constant REGEX_OBJECT.
     */
    private static final String REGEX_OBJECT = "regexobject";

    /**
     * The Constant EXTERNAL.
     */
    private static final String EXTERNAL = "external";

    /**
     * {@inheritDoc}
     */
    @Override
    public RegexScanDTO createRegex( UUID selectionId, RegexScanDTO regexScanDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Notification notification = regexScanDTO.validate();
            if ( notification.hasErrors() ) {
                throw new SusException( notification.getErrors().toString() );
            }
            RegexEntity regexEntity = prepareRegexEntityToCreate( selectionId, regexScanDTO );
            RegexEntity saveRegexEntity = regexDAO.saveRegex( entityManager, regexEntity );
            return prepareRegexScanDTO( saveRegexEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RegexScanDTO updateRegex( UUID id, RegexScanDTO regexScanDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Notification notification = regexScanDTO.validate();
            if ( notification.hasErrors() ) {
                throw new SusException( notification.getErrors().toString() );
            }

            RegexEntity regexEntity = regexDAO.getRegex( entityManager, id );
            regexEntity = prepareRegexEntityToUpdate( regexEntity, regexScanDTO );
            regexEntity = regexDAO.updateRegex( entityManager, regexEntity );
            return prepareRegexScanDTO( regexEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RegexScanDTO getRegex( UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getRegex( entityManager, id );
        } finally {
            entityManager.close();
        }
    }

    private RegexScanDTO getRegex( EntityManager entityManager, UUID id ) {
        return prepareRegexScanDTO( regexDAO.getRegex( entityManager, id ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< RegexScanDTO > getRegexList( UUID selectionId, FiltersDTO filter ) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< RegexEntity > regexEntities = regexDAO.getRegexListBySelectionId( entityManager, selectionId, filter );
            List< RegexScanDTO > regexScanDTOs = new ArrayList<>();
            if ( CollectionUtils.isNotEmpty( regexEntities ) ) {
                for ( RegexEntity regexEntity : regexEntities ) {
                    regexScanDTOs.add( prepareRegexScanDTO( regexEntity ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, regexScanDTOs );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean deleteRegex( List< String > ids ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return deleteRegex( entityManager, ids );
        } finally {
            entityManager.close();
        }
    }

    private Boolean deleteRegex( EntityManager entityManager, List< String > ids ) {
        ids.stream().forEach( id -> regexDAO.deleteRegex( entityManager, regexDAO.getRegex( entityManager, UUID.fromString( id ) ) ) );
        return Boolean.TRUE;
    }

    /**
     * Prepare regex scan DTO.
     *
     * @param regexEntity
     *         the regex entity
     *
     * @return the regex scan DTO
     */
    private RegexScanDTO prepareRegexScanDTO( RegexEntity regexEntity ) {
        RegexScanDTO regexScanDTO = null;
        if ( regexEntity != null ) {
            regexScanDTO = new RegexScanDTO();
            regexScanDTO.setId( regexEntity.getId() );
            regexScanDTO.setVariableName( regexEntity.getVariableName() );
            regexScanDTO.setLineRegex( regexEntity.getLineRegex() );
            regexScanDTO.setLineMatch( regexEntity.getLineMatch() );
            regexScanDTO.setLineOffset( regexEntity.getLineOffset() );
            regexScanDTO.setVariableRegex( regexEntity.getVariableRegex() );
            regexScanDTO.setVariableMatch( regexEntity.getVariableMatch() );
            regexScanDTO.setVariableGroup( regexEntity.getVariableGroup() );
            regexScanDTO.setScannedLine( regexEntity.getScannedLine() );
            regexScanDTO.setScannedValue( regexEntity.getScannedValue() );
        }
        return regexScanDTO;
    }

    /**
     * Prepare regex entity to create.
     *
     * @param selectionId
     *         the workflow id
     * @param regexScanDTO
     *         the regex scan DTO
     *
     * @return the regex entity
     */
    private RegexEntity prepareRegexEntityToCreate( UUID selectionId, RegexScanDTO regexScanDTO ) {
        RegexEntity regexEntity = null;
        if ( regexScanDTO != null ) {
            regexEntity = prepareRegexEntity( UUID.randomUUID(), regexScanDTO.getVariableName(), regexScanDTO.getLineRegex(),
                    regexScanDTO.getLineMatch(), regexScanDTO.getLineOffset(), regexScanDTO.getVariableRegex(),
                    regexScanDTO.getVariableMatch(), regexScanDTO.getVariableGroup(), selectionId );
        }
        return regexEntity;
    }

    /**
     * Prepare regex entity to update.
     *
     * @param regexEntityFromDB
     *         the regex entity from DB
     * @param regexScanDTO
     *         the regex scan DTO
     *
     * @return the regex entity
     */
    private RegexEntity prepareRegexEntityToUpdate( RegexEntity regexEntityFromDB, RegexScanDTO regexScanDTO ) {
        if ( regexEntityFromDB != null ) {
            if ( regexScanDTO.getVariableName() != null ) {
                regexEntityFromDB.setVariableName( regexScanDTO.getVariableName() );
            }
            if ( regexScanDTO.getLineRegex() != null ) {
                regexEntityFromDB.setLineRegex( regexScanDTO.getLineRegex() );
            }
            if ( regexScanDTO.getLineMatch() != null ) {
                regexEntityFromDB.setLineMatch( regexScanDTO.getLineMatch() );
            }
            if ( regexScanDTO.getLineOffset() != null ) {
                regexEntityFromDB.setLineOffset( regexScanDTO.getLineOffset() );
            }
            if ( regexScanDTO.getVariableRegex() != null ) {
                regexEntityFromDB.setVariableRegex( regexScanDTO.getVariableRegex() );
            }
            if ( regexScanDTO.getVariableMatch() != null ) {
                regexEntityFromDB.setVariableMatch( regexScanDTO.getVariableMatch() );
            }
            if ( regexScanDTO.getVariableGroup() != null ) {
                regexEntityFromDB.setVariableGroup( regexScanDTO.getVariableGroup() );
            }
        }
        return regexEntityFromDB;
    }

    /**
     * Prepare regex entity.
     *
     * @param id
     *         the id
     * @param variableName
     *         the variable name
     * @param lineRegex
     *         the line regex
     * @param lineMatch
     *         the line match
     * @param lineOffset
     *         the line offset
     * @param variableRegex
     *         the variable regex
     * @param variableMatch
     *         the variable match
     * @param variableGroup
     *         the variable group
     * @param selectionId
     *         the workflow id
     *
     * @return the regex entity
     */
    private RegexEntity prepareRegexEntity( UUID id, String variableName, String lineRegex, String lineMatch, String lineOffset,
            String variableRegex, String variableMatch, String variableGroup, UUID selectionId ) {
        RegexEntity regexEntity = new RegexEntity();
        regexEntity.setId( id );
        regexEntity.setVariableName( variableName );
        regexEntity.setLineRegex( lineRegex );
        regexEntity.setLineMatch( lineMatch );
        regexEntity.setLineOffset( lineOffset );
        regexEntity.setVariableRegex( variableRegex );
        regexEntity.setVariableMatch( variableMatch );
        regexEntity.setVariableGroup( variableGroup );
        regexEntity.setSelectionId( selectionId );
        return regexEntity;
    }

    /**
     * {@inheritDoc}
     */
    // step 2
    @Override
    public Object getRegexEdit( String userId, String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String selectorValue = null;

            try {
                UUID.fromString( selectionId );
            } catch ( IllegalArgumentException exception ) {
                throw new SusException( "Selection is not a valid UUID!" );
            }

            SelectionEntity selectionEntis = selectionManager.getSelectionDAO()
                    .getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( selectionId ) );
            if ( selectionEntis == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_NOT_AVAILIABLE.getKey() ) );
            } else {

                Map< String, Object > parameter = new HashMap<>();
                parameter = ( Map< String, Object > ) JsonUtils.jsonToMap( ByteUtil.convertByteToString( selectionEntis.getJson() ),
                        parameter );

                Map< String, Object > parameterObject = new HashMap<>();
                if ( parameter.containsKey( REGEX_OBJECT ) ) {
                    parameterObject = ( Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.toJson( parameter.get( REGEX_OBJECT ) ),
                            parameterObject );
                }

                if ( parameterObject.get( TYPE ) != null && (
                        parameterObject.get( TYPE ).toString().equalsIgnoreCase( VariableDropDownEnum.SERVER.getId() )
                                || parameterObject.get( TYPE ).toString().equalsIgnoreCase( VariableDropDownEnum.LOCAL.getId() )
                                || parameterObject.get( TYPE ).toString().equalsIgnoreCase( VariableDropDownEnum.CB2.getId() )
                                || parameterObject.get( TYPE ).toString().equalsIgnoreCase( VariableDropDownEnum.INTERNAL.getId() ) ) ) {
                    selectorValue = parameterObject.get( TYPE ).toString();
                }

            }
            List< UIFormItem > result = new ArrayList<>();
            result.add( prepareObjectHiddenForm( selectionId ) );
            result.add( prepareObjectTypeForm( selectionId, selectorValue ) );
            return result;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getFileContent( String userId, String userUID, UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ScanFileDTO scanFileDTO;
            List< ObjectVariableDTO > objectVariableDTOs;
            List< Object > fileScannedContent = new ArrayList<>();
            String fullPath = getFullPathContent( entityManager, userUID, id );
            List< RegexEntity > regexEntities = regexDAO.getRegexListBySelectionId( entityManager, id );

            if ( CollectionUtils.isNotEmpty( regexEntities ) ) {
                objectVariableDTOs = new ArrayList<>();
                for ( RegexEntity regexEntity : regexEntities ) {
                    objectVariableDTOs.add( prepareObjectVariableDTO( regexEntity ) );
                }
                scanFileDTO = new ScanFileDTO();
                scanFileDTO.setFile( fullPath );
                scanFileDTO.setVariables( objectVariableDTOs );

                List< ScanResponseDTO > scanResponseDTOsIndexed = wfSchemeManager.scanObjectiveFileFromPath( userId, scanFileDTO );

                if ( CollectionUtils.isNotEmpty( scanResponseDTOsIndexed ) ) {
                    for ( int i = 0; i < scanResponseDTOsIndexed.size(); i++ ) {
                        ScanResponseDTO indexedObj = scanResponseDTOsIndexed.get( i );
                        ObjectVariableDTO indexedMap = objectVariableDTOs.get( i );
                        RegexEntity entity = regexDAO.getRegex( entityManager, indexedMap.getId() );
                        entity.setScannedLine( indexedObj.getLineNumberIndexed() );
                        entity.setScannedValue( indexedObj.getMatch() );
                        entity.setEnd( indexedObj.getEnd() );
                        entity.setStart( indexedObj.getStart() );
                        regexDAO.updateRegex( entityManager, entity );
                    }

                    fileScannedContent = prepareScanContent( scanFileDTO, scanResponseDTOsIndexed.stream().collect( collectingAndThen(
                            toCollection( () -> new TreeSet<>(
                                    Comparator.comparing( ScanResponseDTO::getStart, Comparator.nullsLast( Comparator.naturalOrder() ) )
                                            .thenComparing( ScanResponseDTO::getEnd, Comparator.nullsLast( Comparator.naturalOrder() ) )
                                            .thenComparing( ScanResponseDTO::getLineNumber,
                                                    Comparator.nullsLast( Comparator.naturalOrder() ) ) ) ), ArrayList::new ) ) );
                    if ( CollectionUtils.isEmpty( fileScannedContent ) ) {
                        fileScannedContent = getRequiredCharIfOnlyFileSelected( fullPath );
                    }
                }
            } else if ( fullPath != null ) {
                fileScannedContent = getRequiredCharIfOnlyFileSelected( fullPath );
            }

            return fileScannedContent;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the full path content.
     *
     * @param entityManager
     *         the entity manager
     * @param userUID
     *         the user UID
     * @param id
     *         the id
     *
     * @return the full path content
     */
    private String getFullPathContent( EntityManager entityManager, String userUID, UUID id ) {
        JsonNode selectedJson = getJson( entityManager, id );
        JsonNode regexobjectNode = selectedJson.get( REGEX_OBJECT );
        String fullPath = null;
        JsonNode selectedValue = null;
        if ( regexobjectNode != null && regexobjectNode.has( VALUE ) ) {
            selectedValue = regexobjectNode.get( VALUE );

            if ( regexobjectNode.get( TYPE ).asText().equalsIgnoreCase( VariableDropDownEnum.LOCAL.getId() ) ) {
                fullPath = getFilePathContent( entityManager, UUID.fromString( selectedValue.get( ID ).asText() ) );
            } else if ( regexobjectNode.get( TYPE ).asText().equalsIgnoreCase( VariableDropDownEnum.INTERNAL.getId() ) ) {
                List< UUID > selectedItemsList = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectedValue.asText() );
                if ( selectedItemsList != null && !selectedItemsList.isEmpty() ) {
                    fullPath = getFilePathContent( entityManager, selectedItemsList.get( 0 ) );
                }
            } else if ( regexobjectNode.get( TYPE ).asText().equalsIgnoreCase( VariableDropDownEnum.SERVER.getId() ) ) {
                if ( selectedValue.isArray() ) {
                    for ( JsonNode jsonNode : selectedValue ) {
                        fullPath = jsonNode.get( FULL_PATH ).asText();
                    }
                }
            } else if ( regexobjectNode.get( TYPE ).asText().equalsIgnoreCase( VariableDropDownEnum.CB2.getId() ) ) {
                fullPath = getFullPathContentOfCb2Object( entityManager, userUID, fullPath, selectedValue.asText() );
            }
        }
        return fullPath;
    }

    /**
     * Gets full path content of cb 2 object.
     *
     * @param entityManager
     *         the entity manager
     * @param userUID
     *         the user uid
     * @param fullPath
     *         the full path
     * @param selectedValue
     *         the selected value
     *
     * @return the full path content of cb 2 object
     */
    private String getFullPathContentOfCb2Object( EntityManager entityManager, String userUID, String fullPath, String selectedValue ) {
        List< String > cb2OidList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, selectedValue );
        if ( cb2OidList != null && !cb2OidList.isEmpty() ) {
            String cb2OID = cb2OidList.get( 0 );
            BmwCaeBenchEntity cb2ObjectEntity = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( cb2OID ) );
            fullPath = downloadCB2FileAndReturnPath( userUID, cb2ObjectEntity );
        }
        return fullPath;
    }

    /**
     * Gets the file path.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the file path
     */
    private String getFilePathContent( EntityManager entityManager, UUID id ) {
        DocumentEntity documentEntity = getDocumentObjectByObjectId( entityManager, id );
        if ( documentEntity == null ) {
            throw new SusException( "Object not available" );
        }
        String selectedFilePath = PropertiesManager.getVaultPath() + documentEntity.getFilePath();
        File selectedFileObj = new File(
                PropertiesManager.getDefaultServerTempPath() + File.separator + UUID.randomUUID() + documentEntity.getFileName()
                        .replaceAll( "\\s", "_" ) );
        try ( InputStream decritedStreamDromVault = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream(
                new File( selectedFilePath ), prepareEncryptionDecryptionDTO( documentEntity.getEncryptionDecryption() ) ) ) {
            Files.copy( decritedStreamDromVault, selectedFileObj.toPath() );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
        return selectedFileObj.getAbsolutePath();
    }

    /**
     * Prepare object variable DTO.
     *
     * @param regexEntity
     *         the regex entity
     *
     * @return the object variable DTO
     */
    private ObjectVariableDTO prepareObjectVariableDTO( RegexEntity regexEntity ) {
        ObjectVariableDTO objectVariableDTO = null;
        if ( regexEntity != null ) {
            objectVariableDTO = new ObjectVariableDTO();
            objectVariableDTO.setId( regexEntity.getId() );
            objectVariableDTO.setVariableName( regexEntity.getVariableName() );
            objectVariableDTO.setLineRegex( regexEntity.getLineRegex() );
            objectVariableDTO.setLineMatch( regexEntity.getLineMatch() );
            objectVariableDTO.setLineOffset( regexEntity.getLineOffset() );
            objectVariableDTO.setVariableRegex( regexEntity.getVariableRegex() );
            objectVariableDTO.setVariableMatch( regexEntity.getVariableMatch() );
            objectVariableDTO.setVariableGroup( regexEntity.getVariableGroup() );

            ScanResponseDTO ScanResponseDTO = new ScanResponseDTO();
            ScanResponseDTO.setLineNumber( regexEntity.getScannedLine() );
            ScanResponseDTO.setMatch( regexEntity.getScannedValue() );
            ScanResponseDTO.setStart( regexEntity.getStart() );
            ScanResponseDTO.setEnd( regexEntity.getEnd() );
            objectVariableDTO.setHighlight( ScanResponseDTO );
        }
        return objectVariableDTO;
    }

    /**
     * Gets the required char if only file selected.
     *
     * @param fullPath
     *         the full path
     *
     * @return the required char if only file selected
     */
    private List< Object > getRequiredCharIfOnlyFileSelected( String fullPath ) {
        List< Object > fileScannedContent = new ArrayList<>();
        Scanner scanner = new Scanner( FileUtils.getFileFirstRequiredCharContent( fullPath, REQUIRED_CHARACTER ) );
        int scannerLine = 0;
        while ( scanner.hasNextLine() ) {
            String line = scanner.nextLine();
            List< Object > numberOflines = new ArrayList<>();
            numberOflines.add( line );
            List< ScanResponseDTO > responseDTOs = new ArrayList<>();
            numberOflines.add( responseDTOs );
            numberOflines.add( ( scannerLine + 1 ) );
            Collections.reverse( numberOflines );
            fileScannedContent.add( numberOflines );
            scannerLine++;
        }
        scanner.close();
        return fileScannedContent;
    }

    /**
     * Prepare scan content.
     *
     * @param scanFileDTO
     *         the scan file DTO
     * @param scanResponseDTOs
     *         the scan response DT os
     *
     * @return the list
     */
    private List< Object > prepareScanContent( ScanFileDTO scanFileDTO, List< ScanResponseDTO > scanResponseDTOs ) {
        List< Object > fileScannedContent = new ArrayList<>();
        Integer selection = Integer.valueOf( PropertiesManager.getRegexContextLine().trim() );
        List< Integer > scannedLine = new ArrayList<>();
        List< String > contents;
        try {
            contents = org.apache.commons.io.FileUtils.readLines( new File( scanFileDTO.getFile() ), "UTF-8" );
            for ( int scannerLine = 0; scannerLine <= contents.size(); scannerLine++ ) {
                for ( ScanResponseDTO responseDTO : scanResponseDTOs ) {
                    if ( null != responseDTO.getStart() && scannerLine == Integer.parseInt( responseDTO.getLineNumberIndexed() ) ) {
                        scannedLine.add( scannerLine );
                    }
                }
            }
        } catch ( IOException | NumberFormatException e ) {
            return getRequiredCharIfOnlyFileSelected( scanFileDTO.getFile() );

        }

        List< List< Integer > > contexts = prepareContext( selection, scannedLine, contents.size() );

        if ( CollectionUtils.isNotEmpty( contexts ) ) {

            for ( List< Integer > list : contexts ) {

                for ( Integer integer : list ) {
                    List< Object > objects2 = new ArrayList<>();
                    // line string
                    objects2.add( contents.get( integer - 1 ) );
                    List< ScanResponseDTO > responseDTOs = new ArrayList<>();
                    for ( ScanResponseDTO responseDTO : scanResponseDTOs ) {
                        if ( null != responseDTO.getStart() && integer == Integer.parseInt( responseDTO.getLineNumberIndexed() ) ) {
                            responseDTO.setLineNumber( responseDTO.getLineNumberIndexed() );
                            responseDTOs.add( responseDTO );
                        }
                    }
                    // match content
                    objects2.add( responseDTOs );
                    // line number
                    objects2.add( integer );
                    Collections.reverse( objects2 );
                    fileScannedContent.add( objects2 );
                }
            }
        }
        return fileScannedContent;
    }

    /**
     * Prepare context.
     *
     * @param selection
     *         the selection
     * @param scannedLine
     *         the scanned line
     * @param lastLine
     *         the last line
     *
     * @return the list
     */
    private List< List< Integer > > prepareContext( Integer selection, List< Integer > scannedLine, Integer lastLine ) {
        List< List< Integer > > contexts = null;
        if ( CollectionUtils.isNotEmpty( scannedLine ) ) {
            contexts = new ArrayList<>();
            Integer lastScannedLine = scannedLine.get( scannedLine.size() - 1 );
            Integer _previous = 0;
            for ( Integer i = 0; i < scannedLine.size(); i++ ) {
                List< Integer > context = new ArrayList<>();
                Integer sl = scannedLine.get( i );
                if ( sl >= 1 ) {

                    int countBeforeScanned = 0;

                    if ( _previous + 1 == sl ) {

                    } else {
                        for ( Integer j = sl - 1; j > _previous; j-- ) {
                            if ( countBeforeScanned == selection ) {
                                break;
                            } else {
                                context.add( j );
                            }
                            countBeforeScanned++;
                        }
                    }

                }

                Integer _next = 0;

                if ( i + 1 < scannedLine.size() ) {
                    _next = scannedLine.get( i + 1 );

                    Integer countAfterScanned = 0;
                    for ( Integer k = sl + 1; k < _next; k++ ) {
                        if ( countAfterScanned == selection ) {
                            break;
                        } else {
                            context.add( k );
                        }
                        countAfterScanned++;

                    }

                } else if ( sl == lastScannedLine ) {

                    if ( lastScannedLine == lastLine ) {

                    } else {
                        _next = sl + selection;
                        int countAfterScanned = 0;
                        for ( Integer k = sl + 1; k <= _next; k++ ) {
                            if ( countAfterScanned == selection ) {
                                break;
                            } else {
                                if ( k <= lastLine ) {
                                    context.add( k );
                                }
                            }
                            countAfterScanned++;

                        }
                    }

                }

                context.add( sl );
                Collections.sort( context );
                _previous = context.get( context.size() - 1 );
                contexts.add( context );
            }
        }
        return contexts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getRegexUi( String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            try {
                UUID.fromString( id );
            } catch ( IllegalArgumentException exception ) {
                throw new SusException( "Selection is not a valid UUID!" );
            }

            SelectionEntity selectionEntity = selectionManager.getSelectionDAO()
                    .getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( id ) );
            if ( selectionEntity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_NOT_AVAILIABLE.getKey() ) );
            }

            return GUIUtils.listColumns( RegexScanDTO.class );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare object type form.
     *
     * @param wfId
     *         the wf id
     * @param value
     *         the value
     *
     * @return the UI form item
     */
    private UIFormItem prepareObjectTypeForm( String wfId, String value ) {

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( "regexobject.type" );
        if ( value == null ) {
            selectFormItem.setValue( VariableDropDownEnum.SERVER.getId() );
        } else {
            selectFormItem.setValue( value );
        }

        selectFormItem.setLabel( "Object Selector" );
        selectFormItem.setType( "select" );

        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );

        List< SelectOptionsUI > options = new ArrayList<>();
        options.add( new SelectOptionsUI( VariableDropDownEnum.INTERNAL.getId(), VariableDropDownEnum.INTERNAL.getName() ) );
        options.add( new SelectOptionsUI( VariableDropDownEnum.CB2.getId(), VariableDropDownEnum.CB2.getName() ) );
        options.add( new SelectOptionsUI( VariableDropDownEnum.SERVER.getId(), VariableDropDownEnum.SERVER.getName() ) );
        options.add( new SelectOptionsUI( VariableDropDownEnum.LOCAL.getId(), VariableDropDownEnum.LOCAL.getName() ) );
        selectFormItem.setMultiple( Boolean.FALSE );
        selectFormItem.setOptions( options );
        selectFormItem.setBindFrom( "/workflow/scheme/regex/wf/" + wfId + "/dropdown/{__value__}" );
        return selectFormItem;
    }

    /**
     * Prepare object hidden form.
     *
     * @param wfId
     *         the wf id
     *
     * @return the UI form item
     */
    private UIFormItem prepareObjectHiddenForm( String wfId ) {

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( "id" );
        selectFormItem.setLabel( "ID" );
        selectFormItem.setValue( wfId );
        selectFormItem.setType( "hidden" );

        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, false );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );
        selectFormItem.setMultiple( Boolean.FALSE );
        return selectFormItem;
    }

    /**
     * {@inheritDoc}
     */
    // step 3
    @Override
    public UIForm getRegexDropdownSelected( String dropDown, String wfId, UUID userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > result = new ArrayList<>();
            if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.INTERNAL.getId() ) ) {
                Object selectedValue = null;
                Object selectedType = null;
                JsonNode jsonNode = getJson( entityManager, UUID.fromString( wfId ) );
                if ( jsonNode != null && jsonNode.has( REGEX_OBJECT ) ) {
                    selectedValue = jsonNode.get( REGEX_OBJECT ).get( VALUE ).asText();
                    selectedType = jsonNode.get( REGEX_OBJECT ).get( "type" ).asText();
                }
                if ( selectedType != null && selectedValue != null && selectedType.toString()
                        .equalsIgnoreCase( VariableDropDownEnum.INTERNAL.getId() ) ) {
                    result.add( prepareObjectSelectorForm( selectedValue ) );
                } else {
                    result.add( prepareObjectSelectorForm( null ) );
                }
                return GUIUtils.createFormFromItems( result );
            } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.CB2.getId() ) ) {
                if ( StringUtils.equals( userId.toString(), ConstantsID.SUPER_USER_ID ) || licenseManager.isFeatureAllowedToUser(
                        entityManager, SimuspaceFeaturesEnum.CB2_CONNECTOR.getKey(), userId.toString() ) ) {
                    ValidationUtils.validateCB2AccessWithWENLogin();
                    Object selectedValue = null;
                    Object selectedType = null;
                    JsonNode jsonNode = getJson( entityManager, UUID.fromString( wfId ) );
                    if ( jsonNode != null && jsonNode.has( EXTERNAL ) ) {
                        selectedValue = jsonNode.get( EXTERNAL ).asText();
                        result.add( prepareCB2TypeList( selectedValue, wfId ) );
                    } else {
                        result.add( prepareCB2TypeList( null, wfId ) );
                    }
                    return GUIUtils.createFormFromItems( result );
                } else {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.FEATURE_NOT_ALLOWED_TO_USER.getKey(),
                            SimuspaceFeaturesEnum.CB2_CONNECTOR.getKey() ) );
                }
            } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.SERVER.getId() ) ) {
                Object selectedValue = null;
                Object selectedType = null;
                JsonNode jsonNode = getJson( entityManager, UUID.fromString( wfId ) );
                if ( jsonNode != null && jsonNode.has( REGEX_OBJECT ) ) {
                    selectedValue = jsonNode.get( REGEX_OBJECT ).get( VALUE ).toString();
                    selectedType = jsonNode.get( REGEX_OBJECT ).get( "type" ).asText();
                }
                if ( selectedType != null && selectedValue != null && ( selectedType.toString()
                        .equalsIgnoreCase( VariableDropDownEnum.SERVER.getId() ) ) ) {
                    List< Object > serverList;
                    if ( !selectedValue.toString().isEmpty() ) {
                        serverList = new ArrayList<>();
                        serverList = ( List< Object > ) JsonUtils.jsonToList( selectedValue.toString(), serverList );
                        result.add( prepareFileExplorerForm( serverList ) );
                    }
                } else {
                    result.add( prepareFileExplorerForm( null ) );
                }
                return GUIUtils.createFormFromItems( result );
            } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.LOCAL.getId() ) ) {
                Object selectedValue = null;
                Object selectedType = null;
                JsonNode jsonNode = getJson( entityManager, UUID.fromString( wfId ) );
                if ( jsonNode != null && jsonNode.has( REGEX_OBJECT ) ) {
                    selectedValue = jsonNode.get( REGEX_OBJECT ).get( VALUE ).toString();
                    selectedType = jsonNode.get( REGEX_OBJECT ).get( "type" ).asText();
                }
                if ( selectedType != null && selectedValue != null && selectedType.toString()
                        .equalsIgnoreCase( VariableDropDownEnum.LOCAL.getId() ) ) {
                    selectedValue = JsonUtils.jsonToObject( selectedValue.toString(), Object.class );
                    result.add( prepareLocalFileSelectForm( selectedValue ) );
                } else {
                    result.add( prepareLocalFileSelectForm( null ) );
                }
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_TYPE_IS_NOT_VALID.getKey(), dropDown ) );
            }
            return GUIUtils.createFormFromItems( result );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getRegexCb2SelectorUI( String dropDown, String wfId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > result = new ArrayList<>();
            Object selectedValue = null;
            Object selectedType = null;
            JsonNode jsonNode = getJson( entityManager, UUID.fromString( wfId ) );
            if ( jsonNode != null && jsonNode.has( REGEX_OBJECT ) && jsonNode.has( EXTERNAL ) && dropDown.equals(
                    jsonNode.get( EXTERNAL ).asText() ) ) {
                selectedValue = jsonNode.get( REGEX_OBJECT ).get( VALUE ).asText();
                selectedType = jsonNode.get( REGEX_OBJECT ).get( "type" ).asText();
            }
            if ( selectedType != null && selectedValue != null && selectedType.toString()
                    .equalsIgnoreCase( VariableDropDownEnum.CB2.getId() ) ) {
                result.add( prepareCB2Selector( selectedValue, dropDown ) );
            } else {
                result.add( prepareCB2Selector( null, dropDown ) );
            }
            return GUIUtils.createFormFromItems( result );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getRegexContext( UUID id ) {
        List< ContextMenuItem > cml = new ArrayList<>();
        cml.add( prepareCreateRegexContext( id ) );
        cml.add( prepareUpdateRegexContext( id ) );
        cml.add( prepareDeleteRegexContext( id ) );
        return cml;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createRegexUIForm( String id ) {
        return GUIUtils.createFormFromItems( GUIUtils.prepareForm( true, new RegexScanDTO() ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editRegexUIForm( UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return GUIUtils.createFormFromItems( GUIUtils.prepareForm( false, getRegex( entityManager, id ) ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare create regex context.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareCreateRegexContext( UUID selectionId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( "regex/create/{selectionId}".replace( "{selectionId}", ConstantsString.EMPTY_STRING + selectionId ) );
        containerCMI.setTitle( "Create" );
        return containerCMI;
    }

    /**
     * Prepare update regex context.
     *
     * @param id
     *         the id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareUpdateRegexContext( UUID id ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( "regex/update/{id}".replace( "{id}", ConstantsString.EMPTY_STRING + id ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100072x4" ) );
        return containerCMI;
    }

    /**
     * Prepare delete regex context.
     *
     * @param id
     *         the id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareDeleteRegexContext( UUID id ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( "regex/delete/{id}".replace( "{id}", ConstantsString.EMPTY_STRING + id ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100007x4" ) );
        return containerCMI;
    }

    /**
     * Prepare cb 2 type list ui form item.
     *
     * @param value
     *         the value
     * @param wfId
     *         the wf id
     *
     * @return the ui form item
     */
    private UIFormItem prepareCB2TypeList( Object value, String wfId ) {
        SelectFormItem cb2TypeSelect = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        cb2TypeSelect.setType( FieldTypes.SELECTION.getType() );
        cb2TypeSelect.setName( EXTERNAL );
        cb2TypeSelect.setLabel( "Object Type" );
        cb2TypeSelect.setValue( BmwCaeBenchEnums.CB2_SUBMODEL.getKey() );
        List< SelectOptionsUI > options = new ArrayList<>();
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_SUBMODEL.getKey(), "Submodel" ) );
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_VARIANT.getKey(), "Variant" ) );
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_INPUT_DECK.getKey(), "InputDeck" ) );
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_RESULT.getKey(), "Result" ) );
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_KEYRESULTS.getKey(), "KeyResults" ) );
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_STORY_BOARD.getKey(), "StoryBoard" ) );
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_REPORT.getKey(), "Report" ) );
        cb2TypeSelect.setOptions( options );
        cb2TypeSelect.setBindFrom( "workflow/scheme/regex/wf/" + wfId + "/cb2/{__value__}" );
        cb2TypeSelect.setValue( value );
        return cb2TypeSelect;

    }

    /**
     * Prepare CB 2 selector.
     *
     * @param value
     *         the value
     *
     * @return the select UI table
     */
    private TableFormItem prepareCB2Selector( Object value, String external ) {
        TableFormItem cb2Form = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
        cb2Form.setName( REGEX_OBJECT_VALUE );
        cb2Form.setLabel( VariableDropDownEnum.CB2.getName() );
        cb2Form.setMultiple( false );
        cb2Form.setSelectable( null );
        cb2Form.setType( "externalObject" );
        cb2Form.setExternal( external );
        cb2Form.setTriggerModifiedOnInit( true );

        cb2Form.setValue( value );

        Map< String, String > optional = new HashMap<>();
        Map< String, Object > rules = new HashMap<>();

        cb2Form.setRules( rules );
        cb2Form.setExtendTree( optional );
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        cb2Form.setMessages( message );
        return cb2Form;
    }

    /**
     * Prepare object selector form.
     *
     * @param value
     *         the value
     *
     * @return the UI form item
     */
    private UIFormItem prepareObjectSelectorForm( Object value ) {

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( REGEX_OBJECT_VALUE );
        selectFormItem.setLabel( "Sus Object Selector" );
        selectFormItem.setValue( value );
        selectFormItem.setType( OBJECT );

        selectFormItem.setTriggerModifiedOnInit( true );

        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();
        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );
        selectFormItem.setMultiple( Boolean.FALSE );
        return selectFormItem;
    }

    /**
     * Prepare file explorer form.
     *
     * @param value
     *         the value
     *
     * @return the UI form item
     */
    private UIFormItem prepareFileExplorerForm( List< Object > value ) {

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( REGEX_OBJECT_VALUE );
        selectFormItem.setLabel( "Server File Selection" );

        selectFormItem.setTriggerModifiedOnInit( true );

        selectFormItem.setValue( value );
        selectFormItem.setType( "fileExplorer" );
        selectFormItem.setShow( "all" );
        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );
        selectFormItem.setMultiple( Boolean.FALSE );
        return selectFormItem;
    }

    /**
     * Prepare local file select form.
     *
     * @param value
     *         the value
     *
     * @return the UI form item
     */
    private UIFormItem prepareLocalFileSelectForm( Object value ) {

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( REGEX_OBJECT_VALUE );
        selectFormItem.setLabel( "Local File Selection" );
        selectFormItem.setValue( value );
        selectFormItem.setType( "file-upload" );

        selectFormItem.setTriggerModifiedOnInit( true );

        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );
        selectFormItem.setMultiple( Boolean.FALSE );
        return selectFormItem;
    }

    /**
     * {@inheritDoc}
     */
    // step 1
    @Override
    public Object getRegexSelectorModel( String userIdStringFromGeneralHeader, String userNameFromGeneralHeader, String jsonToBeUpdated ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String id = null;
            if ( jsonToBeUpdated != null ) {
                JsonNode jsonNode = JsonUtils.toJsonNode( jsonToBeUpdated );
                if ( jsonNode.has( "id" ) ) {
                    id = jsonNode.get( "id" ).asText();
                }
            }
            if ( id != null ) {
                SelectionEntity selectionEntis = selectionManager.getSelectionDAO()
                        .getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( id ) );
                String oldJson = ByteUtil.convertByteToString( selectionEntis.getJson() );
                selectionEntis.setJson( ByteUtil.convertStringToByte( jsonToBeUpdated ) );
                selectionManager.getSelectionDAO().saveOrUpdate( entityManager, selectionEntis );
                SelectionResponseUI genratedSelection = new SelectionResponseUI();
                genratedSelection.setId( selectionEntis.getId().toString() );
                if ( oldJson != null ) {
                    JsonNode jsonNodeOld = JsonUtils.toJsonNode( oldJson );
                    JsonNode jsonNodeNew = JsonUtils.toJsonNode( jsonToBeUpdated );
                    if ( jsonNodeOld != null && jsonNodeOld.has( REGEX_OBJECT ) && !( jsonNodeOld.get( REGEX_OBJECT ).get( VALUE )
                            .toString().equals( jsonNodeNew.get( REGEX_OBJECT ).get( VALUE ).toString() ) ) ) {
                        List< RegexEntity > regexEntities = regexDAO.getRegexListBySelectionId( entityManager, UUID.fromString( id ) );
                        deleteRegex( entityManager, regexEntities.stream().map( RegexEntity::getId ).map( UUID::toString ).toList() );
                    }
                }
                return genratedSelection;
            } else {
                SelectionResponseUI selectionid = createSelection( entityManager, userIdStringFromGeneralHeader, jsonToBeUpdated );
                SelectionResponseUI genratedSelection = new SelectionResponseUI();
                genratedSelection.setId( selectionid.getId() );
                return genratedSelection;
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Creates the selection.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdStringFromGeneralHeader
     *         the user id string from general header
     * @param json
     *         the json
     *
     * @return the selection response UI
     */
    private SelectionResponseUI createSelection( EntityManager entityManager, String userIdStringFromGeneralHeader, String json ) {
        // generate selectiona gains id
        FiltersDTO filtersDTO = new FiltersDTO();
        List< Object > itmes = new ArrayList<>();
        itmes.add( UUID.randomUUID() );
        filtersDTO.setItems( itmes );

        SelectionResponseUI selectionid = selectionManager.createSelection( entityManager, userIdStringFromGeneralHeader,
                SelectionOrigins.REGEX_SELECTION, filtersDTO );

        SelectionEntity selectionEntis = selectionManager.getSelectionDAO()
                .getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( selectionid.getId() ) );
        selectionEntis.setJson( ByteUtil.convertStringToByte( json ) );
        selectionManager.getSelectionDAO().saveOrUpdate( entityManager, selectionEntis );
        return selectionid;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List< RegexEntity > duplicateRegexEntitiesBySelectionId( UUID oldSelectionId, UUID newSelectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< RegexEntity > regexEntities = regexDAO.getRegexListBySelectionId( entityManager, oldSelectionId );
            List< RegexEntity > newEntities = new ArrayList<>();
            if ( CollectionUtils.isEmpty( regexEntities ) ) {
                return newEntities;
            }
            regexEntities.forEach( oldEntity -> {
                RegexEntity newEntity = oldEntity.copy();
                newEntity.setSelectionId( newSelectionId );
                newEntities.add( regexDAO.saveRegex( entityManager, newEntity ) );
            } );
            return newEntities;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RegexDAO getRegexDAO() {
        return regexDAO;
    }

    /**
     * Sets the regex DAO.
     *
     * @param regexDAO
     *         the new regex DAO
     */
    public void setRegexDAO( RegexDAO regexDAO ) {
        this.regexDAO = regexDAO;
    }

    /**
     * Gets the wf scheme manager.
     *
     * @return the wf scheme manager
     */
    public WFSchemeManager getWfSchemeManager() {
        return wfSchemeManager;
    }

    /**
     * Sets the wf scheme manager.
     *
     * @param wfSchemeManager
     *         the new wf scheme manager
     */
    public void setWfSchemeManager( WFSchemeManager wfSchemeManager ) {
        this.wfSchemeManager = wfSchemeManager;
    }

    /**
     * Gets the selection manager.
     *
     * @return the selection manager
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    /**
     * Sets the selection manager.
     *
     * @param selectionManager
     *         the new selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

    /**
     * Gets the sus DAO.
     *
     * @return the sus DAO
     */
    public SuSGenericObjectDAO< SuSEntity > getSusDAO() {
        return susDAO;
    }

    /**
     * Sets the sus DAO.
     *
     * @param susDAO
     *         the new sus DAO
     */
    public void setSusDAO( SuSGenericObjectDAO< SuSEntity > susDAO ) {
        this.susDAO = susDAO;
    }

    /**
     * Gets the bmw cae bench DAO.
     *
     * @return the bmw cae bench DAO
     */
    public BmwCaeBenchDAO getBmwCaeBenchDAO() {
        return bmwCaeBenchDAO;
    }

    /**
     * Sets the bmw cae bench DAO.
     *
     * @param bmwCaeBenchDAO
     *         the new bmw cae bench DAO
     */
    public void setBmwCaeBenchDAO( BmwCaeBenchDAO bmwCaeBenchDAO ) {
        this.bmwCaeBenchDAO = bmwCaeBenchDAO;
    }

    /**
     * Sets the entity manager factory.
     *
     * @param entityManagerFactory
     *         the new entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Sets document dao.
     *
     * @param documentDAO
     *         the document dao
     */
    public void setDocumentDAO( DocumentDAO documentDAO ) {
        this.documentDAO = documentDAO;
    }

}

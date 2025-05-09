package de.soco.software.simuspace.suscore.object.report.manager.impl;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.filter;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.img;
import static j2html.TagCreator.li;
import static j2html.TagCreator.source;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.thead;
import static j2html.TagCreator.tr;
import static j2html.TagCreator.ul;
import static j2html.TagCreator.video;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.toc.TocGenerator;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.Br;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STBrType;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileExtension;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ImageUtil;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.DataObjectCurveEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectImageEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectMovieEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectValueEntity;
import de.soco.software.simuspace.suscore.data.entity.ReportEntity;
import de.soco.software.simuspace.suscore.data.entity.SectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SectionObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.model.DataObjectCurveDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectMovieDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectValueDTO;
import de.soco.software.simuspace.suscore.data.model.SectionDTO;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.object.manager.DataObjectManager;
import de.soco.software.simuspace.suscore.object.report.dao.ReportDAO;
import de.soco.software.simuspace.suscore.object.report.dao.SectionDAO;
import de.soco.software.simuspace.suscore.object.report.dao.SectionObjectDAO;
import de.soco.software.simuspace.suscore.object.report.manager.ReportManager;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;

import j2html.tags.ContainerTag;

/**
 * The Class ReportManagerImpl.
 *
 * @author Ahsan.Khan
 */
@Log4j2
public class ReportManagerImpl implements ReportManager {

    /**
     * The Constant DATA_TYPE.
     */
    private static final String DATA_TYPE = "type";

    /**
     * The Constant CURVE.
     */
    private static final String CURVE = "Curve";

    /**
     * The Constant IMAGE.
     */
    private static final String IMAGE = "Image";

    /**
     * The Constant VALUE.
     */
    private static final String VALUE = "Value";

    /**
     * The Constant MOVIE.
     */
    private static final String MOVIE = "Movie";

    /**
     * The Constant CURVE_TYPE.
     */
    private static final String CURVE_TYPE = "de.soco.software.simuspace.suscore.data.model.DataObjectCurveDTO";

    /**
     * The Constant IMAGE_TYPE.
     */
    private static final String IMAGE_TYPE = "de.soco.software.simuspace.suscore.data.model.DataObjectImageDTO";

    /**
     * The Constant VALUE_TYPE.
     */
    private static final String VALUE_TYPE = "de.soco.software.simuspace.suscore.data.model.DataObjectValueDTO";

    /**
     * The Constant MOVIE_TYPE.
     */
    private static final String MOVIE_TYPE = "de.soco.software.simuspace.suscore.data.model.DataObjectMovieDTO";

    /**
     * The Constant SELECTOR.
     */
    private static final String SELECTOR = "selectionId";

    /**
     * The Constant REPORT_ENTITY.
     */
    private static final String REPORT_ENTITY = "reportEntity.composedId.id";

    /**
     * The report DAO.
     */
    private ReportDAO reportDAO;

    /**
     * The section DAO.
     */
    private SectionDAO sectionDAO;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The object type config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * The section object DAO.
     */
    private SectionObjectDAO sectionObjectDAO;

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The data manager.
     */
    private DataObjectManager dataObjectManager;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public SectionDTO createSection( String userIdFromGeneralHeader, SectionDTO section, UUID reportId ) {
        if ( section == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SECTION_DETAIL.getKey() ) );
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !userIdFromGeneralHeader.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager,
                    userIdFromGeneralHeader, reportId + ConstantsString.COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_WRITE.getKey(), reportId ) );
            }

            Notification notification = section.validate();
            if ( notification.hasErrors() ) {
                throw new SusException( notification.getErrors().toString() );
            }
            SectionEntity sectionEntity = prepareSectionEntityFromSectionDTO( section );
            sectionEntity.setSelectionId( UUID.fromString(
                    selectionManager.createSelection( entityManager, userIdFromGeneralHeader, SelectionOrigins.REPORT_SECTION,
                            new FiltersDTO() ).getId() ) );
            ReportEntity reportEntity = reportDAO.getLatestObjectById( entityManager, ReportEntity.class, reportId );
            if ( reportEntity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.SECTION_DETAIL.getKey(), reportId ) );
            } else {
                sectionEntity.setReportEntity( reportEntity );
            }
            SectionEntity returnedSectionEntity = sectionDAO.saveOrUpdate( entityManager, sectionEntity );
            return prepareSectionDTOFromSectionEntity( returnedSectionEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SectionDTO > getSectionList( String userId, UUID reportId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< SectionEntity > entityList = sectionDAO.getSectionsByReportId( entityManager, reportId );
            return prepareListOfSectionDTO( entityList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare list of section DTO.
     *
     * @param entityList
     *         the entity list
     *
     * @return the list
     */
    private List< SectionDTO > prepareListOfSectionDTO( List< SectionEntity > entityList ) {
        List< SectionDTO > list = new ArrayList<>();
        if ( CollectionUtils.isNotEmpty( entityList ) ) {
            for ( SectionEntity sectionEntity : entityList ) {
                list.add( prepareSectionDTOFromSectionEntity( sectionEntity ) );
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createSectionUI( String userId ) {
        List< UIFormItem > list = GUIUtils.prepareForm( true, new SectionDTO() );
        for ( UIFormItem uiFormItem : list ) {
            if ( uiFormItem.getName().equals( DATA_TYPE ) ) {
                Map< String, String > map = new HashMap<>();
                map.put( CURVE_TYPE, CURVE );
                map.put( IMAGE_TYPE, IMAGE );
                map.put( VALUE_TYPE, VALUE );
                map.put( MOVIE_TYPE, MOVIE );
                GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getSelectBoxOptions( map ),
                        ConstantsString.EMPTY_STRING, false );
            }
        }
        List< UIFormItem > removeSelection = list.stream().filter( uiFormItem -> uiFormItem.getName().equals( SELECTOR ) ).toList();
        list.removeAll( removeSelection );
        return GUIUtils.createFormFromItems( list );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editSectionUI( String userId, UUID sectionId ) {
        Map< String, String > map = new HashMap<>();
        map.put( CURVE_TYPE, CURVE );
        map.put( IMAGE_TYPE, IMAGE );
        map.put( VALUE_TYPE, VALUE );
        map.put( MOVIE_TYPE, MOVIE );

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SectionEntity retriveEntity = sectionDAO.getLatestObjectById( entityManager, SectionEntity.class, sectionId );
            if ( retriveEntity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.SECTION_DETAIL.getKey(), sectionId ) );
            }
            SectionDTO sectionDTO = prepareSectionDTOFromSectionEntity( retriveEntity );
            List< UIFormItem > list = GUIUtils.prepareForm( false, sectionDTO );
            for ( UIFormItem uiFormItem : list ) {
                if ( uiFormItem.getName().equals( DATA_TYPE ) ) {
                    GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getSelectBoxOptions( map ),
                            sectionDTO.getType(), false );
                }
            }
            return GUIUtils.createFormFromItems( list );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SectionDTO updateSection( String userId, SectionDTO sectionDTO ) {
        SectionEntity returnedSectionEntity = null;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( sectionDTO != null ) {
                SectionEntity retriveEntity = sectionDAO.getLatestObjectById( entityManager, SectionEntity.class, sectionDTO.getId() );
                if ( retriveEntity == null ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.SECTION_DETAIL.getKey(), sectionDTO.getId() ) );
                }
                Notification notify = sectionDTO.validate();
                if ( notify != null && notify.hasErrors() ) {
                    throw new SusException( notify.getErrors().toString() );
                }

                if ( null == sectionDTO.getSelectionId() || StringUtils.isEmpty( sectionDTO.getSelectionId().toString() ) ) {
                    sectionDTO.setSelectionId( retriveEntity.getSelectionId() );
                }

                prepareSectionEntityFromSectionDTO( sectionDTO, retriveEntity );
                retriveEntity = sectionDAO.saveOrUpdate( entityManager, retriveEntity );
                if ( retriveEntity != null ) {
                    /* remove objects against returnedSectionEntity ****/
                    removeOldObjectsFromSection( entityManager, retriveEntity );

                    /* attach new objects against returnedSectionEntity ****/
                    attachNewObjectsAgainstSection( entityManager, retriveEntity );
                } else {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.SECTION_ISSUE.getKey() ) );
                }
            }
            return prepareSectionDTOFromSectionEntity( returnedSectionEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws java.io.IOException
     */
    @Override
    public ContainerTag generateSectionPreview( UUID sectionId ) throws java.io.IOException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return generateSectionPreview( entityManager, sectionId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws java.io.IOException
     */
    @Override
    public ContainerTag generateSectionPreview( EntityManager entityManager, UUID sectionId ) throws java.io.IOException {
        SectionEntity retriveEntity = getSection( entityManager, sectionId );
        List< SectionObjectEntity > sectionObjectEntities = sectionObjectDAO.getSectionObjectEntityBySectionId( entityManager, sectionId );
        switch ( retriveEntity.getType() ) {
            case IMAGE_TYPE -> {
                List< DataObjectImageEntity > dataObjectImageEntities = new ArrayList<>();
                for ( SectionObjectEntity sectionObjectEntity : sectionObjectEntities ) {
                    DataObjectImageEntity dataObjectImageEntity = ( DataObjectImageEntity ) sectionObjectEntity.getObjectEntity();
                    if ( isFileExitInDataObjectEntity( dataObjectImageEntity ) ) {
                        dataObjectImageEntities.add( dataObjectImageEntity );
                    }
                }
                return prepareImageSection( sectionId.toString(), retriveEntity.getName(), dataObjectImageEntities );
            }
            case CURVE_TYPE -> {
                List< DataObjectCurveDTO > dataObjectCurveDTOs = new ArrayList<>();
                for ( SectionObjectEntity sectionObjectEntity : sectionObjectEntities ) {
                    dataObjectCurveDTOs.add( dataObjectManager.getDataObjectCurve( entityManager,
                            sectionObjectEntity.getObjectEntity().getComposedId().getId(), null ) );
                }
                return prepareCurveSection( sectionId.toString(), retriveEntity.getName(), dataObjectCurveDTOs );
            }
            case VALUE_TYPE -> {
                List< DataObjectValueEntity > dataObjectValueEntities = new ArrayList<>();
                for ( SectionObjectEntity sectionObjectEntity : sectionObjectEntities ) {
                    dataObjectValueEntities.add( ( DataObjectValueEntity ) sectionObjectEntity.getObjectEntity() );
                }
                List< DataObjectValueDTO > dataObjectValueDTOs = new ArrayList<>();
                for ( DataObjectValueEntity dataObjectValueEntity : dataObjectValueEntities ) {
                    dataObjectValueDTOs.add(
                            dataObjectManager.getDataObjectValue( entityManager, dataObjectValueEntity.getComposedId().getId(), null ) );
                }
                return prepareTableSections( sectionId.toString(), retriveEntity.getName(), dataObjectValueDTOs );
            }
            case MOVIE_TYPE -> {
                List< DataObjectMovieDTO > dataObjectMovieDTOs = new ArrayList<>();
                for ( SectionObjectEntity sectionObjectEntity : sectionObjectEntities ) {
                    DataObjectMovieEntity dataObjectMovieEntity = ( DataObjectMovieEntity ) sectionObjectEntity.getObjectEntity();
                    if ( isFileExitInDataObjectEntity( dataObjectMovieEntity ) ) {
                        dataObjectMovieDTOs.add(
                                dataObjectManager.getDataObjectMovie( entityManager, dataObjectMovieEntity.getComposedId().getId() ) );
                    }
                }
                return prepareMovieSection( sectionId.toString(), retriveEntity.getName(), dataObjectMovieDTOs );
            }
            default -> throw new SusException( "Invalid type provided." );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File previewReportPdf( UUID reportId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String basePath = PropertiesManager.getFeStaticPath();
            ReportEntity reportEntity = ( ReportEntity ) susDAO.getLatestNonDeletedObjectById( entityManager, reportId );
            if ( reportEntity == null ) {
                throw new SusException( "Report not found." );
            }
            String url = basePath + File.separator + reportEntity.getName() + ".pdf";
            File file = new File( url );
            Document document = new Document();
            PdfWriter writer;
            try ( FileOutputStream fos = new FileOutputStream( url ) ) {
                writer = PdfWriter.getInstance( document, fos );
                document.open();
                List< SectionEntity > sectionEntities = getSectionsByReportId( entityManager, reportId );
                Map< String, PdfTemplate > tocPlaceholder = new HashMap<>();
                createTableOfContentsForReports( document, sectionEntities, tocPlaceholder );
                createSections( entityManager, document, sectionEntities, tocPlaceholder, writer );
                document.close();
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
                throw new SusException( "File not prepared, something went wrong!" );
            }
            return file;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File previewSectionPdf( UUID sectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String basePath = PropertiesManager.getFeStaticPath();
            SectionEntity retriveEntity = sectionDAO.getLatestObjectById( entityManager, SectionEntity.class, sectionId );
            if ( retriveEntity == null ) {
                throw new SusException( "Section not found." );
            }
            String url = basePath + File.separator + retriveEntity.getName() + ".pdf";
            File file = new File( url );
            Document document = new Document();
            PdfWriter writer;
            try ( FileOutputStream fos = new FileOutputStream( url ) ) {
                writer = PdfWriter.getInstance( document, fos );
                document.open();
                Map< String, PdfTemplate > tocPlaceholder = new HashMap<>();
                // add a small introduction chapter the shouldn't be counted.
                final Chapter intro = new Chapter(
                        new Paragraph( "Table of contents", FontFactory.getFont( FontFactory.HELVETICA, 24, Font.NORMAL ) ), 0 );
                intro.setNumberDepth( 0 );
                document.add( intro );
                createSingleTableOfContentsForSections( document, tocPlaceholder, retriveEntity );
                createSingleSection( entityManager, document, tocPlaceholder, writer, 1, retriveEntity );
                document.close();
            } catch ( Exception e ) {
                throw new SusException( "File not prepared, something went wrong!" );
            }
            return file;
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     *
     * @throws java.io.IOException
     */
    @Override
    public File previewReportDocx( UUID reportId ) throws java.io.IOException {

        String basePath = PropertiesManager.getFeStaticPath();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {

            ReportEntity reportEntity = ( ReportEntity ) susDAO.getLatestNonDeletedObjectById( entityManager, reportId );
            if ( reportEntity == null ) {
                throw new SusException( "Report not found." );
            }

            String url = basePath + File.separator + reportEntity.getName() + ".docx";
            File docx = new File( url );
            List< SectionEntity > sectionEntities = getSectionsByReportId( entityManager, reportId );

            try {
                WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.createPackage();
                MainDocumentPart documentPart = wordprocessingMLPackage.getMainDocumentPart();
                ObjectFactory objectFactory = Context.getWmlObjectFactory();

                for ( int i = 1; i < 10; i++ ) {
                    documentPart.getPropertyResolver().activateStyle( String.format( "TOC%s", i ) );
                }
                addPageBreak( documentPart, objectFactory );
                for ( SectionEntity sectionEntity : sectionEntities ) {
                    generateSingleSectionForDocx( entityManager, wordprocessingMLPackage, documentPart, sectionEntity, objectFactory );
                }

                generateTableOfContentViaDocx4j( docx, wordprocessingMLPackage );
            } catch ( org.docx4j.openpackaging.exceptions.InvalidFormatException e ) {
                throw new SusException( "Invalid format exception." );
            }
            return docx;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Generate single section for docx.
     *
     * @param entityManager
     *         the entity manager
     * @param wordprocessingMLPackage
     *         the wordprocessing ml package
     * @param documentPart
     *         the document part
     * @param sectionEntity
     *         the section entity
     * @param objectFactory
     *         the object factory
     *
     * @throws IOException
     *         the io exception
     */
    private void generateSingleSectionForDocx( EntityManager entityManager, WordprocessingMLPackage wordprocessingMLPackage,
            MainDocumentPart documentPart, SectionEntity sectionEntity, ObjectFactory objectFactory ) throws java.io.IOException {
        documentPart.addStyledParagraphOfText( "Heading1", sectionEntity.getName() );

        List< SectionObjectEntity > sectionObjectEntities = sectionObjectDAO.getSectionObjectEntityBySectionId( entityManager,
                sectionEntity.getId() );
        if ( CollectionUtils.isNotEmpty( sectionObjectEntities ) ) {
            if ( sectionEntity.getType().equalsIgnoreCase( VALUE_TYPE ) ) {
                List< DataObjectValueDTO > dataObjectValueDTOs = prepareDataObjectValueDTOs( entityManager, sectionObjectEntities );

                if ( CollectionUtils.isNotEmpty( dataObjectValueDTOs ) ) {
                    prepareDataObjectValuesTableForDocx( wordprocessingMLPackage, documentPart, objectFactory, dataObjectValueDTOs );
                }
            } else {
                switch ( sectionEntity.getType() ) {
                    case IMAGE_TYPE -> {
                        for ( SectionObjectEntity sectionObjectEntity : sectionObjectEntities ) {
                            DataObjectImageEntity dataObjectImageEntity = ( DataObjectImageEntity ) sectionObjectEntity.getObjectEntity();
                            if ( isFileExitInDataObjectEntity( dataObjectImageEntity ) ) {
                                fillImageForDocx( wordprocessingMLPackage, objectFactory, getDataObjectImageSrc( dataObjectImageEntity ),
                                        documentPart );
                            }
                        }
                    }
                    case MOVIE_TYPE -> {
                        for ( SectionObjectEntity sectionObjectEntity : sectionObjectEntities ) {
                            DataObjectMovieEntity dataObjectMovieEntity = ( DataObjectMovieEntity ) sectionObjectEntity.getObjectEntity();
                            if ( isFileExitInDataObjectEntity( dataObjectMovieEntity ) ) {
                                fillImageForDocx( wordprocessingMLPackage, objectFactory, getDataObjectMovieSrc( dataObjectMovieEntity ),
                                        documentPart );
                            }
                        }
                    }
                    case CURVE_TYPE -> {
                        List< DataObjectCurveDTO > dataObjectCurveDTOs = new ArrayList<>();
                        for ( SectionObjectEntity sectionObjectEntity : sectionObjectEntities ) {
                            DataObjectCurveEntity dataObjectCurveEntity = ( DataObjectCurveEntity ) sectionObjectEntity.getObjectEntity();
                            if ( isFileExitInDataObjectEntity( dataObjectCurveEntity ) ) {
                                dataObjectCurveDTOs.add(
                                        dataObjectManager.getDataObjectCurve( entityManager, dataObjectCurveEntity.getComposedId().getId(),
                                                null ) );
                            }
                        }
                        fillImageForDocx( wordprocessingMLPackage, objectFactory, getDataObjectCurveSrc( dataObjectCurveDTOs ),
                                documentPart );
                    }
                    default -> {
                    }
                }
            }
        }
    }

    /**
     * Checks if is file exit in data object entity.
     *
     * @param dataObjectEntity
     *         the data object entity
     *
     * @return true, if is file exit in data object entity
     */
    private boolean isFileExitInDataObjectEntity( DataObjectEntity dataObjectEntity ) {
        return dataObjectEntity.getFile() != null;
    }

    /**
     * Prepare data object values table for docx.
     *
     * @param wordprocessingMLPackage
     *         the wordprocessing ML package
     * @param documentPart
     *         the document part
     * @param objectFactory
     *         the object factory
     * @param dataObjectValueDTOs
     *         the data object value DT os
     */
    private void prepareDataObjectValuesTableForDocx( WordprocessingMLPackage wordprocessingMLPackage, MainDocumentPart documentPart,
            ObjectFactory objectFactory, List< DataObjectValueDTO > dataObjectValueDTOs ) {
        int writableWidthTwips = wordprocessingMLPackage.getDocumentModel().getSections().get( 0 ).getPageDimensions()
                .getWritableWidthTwips();
        int cellWidthTwips = Double.valueOf( Math.floor( ( writableWidthTwips / 4 ) ) ).intValue();

        Tbl table = TblFactory.createTable( ( dataObjectValueDTOs.size() + 1 ), 4, cellWidthTwips );

        Tr headerRow = ( Tr ) table.getContent().get( 0 );
        insertInColumn( objectFactory, headerRow, 0, "Name", true );
        insertInColumn( objectFactory, headerRow, 1, "Dimension", true );
        insertInColumn( objectFactory, headerRow, 2, "Unit", true );
        insertInColumn( objectFactory, headerRow, 3, VALUE, true );

        for ( int i = 0; i < dataObjectValueDTOs.size(); i++ ) {
            Tr row = ( Tr ) table.getContent().get( i + 1 );
            for ( int j = 0; j < 4; j++ ) {
                DataObjectValueDTO dataObjectValue = dataObjectValueDTOs.get( i );
                if ( j == 0 ) {
                    insertInColumn( objectFactory, row, j, dataObjectValue.getName(), false );
                }
                if ( j == 1 ) {
                    insertInColumn( objectFactory, row, j, dataObjectValue.getDimension(), false );
                }
                if ( j == 2 ) {
                    insertInColumn( objectFactory, row, j, dataObjectValue.getUnit(), false );
                }
                if ( j == 3 ) {
                    insertInColumn( objectFactory, row, j, dataObjectValue.getValue(), false );
                }
            }
        }
        documentPart.addObject( table );
    }

    /**
     * Insert in column.
     *
     * @param factory
     *         the factory
     * @param headerRow
     *         the header row
     * @param index
     *         the index
     * @param data
     *         the data
     * @param isBold
     *         the is bold
     */
    private void insertInColumn( ObjectFactory factory, Tr headerRow, int index, String data, boolean isBold ) {
        Tc columnTc = ( Tc ) headerRow.getContent().get( index );
        P columnPara = ( P ) columnTc.getContent().get( 0 );
        Text text = factory.createText();
        text.setValue( data );
        R run = factory.createR();
        if ( isBold ) {
            setFont( factory, run, "50" );
        }
        run.getContent().add( text );
        columnPara.getContent().add( run );
    }

    /**
     * Sets the font.
     *
     * @param factory
     *         the factory
     * @param run
     *         the run
     * @param fontSize
     *         the font size
     */
    private void setFont( ObjectFactory factory, R run, String fontSize ) {
        RPr runProperties = factory.createRPr();
        BooleanDefaultTrue b = new BooleanDefaultTrue();
        b.setVal( true );
        runProperties.setB( b );
        HpsMeasure size = new HpsMeasure();
        size.setVal( new BigInteger( fontSize ) );
        runProperties.setSz( size );
        runProperties.setSzCs( size );
        run.setRPr( runProperties );
    }

    /**
     * Prepare data object value DT os.
     *
     * @param entityManager
     *         the entity manager
     * @param sectionObjectEntities
     *         the section object entities
     *
     * @return the list
     *
     * @throws IOException
     *         the io exception
     */
    private List< DataObjectValueDTO > prepareDataObjectValueDTOs( EntityManager entityManager,
            List< SectionObjectEntity > sectionObjectEntities ) throws java.io.IOException {
        List< DataObjectValueDTO > dataObjectValueDTOs = new ArrayList<>();
        for ( SectionObjectEntity sectionObjectEntity : sectionObjectEntities ) {
            dataObjectValueDTOs.add(
                    dataObjectManager.getDataObjectValue( entityManager, sectionObjectEntity.getObjectEntity().getComposedId().getId(),
                            null ) );
        }
        return dataObjectValueDTOs;
    }

    /**
     * Fill image for docx.
     *
     * @param wordprocessingMLPackage
     *         the wordprocessing ML package
     * @param objectFactory
     *         the object factory
     * @param imgSrc
     *         the img src
     * @param documentPart
     *         the document part
     */
    private void fillImageForDocx( WordprocessingMLPackage wordprocessingMLPackage, ObjectFactory objectFactory, String imgSrc,
            MainDocumentPart documentPart ) {
        BinaryPartAbstractImage imagePart;
        Inline inline;
        try ( InputStream in = getContent( imgSrc.replaceAll( " ", "%20" ) ).openStream() ) {
            byte[] imgBytes = toByteArray( in );
            imagePart = BinaryPartAbstractImage.createImagePart( wordprocessingMLPackage, imgBytes );
            inline = imagePart.createImageInline( "image", "image", 1, 2, false );
        } catch ( Exception e ) {
            throw new SusException( "Issue processing binary image" );
        }
        P celPar = addInlineImageToParagraph( inline, objectFactory );
        documentPart.addObject( celPar );
    }

    private URL getContent( String url ) throws MalformedURLException {
        if ( url.toLowerCase().contains( "https" ) ) {
            SuSClient.ignoreSSL();
        }
        return new URL( url );
    }

    /**
     * Convert file to byte array.
     *
     * @param in
     *         the in
     *
     * @return the byte[]
     *
     * @throws IOException
     *         the io exception
     */
    private byte[] toByteArray( InputStream in ) throws java.io.IOException {
        byte[] bytes;
        try ( ByteArrayOutputStream os = new ByteArrayOutputStream() ) {
            byte[] buffer = new byte[ 1024 ];
            int len;
            // read bytes from the input stream and store them in buffer
            while ( ( len = in.read( buffer ) ) != -1 ) {
                // write bytes from the buffer into output stream
                os.write( buffer, 0, len );
            }
            bytes = os.toByteArray();
        }
        return bytes;
    }

    /**
     * Adds the inline image to paragraph.
     *
     * @param inline
     *         the inline
     * @param factory
     *         the factory
     *
     * @return the p
     */
    private P addInlineImageToParagraph( Inline inline, ObjectFactory factory ) {
        P paragraph = factory.createP();
        R run = factory.createR();
        paragraph.getContent().add( run );
        Drawing drawing = factory.createDrawing();
        run.getContent().add( drawing );
        drawing.getAnchorOrInline().add( inline );
        return paragraph;
    }

    /**
     * Generate table of content via docx 4 j.
     *
     * @param docx
     *         the docx
     * @param wordprocessingMLPackage
     *         the wordprocessing ML package
     */
    private void generateTableOfContentViaDocx4j( File docx, WordprocessingMLPackage wordprocessingMLPackage ) {
        try {
            TocGenerator tocGenerator = new TocGenerator( wordprocessingMLPackage );
            tocGenerator.generateToc( 0, " TOC \\o \"1-3\" \\h \\z \\u ", true );
            wordprocessingMLPackage.save( docx );
        } catch ( Docx4JException e ) {
            throw new SusException( "Table generating issue!" );
        }
    }

    /**
     * Adds the page break.
     *
     * @param documentPart
     *         the document part
     */
    private void addPageBreak( MainDocumentPart documentPart, ObjectFactory objectFactory ) {
        Br breakObj = new Br();
        breakObj.setType( STBrType.PAGE );
        P paragraph = objectFactory.createP();
        paragraph.getContent().add( breakObj );
        documentPart.getJaxbElement().getBody().getContent().add( paragraph );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File previewSectionDocx( UUID sectionId ) {
        return null;
    }

    /**
     * Creates the TOC.
     *
     * @param document
     *         the document
     * @param sectionEntities
     *         the section entities
     * @param tocPlaceholder
     *         the toc placeholder
     *
     * @throws DocumentException
     *         the document exception
     */
    private void createTableOfContentsForReports( Document document, List< SectionEntity > sectionEntities,
            Map< String, PdfTemplate > tocPlaceholder ) throws DocumentException {
        // add a small introduction chapter the shouldn't be counted.
        final Chapter intro = new Chapter(
                new Paragraph( "Table of contents", FontFactory.getFont( FontFactory.HELVETICA, 24, Font.NORMAL ) ), 0 );
        intro.setNumberDepth( 0 );
        document.add( intro );
        for ( SectionEntity sectionEntity : sectionEntities ) {
            createSingleTableOfContentsForSections( document, tocPlaceholder, sectionEntity );
        }
    }

    /**
     * Creates the single table of contents for sections.
     *
     * @param document
     *         the document
     * @param tocPlaceholder
     *         the toc placeholder
     * @param sectionEntity
     *         the section entity
     *
     * @throws DocumentException
     *         the document exception
     */
    private void createSingleTableOfContentsForSections( Document document, Map< String, PdfTemplate > tocPlaceholder,
            SectionEntity sectionEntity ) throws DocumentException {
        final Chunk chunk = new Chunk( sectionEntity.getName() ).setLocalGoto( sectionEntity.getName() );
        document.add( new Paragraph( chunk ) );
        // Add a placeholder for the page reference
        document.add( new VerticalPositionMark() {

            @Override
            public void draw( final PdfContentByte canvas, final float llx, final float lly, final float urx, final float ury,
                    final float y ) {
                final PdfTemplate createTemplate = canvas.createTemplate( 50, 50 );
                tocPlaceholder.put( sectionEntity.getName(), createTemplate );

                canvas.addTemplate( createTemplate, urx - 50, y );
            }
        } );
    }

    /**
     * Creates the sections.
     *
     * @param entityManager
     *         the entity manager
     * @param document
     *         the document
     * @param sectionEntities
     *         the section entities
     * @param tocPlaceholder
     *         the toc placeholder
     * @param writer
     *         the writer
     *
     * @throws DocumentException
     *         the document exception
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void createSections( EntityManager entityManager, Document document, List< SectionEntity > sectionEntities,
            Map< String, PdfTemplate > tocPlaceholder, PdfWriter writer ) throws DocumentException, java.io.IOException {
        int i = 1;
        for ( SectionEntity sectionEntity : sectionEntities ) {
            createSingleSection( entityManager, document, tocPlaceholder, writer, i, sectionEntity );
            i++;
        }
    }

    /**
     * Creates the single section.
     *
     * @param entityManager
     *         the entity manager
     * @param document
     *         the document
     * @param tocPlaceholder
     *         the toc placeholder
     * @param writer
     *         the writer
     * @param i
     *         the i
     * @param sectionEntity
     *         the section entity
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws DocumentException
     *         the document exception
     */
    private void createSingleSection( EntityManager entityManager, Document document, Map< String, PdfTemplate > tocPlaceholder,
            PdfWriter writer, int i, SectionEntity sectionEntity ) throws java.io.IOException, DocumentException {
        final Chunk chunk = new Chunk( sectionEntity.getName(),
                FontFactory.getFont( FontFactory.HELVETICA, 24, Font.NORMAL ) ).setLocalDestination( sectionEntity.getName() );
        final Chapter chapter = new Chapter( new Paragraph( chunk ), i );
        chapter.setNumberDepth( 0 );
        chapter.add( addTitle( sectionEntity.getName() ) );

        PdfPTable pdfPTable = getSectionPdfPTable( entityManager, sectionEntity );
        if ( pdfPTable != null ) {
            chapter.add( pdfPTable );
        }
        document.add( chapter );

        // When we wrote the chapter, we now the pagenumber
        final PdfTemplate template = tocPlaceholder.get( sectionEntity.getName() );
        if ( template != null ) {
            template.beginText();
            template.setFontAndSize( BaseFont.createFont(), 12 );
            template.setTextMatrix( 50 - BaseFont.createFont().getWidthPoint( String.valueOf( writer.getPageNumber() ), 12 ), 0 );
            template.showText( String.valueOf( writer.getPageNumber() ) );
            template.endText();
        }
    }

    /**
     * Adds the title.
     *
     * @param title
     *         the title
     *
     * @return the paragraph
     */
    private Paragraph addTitle( String title ) {
        Font fontbold = FontFactory.getFont( "Times-Roman", 25, Font.NORMAL );
        Paragraph p = new Paragraph( title, fontbold );
        p.setSpacingAfter( 20 );
        // p.setAlignment(1); // Center
        return p;
    }

    /**
     * Gets the section pdf P table.
     *
     * @param entityManager
     *         the entity manager
     * @param retriveEntity
     *         the retrive entity
     *
     * @return the section pdf P table
     *
     * @throws BadElementException
     *         the bad element exception
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private PdfPTable getSectionPdfPTable( EntityManager entityManager, SectionEntity retriveEntity )
            throws BadElementException, java.io.IOException {
        List< SectionObjectEntity > sectionObjectEntities = sectionObjectDAO.getSectionObjectEntityBySectionId( entityManager,
                retriveEntity.getId() );
        if ( CollectionUtils.isNotEmpty( sectionObjectEntities ) ) {
            switch ( retriveEntity.getType() ) {
                case IMAGE_TYPE -> {
                    List< DataObjectImageEntity > dataObjectImageEntities = new ArrayList<>();
                    for ( SectionObjectEntity sectionObjectEntity : sectionObjectEntities ) {
                        DataObjectImageEntity dataObjectImageEntity = ( DataObjectImageEntity ) sectionObjectEntity.getObjectEntity();
                        if ( isFileExitInDataObjectEntity( dataObjectImageEntity ) ) {
                            dataObjectImageEntities.add( dataObjectImageEntity );
                        }
                    }
                    return prepareImageSectionPdf( dataObjectImageEntities );
                }
                case CURVE_TYPE -> {
                    List< DataObjectCurveDTO > dataObjectCurveDTOs = new ArrayList<>();
                    for ( SectionObjectEntity sectionObjectEntity : sectionObjectEntities ) {
                        DataObjectCurveEntity dataObjectCurveEntity = ( DataObjectCurveEntity ) sectionObjectEntity.getObjectEntity();
                        if ( isFileExitInDataObjectEntity( dataObjectCurveEntity ) ) {
                            dataObjectCurveDTOs.add(
                                    dataObjectManager.getDataObjectCurve( entityManager, dataObjectCurveEntity.getComposedId().getId(),
                                            null ) );
                        }
                    }
                    return prepareCurveSectionPdf( dataObjectCurveDTOs );
                }
                case MOVIE_TYPE -> {
                    List< DataObjectMovieEntity > dataObjectMovieEntities = new ArrayList<>();
                    for ( SectionObjectEntity sectionObjectEntity : sectionObjectEntities ) {
                        DataObjectMovieEntity dataObjectMovieEntity = ( DataObjectMovieEntity ) sectionObjectEntity.getObjectEntity();
                        if ( isFileExitInDataObjectEntity( dataObjectMovieEntity ) ) {
                            dataObjectMovieEntities.add( dataObjectMovieEntity );
                        }
                    }
                    return prepareMovieSectionPdf( dataObjectMovieEntities );
                }
                case VALUE_TYPE -> {
                    List< DataObjectValueEntity > dataObjectValueEntities = new ArrayList<>();
                    for ( SectionObjectEntity sectionObjectEntity : sectionObjectEntities ) {
                        dataObjectValueEntities.add( ( DataObjectValueEntity ) sectionObjectEntity.getObjectEntity() );
                    }
                    return prepareValueSectionPdf( entityManager, dataObjectValueEntities );
                }
                default -> throw new SusException( "Invalid type provided." );
            }
        }
        return null;
    }

    /**
     * Prepare value section pdf.
     *
     * @param entityManager
     *         the entity manager
     * @param dataObjectValueEntities
     *         the data object value entities
     *
     * @return the pdf P table
     *
     * @throws IOException
     *         the io exception
     */
    private PdfPTable prepareValueSectionPdf( EntityManager entityManager, List< DataObjectValueEntity > dataObjectValueEntities )
            throws java.io.IOException {
        PdfPTable table = new PdfPTable( 4 );
        table.setWidthPercentage( 100 );
        table.getDefaultCell().setHorizontalAlignment( Element.ALIGN_CENTER );
        table.getDefaultCell().setVerticalAlignment( Element.ALIGN_MIDDLE );
        table.getDefaultCell().setFixedHeight( 70 );
        // header row:
        table.addCell( "Name" );
        table.addCell( "Dimension" );
        table.addCell( "Unit" );
        table.addCell( VALUE );

        // many data rows:
        for ( DataObjectValueEntity dataObjectValueEntity : dataObjectValueEntities ) {
            DataObjectValueDTO dataObjectValue = dataObjectManager.getDataObjectValue( entityManager,
                    dataObjectValueEntity.getComposedId().getId(), null );
            table.addCell( dataObjectValue.getName() );
            table.addCell( dataObjectValue.getDimension() );
            table.addCell( dataObjectValue.getUnit() );
            table.addCell( dataObjectValue.getValue() );
        }
        return table;
    }

    /**
     * Prepare movie section pdf.
     *
     * @param dataObjectMovieEntities
     *         the data object movie entities
     *
     * @return the pdf P table
     *
     * @throws BadElementException
     *         the bad element exception
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private PdfPTable prepareMovieSectionPdf( List< DataObjectMovieEntity > dataObjectMovieEntities )
            throws BadElementException, java.io.IOException {
        PdfPTable table = new PdfPTable( dataObjectMovieEntities.size() );
        table.setWidthPercentage( 100 );
        for ( DataObjectMovieEntity dataObjectMovieEntity : dataObjectMovieEntities ) {
            String url = getDataObjectMovieSrc( dataObjectMovieEntity );
            table.addCell( createImageCell( url ) );
        }
        return table;
    }

    /**
     * Gets the data object movie src.
     *
     * @param dataObjectMovieEntity
     *         the data object movie entity
     *
     * @return the data object movie src
     */
    private String getDataObjectMovieSrc( DataObjectMovieEntity dataObjectMovieEntity ) {
        if ( dataObjectMovieEntity.getFile() != null ) {
            return CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH
                    + dataObjectMovieEntity.getThumbnail().getFilePath() + File.separator + FilenameUtils.removeExtension(
                    dataObjectMovieEntity.getThumbnail().getFileName() ) + ConstantsString.OBJECT_THUMB_NAIL_FILE_POSTFIX
                    + FilenameUtils.EXTENSION_SEPARATOR + ConstantsFileExtension.PNG;
        }
        return null;
    }

    /**
     * Prepare curve section pdf.
     *
     * @param dataObjectCurveDTOs
     *         the data object curve dt os
     *
     * @return the file
     *
     * @throws BadElementException
     *         the bad element exception
     * @throws IOException
     *         the io exception
     */
    private PdfPTable prepareCurveSectionPdf( List< DataObjectCurveDTO > dataObjectCurveDTOs )
            throws BadElementException, java.io.IOException {
        PdfPTable table = new PdfPTable( 1 );
        table.setWidthPercentage( 100 );
        table.addCell( createImageCell( getDataObjectCurveSrc( dataObjectCurveDTOs ) ) );
        return table;
    }

    /**
     * Gets the data object curve src.
     *
     * @param dataObjectCurveDTOs
     *         the data object curve DT os
     *
     * @return the data object curve src
     */
    private String getDataObjectCurveSrc( List< DataObjectCurveDTO > dataObjectCurveDTOs ) {
        return CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.TMP_STATIC_PATH
                + File.separator + createCurveComparedImage( dataObjectCurveDTOs ).getName();
    }

    private static File createCurveComparedImage( List< DataObjectCurveDTO > objectCurveDTOs ) {

        List< List< double[] > > xyCoordinates = new ArrayList<>();
        List< String > xUnits = new ArrayList<>();
        List< String > yUnits = new ArrayList<>();
        List< String > xQuantityTypes = new ArrayList<>();
        List< String > yQuantityTypes = new ArrayList<>();
        List< String > xAxisLables = new ArrayList<>();
        List< String > yAxisLables = new ArrayList<>();
        List< String > strChartLabels = new ArrayList<>();
        String strFileFormate = "jpeg";
        Integer heigth = 1920;
        Integer width = 1080;

        for ( DataObjectCurveDTO dataObjectCurveDTO : objectCurveDTOs ) {
            if ( dataObjectCurveDTO != null ) {
                xyCoordinates.add( dataObjectCurveDTO.getCurve() );
                xUnits.add( dataObjectCurveDTO.getxUnit() );
                yUnits.add( dataObjectCurveDTO.getyUnit() );
                String xQuantityType = dataObjectCurveDTO.getxDimension();
                xQuantityTypes.add( xQuantityType );
                String yQuantityType = dataObjectCurveDTO.getyDimension();
                yQuantityTypes.add( yQuantityType );
                String xAxisLable = xQuantityType + " (" + xUnits + ")";
                xAxisLables.add( xAxisLable );
                String yAxisLable = yQuantityType + " (" + yUnits + ")";
                yAxisLables.add( yAxisLable );
                String strChartLabel = dataObjectCurveDTO.getName();
                strChartLabels.add( strChartLabel );
            }
        }

        byte[] byteArrCureImage = ImageUtil.createImageByXyCharts( strChartLabels, strFileFormate, heigth, width, xyCoordinates );

        String fileName = UUID.randomUUID() + ".jpeg";
        File thumbNailFileToCopyInfeStatic = new File( PropertiesManager.getDefaultServerTempPath() + File.separator + fileName );
        try ( ByteArrayInputStream bais = new ByteArrayInputStream( byteArrCureImage ); FileOutputStream fos = new FileOutputStream(
                thumbNailFileToCopyInfeStatic ) ) {
            IOUtils.copy( bais, fos );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
        return thumbNailFileToCopyInfeStatic;
    }

    /**
     * Prepare image section pdf.
     *
     * @param dataObjectImageEntities
     *         the data object image entities
     *
     * @return the file
     *
     * @throws BadElementException
     *         the bad element exception
     * @throws IOException
     *         the io exception
     */
    private PdfPTable prepareImageSectionPdf( List< DataObjectImageEntity > dataObjectImageEntities )
            throws BadElementException, java.io.IOException {
        PdfPTable table = new PdfPTable( dataObjectImageEntities.size() );
        table.setWidthPercentage( 100 );
        for ( DataObjectImageEntity dataObjectImageEntity : dataObjectImageEntities ) {
            table.addCell( createImageCell( getDataObjectImageSrc( dataObjectImageEntity ) ) );
        }
        return table;
    }

    /**
     * Creates the image cell.
     *
     * @param path
     *         the path
     *
     * @return the pdf P cell
     *
     * @throws BadElementException
     *         the bad element exception
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private PdfPCell createImageCell( String path ) throws BadElementException, java.io.IOException {
        return new PdfPCell( Image.getInstance( path.replaceAll( " ", "%20" ) ), true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainerTag previewReport( UUID reportId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ReportEntity reportEntity = ( ReportEntity ) susDAO.getLatestNonDeletedObjectById( entityManager, reportId );
            if ( reportEntity == null ) {
                throw new SusException( "report not found." );
            }
            List< SectionEntity > sectionEntities = getSectionsByReportId( entityManager, reportId );
            return CollectionUtils.isNotEmpty( sectionEntities ) ? getReportContainerDiv( entityManager, reportEntity.getName(),
                    sectionEntities ) : null;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String downloadPreviewReportPdf( UUID reportId ) {
        return PropertiesManager.getLocationURL() + "/api/report/" + reportId + "/pdf/link";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String downloadPreviewSectionPdf( UUID sectionId ) {
        return PropertiesManager.getLocationURL() + "/api/report/section/" + sectionId + "/pdf/link";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String downloadPreviewReportDocx( UUID reportId ) {
        return PropertiesManager.getLocationURL() + "/api/report/" + reportId + "/docx/link";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String downloadPreviewSectionDocx( UUID sectionId ) {
        return PropertiesManager.getLocationURL() + "/api/report/section/" + sectionId + "/docx/link";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SectionDTO > getSectionOrderList( String userId, UUID reportId, List< UUID > orderIds ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< SectionEntity > entityList = sectionDAO.getObjectListByProperty( entityManager, REPORT_ENTITY, reportId );
            if ( CollectionUtils.isNotEmpty( entityList ) && CollectionUtils.isNotEmpty( orderIds ) ) {
                int i = 1;
                for ( UUID orderId : orderIds ) {
                    SectionEntity sectionToBeUpdate = sectionDAO.getLatestObjectById( entityManager, SectionEntity.class, orderId );
                    sectionToBeUpdate.setSectionOrder( i );
                    sectionDAO.saveOrUpdate( entityManager, sectionToBeUpdate );
                    i++;
                }
            }
            return prepareListOfSectionDTO( sectionDAO.getSectionsByReportId( entityManager, reportId ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the section S by report id.
     *
     * @param entityManager
     *         the entity manager
     * @param reportId
     *         the report id
     *
     * @return the section S by report id
     */
    private List< SectionEntity > getSectionsByReportId( EntityManager entityManager, UUID reportId ) {
        return sectionDAO.getSectionsByReportId( entityManager, reportId );
    }

    /**
     * Gets the report container div.
     *
     * @param entityManager
     *         the entity manager
     * @param reportName
     *         the report name
     * @param sectionEntities
     *         the section entities
     *
     * @return the report container div
     */
    private ContainerTag getReportContainerDiv( EntityManager entityManager, String reportName, List< SectionEntity > sectionEntities ) {

        return div(
                /* Title ***/
                h1( reportName ),

                /* Table of contents ***/
                h2( "Table of contents" ).withId( "100" ),

                ul( each( filter( sectionEntities, sectionEntity -> sectionEntity != null ), sectionEntity -> li( attrs( ".sectionEntity" ),
                                a( sectionEntity.getName() ).withHref( "#" + sectionEntity.getId().toString() ).withClass( "handle-anchor-links" )

                        ).withClass( "list-group-item" ) )

                ).withClass( "list-group" ),

                each( sectionEntities, sectionEntity -> {
                    try {
                        return div( generateSectionPreview( entityManager, sectionEntity.getId() ) );
                    } catch ( java.io.IOException e ) {
                        throw new SusException( e.getMessage() );
                    }
                } )

        );
    }

    /**
     * Prepare image section.
     *
     * @param sectionId
     *         the section id
     * @param sectionNmae
     *         the section nmae
     * @param dataObjectImageEntities
     *         the data object image entities
     *
     * @return the container tag
     */
    private ContainerTag prepareImageSection( String sectionId, String sectionNmae,
            List< DataObjectImageEntity > dataObjectImageEntities ) {
        if ( dataObjectImageEntities == null ) {
            return div( h2( sectionNmae ).withId( "300" ) ).withId( sectionId );
        } else {
            return div( h2( sectionNmae ).withId( "300" ), table( tbody( tr( each( dataObjectImageEntities, dataObjectImageEntity -> td(
                    img().withSrc( getDataObjectImageSrc( dataObjectImageEntity ) )
                            .withAlt( dataObjectImageEntity.getComposedId().getId().toString() )
                            .attr( "width=\"400\"" ) ) ) ) ) ).withClass( "table table-width-auto table-bordered" ) ).withId( sectionId );
        }
    }

    /**
     * Data object image src.
     *
     * @param dataObjectImageEntity
     *         the data object image entity
     *
     * @return the string
     */
    private String getDataObjectImageSrc( DataObjectImageEntity dataObjectImageEntity ) {
        if ( dataObjectImageEntity.getFile() != null ) {
            return PropertiesManager.getWebBaseURL() + File.separator + ConstantsString.STATIC_PATH + dataObjectImageEntity.getFile()
                    .getFilePath() + File.separator + dataObjectImageEntity.getFile().getFileName();
        }
        return null;
    }

    /**
     * Prepare curve section.
     *
     * @param sectionId
     *         the section id
     * @param sectionNmae
     *         the section nmae
     * @param dataObjectCurveDTOs
     *         the data object curve dt os
     *
     * @return the container tag
     */
    private ContainerTag prepareCurveSection( String sectionId, String sectionNmae, List< DataObjectCurveDTO > dataObjectCurveDTOs ) {
        if ( dataObjectCurveDTOs == null ) {
            return div( h2( sectionNmae ).withId( "300" ) ).withId( sectionId );
        } else {
            return div( h2( sectionNmae ).withId( "300" ), table( tbody(
                    tr( td( img().withSrc( getDataObjectCurveSrc( dataObjectCurveDTOs ) ).withAlt( UUID.randomUUID().toString() )
                            .attr( "width=\"1250\"" ) ) ) ) ).withClass( "table table-width-auto table-bordered" ) ).withId( sectionId );
        }
    }

    /**
     * Prepare movie section.
     *
     * @param sectionId
     *         the section id
     * @param sectionNmae
     *         the section nmae
     * @param dataObjectMovieDTOs
     *         the data object movie dt os
     *
     * @return the container tag
     */
    private ContainerTag prepareMovieSection( String sectionId, String sectionNmae, List< DataObjectMovieDTO > dataObjectMovieDTOs ) {
        if ( dataObjectMovieDTOs == null ) {
            return div( h2( sectionNmae ).withId( "300" ) ).withId( sectionId );
        } else {
            return div( h2( sectionNmae ).withId( "300" ), table( tbody( tr( each( dataObjectMovieDTOs, dataObjectMovieDTO -> td(
                    video( source().withSrc( getMovieThumbSrc( dataObjectMovieDTO ) ).withType( "video/mp4" ) ).withText( "media" )
                            .attr( "width=\"400\"" ).attr( "height=\"400\"" ).attr( "controls" ) ) ) ) ) ).withClass(
                    "table table-width-auto table-bordered" ) ).withId( sectionId );
        }
    }

    /**
     * Gets the movie thumb src.
     *
     * @param dataObjectMovieDTO
     *         the data object movie DTO
     *
     * @return the movie thumb src
     */
    private String getMovieThumbSrc( DataObjectMovieDTO dataObjectMovieDTO ) {
        if ( dataObjectMovieDTO.getSources() != null ) {
            return dataObjectMovieDTO.getSources().getWebm();
        }
        return null;
    }

    /**
     * Prepare table sections.
     *
     * @param sectionId
     *         the section id
     * @param sectionNmae
     *         the section nmae
     * @param dataObjectValueDTOs
     *         the data object value dt os
     *
     * @return the container tag
     */
    private ContainerTag prepareTableSections( String sectionId, String sectionNmae, List< DataObjectValueDTO > dataObjectValueDTOs ) {
        if ( dataObjectValueDTOs == null ) {
            return div( h2( sectionNmae ).withId( "200" ) ).withId( sectionId );
        } else {
            return div( h2( sectionNmae ).withId( "200" ), table( thead( tr( th( "Name" ), th( "Dimension" ), th( "Unit" ), th( VALUE ) ) ),
                    tbody( each( dataObjectValueDTOs,
                            dataObjectValueDTO -> tr( td( dataObjectValueDTO.getName() ), td( dataObjectValueDTO.getDimension() ),
                                    td( dataObjectValueDTO.getUnit() ), td( dataObjectValueDTO.getValue() ) ) ) ) ).withClass(
                    "table table-width-auto table-bordered" ) ).withId( sectionId );
        }
    }

    /**
     * Attach new objects against section.
     *
     * @param entityManager
     *         the entity manager
     * @param returnedSectionEntity
     *         the returned section entity
     */
    private void attachNewObjectsAgainstSection( EntityManager entityManager, SectionEntity returnedSectionEntity ) {
        List< UUID > selectedItems = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                returnedSectionEntity.getSelectionId().toString() );
        if ( CollectionUtils.isNotEmpty( selectedItems ) ) {
            if ( returnedSectionEntity.getType().equals( CURVE_TYPE ) ) {
                validateCurveDimensions( entityManager, selectedItems );
            }
            for ( UUID uuid : selectedItems ) {
                attachObjectToSection( entityManager, returnedSectionEntity, uuid );
            }
        }
    }

    /**
     * Validate curve dimensions.
     *
     * @param entityManager
     *         the entity manager
     * @param selectedItems
     *         the selected items
     */
    private void validateCurveDimensions( EntityManager entityManager, List< UUID > selectedItems ) {
        List< DataObjectCurveDTO > dataObjectCurveDTOs = new ArrayList<>();
        List< SuSEntity > suSEntities = susDAO.getObjectsByListOfIds( entityManager, selectedItems );
        for ( SuSEntity susEntity : suSEntities ) {
            if ( susEntity instanceof DataObjectCurveEntity ) {
                DataObjectCurveDTO dataObjectCurveDTO = dataObjectManager.getDataObjectCurve( entityManager,
                        susEntity.getComposedId().getId(), null );
                if ( dataObjectCurveDTO != null ) {
                    dataObjectCurveDTOs.add( dataObjectCurveDTO );
                }
            }
            if ( CollectionUtils.isEmpty( dataObjectCurveDTOs ) ) {
                throw new SusException( "No curve files are attached" );
            }
            if ( !isUniqueDimensions( dataObjectCurveDTOs ) ) {
                throw new SusException( "Curve dimensions must be same" );
            }
        }
    }

    /**
     * Checks if is unique dimensions.
     *
     * @param dataObjectCurveDTOs
     *         the data object curve DT os
     *
     * @return true, if is unique dimensions
     */
    private boolean isUniqueDimensions( List< DataObjectCurveDTO > dataObjectCurveDTOs ) {
        boolean isSame = false;
        Optional< DataObjectCurveDTO > findFirst = dataObjectCurveDTOs.stream().findFirst();
        DataObjectCurveDTO dataObjectCurve = findFirst.orElse( null );
        if ( null != dataObjectCurve ) {
            isSame = dataObjectCurveDTOs.stream().allMatch( curve -> isDimensionSame( curve, dataObjectCurve ) );
        }
        return isSame;
    }

    /**
     * Checks if is unique dimensions.
     *
     * @param curve1
     *         the curve 1
     * @param curve2
     *         the curve 2
     *
     * @return true, if is unique dimensions
     */
    private boolean isDimensionSame( DataObjectCurveDTO curve1, DataObjectCurveDTO curve2 ) {
        if ( curve1.getxDimension() == null || curve2.getxDimension() == null ) {
            throw new SusException( "Curve file is missing in one or more objects" );
        } else {
            return curve1.getxDimension().equals( curve2.getxDimension() ) && curve1.getyDimension().equals( curve2.getyDimension() );
        }
    }

    /**
     * Removes the old objects from section.
     *
     * @param entityManager
     *         the entity manager
     * @param returnedSectionEntity
     *         the returned section entity
     */
    private void removeOldObjectsFromSection( EntityManager entityManager, SectionEntity returnedSectionEntity ) {
        List< SectionObjectEntity > sectionObjectEntities = sectionObjectDAO.getSectionObjectEntityBySectionId( entityManager,
                returnedSectionEntity.getId() );
        if ( CollectionUtils.isNotEmpty( sectionObjectEntities ) ) {
            for ( SectionObjectEntity sectionObjectEntity : sectionObjectEntities ) {
                sectionObjectDAO.delete( entityManager, sectionObjectEntity );
            }
        }
    }

    /**
     * Attach object to section.
     *
     * @param entityManager
     *         the entity manager
     * @param sectionEntity
     *         the section entity
     * @param objectId
     *         the object id
     */
    private void attachObjectToSection( EntityManager entityManager, SectionEntity sectionEntity, UUID objectId ) {
        SuSEntity susEntity = susDAO.getLatestNonDeletedObjectById( entityManager, objectId );
        if ( susEntity != null ) {
            SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                    susEntity.getConfig() );
            if ( !sectionEntity.getType().equals( susObjectModel.getClassName() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.SECTION_MISMATCHED.getKey(), susEntity.getName(),
                        getType( sectionEntity.getType() ) ) );
            } else {
                sectionObjectDAO.save( entityManager, prepareSectionObjectEntity( sectionEntity, susEntity ) );
            }
        }
    }

    /**
     * Gets the type.
     *
     * @param type
     *         the type
     *
     * @return the type
     */
    private String getType( String type ) {
        return switch ( type ) {
            case IMAGE_TYPE -> IMAGE;
            case CURVE_TYPE -> CURVE;
            case MOVIE_TYPE -> MOVIE;
            case VALUE_TYPE -> VALUE;
            default -> throw new SusException( "Invalid type provided!" );
        };
    }

    /**
     * Prepare section object entity.
     *
     * @param sectionEntity
     *         the section entity
     * @param susEntity
     *         the sus entity
     *
     * @return the section object entity
     */
    private SectionObjectEntity prepareSectionObjectEntity( SectionEntity sectionEntity, SuSEntity susEntity ) {
        SectionObjectEntity sectionObjectEntity = new SectionObjectEntity();
        sectionObjectEntity.setId( UUID.randomUUID() );
        sectionObjectEntity.setObjectEntity( susEntity );
        sectionObjectEntity.setSectionEntity( sectionEntity );
        return sectionObjectEntity;
    }

    /**
     * Prepare section DTO from section entity.
     *
     * @param returnedSectionEntity
     *         the returned section entity
     *
     * @return the section DTO
     */
    private SectionDTO prepareSectionDTOFromSectionEntity( SectionEntity returnedSectionEntity ) {
        SectionDTO dto = null;
        if ( returnedSectionEntity != null ) {
            dto = new SectionDTO( returnedSectionEntity.getId(), returnedSectionEntity.getTitle(), returnedSectionEntity.getName(),
                    returnedSectionEntity.getDescription(), returnedSectionEntity.getType(), returnedSectionEntity.getSelectionId() );
        }
        return dto;
    }

    /**
     * Prepare section entity from section DTO.
     *
     * @param section
     *         the section
     * @param isUpdate
     *         the is update
     *
     * @return the section entity
     */
    private SectionEntity prepareSectionEntityFromSectionDTO( SectionDTO section ) {
        return new SectionEntity( UUID.randomUUID(), section.getTitle(), section.getName(), section.getDescription(), section.getType(),
                section.getSelectionId() );
    }

    /**
     * Prepare dto entity from dto DTO.
     *
     * @param dto
     *         the dto
     * @param isUpdate
     *         the is update
     */
    private void prepareSectionEntityFromSectionDTO( SectionDTO dto, SectionEntity entity ) {
        entity.setTitle( dto.getTitle() );
        entity.setName( dto.getName() );
        entity.setDescription( dto.getDescription() );
        entity.setType( dto.getType() );
        entity.setSelectionId( dto.getSelectionId() );
    }

    /**
     * Gets the section.
     *
     * @param entityManager
     *         the entity manager
     * @param sectionId
     *         the section id
     *
     * @return the section
     */
    private SectionEntity getSection( EntityManager entityManager, UUID sectionId ) {
        SectionEntity retriveEntity = sectionDAO.getLatestObjectById( entityManager, SectionEntity.class, sectionId );
        if ( retriveEntity == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SECTION_DETAIL.getKey(), sectionId ) );
        }
        return retriveEntity;
    }

    /**
     * Sets the report DAO.
     *
     * @param reportDAO
     *         the new report DAO
     */
    public void setReportDAO( ReportDAO reportDAO ) {
        this.reportDAO = reportDAO;
    }

    /**
     * Sets the section DAO.
     *
     * @param sectionDAO
     *         the new section DAO
     */
    public void setSectionDAO( SectionDAO sectionDAO ) {
        this.sectionDAO = sectionDAO;
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
     * Gets the config manager.
     *
     * @return the config manager
     */
    public ObjectTypeConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Sets the config manager.
     *
     * @param configManager
     *         the new config manager
     */
    public void setConfigManager( ObjectTypeConfigManager configManager ) {
        this.configManager = configManager;
    }

    /**
     * Sets the section object DAO.
     *
     * @param sectionObjectDAO
     *         the new section object DAO
     */
    public void setSectionObjectDAO( SectionObjectDAO sectionObjectDAO ) {
        this.sectionObjectDAO = sectionObjectDAO;
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
     * Sets the data manager.
     *
     * @param dataObjectManager
     *         the new data manager
     */
    public void setDataObjectManager( DataObjectManager dataObjectManager ) {
        this.dataObjectManager = dataObjectManager;
    }

    /**
     * Gets permission manager.
     *
     * @return the permission manager
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * Sets permission manager.
     *
     * @param permissionManager
     *         the permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}

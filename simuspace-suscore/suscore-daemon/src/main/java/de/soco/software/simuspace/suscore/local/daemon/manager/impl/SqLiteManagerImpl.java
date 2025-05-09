package de.soco.software.simuspace.suscore.local.daemon.manager.impl;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDate;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsOperators;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.local.daemon.dao.AllObjectDAO;
import de.soco.software.simuspace.suscore.local.daemon.dao.ComputedObjectDAO;
import de.soco.software.simuspace.suscore.local.daemon.dao.LocationDAO;
import de.soco.software.simuspace.suscore.local.daemon.dao.UserDaemonDAO;
import de.soco.software.simuspace.suscore.local.daemon.entity.AllObjectEntity;
import de.soco.software.simuspace.suscore.local.daemon.entity.ComputedDataObjectEntity;
import de.soco.software.simuspace.suscore.local.daemon.entity.LocationDTO;
import de.soco.software.simuspace.suscore.local.daemon.entity.UserDaemonEntity;
import de.soco.software.simuspace.suscore.local.daemon.manager.SqLiteManager;

/**
 * The Class SqLiteManagerImpl for CRUD repository methods and custom method provider.
 *
 * @author noman.arshad
 */
@Service
public class SqLiteManagerImpl implements SqLiteManager {

    /**
     * The Constant NAME.
     */
    private static final String NAME = "name";

    /**
     * The Constant DESCRIPTION.
     */
    private static final String DESCRIPTION = "description";

    /**
     * The Constant CREATED_ON_FIELD.
     */
    private static final String CREATED_ON_FIELD = "createdOn";

    /**
     * The sql lite dao.
     */
    @Autowired
    private ComputedObjectDAO sqLiteDao;

    /**
     * The all object DTO.
     */
    @Autowired
    private AllObjectDAO allObjectDTO;

    /**
     * The location DAO.
     */
    @Autowired
    private LocationDAO locationDAO;

    /**
     * The user daemon DAO.
     */
    @Autowired
    private UserDaemonDAO userDaemonDAO;

    /**
     * The em.
     */
    @PersistenceContext
    private EntityManager em;

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.manager.SqLiteManager#getAllObjects()
     */
    @Override
    public List< ComputedDataObjectEntity > getAllObjects() {
        return ( List< ComputedDataObjectEntity > ) sqLiteDao.findAll();

    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.manager.SqLiteManager#save(de.soco.software.simuspace.suscore.local.daemon.model.ComputedDataObjectEntity)
     */

    @Override
    public void save( ComputedDataObjectEntity object ) {
        // it act as save and also update as well
        sqLiteDao.save( object );

    }

    @Override
    public void saveUser( UserDaemonEntity object ) {
        // it act as save and also update as well
        userDaemonDAO.save( object );

    }

    @Override
    public UserDaemonEntity findUserById( String id ) {
        // it act as save and also update as well
        return userDaemonDAO.findById( id );

    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.manager.SqLiteManager#deleteAll()
     */
    @Override
    public void deleteAll() {
        sqLiteDao.deleteAll();
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.manager.SqLiteManager#delete(de.soco.software.simuspace.suscore.local.daemon.model.ComputedDataObjectEntity)
     */
    @Override
    public void delete( ComputedDataObjectEntity object ) {
        sqLiteDao.delete( object );
    }

    /* (non-Javadoc)
     */
    @Override
    public ComputedDataObjectEntity findById( String id ) {
        return sqLiteDao.findById( id );
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.manager.SqLiteManager#findByName(java.lang.String)
     */
    @Override
    public ComputedDataObjectEntity findByName( String name ) {
        return sqLiteDao.findByName( name );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ComputedDataObjectEntity findByPath( String path ) {
        return sqLiteDao.findByPath( path );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ComputedDataObjectEntity > findBycontainerId( String containerId ) {
        return sqLiteDao.findByContainer( containerId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ComputedDataObjectEntity findByPId( String id ) {
        return sqLiteDao.findById( id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< AllObjectEntity > saveBulkData( List< AllObjectEntity > object ) {
        List< LocationDTO > location = new ArrayList< LocationDTO >();
        for ( AllObjectEntity allObjectEntity : object ) {
            if ( CollectionUtils.isNotEmpty( allObjectEntity.getLocations() ) ) {
                location.addAll( allObjectEntity.getLocations() );
            }
        }
        locationDAO.saveAll( location );
        allObjectDTO.saveAll( object );
        // locationDAO.save( location );
        // allObjectDTO.save( object );
        return object;

    }

    @Override
    public AllObjectEntity saveAllObjectEntity( AllObjectEntity object ) {
        if ( CollectionUtils.isNotEmpty( object.getLocations() ) ) {
            locationDAO.saveAll( object.getLocations() );
        }
        allObjectDTO.save( object );
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< AllObjectEntity > listAllByPage( FiltersDTO filtersDTO, int totalRecords ) {
        Criteria critaria = getFilteredResults( AllObjectEntity.class, filtersDTO );
        filtersDTO.setFilteredRecords( ( long ) critaria.list().size() );

        int pageSize = ( totalRecords - filtersDTO.getStart() ) <= filtersDTO.getLength() ? ( totalRecords - filtersDTO.getStart() )
                : filtersDTO.getLength();
        addSortingInCriteria( critaria, filtersDTO, pageSize );
        @SuppressWarnings( "unchecked" )
        List< AllObjectEntity > list = critaria.list();
        return list;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllObject() {
        allObjectDTO.deleteAll();
    }

    /**
     * Gets the filtered results.
     *
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the filtered results
     */
    private Criteria getFilteredResults( Class< ? > entityClazz, FiltersDTO filtersDTO ) {
        Criteria criteria = em.unwrap( Session.class ).createCriteria( entityClazz );
        Disjunction or = Restrictions.disjunction();
        Conjunction and = Restrictions.conjunction();
        if ( filtersDTO.getColumns() != null ) {
            Map< String, String > map = new HashMap<>();
            for ( FilterColumn filterColumn : filtersDTO.getColumns() ) {
                String alias = ConstantsString.EMPTY_STRING;
                String aliasColumn = ConstantsString.EMPTY_STRING;
                String fieldName;
                if ( filterColumn.getName().contains( ConstantsString.DOT ) ) {
                    fieldName = filterColumn.getName().substring( 0, filterColumn.getName().indexOf( ConstantsString.DOT ) );
                    if ( ReflectionUtils.hasField( entityClazz, fieldName )
                            && isEntity( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                        alias = RandomStringUtils.random( ConstantsInteger.INTEGER_VALUE_TEN, true, false );
                        aliasColumn = addAliasToCriteria( criteria, map, filterColumn, alias, fieldName );
                    }

                } else {
                    fieldName = filterColumn.getName();
                }

                if ( filterColumn.getFilters() != null ) {
                    for ( Filter filter : filterColumn.getFilters() ) {
                        try {

                            if ( !alias.isEmpty() ) {
                                populateJunction( aliasColumn, filter.getValue(), filter.getCondition(), filter.getOperator(), or, and );
                            }
                            if ( Integer.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                populateJunction( filterColumn.getName(), Integer.parseInt( filter.getValue() ), filter.getCondition(),
                                        filter.getOperator(), or, and );
                            } else if ( Double.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                populateJunction( filterColumn.getName(), Double.parseDouble( filter.getValue() ), filter.getCondition(),
                                        filter.getOperator(), or, and );
                            } else if ( Float.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                populateJunction( filterColumn.getName(), Float.parseFloat( filter.getValue() ), filter.getCondition(),
                                        filter.getOperator(), or, and );
                            } else if ( UUID.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                populateJunction( filterColumn.getName(), UUID.fromString( filter.getValue() ), filter.getCondition(),
                                        filter.getOperator(), or, and );
                            } else if ( Boolean.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                if ( ConstantsStatus.ACTIVE.equalsIgnoreCase( filter.getValue() ) ) {
                                    populateJunction( filterColumn.getName(), true, filter.getCondition(), filter.getOperator(), or, and );
                                } else if ( ConstantsStatus.DISABLE.equalsIgnoreCase( filter.getValue() )
                                        || ConstantsStatus.INACTIVE.equalsIgnoreCase( filter.getValue() ) ) {
                                    populateJunction( filterColumn.getName(), false, filter.getCondition(), filter.getOperator(), or, and );
                                } else if ( ConstantsStatus.YES.equalsIgnoreCase( filter.getValue() ) ) {
                                    populateJunction( filterColumn.getName(), true, filter.getCondition(), filter.getOperator(), or, and );
                                } else if ( ConstantsStatus.NO.equalsIgnoreCase( filter.getValue() ) ) {
                                    populateJunction( filterColumn.getName(), false, filter.getCondition(), filter.getOperator(), or, and );
                                } else {
                                    populateJunction( filterColumn.getName(), null, filter.getCondition(), filter.getOperator(), or, and );
                                }
                            } else if ( Date.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                populateJunctionForDates( filterColumn.getName(), filter.getValue(), filter.getCondition(),
                                        filter.getOperator(), or, and );
                            } else if ( String.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                populateJunction( filterColumn.getName(), filter.getValue(), filter.getCondition(), filter.getOperator(),
                                        or, and );
                            }
                        } catch ( Exception e ) {
                            ExceptionLogger.logException( e, getClass() );
                            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE_PROVIDED_FOR_COLUMN.getKey(),
                                    filter.getValue(), filterColumn.getName() ) );
                        }
                    }
                }

                // Add search on name and description column
                if ( isSearchColumn( filterColumn.getName() ) ) {
                    Criterion criterion = searchOnAnyColumn( filtersDTO.getSearch(), filterColumn.getName(), ConstantsString.EMPTY_STRING,
                            ConstantsString.EMPTY_STRING, entityClazz );
                    if ( null != criterion ) {
                        or.add( criterion );
                    }
                }

            }
        }

        criteria.add( and );
        criteria.add( or );

        return criteria;
    }

    /**
     * Populate junction.
     *
     * @param column
     *         the column
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     */
    private void populateJunction( String column, Object value, String condition, String operator, Disjunction or, Conjunction and ) {
        if ( operator.equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.eq( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ne( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_IN.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ilike( column, value.toString(), MatchMode.ANYWHERE ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_NOT_IN.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and,
                    Restrictions.not( Restrictions.ilike( column, value.toString(), MatchMode.ANYWHERE ) ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEGINS_WITH.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ilike( column, value.toString(), MatchMode.START ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.ENDS_WITH.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ilike( column, value.toString(), MatchMode.END ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.gt( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ge( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.lt( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.le( column, value ) );
        }

    }

    /**
     * Adds the logical operator to criteria.
     *
     * @param condition
     *         the condition
     * @param or
     *         the or
     * @param and
     *         the and
     * @param criterion
     *         the criterion
     */
    private void addLogicalOperatorToCriteria( String condition, Disjunction or, Conjunction and, Criterion criterion ) {
        if ( condition.equalsIgnoreCase( ConstantsOperators.OR.getName() ) ) {
            or.add( criterion );
        } else {
            and.add( criterion );
        }
    }

    /**
     * Populates junction for Dates.
     *
     * @param column
     *         the column
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     */

    private void populateJunctionForDates( String column, String value, String condition, String operator, Disjunction or,
            Conjunction and ) {

        if ( value.equals( "Today" ) ) {
            addFilterForSingleDay( column, DateUtils.getCurrentDateWithoutTime(), condition, operator, or, and );

        } else if ( value.equals( "Yesterday" ) ) {
            addFilterForSingleDay( column, DateUtils.getYesterdayDateWithoutTime(), condition, operator, or, and );

        } else if ( value.equals( "Last Week" ) ) {
            addFilterForDateRange( column, DateUtils.getLastWeekStartDate(), DateUtils.getLastWeekEndDate(), condition, operator, or, and );

        } else if ( value.equals( "This Week" ) ) {
            addFilterForDateRange( column, DateUtils.getThisWeekStartDate(), DateUtils.getThisWeekEndDate(), condition, operator, or, and );

        } else if ( value.equals( "Last Month" ) ) {
            addFilterForDateRange( column, DateUtils.getLastMonthStartDate(), DateUtils.getLastMonthEndDate(), condition, operator, or,
                    and );

        } else if ( value.equals( "This Month" ) ) {
            addFilterForDateRange( column, DateUtils.getThisMonthStartDate(), DateUtils.getThisMonthEndDate(), condition, operator, or,
                    and );

        } else if ( value.equals( "This Year" ) ) {
            addFilterForDateRange( column, DateUtils.getThisYearStartDate(), DateUtils.getThisYearEndDate(), condition, operator, or, and );

        } else if ( value.equals( "Last Year" ) ) {
            addFilterForDateRange( column, DateUtils.getLastYearStartDate(), DateUtils.getLastYearEndDate(), condition, operator, or, and );

        } else { // else if a particular date is selected
            addFilterForSingleDay( column, DateUtils.fromString( value, ConstantsDate.DATE_ONLY_FORMAT ), condition, operator, or, and );
        }
    }

    /**
     * Add filter for single day.
     *
     * @param column
     *         the column
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     */

    private void addFilterForSingleDay( String column, Date value, String condition, String operator, Disjunction or, Conjunction and ) {

        if ( operator.equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and,
                    Restrictions.and( Restrictions.ge( column, value ), Restrictions.le( column, DateUtils.getTomorrowDate( value ) ) ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and,
                    Restrictions.or( Restrictions.lt( column, value ), Restrictions.ge( column, DateUtils.getTomorrowDate( value ) ) ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.gt( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ge( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.lt( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.lt( column, DateUtils.getTomorrowDate( value ) ) );
        }
    }

    /**
     * Add filter for date range.
     *
     * @param column
     *         the column
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     */

    private void addFilterForDateRange( String column, Date startDate, Date endDate, String condition, String operator, Disjunction or,
            Conjunction and ) {

        if ( operator.equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and,
                    Restrictions.and( Restrictions.ge( column, startDate ), Restrictions.le( column, endDate ) ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and,
                    Restrictions.or( Restrictions.lt( column, startDate ), Restrictions.gt( column, endDate ) ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.gt( column, endDate ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ge( column, startDate ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.lt( column, startDate ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.le( column, endDate ) );
        }
    }

    /**
     * Adds the sorting in criteria.
     *
     * @param criteria
     *         the criteria
     * @param filtersDTO
     *         the filters DTO
     */
    private void addSortingInCriteria( Criteria criteria, FiltersDTO filtersDTO, int pageSize ) {
        boolean hasSorting = false;
        if ( filtersDTO.getColumns() != null ) {
            for ( FilterColumn filterColumn : filtersDTO.getColumns() ) {
                if ( ValidationUtils.isNotNullOrEmpty( filterColumn.getDir() ) ) {
                    hasSorting = true;
                    String columnName = filterColumn.getName();
                    if ( filterColumn.getName().contains( ConstantsString.DOT ) ) {
                        columnName = filterColumn.getName().substring( 0, filterColumn.getName().indexOf( ConstantsString.DOT ) );
                    }

                    if ( filterColumn.getDir().equalsIgnoreCase( ConstantsString.SORTING_DIRECTION_ASCENDING ) ) {

                        criteria.add( Restrictions.sqlRestriction( " 1=1 order by this_." + columnName + " ASC LIMIT "
                                + filtersDTO.getStart() + ConstantsString.COMMA + pageSize ) );

                    } else {
                        criteria.add( Restrictions.sqlRestriction( " 1=1 order by this_." + columnName + " DESC LIMIT "
                                + filtersDTO.getStart() + ConstantsString.COMMA + pageSize ) );
                    }
                }
            }
        }
        if ( !hasSorting ) {

            criteria.add( Restrictions.sqlRestriction( " 1=1 order by this_." + CREATED_ON_FIELD + " DESC LIMIT " + filtersDTO.getStart()
                    + ConstantsString.COMMA + pageSize ) );

        }
    }

    /**
     * Prepare a search on any field of entity.
     *
     * @param searchQuery
     *         the search query
     * @param columnName
     *         the column name
     * @param alias
     *         the alias
     * @param aliasColumn
     *         the alias column
     * @param entityClazz
     *         the entity clazz
     *
     * @return Criterion
     */
    private Criterion searchOnAnyColumn( Object searchQuery, String columnName, String alias, String aliasColumn, Class< ? > entityClazz ) {
        Criterion criterion = null;
        try {
            if ( searchQuery != null && StringUtils.isNotBlank( searchQuery.toString() ) ) {
                if ( !alias.isEmpty() ) {
                    criterion = Restrictions.ilike( aliasColumn, searchQuery.toString(), MatchMode.ANYWHERE );
                } else {
                    criterion = addSearchToColumns( searchQuery, columnName, entityClazz, criterion );
                }
            }

        } catch ( Exception e ) {
            ExceptionLogger.logException( e, e.getClass() );
        }
        return criterion;
    }

    /**
     * Adds the search to columns.
     *
     * @param searchQuery
     *         the search query
     * @param columnName
     *         the column name
     * @param entityClazz
     *         the entity clazz
     * @param criterion
     *         the criterion
     *
     * @return the criterion
     */
    private Criterion addSearchToColumns( Object searchQuery, String columnName, Class< ? > entityClazz, Criterion criterion ) {
        Class< ? > fieldType = ReflectionUtils.getFieldTypeByName( entityClazz, columnName );
        if ( searchQuery instanceof Integer && Integer.class.isAssignableFrom( fieldType ) ) {
            criterion = Restrictions.eq( columnName, Integer.parseInt( searchQuery.toString() ) );
        } else if ( searchQuery instanceof Double && Double.class.isAssignableFrom( fieldType ) ) {
            criterion = Restrictions.eq( columnName, Double.parseDouble( searchQuery.toString() ) );
        } else if ( searchQuery instanceof Float && Float.class.isAssignableFrom( fieldType ) ) {
            criterion = Restrictions.eq( columnName, Float.parseFloat( searchQuery.toString() ) );
        } else if ( searchQuery instanceof UUID && UUID.class.isAssignableFrom( fieldType ) ) {
            criterion = Restrictions.eq( columnName, UUID.fromString( searchQuery.toString() ) );
        } else if ( searchQuery instanceof Date && Date.class.isAssignableFrom( fieldType ) ) {
            criterion = Restrictions.eq( columnName, DateUtils.fromString( searchQuery.toString(), ConstantsDate.DATE_ONLY_FORMAT ) );
        } else if ( searchQuery instanceof String && String.class.isAssignableFrom( fieldType ) ) {
            criterion = Restrictions.ilike( columnName, searchQuery.toString(), MatchMode.ANYWHERE );
        } else if ( searchQuery instanceof Boolean || Boolean.class.isAssignableFrom( fieldType ) ) {
            criterion = appendStatusToCriteria( searchQuery, columnName, criterion );
        }
        return criterion;
    }

    /**
     * Append status to criteria.
     *
     * @param searchQuery
     *         the search query
     * @param columnName
     *         the column name
     * @param criterion
     *         the criterion
     *
     * @return the criterion
     */
    private Criterion appendStatusToCriteria( Object searchQuery, String columnName, Criterion criterion ) {
        if ( ConstantsStatus.ACTIVE.toLowerCase().contains( searchQuery.toString().toLowerCase() ) ) {
            criterion = Restrictions.eq( columnName, true );
        } else if ( ConstantsStatus.DISABLE.toLowerCase().contains( searchQuery.toString().toLowerCase() )
                || ConstantsStatus.INACTIVE.toLowerCase().contains( searchQuery.toString().toLowerCase() ) ) {
            criterion = Restrictions.eq( columnName, false );
        }
        return criterion;
    }

    /**
     * Check if the column is used for search field.
     *
     * @param column
     *         the column name
     *
     * @return true, if is Search Column
     */
    public boolean isSearchColumn( String column ) {
        return column.equalsIgnoreCase( NAME ) || column.equalsIgnoreCase( DESCRIPTION );
    }

    /**
     * Adds the alias to criteria.
     *
     * @param criteria
     *         the criteria
     * @param map
     *         the map
     * @param filterColumn
     *         the filter column
     * @param alias
     *         the alias
     * @param fieldName
     *         the field name
     *
     * @return the string
     */
    private String addAliasToCriteria( Criteria criteria, Map< String, String > map, FilterColumn filterColumn, String alias,
            String fieldName ) {
        String aliasColumn;
        if ( !map.containsKey( fieldName ) ) {
            criteria.createAlias( fieldName, alias, JoinType.LEFT_OUTER_JOIN );
            map.put( fieldName, alias );
            aliasColumn = alias + filterColumn.getName().substring( filterColumn.getName().indexOf( ConstantsString.DOT ) );
        } else {
            aliasColumn = map.get( fieldName ) + filterColumn.getName().substring( filterColumn.getName().indexOf( ConstantsString.DOT ) );
        }
        return aliasColumn;
    }

    /**
     * Checks if is entity.
     *
     * @param clazz
     *         the clazz
     *
     * @return true, if is entity
     */
    private boolean isEntity( Class< ? > clazz ) {
        return clazz != null && clazz.isAnnotationPresent( Entity.class );
    }

}
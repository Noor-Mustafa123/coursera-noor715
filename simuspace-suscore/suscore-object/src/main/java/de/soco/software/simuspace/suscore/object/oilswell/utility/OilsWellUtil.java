package de.soco.software.simuspace.suscore.object.oilswell.utility;

public class OilsWellUtil {
    /*
    
    public static OilsWellInstantResponseDTO readOilsWellInstantCsv( File file ) {
    
    OilsWellInstantResponseDTO oilsWellInstantResponseDTO = null;
    byte[] byteArray;
    try {
    byteArray = IOUtils.toByteArray( FileUtils.openInputStream( file ) );
    } catch ( Exception e1 ) {
    throw new SusException( "CSV file is not available" );
    }
    
    try ( InputStream inputStream = new ByteArrayInputStream( byteArray ); Reader reader = new InputStreamReader( inputStream ) ) {
    oilsWellInstantResponseDTO = new OilsWellInstantResponseDTO();
    Iterator< CSVRecord > csvRecordIterator = new CSVParser( reader, CSVFormat.DEFAULT ).getRecords().iterator();
    List< String > dates = new ArrayList<>();
    List< String > values = new ArrayList<>();
    CSVRecord csvRecordHeader = ( CSVRecord ) csvRecordIterator.next();
    String name = csvRecordHeader.get( 1 );
    while ( csvRecordIterator.hasNext() ) {
    CSVRecord csvRecord = ( CSVRecord ) csvRecordIterator.next();
    dates.add( csvRecord.get( 2 ) );
    values.add( csvRecord.get( 1 ) );
    }
    oilsWellInstantResponseDTO.setName( name );
    oilsWellInstantResponseDTO.setX( dates );
    oilsWellInstantResponseDTO.setY( values );
    return oilsWellInstantResponseDTO;
    } catch ( Exception e ) {
    throw new SusException( "Unable to read CSV file" );
    }
    
    }
    
    public static List< CycleLogsWellEntity > readCycleLogsWellCsv( File file ) {
    List< CycleLogsWellEntity > cycleLogsWellEntities = null;
    byte[] byteArray;
    try {
    byteArray = IOUtils.toByteArray( FileUtils.openInputStream( file ) );
    } catch ( Exception e1 ) {
    throw new SusException( "CSV file is not available" );
    }
    
    try ( InputStream inputStream = new ByteArrayInputStream( byteArray ); Reader reader = new InputStreamReader( inputStream ) ) {
    cycleLogsWellEntities = new ArrayList<>();
    Iterator< CSVRecord > csvRecordIterator = new CSVParser( reader, CSVFormat.DEFAULT ).getRecords().iterator();
    csvRecordIterator.next(); // header skiped
    while ( csvRecordIterator.hasNext() ) {
    CSVRecord csvRecord = ( CSVRecord ) csvRecordIterator.next();
    CycleLogsWellEntity cycleLogsWellEntity = prepareCycleLogsWellEntity( csvRecord );
    cycleLogsWellEntities.add( cycleLogsWellEntity );
    }
    } catch ( Exception e ) {
    throw new SusException( "Unable to read CSV file" );
    }
    return cycleLogsWellEntities;
    }
    
    private static CycleLogsWellEntity prepareCycleLogsWellEntity( CSVRecord csvRecord ) throws ParseException {
    CycleLogsWellEntity cycleLogsWellEntity = new CycleLogsWellEntity();
    cycleLogsWellEntity.setId( UUID.randomUUID() );
    cycleLogsWellEntity
    .setDateTimeAtOpen( new SimpleDateFormat( OilsWellManagerImpl.CYCLE_LOGS_WELL_PATTERN ).parse( csvRecord.get( 1 ) ) );
    cycleLogsWellEntity.setCsgAtOpen( csvRecord.get( 2 ) );
    cycleLogsWellEntity.setTbgAtOpen( csvRecord.get( 3 ) );
    cycleLogsWellEntity.setLineAtOpen( csvRecord.get( 4 ) );
    cycleLogsWellEntity.setTimeOffTotal( csvRecord.get( 5 ) );
    cycleLogsWellEntity.setArrivalType( csvRecord.get( 6 ) );
    cycleLogsWellEntity.setArrivalMinute( csvRecord.get( 7 ) );
    cycleLogsWellEntity.setArrivalVelocity( csvRecord.get( 8 ) );
    cycleLogsWellEntity.setVentMin( csvRecord.get( 9 ) );
    if ( StringUtils.isEmpty( csvRecord.get( 10 ) ) ) {
    cycleLogsWellEntity.setDateTimeAtClose( null );
    } else {
    cycleLogsWellEntity.setDateTimeAtClose(
    new SimpleDateFormat( OilsWellManagerImpl.CYCLE_LOGS_WELL_CLOSED_PATTERN ).parse( csvRecord.get( 10 ) ) );
    }
    
    cycleLogsWellEntity.setCsgAtClose( csvRecord.get( 11 ) );
    cycleLogsWellEntity.setLineAtClose( csvRecord.get( 12 ) );
    cycleLogsWellEntity.setCsgCycleMin( csvRecord.get( 13 ) );
    cycleLogsWellEntity.setFlowRateAtClose( csvRecord.get( 14 ) );
    cycleLogsWellEntity.setTimeAfterFlow( csvRecord.get( 15 ) );
    cycleLogsWellEntity.setVolCycleTotal( csvRecord.get( 16 ) );
    cycleLogsWellEntity.setTbgAtClose( csvRecord.get( 17 ) );
    cycleLogsWellEntity.setMeterDPAtClose( csvRecord.get( 18 ) );
    cycleLogsWellEntity.setCloseTrig( csvRecord.get( 19 ) );
    cycleLogsWellEntity.setCloseTrigRef( csvRecord.get( 20 ) );
    cycleLogsWellEntity.setOpenTrig( csvRecord.get( 21 ) );
    cycleLogsWellEntity.setOpenTrigRef( csvRecord.get( 22 ) );
    return cycleLogsWellEntity;
    }
    
    public static List< OilsWellInstantEntity > readOilsWellInstantCsv( File file, String instant ) {
    List< OilsWellInstantEntity > oilsWellInstantEntities = null;
    byte[] byteArray;
    try {
    byteArray = IOUtils.toByteArray( FileUtils.openInputStream( file ) );
    } catch ( Exception e1 ) {
    throw new SusException( "CSV file is not available" );
    }
    
    try ( InputStream inputStream = new ByteArrayInputStream( byteArray ); Reader reader = new InputStreamReader( inputStream ) ) {
    oilsWellInstantEntities = new ArrayList<>();
    Iterator< CSVRecord > csvRecordIterator = new CSVParser( reader, CSVFormat.DEFAULT ).getRecords().iterator();
    csvRecordIterator.next(); // header skiped
    while ( csvRecordIterator.hasNext() ) {
    CSVRecord csvRecord = ( CSVRecord ) csvRecordIterator.next();
    
    OilsWellInstantEntity oilsWellInstantEntity = null;
    switch ( instant ) {
    case "tbgPres":
    oilsWellInstantEntity = prepareOilsWellTubing( csvRecord.get( 2 ), csvRecord.get( 1 ) );
    break;
    case "gasRate":
    oilsWellInstantEntity = prepareOilsWellGasRate( csvRecord.get( 2 ), csvRecord.get( 1 ) );
    break;
    case "csgPres":
    oilsWellInstantEntity = prepareOilsWellCasing( csvRecord.get( 2 ), csvRecord.get( 1 ) );
    break;
    default:
    }
    oilsWellInstantEntities.add( oilsWellInstantEntity );
    }
    return oilsWellInstantEntities;
    } catch ( Exception e ) {
    throw new SusException( "Unable to read CSV file" );
    }
    
    }
    
    private static OilsWellInstantEntity prepareOilsWellTubing( String tubingDate, String tbgPres ) throws ParseException {
    OilsWellInstantEntity oilsWellInstantEntity = new OilsWellInstantEntity();
    oilsWellInstantEntity.setId( UUID.randomUUID() );
    Date date = new SimpleDateFormat( OilsWellManagerImpl.OILS_WELL_DATE_PATTERN ).parse( tubingDate );
    oilsWellInstantEntity.setBigIntAsDateTime( date );
    oilsWellInstantEntity.setTbgPres( tbgPres );
    return oilsWellInstantEntity;
    }
    
    private static OilsWellInstantEntity prepareOilsWellGasRate( String gasRateDate, String gasRate ) throws ParseException {
    OilsWellInstantEntity oilsWellInstantEntity = new OilsWellInstantEntity();
    oilsWellInstantEntity.setId( UUID.randomUUID() );
    Date date = new SimpleDateFormat( OilsWellManagerImpl.OILS_WELL_DATE_PATTERN ).parse( gasRateDate );
    oilsWellInstantEntity.setBigIntAsDateTime( date );
    oilsWellInstantEntity.setGasRate( gasRate );
    return oilsWellInstantEntity;
    }
    
    private static OilsWellInstantEntity prepareOilsWellCasing( String casingDate, String csgPres ) throws ParseException {
    OilsWellInstantEntity oilsWellInstantEntity = new OilsWellInstantEntity();
    oilsWellInstantEntity.setId( UUID.randomUUID() );
    Date date = new SimpleDateFormat( OilsWellManagerImpl.OILS_WELL_DATE_PATTERN ).parse( casingDate );
    oilsWellInstantEntity.setBigIntAsDateTime( date );
    oilsWellInstantEntity.setCsgPres( csgPres );
    return oilsWellInstantEntity;
    }
    
    */
}

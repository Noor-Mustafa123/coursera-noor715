package de.soco.software.simuspace.suscore.object.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;

/**
 * The type Dashboard excel util.
 */
@Log4j2
public class DashboardExcelUtil {

    /**
     * Instantiates a new Dashboard excel util.
     */
    private DashboardExcelUtil() {

    }

    /**
     * Validate user excel.
     *
     * @param excelPath
     *         the Excel path
     */
    public static void validateUserExcel( Path excelPath ) {
        if ( Files.notExists( excelPath ) ) {
            throw new SusException( "File not found or is in a directory SIMuSPACE can not access" );
        }
        if ( !Files.isReadable( excelPath ) ) {
            throw new SusException( "No permission to read file" );
        }
    }

    /**
     * Gets preview.
     *
     * @param excelPath
     *         the Excel path
     *
     * @return the preview
     */
    public static Object getPreview( Path excelPath ) {
        String excelFilePath = excelPath.toAbsolutePath().toString();
        Map< String, List< List< String > > > preview = new HashMap<>();
        try ( FileInputStream fileInputStream = new FileInputStream( excelFilePath );
                Workbook workbook = new XSSFWorkbook( fileInputStream ) ) {

            // Iterate over all sheets and add their names to the list
            for ( int i = 0; i < workbook.getNumberOfSheets(); i++ ) {
                Sheet sheet = workbook.getSheetAt( i );
                String sheetName = sheet.getSheetName();
                log.info( "preparing data for sheet {} at index {}", sheetName, i );
                preview.put( sheetName, getSheetData( sheet ) );
                log.info( "data prepared for sheet {} at index {}", sheetName, i );
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        }
        return preview;
    }

    /**
     * Gets sheet data.
     *
     * @param sheet
     *         the sheet
     *
     * @return the sheet data
     *
     * @throws IOException
     *         the io exception
     */
    public static List< List< String > > getSheetData( Sheet sheet ) throws IOException {
        List< List< String > > sheetData = new ArrayList<>();

        // Create a 2D list of strings to store the sheet data
        for ( int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++ ) {
            Row row = sheet.getRow( rowIndex );
            List< String > rowData = new ArrayList<>();

            if ( row != null ) {
                for ( int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++ ) {
                    Cell cell = row.getCell( colIndex );
                    String cellValue = getCellValue( cell );

                    // Check if the cell is part of a merged region
                    if ( isCellPartOfMergedRegion( sheet, rowIndex, colIndex ) ) {
                        // Get the value from the top-left merged cell
                        CellRangeAddress mergedRegion = getMergedRegion( sheet, rowIndex, colIndex );
                        cellValue = getCellValue( sheet.getRow( mergedRegion.getFirstRow() ).getCell( mergedRegion.getFirstColumn() ) );
                    }

                    rowData.add( cellValue );
                }
            }

            sheetData.add( rowData );
        }

        return sheetData;
    }

    /**
     * Gets cell value.
     *
     * @param cell
     *         the cell
     *
     * @return the cell value
     */
// Get cell value as String (handles different cell types)
    private static String getCellValue( Cell cell ) {
        if ( cell == null ) {
            return "";
        }
        return switch ( cell.getCellType() ) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf( cell.getNumericCellValue() );
            case BOOLEAN -> String.valueOf( cell.getBooleanCellValue() );
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    /**
     * Is cell part of merged region boolean.
     *
     * @param sheet
     *         the sheet
     * @param rowIndex
     *         the row index
     * @param colIndex
     *         the col index
     *
     * @return the boolean
     */
// Check if a cell is part of a merged region
    private static boolean isCellPartOfMergedRegion( Sheet sheet, int rowIndex, int colIndex ) {
        for ( int i = 0; i < sheet.getNumMergedRegions(); i++ ) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion( i );
            if ( mergedRegion.isInRange( rowIndex, colIndex ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets merged region.
     *
     * @param sheet
     *         the sheet
     * @param rowIndex
     *         the row index
     * @param colIndex
     *         the col index
     *
     * @return the merged region
     */
// Get the merged region for a given cell
    private static CellRangeAddress getMergedRegion( Sheet sheet, int rowIndex, int colIndex ) {
        for ( int i = 0; i < sheet.getNumMergedRegions(); i++ ) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion( i );
            if ( mergedRegion.isInRange( rowIndex, colIndex ) ) {
                return mergedRegion;
            }
        }
        return null;
    }

}

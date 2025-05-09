package de.soco.software.simuspace.suscore.data.utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;

/**
 * This util keep context in particular order and add divider on certain place.
 *
 * @author Noman Arshad
 * @since 2.0
 */
public class ContextUtil {

    /*   >>>>> create context    */

    /**
     * The Constant CREATE.
     */
    private static final String CREATE = "4100018x4";

    /**
     * The Constant CREATE_PROJECT.
     */
    private static final String CREATE_PROJECT = "4100001x4";

    /**
     * The Constant CREATE_WORKFLOW_PROJECT.
     */
    private static final String CREATE_WORKFLOW_PROJECT = "4100015x4";

    /**
     * The Constant CREATE_WORKFLOW.
     */
    private static final String CREATE_WORKFLOW = "4100036x4";

    /**
     * The Constant CREATE_LOADCASE.
     */
    private static final String CREATE_LOADCASE = "4100060x4";

    /**
     * The Constant CREATE_DUMMY_VARIANT.
     */
    private static final String CREATE_DUMMY_VARIANT = "4100031x4";

    /**
     * The Constant CREATE_COPY.
     */
    private static final String CREATE_COPY = "4100059x4";

    /**
     * The Constant CREATE_SCHEME.
     */
    private static final String CREATE_SCHEME = "4100063x4";

    /**
     * The Constant CREATE_CATEGORY.
     */
    private static final String CREATE_CATEGORY = "4100069x4";

    /**
     * The Constant CREATE_LOCAL_DIRECTORY.
     */
    private static final String CREATE_LOCAL_DIRECTORY = "4100077x4";

    /*     Edit Context       */

    /**
     * The Constant EDIT.
     */
    private static final String EDIT = "4100014x4";

    /**
     * The Constant EDIT_CATAGORIES.
     */
    private static final String EDIT_CATAGORIES = "4100068x4";

    /**
     * The Constant EDIT_SCHEME.
     */
    private static final String EDIT_SCHEME = "4100062x4";

    /*      Delete  context      */

    /**
     * The Constant DELETE.
     */
    private static final String DELETE = "4100007x4";

    /**
     * The Constant DELETE_BULK.
     */
    private static final String DELETE_BULK = "4100076x4";

    /*     Manage context       */

    /**
     * The Constant MANAGE.
     */
    private static final String MANAGE = "4100006x4";

    /**
     * The Constant OPEN_WITH.
     */
    private static final String OPEN_WITH = "4100078x4";

    /**
     * The Constant OPEN_FILE.
     */
    private static final String OPEN_FILE = "4100013x4";

    /*        Run context    */

    /**
     * The Constant RUN_WORKFLOW.
     */
    private static final String RUN = "4100058x4";

    /**
     * The Constant RUN_WORKFLOW.
     */
    private static final String RUN_WORKFLOW = "4100045x4";

    /**
     * The Constant RUN_SCHEME.
     */
    private static final String RUN_SCHEME = "4100081x4";

    /*     Synch context   */

    /**
     * The Constant DOWNLOAD.
     */
    private static final String DOWNLOAD = "4100030x4";

    /**
     * The Constant UPLOAD_SYNCH.
     */
    private static final String UPLOAD_SYNCH = "4100022x4";

    /**
     * The Constant DOWNLOAD_SYNCH.
     */
    private static final String DOWNLOAD_SYNCH = "4100020x4";

    /**
     * The Constant ABORT_SYNCH.
     */
    private static final String ABORT_SYNCH = "4100026x4";

    /**
     * The Constant DISCARD_SYNCH.
     */
    private static final String DISCARD_SYNCH = "4100025x4";

    /**
     * The Constant EXPORT_DOWNLOAD_LOCAL_SYNCH.
     */
    private static final String EXPORT_DOWNLOAD_LOCAL_SYNCH = "4100028x4";

    /**
     * The Constant CHECK_IN_SYNCH.
     */
    private static final String CHECK_IN_SYNCH = "4100024x4";

    /**
     * The Constant CHECK_OUT_SYNCH.
     */
    private static final String CHECK_OUT_SYNCH = "4100023x4";

    /**
     * The Constant CHANGE_TYPE_SYNCH.
     */
    private static final String CHANGE_TYPE_SYNCH = "4100021x4";

    /**
     * The Constant CURVE_COMPARE.
     */
    private static final String CURVE_COMPARE = "4100079x4";

    /*     permission and metadata context  */

    /**
     * The Constant ADD_META_DATA.
     */
    private static final String ADD_META_DATA = "4100008x4";

    /**
     * The Constant CHANGE_STATUS.
     */
    private static final String CHANGE_STATUS = "4100005x4";

    /**
     * The Constant PERMISION.
     */
    private static final String PERMISION = "4100012x4";

    /*        Link and export data context    */

    /**
     * The Constant IMPORT_WORKFLOW.
     */
    private static final String IMPORT_WORKFLOW = "4100017x4";

    /**
     * The Constant EXPORT_DATA.
     */
    private static final String EXPORT_DATA = "4100075x4";

    /**
     * The Constant LINK.
     */
    private static final String LINK = "4100009x4";

    /**
     * The Constant LINKS.
     */
    private static final String LINK_ITEMS = "4100073x4";

    /**
     * The Constant REMOVE_LINK_TITLE.
     */
    private static final String REMOVE_LINK_TITLE = "4100010x4";

    /**
     * The Constant MOVE_TO.
     */
    private static final String MOVE_TO = "4100016x4";

    /**
     * The Constant DOWNLOAD_CSV_FILE.
     */
    private static final String DOWNLOAD_CSV_FILE = "4100053x4";

    /**
     * The Constant DOWNLOAD_JOB_LOGS.
     */
    private static final String DOWNLOAD_JOB_LOGS = "4100048x4";

    /**
     * The Constant RESTORE_BULK.
     */
    private static final String RESTORE = "4100027x4";

    /**
     * All ordered context.
     *
     * @param unOrderedContext
     *         the un ordered context
     *
     * @return the list
     */
    public static List< ContextMenuItem > allOrderedContext( List< ContextMenuItem > unOrderedContext ) {
        ContextMenuItem[] orderedContext = new ContextMenuItem[ 150 ];
        Iterator< ContextMenuItem > iter = unOrderedContext.iterator();
        while ( iter.hasNext() ) {
            ContextMenuItem contextMenuItem = iter.next();
            if ( createOptionOrderedContext( contextMenuItem, orderedContext ) ) {
                iter.remove();
                continue;
            }
            if ( editOptionOrderedContext( contextMenuItem, orderedContext ) ) {
                iter.remove();
                continue;
            }
            if ( deleteOptionOrderedContext( contextMenuItem, orderedContext ) ) {
                iter.remove();
                continue;
            }
            if ( manageOptionOrderedContext( contextMenuItem, orderedContext ) ) {
                iter.remove();
                continue;
            }
            if ( openOptionOrderedContext( contextMenuItem, orderedContext ) ) {
                iter.remove();
                continue;
            }
            if ( runOptionOrderedContext( contextMenuItem, orderedContext ) ) {
                iter.remove();
                continue;
            }
            if ( synchOptionOrderedContext( contextMenuItem, orderedContext ) ) {
                iter.remove();
                continue;
            }
            if ( permissionAndTypeAndMetaDataOptionOrderedContext( contextMenuItem, orderedContext ) ) {
                iter.remove();
                continue;
            }
            if ( exportAndImportAndLinkiOptionOrderedContext( contextMenuItem, orderedContext ) ) {
                iter.remove();
                continue;
            }
            if ( restoreOptionOrderedContext( contextMenuItem, orderedContext ) ) {
                iter.remove();
                continue;
            }
            if ( downloadCSVOptionOrderedContext( contextMenuItem, orderedContext ) ) {
                iter.remove();
                continue;
            }
            if ( generateImageOptionOrderedContext( contextMenuItem, orderedContext ) ) {
                iter.remove();
            }
        }
        return prepareContextList( orderedContext, unOrderedContext );
    }

    /**
     * Creates the option ordered context.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param orderedContext
     *         the ordered context
     *
     * @return true, if successful
     */
    private static boolean createOptionOrderedContext( ContextMenuItem contextMenuItem, ContextMenuItem[] orderedContext ) {
        boolean found = false;
        if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CREATE ) ) ) {
            orderedContext[ 1 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CREATE_PROJECT ) ) ) {
            orderedContext[ 2 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CREATE_WORKFLOW_PROJECT ) ) ) {
            orderedContext[ 3 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CREATE_WORKFLOW ) ) ) {
            orderedContext[ 4 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CREATE_LOADCASE ) ) ) {
            orderedContext[ 5 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CREATE_DUMMY_VARIANT ) ) ) {
            orderedContext[ 6 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CREATE_COPY ) ) ) {
            orderedContext[ 7 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CREATE_SCHEME ) ) ) {
            orderedContext[ 8 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CREATE_CATEGORY ) ) ) {
            orderedContext[ 9 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CREATE_LOCAL_DIRECTORY ) ) ) {
            orderedContext[ 10 ] = contextMenuItem;
            found = true;
        }
        return found;
    }

    /**
     * Edits the option ordered context.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param orderedContext
     *         the ordered context
     *
     * @return true, if successful
     */
    private static boolean editOptionOrderedContext( ContextMenuItem contextMenuItem, ContextMenuItem[] orderedContext ) {
        boolean found = false;
        if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( EDIT ) ) ) {
            orderedContext[ 31 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( EDIT_CATAGORIES ) ) ) {
            orderedContext[ 32 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( EDIT_SCHEME ) ) ) {
            orderedContext[ 33 ] = contextMenuItem;
            found = true;
        }

        if ( found && isNotEmpty( orderedContext, 30 ) ) {
            orderedContext[ 30 ] = addDivider();
        }
        return found;
    }

    /**
     * Edits the option ordered context.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param orderedContext
     *         the ordered context
     *
     * @return true, if successful
     */
    private static boolean openOptionOrderedContext( ContextMenuItem contextMenuItem, ContextMenuItem[] orderedContext ) {
        boolean found = false;

        String openWithTitle = MessageBundleFactory.getMessage( OPEN_WITH );
        if ( contextMenuItem.getTitle().equals( MessageBundleFactory.getMessage( OPEN_FILE ) ) ) {
            orderedContext[ 54 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().contains( openWithTitle ) ) {
            ContextMenuItem openWithTitleItem = new ContextMenuItem();
            openWithTitleItem.setTitle( openWithTitle );
            openWithTitleItem.setLinkClass( "disabled" );
            orderedContext[ 54 ] = openWithTitleItem;
            contextMenuItem.setTitle( contextMenuItem.getTitle().replace( openWithTitle, ConstantsString.EMPTY_STRING ) );
            contextMenuItem.setLinkClass( "margin-left-20" );

            // 5 slots for open with options
            for ( int i = 55; i <= 59; i++ ) {
                if ( orderedContext[ i ] == null ) {
                    orderedContext[ i ] = contextMenuItem;
                    found = true;
                    break;
                }
            }
        }

        if ( found && isNotEmpty( orderedContext, 53 ) ) {
            orderedContext[ 53 ] = addDivider();
        }
        return found;
    }

    /**
     * Delete option ordered context.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param orderedContext
     *         the ordered context
     *
     * @return true, if successful
     */
    private static boolean deleteOptionOrderedContext( ContextMenuItem contextMenuItem, ContextMenuItem[] orderedContext ) {
        boolean found = false;
        if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( DELETE ) ) ) {
            orderedContext[ 41 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( DELETE_BULK ) )
                || contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( Messages.DELETE_BULK.getKey() ) ) ) {
            orderedContext[ 42 ] = contextMenuItem;
            found = true;
        }
        if ( found && isNotEmpty( orderedContext, 40 ) ) {
            orderedContext[ 40 ] = addDivider();
        }
        return found;
    }

    /**
     * Manage option ordered context.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param orderedContext
     *         the ordered context
     *
     * @return true, if successful
     */
    private static boolean manageOptionOrderedContext( ContextMenuItem contextMenuItem, ContextMenuItem[] orderedContext ) {
        boolean found = false;
        if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( MANAGE ) ) ) {
            orderedContext[ 51 ] = contextMenuItem;
            found = true;
        }
        if ( found && isNotEmpty( orderedContext, 50 ) ) {
            orderedContext[ 50 ] = addDivider();
        }
        return found;
    }

    /**
     * Run option ordered context.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param orderedContext
     *         the ordered context
     *
     * @return true, if successful
     */
    private static boolean runOptionOrderedContext( ContextMenuItem contextMenuItem, ContextMenuItem[] orderedContext ) {
        boolean found = false;
        if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( RUN ) ) ) {
            orderedContext[ 61 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( RUN_WORKFLOW ) ) ) {
            orderedContext[ 62 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( RUN_SCHEME ) ) ) {
            orderedContext[ 63 ] = contextMenuItem;
            found = true;
        }
        if ( found && isNotEmpty( orderedContext, 60 ) ) {
            orderedContext[ 60 ] = addDivider();
        }
        return found;
    }

    /**
     * Synch option ordered context.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param orderedContext
     *         the ordered context
     *
     * @return true, if successful
     */
    private static boolean synchOptionOrderedContext( ContextMenuItem contextMenuItem, ContextMenuItem[] orderedContext ) {
        boolean found = false;
        if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( DOWNLOAD ) ) ) {
            orderedContext[ 71 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( UPLOAD_SYNCH ) ) ) {
            orderedContext[ 72 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( DOWNLOAD_SYNCH ) ) ) {
            orderedContext[ 73 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( ABORT_SYNCH ) ) ) {
            orderedContext[ 74 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( DISCARD_SYNCH ) ) ) {
            orderedContext[ 75 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( EXPORT_DOWNLOAD_LOCAL_SYNCH ) ) ) {
            orderedContext[ 76 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CHECK_IN_SYNCH ) ) ) {
            orderedContext[ 78 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CHECK_OUT_SYNCH ) ) ) {
            orderedContext[ 79 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CHANGE_TYPE_SYNCH ) ) ) {
            orderedContext[ 80 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CURVE_COMPARE ) ) ) {
            orderedContext[ 81 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( DOWNLOAD_JOB_LOGS ) ) ) {
            orderedContext[ 82 ] = contextMenuItem;
            found = true;
        }

        if ( found && isNotEmpty( orderedContext, 70 ) ) {
            orderedContext[ 70 ] = addDivider();
        }
        return found;
    }

    /**
     * Permission and type and meta data option ordered context.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param orderedContext
     *         the ordered context
     *
     * @return true, if successful
     */
    private static boolean permissionAndTypeAndMetaDataOptionOrderedContext( ContextMenuItem contextMenuItem,
            ContextMenuItem[] orderedContext ) {
        boolean found = false;
        if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( ADD_META_DATA ) ) ) {
            orderedContext[ 91 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( CHANGE_STATUS ) ) ) {
            orderedContext[ 92 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( PERMISION ) ) ) {
            orderedContext[ 93 ] = contextMenuItem;
            found = true;
        }
        if ( found && isNotEmpty( orderedContext, 90 ) ) {
            orderedContext[ 90 ] = addDivider();
        }
        return found;
    }

    /**
     * Export and import and linki option ordered context.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param orderedContext
     *         the ordered context
     *
     * @return true, if successful
     */
    private static boolean exportAndImportAndLinkiOptionOrderedContext( ContextMenuItem contextMenuItem,
            ContextMenuItem[] orderedContext ) {
        boolean found = false;
        if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( EXPORT_DATA ) ) ) {
            orderedContext[ 101 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( IMPORT_WORKFLOW ) ) ) {
            orderedContext[ 102 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( MOVE_TO ) ) ) {
            orderedContext[ 103 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( LINK ) ) ) {
            orderedContext[ 104 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( LINK_ITEMS ) ) ) {
            orderedContext[ 105 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( REMOVE_LINK_TITLE ) ) ) {
            orderedContext[ 106 ] = contextMenuItem;
            found = true;
        }
        if ( found && isNotEmpty( orderedContext, 100 ) ) {
            orderedContext[ 100 ] = addDivider();
        }
        return found;
    }

    /**
     * Restore option ordered context.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param orderedContext
     *         the ordered context
     *
     * @return true, if successful
     */
    private static boolean restoreOptionOrderedContext( ContextMenuItem contextMenuItem, ContextMenuItem[] orderedContext ) {
        boolean found = false;
        if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( Messages.RESTORE_BULK.getKey() ) ) ) {
            orderedContext[ 121 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( RESTORE ) ) ) {
            orderedContext[ 122 ] = contextMenuItem;
            found = true;
        }

        if ( found && isNotEmpty( orderedContext, 120 ) ) {
            orderedContext[ 120 ] = addDivider();
        }
        return found;
    }

    /**
     * Download CSV option ordered context.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param orderedContext
     *         the ordered context
     *
     * @return true, if successful
     */
    private static boolean downloadCSVOptionOrderedContext( ContextMenuItem contextMenuItem, ContextMenuItem[] orderedContext ) {
        boolean found = false;
        if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( Messages.DOWNLOAD_CSV_FILE.getKey() ) ) ) {
            orderedContext[ 131 ] = contextMenuItem;
            found = true;
            orderedContext[ 132 ] = addDivider();
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( Messages.HEATMAP.getKey() ) ) ) {
            orderedContext[ 133 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle()
                .equalsIgnoreCase( MessageBundleFactory.getMessage( Messages.PLOT_BUBBLE_CHART.getKey() ) ) ) {
            orderedContext[ 134 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( Messages.PLOT_CORRELATION.getKey() ) ) ) {
            orderedContext[ 135 ] = contextMenuItem;
            found = true;
        } else if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( Messages.GENERATE_IMAGE.getKey() ) ) ) {
            orderedContext[ 136 ] = contextMenuItem;
            found = true;
        }
        if ( found && isNotEmpty( orderedContext, 130 ) ) {
            orderedContext[ 130 ] = addDivider();
        }
        return found;
    }

    /**
     * Generate image option ordered context.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param orderedContext
     *         the ordered context
     *
     * @return true, if successful
     */
    private static boolean generateImageOptionOrderedContext( ContextMenuItem contextMenuItem, ContextMenuItem[] orderedContext ) {
        boolean found = false;
        if ( contextMenuItem.getTitle().contains( MessageBundleFactory.getMessage( ConstantsString.GENERATE_IMAGE_KEY ) ) ) {
            for ( int i = 141; i <= 149; i++ ) {
                if ( orderedContext[ i ] == null ) {
                    orderedContext[ i ] = contextMenuItem;
                    found = true;
                    break;
                }
            }
        }
        if ( found && isNotEmpty( orderedContext, 140 ) ) {
            orderedContext[ 140 ] = addDivider();
        }
        return found;
    }

    /**
     * Adds the divider.
     *
     * @return the context menu item
     */
    private static ContextMenuItem addDivider() {
        ContextMenuItem dividerContext = new ContextMenuItem();
        dividerContext.setTitle( ConstantsString.EMPTY_STRING );
        dividerContext.setDivider( true );
        return dividerContext;
    }

    /**
     * Is not empty boolean.
     *
     * @param context
     *         the context
     * @param length
     *         the length
     *
     * @return the boolean
     */
    private static boolean isNotEmpty( ContextMenuItem[] context, int length ) {
        return IntStream.range( ConstantsInteger.INTEGER_VALUE_ZERO, length ).anyMatch( i -> context[ i ] != null );
    }

    /**
     * Prepare context list.
     *
     * @param orderedContext
     *         the ordered context
     * @param optionalContext
     *         the context
     *
     * @return the list
     */
    private static List< ContextMenuItem > prepareContextList( ContextMenuItem[] orderedContext, List< ContextMenuItem > optionalContext ) {
        List< ContextMenuItem > result = new ArrayList<>();
        for ( ContextMenuItem contextMenuItem : orderedContext ) {
            if ( contextMenuItem != null ) {
                result.add( contextMenuItem );
            }
        }
        for ( ContextMenuItem contextMenuItem : optionalContext ) {
            if ( contextMenuItem != null ) {
                result.add( contextMenuItem );
            }
        }
        return result;
    }

}

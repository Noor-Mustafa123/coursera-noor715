package de.soco.software.simuspace.suscore.common.records;

import java.util.List;

public record GroupRecord( List< UserEntityRecord > users, String name, String description, Boolean status, String selectionId,
                           long totalUsers ) {

}

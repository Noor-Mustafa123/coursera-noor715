package de.soco.software.simuspace.suscore.common.records;

import de.soco.software.simuspace.suscore.common.model.DocumentDTO;

public record UserRecord( String id, DocumentDTO profilePhoto, String userUid, String description, String status, String restricted,
                          String firstName, String surName, String locationPreferenceSelectionId ) {

}

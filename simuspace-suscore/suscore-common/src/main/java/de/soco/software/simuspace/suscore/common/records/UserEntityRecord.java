package de.soco.software.simuspace.suscore.common.records;

import java.util.UUID;

public record UserEntityRecord( UUID id, boolean isDelete, String userUid, String firstName, String surName, Boolean changeable,
                                Boolean status, String description, DocumentRecord profilePhoto, Boolean restricted, String theme,
                                String locationPreferenceSelectionId, String ldapFirstName, String ldapSurName ) {

}

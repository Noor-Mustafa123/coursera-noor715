package de.soco.software.simuspace.suscore.common.records;

import java.util.UUID;

public record DocumentRecord( String id, UUID userId, String name, String type, Boolean isTemp, String properties, long size, int expiry,
                              String agent, String path, String encoding, String hash, String url, boolean isEncrypted ) {

}
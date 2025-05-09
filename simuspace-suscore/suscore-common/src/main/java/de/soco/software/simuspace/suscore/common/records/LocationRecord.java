package de.soco.software.simuspace.suscore.common.records;

import java.util.UUID;

public record LocationRecord( UUID id, String name, String description, String status, String type, int priority, String vault,
                              String staging, String url, String authToken, boolean isInternal ) {

}
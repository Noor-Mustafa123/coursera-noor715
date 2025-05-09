package de.soco.software.simuspace.suscore.data.entity;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * @author fahad
 */
@Getter
@Setter
public class CustomAttributes {

    private UUID id;

    private String customAttributeName;

    private String customAttributeValue;

}

package de.soco.software.simuspace.server.manager;

import javax.ws.rs.core.UriInfo;

import java.util.List;

import de.soco.software.simuspace.server.model.jsonschema.SMJSONSchema;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;

public interface ShapeModuleManager {

    SMJSONSchema getSMJsonSchema();

    List< UIFormItem > getSMInCustomFlag( UriInfo uriInfo );

    List< UIFormItem > getCustomFlagPluginUI( String plugins, String value );

}
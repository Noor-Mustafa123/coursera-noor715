package de.soco.software.simuspace.suscore.object.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.data.model.TreeNodeDTO;
import de.soco.software.simuspace.suscore.object.dao.BmwDAO;

@Log4j2
public class BmwDAOImpl implements BmwDAO {

    @Override
    public List< TreeNodeDTO > getDummyTree( Map< String, String > optional ) {
        List< TreeNodeDTO > treeNodeDTOS = new ArrayList<>();
        List< JsonNode > dumyTypesList = getDummyNodes( "dummyTypes", optional.get( "solver_type" ) );
        if ( CollectionUtil.isNotEmpty( dumyTypesList ) ) {
            for ( JsonNode dumyType : dumyTypesList ) {
                if ( dumyType.get( "name" ).asText().equals( optional.get( "dummy_type" ) ) ) {
                    TreeNodeDTO treeNodeDTO = new TreeNodeDTO();
                    treeNodeDTO.setId( dumyType.get( "name" ).asText() );
                    treeNodeDTO.setTitle( dumyType.get( "name" ).asText() );
                    treeNodeDTO.setFolder( true );
                    treeNodeDTO.setLazy( false );
                    treeNodeDTO.setIcon( "fa fa-briefcase font-blue" );
                    treeNodeDTO.setState( 0 );
                    treeNodeDTO.setExpanded( false );
                    treeNodeDTO.setUrl(
                            "external/bmw-dummyfiles/" + dumyType.get( "name" ).asText() + "/type/" + optional.get( "solver_type" ) );
                    treeNodeDTO.setElement( null );
                    treeNodeDTOS.add( treeNodeDTO );
                }
            }
        }
        return treeNodeDTOS;
    }

    private List< JsonNode > getDummyNodes( String node, String solverType ) {
        final String propFileName = ConstantsKaraf.KARAF_HOME + File.separator + ConstantsKaraf.KARAF_CONF + File.separator
                + "dummy_config.json";
        JsonNode jsonJobSystemObject = null;
        try ( InputStream propFileNameStream = new FileInputStream( propFileName ) ) {
            jsonJobSystemObject = JsonUtils.convertInputStreamToJsonNode( propFileNameStream );
        } catch ( final IOException e ) {
            log.error( e.getLocalizedMessage() );
        }
        if ( Integer.valueOf( solverType ) == ConstantsInteger.INTEGER_VALUE_ONE ) {
            return JsonUtils.jsonToList( JsonUtils.toJsonString( jsonJobSystemObject.get( "Abaqus" ).get( node ) ), JsonNode.class );
        } else {
            return JsonUtils.jsonToList( JsonUtils.toJsonString( jsonJobSystemObject.get( "LsDyna" ).get( node ) ), JsonNode.class );
        }

    }

}

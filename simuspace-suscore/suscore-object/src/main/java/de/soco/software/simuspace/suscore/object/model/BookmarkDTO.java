package de.soco.software.simuspace.suscore.object.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

@Getter
@Setter
@NoArgsConstructor
public class BookmarkDTO {

    /**
     * the user uuid
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, type = "hidden" )
    @UIColumn( data = "id", filter = "uuid", renderer = "hidden", title = "3000021x4", name = "id", type = "link", isShow = false )
    private UUID id;

    /**
     * The name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000032x4", orderNum = 1 )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * The url.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "url", title = "3000187x4", orderNum = 2 )
    @UIColumn( data = "url", name = "url", filter = "text", renderer = "text", title = "3000187x4", orderNum = 3 )
    private String url;

    private UUID rootParentId;

    private UUID objectId;

    @UIColumn( data = "type", name = "type", filter = "text", renderer = "text", title = "3000173x4", orderNum = 2 )
    private String type;

    /**
     * Instantiates a new Bookmark dto.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param url
     *         the url
     */
    public BookmarkDTO( UUID id, String name, String url ) {
        super();
        this.id = id;
        this.name = name;
        this.url = url;
    }

}

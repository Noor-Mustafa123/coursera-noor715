package de.soco.software.simuspace.wizards.model;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.UIColumn;

public class VariantSubmitDTO {

    @UIColumn( data = "id", name = "id", filter = "uuid", renderer = "hidden", title = "3000021x4", isSortable = false, isShow = false )
    private UUID id;

    @UIColumn( data = "loadcase", name = "loadcase", filter = "", renderer = "text", title = "9800012x4", isSortable = false )
    private String loadcase;

    @UIColumn( data = "assemble", name = "assemble", filter = "", renderer = "checkbox", title = "9800009x4", isSortable = false, manage = "true" )
    private int assemble = 0;

    @UIColumn( data = "solve", name = "solve", filter = "", renderer = "checkbox", title = "9800010x4", isSortable = false, manage = "true" )
    private int solve = 0;

    @UIColumn( data = "post", name = "post", filter = "", renderer = "checkbox", title = "9800011x4", isSortable = false, manage = "true" )
    private int post = 0;

    @UIColumn( data = "submit", name = "submit", filter = "", renderer = "checkbox", title = "9800003x4", isSortable = false, manage = "true" )
    private int submit = 0;

    public String getLoadcase() {
        return loadcase;
    }

    public void setLoadcase( String loadcase ) {
        this.loadcase = loadcase;
    }

    public int getAssemble() {
        return assemble;
    }

    public void setAssemble( int assemble ) {
        this.assemble = assemble;
    }

    public int getSolve() {
        return solve;
    }

    public void setSolve( int solve ) {
        this.solve = solve;
    }

    public int getPost() {
        return post;
    }

    public void setPost( int post ) {
        this.post = post;
    }

    public int getSubmit() {
        return submit;
    }

    public void setSubmit( int submit ) {
        this.submit = submit;
    }

    public UUID getId() {
        return id;
    }

    public void setId( UUID id ) {
        this.id = id;
    }

}

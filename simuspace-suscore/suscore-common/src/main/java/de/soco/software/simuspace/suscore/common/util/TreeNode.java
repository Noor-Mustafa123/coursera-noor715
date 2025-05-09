package de.soco.software.simuspace.suscore.common.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Tree node.
 *
 * @param <T>
 *         the type parameter
 */
@Setter
@Getter
public class TreeNode< T > {

    /**
     * The Data.
     */
    T data;

    /**
     * The Children.
     */
    List< TreeNode< T > > children;

    /**
     * Instantiates a new Tree node.
     *
     * @param data
     *         the data
     */
    public TreeNode( T data ) {
        this.data = data;
        this.children = new ArrayList<>();
    }

}

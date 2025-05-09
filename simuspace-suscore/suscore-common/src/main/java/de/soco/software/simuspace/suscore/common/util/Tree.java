package de.soco.software.simuspace.suscore.common.util;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Tree.
 *
 * @param <T>
 *         the type parameter
 */
@Setter
@Getter
public class Tree< T > implements Serializable {

    @Serial
    private static final long serialVersionUID = -2049482415505001218L;

    /**
     * The Root.
     */
    private TreeNode< T > root;

    /**
     * Instantiates a new Tree.
     *
     * @param root
     *         the root
     */
    public Tree( TreeNode< T > root ) {
        this.root = root;
    }

    /**
     * Add node.
     *
     * @param parent
     *         the parent
     * @param childData
     *         the child data
     *
     * @return tree node
     */
    public TreeNode< T > addChildToParent( TreeNode< T > parent, T childData ) {
        var child = new TreeNode<>( childData );
        parent.children.add( child );
        if ( root == null ) {
            root = parent;
        }
        return child;
    }

}

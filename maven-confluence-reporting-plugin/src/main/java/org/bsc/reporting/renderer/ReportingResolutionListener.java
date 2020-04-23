package org.bsc.reporting.renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ResolutionListener;
import org.apache.maven.artifact.versioning.VersionRange;

/**
 * 
 * @author Sorrentino
 *
 */
public class ReportingResolutionListener implements ResolutionListener {

    private Stack<Node> parents = new Stack<>();

    private Map<String,Node> artifacts = new HashMap<>();

    private Node rootNode;

    @Override
    public void testArtifact( Artifact artifact )
    {
        // intentionally blank
    }

    
    @Override
    public void startProcessChildren( Artifact artifact )
    {
        Node node = artifacts.get( artifact.getDependencyConflictId() );
        if ( parents.isEmpty() )
        {
            rootNode = node;
        }

        parents.push( node );
    }

    @Override
    public void endProcessChildren( Artifact artifact )
    {
        Node check = parents.pop();
        assert artifact.equals( check.artifact );
    }

    @Override
    public void omitForNearer( Artifact omitted, Artifact kept )
    {
        assert omitted.getDependencyConflictId().equals( kept.getDependencyConflictId() );

        Node prev = artifacts.get( omitted.getDependencyConflictId() );
        if ( prev != null )
        {
            if ( prev.parent != null )
            {
                prev.parent.children.remove( prev );
            }
            artifacts.remove( omitted.getDependencyConflictId() );
        }

        includeArtifact( kept );
    }

    @Override
    public void omitForCycle( Artifact artifact )
    {
        // intentionally blank
    }

    @Override
    public void includeArtifact( Artifact artifact )
    {
        if ( artifacts.containsKey( artifact.getDependencyConflictId() ) )
        {
            Node prev = artifacts.get( artifact.getDependencyConflictId() );
            if ( prev.parent != null )
            {
                prev.parent.children.remove( prev );
            }
            artifacts.remove( artifact.getDependencyConflictId() );
        }

        Node node = new Node();
        node.artifact = artifact;
        if ( !parents.isEmpty() )
        {
            node.parent = (Node) parents.peek();
            node.parent.children.add( node );
        }
        artifacts.put( artifact.getDependencyConflictId(), node );
    }

    @Override
    public void updateScope( Artifact artifact, String scope )
    {
        Node node = artifacts.get( artifact.getDependencyConflictId() );

        node.artifact.setScope( scope );
    }

    @Override
    public void manageArtifact( Artifact artifact, Artifact replacement )
    {
        Node node = artifacts.get( artifact.getDependencyConflictId() );

        if ( node != null )
        {
            if ( replacement.getVersion() != null )
            {
                node.artifact.setVersion( replacement.getVersion() );
            }
            if ( replacement.getScope() != null )
            {
                node.artifact.setScope( replacement.getScope() );
            }
        }
    }

    @Override
    public void updateScopeCurrentPom( Artifact artifact, String key )
    {
        // intentionally blank
    }

    @Override
    public void selectVersionFromRange( Artifact artifact )
    {
        // intentionally blank
    }

    @Override
    public void restrictRange( Artifact artifact, Artifact artifact1, VersionRange versionRange )
    {
        // intentionally blank
    }

    public Collection<Node> getArtifacts()
    {
        return artifacts.values();
    }

    static public class Node
    {
        private Node parent;

        private List<Node> children = new ArrayList<>();

        private Artifact artifact;

        public List<Node> getChildren()
        {
            return children;
        }

        public Artifact getArtifact()
        {
            return artifact;
        }
    }

    public Node getRootNode()
    {
        return rootNode;
    }

}

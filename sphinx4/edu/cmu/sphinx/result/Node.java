
/*
 * Copyright 1999-2002 Carnegie Mellon University.
 * Portions Copyright 2002 Sun Microsystems, Inc.
 * Portions Copyright 2002 Mitsubishi Electronic Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 *
 */
package edu.cmu.sphinx.result;

import edu.cmu.sphinx.result.Lattice;
import edu.cmu.sphinx.result.Edge;

import java.util.StringTokenizer;
import java.util.Iterator;
import java.util.Vector;
import java.util.Collection;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Nodes are part of Lattices.  The represent theories that words were spoken over a given time.
 */
public class Node {
    protected static int nodeCount=0; // used to generate unique IDs for new Nodes.

    protected String id;
    protected String word;
    protected int beginTime;
    protected int endTime;
    protected Vector fromEdges;
    protected Vector toEdges;

    {
        fromEdges=new Vector();
        toEdges=new Vector();
        nodeCount++;
    }

    /**
     * Create a new Node
     *
     * @param word
     * @param beginTime
     * @param endTime
     */
    protected Node(String word, int beginTime, int endTime) {
        this(getNextNodeId(), word, beginTime, endTime);
    }

    /**
     * Create a new Node with given ID.  Used when creating a Lattice from a .LAT file
     *
     * @param id
     * @param word
     * @param beginTime
     * @param endTime
     */
    protected Node(String id, String word, int beginTime, int endTime) {
        this.id = id;
        this.word = word;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    /**
     * Get a unique ID for a new Node.  Used when creating a Lattice from a .LAT file
     * @return
     */
    protected static String getNextNodeId() {
        return Integer.toString(nodeCount);
    }

    /**
     * Test if a node has an Edge to a Node
     * @param n
     * @return
     */
    protected boolean hasEdgeToNode(Node n) {
        for( Iterator j=toEdges.iterator(); j.hasNext(); ) {
            Edge e=(Edge) j.next();
            if( e.getToNode() == n ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test is a Node has an Edge from a Node
     *
     * @param n
     * @return
     */
    protected boolean hasEdgeFromNode(Node n) {
        for( Iterator j=fromEdges.iterator(); j.hasNext(); ) {
            Edge e=(Edge) j.next();
            if( e.getFromNode() == n ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test if a Node has all Edges from the same Nodes and another Node.
     *
     * @param n
     * @return
     */
    protected boolean hasEquivalentFromEdges(Node n) {
        if( fromEdges.size() != n.getFromEdges().size() ) {
            return false;
        }
        for(Iterator i=fromEdges.iterator(); i.hasNext();) {
            Edge e=(Edge) i.next();
            Node fromNode = e.getFromNode();
            if( !n.hasEdgeFromNode(fromNode) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Test if a Node has all Edges to the same Nodes and another Node.
     *
     * @param n
     * @return
     */
    public boolean hasEquivalentToEdges(Node n) {
        if( toEdges.size() != n.getToEdges().size() ) {
            return false;
        }
        for(Iterator i=toEdges.iterator(); i.hasNext();) {
            Edge e=(Edge) i.next();
            Node toNode = e.getToNode();
            if( !n.hasEdgeToNode(toNode) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the Edges from this Node
     *
     * @return
     */
    public Collection getFromEdges() {
        return fromEdges;
    }

    /**
     * Get the Edges to this Node
     *
     * @return
     */
    public Collection getToEdges() {
        return toEdges;
    }

    /**
     * Add an Edge from this Node
     *
     * @param e
     */
    protected void addFromEdge(Edge e) {
        fromEdges.add(e);
    }

    /**
     * Add an Edge to this Node
     *
     * @param e
     */
    protected void addToEdge(Edge e) {
        toEdges.add(e);
    }

    /**
     * Remove an Edge from this Node
     *
     * @param e
     */
    protected void removeFromEdge(Edge e) {
        fromEdges.remove(e);
    }

    /**
     * Remove an Edge to this Node
     *
     * @param e
     */
    public void removeToEdge(Edge e) {
        toEdges.remove(e);
    }

    /**
     * Get this unique ID associated with this Node
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Get the word associated with this Node
     *
     * @return
     */
    public String getWord() {
        return word;
    }

    /**
     * Get the time when the word began
     *
     * @return
     */
    public int getBeginTime() {
        return beginTime;
    }

    /**
     * Get the time when the word ends
     *
     * @return
     */
    public int getEndTime() {
        return endTime;
    }

    public String toString() {
        return "Node(" + id + ":" + word + ")";
    }

    /**
     * Internal routine when dumping Lattices as AiSee files
     *
     * @param f
     * @throws IOException
     */
    void dumpAISee(FileWriter f) throws IOException {
        f.write( "node: { title: \"" + id + "\" label: \"" + getWord() + "\" }\n" );
    }

    /**
     * Internal routine used when dumping Lattices as .LAT files
     * @param f
     * @throws IOException
     */
    void dump(PrintWriter f) throws IOException {
        f.println( "node: " + id + " " + word );
    }

    /**
     * Internal routine used when loading Lattices from .LAT files
     * @param lattice
     * @param tokens
     */
    static void load( Lattice lattice, StringTokenizer tokens ) {

        String id = tokens.nextToken();
        String label = tokens.nextToken();

        Node node = lattice.addNode(id,label,0,0);
    }
}

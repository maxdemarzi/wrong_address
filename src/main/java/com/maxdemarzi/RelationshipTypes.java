package com.maxdemarzi;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType
{
    BELONGS_TO,
    HAS_ADDRESS,
    KNOWS,
    WORKS_FOR
}

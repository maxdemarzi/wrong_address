package com.maxdemarzi;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.ArrayList;
import java.util.HashMap;

public class PotentialAddressTest {
    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withFixture(MODEL_STATEMENT)
            .withExtension("/v1", User.class);

    @Test
    public void shouldFindMatchingAddress() {
        HashMap<String, Object> QUERY_MAP = new HashMap<String, Object>(){{
            put("address", "176 Norht Harbor Dr. Chicago IL 60601");
        }};

        HTTP.Response response = HTTP.POST(neo4j.httpURI().resolve("/v1/user/maxdemarzi/potential_address").toString(), QUERY_MAP);
        ArrayList<HashMap<String, Object>> actual  = response.content();
        Assert.assertEquals(expected, actual);
    }

    private static final ArrayList expected = new ArrayList() {{

        add(new HashMap<String, Object>() {{
            put("address", "175 North Harbor Dr. Chicago IL 60605-1234");
            put("score", 0.8478261);
        }});
        add(new HashMap<String, Object>() {{
            put("address", "571 South Harbor Dr. Chicago IL 60601-4321");
            put("score", 0.73913044);
        }});
    }};

    public static final String MODEL_STATEMENT =
            new StringBuilder()
                    .append("CREATE (max:User {username:'maxdemarzi'})")
                    .append("CREATE (a1:Address {line1:'175 North Harbor Dr.', city:'Chicago', state:'IL', zip:'60605', zip_plus_4:'1234'})")
                    .append("CREATE (a2:Address {line1:'571 South Harbor Dr.', city:'Chicago', state:'IL', zip:'60601', zip_plus_4:'4321'})")
                    .append("CREATE (max)-[:HAS_ADDRESS]->(a1)")
                    .append("CREATE (max)-[:HAS_ADDRESS]->(a2)")
                    .append("CREATE (sam:User {username:'samadams'})")
                    .append("CREATE (a3:Address {line1:'176 North Harbor Dr.', city:'Chicago', state:'IL', zip:'60605', zip_plus_4:'1234'})")
                    .append("CREATE (sam)-[:HAS_ADDRESS]->(a2)")
                    .toString();
}
package com.maxdemarzi;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.*;
import org.simmetrics.StringMetric;
import org.simmetrics.metrics.DamerauLevenshtein;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import static com.maxdemarzi.Validators.getValidPotentialAddressQueryInput;

@Path("/user")
public class User {


    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final AddressSimplifier addressSimplifier = new AddressSimplifier();
    private static final ScoreComparator scoreComparator = new ScoreComparator();
    StringMetric metric = new DamerauLevenshtein();

    /**
     * JSON formatted body requires:
     * address: The possible address
     *
     */
    @POST
    @Path("/{username}/potential_address")
    public Response potentialAddress(@PathParam("username") String username, String body, @Context GraphDatabaseService db) throws IOException {
        ArrayList<HashMap> results = new ArrayList<>();
        HashMap input = getValidPotentialAddressQueryInput(body);

        try (Transaction tx = db.beginTx()) {
            Node user = db.findNode(Labels.User, "username", username);
            if (user != null) {
                HashSet<String> addresses = new HashSet<>();

                for (Relationship rel : user.getRelationships(Direction.OUTGOING, RelationshipTypes.HAS_ADDRESS)) {
                    Node address = rel.getEndNode();
                    addresses.add(
                            address.getProperty("line1", "") + " " +
                                    address.getProperty("city", "") + " " +
                                    address.getProperty("state", "") + " " +
                                    address.getProperty("zip", "") + "-" +
                                    address.getProperty("zip_plus_4", "")
                    );
                }

                String potential_address = addressSimplifier.simplify((String) input.get("address"));
                for (String address : addresses) {
                    Float score = metric.compare(potential_address, addressSimplifier.simplify(address));
                    if (score > 0.50) {
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("address", address);
                        result.put("score", score);
                        results.add(result);
                    }
                }
            }
        }
        Collections.sort(results, scoreComparator);

        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
    }
}

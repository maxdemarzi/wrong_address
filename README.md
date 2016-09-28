# wrong_address

Find likely wrong addresses by exploring known connected addresses

#### Instructions

1. Build it:

        mvn clean package

2. Copy target/wrong-address-1.0-SNAPSHOT-jar-with-dependencies to the plugins/ directory of your Neo4j server.

3. Configure Neo4j by adding a line to conf/neo4j.conf:

        dbms.unmanaged_extension_classes=com.maxdemarzi=/v1

4. Start Neo4j server.

        bin/neo4j start

5. Check that it is installed correctly over HTTP:

        :GET /v1/service/helloworld
        
6. Create the schema:

        :POST /v1/schema/create        

7. Make some trial data:

       CREATE (max:User {username:'maxdemarzi'})
       CREATE (a1:Address {line1:'175 North Harbor Dr.', city:'Chicago', state:'IL', zip:'60605', zip_plus_4:'1234'})
       CREATE (max)-[:HAS_ADDRESS]->(a1)
       CREATE (tom:User {username:'tomjones'})
       CREATE (a2:Address {line1:'571 South Harbor Dr.', city:'Chicago', state:'IL', zip:'60601', zip_plus_4:'4321'})
       CREATE (tom)-[:HAS_ADDRESS]->(a2)
       CREATE (max)-[:KNOWS]->(tom)
       CREATE (neo:Company {name:'Neo Technology'})
       CREATE (a3:Address {line1:'111 E 5th Avenue', city:'San Mateo', state:'CA', zip:'94401'})
       CREATE (neo)-[:HAS_ADDRESS]->(a3)
       CREATE (max)-[:WORKS_FOR]->(neo)
       CREATE (org:Organization {name:'Graph Meetup'})
       CREATE (a4:Address {line1:'17 East State St.', city:'Chicago', state:'IL', zip:'60605', zip_plus_4:'5533'})
       CREATE (org)-[:HAS_ADDRESS]->(a4)
       CREATE (max)-[:BELONGS_TO]->(org)
        
8. Try the extension:

        :POST /v1/user/{user_name}/potential_address {"address"":"some address"}

   example:
        
        :POST /v1/user/maxdemarzi/potential_address {"address":"571 South State St. Chicago IL 60601"}
        :POST /v1/user/samadams/potential_address {"address":"167 South Harbor Dr. Chicoga IL 60604"}


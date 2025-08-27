package me.sreejithnair.linkup.connection_service.repository;

import me.sreejithnair.linkup.connection_service.node.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, Long> {

    @Query("MATCH (personA:Person) -[:CONNECTED_TO]- (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "RETURN personB"
    )
    List<Person> getFirstDegreeConnections(Long userId);


    @Query("MATCH (personA:Person) -[r:REQUESTED_TO]-> (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "AND personB.userId = receiverId " +
            "RETURN count(r) > 0"
    )
    boolean connectionRequestExist(Long senderId, Long receiverId);

    @Query("MATCH (personA:Person) -[r:CONNECTED_TO]- (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "AND personB.userId = receiverId " +
            "RETURN count(r) > 0"
    )
    boolean alreadyConnected(Long senderId, Long receiverId);


    @Query("MATCH (personA:Person), (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "AND personB.userId = receiverId " +
            "CREATE (personA) -[r:REQUESTED_TO]-> (personB)"
    )
    void addConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (personA:Person) -[r:REQUESTED_TO]-> (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "DELETE r " +
            "CREATE (personA) -[r:CONNECTED_TO]-> (personB)"
    )
    void acceptConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (personA:Person) -[r:REQUESTED_TO]-> (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "DELETE r "
    )
    void rejectConnectionRequest(Long senderId, Long receiverId);
}

package kz.kaspi.core.antifraudengine.gateway.repository.neo4j;

import kz.kaspi.core.antifraudengine.gateway.entity.neo4j.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphUserRepository extends Neo4jRepository<UserNode, String> {

    // Cypher-запрос для поиска колец отмывания денег глубиной до 3 шагов.
    // Если A -> B -> C -> A, то это подозрительное кольцо!
    @Query("MATCH path = (u:User {userId: $senderId})-[:TRANSFERRED_TO*1..3]->(u) RETURN count(path) > 0")
    boolean isPartOfMoneyLaunderingRing(@Param("senderId") String senderId);

    // Добавляем связь о переводе
    @Query("MERGE (a:User {userId: $senderId}) MERGE (b:User {userId: $receiverId}) MERGE (a)-[:TRANSFERRED_TO]->(b)")
    void recordTransaction(@Param("senderId") String senderId, @Param("receiverId") String receiverId);
}

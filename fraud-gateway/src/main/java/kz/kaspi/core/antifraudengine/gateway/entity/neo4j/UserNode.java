package kz.kaspi.core.antifraudengine.gateway.entity.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@Node("User")
public class UserNode {
    @Id
    private String userId;

    @Relationship(type = "TRANSFERRED_TO", direction = Relationship.Direction.OUTGOING)
    private Set<UserNode> transferredTo = new HashSet<>();
}

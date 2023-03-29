package ovh.major.joboffers.domain.offer;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document("offers")
public
record Offer(
        @Id String id,
        String position, //@Field("nazwa") = można zmienić nazwę pola w bazie
        String company,
        String salary,
        @Indexed(unique = true) String offerUrl) {
}

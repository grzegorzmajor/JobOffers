package ovh.major.joboffers.controler.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import ovh.major.joboffers.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OfferControllerErrorIntegrationTest extends BaseIntegrationTest {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    public void should_return_conflict_409_when_added_offer_with_same_ofer_url() throws Exception {
        //Step 1
        //given
        String contentOfferJson1 = """
                {
                "position" : "Junior Java Developer",
                "company": "Company that employs juniors",
                "salary": "5000 - 9000 PLN",
                "offerUrl": "https://goodoffer.pl/offer/7684325"
                }
                """.trim();

        //when
        ResultActions perform1 = mockMvc.perform(post("/offers")
                .content(contentOfferJson1)
                .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8" )
        );

        //then
        perform1.andExpect(status().isCreated());

        //Step 2
        //given
        String contentOfferJson2 = """
                {
                "position" : "Junior Java Developer",
                "company": "The Same Company that employs juniors as before",
                "salary": "5000 - 9000 PLN",
                "offerUrl": "https://goodoffer.pl/offer/7684325"
                }
                """.trim();

        //when
        ResultActions perform2 = mockMvc.perform(post("/offers")
                .content(contentOfferJson2)
                .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8" )
        );

        //then
        perform2.andExpect(status().isConflict());
    }

    @Test
    void should_return_404_not_found_when_delete_offer_with_an_id_that_doesnt_exist() throws Exception {
        //given
        String id = "takie id nie istnieje";

        //when
        ResultActions perform = mockMvc.perform(post("/offers/" + id));

        //then
        perform.andExpect(status().isNotFound());

    }

}

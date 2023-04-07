package ovh.major.joboffers.apivalidationerror;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ovh.major.joboffers.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ApiValidationFailedIntegrationTest  extends BaseIntegrationTest {

    @Test
    public void should_return_400_bad_request_and_validation_message_when_request_has_empty_object() throws Exception {
        //given
        //when
        String contentOfferJson = """
                {}
                """.trim();
        //when
        ResultActions performPost = mockMvc.perform(post("/offers")
                .content(contentOfferJson)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult mvcResult = performPost.andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void should_return_400_bad_request_and_validation_message_when_request_has_object_with_empty_values() throws Exception {
        //given
        //when
        String contentOfferJson = """
                {
                "position" : "",
                "company": "",
                "salary": "",
                "offerUrl": ""
                }
                """.trim();
        //when
        ResultActions performPost = mockMvc.perform(post("/offers")
                .content(contentOfferJson)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        MvcResult mvcResult = performPost.andExpect(status().isBadRequest()).andReturn();
    }
}

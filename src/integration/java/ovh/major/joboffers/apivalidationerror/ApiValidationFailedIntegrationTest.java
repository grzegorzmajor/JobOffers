package ovh.major.joboffers.apivalidationerror;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ovh.major.joboffers.BaseIntegrationTest;
import ovh.major.joboffers.infrastructure.offer.validation.ValidationErrorDto;
import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ApiValidationFailedIntegrationTest  extends BaseIntegrationTest {

    @Test
    @WithMockUser
    public void should_return_400_bad_request_and_validation_message_when_request_has_empty_or_null_object_or_value_in_object() throws Exception {

        //given
        //when
        String contentOfferJson1 = """
                {}
                """.trim();
        //when
        ResultActions performPost1 = mockMvc.perform(post("/offers")
                .content(contentOfferJson1)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult mvcResult1 = performPost1.andExpect(status().isBadRequest()).andReturn();
        String resultJson1 = mvcResult1.getResponse().getContentAsString();
        ValidationErrorDto result1 = objectMapper.readValue(resultJson1, ValidationErrorDto.class );
        assertThat(result1.messages()).containsExactlyInAnyOrder(
                "position must not be null",
                "company must not be null",
                "salary must not be null",
                "offer url must not be null",
                "position must not be empty",
                "company must not be empty",
                "salary must not be empty",
                "offer url must not be empty"
        );

        //given
        //when
        String contentOfferJson2 = """
                {
                "position" : "",
                "company": "",
                "salary": "",
                "offerUrl": ""
                }
                """.trim();
        //when
        ResultActions performPost2 = mockMvc.perform(post("/offers")
                .content(contentOfferJson2)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        MvcResult mvcResult2 = performPost2.andExpect(status().isBadRequest()).andReturn();
        String resultJson2 = mvcResult2.getResponse().getContentAsString();
        ValidationErrorDto result2 = objectMapper.readValue(resultJson2, ValidationErrorDto.class );
        assertThat(result2.messages()).containsExactlyInAnyOrder(
                "position must not be empty",
                "company must not be empty",
                "salary must not be empty",
                "offer url must not be empty"
        );
    }
}

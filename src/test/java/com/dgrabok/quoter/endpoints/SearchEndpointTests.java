package com.dgrabok.quoter.endpoints;

import com.dgrabok.quoter.services.api.SearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class SearchEndpointTests {
    @Autowired
    private SearchService service;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void BeforeEach() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    /**
     * `/api/search` ENDPOINT TESTS
     */

    @Test
    public void quotesShouldBeSearched() throws Exception {
        String url = "/api/search?text=" +
                "aaabbbccc" +
                "It's sort of my job to feel good." +
                "dddeeefff" +
                "I would like to direct." +
                "ggghhhiii";
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string("[" +
                        "{\"author\":\"A. J. Jacobs\",\"text\":\"It's sort of my job to feel good.\"}," +
                        "{\"author\":\"Aaron Eckhart\",\"text\":\"I would like to direct.\"}" +
                "]"));
    }

    @Test
    public void quoteShouldBeSearchedWhenTextFullyEqualToQuote() throws Exception {
        String url = "/api/search?text=If you live to be a hundred, I want to live to be a hundred minus one day so I never have to live without you.";
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string("[" +
                        "{\"author\":\"A. A. Milne\",\"text\":\"If you live to be a hundred, I want to live to be a hundred minus one day so I never have to live without you.\"}" +
                "]"));
    }

    @Test
    public void quoteShouldBeSearchedWhenTextStartedWithQuote() throws Exception {
        String url = "/api/search?text=" +
                "It's sort of my job to feel good." +
                "dddeeefff" +
                "I would like to direct." +
                "ggghhhiii";
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string("[" +
                        "{\"author\":\"A. J. Jacobs\",\"text\":\"It's sort of my job to feel good.\"}," +
                        "{\"author\":\"Aaron Eckhart\",\"text\":\"I would like to direct.\"}" +
                        "]"));
    }

    @Test
    public void quoteShouldBeSearchedWhenTextEndedWithQuote() throws Exception {
        String url = "/api/search?text=" +
                "aaabbbccc" +
                "It's sort of my job to feel good." +
                "dddeeefff" +
                "I would like to direct.";
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string("[" +
                        "{\"author\":\"A. J. Jacobs\",\"text\":\"It's sort of my job to feel good.\"}," +
                        "{\"author\":\"Aaron Eckhart\",\"text\":\"I would like to direct.\"}" +
                        "]"));
    }

    @Test
    public void shouldProduceEmptyJsonArrayForBlankTestParameter() throws Exception {
        mockMvc.perform(get("/api/search?text=").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string("[]"));
    }

    @Test
    public void shouldThrowExceptionWhenTextNotProvided() throws Exception {
        mockMvc.perform(get("/api/search").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}

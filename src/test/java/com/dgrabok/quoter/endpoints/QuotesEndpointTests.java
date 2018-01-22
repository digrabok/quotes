package com.dgrabok.quoter.endpoints;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintViolationException;

import static com.dgrabok.quoter.TestsUtils.executionExceptionTest;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class QuotesEndpointTests {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void BeforeEach() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    /**
     * `fetchAll` METHOD TESTS
     */

    @Test
    public void shouldReturnAllQuotes() throws Exception {
        mockMvc.perform(get("/api/quotes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string("[" +
                        "{\"author\":\"A. A. Milne\",\"text\":\"If you live to be a hundred, I want to live to be a hundred minus one day so I never have to live without you.\"}," +
                        "{\"author\":\"A. J. Jacobs\",\"text\":\"It's sort of my job to feel good.\"}," +
                        "{\"author\":\"A. P. J. Abdul Kalam\",\"text\":\"Great dreams of great dreamers are always transcended.\"}," +
                        "{\"author\":\"Aaliyah\",\"text\":\"There are times in my life when I just want to be by myself.\"}," +
                        "{\"author\":\"Aaron Eckhart\",\"text\":\"I would like to direct.\"}" +
                        "]"));
    }

    /**
     * `fetch` METHOD TESTS
     */

    @Test
    public void shouldReturnQuoteById() throws Exception {
        mockMvc.perform(get("/api/quotes/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string("{\"author\":\"A. P. J. Abdul Kalam\",\"text\":\"Great dreams of great dreamers are always transcended.\"}"));
    }

    @Test
    public void shouldThrowExceptionIfIdValueIsIncorrect() throws Exception {
        boolean success = false;
        try {
            mockMvc.perform(get("/api/quotes/0").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                success = true;
            }
        }
        assertTrue("ConstraintViolationException should be throwed for 0 id", success);
    }
}

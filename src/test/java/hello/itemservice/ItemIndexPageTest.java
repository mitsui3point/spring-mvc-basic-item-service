package hello.itemservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ItemServiceApplication.class)
public class ItemIndexPageTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void indexTest() throws Exception {
        //given
        byte[] expected = getFileBytes("src/main/resources/static/index.html");

        //when
        ResultActions perform = mvc.perform(get("/index.html")
                .contentType(MediaType.TEXT_HTML_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.TEXT_HTML_VALUE.concat(";charset=").concat(StandardCharsets.UTF_8.name()))
        );

        //then
        perform.andDo(print())
                .andExpect(content().bytes(expected));
    }

    private byte[] getFileBytes(String path) throws IOException {
        return Files.readAllBytes(new File(path).toPath());
    }
}

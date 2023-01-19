package hello.itemservice.web;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BasicItemController.class)
public class BasicItemControllerTest {
    @Autowired
    private MockMvc mvc;

    /**
     * @MockBean : {@link MockBean}은 Spring ApplicationContext에 Mock객체를 추가하게 해주는 주석이다.
     * <br>: BasicItemController 에 ItemRepository 를 주입해야 하므로 ApplicationContext 에 구현되지 않은 MockBean 으로 등록하였음.
     * <br>: @Autowired 그대로 입력시 hello.itemservice.web.BasicItemController required a bean... 오류
     * <br>: https://velog.io/@sproutt/MockBean%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%9C-%ED%86%B5%ED%95%A9Controller%ED%85%8C%EC%8A%A4%ED%8A%B8#mockbean
     */
    @MockBean
    private ItemRepository itemRepository;

    @Test
    void itemsTest() throws Exception {
        //given
        Item item1 = Item.builder().itemName("item1").price(10000).quantity(10).build();
        Item item2 = Item.builder().itemName("item2").price(20000).quantity(20).build();
        item1.setId(1L);
        item2.setId(2L);

        //when
        when(itemRepository.findAll())
                .thenReturn(Arrays.asList(item1, item2));//Mockito MockBean (ItemRepository 는 현재 BasicItemController 주입목적인 MockBean 이므로 로직이 동작하지 않기 때문에 결과를 직접 입력)
        ResultActions perform = mvc.perform(get("/basic/items")
                .accept(MediaType.ALL)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        //then
        perform.andDo(print())
                .andExpect(view().name("basic/items"))
                .andExpect(model().attribute("items", itemRepository.findAll()));
    }

    @Test
    void itemTest() throws Exception {
        //given
        Item item1 = Item.builder().itemName("item1").price(10000).quantity(10).build();
        Item item2 = Item.builder().itemName("item2").price(20000).quantity(20).build();
        item1.setId(1L);
        item2.setId(2L);

        //when
        when(itemRepository.findById(item2.getId()))
                .thenReturn(item2);//Mockito MockBean (ItemRepository 는 현재 BasicItemController 주입목적인 MockBean 이므로 로직이 동작하지 않기 때문에 결과를 직접 입력)
        ResultActions perform = mvc.perform(get("/basic/items/%d".formatted(item2.getId()))
                .accept(MediaType.ALL)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("basic/item"))
                .andExpect(model().attribute("item", itemRepository.findById(item2.getId())));
    }
}

package hello.itemservice.repository;

import hello.itemservice.domain.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRepositoryTest {
    private final ItemRepository itemRepository = new ItemRepository();

    @BeforeEach
    void setUp() {
        itemRepository.clear();
    }

    @Test
    void saveTest() {
        //given
        Item expected = Item.builder().itemName("item1").price(11).quantity(10).build();
        Item expected2 = Item.builder().itemName("item2").price(22).quantity(20).build();
        //when
        Item saveItem = itemRepository.save(expected);
        Item saveItem2 = itemRepository.save(expected2);
        //then
        assertThat(saveItem).extracting("itemName").isEqualTo(expected.getItemName());
        assertThat(saveItem).extracting("id").isEqualTo(expected.getId());
        assertThat(saveItem2).extracting("itemName").isEqualTo(expected2.getItemName());
        assertThat(saveItem2).extracting("id").isEqualTo(expected2.getId());
    }

    @Test
    void findByIdTest() {
        //given
        Item expected = Item.builder().itemName("item1").price(11).quantity(10).build();
        Item expected2 = Item.builder().itemName("item2").price(22).quantity(20).build();
        //when
        itemRepository.save(expected);
        itemRepository.save(expected2);
        Item actual = itemRepository.findById(expected.getId());
        //then
        assertThat(actual).extracting("itemName").isEqualTo(expected.getItemName());
        assertThat(actual).extracting("id").isEqualTo(expected.getId());
    }

    @Test
    void findAllTest() {
        //given
        Item[] expected = new Item[]{
                itemRepository.save(Item.builder().itemName("item1").price(11).quantity(10).build()),
                itemRepository.save(Item.builder().itemName("item2").price(22).quantity(20).build())
        };
        //when
        List<Item> actual = itemRepository.findAll();
        //then
        assertThat(actual).containsExactly(expected);
    }

    @Test
    void updateTest() {
        //given
        Item targetItem = itemRepository.save(Item.builder().itemName("item1").price(11).quantity(10).build());
        itemRepository.save(Item.builder().itemName("item2").price(22).quantity(20).build());

        Item updatedParam = Item.builder().itemName("updatedItem").price(22).quantity(30).build();

        //when
        itemRepository.update(targetItem.getId(), updatedParam);
        Item actual = itemRepository.findById(targetItem.getId());

        //then
        assertThat(actual).extracting("itemName").isEqualTo(updatedParam.getItemName());
        assertThat(actual).extracting("price").isEqualTo(updatedParam.getPrice());
        assertThat(actual).extracting("quantity").isEqualTo(updatedParam.getQuantity());
    }

    @AfterEach
    void tearDown() {
        itemRepository.clear();
    }
}

package hello.itemservice.domain;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 상품 도메인 모델
 * 상품 ID
 * 상품명
 * 가격
 * 수량
 */
public class ItemTest {
    @Test
    void itemDomainTest() {
        //given
        Long id = new AtomicLong(1L).get();
        String itemName = "item1";
        Integer price = 100;
        Integer quantity = 10;
        //when
        Item item = Item.builder()
                .itemName(itemName)
                .price(price)
                .quantity(quantity)
                .build();
        //then
        assertThat(item.getItemName()).isEqualTo(itemName);
        assertThat(item.getPrice()).isEqualTo(price);
        assertThat(item.getQuantity()).isEqualTo(quantity);
    }
}

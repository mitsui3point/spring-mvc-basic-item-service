package hello.itemservice.domain;

import lombok.*;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@ToString(of = {"id", "itemName", "price", "quantity"})
@NoArgsConstructor
@EqualsAndHashCode
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    @Builder
    private Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}

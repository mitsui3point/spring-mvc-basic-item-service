package hello.itemservice.dto;

import hello.itemservice.domain.Item;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "itemName", "price", "quantity"})
@EqualsAndHashCode
public class ItemEditFormDto {
    private static final String INVALID_ITEM_NAME_EMPTY = "상품명은 빈값일 수 없습니다.";

    private Long id;

    @NotBlank(message = INVALID_ITEM_NAME_EMPTY)
    private String itemName;

    private Integer price;

    private Integer quantity;

    @Builder
    private ItemEditFormDto(Long id, String itemName, Integer price, Integer quantity) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public Item toItem() {
        Item item = Item.builder()
                .itemName(this.itemName)
                .price(this.price)
                .quantity(this.quantity)
                .build();
        item.setId(this.id);
        return item;
    }
}

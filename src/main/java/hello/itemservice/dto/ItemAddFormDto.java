package hello.itemservice.dto;

import hello.itemservice.domain.Item;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemAddFormDto {
    private static final String INVALID_ITEM_NAME_EMPTY = "상품명은 빈값일 수 없습니다.";
    @NotBlank(message = INVALID_ITEM_NAME_EMPTY)
    private String itemName;
    private Integer price;
    private Integer quantity;

    @Builder
    private ItemAddFormDto(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public Item toItem() {
        return Item.builder()
                .itemName(this.itemName)
                .price(this.price)
                .quantity(this.quantity)
                .build();
    }
}

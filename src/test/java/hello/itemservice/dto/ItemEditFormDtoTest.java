package hello.itemservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ItemEditFormDtoTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeEach
    void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    void tearDown() {
        factory.close();
    }

    @Test
    void invalidFieldsTest() {
        //given
        ItemEditFormDto itemEditFormDto = ItemEditFormDto.builder()
                .id(1L)
                .itemName("")//빈값 불허
                .price(10000)
                .quantity(10)
                .build();

        //when
        Set<ConstraintViolation<ItemEditFormDto>> validate = validator.validate(itemEditFormDto);

        //then
        assertThat(validate.size()).isEqualTo(1);
        assertThat(validate)
                .extracting(constraintViolation -> constraintViolation.getPropertyPath().toString())
                .containsExactly("itemName");
        assertThat(validate)
                .extracting(ConstraintViolation::getMessage)
                .containsExactly("상품명은 빈값일 수 없습니다.");
    }
}

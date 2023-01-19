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

/**
 * @ValidatorFactory {@link ValidatorFactory}
 * @Validator {@link Validator}
 * @refLink https://velog.io/@_koiil/SpringBoot-Spring-Validation%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%9C%A0%ED%9A%A8%EC%84%B1-%EA%B2%80%EC%A6%9D
 */
@SpringBootTest
public class ItemAddFormDtoTest {
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
    void invalidNameTest() {
        //given
        ItemAddFormDto itemAddDto = ItemAddFormDto.builder().itemName("").price(10000).quantity(10).build();

        //when
        Set<ConstraintViolation<ItemAddFormDto>> validate = validator.validate(itemAddDto);

        //then
        assertThat(validate.size()).isEqualTo(1);

        validate.forEach(violation -> {
            assertThat(violation.getPropertyPath().toString()).isEqualTo("itemName");
            assertThat(violation.getMessage()).isEqualTo("상품명은 빈값일 수 없습니다.");
        });
    }
}

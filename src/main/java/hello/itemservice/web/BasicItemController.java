package hello.itemservice.web;

import hello.itemservice.domain.Item;
import hello.itemservice.dto.ItemAddFormDto;
import hello.itemservice.dto.ItemEditFormDto;
import hello.itemservice.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/basic/items")
public class BasicItemController {
    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        model.addAttribute("item", itemRepository.findById(itemId));
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("itemAddFormDto", new ItemAddFormDto());
        return "basic/addForm";
    }

    /**
     * @ModelAttribute - 요청 파라미터 처리
     * <br>: {@link ModelAttribute} 는 Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력해준다.
     * <br>: Model 추가
     * <br>: {@link ModelAttribute} 의 중요한 한가지 기능이 더 있는데, 바로 모델(Model)에 @ModelAttribute 로 지정한 객체를 자동으로 넣어준다.
     * <br>: 지금 코드를 보면 model.addAttribute("itemAddFormDto", itemAddFormDto) 가 주석처리 되어 있어도 잘 동작하는 것을 확인할 수 있다.
     * <br>: 모델에 데이터를 담을 때는 이름이 필요하다. 이름은 {@link ModelAttribute} 에 지정한 name(value) 속성을 사용한다. {@code @ModelAttribute("itemAddFormDto")}
     * <br>: 만약 다음과 같이 {@link ModelAttribute} 의 이름을 다르게 지정하면 다른 이름으로 모델에 포함된다.
     * @ModelAttribute - 이름 생략
     * <br>: {@link ModelAttribute}의 이름을 생략할 수 있다.
     */
//    @PostMapping(value = "/add")
    public String savePRG(/*@ModelAttribute("itemAddFormDto") */
            @Valid ItemAddFormDto itemAddFormDto,//attribute name ItemAddFormDto -> itemAddFormDto
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "basic/addForm";
        }
        //model.addAttribute("itemAddFormDto", itemAddFormDto)
        Item saveParam = itemAddFormDto.toItem();
        Item resultBody = itemRepository.save(saveParam);
        return "redirect:/basic/items/" + resultBody.getId();
    }

    /**
     * 상품 등록을 완료하고 웹 브라우저의 새로고침 버튼을 클릭해보자.
     * <br> 상품이 계속해서 중복 등록되는 것을 확인할 수 있다.
     * <br> 웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.
     * <br> 상품 등록 폼에서 데이터를 입력하고 저장을 선택하면 POST /add + 상품 데이터를 서버로 전송한다.
     * <br> 이 상태에서 새로 고침을 또 선택하면 마지막에 전송한 POST /add + 상품 데이터를 서버로 다시 전송하게 된다.
     * <br> 그래서 내용은 같고, ID만 다른 상품 데이터가 계속 쌓이게 된다.
     * <p>
     * 이 문제를 어떻게 해결할 수 있을까?
     * <br> POST, Redirect GET
     * <br> 웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.
     * <br> 새로 고침 문제를 해결하려면 상품 저장 후에 뷰 템플릿으로 이동하는 것이 아니라,
     * <br> 상품 상세 화면으로 리다이렉트를 호출해주면 된다.
     * <br> 웹 브라우저는 리다이렉트의 영향으로 상품 저장 후에 실제 상품 상세 화면으로 다시 이동한다.
     * <br> 따라서 마지막에 호출한 내용이 상품 상세 화면인 GET /items/{id} 가 되는 것이다.
     * <br> 이후 새로고침을 해도 상품 상세 화면으로 이동하게 되므로 새로 고침 문제를 해결할 수 있다.
     */
//    @PostMapping(value = "/add")
    public String saveWrongExample(@Valid ItemAddFormDto itemAddFormDto,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            return "basic/addForm";
        }
        Item saveParam = itemAddFormDto.toItem();
        model.addAttribute("item", itemRepository.save(saveParam));
        return "basic/item";
    }

    /**
     * @RedirectAttributes {@link RedirectAttributes}
     * <br> RedirectAttributes 를 사용하면 URL 인코딩도 해주고, pathVarible, 쿼리 파라미터까지 처리해준다.
     * <br> redirect:/basic/items/{itemId}
     * <br> pathVariable 바인딩: {itemId}
     * <br> 나머지는 쿼리 파라미터로 처리: ?status=true
     */
    @PostMapping(value = "/add")
    public String saveRedirectAttribute(@ModelAttribute @Valid ItemAddFormDto itemAddFormDto,
                                        BindingResult bindingResult,//BindingResult bindingRsult 파라미터의 위치는 @ModelAttribute Item item 다음에 와야한다.
                                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "basic/addForm";
        }

        Item saveParam = itemAddFormDto.toItem();

        Item resultBody = itemRepository.save(saveParam);

        redirectAttributes.addAttribute("itemId", resultBody.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);

        if (item == null) {
            return "basic/notFound";
        }

        ItemEditFormDto itemEditFormDto = ItemEditFormDto.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build();
        model.addAttribute("itemEditFormDto", itemEditFormDto);
        return "basic/editForm";
    }

    /**
     * @참고 <br> HTML Form 전송은 PUT, PATCH를 지원하지 않는다. GET, POST만 사용할 수 있다.
     * <br> PUT, PATCH는 HTTP API 전송시에 사용
     * <br> 스프링에서 HTTP POST로 Form 요청할 때 히든 필드를 통해서 PUT, PATCH 매핑을 사용하는 방법이 있지만, HTTP 요청상 POST 요청이다
     */
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId,
                       @Valid @ModelAttribute ItemEditFormDto itemEditFormDto,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "basic/editForm";
        }
        Item updateParam = itemEditFormDto.toItem();
        itemRepository.update(itemEditFormDto.getId(), updateParam);

        return "redirect:/basic/items/{itemId}";
    }

    @PostConstruct
    public void init() {
        itemRepository.save(Item.builder().itemName("itemA").price(10000).quantity(10).build());
        itemRepository.save(Item.builder().itemName("itemB").price(20000).quantity(20).build());
    }
}

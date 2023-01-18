package hello.itemservice.web;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

    @GetMapping("/basic/items")
    public String items(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        return "basic/items";
    }

    @PostConstruct
    public void init() {
        itemRepository.save(Item.builder().itemName("itemA").price(10000).quantity(10).build());
        itemRepository.save(Item.builder().itemName("itemB").price(20000).quantity(20).build());
    }
}

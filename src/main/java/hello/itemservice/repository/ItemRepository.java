package hello.itemservice.repository;

import hello.itemservice.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemRepository {
    private static final Map<Long, Item> store = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0L);

    public Item save(Item item) {
        long itemId = sequence.incrementAndGet();
        item.setId(itemId);
        store.put(itemId, item);
        return store.get(itemId);
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clear() {
        sequence.set(0L);
        store.clear();
    }

    public void update(Long itemId, Item updateParam) {
        Item item = findById(itemId);
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }
}

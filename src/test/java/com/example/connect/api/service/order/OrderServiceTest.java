package com.example.connect.api.service.order;

import com.example.connect.api.domain.embedded.Address;
import com.example.connect.api.domain.item.Book;
import com.example.connect.api.domain.item.Item;
import com.example.connect.api.domain.item.ItemRepository;
import com.example.connect.api.domain.member.Member;
import com.example.connect.api.domain.member.MemberRepository;
import com.example.connect.api.domain.order.Order;
import com.example.connect.api.domain.order.OrderStatus;
import com.example.connect.api.domain.orderitem.OrderItem;
import com.example.connect.api.domain.order.OrderRepository;
import com.example.connect.api.exception.NotEnoughStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @DisplayName("")
    @Test
    @Transactional
    void 상품주문() {
        // given
        Member member = createMember();
        Item item = createBook();
        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findById(orderId).get();

        List<OrderItem> orderItems = getOrder.getOrderItems();

        Item getItem = itemRepository.findById(item.getId()).get();

        assertThat(getOrder.getOrderStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(orderItems).hasSize(1);
        assertThat(getOrder.getTotalPrice()).isEqualTo(5000 * 2);
        assertThat(getItem.getStockQuantity()).isEqualTo(3 - 2);

    }

    @DisplayName("")
    @Test
    @Transactional
    void 상품주문_재고수량초과() {
        // given
        Member member = createMember();
        Item item = createBook();
        int orderCount = 4;

        // when & then
        Long memberId = member.getId();
        Long itemId = item.getId();

        // when & then
        assertThatExceptionOfType(NotEnoughStockException.class)
                .isThrownBy(() -> orderService.order(memberId, itemId, orderCount))
                .withMessage("need more stock");
    }

    @DisplayName("")
    @Test
    @Transactional
    void 상품주문취소() {
        // given
        Member member = createMember();
        Item item = createBook();
        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findById(orderId).get();
        Item getItem = itemRepository.findById(item.getId()).get();
        assertThat(getOrder.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(getItem.getStockQuantity()).isEqualTo(3);
    }

    private Member createMember() {
        Address address = Address.create("서울", "강가", "123-123");
        Member member = Member.create("01012341234", "이광수", address);
        return memberRepository.save(member);
    }

    private Book createBook() {
        Book book = Book.create("ORM", 5000, 3, "김영한", "?");
        return itemRepository.save(book);
    }

}
package com.example.connect.api.service.order;

import com.example.connect.api.domain.delivery.Delivery;
import com.example.connect.api.domain.item.Item;
import com.example.connect.api.domain.member.Member;
import com.example.connect.api.domain.member.MemberRepository;
import com.example.connect.api.domain.order.Order;
import com.example.connect.api.domain.order.OrderSearch;
import com.example.connect.api.domain.orderitem.OrderItem;
import com.example.connect.api.domain.order.OrderRepository;
import com.example.connect.api.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemService itemService;

    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        Item item = itemService.findOne(itemId);

        Delivery delivery = Delivery.create(member.getAddress());

        OrderItem orderItem = OrderItem.create(item, item.getPrice(), count);

        Order order = Order.createOrder(member, delivery, orderItem);

        Order createdOrder = orderRepository.create(order);
        return createdOrder.getId();
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        order.cancel();
    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        if (orderSearch.isOrderStatusSearch()) {
            return orderRepository.findByOrderStatus(orderSearch.getOrderStatus());
        }

        return orderRepository.findByMemberName(orderSearch.getMemberName());
    }

    public Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
    }
}

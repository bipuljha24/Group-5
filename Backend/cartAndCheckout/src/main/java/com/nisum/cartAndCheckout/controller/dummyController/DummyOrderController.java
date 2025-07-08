package com.nisum.cartAndCheckout.controller.dummyController;

import com.nisum.cartAndCheckout.dto.request.OrderRequestDTO;
import com.nisum.cartAndCheckout.dto.response.OrderResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class DummyOrderController {

    @PostMapping("/place")
    public OrderResponseDTO placeOrder(@RequestBody OrderRequestDTO orderRequest) {
        // Just return a dummy order ID
        return new OrderResponseDTO("ORD123456");
    }
}

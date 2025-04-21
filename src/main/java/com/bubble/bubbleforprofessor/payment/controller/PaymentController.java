package com.bubble.bubbleforprofessor.payment.controller;

import com.bubble.bubbleforprofessor.payment.dto.request.OrderRequestDto;
import com.bubble.bubbleforprofessor.payment.dto.response.InitPaymentResponseDto;
import com.bubble.bubbleforprofessor.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name="토스페이먼츠 API")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<InitPaymentResponseDto> initPayment(@RequestBody OrderRequestDto orderRequestDto) {
        InitPaymentResponseDto responseDto = paymentService.initPayment(orderRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/success")
    public ResponseEntity<String> success(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam int amount) {
        paymentService.completePayment(paymentKey, orderId, amount);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/fail")
    public ResponseEntity<String> fail(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam int amount) {
        paymentService.failPayment(paymentKey, orderId, amount);
        return ResponseEntity.ok("fail");
    }
}

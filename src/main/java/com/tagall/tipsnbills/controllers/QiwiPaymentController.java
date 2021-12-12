package com.tagall.tipsnbills.controllers;

import com.qiwi.billpayments.sdk.client.BillPaymentClient;
import com.qiwi.billpayments.sdk.client.BillPaymentClientFactory;
import com.qiwi.billpayments.sdk.model.MoneyAmount;
import com.qiwi.billpayments.sdk.model.in.CreateBillInfo;
import com.qiwi.billpayments.sdk.model.in.Customer;
import com.qiwi.billpayments.sdk.model.in.PaymentInfo;
import com.qiwi.billpayments.sdk.model.out.BillResponse;
import com.tagall.tipsnbills.exceptions.ResourceNotFoundException;
import com.tagall.tipsnbills.module.Organization;
import com.tagall.tipsnbills.module.requested.LeaveTipDto;
import com.tagall.tipsnbills.module.requested.RefreshTokenRequestDto;
import com.tagall.tipsnbills.services.OrganizationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.UUID;

@Controller
@CrossOrigin("*") // TODO : DELETE
@RequestMapping("/api/qiwi")
@Log4j2
public class QiwiPaymentController {
    @Autowired
    OrganizationService organizationService;

    private final BillPaymentClient client = BillPaymentClientFactory.createDefault("secretKey");

    @PostMapping("/payment")
    public ResponseEntity<?> paymentChai(@Valid @RequestBody LeaveTipDto leaveTipDto) {
        return ResponseEntity.ok(payUrl(leaveTipDto.getMoney()));
    }

    public String payUrl(Long money) {

        Organization organization = organizationService.findOrganizationByUsername(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Organization Not Found"));

        MoneyAmount amount = new MoneyAmount(
                BigDecimal.valueOf(money),
                Currency.getInstance("RUB")
        );

        String billId = UUID.randomUUID().toString();
        String paymentUrl = client.createPaymentForm(new PaymentInfo("48e7qUxn9T7RyYE1MVZswX1FRSbE6iyCj2gCRwwF3Dnh5XrasNTx3BGPiMsyXQFNKQhvukniQG8RTVhYm3iP6yRbniW1HhRUAZWfdGa6EWd2zz8sW3HrCDjrfyZX1rdN6N7mrCZwSrXLR79r37JHhH45uFBckEhiEUYbQwTyvJ63BVQFFeVVz39oySRLi", amount, billId, "https://tipsnbills.herokuapp.com"));
        return paymentUrl;

    }

    public void getStatus(String orderId) {
        String status = client.getBillInfo(orderId).getStatus().getValue().toString();
        // Когда будет готов OrderService здесь необходимо записать встатус "WAITING"(Ожидает оплаты)
        while (!status.contains("PAID")) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            status = client.getBillInfo(orderId).getStatus().getValue().toString();
        }
        // А здесь записать в статус "PAID"(Оплачено)
    }

    private String getCurrentUsername() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

}

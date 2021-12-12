package com.tagall.tipsnbills.controllers;

import com.qiwi.billpayments.sdk.client.BillPaymentClient;
import com.qiwi.billpayments.sdk.client.BillPaymentClientFactory;
import com.qiwi.billpayments.sdk.model.MoneyAmount;
import com.qiwi.billpayments.sdk.model.in.CreateBillInfo;
import com.qiwi.billpayments.sdk.model.in.Customer;
import com.qiwi.billpayments.sdk.model.out.BillResponse;
import com.tagall.tipsnbills.exceptions.ResourceNotFoundException;
import com.tagall.tipsnbills.module.Organization;
import com.tagall.tipsnbills.module.requested.LeaveTipDto;
import com.tagall.tipsnbills.module.requested.RefreshTokenRequestDto;
import com.tagall.tipsnbills.services.OrganizationService;
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

        CreateBillInfo billInfo = new CreateBillInfo(
                UUID.randomUUID().toString(),
                new MoneyAmount(
                        BigDecimal.valueOf(money),
                        Currency.getInstance("RUB")
                ),
                "Оплата чаевых",
                ZonedDateTime.now().plusDays(1),
                new Customer(
                        organization.getUsername(),
                        UUID.randomUUID().toString(),
                        organization.getPhoneNumber()
                ),
                "https://tipsnbills.herokuapp.com/");
        BillResponse billResponse = null;
        try {
            billResponse = client.createBill(billInfo);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (billResponse != null) {
            System.out.println(billResponse.getPayUrl());
            return billResponse.getPayUrl();
        } else {
            return null;
        }
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

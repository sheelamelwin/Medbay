package com.medbay.admin.email.service;

public interface AdminEmailService {

    void sendOrderStatusChangeEmail(String customerEmailAddress,  String orderId, String oldStatus, String newStatus);
}

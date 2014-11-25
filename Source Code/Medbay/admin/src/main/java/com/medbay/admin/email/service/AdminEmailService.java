package com.medbay.admin.email.service;

public interface AdminEmailService {

    void sendOrderStatusChangeEmail(String oldStatus, String newStatus);
}

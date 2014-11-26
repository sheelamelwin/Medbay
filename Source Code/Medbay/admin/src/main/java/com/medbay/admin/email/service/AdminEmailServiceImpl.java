package com.medbay.admin.email.service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.broadleafcommerce.common.email.service.EmailService;
import org.broadleafcommerce.common.email.service.info.EmailInfo;
import org.broadleafcommerce.openadmin.server.security.dao.AdminUserDao;
import org.broadleafcommerce.openadmin.server.security.domain.AdminUser;
import org.springframework.stereotype.Service;

@Service("mbAdminEmailService")
public class AdminEmailServiceImpl implements AdminEmailService {
	
	@Resource(name = "blEmailService")
    protected EmailService emailService;
	
	@Resource(name = "mbOrderStatusChangeEmailInfo")
    protected EmailInfo orderStatusChangeEmailInfo;
	
	@Resource(name = "blAdminUserDao")
    protected AdminUserDao adminUserDao;

	public EmailInfo getOrderStatusChangeEmailInfo() {
		return orderStatusChangeEmailInfo;
	}

	public void setOrderStatusChangeEmailInfo(EmailInfo orderStatusChangeEmailInfo) {
		this.orderStatusChangeEmailInfo = orderStatusChangeEmailInfo;
	}

	public void sendOrderStatusChangeEmail(String customerEmailAddress, String orderId, String oldStatus, String newStatus){
	    	HashMap<String, Object> vars = new HashMap<String, Object>();
	    	
	    	vars.put("orderId", orderId);
			vars.put("oldStatus", oldStatus);
			vars.put("newStatus", newStatus);
			
			List<AdminUser> adminUsers = adminUserDao.readAllAdminUsers();
			
			//sending email for admin users
			for(AdminUser adminUser:adminUsers){
				emailService.sendTemplateEmail(adminUser.getEmail(), getOrderStatusChangeEmailInfo(), vars);
			}
			
			//sending email for customer
			if (customerEmailAddress!=null){
				emailService.sendTemplateEmail(customerEmailAddress, getOrderStatusChangeEmailInfo(), vars);
			}
	}
}
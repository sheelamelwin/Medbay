package com.medbay.admin.email.service;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.email.service.EmailService;
import org.broadleafcommerce.common.email.service.info.EmailInfo;
import org.springframework.stereotype.Service;

@Service("mbAdminEmailService")
public class AdminEmailServiceImpl implements AdminEmailService {
	
	private static final Log LOG = LogFactory.getLog(AdminEmailServiceImpl.class);
	
	@Resource(name = "blEmailService")
    protected EmailService emailService;
	
	@Resource(name = "mbOrderStatusChangeEmailInfo")
    protected EmailInfo orderStatusChangeEmailInfo;

	public EmailInfo getOrderStatusChangeEmailInfo() {
		return orderStatusChangeEmailInfo;
	}

	public void setOrderStatusChangeEmailInfo(EmailInfo orderStatusChangeEmailInfo) {
		this.orderStatusChangeEmailInfo = orderStatusChangeEmailInfo;
	}

	public void sendOrderStatusChangeEmail(String oldStatus, String newStatus){
	    	HashMap<String, Object> vars = new HashMap<String, Object>();
	    	String name ="Sheela";
	    	String comments="Order Status Changed";
	    	String targetEmailAddress = "admin@medbay.com";
	        vars.put("name", name);
			vars.put("comments", comments);
			vars.put("oldStatus", oldStatus);
			vars.put("newStatus", newStatus);
			
			emailService.sendTemplateEmail(targetEmailAddress, getOrderStatusChangeEmailInfo(), vars);
	       
		
	}
}
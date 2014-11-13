package com.medbay.admin.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.common.exception.SecurityServiceException;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.openadmin.dto.BasicFieldMetadata;
import org.broadleafcommerce.openadmin.dto.ClassMetadata;
import org.broadleafcommerce.openadmin.dto.DynamicResultSet;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.dto.Property;
import org.broadleafcommerce.openadmin.dto.SectionCrumb;
import org.broadleafcommerce.openadmin.server.domain.PersistencePackageRequest;
import org.broadleafcommerce.openadmin.server.security.remote.EntityOperationType;
import org.broadleafcommerce.openadmin.web.controller.AdminAbstractController;
import org.broadleafcommerce.openadmin.web.form.component.ListGrid;
import org.broadleafcommerce.openadmin.web.form.entity.DefaultMainActions;
import org.broadleafcommerce.openadmin.web.form.entity.EntityForm;
import org.broadleafcommerce.openadmin.web.form.entity.EntityFormAction;
import org.broadleafcommerce.openadmin.web.form.entity.Field;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerAddress;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The controller responsible for viewing reports
 */
@Controller
@RequestMapping("/" + ReportController.SECTION_KEY)
public class ReportController extends AdminAbstractController {
    
	protected static final String SECTION_KEY = "order-report";
	
	protected static final String CLASS_NAME = "Order";
	
	@Resource(name = "blOrderService")
    OrderService orderService;
	

	/*
	@Override
	public String showViewCollectionItem(HttpServletRequest request,
			HttpServletResponse response, Model model,
			Map<String, String> pathVars, String id, String collectionField,
			String collectionItemId) throws Exception {
		
		System.out.println("#####--------------- ##########");
        String returnPath = super.showViewCollectionItem(request, response, model, pathVars, id, collectionField, collectionItemId);
        System.out.println("#####collectionField#### "+collectionField+" ##########");
        if ("orderItems".equals(collectionField)) {
            EntityForm ef = (EntityForm) model.asMap().get("entityForm");

            ListGrid adjustmentsGrid = ef.findListGrid("orderItemAdjustments");
            if (adjustmentsGrid != null && CollectionUtils.isEmpty(adjustmentsGrid.getRecords())) {
                ef.removeListGrid("orderItemAdjustments");
            }

            ListGrid priceDetailsGrid = ef.findListGrid("orderItemPriceDetails");
            if (priceDetailsGrid != null && CollectionUtils.isEmpty(priceDetailsGrid.getRecords())) {
                ef.removeListGrid("orderItemPriceDetails");
            }
        }
        System.out.println("#####Path#### "+returnPath+" ##########");
        
        return "reportTemplates/orderReport";
		
	}
    */
    
	/*
    @RequestMapping(method = RequestMethod.GET)
    public String viewOrderHistory(HttpServletRequest request, Model model) {
        return super.viewOrderHistory(request, model); 
    }

    @RequestMapping(value = "/{orderNumber}", method = RequestMethod.GET)
    public String viewOrderDetails(HttpServletRequest request, Model model, @PathVariable("orderNumber") String orderNumber) {
        return super.viewOrderDetails(request, model, orderNumber);
    }
    
    @Override
    public String getOrderHistoryView() {
    	// TODO Auto-generated method stub
    	return "reportTemplates/orderReport";
    }
	*/
	
	/**
     * Renders the order history details
     * criteria.
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param criteria a Map of property name -> list critiera values
     * @return the return view path
     * @throws Exception
     */
	@RequestMapping(value="",method=RequestMethod.GET)
     
    public String viewOrderReport(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable Map<String, String> pathVars,
            @RequestParam MultiValueMap<String, String> requestParams) throws Exception
    {
    	setModelAttributes(model, SECTION_KEY);    	
    	
        String sectionClassName = getClassNameForSection(CLASS_NAME);
        List<SectionCrumb> crumbs = getSectionCrumbs(request, null, null);
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(sectionClassName, requestParams, crumbs, pathVars);
        
        ClassMetadata cmd = service.getClassMetadata(ppr).getDynamicResultSet().getClassMetaData();
        DynamicResultSet drs =  service.getRecords(ppr).getDynamicResultSet();
        
        ListGrid listGrid = formService.buildMainListGrid(drs, cmd, SECTION_KEY, crumbs);
        List<EntityFormAction> mainActions = new ArrayList<EntityFormAction>();
        addAddActionIfAllowed(sectionClassName, cmd, mainActions);
        
        Field firstField = listGrid.getHeaderFields().iterator().next();
        if (requestParams.containsKey(firstField.getName())) {
            model.addAttribute("mainSearchTerm", requestParams.get(firstField.getName()).get(0));            
        }
        
        extensionManager.getProxy().addAdditionalMainActions(sectionClassName, mainActions);
        
        model.addAttribute("entityFriendlyName", cmd.getPolymorphicEntities().getFriendlyName());
        model.addAttribute("currentUrl", request.getRequestURL().toString());
        model.addAttribute("listGrid", listGrid);
        model.addAttribute("mainActions", mainActions);
        model.addAttribute("viewType", "entityList");

        setModelAttributes(model, SECTION_KEY);
       // return "modules/defaultContainer";
        System.out.println("---------before return--------");
        return "reportTemplates/orderReport";
    }
    

     
	/**
     * Renders the order detail for the specified id
     * 
     * @param request
     * @param model
     * @param id
     * @param pathVars
     * @return the return view path
     * @throws Exception
     */
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String viewOrderDetails(HttpServletRequest request, Model model, @PathVariable("id") String id,@PathVariable Map<String, String> pathVars) throws Exception{
		System.out.println("---------Inside view details-------pathVars----"+pathVars);
		System.out.println("---------Long Value--------"+Long.parseLong(id));
		
		setModelAttributes(model, SECTION_KEY);
		
		String sectionClassName = getClassNameForSection(CLASS_NAME);
		
		List<SectionCrumb> crumbs = getSectionCrumbs(request, SECTION_KEY, id);
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(sectionClassName, crumbs, pathVars);

        ClassMetadata cmd = service.getClassMetadata(ppr).getDynamicResultSet().getClassMetaData();
        Entity entity = service.getRecord(ppr, id, cmd, false).getDynamicResultSet().getRecords()[0];
        
        Map<String, DynamicResultSet> subRecordsMap = service.getRecordsForAllSubCollections(ppr, entity, crumbs);

        EntityForm entityForm = formService.createEntityForm(cmd, entity, subRecordsMap, crumbs);
                      
		
		Order order= orderService.findOrderById(Long.parseLong(id));
		CustomerAddress customerAddress = order.getCustomer().getCustomerAddresses().get(0);
	
		
		model.addAttribute("order",order);
		model.addAttribute("shipmentAddress",customerAddress);
		model.addAttribute("entity", entity);
        model.addAttribute("entityForm", entityForm);
        model.addAttribute("viewType", "entityView");
        model.addAttribute("currentUrl", request.getRequestURL().toString());
      
		
        return "reportTemplates/orderReportDetails";
    }
	
	/*
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String viewOrderReportById(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable("id") String id) throws Exception {
        //String sectionKey = getSectionKey(pathVars);
    	System.out.println("---------Inside view details--------");
        String className = "Order";
        String sectionClassName = getClassNameForSection(className);
        List<SectionCrumb> crumbs = getSectionCrumbs(request, SECTION_KEY, id);
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(sectionClassName, crumbs, pathVars);
    	System.out.println("---------@@@@@--------");
        ClassMetadata cmd = service.getClassMetadata(ppr).getDynamicResultSet().getClassMetaData();
        Entity entity = service.getRecord(ppr, id, cmd, false).getDynamicResultSet().getRecords()[0];
    	System.out.println("---------&&&--------");
        Map<String, DynamicResultSet> subRecordsMap = service.getRecordsForAllSubCollections(ppr, entity, crumbs);

        EntityForm entityForm = formService.createEntityForm(cmd, entity, subRecordsMap, crumbs);
        
        model.addAttribute("entity", entity);
        model.addAttribute("entityForm", entityForm);
        model.addAttribute("currentUrl", request.getRequestURL().toString());

        setModelAttributes(model, SECTION_KEY);
        
        if (sandBoxHelper.isSandBoxable(entityForm.getEntityType())) {
            Tab auditTab = new Tab();
            auditTab.setTitle("Audit");
            auditTab.setOrder(Integer.MAX_VALUE);
            auditTab.setTabClass("audit-tab");
            entityForm.getTabs().add(auditTab);
        }

        if (isAjaxRequest(request)) {
            entityForm.setReadOnly();
            model.addAttribute("viewType", "modal/entityView");
            model.addAttribute("modalHeaderType", "viewEntity");
            return "reportTemplates/orderReportDetails";
        } else {
            model.addAttribute("viewType", "entityEdit");
            return "reportTemplates/orderReport";
        }
    }

	  */
	
	
	
	 

	
	   
	/**
     * Adds the "Add" button to the main entity form if the current user has permissions to create new instances
     * of the entity and all of the fields in the entity aren't marked as read only.
     * 
     * @param sectionClassName
     * @param cmd
     * @param mainActions
     */
    protected void addAddActionIfAllowed(String sectionClassName, ClassMetadata cmd, List<EntityFormAction> mainActions) {
        if (isAddActionAllowed(sectionClassName, cmd)) {
            mainActions.add(DefaultMainActions.ADD);
        }
        
        mainEntityActionsExtensionManager.getProxy().modifyMainActions(cmd, mainActions);
    }
    
    protected boolean isAddActionAllowed(String sectionClassName, ClassMetadata cmd) {
        // If the user does not have create permissions, we will not add the "Add New" button
        boolean canCreate = true;
        try {
            adminRemoteSecurityService.securityCheck(sectionClassName, EntityOperationType.ADD);
        } catch (ServiceException e) {
            if (e instanceof SecurityServiceException) {
                canCreate = false;
            }
        }
        
        if (canCreate) {
            checkReadOnly: {
                //check if all the metadata is read only
                for (Property property : cmd.getProperties()) {
                    if (property.getMetadata() instanceof BasicFieldMetadata) {
                        if (((BasicFieldMetadata) property.getMetadata()).getReadOnly() == null ||
                                !((BasicFieldMetadata) property.getMetadata()).getReadOnly()) {
                            break checkReadOnly;
                        }
                    }
                }
                canCreate = false;
            }
        }
        
        return canCreate;
    }
	
	
   
    
	
}

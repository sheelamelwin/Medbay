UPDATE blc_admin_module SET name='Order Management' WHERE admin_module_id=-8;

UPDATE blc_admin_module SET module_key='BLCOrderManagement' WHERE admin_module_id=-8;

INSERT INTO `blc_admin_section` (`ADMIN_SECTION_ID`, `CEILING_ENTITY`, `DISPLAY_CONTROLLER`, `DISPLAY_ORDER`, `NAME`, `SECTION_KEY`, `URL`, `USE_DEFAULT_HANDLER`, `ADMIN_MODULE_ID`) VALUES (-19, 'org.broadleafcommerce.core.order.domain.Order', NULL, 2000, 'Update Status', 'UpdateStatus', '/update-order', NULL, -8);


INSERT INTO `blc_admin_sec_perm_xref` (`ADMIN_SECTION_ID`, `ADMIN_PERMISSION_ID`) VALUES (-19, -1);

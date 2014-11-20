$(document).ready(function() {

$("#btnPrint").click(function() {	
	var headStr = "<html><head><title>Order Report</title>"+$("head").html()+"</head><body>";
	var footStr = "</body></html>";
	var bodyStr = document.all.item('printableDiv').innerHTML;
	var html = headStr+bodyStr+footStr;
	var oldStr = document.body.innerHTML;
	var printWP = window.open('', 'Order Report',resizable=1,'height=400,width=600');
	printWP.document.open();
	printWP.document.write(html);
	printWP.document.close(); 
	printWP.print();
	printWP.close();

});
});
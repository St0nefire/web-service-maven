//function setPageButtons() {
//	if(pageCount <= 0)
//		return;
//	var totalPageShown = 5;
//	var pageLeft = pageCount - pageNo;
//	var backSlot = ( pageLeft + 1 ) >= totalPageShown ? 0 : totalPageShown - (pageLeft + 1);
//	var startPage = pageNo - backSlot ;
//	if(startPage < 1)
//		startPage = 1;
//	$("#pagenumber_area > button").each(function(index) {
//		this.innerHTML = "" + startPage ;
//		if(startPage > pageCount) {
//			this.style.visibility = "hidden";
//		}
//		else if(startPage == pageNo) {
//			this.style.backgroundColor = "blue";
//			this.style.color = "white";
//		}
//		else {
//			this.style.backgroundColor = "white";
//			this.style.color = "black";
//		}
//		startPage++;
//	})
//};

function setPageButtons() {
	if(totalPages <= 0)
		return;
	var totalPageShown = 5;
	var pageLeft = totalPages - pageNo;
	var backSlot = ( pageLeft + 1 ) >= totalPageShown ? 0 : totalPageShown - (pageLeft + 1);
	var startPage = pageNo - backSlot ;
	if(startPage < 1)
		startPage = 1;
	$("#pagenumber_area > button").each(function(index) {
		this.innerHTML = "" + startPage ;
		if(startPage > totalPages) {
			this.style.visibility = "hidden";
		}
		else if(startPage == pageNo) {
			this.style.backgroundColor = "blue";
			this.style.color = "white";
		}
		else {
			this.style.backgroundColor = "white";
			this.style.color = "black";
		}
		startPage++;
	})
};
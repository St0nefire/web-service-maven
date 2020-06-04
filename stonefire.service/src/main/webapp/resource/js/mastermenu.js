


function setPageButtons() {
	if(pageCount <= 0)
		return;
	var totalPageShown = 5;
	var pageLeft = pageCount - pageNo;
	var backSlot = ( pageLeft + 1 ) >= totalPageShown ? 0 : totalPageShown - (pageLeft + 1);
	var startPage = pageNo - backSlot ;
	if(startPage < 1)
		startPage = 1;
	$("#pagenumber_area > button").each(function(index) {
		this.innerHTML = "" + startPage ;
		if(startPage > pageCount) {
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

function setAddButton() {
	$("#button_add_master_menu")
		.click(function() {
			showDialog(null);
//			$("#addmastermenu").dialog({
//				width: "80%"
//			});
		})
}

function showDialog(_masterMenu) {
	if(_masterMenu) {
		masterMenu = _masterMenu;
		$("#input_menuname").val(_masterMenu.name);
		$("#input_menudetail").val(_masterMenu.konten);
		$("#input_menumemo").val(_masterMenu.memo);
		$("#button_submit").text("Edit");
		console.log("MID: " + _masterMenu.id);
	}
	else {
		masterMenu = null;
		$("#input_menuname").val(null);
		$("#input_menudetail").val(null);
		$("#input_menumemo").val(null);
		$("#button_submit").text("Create");
	}
	$("#addmastermenu").dialog({width: "80%" });
}

function setEditButtons() {
	$(".btnEdit")
		.click(function() {
			showDialog(listMM[$(this).data("index")]);
		})
}



setPageButtons();
setAddButton();
setEditButtons();



//
// import { Component, Input, Output, EventEmitter, ViewChild } from
// '@angular/core';
// import { MasterMenuService } from '../../services/mastermenuservice'; ;
// import { UtilService } from '../../services/utilservice';
// import { AddMenuDialog } from '../../dialog/addmenu';
// import { PagerPage } from '../pagerpage/pagerpage';
//
// @Component({
// selector: 'mastermenu',
// templateUrl: 'mastermenu.html',
// styleUrls: ['mastermenu.scss']
// })
// export class MasterMenu extends PagerPage {
//    
// constructor(private masterMenuService: MasterMenuService, protected
// utilService: UtilService) {
// super(utilService, 5);
// }
//    
// // rowNumber: number = 5;
// // arrOriginMasterMenu: any[];
// // arrAllMasterMenu: any[][] ;
// // listMasterMenu: any[];
// // currentPageNum: number = 1;
// // arrPageNum: any[] = [{value: 0},{value: 0},{value: 0},{value: 0},{value:
// 0}];
// // pageNum1: number = 0;
// // pageNum2: number = 0;
// // pageNum3: number = 0;
// // pageNum4: number = 0;
// // pageNum5: number = 0;
//    
// // @ViewChild("inputSearch") inputSearch: any;
//    
// isLoading: boolean = false;
//    
// @Output()
// addButtonClicked: EventEmitter<any> = new EventEmitter<any>();
//    
// @Output()
// editButtonClicked: EventEmitter<any> = new EventEmitter<any>();
//    
// public refreshData() {
// this.arrOriginObjects =
// this.utilService.cloneObject(this.masterMenuService.mmList);
// if (!this.arrOriginObjects)
// this.arrOriginObjects = [];
// this.makeObjectPages(this.arrOriginObjects);
// }
//    
// public getImageURL(image: string) {
// let imageURL = this.masterMenuService.getImageURL(image);
// return imageURL;
// }
//
// deleteMasterMenu(mm) {
// let obs = this.masterMenuService.deleteMasterMenu(mm.id);
// this.isLoading=true;
// setTimeout(() => {
// obs.subscribe(
// data => {
// if(data.errorMessage) {
// alert("ERROR RESPONSE: " + data.errorMessage);
// return;
// }
// this.removeFromObjects(mm);
// },
// error => {
// alert("ERROR: " + error);
// },
// () => {
// this.isLoading = false;
// }
// )}, 1500);
// }
//    
// toogleActiveStatus(mm) {
// let obs = this.masterMenuService.editActiveStatus(mm.id, !mm.active);
// this.isLoading = true;
// setTimeout( () => {
// obs.subscribe(
// data => {
// if(data.errorMessage) {
// alert("ERROR RESPONSE: " + data.errorMessage);
// return;
// }
// mm.active = !mm.active;
// },
// error => {
// alert("ERROR: " + error);
// },
// () => {
// this.isLoading = false;
// }
// )}, 1500);
// }
//    
//    
//    
//    
// getActiveStyleForIndex(i: number) {
// if (i >= this.listObjects.length)
// return;
//
// if (this.listObjects[i].active)
// return {"background-color": "green"};
// else
// return {"background-color": "pink"};
// }
// }

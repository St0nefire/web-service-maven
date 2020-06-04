var listItem = [];

function setOnOptionChange() {
	console.log("On option change called");
	$("#select_mm")
		.change(function() {
			console.log("change called");
			if($.inArray($("#select_mm").val(), listItem) > -1)
				return;
			
			addItem($("#select_mm").val());
//			$("#button_mm").clone()
//				.find("span")
//				.text($("#select_mm").val())
//				.end()
//				.click(function() {
//					$("#list_item").get(0).removeChild(this);
//					var mmName = $("span", $(this)).text();
//					var index = $.inArray(mmName, listItem);
//					if (index>=0) listItem.splice(index, 1);
//				})
//				.css("display", "initial")
//				.appendTo($("#list_item"));
//			
//			listItem.push($("#select_mm").val());
		})
}

function addItem(data) {
	var dataArray = [];
	console.log("Is Array: " + (data instanceof Array ? "true" : "false"));
	console.log("Is data true: " + (data ? "true" : "false"));
	
	if(data) {
		if(data instanceof Array) {
			if(data.length > 0) {
				for(i=0; i < data.length ; i++) {
					dataArray.push(data[i]);
				}
			}
		}
		else {
			dataArray.push(data);
		}
	}
	else {
		$("#list_item").empty();
		listItem = [];
		return;
	}

	
	for(i=0; i < dataArray.length; i++) {
		console.log("Index: " + i);
		$("#button_mm").clone()
		.find("span")
		.text(dataArray[i])
		.end()
		.click(function() {
			$("#list_item").get(0).removeChild(this);
			var mmName = $("span", $(this)).text();
			var index = $.inArray(mmName, listItem);
			if (index>=0) 
				listItem.splice(index, 1);
		})
		.css("display", "initial")
		.appendTo($("#list_item"));
	
		listItem.push(dataArray[i]);
	}
}

setOnOptionChange();


//
//import { Component, Input, Output, EventEmitter, ViewChild  } from '@angular/core'; 
//import { MasterMenuService  } from '../services/mastermenuservice'; ;
//import { UtilService  } from '../services/utilservice'; 
//import { TextListComponent  } from '../components/textlist/textlist'; 
//
//@Component({
//  selector: 'addmenu',
//  templateUrl: 'addmenu.html',
//  styleUrls: ['addmenu.scss']
// })
//export class AddMenuDialog {
//    
//    constructor(public mmService: MasterMenuService, private utilService: UtilService) {}
//    
//    _show: boolean = false;
//    visible: boolean = false;
//    _menus: any[] = [];
//    
//    @ViewChild("addMenu") addMenu;
//    @ViewChild("textList") textList: TextListComponent;
//    
//    @Input()
//    date: number;
//    
//    @Input()
//    set menus(value: any[]) {
////        if(value)
////            this._menus = this.utilService.cloneObject(value);
////        else
////            this._menus = [];
//        if(value)
//            this._menus = value;
//        else
//            this._menus = [];
//    }
//    
//    @Input()
//    set show(value: boolean) {
//        if (value) {
//            this.utilService.showInDialog(this.addMenu.nativeElement);
//        }
//        else {
//            this.utilService.stopDialog();
//        }
//
//        this._show = value;
//        this.visible = value;
//    }
//    
//    @Output()
//    okButtonClicked: EventEmitter<any> = new EventEmitter();
//    
//    @Output()
//    cancelButtonClicked: EventEmitter<any> = new EventEmitter();
//                
//    getMenus() {
//        return this._menus;
//    }
//    
//    
//    signalOkButtonClicked() {
//        this.utilService.stopDialog();
//        this.okButtonClicked.emit(this.getMenus());
//        this._menus = [];
//    }
//    
//    signalCancelButtonClicked() {
//        this.utilService.stopDialog();
//        this.cancelButtonClicked.emit();
//    }
//    
//    addObject(obj) {
//        let addedObj = null;
//        for (var i = 0; i < this.mmService.mmList.length; i++) {
//            if (this.mmService.mmList[i].id == obj)
//                addedObj = this.utilService.cloneObject(this.mmService.mmList[i]);
//        }
//        
//        if (!addedObj)
//            return;
//        
//        for(var i = 0 ; i < this._menus.length; i++) {
//            if(this._menus[i].id == obj)
//                return;
//        }
//        this._menus.push(addedObj);
////        this.utilService.printObject("OBJECTS: ", this._menus);
//    }    
//     
//     setShow(value: boolean) {
//        if (value) {
//            this.utilService.showInDialog(this.addMenu.nativeElement);
//        }
//        else {
//            this.utilService.stopDialog();
//        }
//        this._show = value;
//        this.visible = value;
//    }
//    
//    getStyle() {
//        if (this._show)
//            return {display: "flex"};
//        else
//            return {display: "none"};
//    }
//}  
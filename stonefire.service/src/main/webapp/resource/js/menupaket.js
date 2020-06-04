function setAddButton() {
	$("#button_add_menu_paket")
		.click(function() {
			setData(null);
		})
}


function setEditButtons() {
	$(".btn-edit")
		.click(function() {
			setData(listScheduledPackage[$(this).data("index")]);
		})
}

setPageButtons();

//
//import { Component, Input, Output, EventEmitter, ViewChild  } from '@angular/core'; 
//import { ScheduledPackageService  } from '../../services/scheduledpackageservice'; 
//import { CredService  } from '../../services/credservice'; 
//import { UtilService  } from '../../services/utilservice'; 
//import { PagerPage  } from '../pagerpage/pagerpage'; 
//
//
//@Component({
//  selector: 'menupaket',
//  templateUrl: 'menupaket.html',
//  styleUrls: ['menupaket.scss']
// })
//export class MenuPaket extends PagerPage{
//    
//    constructor(private spService: ScheduledPackageService, protected utilService: UtilService, private credService: CredService) {
//        super(utilService, 3);
//    }
//    isLoading: boolean = false;
//    
//    @Output()
//    addButtonClicked: EventEmitter<any> = new EventEmitter<any>();
//    
//    @Output()
//    editButtonClicked: EventEmitter<any> = new EventEmitter<any>();
//                
//    public refreshData() {
//        this.arrOriginObjects = this.utilService.cloneObject(this.spService.spList);
//        if (!this.arrOriginObjects)
//            this.arrOriginObjects = [];
//        this.makeObjectPages(this.arrOriginObjects);
//    }
//    
//    public getImageURL(image: string) {
//        let imageURL = this.credService.getImageURL(image);
//        return imageURL;
//    }
//        
//    deleteMenuPaket(sp) {
//        let obs = this.spService.deleteScheduledPackage(sp.id);
//        this.isLoading = true;
//        setTimeout(() => {
//            obs.subscribe(
//                data => {
//                    if(data.errorMessage) {
//                        alert("ERROR RESPONSE: " + data.errorMessage);
//                        return;
//                    }
//                    this.removeFromObjects(sp);
//                },
//                error => {
//                    alert("ERROR: " + error);
//                },
//                () => {
//                    this.isLoading = false; 
//                }
//           )
//        }, 1500);
//    }
// }
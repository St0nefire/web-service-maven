var scheduledPackage;

function fileChange(event) {

	let fileList = event.target.files;
	if (fileList.length > 0) {
		image = fileList[0];
		// let file = fileList[0];
		// this.image = file;
		// this.fileName = file.name;
		// this.masterMenu.image = file.name;
		readUrl(event);
	}
}

function readUrl(event) {
	if (event.target.files && event.target.files[0]) {
		var reader = new FileReader();
		reader.onload = function(event) {
			imageData = event.target.result;
			console.log("BASE 64: " + imageData);
			$("#image_preview").attr('src', imageData);
		}
		reader.readAsDataURL(event.target.files[0]);
	}
}

function setData(_sp) {
	if(sp) {
		scheduledPackage = _sp;
		$("#input_menuname").val(_sp.name);
		$("#input_menudetail").val(_sp.konten);
		$("#input_menumemo").val(_sp.memo);
		$("#input_harga").val(_sp.harga);
		$("#input_minorder").val(_sp.minOrder);
		
		setTableData(_sp.menus);

		$("#button_submit").text("Edit");
		console.log("SID: " + _sp.id);
	}
	else {
		scheduledPackage = null;
		$("#input_menuname").val(null);
		$("#input_menudetail").val(null);
		$("#input_menumemo").val(null);
		$("#input_harga").val(null);
		$("#input_minorder").val(null);
		
		setTableData(null);
		
		$("#button_submit").text("Create");
	}
}

function onSubmit() {
	
	function successHandler (data, textStatus, jqXHR) {
		console.log("RESPONSE JSON: " + JSON.stringify(data, undefined, 3));
		var message ;
		if(!data.errorMessage) {
			if(scheduledPackage)
				message = "Scheduled package successfully edited";
			else
				message = "Scheduled package successfully added";
			
			$.alert(message, "Info");
			$("#addmenupaket").dialog("close");
		}	
		else {
			var message ;
			if(masterMenu)
				message = "Error in editing product <br/>";
			else
				message = "Error in adding product <br/>";
			
//			message + data.errorMessage;
			$.alert(message + data.errorMessage, "Error");
		}
	};
	
	var menus = "";
	for(i=0 ; i < tableData.length ; i++) {
		if(tableData[i]) {
			for(c=0 ; c < tableData[i].length; c++) {
				menus += tableData[i][c] + "_" + (i + 1) + ";";
			}
		}
	}
	menus = menus.slice(0, -1);
	
	var sp = {
		name: $("#input_menuname").val(), 
		konten:	 $("#input_menudetail").val(),
		memo: $("#input_menumemo").val(),
		harga: $("#input_harga").val(),
		minOrder: $("#input_minorder").val()
	}
	
	if(menus.length > 0)
		sp.menus = menus;
	
	if(scheduledPackage)
		sp.id = scheduledPackage.id;
	
	var jForm = new FormData();
	jForm.append("name", $("#input_menuname").val());
	jForm.append("konten", $("#input_menudetail").val());
	jForm.append("harga", $("#input_harga").val());

	if ($("#input_menumemo").val())
		jForm.append("memo", $("#input_menumemo").val());
	
	if ($("#input_minorder").val())
		jForm.append("minOrder", $("#input_minorder").val());

	if (image) 
		jForm.append("uploadFile", image);
	
	if(menus.length > 0)
		jForm.append("menus", menus);
	
	if(scheduledPackage)
		jForm.append("id", id);
		
	
	
	function failureHandler (xhr, textStatus, errorThrown) {
		var message ;
		if(masterMenu)
			message = "Error in editing product <br/>";
		else
			message = "Error in adding product <br/>";
		$.alert(message + errorThrown, "Error");
		$("#addmenupaket").dialog("close");
	};
	
	if(scheduledPackage) {
		$.ajax({
			url : postURL,
			type : "PUT",
			data: JSON.stringify(sp, undefined,4),
		 	contentType: "application/json",
			dataType : "json",
			cache : false,
			processData: false,
			success : successHandler,
			error : failureHandler
		});
	}
	else {
		$.ajax({
			url : postURL,
			type : "POST",
			data : jForm,
			mimeType : "multipart/form-data",
			dataType : "json",
			contentType : false,
			cache : false,
			processData : false,
			success : successHandler,
			error : failureHandler
		});
	}
}

$("#chooseFile").change(fileChange);
$("#button_submit").click(onSubmit);


//
//import { Component, Input, Output, EventEmitter, ViewChild  } from '@angular/core'; ;
//import { ScheduledPackageService  } from '../../services/scheduledpackageservice';
//import { CredService  } from '../../services/credservice';
//import { TagService  } from '../../services/tagservice';
//import { UtilService  } from '../../services/utilservice';
//import { AutoCompleteComponent  } from '../../components/autocomplete/autocomplete';
//
//@Component({
//  selector: 'addmenupaket',
//  templateUrl: 'addmenupaket.html',
//  styleUrls: ['addmenupaket.scss']
// })
//export class AddMenuPaket {
//    
//    constructor(private scheduledPackageService: ScheduledPackageService, private utilService: UtilService, private credService: CredService, public tagService: TagService) {
//    }
//    
//    fileName: string = "";
//    fileUrl: string = "";
//    image: File = null;
//    mode = 0;
//    currentView = 'form';
//    scheduledPackage: any = {name: "", konten: "", memo: "", price: null, minOrder: null, menus: null};
//    formData:FormData ;
//    tags: any[] = [{id: 0, tagName: "RETRIEVING DATA, PLEASE WAIT"}]
//    isLoading: boolean = false;
//    
//    @ViewChild("inputImage") inputImage: any;
//    @ViewChild("autoComplete") autoComplete: AutoCompleteComponent;
//    
//    @Output()
//    cancelButtonClicked: EventEmitter<any> = new EventEmitter<any>();
//    
//    @Output()
//    okButtonClicked: EventEmitter<any> = new EventEmitter<any>();
//    
//    public setScheduledPackage(sp) {
////        this.utilService.printObject("SP : ", sp);
//        if(sp) {
//            this.scheduledPackage = {id: sp.id, name: sp.name, konten: sp.konten, memo: sp.memo, price: sp.price, minOrder: sp.minOrder, 
//                tags: sp.tags, image: sp.image, menus: sp.menus}
//            this.fileUrl = this.credService.getImageURL(sp.image);
//            this.fileName = sp.image;
//            this.image = null;
//        }
//        else {
//            this.scheduledPackage = {name: "", konten: "", memo: "", price: null, minOrder: null, tags: null, menus: null};
//            this.fileUrl = null;
//            this.fileName = null;
//            this.image = null;
//        }
//    }  
//            
//    goBack() {
//        this.scheduledPackage.tags = null;
//        this.cancelButtonClicked.emit();
//    }
//
//    
//    fileChange(event) {
//        let fileList: FileList = event.target.files;
//        if(fileList.length > 0) {
//            let file: File = fileList[0];
//            this.image = file;
//            this.fileName = file.name;
//            this.scheduledPackage.image = file.name;
//            this.readUrl(event);
//        }
//    }  
//    
//    readUrl(event) {
//        if (event.target.files && event.target.files[0]) {
//            var reader = new FileReader();
//            reader.onload = (event:any) => {
//                this.fileUrl = event.target.result;
//            }
//            reader.readAsDataURL(event.target.files[0]);
//        }
//    } 
//    
//    isFormValid() {
//        return this.scheduledPackage.name && this.scheduledPackage.konten && this.scheduledPackage.price && this.scheduledPackage.minOrder && 
//            this.scheduledPackage.image;
//    }
//    
//    submit() {
//        let obs ;
//        let tags = this.autoComplete.getTags();
//        this.isLoading = true;
//        if(this.scheduledPackage.id) {
//            obs = this.scheduledPackageService.editScheduledPackage(this.scheduledPackage.id, this.scheduledPackage.name, this.scheduledPackage.konten, 
//                this.scheduledPackage.memo, this.scheduledPackage.price, this.scheduledPackage.minOrder, this.scheduledPackage.available, tags, 
//                this.scheduledPackage.menus, this.image);
//        }
//        else
//            obs = this.scheduledPackageService.addSecheduledPackage(this.scheduledPackage.name, this.scheduledPackage.konten, 
//                this.scheduledPackage.memo, this.scheduledPackage.price, this.scheduledPackage.minOrder, tags, this.scheduledPackage.menus, this.image);
//        
//        setTimeout(() => {
//            obs.subscribe(
//                data => {
//                    if(data.errorMessage) {
//                        alert("ERROR RESPONSE: " + data.errorMessage);
//                        return;
//                    }
//                    this.okButtonClicked.emit(data);
//                },
//                error => {
//                    alert("ERROR: " + error);
//                },
//                () => {
//                    this.isLoading = false;
//                }
//            )
//        }, 1500);
//    }
//    
//    setMenuHarian(obj) {
//        this.currentView = "form";
//        this.scheduledPackage.menus = obj;
//    }
//    
//    toMenuHarian() {
//        this.currentView = "menuharian";
//    }      
//}
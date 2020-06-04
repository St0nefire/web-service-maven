var image;
var imageData;
var masterMenu ;

$.extend({
	alert : function(message, title) {
		$("<div></div>").dialog({
			buttons : {
				"Ok" : function() {
					$(this).dialog("close");
				}
			},
			close : function(event, ui) {
				$(this).remove();
			},
			resizable : false,
			title : title,
			modal : true
		}).html(message);
	}
});

$.extend({
    confirm: function(message, title, okAction) {
        $("").dialog({
            // Remove the closing 'X' from the dialog
            open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }, 
            buttons: {
                "Ok": function() {
                    $(this).dialog("close");
                    okAction();
                },
                "Cancel": function() {
                    $(this).dialog("close");
                }
            },
            close: function(event, ui) { $(this).remove(); },
            resizable: false,
            title: title,
            modal: true
        }).html(message);
    }
});

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

function onSubmit() {
	
	function successHandler (data, textStatus, jqXHR) {
		console.log("RESPONSE JSON: " + JSON.stringify(data, undefined, 3));
		var message ;
		if(!data.errorMessage) {
			console.log("MASTER MENU: " + JSON.stringify(masterMenu, undefined, 4));
			if(masterMenu)
				message = "Product successfully edited";
			else
				message = "Product successfully added";
			
			$.alert(message, "Info");
			$("#addmastermenu").dialog("close");
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
	
	function failureHandler (xhr, textStatus, errorThrown) {
		var message ;
		if(masterMenu)
			message = "Error in editing product <br/>";
		else
			message = "Error in adding product <br/>";
		$.alert(message + errorThrown, "Error");
		$("#addmastermenu").dialog("close");
	};
	
	
	var jForm = new FormData();
	jForm.append("sessionString", sessionString);
	jForm.append("name", $("#input_menuname").val());
	jForm.append("konten", $("#input_menudetail").val());

	if ($("#input_menumemo").val())
		jForm.append("memo", $("#input_menumemo").val());

	if (image) 
		jForm.append("uploadFile", image);

	console.log("NAME: " + $("#input_menuname").val());
	console.log("Obj: " + jForm);

	if(masterMenu) {
		var data = {	id: masterMenu.id,
			 	name: $("#input_menuname").val(), 
			 	konten:	 $("#input_menudetail").val(),
			 	memo: $("#input_menumemo").val(), 
			 	image: image.name,
			 	uploadFile: imageData.substring(imageData.indexOf(",") + 1),
			 	sessionString: sessionString};
		$.ajax({
			url : postURL,
			type : "PUT",
			data: JSON.stringify(data, undefined,4),
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

function onTestAjax() {
	
	function successHandler (data, textStatus, jqXHR) {
		console.log("RESPONSE JSON: " + JSON.stringify(data, undefined, 3));
		var message ;
		if(!data.errorMessage) {
			console.log("MASTER MENU: " + JSON.stringify(masterMenu, undefined, 4));
			if(masterMenu)
				message = "Product successfully edited";
			else
				message = "Product successfully added";
			
			$.alert(message, "Info");
			$("#addmastermenu").dialog("close");
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
	
	function failureHandler (xhr, textStatus, errorThrown) {
		var message ;
		if(masterMenu)
			message = "Error in editing product <br/>";
		else
			message = "Error in adding product <br/>";
		$.alert(message + errorThrown, "Error");
		$("#addmastermenu").dialog("close");
	};
	
	$.ajax({
		url : "http://localhost:8080/pos/resources/products",
		type : "POST",
		data: JSON.stringify({name:"name"}, undefined,4),
	 	contentType: "application/json",
		dataType : "json",
		cache : false,
		processData: false,
		success : successHandler,
		error : failureHandler
	});
	
}

$("#chooseFile").change(fileChange);
$("#button_submit").click(onSubmit);
//$("#button_submit").click(onTestAjax);

//
// import { Component, Input, Output, EventEmitter, ViewChild } from
// '@angular/core'; ;
// import { MasterMenuService } from '../../services/mastermenuservice';
// import { CredService } from '../../services/credservice';
//
// @Component({
// selector: 'addmastermenu',
// templateUrl: 'addmastermenu.html',
// styleUrls: ['addmastermenu.scss']
// })
// export class AddMasterMenu {
//    
// constructor(private masterMenuService: MasterMenuService) {}
//    
// fileName: string = "";
// fileUrl: string = "";
// image: File = null;
// mode = 0;
// masterMenu: any = {"name": "", "konten": "", "memo": ""};
// formData:FormData ;
// isLoading: boolean = false;
//    
// // @ViewChild("inputFakeFile") inputFakeFile: any;
// @ViewChild("inputImage") inputImage: any;
//    
// @Output()
// cancelButtonClicked: EventEmitter<any> = new EventEmitter<any>();
//    
// @Output()
// okButtonClicked: EventEmitter<any> = new EventEmitter<any>();
//    
// public setMasterMenu(mm) {
// if(mm) {
// this.masterMenu = mm;
// this.fileUrl = this.masterMenuService.getImageURL(mm.image);
// this.fileName = mm.image;
// this.image = null;
// }
// else {
// this.masterMenu = {"name": "", "konten": "", "memo": ""};
// this.fileUrl = null;
// this.fileName = null;
// this.image = null;
// }
// }
//    
// fileChange(event) {
// let fileList: FileList = event.target.files;
// if(fileList.length > 0) {
// let file: File = fileList[0];
// this.image = file;
// this.fileName = file.name;
// this.masterMenu.image = file.name;
// this.readUrl(event);
// }
// }
//    
// readUrl(event) {
// if (event.target.files && event.target.files[0]) {
// var reader = new FileReader();
// reader.onload = (event:any) => {
// this.fileUrl = event.target.result;
// }
// reader.readAsDataURL(event.target.files[0]);
// }
// }
//    
// isFormValid() {
// return this.masterMenu.name && this.masterMenu.konten &&
// this.masterMenu.image;
// }
//    
// submit() {
// let obs ;
//        
// if(this.masterMenu.id) {
// obs = this.masterMenuService.editMasterMenu(this.masterMenu.id,
// this.masterMenu.name, this.masterMenu.konten, this.masterMenu.memo,
// this.image);
// }
// else
// obs = this.masterMenuService.addMasterMenu(this.masterMenu.name,
// this.masterMenu.konten, this.masterMenu.memo, this.image);
//        
// this.isLoading = true;
//        
// setTimeout(() => {
// obs.subscribe(
// data => {
// if(data.errorMessage) {
// alert("ERROR RESPONSE: " + data.errorMessage);
// return;
// }
//
// this.okButtonClicked.emit(data);
// },
// error => {
// alert("ERROR: " + error);
// },
// () => {
// this.isLoading = false;
//                }
//        )}, 1500);
//    }    
//}
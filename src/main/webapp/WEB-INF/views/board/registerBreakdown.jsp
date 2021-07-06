<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="../includes/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자전거 고장신고 접수</title>
	<!-- Bootstrap 4 -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
	<!-- css -->
	<link rel="stylesheet" href="/resources/css/registerBreakdown.css" type="text/css">
</head>
<body>
	 <!-- div class="row">
         <div class="col-lg-12">
             <h1 class="page-header">고장현황 등록</h1>
         </div>
         <!-- /.col-lg-12 -->
     </div -->
     <!-- /.row -->
     <div class="row">
         <div class="col-lg-12">
             <div class="panel panel-default">
                 <div class="panel-heading">
                     	고장신고
                     	<button type="reset" class="btnReset">초기화</button>
                     	<button type="list" class="btnList">목록</button>
                 </div>
                 <!-- /.panel-heading -->
                 <div class="panel-body">
                     <form id="postForm" action="/board/registerBreakdown" method="post">
                     	<div class="form-group">
                             <label>제목</label>
                             <input class="form-control" name="btitle">
                         </div>
                         <div class="form-group">
                             <label>자전거 정류장</label>
                             <input class="form-control" name="stationid">
                         </div>
                         <div class="form-group">
                             <label>자전거 번호</label>
                             <input class="form-control" name="bikenum">
                         </div>
                         <div class="form-group">
                             <label>고장부품</label>
                             <input class="form-control" name="brokenparts">
                         </div>
                         <div class="form-group">
                             <label>작성자</label>
                             <input class="form-control" name="writer">
                         </div>
                         <div class="form-group">
                             <label>내용</label>
                             <textarea class="form-control" rows="5" cols="50" name="content"></textarea>
                         </div>
                     	<button type="register" class="btnSubmit">생성</button>
                     	<button type="delete" class="btnCancel">취소</button>
                     </form>
                 </div>
                 <!-- /.panel-body -->
                 <div class="bigPictureWrapper"><!-- Image Expand -->
					<div class="bigPicture">
					</div>
				 </div><!-- End Image Expand -->
                 <div class="row">
                 	<div class="col-lg-12">
                 		<div class="panel panel-default">
                 			<div class="panel-heading">File Attach<button type="button" data-toggle="modal" data-target="#myModal" id="btnModal">전체 다운로드</button></div>
                 			<div class="panel-body">
                 				<div class="form-gorup" id="uploadDiv">
                 					<input type="file" name="uploadFile" multiple>
                 				</div>
                 				<div class="uploadResult">
                 					<ul>
                 					</ul>
                 				</div>
                 			</div><!-- End panel-body -->
                 		</div><!-- End panel-body -->
                 	</div><!-- End panel-body -->
                 </div><!-- End Row -->
             </div><!-- /.panel -->
             <!-- Modal 추가  -->
			<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="z-index:1050; visibility:visible;">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<h4 class="modal-title" id="myModalLabel">Modal Screen</h4>
						</div>
						<div class="modal-body">첨부파일을 전부 다운 받으시려면 Download를 눌러주세요</div>
						<div class="modal-footer">
							<button type="button" class="btn btnDownload">Download</button>
							<!-- button type="button" class="btn btnOriginal">Original</button -->
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						</div>
					</div><!-- End modal-content -->
				</div><!-- End modal-dialog -->
			</div><!-- End Modal -->
         </div>
         <!-- /.col-lg-12 -->
     </div>
     <!--  /.row -->
<!-- JQuery --> 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript">

function showImage(fileCallPath){
	
	if(document.querySelector(".bigPictureWrapper").style.display=='none'){
		document.querySelector(".bigPictureWrapper").style.display='block';
	}
	
	$(".bigPicture")
	.html("<img src='/display?fileName="+encodeURI(fileCallPath)+"'>")
	.animate({width:'30%',height:'30%',margin:'auto'},1000);
	
	$(".bigPictureWrapper").on("click",function(e){
		$(".bigPicture").animate({width:'0%',height:'0%'},1000);
		setTimeout(function(){
			document.querySelector(".bigPictureWrapper").style.display='none';
		},1000);
	})
	
}

$(document).ready(function(){	
	
	function validateInputValue(){
		
		var mandatoryItem  = {
				stationid : "자전거 정류장",
				bikenum : "자전거 번호",
				writer : "작성자"
		}
		
		var conditionItem  = {
				btitle : "제목",
				brokenparts : "고장부품",
				content : "내용"
		}
		
		var returnItem = new Array();
		var mBox = new Array();
		var cBox = new Array();
		for(item in mandatoryItem){
			if($("input[name="+item+"]").val()==""){
				mBox.push(mandatoryItem[item]);
			}
		}
		
		returnItem.push(mBox);
		
		if($("input[name=btitle]").val()=="" && $("input[name=brokenparts]").val()=="" && $("textarea[name=content]").val()==""){
			for(item in conditionItem){
				cBox.push(conditionItem[item]);
			}
		}
		
		returnItem.push(cBox);
		
		if(returnItem[0].length==0 && returnItem[1].length==0){
			return 'pass';
		}else{
			return returnItem;
		}
	}
	
	// 리스트로 돌아가기
	$(".btnList").on("click",function(e){
		e.preventDefault();
		
		$.ajax({
			url:'/board/breakdownList',
			type:'GET',
			error : function(){
				alert("통신 실패");
			},
			success : function(result){
				window.location.href = '/board/breakdownList';
			}
		});
		
	});
	
	// 입력데이터 초기화
	$(".btnReset").on("click",function(){
		
		if(confirm("초기화 하시겠습니까?")){
			$("input[name=btitle]").val("")
			$("input[name=stationid]").val("")
			$("input[name=bikenum]").val("")
			$("input[name=brokenparts]").val("")
			$("input[name=writer]").val("")
			$("textarea[name=content]").val("")	
		}
		
	});
	
	
	var formObj = $("#postForm");
	
	$(".btnSubmit").on("click",function(e){
		e.preventDefault();
		
		var resultValid = validateInputValue();
		
		if(resultValid!='pass'){
			var message = "아래의 조건을 입력해야합니다.";
			if(resultValid[0].length>0){
				message += "\n"+"필수 입력 값 : "+resultValid[0];
			}
			if(resultValid[1].length>0){
				message += "\n"+"하나는 필수 : "+resultValid[1];
			}
			alert(message);
		}else{
			var str = "";
			
			$(".uploadResult ul li").each(function(i,obj){
				var jobj = $(obj);
				console.dir(jobj);
				str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+jobj.data("type")+"'>";
			});
			
			formObj.append(str);
			
			formObj.attr("action","/board/registerBreakdown").submit();	
		}
		
	});
	
	function enterEvent(){
		if(window.event.keyCode == 13){
			console.log("엔터 눌렀니?");
		}
	}
	
	var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
	var maxSize = 5242880;
	
	function checkExtension(fileName,fileSize){
		
		if(fileSize>=maxSize){
			alert("파일 사이즈 초과");
			return false;
		}
		
		if(regex.test(fileName)){
			alert("해당 종류의 파일은 업로드 할 수 없습니다.");
			return false;
		}
		return true;
	}
	
	$("input[name='uploadFile']").change(function(e){
		
		var formData = new FormData();
		var inputFile = $("input[name='uploadFile']")
		var files = inputFile[0].files;
		for(var i=0;i<files.length;i++){
			if(!checkExtension(files[i].name,files[i].size)){
				return false;
			}
			formData.append("uploadFile",files[i]);
		}
		
		$.ajax({
			url : '/uploadAjaxAction',
			processData : false,
			contentType : false,
			data : formData,
			type : 'POST',
			dataType : 'json',
			success : function(result){
				console.log(result);
				showUploadFile(result);
			}
			 
		}); //End ajax

	});
	
	var uploadResult = $(".uploadResult ul");
	function showUploadFile(uploadResultArr){
		var str = "";
		$(uploadResultArr).each(function(i,obj){
			
			if(!obj.image){
				
				var fileCallPath = encodeURIComponent(obj.uploadPath+"/"+obj.uuid+"_"+obj.fileName);
				
				var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
				
				str +="<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'><div><a href='/downloadFile?fileName="+fileCallPath+"'>"+"<img src='/resources/img/attach.png'>"
					+ obj.fileName+"</a>"+"<button type='button' class='btn btn-warning btn-circle'><i class='fa fa-times'>X</i></button>"+"</div></li>";
			}else{
				
				var fileCallPath = encodeURIComponent(obj.uploadPath+"/s_"+obj.uuid+"_"+obj.fileName);
				
				var originPath = obj.uploadPath+"\\"+obj.uuid+"_"+obj.fileName;
				
				originPath = originPath.replace(new RegExp(/\\/g),"/");
				
				str += "<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'><a href=\"javascript:showImage(\'"+originPath+"\')\"><img src='/display?fileName="+fileCallPath+"'></a><a href='/downloadFile?fileName="+originPath+"'>"+obj.fileName+"</a>"+"<button type='button' data-file=\'"+fileCallPath+"\' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'>X</i></button>"+"</li>";
			}
			
		});
		uploadResult.append(str);
	}
	
	$(".uploadResult").on("click","button",function(e){
		var targetFile = $(this).data("file");
		var type = $(this).data("type");
		var fileIndex = $(this).index()-1; 
		console.log(targetFile);
		
		$.ajax({
			url : '/deleteFile',
			data : {fileName : targetFile,type:type,fileIndex:fileIndex},
			dataType : 'text',
			type : 'POST',
			success : function(result){
				alert(result);
				// 삭제된 파일의 인덱스 찾기
				var index = result.substring(result.indexOf(":")+2,result.length);
				// 삭제된 사진 화면에서 제거하기.
				var uploadList = uploadList = document.querySelector(".uploadResult").childNodes[1];
				uploadList.removeChild(uploadList.childNodes[parseInt(index)]);
			}
		});//$.ajax
	});
	
	$(".btnDownload").on("click",function(){
		
		var fileCnt = document.querySelector(".uploadResult").childNodes[1].childNodes.length-1;
		
		var fileList = new Array();
		
		if(fileCnt>0){
			for(var loop=1;loop<fileCnt+1;loop++){
			    var originalFileName = document.querySelector(".uploadResult").childNodes[1].childNodes[loop].childNodes[0].href.substring(document.querySelector(".uploadResult").childNodes[1].childNodes[loop].childNodes[0].href.indexOf("'")+1,document.querySelector(".uploadResult").childNodes[1].childNodes[loop].childNodes[0].href.lastIndexOf("'"))
			    console.log(originalFileName);
			    fileList.push(originalFileName);
			}	
		}
		
		$.ajax({
			url : '/downloadFileAll',
			data : {
				'fileList':fileList
			},
			type : 'GET',
			success : function(result){
			}
		}); //End ajax
			
	});
	
});
</script>

</body>
</html>
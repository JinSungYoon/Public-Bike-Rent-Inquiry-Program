<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="../includes/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자전거 고장신고 상세</title>
	<!-- Bootstrap 4 -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">	
	<!-- css -->
	<link rel="stylesheet" href="/resources/css/detailBreakdownReport.css" type="text/css">
</head>
<body>
	<div class="row">
         <div class="col-lg-12">
             <div class="panel panel-default">
                 <div class="panel-heading">
                     	고장신고 상세내용
                     	<button type="list" class="btnList">목록</button>
                 </div>
                 <!-- /.panel-heading -->
                 <div class="panel-body">
                   	   <div class="form-group" style="display:none">
                           <label>게시글번호</label>
                           <span class="btitle"></span>
                           <input class="form-control" name="bnum" value='<c:out value="${post.bnum}"/>' readonly="readonly">
                       </div>
                   	   <div class="form-group">
                           <label>제목</label>
                           <span class="btitle"></span>
                           <input class="form-control" name="btitle" value='<c:out value="${post.btitle}"/>'>
                       </div>
                       <div class="form-group">
                           <label>자전거 정류장</label>
                           <input class="form-control" name="stationid" value='<c:out value="${post.stationid}"/>'>
                       </div>
                       <div class="form-group">
                           <label>자전거 번호</label>
                           <input class="form-control" name="bikenum" value='<c:out value="${post.bikenum}"/>'>
                       </div>
                       <div class="form-group">
                           <label>고장부품</label>
                           <input class="form-control" name="brokenparts" value='<c:out value="${post.brokenparts}"/>'>
                       </div>
                       <div class="form-group">
                           <label>작성자</label>
                           <input class="form-control" name="writer" value='<c:out value="${post.writer}"/>'>
                       </div>
                       <div class="form-group">
                           <label>내용</label>
                           <textarea class="form-control" rows="5" cols="50" name="content"><c:out value="${post.content}"/></textarea>
                       </div>
                   	<button type="modify" class="btnModify">수정</button>
                   	<button type="delete" class="btnDelete">삭제</button>
                 </div>
                 <!-- /.panel-body -->
             </div>
             <!-- /.panel -->
         </div>
         <!-- /.col-lg-12 -->
     </div>
     <!--  /.row -->
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
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script type="text/javascript">
function showImage(fileCallPath){
	console.log(fileCallPath);
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
	(function(){
	
		var bnum = '<c:out value="${post.bnum}"/>';
		
		$.getJSON("/board/getAttachList",{bnum:bnum},function(arr){
			console.log(arr);
			
			var str = "";
			
			$(arr).each(function(i,attach){
				if(attach.fileType){
					
					var fileCallPath = encodeURIComponent(attach.uploadPath+"/s_"+attach.uuid+"_"+attach.fileName);
					
					var originPath = attach.uploadPath+"\\"+attach.uuid+"_"+attach.fileName;
					
					originPath = originPath.replace(new RegExp(/\\/g),"/");
					str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"'><a href=\"javascript:showImage(\'"+originPath+"\')\"><img src='/display?fileName="+fileCallPath+"'></a><a href='/downloadFile?fileName="+originPath+"'>"+attach.fileName+"</a>"+"<button type='button' data-file=\'"+fileCallPath+"\' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'>X</i></button>"+"</li>";
				}else{
					var fileCallPath = encodeURIComponent(attach.uploadPath+"/"+attach.uuid+"_"+attach.fileName);
					var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
					/*
					str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"'><div>";
					str += "<span> "+attach.fileName+"</span><br/>";
					str += "<img src='/resources/img/attach.png'>";
					str += "<button  type='button' class='btn btn-warning btn-circle'><i class='fa fa-times'>X</i></button>";
					str += "</div>";
					str += "</li>";
					*/
					str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"'><img src='/resources/img/attach.png'><a href='/downloadFile?fileName="+originPath+"'>"+attach.fileName+"</a>"+"<button type='button' data-file=\'"+fileCallPath+"\' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'>X</i></button>"+"</li>";
				}
			});
			$(".uploadResult ul").append(str);
		});//end json
	})();//end function
	
	// 이미지 파일 확대해서 Display
	$(".uploadResult").on("click","li",function(e){
		console.log("view image");
		var liObj = $(this);
				
		var path= encodeURIComponent(liObj.data("path")+"\\"+liObj.data("uuid")+"_"+liObj.data("filename"));
		
		console.log(path);
		
		if(liObj.data("type")){
			showImage(path.replace(new RegExp(/%5C/g),"/"));
		}
		
	});
	
	// 이미지 파일 삭제
	$(".uploadResult").on("click","button",function(e){
		var targetFile = $(this).data("file");
		var type = $(this).data("type");
		var fileIndex = $(this).parent().index()+1;
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
				var uploadList = document.querySelector(".uploadResult").childNodes[1];
				uploadList.removeChild(uploadList.childNodes[parseInt(index)]);
			}
		});//$.ajax
		
	});
	
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
	
	//이미지 파일 추가
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
	
	$(".btnList").on("click",function(e){
		e.preventDefault();
		$.ajax({
			url:'/board/breakdownList',
			type : 'GET',
			error : function(){
				
			},
			success : function(){
				window.location.href = '/board/breakdownList';
			}
		});
	});
	
	$(".btnDelete").on("click",function(e){
		
		if(confirm("삭제하시겠습니까?")){
			e.preventDefault();
			$.ajax({
				url:'/board/deleteBreakdownReport',
				data : {bnum:$("input[name=bnum]").val()},
				type:'POST',
				error : function(e){
					alert("통신 실패 : "+e);
				},
				success : function(result){
					var returnNum = '<c:out value="${returnVal}"/>';
					
					alert("성공적으로 "+$("input[name=bnum]").val()+"번이 삭제되었습니다.");
					
					window.location.href = '/board/breakdownList';
				}
				
			});
		}
	});
	
	
	// https://yulfsong.tistory.com/76 참조
	$(".btnModify").on("click",function(e){
		
		var bnum = $("input[name=bnum]").val();
		var btitle = $("input[name=btitle]").val();
		var stationid = $("input[name=stationid]").val();
		var bikenum = $("input[name=bikenum]").val();
		var brokenparts = $("input[name=brokenparts]").val();
		var writer = $("input[name=writer]").val();
		var content = $("textarea[name=content]").val();	
		var attachList = new Array();
		var param = {"bnum":bnum,"btitle":btitle,"stationid":stationid,"bikenum":bikenum,"brokenparts":brokenparts,"writer":writer,"content":content};
		
		$(".uploadResult ul li").each(function(i,obj){
			var object = {};
			object.fileName = $(obj).data("filename");
			object.uuid = $(obj).data("uuid");
			object.uploadPath = $(obj).data("path");
			object.fileType = $(obj).data("type");
			attachList.push(object);
		});
		
		param.attachList = attachList;
		
		if(confirm("수정하시겠습니까?")){
			e.preventDefault();
			$.ajax({
				url  : '/board/updateBreakdownReport',
				data : JSON.stringify(param),
				contentType : 'application/json',
				type : 'POST',
				dataType : "text",
				error : function(e){
					console.log(e);
				},
				success : function(result){

					alert("성공적으로 "+$("input[name=bnum]").val()+"번이 수정되었습니다.");

					window.location.href = '/board/breakdownList';
				}
			});
		}
	});
});
</script>
</html>
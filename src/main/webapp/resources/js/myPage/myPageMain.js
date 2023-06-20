function openPopup() {
    const popup = document.getElementById('popup');
    popup.style.display = 'block';
}
  
function closePopup() {
    const popup = document.getElementById('popup');
    popup.style.display = 'none';
}
function openPopup2() {
    const popup2 = document.getElementById('popup2');
    popup2.style.display = 'block';
}
  
function closePopup2() {
    const popup2 = document.getElementById('popup2');
    popup2.style.display = 'none';
}

function openPopup3() {
    const popup = document.getElementById('popup3');
    popup.style.display = 'block';

    $.ajax({
        url: 'openChatRoom', //채팅방 번호를 만들어줄 url 입력합니다.
        success: function(result) {
          if (result === 1) {
            console.log("성공");
          } else {
            console.log("채팅방 실패");
          }
        },
        error: function() {
          console.log('채팅 오픈 ajax 오류');
        }
      });
}
  
function closePopup3() {
    const popup = document.getElementById('popup3');
    popup.style.display = 'none';
}

function readValue(){
    const bg = document.querySelector(".chat-bg")
    const input = document.querySelector("#chattingInput")
    if(input.value.trim().length >0){
        bg.innerHTML += "<p><span>"+ input.value +"</span></p>";
        bg.scrollTop = bg.scrollHeight;
    }
    input.value = "";
}

function inputEnter(){
    if(window.event.key == "Enter"){
        readValue();
    }
}

const inputImage = document.getElementById("input-image");

if(inputImage != null){ // inputImage 요소가 화면에 존재 할 때
 
    // input type="file" 요소는 파일이 선택 될 때 change 이벤트가 발생한다.
    inputImage.addEventListener("change", function(){
       
        // this : 이벤트가 발생한 요소 (input type="file")

        // files : input type="file"만 사용 가능한 속성으로
        //         선택된 파일 목록(배열)을 반환
        //console.log(this.files)
        //console.log(this.files[0]) // 파일목록에서 첫 번째 파일 객체를 선택

        if(this.files[0] != undefined){ // 파일이 선택되었을 때

            const reader = new FileReader();
            // 자바스크립트의 FileReader
            // - 웹 애플리케이션이 비동기적으로 데이터를 읽기 위하여 사용하는 객체

            reader.readAsDataURL(this.files[0]);
            // FileReader.readAsDataURL(파일)
            // - 지정된 파일의 내용을 읽기 시작함.
            // - 읽어오는게 완료되면 result 속성 data:에 
            //   읽어온 파일의 위치를 나타내는 URL이 포함된다.  
            //  -> 해당 URL을 이용해서 브라우저에 이미지를 볼 수 있다.


            // FileReader.onload = function(){}
            // - FileReader가 파일을 다 읽어온 경우 함수를 수행
            reader.onload = function(e){ // 고전 이벤트 모델
                // e : 이벤트 발생 객체
                // e.target : 이벤트가 발생한 요소(객체) -> FileReader
                // e.target.result : FileReader가 읽어온 파일의 URL

                // 프로필 이미지의 src 속성의 값을 FileReader가 읽어온 파일의 URL로 변경
                const profileImage = document.getElementById("profile-image");

                profileImage.setAttribute("src", e.target.result);
                // -> setAttribute() 호출 시 중복되는 속성이 존재하면 덮어쓰기

                document.getElementById("delete").value = 0;
            }

        }
    });
}

// 이미지 선택 확인
function profileValidate(){
    const inputImage = document.getElementById("input-image");
    const del = document.getElementById("delete");
    if( inputImage.value == "" && del.value == 0 ){ 
        // 빈문자열 == 파일 선택 X / del의 값이 0 == x를 누르지도 않았다
        // --> 아무것도 안하고 변경버튼을 클릭한 경우

        alert("이미지를 선택한 후 변경 버튼을 클릭해주세요.");
        return false;
    }
    return true;
}
const contextPath = getContextPath();
function getContextPath() {
	return sessionStorage.getItem("contextpath");
}

document.getElementById("defaultUser").addEventListener("click", function(){
    // 0 : 안눌러짐
    // 1 : 눌러짐
    const del = document.getElementById("delete");
    const defaultImg = contextPath + "/resources/img/memberProfile/defaultUser.png";
    if(del.value == 0){ // 눌러지지 않은 경우
        // 1) 프로필 이미지를 기본 이미지로 변경

        document.getElementById("profile-image").setAttribute("src", defaultImg );                     

        // 2) input type="file"에 저장된 값(value)에 "" 대입 
        document.getElementById("input-image").value = "";

        del.value = 1; // 눌러진걸로 인식
    }
}); 

document.getElementById("admin_talk").addEventListener("click",function(){
    // $.ajax({
    //     url: '/stroke/chat/openChatRoom', //채팅방 번호를 만들어줄 url 입력합니다.
    //     type : "POST",
    //     success: function(result) {
    //       if (result > 0) {
    //         console.log("성공");
    //       } else {
    //         console.log("채팅방 실패");
    //       }
    //     },
    //     error: function() {
    //       console.log('채팅 오픈 ajax 오류');
    //     }
    //   });
});
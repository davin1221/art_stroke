

// 이벤트 캐러셀 -------------------------------------------------------
const eventLeftBtn = document.getElementById("mainpage-event-lbtn"); 
const eventRightBtn = document.getElementById("mainpage-event-rbtn"); 
const eventLeftBtn2 = document.getElementById("mainpage-event-lbtn2");
const eventRightBtn2 = document.getElementById("mainpage-event-rbtn2");
const eventContainer = document.querySelector(".mainpage-event-container");


(function addEvent(){
    eventLeftBtn.addEventListener("click",()=>{
        slideContainer.bind(this,0)();
        eventLeftBtn2.style.backgroundColor = "rgb(34,34,34)"
        eventRightBtn2.style.backgroundColor = "rgb(34,34,34,0.3)"
    });
    eventRightBtn.addEventListener("click",()=>{
        slideContainer.bind(this,-1)();
        eventRightBtn2.style.backgroundColor = "rgb(34,34,34)"
        eventLeftBtn2.style.backgroundColor = "rgb(34,34,34,0.3)"
    });

    eventLeftBtn2.addEventListener("click",()=>{
        slideContainer.bind(this,0)();
        eventLeftBtn2.style.backgroundColor = "rgb(34,34,34)"
        eventRightBtn2.style.backgroundColor = "rgb(34,34,34,0.3)"
    });
    eventRightBtn2.addEventListener("click",()=>{
        slideContainer.bind(this,-1)();
        eventRightBtn2.style.backgroundColor = "rgb(34,34,34)"
        eventLeftBtn2.style.backgroundColor = "rgb(34,34,34,0.3)"
    });

})();


function slideContainer(direction){
    eventContainer.style.transitionDuration = '500ms';
    eventContainer.style.transform = `translateX(${direction * 100/2}%)`;
}
// 이벤트 캐러셀 end -------------------------------------------------------


// 베스트 상품 슬라이드 
const bestLeftBtn = document.getElementById("mainpage-best-lbtn");
const bestRightBtn = document.getElementById("mainpage-best-rbtn");
const bestContainer = document.querySelector(".mainpage-best-product-wrap .product-list");

(function addEvent(){
    bestLeftBtn.addEventListener("click", ()=>{
        bestSlider(1);
    });
    bestRightBtn.addEventListener("click", ()=>{
        bestSlider(-1);
    });
})();


function bestSlider(direction){
    const selectBtn = (direction === 1) ? "left" : "right";
    bestContainer.style.transitionDuration = '500ms';
    bestContainer.style.transform = `translateX(${direction * 100/5}%)`;

    setTimeout(() => {
        bestReorganizeEl(selectBtn);
    }, 500); 

}

function bestReorganizeEl(selectedBtn) {
    bestContainer.removeAttribute('style');
    (selectedBtn === 'left') ? bestContainer.insertBefore(bestContainer.lastElementChild, bestContainer.firstElementChild)
                             : bestContainer.appendChild(bestContainer.firstElementChild);
  }




// 베스트 슬라이딩 메뉴 
let highlight = document.querySelector(".mainpage-best-category-highlight");
const items = document.querySelectorAll(".mainpage-best-category-selector-item");

items[0].classList.add("mainpage-best-category-selector-item--active")

function addClass(target){
    target.classList.add("mainpage-best-category-selector-item--active");
}

function selectItem(event){
    const target = event.target;
    const parent = document.querySelector(".mainpage-best-category-menu");
    const targetRect = target.getBoundingClientRect();
    const parentRect = parent.getBoundingClientRect();

    items.forEach(el => {
        el.classList.remove("mainpage-best-category-selector-item--active");
    });
    highlight.style.top = `${targetRect.top - parentRect.top}px`;
    addClass(target);
}   


// 리뷰 모달 -----
$(function(){
    $(".mainpage-review-img").click(function(){
        $(".mainpage-review-modal-overlay").fadeIn();
        $(".mainpage-review-modal-overlay").css("display", "flex");
    });

    $(".mainpage-review-modal-close").click(function(){
        $(".mainpage-review-modal-overlay").fadeOut();
    });

});



document.getElementById("mainpage-review-modal-product-btn").addEventListener("click", function(){

    const modalProduct = document.querySelector(".mainpage-review-modal-product");
    modalProduct.classList.toggle("show");

    console.log(modalProduct.style.display)


    if(modalProduct.style.display === "" || modalProduct.style.display === "flex") {
        modalProduct.style.display = "block";
        modalProduct.style.display = "flex";
    }
   
});


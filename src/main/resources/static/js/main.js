let menu = document.querySelectorAll('.menu-bottom'),
    lb = document.querySelector('.login-bottom'),
    rb = document.querySelector('.register-bottom');

function removeSt(){
    for(let i=0; i<menu.length;i++){
        menu[i].classList.remove("active");
    }
}

for(let i = 0; i<menu.length;i++){
    menu[i].addEventListener('click',()=>{
        removeSt();
        removeLbSb();
        menu[i].classList.add("active");
    })
}

function removeLbSb(){
    lb.classList.remove('lb-active');
    rb.classList.remove('lb-active');
}

lb.addEventListener('click',()=>{
    removeSt();
    removeLbSb();
    lb.classList.add('lb-active')
})

rb.addEventListener('click',()=>{
    removeSt();
    removeLbSb();
    rb.classList.add('lb-active');
})
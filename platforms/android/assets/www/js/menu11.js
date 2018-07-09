var option = {
    "menu": [
        {
            "name": "首页",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        },
        {
            "name": "功能1",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        },
        {
            "name": "功能2",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        },
        {
            "name": "功能3",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        },
        {
            "name": "功能4",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        },
        {
            "name": "功能5",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        },
        {
            "name": "功能6",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        },
        {
            "name": "功能7",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        },
        {
            "name": "功能8",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        },
        {
            "name": "功能9",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        },
        {
            "name": "功能10",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        },
        {
            "name": "功能11",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        },
        {
            "name": "功能12",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        },
        {
            "name": "功能13",
            "url": "../demo/demo1.html",
            "icon": "img/account.png",
        }
    ]
};
document.addEventListener("DOMContentLoaded",initial);
var myscroll;
var headerRate = 0,midRate = 0.92,footerRate = 0.08;
function initial() {
	var contentHeight = document.body.scrollHeight;
//	var y = document.body.scrollHeight - Math.floor(contentHeight*0.35) - Math.floor(contentHeight*0.55) - Math.floor(contentHeight*0.1);
	var top = document.getElementsByClassName("top")[0];
	top.style.height = contentHeight*headerRate+'px';
	var bottomNav = document.getElementsByClassName("bottomNav")[0];
	bottomNav.style.height = contentHeight*footerRate+'px';
	var bottom = document.getElementsByClassName("bottom")[0];
	bottom.style.height = contentHeight*midRate-2+'px';
	
	var navType = getQueryString("navType");
	if(null != navType && navType != undefined){
		selectNav(navType);
	}
	
	jQuery(".bottomNav li").each(function(){
		this.getElementsByTagName("a")[0].style.height = contentHeight*footerRate+'px'; //调整navBar按钮高度
		this.getElementsByTagName("a")[0].style.lineHeight = contentHeight*footerRate+'px'; //调整navBar按钮高度
		jQuery(this).bind("tap",function(){
			
			changePage($(this).find('a').attr('navType'));
		});
	});
	
//	generateMenu();
//	myScroll = new iScroll('menuWrapper',{
//		vScrollbar:false,
//		hScrollbar:false
//	});
	this.myScroll = $('#wrapper').iScroll({
	    snap: true,
	    momentum: false,
	    hScrollbar: false,
	    onScrollEnd: function () {
	      document.querySelector('#indicator > li.active').className = '';
	      document.querySelector('#indicator > li:nth-child(' + (this.currPageX+1) + ')').className = 'active';
	    }
	});
	var leftW = (document.getElementById("nav").offsetWidth-document.getElementById("indicator").offsetWidth)/2;
	document.getElementById("indicator").style.marginLeft = leftW +'px';
}

function generateMenu(){
	var menuData = option.menu;
	var counter = 0;
	for(var i = 0 ; i < menuData.length ; i+=9){
		var li = document.createElement("li");
		li.style.width = parseInt(document.body.scrollWidth*0.9)+'px';
		for(var j = i; j < i+9 && j < menuData.length;j++){
			var div = document.createElement("div");
			div.setAttribute("class","menuItem");
//			div.innerHTML = menuData[j].name;
			var menuIcon = document.createElement("img");
			menuIcon.src = menuData[j].icon;
			menuIcon.style.width = parseInt(document.body.scrollHeight*0.48/4*0.65)+'px';
			menuIcon.style.height = parseInt(document.body.scrollHeight*0.48/4*0.65)+'px';
			menuIcon.menuUrl = menuData[j].url;
			jQuery(menuIcon).bind("tap",function(){
				var selectedNav = document.getElementsByClassName("bottomNav")[0].getElementsByClassName("ui-btn-active");
				window.location.href = "PhoneLayout3_2.html?tar="+this.menuUrl+"&navType="+selectedNav[0].getAttribute("navType");
			});
			var menuLabel = document.createElement("div");
			menuLabel.innerHTML = menuData[j].name;
			menuLabel.setAttribute("class","menuLabel");
			
			div.appendChild(menuIcon);
			div.appendChild(menuLabel);
			
			div.style.width = parseInt(document.body.scrollWidth*0.9*0.3)+'px';
			if((j+1)%3 == 0){
				div.style.marginRight = 0+'px';
			}
			li.appendChild(div);
		}
		document.getElementById("thelist").appendChild(li);
		counter++;
	}
	var scroller = document.getElementById("scroller");
	scroller.style.width = document.body.scrollWidth*0.9*counter+'px';
	for(var i = 0 ; i < counter-1 ; i++){
		var navli = document.createElement("li");
		navli.innerHTML = i+2;
		document.getElementById("indicator").appendChild(navli);
	}
}

function refreshMenu(){
	var thelist = document.getElementById("thelist");
	thelist.innerHTML = "";
	var indicator = document.getElementById("indicator");
	indicator.innerHTML = "";
	var indicator1 = document.createElement("li")	;
	indicator1.setAttribute("class","active");
	indicator.appendChild(indicator1);
	generateMenu();
	this.myScroll = $('#wrapper').iScroll({
	    snap: true,
	    momentum: false,
	    hScrollbar: false,
	    onScrollEnd: function () {
	      document.querySelector('#indicator > li.active').className = '';
	      document.querySelector('#indicator > li:nth-child(' + (this.currPageX+1) + ')').className = 'active';
	    }
	});
}

function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		return unescape(r[2]);
	}
	return null;
}

function hasClass( elements,cName ){  
    return !!elements.className.match( new RegExp( "(\\s|^)" + cName + "(\\s|$)") ); 
};  
function addClass( elements,cName ){  
    if( !hasClass( elements,cName ) ){  
        elements.className += " " + cName;  
    };  
};  
function removeClass( elements,cName ){  
    if( hasClass( elements,cName ) ){  
        elements.className = elements.className.replace( new RegExp( "(\\s|^)" + cName + "(\\s|$)" ), " " );
    };  
};

function selectNav(navType){
	var navBar = document.getElementsByClassName("bottomNav")[0].getElementsByTagName("a");
	for(var i = 0 ; i < navBar.length ; i++){
		if(navBar[i].getAttribute("navType") == navType){
			var pre = document.getElementsByClassName("bottomNav")[0].getElementsByClassName("ui-btn-active")[0];
			removeClass(pre,"ui-btn-active");
			addClass(navBar[i],"ui-btn-active");
			break;
		}
	}
}

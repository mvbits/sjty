// ActionScript Document
window.alert = function(str)
{
var info="��ʾ"
var msg="ȷ��"
var shield = document.createElement("DIV");
shield.id = "shield";
shield.style.position = "absolute";
shield.style.left = "0px";
shield.style.top = "0px";
shield.style.width = "100%";
shield.style.height = document.body.scrollHeight+"px";
//�����Ի���ʱ�ı�����ɫ
//shield.style.background = "#fff";
shield.style.textAlign = "center";
shield.style.zIndex = "25";
//����͸�� IE��Ч
//shield.style.filter = "alpha(opacity=0)";
var alertFram = document.createElement("DIV");
alertFram.id="alertFram";
alertFram.style.position = "absolute";
alertFram.style.left = "40%";
alertFram.style.top = "50%";
alertFram.style.marginLeft = "-90px";
alertFram.style.marginTop = "-75px";
alertFram.style.width = "250px";
alertFram.style.height = "100px";
alertFram.style.background = "#ff0000";
alertFram.style.textAlign = "center";
alertFram.style.lineHeight = "150px";
alertFram.style.zIndex = "300";
strHtml = "<ul style=\"list-style:none;margin:0px;padding:0px;width:100%\">\n";
strHtml += " <li style=\"background:#DD828D;text-align:left;padding-left:20px;font-size:14px;font-weight:bold;height:25px;line-height:25px;border:1px solid #F9CADE;\">"+info+"</li>\n";
strHtml += " <li style=\"background:#fff;text-align:center;font-size:12px;height:80px;line-height:80px;border-left:1px solid #F9CADE;border-right:1px solid #F9CADE;\">"+str+"</li>\n";
strHtml += " <li style=\"background:#FDEEF4;text-align:center;font-weight:bold;height:25px;line-height:25px; border:1px solid #F9CADE;\"><input type=\"button\" value="+msg+" onclick=\"doOk()\" /></li>\n";
strHtml += "</ul>\n";
alertFram.innerHTML = strHtml;
document.body.appendChild(alertFram);
document.body.appendChild(shield);
this.doOk = function(){
alertFram.style.display = "none";
shield.style.display = "none";
}
alertFram.focus();
document.body.onselectstart = function(){return false;};
} 
/*
   Copyright Jaimon Mathew <mail@jaimon.co.uk>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var lmc={};
lmc.prototype=Object.prototype;
(function() {
    lmc.prototype.docToLoad=null;
    lmc.prototype.ge$=function(d) {return document.getElementById(d);};
    lmc.prototype.setLogLevel=function(l) {
        var d=lmc.ge$('logLevel');
        for(var i=0;i<d.options.length;i++) {
            if(d.options[i].text==l) {
                d.selectedIndex=i;
                break;
            }
        }
    };
    lmc.prototype.disableButtons=function(d) {
        lmc.ge$('changeLevelBtn').disabled=d;
        lmc.ge$('stopLoggerBtn').disabled=d;
    };
    lmc.prototype.reloadIFrame=function(a,l,k) {
        var d=lmc.ge$('livelog').src;
        var i=d.indexOf('&action');
        if(i > 0) {
            d = d.substring(0,i);
        }
        d+='&action='+a+'&runningId='+window.livelog.lmg.runningId;
        if(l) {
            d+='&logLevel='+l;
        }
        if(k) {
            lmc.docToLoad=d;
            lmc.loadiframe();
        }else {
            lmc.docToLoad=d;
            setTimeout(lmc.loadiframe,50);
        }
    };
    lmc.prototype.stopLogger=function(){
        try {
            if(window.livelog.lmg.stopped) {return;}
            lmc.reloadIFrame('stop',false,true);
            window.livelog.lmg.runningId=null;
            window.livelog.lmg.stopped=true;
        }catch(error) {window.status=error;}
    };
    lmc.prototype.handleUnload=function(e) {
        var st=window.livelog.lmg.stopped;
        lmc.stopLogger();
        if(!st) {
            alert('Stoping the monitor. Press OK to continue');
        }
    }
    lmc.prototype.changeLevel=function() {
        //if(document.location.href.indexOf('loggerName=root') > 0) {
        //    alert("Sorry, you can't change the log level for Root Logger");
        //}else {
            var l=lmc.ge$('logLevel');
            lmc.reloadIFrame('resume',l.options[l.selectedIndex].text,true);
        //}
    };
    lmc.prototype.setWH=function() {
        try {
            var viewportwidth;
            var viewportheight;
            if (typeof(window.innerWidth) == 'number') {viewportwidth = window.innerWidth;viewportheight = window.innerHeight;
            }else if(document.documentElement && document.documentElement.clientWidth) {viewportwidth = document.documentElement.clientWidth;viewportheight = document.documentElement.clientHeight;
            }else {viewportwidth = document.body.clientWidth;viewportheight = document.body.clientHeight;}
            var tb=lmc.ge$('txtBox').style;
            tb.display='none';
            tb.height=(viewportheight-25)+'px';
            tb.width=(viewportwidth - 352 - 10)+'px';
            tb.display='';
        }catch(error){}
    };
    lmc.prototype.loadiframe=function() {
        lmc.ge$('livelog').src=lmc.docToLoad;
    };
    lmc.prototype.trapEsc=function(e) {
        if(!e) {e=(window.event)?window.event:event;}
        var ks=e.keyCode?e.keyCode:((e.charCode)?e.charCode:e.which);
        if(ks==27) {
            e.returnValue=false;
            try {
                e.preventDefault();
            }catch(error){}
            return false;
        }else {
            return true;
        }
    };
    lmc.prototype.loadIFFromLoc=function() {
        var d=document.location.href;
        var i=d.indexOf("?");
        d=d.substr(i+1);
        lmc.docToLoad='../liveLogFeeder?' + d;
        lmc.loadiframe();
    };
    lmc.prototype.g2l=function(d) {
        if(window.livelog.lmg.stopped) {
            window.location.href=d;
        }else {
            alert('Please stop monitoring before you can select another logger');
        }
    };
}());
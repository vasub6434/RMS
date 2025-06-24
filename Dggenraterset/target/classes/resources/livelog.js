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
var lmg={};
lmg.prototype=Object.prototype;
lmg.prototype.MAX_LOG_ITEMS_TO_DISPLAY = 1000;
lmg.prototype.stopped=false;
lmg.prototype.msgQ=[];
lmg.prototype.runningId=0;
(function() {
    lmg.prototype.gpe$ = function(d) {return parent.document.getElementById(d);};
    lmg.prototype.addLText=function(tx) {
        var tb = lmg.gpe$('txtLBox');
        var t = tb.value;
        if(t=='') {
            t=tx;
        }else {
            t=tx+'\n'+t;
        }
        tb.value=t;
    };
    lmg.prototype.addText=function(t,c) {
        var el=parent.document.createElement("div");
        el.className=c;
        el.innerHTML=t;
        lmg.msgQ.push(el);
    };
    lmg.prototype.insertText=function() {
        var ml = lmg.msgQ.length;
        if(lmg.msgQ.length > 0) {
            var tb = lmg.gpe$('txtBox');
            for(var i=0;i<ml;i++) {
                tb.insertBefore(lmg.msgQ[i],i==0?tb.firstChild:lmg.msgQ[i-1]);
            }
            lmg.msgQ=[];
            if(lmg.MAX_LOG_ITEMS_TO_DISPLAY > 0) {
                var l=tb.childNodes.length;
                while(l > lmg.MAX_LOG_ITEMS_TO_DISPLAY) {
                    tb.removeChild(tb.childNodes[--l]);
                }
            }
        }
    };
})();
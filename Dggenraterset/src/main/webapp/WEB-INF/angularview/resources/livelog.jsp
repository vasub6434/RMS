<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252" />
    <title>Live Log Streaming</title>
    <script type="text/javascript" src="livelogCtrl.js"></script>
    <link type="text/css" rel="stylesheet" href="livelog.css"/>
  </head>
  <body class="tah" onkeydown="lmc.trapEsc(event);">
    <form action="#">
      <div id='crc'><div id='crl'>Live Log Monitor v0.1</div><div id='crr'>&copy; <a href='http://www.jaimon.co.uk' target='_blank'>Jaimon Mathew</a></div></div>
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td width="*%" valign="top">
            <div class="tah" id="txtBox" style="overflow:scroll;display:none;width:100%;"></div>
          </td>
          <td width="2">&nbsp;</td>
          <td width="350" valign="top">
              <span class="tah">Status Window:</span>
            <textarea class="cnew" id="txtLBox" rows="8" cols="47"></textarea>
            Log level
            <select id="logLevel" style="width: 70px;" class="tah">
                <option>FATAL</option>
                <option>ERROR</option>
                <option>WARN</option>
                <option>INFO</option>
                <option>DEBUG</option>
                <option>TRACE</option>
            </select>
            <input type="button" value="Change Level" class="tah mbt" onclick="lmc.changeLevel();" id="changeLevelBtn" style="width:110px;"/>
            <hr size="1"/>
            <input type="button" value="Clear Log Window" class="tah mbt" onclick="lmc.ge$('txtBox').innerHTML='';"/>
            <input type="button" value="Clear Status Window" class="tah mbt" onclick="lmc.ge$('txtLBox').value='';"/>
            <hr size="1"/>
            <input type="button" value="Stop" class="tah mbt" onclick="lmc.stopLogger();" style="width: 75px;" id="stopLoggerBtn"/>
            <input type="button" value="Restart" class="tah mbt" onclick="lmc.reloadIFrame('resume',false,true);" style="width: 75px;"/>
            <input type="button" value="Return" class="tah mbt" onclick="lmc.g2l('../livelog');" style="width: 75px;"/>
          </td>
        </tr>
      </table>
    </form>
    <script type="text/javascript">
        (function() {
            window.onbeforeunload=function(){lmc.handleUnload();};
            window.onresize=lmc.setWH;
            setTimeout(lmc.setWH,50);
            setTimeout(lmc.loadIFFromLoc,50);
        })();
    </script>
      <iframe id="livelog" name="livelog" style="display:none;" height="0" width="0"></iframe>
  </body>
</html>
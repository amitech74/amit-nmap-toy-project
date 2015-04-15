/**
 * Functionality to initiate host(s) scanning and also retrieve history
 */
function nmapscan() {
  /**
   * Remove all added transactions/history and enable all buttons
   */
  this.initReset=function(){
    var myObj = this;
    $("#idBtnResetEverything").on('click',
        function(event){
          $("#idSubmitScanHosts").prop('disabled',false);
          $("#idBtnGetHostHistory").prop('disabled',false);

          //-- any div attached displaying a transaction progress
          $( "div[id^='idTransactionDisplay-']").remove();

          //-- any div attached displaying a host history
          $( "div[id^='idHostHistoryDisplay-']").remove();

        }
    );//-- On Asset link Click event
  }

  this.initProcessHostScanRequest=function(){

    var myNmapscanObj=window.nmapscanObj;

    $("#idSubmitScanHosts").on('click',
        function(event){

          var data = $('#idInputScanHosts').val();

          console.log("Validating and processing " + data);

          //-- Validate Data
          var nsu = new nmapscanUtil();
          $('#idInputScanHostsError').html("");

          if(nsu.isSearchDataValid(data)==false){
            $('#idInputScanHostsError').html("Please specify space separated hosts/ips or a single host/ip to scan.  Only . (dot) and spaces allowed");
            return;
          }

          //-- Disable the buttons till we process the request.
          $("#idSubmitScanHosts").prop('disabled',true);
          $("#idBtnGetHostHistory").prop('disabled',true);

          var uniqueHostValues = nsu.extractUniqueHostValues(data);
          jQuery.each(uniqueHostValues,
              function(i,hostName){
                console.log("Initiating scanning of host = " + hostName);
                myNmapscanObj.initiateHostScanningAndPolling(hostName);
              }
          );


        }
    );

  }


  this.initiateHostScanningAndPolling=function(hostName){
    //console.log("Reached into initiate host scanning for " + hostName);
    $.post(
        "/nmap",//-- URL
        //"{'hostName': '"+hostName+"'}",//-- Parameters
        '{"hostName": "'+hostName+'"}',
        function(data){//-- Success handler
          //-- Data is already parsed.
          var transactionId=data.transactionId;
          console.log("Transaction id ="+transactionId);
          var myNmapscanObj=window.nmapscanObj;

          //-- Add Div for this transaction Id
          myNmapscanObj.addTransactionTrackingDiv(data);

          //-- Initiate Polling
          setTimeout(function(){myNmapscanObj.startPolling(transactionId)},5000);

        },//-- success handler

        //-- json response
        "json"
    ).fail(
        function(xhr, status, text){

          console.log("xhr="+xhr+"status="+status+"text="+text);
        }
    );
    //-- endof post

  }//-

  this.addTransactionTrackingDiv=function(data){
    var transactionId=data.transactionId;
    $('<div>')
        .attr('id', 'idTransactionDisplay-' + transactionId)
        .html('<br>Processing Transaction Id = ' + transactionId)
        .appendTo("#idTransactionsDiaplayContainer");

    //var spinnerImg=window.resources + "/images/spinner.gif";
    //$('<img/>').attr('src',spinnerImg).appendTo($('#idTransactionDisplay-' + transactionId));
    //var myNmapscanObj=window.nmapscanObj;
    //myNmapscanObj.loadImage("/images/spinner.gif", 800, 800,$('#idTransactionDisplay-' + transactionId));
  }

  this.startPolling=function(transactionId){
    console.log("polling for transactionId="+transactionId);

    $.getJSON('nmap/'+transactionId, function(data) {

          //-- Poll after timeout, if transaction is still in progress
          if(data.status=="InProgress"){
            var myNmapscanObj=window.nmapscanObj;
            setTimeout(function(){myNmapscanObj.startPolling(transactionId)},5000);
            console.log("continuing to poll for transactionId="+transactionId);
          }
          else{
            console.log("Scan finished for transactionId="+transactionId);
            console.log(data);

            //-- Display results
            $("#idTransactionDisplay-"+transactionId).html("<br> Results for <b>" + data.hostScanResult.host + "</b><br>" +JSON.stringify(data, undefined, 2));
          }

        }
        ,
        "json"
    ).fail(
        function(xhr, status, text){
          console.log("xhr="+xhr+"status="+status+"text="+text);
        }
    );
  }


  this.addHostHistoryDiv=function(hostName){
    var cleanHostName = hostName.replace(/\./g, '');
    $('<div>')
        .attr('id', 'idHostHistoryDisplay-' + cleanHostName)
        .html('<br>Getting history for = ' + cleanHostName)
        .appendTo("#idTransactionsDiaplayContainer");

    //var spinnerImg=window.resources + "/images/spinner.gif";
    //$('<img/>').attr('src',spinnerImg).appendTo($('#idHostHistoryDisplay-' + cleanHostName));

  }

  this.loadImage=function(path, width, height, target) {
      $('<img src="'+ path +'">').load(function() {
        $(this).width(width).height(height).appendTo(target);
      });
    }

  this.initProcessHostHistoryScanRequest=function(){
    $("#idBtnGetHostHistory").on('click',
        function(event){

          var hostName = $('#idNmaphostHistory').val();

          console.log("Validating and processing host history " + hostName);

          //-- Validate Data
          var nsu = new nmapscanUtil();
          $('#idNmaphostHistoryError').html("");

          if(nsu.isHostHistoryDataValid(hostName)==false){
            $('#idNmaphostHistoryError').html("Please specify only 1 host without spaces");
            return;
          }

          //-- Disable the buttons till we process the request.
          $("#idSubmitScanHosts").prop('disabled',true);
          $("#idBtnGetHostHistory").prop('disabled',true);

          var myNmapscanObj=window.nmapscanObj;
          myNmapscanObj.addHostHistoryDiv(hostName);

          $.getJSON('history/'+hostName, function(data) {
                //-- Display results
                //-- remove .
                var cleanHostName = data.host.replace(/\./g, '');
                $("#idHostHistoryDisplay-"+cleanHostName).html("<br> Results for <b>" + data.host + "</b><br>" +JSON.stringify(data, undefined, 2));

              }
              ,
              "json"
          ).fail(
              function(xhr, status, text){
                console.log("xhr="+xhr+"status="+status+"text="+text);
              }
          );


        }
    );

  }

}

function nmapscanUtil() {

  this.isSearchDataValid=function(data){
    var pattern = /^[A-Za-z0-9 .]+$/;
    if(!pattern.test(data)){
      return false;
    }

    return true;
  }

  this.isHostHistoryDataValid=function(data){
    var pattern = /^[A-Za-z0-9.]+$/;
    if(!pattern.test(data)){
      return false;
    }

    return true;
  }

  this.extractUniqueHostValues=function(data){
    return $.unique(data.trim().split(' '));
  }

}
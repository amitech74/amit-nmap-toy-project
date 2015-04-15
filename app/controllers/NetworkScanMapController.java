package controllers;

import amit.nmapscans.models.*;
import amit.nmapscans.services.ScanService;
import com.fasterxml.jackson.databind.JsonNode;
import com.wordnik.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import play.libs.F;
import play.mvc.BodyParser;
import play.mvc.Result;

import javax.ws.rs.PathParam;

@org.springframework.stereotype.Controller
@Api(
        value = "/nmap",
        description = "Various end points - Scan for open ports using nmap",
        consumes = "application/json",
        produces = "application/json")
public class NetworkScanMapController {

  private static Logger log = LoggerFactory.getLogger(NetworkScanMapController.class);

  @Autowired
  ScanService scanService;


  @ApiOperation(
          nickname = "initiateHostScanning",
          value = "Instantiate host scanning for open ports",
          notes = "Instantiates scanning of a host for open ports using nmap.  Returns a call back url to poll for results",
          response = HostScanInitiated.class,
          tags = "HostScan",
          httpMethod = "POST")
  @ApiResponses( value = {
          @ApiResponse(code = 400, message = "Caller provided incorrect parameters", response = ServiceFault.class),
          @ApiResponse(code = 404, message = "Couldn't find the information", response = ServiceFault.class),
          @ApiResponse(code = 500, message = "An internal error occurred on the server", response = ServiceFault.class),
  })
  @ApiImplicitParams(@ApiImplicitParam(name = "body", value = "host name", required = true, dataType = "String", paramType = "body"))
  @BodyParser.Of(BodyParser.Json.class)
  public F.Promise<Result> initiateHostScanning(){

    JsonNode json = play.mvc.Controller.request().body().asJson();
    String hostName = json.findPath("hostName").textValue();
    if(hostName == null) {
      return ModelsUtil.prepareBadRequestPromise("400", "Missing parameter [hostName]", "");
    }

    log.debug("processing nmap for {}",hostName);

    F.Promise<Result> hsiResult = scanService.initiateHostScan(hostName);
    return hsiResult;

  }

  @ApiOperation(
          nickname = "retrieveScanResultForTransactionId",
          value = "Retrieve scanning history of a specific transactionId",
          notes = "Retrieves scanning results of specified transactionId",
          response = HostScanStatus.class,
          tags = "HostScan",
          httpMethod = "GET")
  @ApiResponses( value = {
          @ApiResponse(code = 400, message = "Caller provided incorrect parameters", response = ServiceFault.class),
          @ApiResponse(code = 404, message = "Couldn't find the information", response = ServiceFault.class),
          @ApiResponse(code = 500, message = "An internal error occurred on the server", response = ServiceFault.class),
  })
  public F.Promise<Result> retrieveScanResultForTransactionId(
          @ApiParam(
                  value = "Retrieve result for this transactionId if available",
                  required = true)
          @PathParam("transactionId") Integer transactionId

  ){

    if(ModelsUtil.invalidParameters(transactionId))
      return ModelsUtil.prepareBadRequestPromise("400", "Incorrect parameter [transactionId]", "");

      return scanService.retrieveScanResultForTransactionId(transactionId);
  }



  @ApiOperation(
          nickname = "retrieveScanHistoryForHost",
          value = "Retrieve scanning history of host",
          notes = "Retrieves all past scanning history results of specified host",
          response = HostScanHistory.class,
          tags = "HostScanHistory",
          httpMethod = "GET")
  @ApiResponses( value = {
          @ApiResponse(code = 400, message = "Caller provided incorrect parameters", response = ServiceFault.class),
          @ApiResponse(code = 404, message = "Couldn't find the information", response = ServiceFault.class),
          @ApiResponse(code = 500, message = "An internal error occurred on the server", response = ServiceFault.class),
  })
  public F.Promise<Result> retrieveScanHistoryForHost(
          @ApiParam(
                  value = "Retrieve history for this hostname",
                  required = true)
          @PathParam("hostName") String hostName
  ){

    if(StringUtils.isEmpty(hostName))
      return ModelsUtil.prepareBadRequestPromise("400", "Invalid parameter [hostName]", "");

      return scanService.retrieveScanHistoryForHost(hostName);
  }


}

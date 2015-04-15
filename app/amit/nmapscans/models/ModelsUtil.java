package amit.nmapscans.models;

import amit.nmapscans.xml.jaxb.Nmaprun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

public class ModelsUtil {
  private static Logger log = LoggerFactory.getLogger(ModelsUtil.class);

  public static Set<Port> copyPorts(List<Nmaprun.Host.Ports.Port> nmapPorts){
    Set<Port> modelPorts = new LinkedHashSet<>();
    if(nmapPorts == null)
      return modelPorts;

    for(Nmaprun.Host.Ports.Port nmapPort : nmapPorts){
      modelPorts.add(new Port(Integer.parseInt(nmapPort.getPortid()), nmapPort.getProtocol()));
    }

    return modelPorts;
  }

  public static  boolean invalidParameters(int id){
    if(id<=0) {
      log.debug("Invalid parameter(s) - bad request");
      return true;
    }
    return false;
  }

  public static HostScanInitiated createHostScanIntiated(int transactionId, String status) {
    return new HostScanInitiated(transactionId, status);
  }

  public static Result prepareBadRequestResult(String code, String message, String link){
    return Controller.badRequest(Json.toJson(new ServiceFault(code, message, link)));
  }

  public static Result prepareInternalServerErrorResult(String code, String link, Exception ex){
    log.error("Internal Server Error - ", ex);
    return Controller.internalServerError(Json.toJson(new ServiceFault(code, ex.getMessage(), link)));
  }

  public static F.Promise<Result> prepareBadRequestPromise(final String code, final String message, final String link){
    return F.Promise.promise(
            new F.Function0<Result>() {
              public Result apply() {
                return prepareBadRequestResult(code, message, link);
              }
            }
    );

  }

  public static F.Promise<Result> prepareInternalServerErrorPromise(final String code, final String link, final Exception ex){
    return F.Promise.promise(
            new F.Function0<Result>() {
              public Result apply() {
                return prepareInternalServerErrorResult(code, link, ex);
              }
            }
    );

  }

  public static Result processBusinessOutputIntoResult(Object businessData){

    if(noDataFound(businessData))
      return play.mvc.Controller.notFound();

    return play.mvc.Controller.ok(
            Json.toJson(businessData)
    );
  }

  public static <V> F.Promise<Result> handleBusinessDataWithRecovery(F.Promise<V> businessData){
    return businessData.map(new F.Function<V, Result>() {
      @Override
      public Result apply(V businessData) throws Throwable {
        return processBusinessOutputIntoResult(businessData);
      }
    }).recover(
            new F.Function<Throwable, Result>() {
              @Override
              public Result apply(Throwable throwable)  {
                return Controller.internalServerError(
                        Json.toJson(new ServiceFault("500", throwable.getMessage(), "")));
              }
            }

    );

  }

  public static  boolean noDataFound(Object responseData){
    if(responseData instanceof Collection)
      return emptyCollection((Collection)responseData);

    return (responseData==null);
  }

  public static  boolean emptyCollection(Collection responseData){
    return (responseData==null || responseData.size()==0);
  }

}

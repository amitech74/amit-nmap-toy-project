package amit.nmapscans.models;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="Fault",description="Service Fault")
public class ServiceFault {

    private String code = "";
    private String message = "";
    private Link link;


    public ServiceFault(String code, String errorMessage, String linkRel, String detailsLink) {
        this.code = code;
        this.message = errorMessage;
        this.link = new Link(linkRel, detailsLink);
    }

    public ServiceFault(String code, String errorMessage, String detailsLink) {
        this(code, errorMessage, "self", detailsLink);
    }

    public ServiceFault(String code, String errorMessage, Link faultLink) {
        this.code = code;
        this.message = errorMessage;
        this.link = faultLink;
    }

    @ApiModelProperty(value = "Code", required = true, position = 1, dataType = "String")
    public String getCode() {
        return this.code;
    }

    @ApiModelProperty(value = "Message", required = true, position = 2, dataType = "String")
     public String getMessage() {
        return this.message;
    }

    @ApiModelProperty(value = "Link", required = false, position = 3, dataType = "Link")
    public Link getLink() {
        return this.link;
    }

}

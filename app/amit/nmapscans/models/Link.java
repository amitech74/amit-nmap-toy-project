package amit.nmapscans.models;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="Link",description="Navigation link to related resources")
public class Link {

    private String rel;
    private String href;


    public Link(String urlLink) {
        this.rel = "self";
        this.href = urlLink;
    }

    public Link(String name, String urlLink) {
        this.rel = name;
        this.href = urlLink;
    }

    @ApiModelProperty(value = "rel", required = false, position = 1, dataType = "String")
    public String getRel() {
        return this.rel;
    }

    @ApiModelProperty(value = "href", required = false, position = 2, dataType = "String")
    public String getHref() {
        return this.href;
    }

    @Override
    public String toString() {
        return getRel() +  "::" + getHref();
    }
}
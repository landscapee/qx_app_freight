package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class IOManifestBean implements Serializable {

    private List<SmInventoryEntryandexit> records;
    private Integer total;
    private Integer size;
    private Integer current;
    private Integer pages;

}

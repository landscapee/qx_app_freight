package qx.app.freight.qxappfreight.bean.loadinglist;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;

@Setter
@Getter
public class CargoManifestEventBusEntity {

    public final List <FlightAllReportInfo> beans;

    public CargoManifestEventBusEntity(List<FlightAllReportInfo> beans) {
        this.beans = beans;
    }

}

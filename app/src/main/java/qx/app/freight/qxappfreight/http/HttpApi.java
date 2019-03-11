package qx.app.freight.qxappfreight.http;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.bean.request.AddInfoEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.bean.request.FightScooterSubmitEntity;
import qx.app.freight.qxappfreight.bean.request.GetScooterListInfoEntity;
import qx.app.freight.qxappfreight.bean.request.GpsInfoEntity;
import qx.app.freight.qxappfreight.bean.request.LoginEntity;
import qx.app.freight.qxappfreight.bean.request.ModifyTextEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.request.QueryContainerInfoEntity;
import qx.app.freight.qxappfreight.bean.request.ReturnWeighingEntity;
import qx.app.freight.qxappfreight.bean.request.ScooterSubmitEntity;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.request.TransportListCommitEntity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.AgentBean;
import qx.app.freight.qxappfreight.bean.response.AirlineRequireBean;
import qx.app.freight.qxappfreight.bean.response.ArrivalDeliveryInfoBean;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.ExistBean;
import qx.app.freight.qxappfreight.bean.response.FreightInfoBean;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.GetQualificationsBean;
import qx.app.freight.qxappfreight.bean.response.GetScooterByScooterCodeBean;
import qx.app.freight.qxappfreight.bean.response.GetScooterListInfoBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.QueryAviationRequireBean;
import qx.app.freight.qxappfreight.bean.response.QueryContainerInfoBean;
import qx.app.freight.qxappfreight.bean.response.QueryReservoirBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TestInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.bean.response.UldInfoListBean;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * app所有接口
 */
public interface HttpApi {
    //登录接口
    @POST("/service-base-sysmanage/auth/login")
    Observable<BaseEntity<LoginResponseBean>> login(@Body LoginEntity model);

    /***********收验****************************/
    //获取货代公司资质
    @GET("/service-product-transportcheck/freight/get/{freightCode}")
    Observable<BaseEntity<GetQualificationsBean>> getQualifications(@Path("freightCode") String freightCode);

    //查询航空公司要求
    @GET("service-product-transportcheck/airLine/get/{airLineId}")
    Observable<BaseEntity<List<QueryAviationRequireBean>>> getQueryAviationRequire(@Path("airLineId") String airLineId);

    //暂存提交
    @POST("service-product-transportcheck/ins/commit")
    Observable<BaseEntity<Object>> storageCommit(@Body StorageCommitEntity model);

    //修改收验
    @GET("service-product-transportcheck/ins/update")
    Observable<BaseEntity<Object>> modifyTest(@Body ModifyTextEntity model);

    //收验详情
    @GET("service-product-transportcheck/ins/get/{waybillId}")
    Observable<BaseEntity<TestInfoListBean>> testInfo(@Path("waybillId") String waybillId);

    /***********收运****************************/
    //代办
    @POST("service-base-taskassign/todoCenter/task-todo-info/selectTodoList")
    Observable<BaseEntity<List<TransportListBean>>> transportList(@Body BaseFilterEntity model);

    //提交或者暂存
    @POST("service-product-receivecargo/rc/commit")
    Observable<BaseEntity<Object>> transportListCommit(@Body TransportListCommitEntity model);


    //出港收货 -收运
    @POST("service-product-receivecargo/rc/list")
    Observable<BaseEntity<AgentBean>> agentTransportationList(@Body BaseFilterEntity model);

    //查询集装箱列表信息
    @POST("service-base-sysmanage/uld/list")
    Observable<BaseEntity<List<QueryContainerInfoBean>>> queryContainerInfo(@Body QueryContainerInfoEntity model);

    //查询库区类别
    @GET("service-base-sysmanage/repInfo/list")
    Observable<BaseEntity<List<QueryReservoirBean>>> queryReservoir();

    //添加收运信息
    @POST("service-product-receivecargo/rc/saveOrUpdate")
    Observable<BaseEntity<MyAgentListBean>> addInfo(@Body MyAgentListBean model);

    //判断板车是否被占用
    @GET("service-product-receivecargo/rc/exist/{scooterId}")
    Observable<BaseEntity<MyAgentListBean>> exist(@Path("scooterId") String scooterId);

    //板车列表信息
    @POST("service-base-sysmanage/bd/list")
    Observable<BaseEntity<List<ScooterInfoListBean>>> scooterInfoList(@Body BaseFilterEntity model);

    //ULD号查询
    @POST("service-base-sysmanage/uld/list")
    Observable<BaseEntity<List<UldInfoListBean>>> uldInfoList(@Body BaseFilterEntity model);

//    //库区库位列表信息
//    @POST("service-base-sysmanage/rcPlace/list")
//    Observable<BaseEntity<List<ScooterInfoListBean>>> storehouseInfoList(@Body BaseFilterEntity model);

    //删除收运信息
    @DELETE("service-product-receivecargo/rc/delete/{id}")
    Observable<BaseEntity<Object>> deleteCollectionInfo(@Path("id") String id);

    //文件上传
    @POST("service-base-file/base/file/uploads")
    @Multipart
    Observable<BaseEntity<Object>> upLoads(@Part List<MultipartBody.Part> files);

    //货代资质
    @GET("service-base-sysmanage/freightForwardingInfo/get/{id}")
    Observable<BaseEntity<FreightInfoBean>> freightInfo(@Path("id") String Id);

    //航司资质
    @POST("service-base-sysmanage/airlineRequire/list")
    @Multipart
    Observable<BaseEntity<List<AirlineRequireBean>>> airlineRequire(@Body BaseFilterEntity model);

    //取重
    @GET("service-product-receivecargo/rc/getWeight/{pbName}")
    Observable<BaseEntity<Object>> getWeight(@Path("pbName") String pbName);

    //组板提交
    @POST("service-product-cargotallying/fightScooter/submit")
    Observable<BaseEntity<Object>> fightScooterSubmit(@Body FightScooterSubmitEntity model);

    //数据获取(获取该航班的板车/板车的收运记录/无板收运记录)
    @POST("service-product-cargotallying/fightScooter/listByFightId")
    Observable<BaseEntity<GetScooterListInfoBean>> getScooterListInfo(@Body GetScooterListInfoEntity getScooterListInfoEntity);

    /***********************复重*****************************/
    //复重数据获取
    @POST("service-product-cargoweighing/scooter/getTaskList")
    Observable<BaseEntity<List<GetInfosByFlightIdBean>>> getInfosByFlightId(@Body BaseFilterEntity model);

    //复重提交
    @POST("service-product-cargoweighing/scooter/submit")
    Observable<BaseEntity<Object>> scooterSubmit(@Body ScooterSubmitEntity model);

    //复重/获取板车信息
    @POST("service-product-cargoweighing/scooter/getScooterByScooterCode")
    Observable<BaseEntity<GetInfosByFlightIdBean>> getScooterByScooterCode(@Body BaseFilterEntity model);

    //复重/保存
    @POST("service-product-cargoweighing/scooter/saveScooter")
    Observable<BaseEntity<Object>> saveScooter(@Body GetInfosByFlightIdBean model);

    //复重/异常退回
    @POST("service-product-cargoweighing/scooter/returnWeighing")
    Observable<BaseEntity<Object>> returnWeighing(@Body ReturnWeighingEntity model);

    /***********************运输*****************************/
    //结束运输接口
    @POST("service-product-transport/tp-main-info/transportEnd")
    Observable<BaseEntity<Object>> transportEnd(@Body TransportEndEntity model);

    //开始运输接口
    @POST("service-product-transport/tp-main-info/transportBegin")
    Observable<BaseEntity<Object>> transportBegin(@Body TransportEndEntity model);

    //板车删除取消锁定接口
    @POST("service-product-transport/tp-main-info/scanScooterDelete")
    Observable<BaseEntity<Object>> scanScooterDelete(@Body TransportEndEntity model);

    //扫描板车并锁定 (添加)
    @POST("service-product-transport/tp-main-info/scanScooter")
    Observable<BaseEntity<Object>> scanScooter(@Body TransportTodoListBean model);

    //扫描板车查询
    @GET("service-product-transport/tp-main-info/scooterWithUser/{user}")
    Observable<BaseEntity<List<TransportTodoListBean>>> scooterWithUser(@Path("user") String user);


    //查询出待运输
    @GET("service-product-transport/tp-main-info/transportTodoList")
    Observable<BaseEntity<List<TransportTodoListBean>>> transportTodoList();

    //领受、运输
    @POST("service-product-transport/tp-main-info/performTaskSteps")
    Observable<BaseEntity<Object>> performTaskSteps(@Body PerformTaskStepsEntity model);

    //GPS
    @POST("service-product-transport/tp-terminal-gps/saveGpsInfo")
    Observable<BaseEntity<Object>> saveGpsInfo(@Body GpsInfoEntity model);

    //代办
    @POST("service-product-transport/tp-main-info/outFieldTodo")
    Observable<BaseEntity<List<AcceptTerminalTodoBean>>> acceptTerminalTodo(@Body BaseFilterEntity model);

    /***********************装卸机*****************************/
    //出港装机 -异常上报
    @POST("service-product-transport/tp-exception-report/exceptionReport")
    Observable<BaseEntity<Object>> exceptionReport(@Body ExceptionReportEntity exceptionReportEntity);

    //结束卸机
    @POST("service-product-transport/tp-main-info/flightUnloadInstall")
    Observable<BaseEntity<Object>> arrivalDataSave(@Body TransportEndEntity model);
    //拉货上报
    @POST("service-product-transport/tp-main-info/saveLoadPullIn")
    Observable<BaseEntity<Object>> pullGoodsReport(@Body ExceptionReportEntity model);

    //装卸机 货物扫描
    @POST("service-product-transport/tp-main-info/loadAndUnloadCarSubmit")
    Observable<BaseEntity<List<TransportTodoListBean>>> loadAndUnloadCarSubmit();

    //装机 - 装机单
    @GET("service-product-transport/tp-main-info/getFlightCargoRes/{flightId}")
    Observable<BaseEntity<List<GetFlightCargoResBean>>> getFlightCargoRes(@Path("flightId") String flightId);

    //结束装机
    @POST("service-product-transport/tp-main-info/flightDoneInstall")
    Observable<BaseEntity<Object>> flightDoneInstall(@Body GetFlightCargoResBean model);

    //装卸机代办   1是装机  2是卸机  3装卸机
    @POST("service-product-transport/tp-main-info/loadAndUnloadTodo")
    Observable<BaseEntity<List<LoadAndUnloadTodoBean>>> loadAndUnloadTodo(@Body BaseFilterEntity model);

    /***********************交货、提货*****************************/

    /****
     * 当流水号里面的对应的运单全部已提货，才能调用完成接口，否则关闭列表不调用
     * @param model
     * @return
     */

    //根据收费记录ID(流水号) 查询收费记录对应的进港运单记录
    @POST("service-product-delivery/delivery/selectInwaybillByCounterBillId")
    Observable<BaseEntity<ArrivalDeliveryInfoBean>> arrivalDeliveryInfo(@Body BaseFilterEntity model);

    //国内进港运单-出库
    @POST("service-product-delivery/delivery/deliveryInWaybill")
    Observable<BaseEntity<Object>> deliveryInWaybill(@Body BaseFilterEntity model);

    //运单出库完成
    @POST("service-product-delivery/delivery/completDelivery")
    Observable<BaseEntity<Object>> completDelivery(@Body BaseFilterEntity model);
}

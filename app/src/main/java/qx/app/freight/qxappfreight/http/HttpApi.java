package qx.app.freight.qxappfreight.http;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import qx.app.freight.qxappfreight.bean.CargoUploadBean;
import qx.app.freight.qxappfreight.bean.GetWaybillInfoByIdDataBean;
import qx.app.freight.qxappfreight.bean.GoodsIdEntity;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;
import qx.app.freight.qxappfreight.bean.OverWeightSaveResultBean;
import qx.app.freight.qxappfreight.bean.PullGoodsInfoBean;
import qx.app.freight.qxappfreight.bean.ReservoirArea;
import qx.app.freight.qxappfreight.bean.SearchFilghtEntity;
import qx.app.freight.qxappfreight.bean.SelectTaskMemberEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.ChangeWaybillEntity;
import qx.app.freight.qxappfreight.bean.request.DeclareWaybillEntity;
import qx.app.freight.qxappfreight.bean.request.ErrorFilingEntity;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.bean.request.FightScooterSubmitEntity;
import qx.app.freight.qxappfreight.bean.request.FlightIdBean;
import qx.app.freight.qxappfreight.bean.request.FlightPhotoEntity;
import qx.app.freight.qxappfreight.bean.request.GetScooterListInfoEntity;
import qx.app.freight.qxappfreight.bean.request.GpsInfoEntity;
import qx.app.freight.qxappfreight.bean.request.InPortTallyCommitEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordGetEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitNewEntity;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListRequestEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListSendEntity;
import qx.app.freight.qxappfreight.bean.request.LockScooterEntity;
import qx.app.freight.qxappfreight.bean.request.LoginEntity;
import qx.app.freight.qxappfreight.bean.request.ModifyTextEntity;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.request.PhoneParametersEntity;
import qx.app.freight.qxappfreight.bean.request.PullGoodsEntity;
import qx.app.freight.qxappfreight.bean.request.QueryContainerInfoEntity;
import qx.app.freight.qxappfreight.bean.request.QueryWaybillInfoEntity;
import qx.app.freight.qxappfreight.bean.request.ReturnWeighingEntity;
import qx.app.freight.qxappfreight.bean.request.SaveOrUpdateEntity;
import qx.app.freight.qxappfreight.bean.request.ScooterSubmitEntity;
import qx.app.freight.qxappfreight.bean.request.ScooterTransitBean;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;
import qx.app.freight.qxappfreight.bean.request.TaskClearEntity;
import qx.app.freight.qxappfreight.bean.request.TaskLockEntity;
import qx.app.freight.qxappfreight.bean.request.TodoScootersEntity;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.request.TransportListCommitEntity;
import qx.app.freight.qxappfreight.bean.request.UnLoadRequestEntity;
import qx.app.freight.qxappfreight.bean.request.UpdatePwdEntity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.AddScooterBean;
import qx.app.freight.qxappfreight.bean.response.AgentBean;
import qx.app.freight.qxappfreight.bean.response.AirlineRequireBean;
import qx.app.freight.qxappfreight.bean.response.ArrivalCargoInfoBean;
import qx.app.freight.qxappfreight.bean.response.ArrivalDeliveryInfoBean;
import qx.app.freight.qxappfreight.bean.response.AutoReservoirBean;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.BaseParamBean;
import qx.app.freight.qxappfreight.bean.response.CargoCabinData;
import qx.app.freight.qxappfreight.bean.response.CargoReportHisBean;
import qx.app.freight.qxappfreight.bean.response.ChangeStorageBean;
import qx.app.freight.qxappfreight.bean.response.DeclareApplyForRecords;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.bean.response.DocumentsBean;
import qx.app.freight.qxappfreight.bean.response.FilterTransportDateBase;
import qx.app.freight.qxappfreight.bean.response.FindAirlineAllBean;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;
import qx.app.freight.qxappfreight.bean.response.FlightBean;
import qx.app.freight.qxappfreight.bean.response.FlightInfoAndScootersBean;
import qx.app.freight.qxappfreight.bean.response.FlightInfoBean;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;
import qx.app.freight.qxappfreight.bean.response.FlightServiceBean;
import qx.app.freight.qxappfreight.bean.response.ForkliftWorkingCostBean;
import qx.app.freight.qxappfreight.bean.response.ForwardInfoBean;
import qx.app.freight.qxappfreight.bean.response.GetAllRemoteAreaBean;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;
import qx.app.freight.qxappfreight.bean.response.GetHistoryBean;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.GetQualificationsBean;
import qx.app.freight.qxappfreight.bean.response.GetScooterListInfoBean;
import qx.app.freight.qxappfreight.bean.response.GroundAgentBean;
import qx.app.freight.qxappfreight.bean.response.IOManifestBean;
import qx.app.freight.qxappfreight.bean.response.InPortResponseBean;
import qx.app.freight.qxappfreight.bean.response.InWaybillRecordBean;
import qx.app.freight.qxappfreight.bean.response.InventoryBean;
import qx.app.freight.qxappfreight.bean.response.InventoryQueryBean;
import qx.app.freight.qxappfreight.bean.response.LastReportInfoListBean;
import qx.app.freight.qxappfreight.bean.response.ListByTypeBean;
import qx.app.freight.qxappfreight.bean.response.ListWaybillCodeBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.bean.response.MarketCollectionRequireBean;
import qx.app.freight.qxappfreight.bean.response.MsMessageViewBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.NoticeBean;
import qx.app.freight.qxappfreight.bean.response.NoticeViewBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.bean.response.OverweightBean;
import qx.app.freight.qxappfreight.bean.response.PageListBean;
import qx.app.freight.qxappfreight.bean.response.PickGoodsRecordsBean;
import qx.app.freight.qxappfreight.bean.response.QueryAviationRequireBean;
import qx.app.freight.qxappfreight.bean.response.QueryContainerInfoBean;
import qx.app.freight.qxappfreight.bean.response.QueryReservoirBean;
import qx.app.freight.qxappfreight.bean.response.ReservoirBean;
import qx.app.freight.qxappfreight.bean.response.RespBean;
import qx.app.freight.qxappfreight.bean.response.RespLoginBean;
import qx.app.freight.qxappfreight.bean.response.ReturnBean;
import qx.app.freight.qxappfreight.bean.response.ScooterConfBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListDataBean;
import qx.app.freight.qxappfreight.bean.response.SearchFlightInfoBean;
import qx.app.freight.qxappfreight.bean.response.SearchReservoirBean;
import qx.app.freight.qxappfreight.bean.response.SmInventoryEntryandexit;
import qx.app.freight.qxappfreight.bean.response.TestInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.bean.response.UldInfoListBean;
import qx.app.freight.qxappfreight.bean.response.UldLikeBean;
import qx.app.freight.qxappfreight.bean.response.UnLoadListBillBean;
import qx.app.freight.qxappfreight.bean.response.UpdateVersionBean2;
import qx.app.freight.qxappfreight.bean.response.WaybillQuickGetBean;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.bean.response.WaybillsListBean;
import qx.app.freight.qxappfreight.model.ManifestBillModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * app所有接口
 */
public interface HttpApi {
    //登录接口
//    @POST("/service-base-sysmanage/auth/login")
    @POST("/service-base-sysmanage/login")
    Observable<BaseEntity<LoginResponseBean>> login(@Body LoginEntity model);

    //修改密码
    @POST("/service-base-sysmanage/auth/updatePWD")
    Observable<BaseEntity<Object>> updatePWD(@Body UpdatePwdEntity model);

    //登录一期智能调度 获取im使用 token
    @POST("userLogin")
    @FormUrlEncoded
    Observable<RespLoginBean> loginQxAi(@FieldMap Map<String, String> masp);

    //登出一期智能调度
    @POST("userLoginOut")
    @FormUrlEncoded
    Observable<RespBean> loginOutQxAi(@FieldMap Map<String, String> masp);


    //获取板车类型基础配置
    @GET("/service-bussiness-facility/baseParam/listByType/{type}")
    Observable<BaseEntity<List<ScooterConfBean.ScooterConf>>> getScooterConf(@Path("type") String type);

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

    //批量收验
    @POST("service-product-transportcheck/ins/commitList")
    Observable<BaseEntity<Object>> commitReceiveList(@Body List<StorageCommitEntity> model);

    //修改收验
    @GET("service-product-transportcheck/ins/update")
    Observable<BaseEntity<Object>> modifyTest(@Body ModifyTextEntity model);

    //收验详情
    @GET("service-product-transportcheck/ins/get/{waybillId}/{freightId}/{taskTypeCode}")
    Observable<BaseEntity<TestInfoListBean>> testInfo(@Path("waybillId") String waybillId, @Path("freightId") String freightId, @Path("taskTypeCode") String taskTypeCode);

    /***********收运****************************/

    //换单审核 - 获取数据 - guohao
    @POST("service-product-receivecargo/rc/selectChangeWaybill")
    Observable<BaseEntity<DeclareWaybillBean>> getChangeWaybill(@Body DeclareWaybillEntity declareWaybillEntity);

    //换单审核 - 提交（接受或拒绝） - guohao
    @POST("service-product-receivecargo/rc/changeSubmit")
    Observable<BaseEntity<Object>> changeSubmit(@Body ChangeWaybillEntity changeWaybillEntity);

    //收运打印
    @GET("service-product-receivecargo/rc/sendPrintMessage/{waybillId}")
    Observable<BaseEntity<Object>> sendPrintMessage(@Path("waybillId") String waybillId);

    //收运 代办
    @POST("service-product-receivecargo/todoTask/searchTodoTaskByMT")
    Observable<BaseEntity<TransportListBean>> transportList(@Body BaseFilterEntity model);

    //预配组板获取代办数据
    @POST("service-base-taskassign/todoCenter/task-todo-info/selectTodoList")
    Observable<BaseEntity<FilterTransportDateBase>> getGroupBoardToDo(@Body BaseFilterEntity model);

    //复重任务代办数据
    @POST("service-product-cargoweighing/api/selectTodoList")
    Observable<BaseEntity<FilterTransportDateBase>> getOverWeightToDo(@Body BaseFilterEntity model);

    //存储类型变更
    @POST("service-bussiness-baseparam/baseParam/list")
    Observable<BaseEntity<BaseParamBean>> baseParam(@Body BaseFilterEntity model);


    //根据存储类型 查询库区
    @POST("service-bussiness-warehouse/reservoir/list")
    Observable<BaseEntity<BaseParamBean>> baseParamType(@Body BaseFilterEntity model);

    /**
     * 收验，代办 -- pr & guohao
     *
     * @param model
     * @return
     */
    @POST("service-product-transportcheck/todoTask/searchTodoTaskByMT")
    Observable<BaseEntity<TransportListBean>> searchTodoTask(@Body BaseFilterEntity model);


    //编辑修改页面
    @GET("service-product-waybill/declare-waybill/getWayBillInfoById/{id}")
    Observable<BaseEntity<DeclareWaybillBean>> getWayBillInfoById(@Path("id") String id);

    //提交或者暂存
    @POST("service-product-receivecargo/rc/commit")
    Observable<BaseEntity<Object>> transportListCommit(@Body TransportListCommitEntity model);

    //出港退货提交
    @POST("service-product-receivecargo/rc/returnCargo")
    Observable<BaseEntity<Object>> returnCargoCommit(@Body TransportListCommitEntity model);


    //出港退货 -收运
    @POST("service-product-receivecargo/rc/list")
    Observable<BaseEntity<AgentBean>> agentTransportationList(@Body BaseFilterEntity model);

    //出港退货 -收运
    @POST("service-product-receivecargo/rc/list")
    Observable<BaseEntity<List<ReturnBean>>> returnTransportationList(@Body BaseFilterEntity model);

    //出港收货 -收运
    @POST("service-product-receivecargo/rc/list")
    Observable<BaseEntity<DeclareApplyForRecords>> changeStorageList(@Body BaseFilterEntity model);

    //存储变更 -收运
    @POST("service-product-receivecargo/rc/changeStorage")
    Observable<BaseEntity<Object>> changeStorage(@Body ChangeStorageBean model);

    // 查询库区-收运
    @POST("service-bussiness-warehouse/reservoir/list")
    Observable<BaseEntity<SearchReservoirBean>> searchReservoir(@Body BaseFilterEntity model);

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

    //查询库区
    @POST("service-bussiness-warehouse/reservoir/autoReservoir")
    Observable<BaseEntity<AutoReservoirBean>> autoReservoirv(@Body BaseFilterEntity model);

    //查询所有库区
    @POST("service-bussiness-warehouse/reservoir/list")
    Observable<BaseEntity<ReservoirBean>> reservoir(@Body BaseFilterEntity model);

    //板车列表信息
    @POST("service-bussiness-facility/bd/list")
    Observable<BaseEntity<ScooterInfoListDataBean>> scooterInfoList(@Body BaseFilterEntity model);

    //板车列表信息(收银专用)
    @POST(" /service-product-cargotallying/base-scooter/getUsableScooters")
    Observable<BaseEntity<ScooterInfoListDataBean>> scooterInfoListForReceive(@Body BaseFilterEntity model);

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

    //航空公司资质
    @GET("service-bussiness-market/collectionRequire/get/{iata}")
    Observable<BaseEntity<List<MarketCollectionRequireBean>>> freightInfo(@Path("iata") String iata);

    //获取卸机已扫板车数据
    @GET("tp-task-his/transportTaskScooterQuery/{acdmDtoId}")
    Observable<BaseEntity<List<TransportTodoListBean>>> getUnloadDoneScooter(@Path("acdmDtoId") String acdmDtoId);

    //货代资质
    @GET("service-bussiness-shipper/freightInfo/getById/{freightId}")
    Observable<BaseEntity<ForwardInfoBean>> forwardInfo(@Path("freightId") String freightId);

    //根据品名id list 获取 证明清单
    @POST("service-bussiness-cargoname/cn/commodityName/getCommdityQualificationFileBatchByIds")
    Observable<BaseEntity<List<DocumentsBean>>> getgetCommdityById(@Body GoodsIdEntity goodsNames);


    //航司资质
    @POST("service-base-sysmanage/airlineRequire/list")
    @Multipart
    Observable<BaseEntity<List<AirlineRequireBean>>> airlineRequire(@Body BaseFilterEntity model);

    //取重
    @GET("service-product-receivecargo/rc/getWeight/{pbName}")
    Observable<BaseEntity<Object>> getWeight(@Path("pbName") String pbName);


    //获取航司对应的运单前缀信息
    @GET("service-base-flight/f-flight/getAirWaybillPrefix")
    Observable<BaseEntity<Object>> getAirWaybillPrefix(@Query("iata") String iata);

    //组板提交
    @POST("service-product-cargotallying/fightScooter/submit2")
    Observable<BaseEntity<Object>> fightScooterSubmit(@Body FightScooterSubmitEntity model);

    //理货退回到预配 -- guohao
    @POST("/service-product-cargotallying/fightScooter/returnPrematching")
    Observable<BaseEntity<Object>> returnPrematching(@Body BaseFilterEntity entity);

    //新增板
    @GET("service-product-cargotallying/fightScooter/addScooter")
    Observable<BaseEntity<AddScooterBean>> addScooter();

    //数据获取(获取该航班的板车/板车的收运记录/无板收运记录)
    @POST("service-product-cargotallying/fightScooter/getTodoScooters")
    Observable<BaseEntity<GetScooterListInfoBean>> getScooterListInfo(@Body GetScooterListInfoEntity getScooterListInfoEntity);

    /***********************复重*****************************/
    //复重数据获取   ！！弃用！！
    @POST("service-product-cargoweighing/scooter/getTaskList")
    Observable<BaseEntity<List<GetInfosByFlightIdBean>>> getInfosByFlightId(@Body BaseFilterEntity model);

    //复重提交
    @POST("service-product-cargoweighing/scooter/submit")
    Observable<BaseEntity<Object>> scooterSubmit(@Body ScooterSubmitEntity model);

    //复重/获取板车信息
    @POST("service-product-cargoweighing/scooter/getScooterByScooterCode")
    Observable<BaseEntity<List<GetInfosByFlightIdBean>>> getScooterByScooterCode(@Body BaseFilterEntity model);

    // 复重板车退回 组板
    @POST("service-product-cargoweighing/scooter/returnGroupScooterTask")
    Observable<BaseEntity<Object>> returnGroupScooterTask(@Body GetInfosByFlightIdBean model);

    //复重/保存
    @POST("service-product-cargoweighing/scooter/saveScooter")
    Observable<BaseEntity<OverWeightSaveResultBean>> saveScooter(@Body GetInfosByFlightIdBean model);

    //复重/异常退回
    @POST("service-product-cargoweighing/scooter/returnWeighing")
    Observable<BaseEntity<Object>> returnWeighing(@Body ReturnWeighingEntity model);

    //复重 / 获取航班所有板车
    @POST("service-product-cargoweighing/scooter/getTodoScooters")
    Observable<BaseEntity<FlightInfoAndScootersBean>> getTodoScooters(@Body TodoScootersEntity model);

    //复重 / 获取航班舱单信息
    @POST("service-product-stowage/stowage-waybill-info/getStowagePrint")
    Observable<BaseEntity<List<ManifestBillModel>>> getManifest(@Body BaseFilterEntity model);


    //复重 / 复重历史
    @POST("service-product-cargoweighing/scooter/getHistoryScootersPage")
    Observable<BaseEntity<GetHistoryBean>> getHistoryScootersPage(@Body BaseFilterEntity model);


    //根据关键字搜索关联航班号列表
    @POST("service-base-flight/f-flight/searchFlightByParam")
    Observable<BaseEntity<List<SearchFlightInfoBean>>> searchFlightsByKey(@Body SearchFilghtEntity entity);


    /***********************运输*****************************/

    //扫描板车并锁定 (首件行李  通知运输使用)
    @POST("service-product-transport/tp-main-info/postScooterDataManual")
    Observable<BaseEntity<Object>> scanLockScooter(@Body TransportEndEntity model);

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
    @GET("service-product-transport/tp-main-info/scooterWithUser/{user}/{flightId}/{taskId}")
    Observable<BaseEntity<List<TransportTodoListBean>>> scooterWithUser(@Path("user") String user, @Path("flightId") String flightId, @Path("taskId") String taskId);

    //进入运输任务 获取该任务下可拉板车的列表
    @GET("service-product-transport/tp-main-info/scooterWithTask/{taskId}")
    Observable<BaseEntity<List<TransportTodoListBean>>> scooterWithUserForTask(@Path("taskId") String taskId);


    //查询出待运输
    @GET("service-product-transport/tp-main-info/transportTodoList")
    Observable<BaseEntity<List<TransportTodoListBean>>> transportTodoList();

    //领受、运输
    @POST("service-product-transport/tp-main-info/performTaskSteps")
    Observable<BaseEntity<Object>> performTaskSteps(@Body PerformTaskStepsEntity model);

    //装卸机确认版本
    @POST("service-product-finishloading/stowage-report-info/installedSingleConfirm")
    Observable<BaseEntity<Object>> confirmLoadPlan(@Body BaseFilterEntity model);

    //装机判断红点状态
    @POST("service-product-returngoods/pullGoods/hasUnReport")
    Observable<BaseEntity<String>> getPullStatus(@Body BaseFilterEntity model);

    //上传航班照片记录
    @POST("service-product-transport/tp-main-info/modifyTaskPic")
    Observable<BaseEntity<Object>> uploadFlightPhoto(@Body FlightPhotoEntity exceptionContent);

    //监装发起的 板车拉下
    @POST("service-product-returngoods/pullGoods/startPull")
    Observable<BaseEntity<Object>> startPull(@Body PullGoodsEntity model);


    //GPS
    @POST("service-product-transport/tp-terminal-gps/saveGpsInfo")
    Observable<BaseEntity<Object>> saveGpsInfo(@Body GpsInfoEntity model);

    //代办
    @POST("service-product-transport/tp-main-info/outFieldTodo")
    Observable<BaseEntity<List<AcceptTerminalTodoBean>>> acceptTerminalTodo(@Body BaseFilterEntity model);


    //运输 -异常结束
    @POST("service-product-transport/tp-main-info/transportEndException")
    Observable<BaseEntity<Object>> exceptionTpEnd(@Body ExceptionReportEntity exceptionReportEntity);

    /***********************装卸机*****************************/
    //发起清场任务
    @POST("service-product-transport/tp-main-info/taskAssist")
    Observable<BaseEntity<Object>> startClearTask(@Body TaskClearEntity model);

    //出港装机 -异常上报
    @POST("service-product-transport/tp-exception-report/exceptionReport")
    Observable<BaseEntity<Object>> exceptionReport(@Body ExceptionReportEntity exceptionReportEntity);

    //结束卸机
    @POST("service-product-transport/tp-main-info/flightUnloadInstall")
    Observable<BaseEntity<Object>> arrivalDataSave(@Body TransportEndEntity model);

    //拉货上报
    @GET("service-product-returngoods/pullGoods/getPullInfo/{flightInfoId}")
    Observable<BaseEntity<PullGoodsInfoBean>> getPullGoodsInfo(@Path("flightInfoId") String flightInfoId);

    //拉货上报提交
    @POST("service-product-returngoods/pullGoods/pullReport")
    Observable<BaseEntity<Object>> pullGoodsInfoCommit(@Body PullGoodsInfoBean entity);

    //获取运单信息
    @GET("service-product-waybill/declare-waybill/getWayBillInfoByCode/{waybillCode}")
    Observable<BaseEntity<GetWaybillInfoByIdDataBean>> getWayBillInfoByCode(@Path("waybillCode") String waybillCode);

    //获取运单信息
    @GET("service-product-waybill/declare-waybill/getWaybillInfo/{id}")
    Observable<BaseEntity<List<GetWaybillInfoByIdDataBean>>> getWaybillInfo(@Path("id") String id);

    //装卸机 货物扫描
    @POST("service-product-transport/tp-main-info/loadAndUnloadCarSubmit")
    Observable<BaseEntity<List<TransportTodoListBean>>> loadAndUnloadCarSubmit();

    //获取航班 舱位信息
    @POST("service-base-flight/f-flight/searchFlightAirCraftNoRsByFlightId")
    Observable<BaseEntity<CargoCabinData>> getFlightSpace(@Body FlightIdBean entity);

    //装机 - 装机单
    @POST("service-product-finishloading/stowage-report-info/getFlightCargotallyingResultByAndroid")
    Observable<LoadingListBean> getLoadingList(@Body LoadingListRequestEntity entity);

    //判断板车是否被扫描上传过
    @GET("service-product-transport/tp-main-info/checkDeviceScooterExists/{scooterCode}/{flightId}/{scSubCategory}")
    Observable<BaseEntity<Object>> checkScooterCode(@Path("scooterCode") String scooterCode, @Path("flightId") String flightId, @Path("scSubCategory") String scSubType);

    //结束装机
    @POST("service-product-transport/tp-main-info/flightDoneInstall")
    Observable<BaseEntity<Object>> flightDoneInstall(@Body GetFlightCargoResBean model);

    //发送至结载
    @POST("service-product-finishloading/stowage-report-info/installedAdviceForAndroid")
    Observable<BaseEntity<Object>> overLoad(@Body LoadingListSendEntity model);

    //装卸机代办   1是装机  2是卸机  3装卸机
    @POST("service-product-transport/tp-main-info/loadAndUnloadTodo")
    Observable<BaseEntity<List<LoadAndUnloadTodoBean>>> loadAndUnloadTodo(@Body BaseFilterEntity model);


    //发送至结载
    @POST(" service-product-finishloading/stowage-middlewar/lockOrUnlockScooter")
    Observable<BaseEntity<Object>> lockOrUnlockScooter(@Body LockScooterEntity model);

    //装卸员小组长获取可选择人员 6是装机  7是卸机  8装卸机
    @GET("service-product-transport/tp-stevedores/selectAreaStaffs/{taskId}")
    Observable<BaseEntity<List<SelectTaskMemberEntity>>> getLoadUnloadLeaderList(@Path("taskId") String taskId);

    //装卸员小组长任务代办获取  6是装机  7是卸机  8装卸机
    @POST("service-product-transport/tp-stevedores/stevedoresTaskTodo")
    Observable<BaseEntity<List<LoadAndUnloadTodoBean>>> getLoadUnloadLeaderToDo(@Body BaseFilterEntity model);

    //装卸员小组长选择任务人员  6是装机  7是卸机  8装卸机
    @POST("service-product-transport/tp-stevedores/stevedoresStaffSelect")
    Observable<BaseEntity<Object>> selectMember(@Body BaseFilterEntity model);

    //装卸员小组长任务拒绝
    @POST("service-product-transport/tp-stevedores/stevedoresTaskRefuse")
    Observable<BaseEntity<Object>> refuseTask(@Body BaseFilterEntity model);

    //结载代办
    @POST("service-product-transport/tp-main-info/passengerLoadTodo")
    Observable<BaseEntity<List<LoadAndUnloadTodoBean>>> getEndInstallTodo(@Body BaseFilterEntity model);


    //行李区行李数据提交
    @POST("service-product-transport/tp-main-info/baggageAreaSub")
    Observable<BaseEntity<Object>> baggageAreaSub(@Body RequestBody model);


    //锁定行李扫描航班
    @POST("service-base-flight/f-flight/lookLUggageScannigFlight")
    Observable<BaseEntity<Object>> lookLUggageScannigFlight(@Body BaseFilterEntity model);

    //行李转盘扫描处获取对应航班计划信息
    @POST("service-base-flight/f-flight/getDepartureFlightByAndroid")
    Observable<BaseEntity<List<FlightLuggageBean>>> getDepartureFlightByAndroid(@Body BaseFilterEntity model);

    //行李转盘扫描处获取对应航班计划信息
    @POST("service-base-flight/f-flight/getAllInternationalAndMixedFlight")
    Observable<BaseEntity<List<FlightLuggageBean>>> getAllInternationalAndMixedFlight(@Body BaseFilterEntity model);


    /***********************交货、提货*****************************/
    /****
     * 当流水号里面的对应的运单全部已提货，才能调用完成接口，否则关闭列表不调用
     * @param model
     * @return
     */

    //根据收费记录ID(流水号) 查询收费记录对应的进港运单记录
    @POST("service-product-delivery/delivery/selectInwaybillByCounterBillId")
    Observable<BaseEntity<ArrivalDeliveryInfoBean>> arrivalDeliveryInfo(@Body BaseFilterEntity model);


    //查询提货管理列表
    @POST("service-product-inwaybill/arrival-waybill/searchWaybillByWaybillCode")
    Observable<BaseEntity<WaybillsListBean>> searchWaybillByWaybillCode(@Body BaseFilterEntity model);

    //查询提货分批出库记录
    @GET("service-product-delivery/delivery/getOutboundList/{waybillId}")
    Observable<BaseEntity<List<PickGoodsRecordsBean>>> getOutboundList(@Path("waybillId") String waybillId);

    //撤销 提货分批出库记录
    @POST(" service-product-delivery/delivery/revokeInboundDelevery")
    Observable<BaseEntity<Object>> revokeInboundDelevery(@Body PickGoodsRecordsBean model);


    //查询运单超重记录
    @POST("service-product-delivery/delivery/getOverweightByWaybillId")
    Observable<BaseEntity<List<OverweightBean>>> getWaybillOverWeight(@Body BaseFilterEntity model);

    //添加运单超重记录
    @POST("service-product-delivery/delivery/addOverweightInfo")
    Observable<BaseEntity<Object>> addWaybillOverWeight(@Body List<OverweightBean> model);

    //查询运单叉车使用费记录
    @GET("service-product-delivery/forklift_trunk/listForkliftTrunkInfo/{waybillId}")
    Observable<BaseEntity<List<ForkliftWorkingCostBean>>> getWaybillForklift(@Path("waybillId") String waybillId);

    //添加运单叉车使用费记录
    @POST("service-product-delivery/forklift_trunk/savaForkliftTrunkInfo")
    Observable<BaseEntity<Object>> addWaybillForklift(@Body List<ForkliftWorkingCostBean> model);

    //删除运单超重记录
    @POST("service-product-delivery/delivery/deleteOverweightById")
    Observable<BaseEntity<Object>> deleteWaybillOverWeight(@Body OverweightBean model);

    //国内进港运单-出库
    @POST("service-product-delivery/delivery/deliveryInWaybill")
    Observable<BaseEntity<Object>> deliveryInWaybill(@Body List<BaseFilterEntity> model);

    //运单出库完成
    @POST("service-product-delivery/delivery/completDelivery")
    Observable<BaseEntity<Object>> completDelivery(@Body BaseFilterEntity model);

    //进港理货列表数据
    @POST("service-product-cargotallying/inwaybill/getList")
    Observable<BaseEntity<InPortResponseBean>> getInPortTallyList(@Body BaseFilterEntity model);

    //异常立案提交
    @POST("service-product-cargotallying/inwaybill/errorSubmit")
    Observable<BaseEntity<Object>> errorFiling(@Body ErrorFilingEntity model);

    //舱单理货列表提交
    @POST("service-product-cargotallying/inwaybill/submit")
    Observable<BaseEntity<Object>> inPortTallyCommit(@Body InPortTallyCommitEntity model);

    /**
     * 进港分拣 获取信息信息  -- guohao
     *
     * @param entity 航班业务id(UUID超级长的, 非数字id)
     * @return {
     * list : InWaybillRecord 分拣运单实体类集合
     * count : 运单总数
     * total : 总数量
     * closeFlag : 关闭标识(0开启;1关闭;) 如果为关闭状态，则不允许修改和提交和暂存
     * }
     */
    @POST("/service-product-cargotallying/sorting/getList")
    Observable<BaseEntity<InWaybillRecordBean>> getInWaybillRecrodList(@Body InWaybillRecordGetEntity entity);


    /**
     * 进港分拣 -- 暂存/提交 接口 - guohao
     *
     * @param params 整个数据
     * @return 成功/失败
     */
    @POST("/service-product-cargotallying/sorting/submit")
    Observable<BaseEntity<Object>> submitWillbillRecord(@Body InWaybillRecordSubmitNewEntity params);

    /**
     * 删除分拣信息中的某一条信息 - guohao
     *
     * @param id 分拣运单实体id
     * @return 成功/失败
     */
    @DELETE("service-product-cargotallying/sorting/deleteByIdForAndroid/{id}")
    Observable<BaseEntity<Object>> deleteInWayBillRecordById(@Path("id") String id);

    /**
     * * 进港分拣 - 通知服务器已全部到齐
     *
     * @param data 需要通知已全部到齐的数据
     * @return 成功/失败
     */
    @POST("service-product-cargotallying/sorting/saveRecord")
    Observable<WaybillQuickGetBean> allGoodsArrived(@Body InWaybillRecordSubmitNewEntity.SingleLineBean data);


    /***********************消息中心*****************************/

    //查看系统消息接口
    @POST("service-base-message/msMessage/pageList")
    Observable<BaseEntity<PageListBean>> pageList(@Body BaseFilterEntity model);

    //消息查看
    @POST("service-base-message/msMessage/view")
    Observable<BaseEntity<MsMessageViewBean>> msMessageView(@Body BaseFilterEntity model);

    //未读系统消息
    @POST("service-base-message/msMessage/noReadCount")
    Observable<BaseEntity<Object>> noReadCount(@Body PageListEntity model);

    /***********************通知公告*****************************/


    //通知公告查看
    @POST("service-base-message/bazaar-announcement-notice/findUserNoticeByPage")
    Observable<BaseEntity<NoticeBean>> findUserNoticeByPage(@Body BaseFilterEntity model);

    //查看记录
    @POST("service-base-message/bazaar-announcement-notice/view")
    Observable<BaseEntity<NoticeViewBean>> NoticeView(@Body BaseFilterEntity model);

    //通知公告未读信息
    @GET("service-base-message/bazaar-announcement-notice/noReadCount")
    Observable<BaseEntity<Object>> noReadNoticeCount(@Query("userId") String userId);

    /***********************航班动态*****************************/

    //列表
    @POST("scheduling/getFlightList")
    Observable<BaseEntity<FlightBean>> flightdynamic(@Body BaseFilterEntity model);


    //用于接收手机参数的实体
    @POST("app/userSignIn")
    Observable<BaseEntity<Object>> getPhoneParameters(@Body PhoneParametersEntity model);

    //详情
    @POST("scheduling/getFlightInfoByFlightIdForCargo")
    Observable<BaseEntity<FlightInfoBean>> flightInfo(@Body BaseFilterEntity model);

    //里程碑
    @POST("app/milepost/getMilepostDataForCargo")
    Observable<BaseEntity<FlightServiceBean>> getMilepostData(@Body BaseFilterEntity model);

    /*********************清库***********************************/

    //清库列表
    @POST("service-bussiness-warehouse/inventory/listInventoryTaskByPage")
    Observable<BaseEntity<InventoryQueryBean>> inventoryQuery(@Body BaseFilterEntity entity);

    //清库提交
    @POST("service-bussiness-warehouse/inventory/addInventoryDetail")
    Observable<BaseEntity> addInventoryDetail(@Body List<InventoryDetailEntity> entity);

    //清库详情
    @POST("service-bussiness-warehouse/inventory/listInventoryDetail")
    Observable<BaseEntity<List<InventoryDetailEntity>>> listInventoryDetail(@Body BaseFilterEntity entity);

    //清库运单模糊查询
    @GET("service-bussiness-warehouse/inventory/listWaybillCode")
    Observable<ListWaybillCodeBean> listWaybillCode(@Query("code") String code, @Query("inventoryTaskId") String inventoryTaskId);

    //从服务器 获取 运单号
    @GET("service-product-inwaybill/arrival-waybill/generateWBQWaybillCode")
    Observable<BaseEntity<Object>> getWaybillCode();

    /*********************国际货物***************************/

    @POST(" service-product-transport/tp-main-info/internationalCargoReport")
    Observable<BaseEntity<Object>> internationalCargoReport(@Body CargoUploadBean model);

    //版本更新检测
    @POST("app/scheduling/findVersionUpdate")
    Call<UpdateVersionBean2> updateVersion(@Query("deviceType") String deviceType);

    /**
     * 获取库区接口
     *
     * @param deptCode 从用户信息里面获取
     * @return
     */
    @GET("service-bussiness-warehouse/reservoir/listReservoirInfoByCode/{deptCode}")
    Observable<BaseEntity<List<ReservoirArea>>> listReservoirInfoByCode(@Path("deptCode") String deptCode);

    //xxx
    @GET("service-product-transport/tp-api/getAllRemoteArea")
    Observable<BaseEntity<List<GetAllRemoteAreaBean>>> getAllRemoteArea();

    //获取卸机单数据
    @POST("service-product-finishloading/stowage-report-info/queryUnloadingMachine")
    Observable<UnLoadListBillBean> getUnLoadingList(@Body UnLoadRequestEntity model);

    /**
     * 待办 锁定
     *
     * @param entity
     * @return
     */
    @POST("service-base-taskassign/taskHandler/taskLock")
    Observable<BaseEntity<Object>> taskLock(@Body TaskLockEntity entity);

    /**
     * 模糊搜索ULD号
     *
     * @param entity
     * @return
     */
    @POST("/service-bussiness-facility/uld/likePage")
    Observable<BaseEntity<UldLikeBean>> likePage(@Body BaseFilterEntity entity);

    /**
     * 获取基础模糊参数类型
     *
     * @return
     */
    @POST("service-bussiness-facility/baseParam/likeByType")
    Observable<BaseEntity<ListByTypeBean>> listByType(@Body BaseFilterEntity entity);

    /**
     * 添加或保存ULD集装箱
     *
     * @return
     */
    @POST("service-bussiness-facility/uld/saveOrUpdate")
    Observable<BaseEntity<Object>> saveOrUpdate(@Body SaveOrUpdateEntity entity);

    /**
     * 获取基础参数类型
     *
     * @return
     */
    @GET("service-base-flight/f-flight/configCenterData/findAirlineAll")
    Observable<BaseEntity<List<FindAirlineAllBean>>> findAirlineAll();

    /**
     * 通知录入
     *
     * @return
     */
    @POST("service-product-finishloading/stowage-report-info/synchronousLoadingByFlightId")
    Observable<BaseEntity<Object>> synchronousLoading(@Body BaseFilterEntity entity);

    /**
     * 释放
     *
     * @return
     */
    @POST("service-product-finishloading/stowage-report-info/releaseFlightByFlightId")
    Observable<BaseEntity<Object>> auditManifest(@Body BaseFilterEntity entity);

    /**
     * 重新写入离港系统
     *
     * @return
     */
    @POST("service-product-finishloading/stowage-report-info/repartWriteLoadingInfo")
    Observable<BaseEntity<Object>> repartWriteLoading(@Body BaseFilterEntity entity);

    /**
     * 获取装机单或货邮舱单数据
     *
     * @return
     */
    @POST("service-product-finishloading/stowage-report-info/getFlightAllReportInfoByAndroid")
    Observable<BaseEntity<List<FlightAllReportInfo>>> getFlightAllReportInfo(@Body BaseFilterEntity entity);

    @POST("service-product-finishloading/stowage-report-info/getLastReportInfo")
    Observable<BaseEntity<LastReportInfoListBean>> getLastReportInfo(@Body BaseFilterEntity entity);

    /**
     * 重新开启装机任务
     *
     * @return
     */
    @POST("service-product-transport/tp-main-info/restartUnloadTask")
    Observable<BaseEntity<Object>> reOpenLoadTask(@Body BaseFilterEntity entity);


    /****************************已办***********************************/

    //货物上报已办
    @POST("service-product-transport/tp-task-his/cargoReportHis")
    Observable<BaseEntity<List<CargoReportHisBean>>> cargoReportHis(@Body BaseFilterEntity operatorId);

    //行李上报已办
    @POST("service-product-transport/tp-task-his/baggageSubHis")
    Observable<BaseEntity<List<CargoReportHisBean>>> baggageSubHis(@Body BaseFilterEntity operatorId);

    //截载已办
    @POST("service-product-transport/tp-task-his/reportTaskHis")
    Observable<BaseEntity<List<LoadAndUnloadTodoBean>>> reportTaskHis(@Body BaseFilterEntity operatorId);

    //运输已办
    @POST("service-product-transport/tp-task-his/transportTaskHis")
    Observable<BaseEntity<List<OutFieldTaskBean>>> transportTaskHis(@Body BaseFilterEntity operatorId);

    //装卸员已办
    @POST("service-product-transport/tp-task-his/stevedoresTaskHis")
    Observable<BaseEntity<List<LoadAndUnloadTodoBean>>> stevedoresTaskHis(@Body BaseFilterEntity operatorId);

    //监装监卸已办
    @POST("service-product-transport/tp-task-his/loadUnloadTaskHis")
    Observable<BaseEntity<List<LoadAndUnloadTodoBean>>> loadUnloadTaskHis(@Body BaseFilterEntity operatorId);

    //异常捕获 上传服务器
    @POST("service-product-transport/tp-main-info/exceptionUpload")
    Observable<BaseEntity<Object>> exceptionContent(@Body BaseFilterEntity exceptionContent);

    //手持设备打印舱单/装机单
    @POST("service-product-finishloading/stowage-report-info/printRequest")
    Observable<BaseEntity<Object>> printRequest(@Body BaseFilterEntity exceptionContent);

    /****************************库区管理***********************************/

    //获取 出入库列表
    @POST("service-bussiness-warehouse/storage/pageByType")
    Observable<BaseEntity<IOManifestBean>> getIOManifestList(@Body BaseFilterEntity entity);

    //获取 库存列表
    @POST("service-bussiness-warehouse/storage/listAreaInfo")
    Observable<BaseEntity<InventoryBean>> getInventory(@Body BaseFilterEntity entity);

    //提交出库入库 单
    @POST("service-bussiness-warehouse/storage/affirmInventory")
    Observable<BaseEntity<Object>> submitIOManifestList(@Body SmInventoryEntryandexit entity);

    //根据运单号查数据库，如果库里有此运单号，返回的运单数据
    @POST("service-product-cargotallying/sorting/getCargoInfoByWaybillCode")
    Observable<BaseEntity<InWaybillRecordSubmitNewEntity.SingleLineBean>> getWaybillInfoByCode(@Body QueryWaybillInfoEntity entity);

    /**
     * 获取运单 基本信息
     *
     * @return
     */
    @GET("service-bussiness-warehouse/inventory/getWaybillInfoByWaybillCode/{waybillId}")
    Observable<BaseEntity<WaybillsBean>> getWaybillInfoByWaybillCode(@Path("waybillId") String waybillCode);

    /**
     * 获取地面代理列表接口详情
     * @return
     */
    @GET(" service-bussiness-shipper/ground-agent/getAll")
    Observable<BaseEntity<List<GroundAgentBean>>> getAllAgent();
    /**
     * 新增基础板车接口详情
     *
     * @return
     */
    @POST("/service-bussiness-facility/bd/saveOrUpdate")
    Observable<BaseEntity<Object>> newScooter(@Body ScooterTransitBean entity);
}

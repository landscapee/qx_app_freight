package qx.app.freight.qxappfreight.utils.httpUtils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import qx.app.freight.qxappfreight.bean.CargoUploadBean;
import qx.app.freight.qxappfreight.bean.GetWaybillInfoByIdDataBean;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;
import qx.app.freight.qxappfreight.bean.PullGoodsInfoBean;
import qx.app.freight.qxappfreight.bean.ReservoirArea;
import qx.app.freight.qxappfreight.bean.SelectTaskMemberEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.ChangeWaybillEntity;
import qx.app.freight.qxappfreight.bean.request.DeclareWaybillEntity;
import qx.app.freight.qxappfreight.bean.request.ErrorFilingEntity;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.bean.request.FightScooterSubmitEntity;
import qx.app.freight.qxappfreight.bean.request.GetScooterListInfoEntity;
import qx.app.freight.qxappfreight.bean.request.GpsInfoEntity;
import qx.app.freight.qxappfreight.bean.request.GroupBoardRequestEntity;
import qx.app.freight.qxappfreight.bean.request.InPortTallyCommitEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordGetEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitEntity;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListRequestEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListSendEntity;
import qx.app.freight.qxappfreight.bean.request.LoginEntity;
import qx.app.freight.qxappfreight.bean.request.ModifyTextEntity;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.request.PhoneParametersEntity;
import qx.app.freight.qxappfreight.bean.request.QueryContainerInfoEntity;
import qx.app.freight.qxappfreight.bean.request.ReturnWeighingEntity;
import qx.app.freight.qxappfreight.bean.request.SaveOrUpdateEntity;
import qx.app.freight.qxappfreight.bean.request.ScooterSubmitEntity;
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
import qx.app.freight.qxappfreight.bean.response.ArrivalDeliveryInfoBean;
import qx.app.freight.qxappfreight.bean.response.AutoReservoirBean;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.BaseParamBean;
import qx.app.freight.qxappfreight.bean.response.CargoReportHisBean;
import qx.app.freight.qxappfreight.bean.response.ChangeStorageBean;
import qx.app.freight.qxappfreight.bean.response.DeclareApplyForRecords;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.bean.response.FindAirlineAllBean;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;
import qx.app.freight.qxappfreight.bean.response.FlightBean;
import qx.app.freight.qxappfreight.bean.response.FlightInfoBean;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;
import qx.app.freight.qxappfreight.bean.response.FlightServiceBean;
import qx.app.freight.qxappfreight.bean.response.ForwardInfoBean;
import qx.app.freight.qxappfreight.bean.response.GetAllRemoteAreaBean;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;
import qx.app.freight.qxappfreight.bean.response.GetHistoryBean;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.GetQualificationsBean;
import qx.app.freight.qxappfreight.bean.response.GetScooterListInfoBean;
import qx.app.freight.qxappfreight.bean.response.InPortResponseBean;
import qx.app.freight.qxappfreight.bean.response.InWaybillRecordBean;
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
import qx.app.freight.qxappfreight.bean.response.QueryAviationRequireBean;
import qx.app.freight.qxappfreight.bean.response.QueryContainerInfoBean;
import qx.app.freight.qxappfreight.bean.response.QueryReservoirBean;
import qx.app.freight.qxappfreight.bean.response.ReservoirBean;
import qx.app.freight.qxappfreight.bean.response.RespBean;
import qx.app.freight.qxappfreight.bean.response.RespLoginBean;
import qx.app.freight.qxappfreight.bean.response.ReturnBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListDataBean;
import qx.app.freight.qxappfreight.bean.response.SearchReservoirBean;
import qx.app.freight.qxappfreight.bean.response.TestInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.bean.response.UldInfoListBean;
import qx.app.freight.qxappfreight.bean.response.UldLikeBean;
import qx.app.freight.qxappfreight.bean.response.UnLoadListBillBean;
import qx.app.freight.qxappfreight.constant.HttpConstant;
import qx.app.freight.qxappfreight.http.HttpApi;
import qx.app.freight.qxappfreight.model.ManifestBillModel;

public class UpdateRepository extends BaseRepository {
    private volatile static UpdateRepository instance;
    private volatile static HttpApi mUpdateApis;
    private volatile static HttpApi mUpdateApisFlight;
    private volatile static HttpApi mUpdateApisQxAi;

    private UpdateRepository() {
        //基类解析
        RetrofitFactory factory = RetrofitHelper.getRetrofit(HttpConstant.TEST);
        mUpdateApis = factory.getApiService(HttpApi.class);
        RetrofitFactory factoryQxAi = RetrofitHelper.getRetrofit(HttpConstant.QXAITEST);
        mUpdateApisQxAi = factoryQxAi.getApiService(HttpApi.class);
        RetrofitFactory factoryFlight = RetrofitHelper.getRetrofit(HttpConstant.HUOYUN);
        mUpdateApisFlight = factoryFlight.getApiService(HttpApi.class);
    }

    public HttpApi getService() {
        return mUpdateApis;
    }

    public HttpApi getServiceQxAi() {
        return mUpdateApisQxAi;
    }

    public static UpdateRepository getInstance() {
        if (instance == null) {
            synchronized (UpdateRepository.class) {
                if (instance == null) {
                    instance = new UpdateRepository();
                }
            }
        }
        return instance;
    }

    /****
     * 登录
     * @param loginEntity
     * @return
     */
    public Observable<LoginResponseBean> login(LoginEntity loginEntity) {
        return transform(getService().login(loginEntity));
    }

    /****
     * 登录
     * @param loginEntity
     * @return
     */
    public Observable<String> updatePWD(UpdatePwdEntity loginEntity) {
        return nothingtransform(getService().updatePWD(loginEntity));
    }

    /****
     * 登录智能调度一期
     * @param
     * @return
     */
    public Observable<RespLoginBean> loginQxAi(Map<String, String> map) {
        return nothingDatatransformForOneDisP(getServiceQxAi().loginQxAi(map));
    }

    /****
     * 登出智能调度一期
     * @param
     * @return
     */
    public Observable<RespBean> loginOutQxAi(Map<String, String> map) {
        return nothingDatatransformForOneDisPOut(getServiceQxAi().loginOutQxAi(map));
    }


    /*****
     * @param
     * @return
     */
    public Observable<AgentBean> agentTransportationList(BaseFilterEntity param) {
        return transform(getService().agentTransportationList(param));
    }

    /*****
     * @param
     * @return
     */
    public Observable<List<ReturnBean>> returnTransportationList(BaseFilterEntity param) {
        return transform(getService().returnTransportationList(param));
    }

    /*****
     * @param
     * @return
     */
    public Observable<DeclareApplyForRecords> changeStorageList(BaseFilterEntity param) {
        return transform(getService().changeStorageList(param));
    }

    /*****
     * 存储变更
     * @param
     * @return
     */
    public Observable<String> changeStorage(ChangeStorageBean param) {
        return nothingtransform(getService().changeStorage(param));
    }

    /*****
     * 库区查询
     * @param
     * @return
     */
    public Observable<SearchReservoirBean> searchReservoir(BaseFilterEntity param) {
        return transform(getService().searchReservoir(param));
    }

    /***
     *获取货代公司资质
     * @param freightCode:
     * 货代编码
     * @return
     */
    public Observable<GetQualificationsBean> getQualifications(String freightCode) {
        return transform(getService().getQualifications(freightCode));
    }

    /***
     * 查询航空公司要求
     * @param airLineId:
     * 航空公司Id 或者 航空公司编码
     * @return
     */
    public Observable<List<QueryAviationRequireBean>> getQueryAviationRequire(String airLineId) {
        return transform(getService().getQueryAviationRequire(airLineId));
    }

    /*****
     *暂存提交
     * @param storageCommitEntity
     * @return
     * 是否成功信息
     */
    public Observable<String> storageCommit(StorageCommitEntity storageCommitEntity) {
        return nothingtransform(getService().storageCommit(storageCommitEntity));
    }


    /****
     * 收验批量提交
     */
    public Observable<String> commitReceiveList(List<StorageCommitEntity> storageCommitEntity) {
        return nothingtransform(getService().commitReceiveList(storageCommitEntity));
    }

    /******
     * 修改收验
     * @param modifyTextEntity
     * @return
     * 是否成功信息
     */
    public Observable<String> modifyTest(ModifyTextEntity modifyTextEntity) {
        return nothingtransform(getService().modifyTest(modifyTextEntity));
    }

    /******
     * 收验详情
     * @param waybillId
     * @return
     */
    public Observable<TestInfoListBean> testInfo(String waybillId, String freightId, String mTaskTypeCode) {
        return transform(getService().testInfo(waybillId, freightId, mTaskTypeCode));
    }

    /****
     * 查询集装箱列表信息
     * @param queryContainerInfoEntity
     * @return
     */
    public Observable<List<QueryContainerInfoBean>> queryContainerInfo(QueryContainerInfoEntity queryContainerInfoEntity) {
        return transform(getService().queryContainerInfo(queryContainerInfoEntity));
    }

    /***
     *查询库区类别
     * @return
     */
    public Observable<List<QueryReservoirBean>> queryReservoir() {
        return transform(getService().queryReservoir());
    }

    /*****
     * 添加收运信息
     * @param myAgentListBean
     * @return
     */
    public Observable<MyAgentListBean> addInfo(MyAgentListBean myAgentListBean) {
        return transform(getService().addInfo(myAgentListBean));
    }


    /*****
     * 判断板车是否被占用
     * @param scooterId
     * @return
     */
    public Observable<MyAgentListBean> exist(String scooterId) {
        return transform(getService().exist(scooterId));
    }

    /****
     * 查询库区
     * @param model
     * @return
     */
    public Observable<AutoReservoirBean> autoReservoirv(BaseFilterEntity model) {
        return transform(getService().autoReservoirv(model));
    }

    /****
     * 查询所有库区
     * @param model
     * @return
     */
    public Observable<ReservoirBean> reservoir(BaseFilterEntity model) {
        return transform(getService().reservoir(model));
    }

    /****
     * 删除收运信息
     * @param id
     * @return
     */
    public Observable<String> deleteCollectionInfo(String id) {
        return nothingtransform(getService().deleteCollectionInfo(id));
    }


    /********
     * 代办收运列表
     * @param model
     * @return
     */
    public Observable<TransportListBean> transportList(BaseFilterEntity model) {
        return transform(getService().transportList(model));
    }

    /********
     * 代办收运列表
     * @param model
     * @return
     */
    public Observable<List<TransportDataBase>> getGroupBoardToDo(GroupBoardRequestEntity model) {
        return transform(getService().getGroupBoardToDo(model));
    }

    /********
     * 代办收运列表
     * @param model
     * @return
     */
    public Observable<BaseParamBean> baseParam(BaseFilterEntity model) {
        return transform(getService().baseParam(model));
    }


    /********
     * 代验收运列表
     * @param model
     * @return
     */
    public Observable<TransportListBean> searchTodoTask(BaseFilterEntity model) {
        return transform(getService().searchTodoTask(model));
    }


    /**
     * 收运编辑提交页面
     *
     * @param id
     * @return
     */

    public Observable<DeclareWaybillBean> getWayBillInfoById(String id) {
        return transform(getService().getWayBillInfoById(id));
    }

    /*****
     * 文件上传
     * @param files
     * @return
     */
    public Observable<Object> upLoads(List<MultipartBody.Part> files) {
        return transform(getService().upLoads(files));
    }

    /***
     * 提交或暂存
     * @param transportListCommitEntity
     * @return
     */
    public Observable<String> transportListCommit(TransportListCommitEntity transportListCommitEntity) {
        return nothingtransform(getService().transportListCommit(transportListCommitEntity));
    }

    /***
     * 出港退货提交
     * @param transportListCommitEntity
     * @return
     */
    public Observable<String> returnCargoCommit(TransportListCommitEntity transportListCommitEntity) {
        return nothingtransform(getService().returnCargoCommit(transportListCommitEntity));
    }

    /****
     * 板车信息查询
     * @param baseFilterEntity
     * @return
     */
    public Observable<ScooterInfoListDataBean> scooterInfoList(BaseFilterEntity baseFilterEntity) {
        return transform(getService().scooterInfoList(baseFilterEntity));
    }

    /****
     * 板车信息查询 (收运专用)
     * @param baseFilterEntity
     * @return
     */
    public Observable<ScooterInfoListDataBean> scooterInfoListForReceive(BaseFilterEntity baseFilterEntity) {
        return transform(getService().scooterInfoListForReceive(baseFilterEntity));
    }

    /***
     * uld号查询
     * @param baseFilterEntity
     * @return
     */
    public Observable<List<UldInfoListBean>> uldInfoList(BaseFilterEntity baseFilterEntity) {
        return transform(getService().uldInfoList(baseFilterEntity));
    }

    /****
     * 航司资质
     * @param iata
     * @return
     */
    public Observable<List<MarketCollectionRequireBean>> freightInfo(String iata) {
        return transform(getService().freightInfo(iata));
    }

    /****
     * 货代资质
     * @param freightId
     * @return
     */

    public Observable<ForwardInfoBean> forwardInfo(String freightId) {
        return transform(getService().forwardInfo(freightId));
    }

    /****
     * 航司资质
     * @param model
     * @return
     */
    public Observable<List<AirlineRequireBean>> airlineRequire(BaseFilterEntity model) {
        return transform(getService().airlineRequire(model));
    }

    /*****
     * 组板提交
     * @param model
     * @return
     */
    public Observable<String> fightScooterSubmit(FightScooterSubmitEntity model) {
        return nothingtransform(getService().fightScooterSubmit(model));
    }

    /***
     * 新增板车
     * @return
     */
    public Observable<AddScooterBean> addScooter() {
        return transform(getService().addScooter());
    }

    /*****
     * 组数据获取
     * @param getScooterListInfoEntity
     * @return
     */
    public Observable<GetScooterListInfoBean> getScooterListInfo(GetScooterListInfoEntity getScooterListInfoEntity) {
        return transform(getService().getScooterListInfo(getScooterListInfoEntity));
    }

    /****
     * 取重
     * @param pbName
     * @return
     */
    public Observable<String> getWeight(String pbName) {
        return nothingDatatransform(getService().getWeight(pbName));
    }

    /****
     * 取重
     * @param iata
     * @return
     */
    public Observable<String> getAirWaybillPrefix(String iata) {
        return nothingDatatransform(getService().getAirWaybillPrefix(iata));
    }

    /****
     * 结束运输接口
     * @param model
     * @return
     */
    public Observable<String> transportEnd(TransportEndEntity model) {
        return nothingtransform(getService().transportEnd(model));
    }

    /****
     * 开始运输接口
     * @param model
     * @return
     */
    public Observable<String> transportBegin(TransportEndEntity model) {
        return nothingtransform(getService().transportBegin(model));
    }

    /*****
     * 板车删除取消锁定接口
     * @param model
     * @return
     */
    public Observable<String> scanScooterDelete(TransportEndEntity model) {
        return nothingtransform(getService().scanScooterDelete(model));
    }

    /*****
     * 扫描板车并锁定 (添加)
     * @param model
     * @return
     */
    public Observable<String> scanScooter(TransportTodoListBean model) {
        return nothingtransform(getService().scanScooter(model));
    }

    /*****
     * 扫描板车并锁定 (添加)
     * @param model
     * @return
     */
    public Observable<String> scanLockScooter(TransportEndEntity model) {
        return nothingtransform(getService().scanLockScooter(model));
    }

    /****
     * 扫描板车查询
     * @param model
     * @return
     */
    public Observable<List<TransportTodoListBean>> scooterWithUser(String model, String flightId) {
        return transform(getService().scooterWithUser(model, flightId));
    }

    /****
     * 扫描板车查询
     * @param taskId
     * @return
     */
    public Observable<List<TransportTodoListBean>> scooterWithUserTask(String taskId) {
        return transform(getService().scooterWithUserForTask(taskId));
    }

    /*****
     * 查询出待运输的
     * @return
     */
    public Observable<List<TransportTodoListBean>> transportTodoList() {
        return transform(getService().transportTodoList());
    }


    /****
     * 领受、到位、开始、结束
     * @param performTaskStepsEntity
     * @return
     */
    public Observable<String> performTaskSteps(PerformTaskStepsEntity performTaskStepsEntity) {
        return nothingtransform(getService().performTaskSteps(performTaskStepsEntity));
    }
    /****
     * 装卸机人员确认装机单版本建议
     * @return
     */
    public Observable<String> confirmLoadPlan(BaseFilterEntity entity) {
        return nothingtransform(getService().confirmLoadPlan(entity));
    }
    /****
     * 装机页面判断红点
     * @return
     */
    public Observable<BaseEntity<String>> getPullStatus(BaseFilterEntity entity) {
        return getService().getPullStatus(entity);
    }

    /*****
     * GPs
     * @param gpsInfoEntity
     * @return
     */
    public Observable<String> saveGpsInfo(GpsInfoEntity gpsInfoEntity) {
        return nothingtransform(getService().saveGpsInfo(gpsInfoEntity));
    }

    /****
     * 外场运输待办
     * @return
     */
    public Observable<List<AcceptTerminalTodoBean>> acceptTerminalTodo(BaseFilterEntity model) {
        return transform(getService().acceptTerminalTodo(model));
    }

    /***
     * 运输 异常结束
     * @param exceptionReportEntity
     * @return
     */
    public Observable<String> exceptionTpEnd(ExceptionReportEntity exceptionReportEntity) {
        return nothingtransform(getService().exceptionTpEnd(exceptionReportEntity));
    }

    /***
     * 异常上报
     * @param exceptionReportEntity
     * @return
     */
    public Observable<String> exceptionReport(ExceptionReportEntity exceptionReportEntity) {
        return nothingtransform(getService().exceptionReport(exceptionReportEntity));
    }

    /****
     * 结束卸机
     * @param transportEndEntity
     * @return
     */
    public Observable<String> arrivalDataSave(TransportEndEntity transportEndEntity) {
        return nothingtransform(getService().arrivalDataSave(transportEndEntity));
    }

    /****
     * 发起清场任务
     * @param taskClearEntity
     * @return
     */
    public Observable<String> startClearTask(TaskClearEntity taskClearEntity) {
        return nothingtransform(getService().startClearTask(taskClearEntity));
    }

    /****
     * 拉货上报获取需要拉回的板车和运单数据
     * @param flightInfoId
     * @return
     */
    public Observable<PullGoodsInfoBean> getPullGoodsInfo(String flightInfoId) {
        return transform(getService().getPullGoodsInfo(flightInfoId));
    }

    /****
     * 拉货上报修改需要拉回的板车和运单数据后进行提交
     * @param entity
     * @return
     */
    public Observable<String> pullGoodsInfoCommit(PullGoodsInfoBean entity) {
        return nothingtransform(getService().pullGoodsInfoCommit(entity));
    }

    /****
     * 拉货上报
     * @return
     */
    public Observable<GetWaybillInfoByIdDataBean> getWayBillInfoByCode(String waybillCode) {
        return transform(getService().getWayBillInfoByCode(waybillCode));
    }

    /****
     * 拉货上报
     * @return
     */
    public Observable<List<GetWaybillInfoByIdDataBean>> getWaybillInfo(String id) {
        return transform(getService().getWaybillInfo(id));
    }

    /**
     * 获取装机单数据
     *
     * @param entity 请求参数
     * @return
     */
    public Observable<LoadingListBean> getLoadingList(LoadingListRequestEntity entity) {
        return getService().getLoadingList(entity);
    }

    /*****
     * 结束装机
     * @param model
     * @return
     */
    public Observable<String> flightDoneInstall(GetFlightCargoResBean model) {
        return nothingtransform(getService().flightDoneInstall(model));
    }

    /*****
     * 结束装机
     * @param model
     * @return
     */
    public Observable<String> overLoad(LoadingListSendEntity model) {
        return nothingtransform(getService().overLoad(model));
    }


    /****
     * 装卸机代办
     * @param model
     * @return
     */
    public Observable<List<LoadAndUnloadTodoBean>> loadAndUnloadTodo(BaseFilterEntity model) {
        return transform(getService().loadAndUnloadTodo(model));
    }

    /****
     * 装卸员小组长任务代办
     * @param  taskId
     * @return
     */
    public Observable<List<SelectTaskMemberEntity>> getLoadUnloadLeaderList(String taskId) {
        return transform(getService().getLoadUnloadLeaderList(taskId));
    }

    /****
     * 装卸员小组长选择任务人员
     * @param  baseFilterEntity
     * @return
     */
    public Observable<String> selectMember(BaseFilterEntity baseFilterEntity) {
        return nothingtransform(getService().selectMember(baseFilterEntity));
    }

    /****
     * 装卸员小组长接收新任务拒绝任务
     * @param  baseFilterEntity
     * @return
     */
    public Observable<String> refuseTask(BaseFilterEntity baseFilterEntity) {
        return nothingtransform(getService().refuseTask(baseFilterEntity));
    }

    /****
     * 装卸员小组长任务代办列表
     * @param  baseFilterEntity
     * @return
     */
    public Observable<List<LoadAndUnloadTodoBean>> getLoadUnloadLeaderToDo(BaseFilterEntity baseFilterEntity) {
        return transform(getService().getLoadUnloadLeaderToDo(baseFilterEntity));
    }

    /****
     * 结载代办
     * @return
     */
    public Observable<List<LoadAndUnloadTodoBean>> getEndInstallTodo(BaseFilterEntity model) {
        return transform(getService().getEndInstallTodo(model));
    }

    /****
     * 行李区行李数据提交
     * @param string
     * @return
     */
    public Observable<String> baggageAreaSub(String string) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                string);
        return nothingtransform(getService().baggageAreaSub(requestBody));
    }

    /****
     * 行李转盘扫描处获取对应航班计划信息
     * @param model
     * @return
     */
    public Observable<String> lookLUggageScannigFlight(BaseFilterEntity model) {
        return nothingtransform(getService().lookLUggageScannigFlight(model));
    }

    /****
     * 锁定行李扫描航班
     * @param model
     * @return
     */
    public Observable<List<FlightLuggageBean>> getDepartureFlightByAndroid(BaseFilterEntity model) {
        return transform(getService().getDepartureFlightByAndroid(model));
    }

    /****
     * 锁定行李扫描航班
     * @return
     */
    public Observable<List<FlightLuggageBean>> getAllInternationalAndMixedFlight(BaseFilterEntity model) {
        return transform(getService().getAllInternationalAndMixedFlight(model));
    }

    /*****
     * 复重数据获取
     * @param model
     * @return
     */
    public Observable<List<GetInfosByFlightIdBean>> getInfosByFlightId(BaseFilterEntity model) {
        return transform(getService().getInfosByFlightId(model));
    }

    /****
     * 复重提交
     * @param model
     * @return
     */
    public Observable<String> scooterSubmit(ScooterSubmitEntity model) {
        return nothingtransform(getService().scooterSubmit(model));
    }

    /****
     * 复重/获取板车信息
     * @param model
     * @return
     */
    public Observable<GetInfosByFlightIdBean> getScooterByScooterCode(BaseFilterEntity model) {
        return transform(getService().getScooterByScooterCode(model));
    }

    /****
     * 复重/保存
     * @param model
     * @return
     */
    public Observable<String> saveScooter(GetInfosByFlightIdBean model) {
        return nothingtransform(getService().saveScooter(model));
    }

    /****
     * 复重/异常退回
     * @param model
     * @return
     */
    public Observable<String> returnWeighing(ReturnWeighingEntity model) {
        return nothingtransform(getService().returnWeighing(model));
    }


    /****
     * 复重 / 获取航班所有板车
     * @param model
     * @return
     */
    public Observable<List<GetInfosByFlightIdBean>> getTodoScooters(TodoScootersEntity model) {
        return transform(getService().getTodoScooters(model));
    }

    /****
     * //复重 / 复重历史
     * @param model
     * @return
     */
    public Observable<GetHistoryBean> getHistoryScootersPage(BaseFilterEntity model) {
        return transform(getService().getHistoryScootersPage(model));
    }

    /****
     * 复重 / 获取航班所有板车
     * @param model
     * @return
     */
    public Observable<List<ManifestBillModel>> getManifest(BaseFilterEntity model) {
        return transform(getService().getManifest(model));
    }


    /*****
     * 根据收费记录ID(流水号) 查询收费记录对应的进港运单记录
     * @param model
     * @return
     */
    public Observable<ArrivalDeliveryInfoBean> arrivalDeliveryInfo(BaseFilterEntity model) {
        return transform(getService().arrivalDeliveryInfo(model));
    }

    /****
     * 进港-出库
     * @param model
     * @return
     */
    public Observable<String> deliveryInWaybill(BaseFilterEntity model) {
        return nothingtransform(getService().deliveryInWaybill(model));
    }

    /****
     * 进港-查询出库运单超重
     * @param model
     * @return
     */
    public Observable<List<OverweightBean>> getOverweight(BaseFilterEntity model) {
        return transform(getService().getWaybillOverWeight(model));
    }

    /****
     * 进港-添加超重记录
     * @param model
     * @return
     */
    public Observable<String> addOverweight(List<OverweightBean> model) {
        return nothingtransform(getService().addWaybillOverWeight(model));
    }

    /****
     * 进港-删除超重记录
     * @param model
     * @return
     */
    public Observable<String> deleteOverweight(OverweightBean model) {
        return nothingtransform(getService().deleteWaybillOverWeight(model));
    }

    /***
     * 出港-完成
     * @param model
     * @return
     */
    public Observable<String> completDelivery(BaseFilterEntity model) {
        return nothingtransform(getService().completDelivery(model));
    }

    /***
     * 异常立案
     * @param model
     * @return
     */
    public Observable<String> errorFiling(ErrorFilingEntity model) {
        return nothingtransform(getService().errorFiling(model));
    }

    /***
     * 获取进港理货列表数据
     * @param model
     * @return
     */
    public Observable<InPortResponseBean> getInPortTallyList(BaseFilterEntity model) {
        return transform(getService().getInPortTallyList(model));
    }

    /***
     * 进港理货列表数据提交
     * @param model
     * @return
     */
    public Observable<String> inPortTallyCommit(InPortTallyCommitEntity model) {
        return nothingtransform(getService().inPortTallyCommit(model));
    }


    /*****
     * 查看系统消息接口
     * @param model
     * @return
     */
    public Observable<PageListBean> pageList(BaseFilterEntity model) {
        return transform(getService().pageList(model));
    }

    /****
     * 消息查看
     * @param model
     * @return
     */
    public Observable<MsMessageViewBean> msMessageView(BaseFilterEntity model) {
        return transform(getService().msMessageView(model));
    }

    /****
     * 未读系统消息
     * @param model
     * @return
     */
    public Observable<String> noReadCount(PageListEntity model) {
        return nothingDatatransform(getService().noReadCount(model));
    }

    /****
     * 通知公告查看
     * @param model
     * @return
     */
    public Observable<NoticeBean> findUserNoticeByPage(BaseFilterEntity model) {
        return transform(getService().findUserNoticeByPage(model));
    }

    /****
     * 查看记录
     * @param model
     * @return
     */
    public Observable<NoticeViewBean> NoticeView(BaseFilterEntity model) {
        return transform(getService().NoticeView(model));
    }

    /***
     * 通知公告未读信息
     * @param userId
     * @return
     */
    public Observable<String> noReadNoticeCount(String userId) {
        return nothingDatatransform(getService().noReadNoticeCount(userId));
    }


    /*****
     * 航班动态
     */
    public Observable<FlightBean> flightdynamic(BaseFilterEntity model) {
        return flightTransform(mUpdateApisFlight.flightdynamic(model));
    }

    /*****
     * 用于接收手机参数的实体
     */
    public Observable<String> getPhoneParameters(PhoneParametersEntity entity) {
        return notingflightTransform(mUpdateApisFlight.getPhoneParameters(entity));
    }

    /***
     * 航班详情
     * @param model
     * @return
     */
    public Observable<FlightInfoBean> flightInfo(BaseFilterEntity model) {
        return flightTransform(mUpdateApisFlight.flightInfo(model));
    }

    /***
     * 航班详情
     * @param model
     * @return
     */
    public Observable<FlightServiceBean> getMilepostData(BaseFilterEntity model) {
        return flightTransform(mUpdateApisFlight.getMilepostData(model));
    }

    /**
     * 获取换单审核的数据
     *
     * @param declareWaybillEntity
     * @return （带新订单id的） 换单审核数据
     */
    public Observable<DeclareWaybillBean> getChangeWaybill(DeclareWaybillEntity declareWaybillEntity) {
        return transform(getService().getChangeWaybill(declareWaybillEntity));
    }

    /**
     * 换单审核提交（接受或者拒绝）
     *
     * @param changeWaybillEntity
     * @return 提交结果
     */
    public Observable<String> changeSubmit(ChangeWaybillEntity changeWaybillEntity) {
        return nothingtransform(getService().changeSubmit(changeWaybillEntity));
    }

    /**
     * 收运打印
     *
     * @return 提交结果
     */
    public Observable<String> sendPrintMessage(String waybillId) {
        return nothingtransform(getService().sendPrintMessage(waybillId));
    }


    /*****
     * 扫描板车并锁定 (添加)
     * @param scooterCode
     * @return
     */
    public Observable<BaseEntity<Object>> checkScooterCode(String scooterCode) {
        return getService().checkScooterCode(scooterCode);
    }

    /**
     * 进港分拣 - 获取分拣数据 - guohao
     *
     * @param entity 航班业务id(UUID超级长的, 非数字id)
     * @return
     */
    public Observable<InWaybillRecordBean> getInWaybillRecrodList(InWaybillRecordGetEntity entity) {
        return transform(getService().getInWaybillRecrodList(entity));
    }

    /**
     * 进港分拣 - 提交/暂存
     *
     * @param entity 所有数据
     * @return 成功/失败
     */
    public Observable<String> submitWillbillRecord(InWaybillRecordSubmitEntity entity) {
        return nothingtransform(getService().submitWillbillRecord(entity));
    }

    /**
     * 进港分拣 - 根据id删除分拣信息
     *
     * @param id 分拣子信息的id
     * @return 成功/失败
     */
    public Observable<String> deleteInWayBillRecordById(String id) {
        return nothingtransform(getService().deleteInWayBillRecordById(id));
    }

    /**
     * 进港分拣 - 通知服务器已全部到齐
     *
     * @param data 需要通知已全部到齐的数据
     * @return 成功/失败
     */
    public Observable<String> allGoodsArrived(InWaybillRecord data) {
        return nothingDatatransform(getService().allGoodsArrived(data));
    }

    /**
     * 回退到预配 -- guohao
     *
     * @param entity
     * @return 成功/失败
     */
    public Observable<String> returnPrematching(BaseFilterEntity entity) {
        return nothingtransform(getService().returnPrematching(entity));
    }

    /**
     * 清库列表
     *
     * @return 成功/失败
     */
    public Observable<InventoryQueryBean> inventoryQuery(BaseFilterEntity entity) {
        return transform(getService().inventoryQuery(entity));
    }

    /**
     * 清库提交
     *
     * @return 成功/失败
     */
    public Observable<String> addInventoryDetail(List<InventoryDetailEntity> entity) {
        return nothingtransform(getService().addInventoryDetail(entity));
    }

    /**
     * 清库详情
     *
     * @return 成功/失败
     */
    public Observable<List<InventoryDetailEntity>> listInventoryDetail(BaseFilterEntity entity) {
        return transform(getService().listInventoryDetail(entity));
    }

    /**
     * 运单查询
     *
     * @return 成功/失败
     */
    public Observable<ListWaybillCodeBean> listWaybillCode(String code, String taskId) {
        return getService().listWaybillCode(code, taskId);
    }

    /**
     * 清库列表
     *
     * @return 成功/失败
     */
    public Observable<String> internationalCargoReport(CargoUploadBean entity) {
        return nothingtransform(getService().internationalCargoReport(entity));
    }

    public Observable<List<ReservoirArea>> listReservoirInfoByCode(String deptCode) {
        return transform(getService().listReservoirInfoByCode(deptCode));
    }

    public Observable<List<GetAllRemoteAreaBean>> getAllRemoteArea() {
        return transform(getService().getAllRemoteArea());
    }

    public Observable<UnLoadListBillBean> getUnLoadingList(UnLoadRequestEntity entity) {
        return getService().getUnLoadingList(entity);
    }

    /**
     * 待办锁定
     *
     * @param entity
     * @return
     */
    public Observable<String> taskLock(TaskLockEntity entity) {
        return nothingtransform(getService().taskLock(entity));
    }

    /**
     * ULD根据字段过滤
     *
     * @param entity
     * @return
     */
    public Observable<UldLikeBean> likePage(BaseFilterEntity entity) {
        return transform(getService().likePage(entity));
    }

    /**
     * ULD根据字段过滤
     *
     * @param entity
     * @return
     */
    public Observable<ListByTypeBean> listByType(BaseFilterEntity entity) {
        return transform(getService().listByType(entity));
    }
    /**
     * ULD根据字段过滤
     *
     * @param entity
     * @return
     */
    public Observable<List<FlightAllReportInfo>> getFlightAllReportInfo(BaseFilterEntity entity) {
        return transform(getService().getFlightAllReportInfo(entity));
    }

    /**
     * ULD根据字段过滤
     *
     * @param entity
     * @return
     */
    public Observable<LastReportInfoListBean> getLastReportInfo(BaseFilterEntity entity) {
        return transform(getService().getLastReportInfo(entity));
    }

    public Observable<String> saveOrUpdate(SaveOrUpdateEntity entity) {
        return nothingtransform(getService().saveOrUpdate(entity));
    }

    public Observable<List<FindAirlineAllBean>> findAirlineAll() {
        return transform(getService().findAirlineAll());
    }

    public Observable<String> synchronousLoading(BaseFilterEntity entity) {
        return nothingtransform(getService().synchronousLoading(entity));
    }

    public Observable<String> auditManifest(BaseFilterEntity entity) {
        return nothingtransform(getService().auditManifest(entity));
    }


    public Observable<List<CargoReportHisBean>> cargoReportHis(String operatorId) {
        return transform(getService().cargoReportHis(operatorId));
    }

    public Observable<List<CargoReportHisBean>> baggageSubHis(String operatorId) {
        return transform(getService().baggageSubHis(operatorId));
    }

    public Observable<List<LoadAndUnloadTodoBean>> reportTaskHis(String operatorId) {
        return transform(getService().reportTaskHis(operatorId));
    }

    public Observable<List<OutFieldTaskBean>> transportTaskHis(String operatorId) {
        return transform(getService().transportTaskHis(operatorId));
    }

    public Observable<List<LoadAndUnloadTodoBean>> stevedoresTaskHis(String operatorId) {
        return transform(getService().stevedoresTaskHis(operatorId));
    }

    public Observable<List<LoadAndUnloadTodoBean>> loadUnloadTaskHis(String operatorId) {
        return transform(getService().loadUnloadTaskHis(operatorId));
    }
}


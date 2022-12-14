package qx.app.freight.qxappfreight.utils.httpUtils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import qx.app.freight.qxappfreight.bean.CargoUploadBean;
import qx.app.freight.qxappfreight.bean.GetWaybillInfoByIdDataBean;
import qx.app.freight.qxappfreight.bean.GoodsIdEntity;
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
import qx.app.freight.qxappfreight.bean.response.WaybillQuickGetBean;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.bean.response.WaybillsListBean;
import qx.app.freight.qxappfreight.constant.HttpConstant;
import qx.app.freight.qxappfreight.http.HttpApi;
import qx.app.freight.qxappfreight.model.ManifestBillModel;

public class UpdateRepository extends BaseRepository {
    private volatile static UpdateRepository instance;
    private volatile static HttpApi mUpdateApis;
    private volatile static HttpApi mUpdateApisFlight;
    private volatile static HttpApi mUpdateApisQxAi;

    private UpdateRepository() {
        //????????????
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
     * ??????
     * @param loginEntity
     * @return
     */
    public Observable<LoginResponseBean> login(LoginEntity loginEntity) {
        return transform(getService().login(loginEntity));
    }

    /****
     * ??????
     * @param loginEntity
     * @return
     */
    public Observable<String> updatePWD(UpdatePwdEntity loginEntity) {
        return nothingtransform(getService().updatePWD(loginEntity));
    }

    /****
     * ????????????????????????
     * @param
     * @return
     */
    public Observable<RespLoginBean> loginQxAi(Map<String, String> map) {
        return nothingDatatransformForOneDisP(getServiceQxAi().loginQxAi(map));
    }

    /****
     * ????????????????????????
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
     * ????????????
     * @param
     * @return
     */
    public Observable<String> changeStorage(ChangeStorageBean param) {
        return nothingtransform(getService().changeStorage(param));
    }

    /*****
     * ????????????
     * @param
     * @return
     */
    public Observable<SearchReservoirBean> searchReservoir(BaseFilterEntity param) {
        return transform(getService().searchReservoir(param));
    }

    /*****
     * ??????????????????
     * @param
     * @return
     */
    public Observable<List<ScooterConfBean.ScooterConf>> getScooterConf(String param) {
        return transform(getService().getScooterConf(param));
    }


    /***
     *????????????????????????
     * @param freightCode:
     * ????????????
     * @return
     */
    public Observable<GetQualificationsBean> getQualifications(String freightCode) {
        return transform(getService().getQualifications(freightCode));
    }

    /***
     * ????????????????????????
     * @param airLineId:
     * ????????????Id ?????? ??????????????????
     * @return
     */
    public Observable<List<QueryAviationRequireBean>> getQueryAviationRequire(String airLineId) {
        return transform(getService().getQueryAviationRequire(airLineId));
    }

    /*****
     *????????????
     * @param storageCommitEntity
     * @return
     * ??????????????????
     */
    public Observable<String> storageCommit(StorageCommitEntity storageCommitEntity) {
        return nothingtransform(getService().storageCommit(storageCommitEntity));
    }


    /****
     * ??????????????????
     */
    public Observable<String> commitReceiveList(List<StorageCommitEntity> storageCommitEntity) {
        return nothingtransform(getService().commitReceiveList(storageCommitEntity));
    }

    /******
     * ????????????
     * @param modifyTextEntity
     * @return
     * ??????????????????
     */
    public Observable<String> modifyTest(ModifyTextEntity modifyTextEntity) {
        return nothingtransform(getService().modifyTest(modifyTextEntity));
    }

    /******
     * ????????????
     * @param waybillId
     * @return
     */
    public Observable<TestInfoListBean> testInfo(String waybillId, String freightId, String mTaskTypeCode) {
        return transform(getService().testInfo(waybillId, freightId, mTaskTypeCode));
    }

    /****
     * ???????????????????????????
     * @param queryContainerInfoEntity
     * @return
     */
    public Observable<List<QueryContainerInfoBean>> queryContainerInfo(QueryContainerInfoEntity queryContainerInfoEntity) {
        return transform(getService().queryContainerInfo(queryContainerInfoEntity));
    }

    /***
     *??????????????????
     * @return
     */
    public Observable<List<QueryReservoirBean>> queryReservoir() {
        return transform(getService().queryReservoir());
    }

    /*****
     * ??????????????????
     * @param myAgentListBean
     * @return
     */
    public Observable<MyAgentListBean> addInfo(MyAgentListBean myAgentListBean) {
        return transform(getService().addInfo(myAgentListBean));
    }


    /*****
     * ???????????????????????????
     * @param scooterId
     * @return
     */
    public Observable<MyAgentListBean> exist(String scooterId) {
        return transform(getService().exist(scooterId));
    }

    /****
     * ????????????
     * @param model
     * @return
     */
    public Observable<AutoReservoirBean> autoReservoirv(BaseFilterEntity model) {
        return transform(getService().autoReservoirv(model));
    }

    /****
     * ??????????????????
     * @param model
     * @return
     */
    public Observable<ReservoirBean> reservoir(BaseFilterEntity model) {
        return transform(getService().reservoir(model));
    }

    /****
     * ??????????????????
     * @param id
     * @return
     */
    public Observable<String> deleteCollectionInfo(String id) {
        return nothingtransform(getService().deleteCollectionInfo(id));
    }


    /********
     * ??????????????????
     * @param model
     * @return
     */
    public Observable<TransportListBean> transportList(BaseFilterEntity model) {
        return transform(getService().transportList(model));
    }

    /********
     * ??????????????????
     * @param model
     * @return
     */
    public Observable<FilterTransportDateBase> getGroupBoardToDo(BaseFilterEntity model) {
        return transform(getService().getGroupBoardToDo(model));
    }

    /********
     * ????????????????????????
     * @param model
     * @return
     */
    public Observable<FilterTransportDateBase> getOverWeightToDo(BaseFilterEntity model) {
        return transform(getService().getOverWeightToDo(model));
    }


    /********
     * ??????????????????
     * @param model
     * @return
     */
    public Observable<BaseParamBean> baseParam(BaseFilterEntity model) {
        return transform(getService().baseParam(model));
    }

    public Observable<BaseParamBean> baseParamType(BaseFilterEntity model) {
        return transform(getService().baseParamType(model));
    }


    /********
     * ??????????????????
     * @param model
     * @return
     */
    public Observable<TransportListBean> searchTodoTask(BaseFilterEntity model) {
        return transform(getService().searchTodoTask(model));
    }

    /**
     * ????????????????????????
     *
     * @param id
     * @return
     */

    public Observable<DeclareWaybillBean> getWayBillInfoById(String id) {
        return transform(getService().getWayBillInfoById(id));
    }

    /*****
     * ????????????
     * @param files
     * @return
     */
    public Observable<Object> upLoads(List<MultipartBody.Part> files) {
        return transform(getService().upLoads(files));
    }

    /***
     * ???????????????
     * @param transportListCommitEntity
     * @return
     */
    public Observable<String> transportListCommit(TransportListCommitEntity transportListCommitEntity) {
        return nothingtransform(getService().transportListCommit(transportListCommitEntity));
    }

    /***
     * ??????????????????
     * @param transportListCommitEntity
     * @return
     */
    public Observable<String> returnCargoCommit(TransportListCommitEntity transportListCommitEntity) {
        return nothingtransform(getService().returnCargoCommit(transportListCommitEntity));
    }

    /****
     * ??????????????????
     * @param baseFilterEntity
     * @return
     */
    public Observable<ScooterInfoListDataBean> scooterInfoList(BaseFilterEntity baseFilterEntity) {
        return transform(getService().scooterInfoList(baseFilterEntity));
    }

    /****
     * ?????????????????? (????????????)
     * @param baseFilterEntity
     * @return
     */
    public Observable<ScooterInfoListDataBean> scooterInfoListForReceive(BaseFilterEntity baseFilterEntity) {
        return transform(getService().scooterInfoListForReceive(baseFilterEntity));
    }

    /***
     * uld?????????
     * @param baseFilterEntity
     * @return
     */
    public Observable<List<UldInfoListBean>> uldInfoList(BaseFilterEntity baseFilterEntity) {
        return transform(getService().uldInfoList(baseFilterEntity));
    }

    /****
     * ????????????
     * @param iata
     * @return
     */
    public Observable<List<MarketCollectionRequireBean>> freightInfo(String iata) {
        return transform(getService().freightInfo(iata));
    }

    /****
     * ??????????????????????????????
     * @param acdmDtoId
     * @return
     */
    public Observable<List<TransportTodoListBean>> getUnloadDoneScooter(String acdmDtoId) {
        return transform(getService().getUnloadDoneScooter(acdmDtoId));
    }

    /****
     * ????????????
     * @param freightId
     * @return
     */

    public Observable<ForwardInfoBean> forwardInfo(String freightId) {
        return transform(getService().forwardInfo(freightId));
    }

    /****
     * ????????????
     * @param model
     * @return
     */
    public Observable<List<AirlineRequireBean>> airlineRequire(BaseFilterEntity model) {
        return transform(getService().airlineRequire(model));
    }

    /****
     * ????????????id list ?????? ????????????
     * @param goodsNames
     * @return
     */

    public Observable<List<DocumentsBean>> getgetCommdityById(GoodsIdEntity goodsNames) {
        return transform(getService().getgetCommdityById(goodsNames));
    }

    /*****
     * ????????????
     * @param model
     * @return
     */
    public Observable<String> fightScooterSubmit(FightScooterSubmitEntity model) {
        return nothingtransform(getService().fightScooterSubmit(model));
    }

    /***
     * ????????????
     * @return
     */
    public Observable<AddScooterBean> addScooter() {
        return transform(getService().addScooter());
    }

    /*****
     * ???????????????
     * @param getScooterListInfoEntity
     * @return
     */
    public Observable<GetScooterListInfoBean> getScooterListInfo(GetScooterListInfoEntity getScooterListInfoEntity) {
        return transform(getService().getScooterListInfo(getScooterListInfoEntity));
    }

    /****
     * ??????
     * @param pbName
     * @return
     */
    public Observable<String> getWeight(String pbName) {
        return nothingDatatransform(getService().getWeight(pbName));
    }

    /****
     * ??????
     * @param iata
     * @return
     */
    public Observable<String> getAirWaybillPrefix(String iata) {
        return nothingDatatransform(getService().getAirWaybillPrefix(iata));
    }

    /****
     * ??????????????????
     * @param model
     * @return
     */
    public Observable<String> transportEnd(TransportEndEntity model) {
        return nothingtransform(getService().transportEnd(model));
    }

    /****
     * ??????????????????
     * @param model
     * @return
     */
    public Observable<String> transportBegin(TransportEndEntity model) {
        return nothingtransform(getService().transportBegin(model));
    }

    /*****
     * ??????????????????????????????
     * @param model
     * @return
     */
    public Observable<String> scanScooterDelete(TransportEndEntity model) {
        return nothingtransform(getService().scanScooterDelete(model));
    }

    /*****
     * ????????????????????? (??????)
     * @param model
     * @return
     */
    public Observable<String> scanScooter(TransportTodoListBean model) {
        return nothingtransform(getService().scanScooter(model));
    }

    /*****
     * ????????????????????? (??????)
     * @param model
     * @return
     */
    public Observable<String> scanLockScooter(TransportEndEntity model) {
        return nothingtransform(getService().scanLockScooter(model));
    }

    /****
     * ??????????????????
     * @param model
     * @return
     */
    public Observable<List<TransportTodoListBean>> scooterWithUser(String model, String flightId, String taskId) {
        return transform(getService().scooterWithUser(model, flightId, taskId));
    }

    /****
     * ??????????????????
     * @param taskId
     * @return
     */
    public Observable<List<TransportTodoListBean>> scooterWithUserTask(String taskId) {
        return transform(getService().scooterWithUserForTask(taskId));
    }

    /*****
     * ?????????????????????
     * @return
     */
    public Observable<List<TransportTodoListBean>> transportTodoList() {
        return transform(getService().transportTodoList());
    }


    /****
     * ?????????????????????????????????
     * @param performTaskStepsEntity
     * @return
     */
    public Observable<String> performTaskSteps(PerformTaskStepsEntity performTaskStepsEntity) {
        return nothingtransform(getService().performTaskSteps(performTaskStepsEntity));
    }

    /****
     * ??????????????????????????????????????????
     * @return
     */
    public Observable<String> confirmLoadPlan(BaseFilterEntity entity) {
        return nothingtransform(getService().confirmLoadPlan(entity));
    }

    /****
     * ????????????????????????
     * @return
     */
    public Observable<BaseEntity<String>> getPullStatus(BaseFilterEntity entity) {
        return getService().getPullStatus(entity);
    }

    /****
     * ????????????????????????
     * @return
     */
    public Observable<String> uploadFlightPhoto(FlightPhotoEntity entity) {
        return nothingtransform(getService().uploadFlightPhoto(entity));
    }

    /****
     * ??????????????? ??????
     * @return
     */
    public Observable<String> startPull(PullGoodsEntity entity) {
        return nothingtransform(getService().startPull(entity));
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
     * ??????????????????
     * @return
     */
    public Observable<List<AcceptTerminalTodoBean>> acceptTerminalTodo(BaseFilterEntity model) {
        return transform(getService().acceptTerminalTodo(model));
    }

    /***
     * ?????? ????????????
     * @param exceptionReportEntity
     * @return
     */
    public Observable<String> exceptionTpEnd(ExceptionReportEntity exceptionReportEntity) {
        return nothingtransform(getService().exceptionTpEnd(exceptionReportEntity));
    }

    /***
     * ????????????
     * @param exceptionReportEntity
     * @return
     */
    public Observable<String> exceptionReport(ExceptionReportEntity exceptionReportEntity) {
        return nothingtransform(getService().exceptionReport(exceptionReportEntity));
    }

    /****
     * ????????????
     * @param transportEndEntity
     * @return
     */
    public Observable<String> arrivalDataSave(TransportEndEntity transportEndEntity) {
        return nothingtransform(getService().arrivalDataSave(transportEndEntity));
    }

    /****
     * ??????????????????
     * @param taskClearEntity
     * @return
     */
    public Observable<String> startClearTask(TaskClearEntity taskClearEntity) {
        return nothingtransform(getService().startClearTask(taskClearEntity));
    }

    /****
     * ??????????????????????????????????????????????????????
     * @param flightInfoId
     * @return
     */
    public Observable<PullGoodsInfoBean> getPullGoodsInfo(String flightInfoId) {
        return transform(getService().getPullGoodsInfo(flightInfoId));
    }

    /****
     * ?????????????????????????????????????????????????????????????????????
     * @param entity
     * @return
     */
    public Observable<String> pullGoodsInfoCommit(PullGoodsInfoBean entity) {
        return nothingtransform(getService().pullGoodsInfoCommit(entity));
    }

    /****
     * ????????????
     * @return
     */
    public Observable<GetWaybillInfoByIdDataBean> getWayBillInfoByCode(String waybillCode) {
        return transform(getService().getWayBillInfoByCode(waybillCode));
    }

    /****
     * ????????????
     * @return
     */
    public Observable<List<GetWaybillInfoByIdDataBean>> getWaybillInfo(String id) {
        return transform(getService().getWaybillInfo(id));
    }

    /**
     * ?????????????????????
     *
     * @param entity ????????????
     * @return
     */
    public Observable<LoadingListBean> getLoadingList(LoadingListRequestEntity entity) {
        return getService().getLoadingList(entity);
    }

    /**
     * ????????????????????????
     *
     * @param entity ????????????
     * @return
     */
    public Observable<CargoCabinData> getFlightSpace(FlightIdBean entity) {
        return transform(getService().getFlightSpace(entity));
    }


    /*****
     * ????????????
     * @param model
     * @return
     */
    public Observable<String> flightDoneInstall(GetFlightCargoResBean model) {
        return nothingtransform(getService().flightDoneInstall(model));
    }

    /*****
     * ????????????
     * @param model
     * @return
     */
    public Observable<String> overLoad(LoadingListSendEntity model) {
        return nothingtransform(getService().overLoad(model));
    }


    /****
     * ???????????????
     * @param model
     * @return
     */
    public Observable<List<LoadAndUnloadTodoBean>> loadAndUnloadTodo(BaseFilterEntity model) {
        return transform(getService().loadAndUnloadTodo(model));
    }

    /*****
     * ????????????
     * @param model
     * @return
     */
    public Observable<String> lockOrUnlockScooter(LockScooterEntity model) {
        return nothingtransform(getService().lockOrUnlockScooter(model));
    }


    /****
     * ??????????????????????????????
     * @param  taskId
     * @return
     */
    public Observable<List<SelectTaskMemberEntity>> getLoadUnloadLeaderList(String taskId) {
        return transform(getService().getLoadUnloadLeaderList(taskId));
    }

    /****
     * ????????????????????????????????????
     * @param  baseFilterEntity
     * @return
     */
    public Observable<String> selectMember(BaseFilterEntity baseFilterEntity) {
        return nothingtransform(getService().selectMember(baseFilterEntity));
    }

    /****
     * ?????????????????????????????????????????????
     * @param  baseFilterEntity
     * @return
     */
    public Observable<String> refuseTask(BaseFilterEntity baseFilterEntity) {
        return nothingtransform(getService().refuseTask(baseFilterEntity));
    }

    /****
     * ????????????????????????????????????
     * @param  baseFilterEntity
     * @return
     */
    public Observable<List<LoadAndUnloadTodoBean>> getLoadUnloadLeaderToDo(BaseFilterEntity baseFilterEntity) {
        return transform(getService().getLoadUnloadLeaderToDo(baseFilterEntity));
    }

    /****
     * ????????????
     * @return
     */
    public Observable<List<LoadAndUnloadTodoBean>> getEndInstallTodo(BaseFilterEntity model) {
        return transform(getService().getEndInstallTodo(model));
    }

    /****
     * ???????????????????????????
     * @param string
     * @return
     */
    public Observable<String> baggageAreaSub(String string) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                string);
        return nothingtransform(getService().baggageAreaSub(requestBody));
    }

    /****
     * ???????????????????????????????????????????????????
     * @param model
     * @return
     */
    public Observable<String> lookLUggageScannigFlight(BaseFilterEntity model) {
        return nothingtransform(getService().lookLUggageScannigFlight(model));
    }

    /****
     * ????????????????????????
     * @param model
     * @return
     */
    public Observable<List<FlightLuggageBean>> getDepartureFlightByAndroid(BaseFilterEntity model) {
        return transform(getService().getDepartureFlightByAndroid(model));
    }

    /****
     * ????????????????????????
     * @return
     */
    public Observable<List<FlightLuggageBean>> getAllInternationalAndMixedFlight(BaseFilterEntity model) {
        return transform(getService().getAllInternationalAndMixedFlight(model));
    }

    /*****
     * ??????????????????
     * @param model
     * @return
     */
    public Observable<List<GetInfosByFlightIdBean>> getInfosByFlightId(BaseFilterEntity model) {
        return transform(getService().getInfosByFlightId(model));
    }

    /****
     * ????????????
     * @param model
     * @return
     */
    public Observable<String> scooterSubmit(ScooterSubmitEntity model) {
        return nothingtransform(getService().scooterSubmit(model));
    }

    /****
     * ??????/??????????????????
     * @param model
     * @return
     */
    public Observable<List<GetInfosByFlightIdBean>> getScooterByScooterCode(BaseFilterEntity model) {
        return transform(getService().getScooterByScooterCode(model));
    }
    /****
     * ??????/??????????????????
     * @param model
     * @return
     */
    public Observable<BaseEntity<Object>> returnGroupScooterTask(GetInfosByFlightIdBean model) {
        return getService().returnGroupScooterTask(model);
    }

    /****
     * ??????/??????
     * @param model
     * @return
     */
    public Observable<OverWeightSaveResultBean> saveScooter(GetInfosByFlightIdBean model) {
        return transform(getService().saveScooter(model));
    }

    /****
     * ??????/????????????
     * @param model
     * @return
     */
    public Observable<String> returnWeighing(ReturnWeighingEntity model) {
        return nothingtransform(getService().returnWeighing(model));
    }


    /****
     * ?????? / ????????????????????????
     * @param model
     * @return
     */
    public Observable<FlightInfoAndScootersBean> getTodoScooters(TodoScootersEntity model) {
        return transform(getService().getTodoScooters(model));
    }

    /****
     * //?????? / ????????????
     * @param model
     * @return
     */
    public Observable<GetHistoryBean> getHistoryScootersPage(BaseFilterEntity model) {
        return transform(getService().getHistoryScootersPage(model));
    }

    /****
     * ??????????????????????????????????????????
     * @param entity
     * @return
     */
    public Observable<List<SearchFlightInfoBean>> searchFlightsByKey(SearchFilghtEntity entity) {
        return transform(getService().searchFlightsByKey(entity));
    }

    /****
     * ?????? / ????????????????????????
     * @param model
     * @return
     */
    public Observable<List<ManifestBillModel>> getManifest(BaseFilterEntity model) {
        return transform(getService().getManifest(model));
    }


    /*****
     * ??????????????????ID(?????????) ?????????????????????????????????????????????
     * @param model
     * @return
     */
    public Observable<ArrivalDeliveryInfoBean> arrivalDeliveryInfo(BaseFilterEntity model) {
        return transform(getService().arrivalDeliveryInfo(model));
    }

    /****
     * ??????????????? ????????????
     * @param model
     * @return
     */
    public Observable<WaybillsListBean> searchWaybillByWaybillCode(BaseFilterEntity model) {
        return transform(getService().searchWaybillByWaybillCode(model));
    }

    /****
     * ??????-??????
     * @param model
     * @return
     */
    public Observable<String> deliveryInWaybill(List<BaseFilterEntity> model) {
        return nothingtransform(getService().deliveryInWaybill(model));
    }

    /****
     * ??????-????????????????????????
     * @param model
     * @return
     */
    public Observable<List<OverweightBean>> getOverweight(BaseFilterEntity model) {
        return transform(getService().getWaybillOverWeight(model));
    }

    /****
     * ??????-??????????????????
     * @param model
     * @return
     */
    public Observable<String> addOverweight(List<OverweightBean> model) {
        return nothingtransform(getService().addWaybillOverWeight(model));
    }

    /****
     * ??????-??????????????????????????????????????????
     * @param waybillId
     * @return
     */
    public Observable<List<ForkliftWorkingCostBean>> getWaybillForklift(String waybillId) {
        return transform(getService().getWaybillForklift(waybillId));
    }

    /****
     * ??????-??????????????????????????????
     * @param waybillId
     * @return
     */
    public Observable<List<PickGoodsRecordsBean>> getOutboundList(String waybillId) {
        return transform(getService().getOutboundList(waybillId));
    }

    /****
     * ??????-??????????????????
     * @param model
     * @return
     */
    public Observable<String> revokeInboundDelevery(PickGoodsRecordsBean model) {
        return nothingtransform(getService().revokeInboundDelevery(model));
    }

    /****
     * ??????-??????????????????????????????
     * @param model
     * @return
     */
    public Observable<String> addWaybillForklift(List<ForkliftWorkingCostBean> model) {
        return nothingtransform(getService().addWaybillForklift(model));
    }

    /****
     * ??????-??????????????????
     * @param model
     * @return
     */
    public Observable<String> deleteOverweight(OverweightBean model) {
        return nothingtransform(getService().deleteWaybillOverWeight(model));
    }

    /***
     * ??????-??????
     * @param model
     * @return
     */
    public Observable<String> completDelivery(BaseFilterEntity model) {
        return nothingtransform(getService().completDelivery(model));
    }

    /***
     * ????????????
     * @param model
     * @return
     */
    public Observable<String> errorFiling(ErrorFilingEntity model) {
        return nothingtransform(getService().errorFiling(model));
    }

    /***
     * ??????????????????????????????
     * @param model
     * @return
     */
    public Observable<InPortResponseBean> getInPortTallyList(BaseFilterEntity model) {
        return transform(getService().getInPortTallyList(model));
    }

    /***
     * ??????????????????????????????
     * @param model
     * @return
     */
    public Observable<String> inPortTallyCommit(InPortTallyCommitEntity model) {
        return nothingtransform(getService().inPortTallyCommit(model));
    }


    /*****
     * ????????????????????????
     * @param model
     * @return
     */
    public Observable<PageListBean> pageList(BaseFilterEntity model) {
        return transform(getService().pageList(model));
    }

    /****
     * ????????????
     * @param model
     * @return
     */
    public Observable<MsMessageViewBean> msMessageView(BaseFilterEntity model) {
        return transform(getService().msMessageView(model));
    }

    /****
     * ??????????????????
     * @param model
     * @return
     */
    public Observable<String> noReadCount(PageListEntity model) {
        return nothingDatatransform(getService().noReadCount(model));
    }

    /****
     * ??????????????????
     * @param model
     * @return
     */
    public Observable<NoticeBean> findUserNoticeByPage(BaseFilterEntity model) {
        return transform(getService().findUserNoticeByPage(model));
    }

    /****
     * ????????????
     * @param model
     * @return
     */
    public Observable<NoticeViewBean> NoticeView(BaseFilterEntity model) {
        return transform(getService().NoticeView(model));
    }

    /***
     * ????????????????????????
     * @param userId
     * @return
     */
    public Observable<String> noReadNoticeCount(String userId) {
        return nothingDatatransform(getService().noReadNoticeCount(userId));
    }


    /*****
     * ????????????
     */
    public Observable<FlightBean> flightdynamic(BaseFilterEntity model) {
        return flightTransform(mUpdateApisFlight.flightdynamic(model));
    }

    /*****
     * ?????????????????????????????????
     */
    public Observable<String> getPhoneParameters(PhoneParametersEntity entity) {
        return notingflightTransform(mUpdateApisFlight.getPhoneParameters(entity));
    }

    /***
     * ????????????
     * @param model
     * @return
     */
    public Observable<FlightInfoBean> flightInfo(BaseFilterEntity model) {
        return flightTransform(mUpdateApisFlight.flightInfo(model));
    }

    /***
     * ????????????
     * @param model
     * @return
     */
    public Observable<FlightServiceBean> getMilepostData(BaseFilterEntity model) {
        return flightTransform(mUpdateApisFlight.getMilepostData(model));
    }

    /**
     * ???????????????????????????
     *
     * @param declareWaybillEntity
     * @return ???????????????id?????? ??????????????????
     */
    public Observable<DeclareWaybillBean> getChangeWaybill(DeclareWaybillEntity declareWaybillEntity) {
        return transform(getService().getChangeWaybill(declareWaybillEntity));
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param changeWaybillEntity
     * @return ????????????
     */
    public Observable<String> changeSubmit(ChangeWaybillEntity changeWaybillEntity) {
        return nothingtransform(getService().changeSubmit(changeWaybillEntity));
    }

    /**
     * ????????????
     *
     * @return ????????????
     */
    public Observable<String> sendPrintMessage(String waybillId) {
        return nothingtransform(getService().sendPrintMessage(waybillId));
    }


    /*****
     * ????????????????????? (??????)
     * @param scooterCode
     * @return
     */
    public Observable<BaseEntity<Object>> checkScooterCode(String scooterCode, String flightId, String scSubType) {
        return getService().checkScooterCode(scooterCode, flightId, scSubType);
    }

    /**
     * ???????????? - ?????????????????? - guohao
     *
     * @param entity ????????????id(UUID????????????, ?????????id)
     * @return
     */
    public Observable<InWaybillRecordBean> getInWaybillRecrodList(InWaybillRecordGetEntity entity) {
        return transform(getService().getInWaybillRecrodList(entity));
    }

    /**
     * ???????????? - ??????/??????
     *
     * @param entity ????????????
     * @return ??????/??????
     */
    public Observable<BaseEntity<Object>> submitWillbillRecord(InWaybillRecordSubmitNewEntity entity) {
        return getService().submitWillbillRecord(entity);
    }

    /**
     * ???????????? - ??????id??????????????????
     *
     * @param id ??????????????????id
     * @return ??????/??????
     */
    public Observable<String> deleteInWayBillRecordById(String id) {
        return nothingtransform(getService().deleteInWayBillRecordById(id));
    }

    /**
     * ???????????? - ??????????????????????????????
     *
     * @param data ????????????????????????????????????
     * @return ??????/??????
     */
    public Observable<WaybillQuickGetBean> allGoodsArrived(InWaybillRecordSubmitNewEntity.SingleLineBean data) {
        return getService().allGoodsArrived(data);
    }

    /**
     * ??????????????? -- guohao
     *
     * @param entity
     * @return ??????/??????
     */
    public Observable<String> returnPrematching(BaseFilterEntity entity) {
        return nothingtransform(getService().returnPrematching(entity));
    }

    /**
     * ????????????
     *
     * @return ??????/??????
     */
    public Observable<InventoryQueryBean> inventoryQuery(BaseFilterEntity entity) {
        return transform(getService().inventoryQuery(entity));
    }

    /**
     * ????????????
     *
     * @return ??????/??????
     */
    public Observable<BaseEntity> addInventoryDetail(List<InventoryDetailEntity> entity) {
        return getService().addInventoryDetail(entity);
    }

    /**
     * ????????????
     *
     * @return ??????/??????
     */
    public Observable<List<InventoryDetailEntity>> listInventoryDetail(BaseFilterEntity entity) {
        return transform(getService().listInventoryDetail(entity));
    }

    /**
     * ????????????
     *
     * @return ??????/??????
     */
    public Observable<ListWaybillCodeBean> listWaybillCode(String code, String taskId) {
        return getService().listWaybillCode(code, taskId);
    }

    /**
     * //???????????? ?????? ?????????
     *
     * @return ??????/??????
     */
    public Observable<String> getWaybillCode() {
        return nothingDatatransform(getService().getWaybillCode());
    }

    /**
     * ????????????
     *
     * @return ??????/??????
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
     * ????????????
     *
     * @param entity
     * @return
     */
    public Observable<String> taskLock(TaskLockEntity entity) {
        return nothingtransform(getService().taskLock(entity));
    }

    /**
     * ULD??????????????????
     *
     * @param entity
     * @return
     */
    public Observable<UldLikeBean> likePage(BaseFilterEntity entity) {
        return transform(getService().likePage(entity));
    }

    /**
     * ULD??????????????????
     *
     * @param entity
     * @return
     */
    public Observable<ListByTypeBean> listByType(BaseFilterEntity entity) {
        return transform(getService().listByType(entity));
    }

    /**
     * ??????????????????/????????? /????????? ?????????????????????
     *
     * @param entity
     * @return
     */
    public Observable<List<FlightAllReportInfo>> getFlightAllReportInfo(BaseFilterEntity entity) {
        return transform(getService().getFlightAllReportInfo(entity));
    }

    /**
     * ??????????????????/????????? /????????? ?????????????????????
     *
     * @param entity
     * @return
     */
    public Observable<List<FlightAllReportInfo>> getLastReportInfo(BaseFilterEntity entity) {
        return transform(getService().getFlightAllReportInfo(entity));
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param entity
     * @return
     */
    public Observable<String> reOpenLoadTask(BaseFilterEntity entity) {
        return nothingtransform(getService().reOpenLoadTask(entity));
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

    public Observable<String> repartWriteLoading(BaseFilterEntity entity) {
        return nothingtransform(getService().repartWriteLoading(entity));
    }


    public Observable<List<CargoReportHisBean>> cargoReportHis(BaseFilterEntity operatorId) {
        return transform(getService().cargoReportHis(operatorId));
    }

    public Observable<List<CargoReportHisBean>> baggageSubHis(BaseFilterEntity operatorId) {
        return transform(getService().baggageSubHis(operatorId));
    }

    public Observable<List<LoadAndUnloadTodoBean>> reportTaskHis(BaseFilterEntity operatorId) {
        return transform(getService().reportTaskHis(operatorId));
    }

    public Observable<List<OutFieldTaskBean>> transportTaskHis(BaseFilterEntity operatorId) {
        return transform(getService().transportTaskHis(operatorId));
    }

    public Observable<List<LoadAndUnloadTodoBean>> stevedoresTaskHis(BaseFilterEntity operatorId) {
        return transform(getService().stevedoresTaskHis(operatorId));
    }

    public Observable<List<LoadAndUnloadTodoBean>> loadUnloadTaskHis(BaseFilterEntity operatorId) {
        return transform(getService().loadUnloadTaskHis(operatorId));
    }

    public Observable<String> exceptionContent(BaseFilterEntity exceptionContent) {
        return nothingtransform(getService().exceptionContent(exceptionContent));
    }

    public Observable<String> printRequest(BaseFilterEntity exceptionContent) {
        return nothingtransform(getService().printRequest(exceptionContent));
    }

    public Observable<IOManifestBean> getIOManifestList(BaseFilterEntity exceptionContent) {
        return transform(getService().getIOManifestList(exceptionContent));
    }

    public Observable<InventoryBean> getInventory(BaseFilterEntity exceptionContent) {
        return transform(getService().getInventory(exceptionContent));
    }

    public Observable<String> submitIOManifestList(SmInventoryEntryandexit exceptionContent) {
        return nothingtransform(getService().submitIOManifestList(exceptionContent));
    }

    public Observable<InWaybillRecordSubmitNewEntity.SingleLineBean> getWaybillInfoByCode(QueryWaybillInfoEntity entity) {
        return transform(getService().getWaybillInfoByCode(entity));
    }
    public Observable<WaybillsBean> getWaybillInfoByWaybillCode(String waybillCode) {
        return transform(getService().getWaybillInfoByWaybillCode(waybillCode));
    }
    public Observable <List<GroundAgentBean>> getAllAgent() {
        return transform(getService().getAllAgent());
    }
    public Observable <String> newScooter(ScooterTransitBean entity) {
        return nothingtransform(getService().newScooter(entity));
    }
}


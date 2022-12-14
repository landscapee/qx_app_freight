package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import com.jwenfeng.library.pulltorefresh.util.DisplayUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.UldAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.SaveOrUpdateEntity;
import qx.app.freight.qxappfreight.bean.request.SearchULDEntity;
import qx.app.freight.qxappfreight.bean.response.DeclareItem;
import qx.app.freight.qxappfreight.bean.response.FindAirlineAllBean;
import qx.app.freight.qxappfreight.bean.response.FtRuntimeFlightScooter;
import qx.app.freight.qxappfreight.bean.response.LikePageBean;
import qx.app.freight.qxappfreight.bean.response.ListByTypeBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.RecordsBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.FindAirlineAllContract;
import qx.app.freight.qxappfreight.contract.GetWeightContract;
import qx.app.freight.qxappfreight.contract.LikePageContract;
import qx.app.freight.qxappfreight.contract.ListByTypeContract;
import qx.app.freight.qxappfreight.contract.SaveOrUpdateContract;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.dialog.ReturnGoodsDialog;
import qx.app.freight.qxappfreight.model.ListByTypePresenter;
import qx.app.freight.qxappfreight.presenter.FindAirlineAllPresenter;
import qx.app.freight.qxappfreight.presenter.GetWeightPresenter;
import qx.app.freight.qxappfreight.presenter.LikePagePresenter;
import qx.app.freight.qxappfreight.presenter.SaveOrUpdatePresenter;
import qx.app.freight.qxappfreight.presenter.ScooterInfoListPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.utils.TransInformation;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * TODO : ??????????????????
 * Created by pr
 */
public class AddReceiveGoodActivity extends BaseActivity implements GetWeightContract.getWeightView, ScooterInfoListContract.scooterInfoListView, LikePageContract.likePageView, ListByTypeContract.listByTypeView, SaveOrUpdateContract.saveOrUpdateView, FindAirlineAllContract.findAirlineAllView {

    @BindView(R.id.sp_product)
    Spinner mSpProduct; //??????
    @BindView(R.id.ll_product)
    LinearLayout llProduct;
    @BindView(R.id.ll_overweight)
    LinearLayout llOverweight;
    @BindView(R.id.edt_overweight)
    TextView mEdtOverWeight;    //????????????
    @BindView(R.id.tv_product_name)
    TextView tvProductName;    //??????
    @BindView(R.id.edt_dead_weight)
    EditText mEdtDeadWeight;   //ULD??????
    //    @BindView(R.id.et_uldnumber)
//    EditText mEtUldNumber;     //Uld???
    @BindView(R.id.tv_cooter_weight)
    TextView mTvCooterWeight;  //????????????
    @BindView(R.id.tv_scooter)
    TextView mTvScooter;         //?????????
    @BindView(R.id.btn_takeweight)
    Button mBtnTakeWeight;      //??????
    @BindView(R.id.tv_weight)
    EditText mTvWeight;        //????????????
    @BindView(R.id.edt_volume)
    EditText mEdtVolume;        //??????
    @BindView(R.id.edt_number)
    EditText mEdtNumber;        //??????
    @BindView(R.id.btn_commit)
    Button mBtnCommit;          //??????
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.iv_scan)
    ImageView mIvScan;          //?????????
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    //    @BindView(R.id.rc_uld)
//    RecyclerView rcUld;
    @BindView(R.id.tv_uldnumber)
    TextView mTvUldnumber;

    private MyAgentListBean mMyAgentListBean;
    private String waybillId, waybillCode;
    private String cargoCn;
    private String mScooterCode;
    private ScooterInfoListBean scooterInfo;
    List <RcInfoOverweight> rcInfoOverweight; // ??????????????????
    private MyAgentListBean mList;


    private RecordsBean selectArea;//?????????????????????????????????

    private int tag;
    private String wayBillId;
    private String taskTypeCode;
    private String id;
    private String scooterId;
    private String scooterType;
    //??????
    private PopupWindow windowAdd;
    private View mPopAdd;
    private TextView tvTitleAdd, tvHandcarNumAdd, tvHandcarWeight;
    private EditText etULDWeight, etUldAirline, etUldNo, etUldType;
    private RecyclerView rcUldType, rcAirline;
    private ImageView ivCloseAdd, ivAdd;
    private UldAdapter uldAdapter;
    private UldAdapter uldTypeAdapter;
    private UldAdapter uldAirlineAdapter;
    private LinearLayout llSearchUld;
    private Button btnAllAdd;
    private List <String> ulds = new ArrayList <>();
    private List <String> uldTypes = new ArrayList <>();
    private List <String> airlineTwo = new ArrayList <>();
    private LikePageBean mLikePageBean = null;//???????????????uld
    private List <LikePageBean> LikePageBeans = new ArrayList <>();//uld?????????
    private List <ListByTypeBean.RecordsBean> uldTypeList = new ArrayList <>();//uld???????????????
    private boolean isShowAddUld = false; //true ??????????????? uld?????????
    private int uldCount = 0;//uld??????????????????
    private int uldTypeCount = 0;//uld??????????????????
    private int airlineCount = 0;//uld??????????????????
    private List <FindAirlineAllBean> findAirlineAllBeans = new ArrayList <>(); //??????????????????

    private boolean isPopWindow = false;

    private PopupWindow windowAll;
    private View mPopAll;

    private Button btnAll;

    /**
     * @param context
     * @param waybillId
     * @param waybillCode //     * @param declareItemBean  ???????????? ?????????
     * @param cargoCn     ?????????????????????
     */
    public static void startActivity(Activity context, String waybillId, String waybillCode, String cargoCn, MyAgentListBean declareItem, int tag, String wayBillId, String taskTypeCode, String id,RecordsBean selectArea) {
        Intent starter = new Intent(context, AddReceiveGoodActivity.class);
        starter.putExtra("waybillId", waybillId);
//        starter.putExtra("mScooterCode", mScooterCode);
        starter.putExtra("waybillCode", waybillCode);
        starter.putExtra("cargoCn", cargoCn);
        starter.putExtra("wayBillId", wayBillId);
        starter.putExtra("taskTypeCode", taskTypeCode);
        starter.putExtra("tag", tag);
        starter.putExtra("id", id);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("MyAgentListBean", (Serializable) declareItem);
        mBundle.putSerializable("selectArea", selectArea);

        starter.putExtras(mBundle);
        context.startActivityForResult(starter, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_receive_good;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setMainTitle(Color.WHITE, "??????");
        //????????????
        toolbar.setRightTextViewImage(this, View.VISIBLE, Color.RED, "??????", R.mipmap.delete_all,
                v -> {
                    mEdtOverWeight.setText("");
                    mEdtDeadWeight.setText("");
                    mTvUldnumber.setText("");
                    mTvCooterWeight.setText("");
                    mTvScooter.setText("");
                    mTvWeight.setText("");
                    mEdtVolume.setText("");
                    mEdtNumber.setText("");
                });
        initView();
        //??????
        mBtnCommit.setOnClickListener(v -> {
            if (StringUtil.isEmpty(mEdtNumber.getText().toString().trim())) {
                ToastUtil.showToast(this, "???????????????");
            } else if (StringUtil.isEmpty(mTvWeight.getText().toString().trim())) {
                ToastUtil.showToast(this, "???????????????");
            } else {
                if (Integer.valueOf(mEdtNumber.getText().toString()) <= 0){
                    ToastUtil.showToast(this, "???????????????0");
                    return;
                }
                if (StringUtil.isDouble(mTvWeight.getText().toString())){
                    if (Double.valueOf(mTvWeight.getText().toString()) <= 0){
                        ToastUtil.showToast(this, "???????????????0");
                        return;
                    }
                }
                else {
                    ToastUtil.showToast(this, "????????????????????????");
                    return;
                }
                int num = 0;
                int weight = 0;
                for (RcInfoOverweight rcInfoOverweight: rcInfoOverweight){
                    num = num+rcInfoOverweight.getCount();
                    weight= weight+rcInfoOverweight.getWeight();
                }
                if (Integer.valueOf(mEdtNumber.getText().toString()) < num){
                    ToastUtil.showToast("????????????????????????????????????");
                    return;
                }
                if (StringUtil.isDouble(mTvWeight.getText().toString())){
                    if (Double.valueOf(mTvWeight.getText().toString()) < weight){
                        ToastUtil.showToast("????????????????????????????????????");
                        return;
                    }
                }

                finishForResult();
            }
        });

        llAdd.setOnClickListener(v -> {
            showPopWindowListAdd(scooterInfo);
        });

        getAirlineData();

//        mEtUldNumber.setOnTouchListener((v, event) -> {
//            initPopupWindow();
////            Tools.showSoftKeyboard(mEtUldNumber);
////            return false;
//        });
        mTvUldnumber.setOnClickListener(v -> {
            if (null != scooterInfo) {
                if (!TextUtils.isEmpty(mTvScooter.getText()) || !scooterInfo.getScooterType().equals(Constants.SCOOTER_P))
                    showPopWindowUld();
                else
                    ToastUtil.showToast("?????????????????????????????????????????????????????????ULD???");
            } else
                ToastUtil.showToast("?????????????????????????????????????????????????????????ULD???");
        });

    }

    private void initView() {
        String content = "???<font color='#FF0000'>" + "*" + "</font>????????????";
        mTvInfo.setText(Html.fromHtml(content));
        mMyAgentListBean = new MyAgentListBean();
        waybillId = getIntent().getStringExtra("waybillId");
        wayBillId = getIntent().getStringExtra("wayBillId");
        taskTypeCode = getIntent().getStringExtra("taskTypeCode");
//        mScooterCode = getIntent().getStringExtra("mScooterCode");
        waybillCode = getIntent().getStringExtra("waybillCode");
        cargoCn = getIntent().getStringExtra("cargoCn");
        id = getIntent().getStringExtra("id");
        tag = getIntent().getIntExtra("tag", 0);
        mList = (MyAgentListBean) getIntent().getSerializableExtra("MyAgentListBean");
        selectArea = (RecordsBean) getIntent().getSerializableExtra("selectArea");
        if (2 == tag) {
            if (mList != null) {
                mEdtVolume.setText(mList.getVolume() + "");
                mEdtNumber.setText(mList.getNumber() + "");
                mTvWeight.setText(mList.getWeight() + "");
                mTvCooterWeight.setText(mList.getScooterWeight());
                mTvUldnumber.setText(mList.getUldCode());
                mEdtDeadWeight.setText(mList.getUldWeight() + "");
                mTvScooter.setText(mList.getScooterCode());
            }
        }
        tvProductName.setText(cargoCn);
        //??????
        mBtnTakeWeight.setOnClickListener(v -> {
            mPresenter = new GetWeightPresenter(this);
            ((GetWeightPresenter) mPresenter).getWeight("pb1");
        });
        rcInfoOverweight = new ArrayList <>();
        if (mList.getSpOverweight() != null && mList.getSpOverweight().size() > 0)
            rcInfoOverweight.addAll(mList.getSpOverweight());

        llOverweight.setOnClickListener(v -> {
            showPopWindowList();
        });
        mIvScan.setOnClickListener(v -> {
//            CustomCaptureActivity.startActivity(AddReceiveGoodActivity.this,"AddReceiveGoodActivity");
            ScanManagerActivity.startActivity(AddReceiveGoodActivity.this,"AddReceiveGoodActivity");
        });
    }

    //????????????????????????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (result.getFunctionFlag().equals("AddReceiveGoodActivity")) {
            if (result.getData() != null && result.getData().length() == Constants.SCOOTER_NO_LENGTH) {
                //?????????
                mScooterCode = result.getData();
                if (!TextUtils.isEmpty(mScooterCode)) {
                    //?????????
                    getNumberInfo(mScooterCode);
                }
            } else {
                ToastUtil.showToast("????????????????????????????????????");
            }
        }
    }

//    //????????????
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (Constants.SCAN_RESULT == resultCode) {
//            mScooterCode = data.getStringExtra(Constants.SACN_DATA);
//            if (mScooterCode != null && mScooterCode.length() == Constants.SCOOTER_NO_LENGTH) {
//                if (!"".equals(mScooterCode)) {
//                    //?????????
//                    getNumberInfo(mScooterCode);
//                } else {
//                    ToastUtil.showToast(AddReceiveGoodActivity.this, "?????????????????????????????????");
//                }
//            }
//            else {
//                ToastUtil.showToast("????????????????????????????????????");
//            }
//
//        }
//    }

    //????????????
    private String setCargoCn(List <DeclareItem> data) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            str.append(data.get(i).getCargoCn() + ",");
        }
        return str.substring(0, str.length() - 1);
    }


    private void finishForResult() {
        //id
        if (!StringUtil.isEmpty(mList.getRepId())){
            mMyAgentListBean.setRepId(mList.getRepId());
            mMyAgentListBean.setReservoirType(mList.getReservoirType());
        }
        else {
            mMyAgentListBean.setRepId(selectArea.getId());
            mMyAgentListBean.setReservoirType(selectArea.getReservoirType());
        }

        mMyAgentListBean.setId(mList.getId());
        mMyAgentListBean.setScooterId(scooterId);
        mMyAgentListBean.setScooterType(scooterType);
        mMyAgentListBean.setAddOrderId(id);
        //??????id
        mMyAgentListBean.setWaybillId(waybillId);
        //
        mMyAgentListBean.setTaskTypeCode(taskTypeCode);



        mMyAgentListBean.setWaybillCode(waybillCode);
        //??????
        mMyAgentListBean.setCargoCn(tvProductName.getText().toString());
        //??????
        mMyAgentListBean.setNumber(Integer.valueOf(mEdtNumber.getText().toString().trim()));
        //??????
        if ("".equals(mEdtVolume.getText().toString())) {
            mMyAgentListBean.setVolume(0);
        } else {
            mMyAgentListBean.setVolume(Double.valueOf(mEdtVolume.getText().toString().trim()));
        }
        if ("".equals(mTvWeight.getText().toString())) {
            mMyAgentListBean.setWeight(0);
        } else {
            mMyAgentListBean.setWeight(Double.valueOf(mTvWeight.getText().toString().trim()));
        }
        //????????????
        mMyAgentListBean.setScooterWeight(mTvCooterWeight.getText().toString().trim());
        //??????ULD????????????10???
        if (mTvUldnumber.getText().toString().length() == 10) {
            if (mLikePageBean != null) {
                //ULD???
                mMyAgentListBean.setUldCode(mLikePageBean.getUldCode());
                //ULD Id
                mMyAgentListBean.setUldId(mLikePageBean.getId());
                //ULD TYPE
                mMyAgentListBean.setUldType(mLikePageBean.getUldType());

                mMyAgentListBean.setIata(mLikePageBean.getIata());
                //ULD??????
                mMyAgentListBean.setUldWeight(Integer.valueOf(mEdtDeadWeight.getText().toString().trim()));
            } else {
                ToastUtil.showToast("ULD????????????");
                return;
            }

        }
//        else {
//            mMyAgentListBean.setUldWeight(0);
//        }
        //?????????
        mMyAgentListBean.setScooterCode(mTvScooter.getText().toString().trim());


        mMyAgentListBean.setSpOverweight(rcInfoOverweight);
        mPresenter = new ScooterInfoListPresenter(this);
        ((ScooterInfoListPresenter) mPresenter).addInfo(mMyAgentListBean);
    }

    //??????????????????
    public void getNumberInfo(String mScooterCode) {
        mPresenter = new ScooterInfoListPresenter(this);
        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
        MyAgentListBean myAgentListBean = new MyAgentListBean();
        baseFilterEntity.setSize(10);
        baseFilterEntity.setCurrent(1);
        myAgentListBean.setScooterCode(mScooterCode);
        baseFilterEntity.setFilter(myAgentListBean);
        ((ScooterInfoListPresenter) mPresenter).ScooterInfoListForReceicve(baseFilterEntity);
    }

    private void getAirlineData() {
        mPresenter = new FindAirlineAllPresenter(this);
        ((FindAirlineAllPresenter) mPresenter).findAirlineAll();
    }

    //??????
    @Override
    public void getWeightResult(String result) {
        if (!"".equals(result))
            mTvWeight.setText(result);
        else
            ToastUtil.showToast("?????????????????????????????????????????????");
    }

    private void finishAndToast() {
        ToastUtil.showToast("??????????????????????????????????????????");
    }

    //??????????????????????????????????????????????????????
    @Override
    public void scooterInfoListResult(List <ScooterInfoListBean> scooterInfoListBeans) {

    }

    @Override
    public void scooterInfoListForReceiveResult(List <ScooterInfoListBean> scooterInfoListBeans) {
        if (scooterInfoListBeans != null && scooterInfoListBeans.size() > 0) {
            scooterInfo = scooterInfoListBeans.get(0);
            scooterId = scooterInfoListBeans.get(0).getId();
            scooterType = scooterInfoListBeans.get(0).getScooterType();
            //????????????????????????????????? ???
            if (Constants.SCOOTER_P.equals(scooterInfoListBeans.get(0).getScooterType())) {
                mTvScooter.setText(scooterInfoListBeans.get(0).getScooterCode());
                mTvCooterWeight.setText(scooterInfoListBeans.get(0).getScooterWeight() + "");
            } else {
                //?????????
                mTvScooter.setText(scooterInfoListBeans.get(0).getScooterCode());
                //????????????
                mTvCooterWeight.setText(scooterInfoListBeans.get(0).getScooterWeight() + "");
            }
//            ((ScooterInfoListPresenter) mPresenter).exist(scooterInfoListBeans.get(0).getId());
        } else {
            finishAndToast();
        }
    }

    @Override
    public void existResult(MyAgentListBean existBean) {
        //????????????????????????????????????
        int deadWeight = existBean.getUldWeight() == 0 ? 0 : existBean.getUldWeight();
        //uld???
//        mEtUldNumber.setText(existBean.getUldCode() == null ? "" : existBean.getUldCode());
        //uld??????
//        if (deadWeight == 0)
//            mEdtDeadWeight.setText("");
//        else
//            mEdtDeadWeight.setText(deadWeight + "");
    }

    /**
     * ????????????????????????????????????
     *
     * @param result
     */
    @Override
    public void addInfoResult(MyAgentListBean result) {
        if (null != result) {
            Intent intent = new Intent();
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("mMyAgentListBean", result);
            intent.putExtras(mBundle);
            setResult(Constants.FINISH_REFRESH, intent);
            finish();
        } else
            Log.e("123", "123");
    }


    @Override
    public void toastView(String error) {
        ToastUtil.showToast(this, error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("?????????????????????");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }


    private void showPopWindowList() {
        ReturnGoodsDialog dialog = new ReturnGoodsDialog(this);
        dialog.setData(rcInfoOverweight)
                .setOnClickListener(new ReturnGoodsDialog.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        mEdtOverWeight.setText(text);
                    }
                })
                .show();

    }

    private void addUld(SaveOrUpdateEntity mSaveOrUpdateEntity) {
        mPresenter = new SaveOrUpdatePresenter(this);
        ((SaveOrUpdatePresenter) mPresenter).saveOrUpdate(mSaveOrUpdateEntity);
    }

    /**
     * ?????????????????? ??????ULD ???
     *
     * @param scooterInfoListBean
     */
    private void showPopWindowListAdd(ScooterInfoListBean scooterInfoListBean) {
        isPopWindow = true;
        initPopupWindowAdd();
        setDataPop3(scooterInfoListBean);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        windowAdd.setAnimationStyle(R.style.animTranslate);
        windowAdd.showAtLocation(btnAllAdd, Gravity.CENTER, 0, 0);
    }


    private void initPopupWindow() {
        if (windowAll == null) {
            mPopAll = getLayoutInflater().inflate(R.layout.popup_uld_list, null);
            windowAll = new PopupWindow(mPopAll,
                    DisplayUtil.dp2Px(this, 340),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // ??????popuwindow???????????????
            windowAll.setOutsideTouchable(true);
            btnAll = mPopAll.findViewById(R.id.btn_submit);
            ImageView ivClose = mPopAll.findViewById(R.id.iv_close);
            EditText mEtUldNumber = mPopAll.findViewById(R.id.et_uldnumber);
            RecyclerView rcUld = mPopAll.findViewById(R.id.rc_uld);
            windowAll.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            windowAll.setFocusable(true);

            mEtUldNumber.setOnTouchListener((v, event) -> {
                Tools.showSoftKeyboard(mEtUldNumber);
                return false;
            });

            mEtUldNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!StringUtil.isEmpty(mEtUldNumber.getText().toString())) {
                        uldCount = mEtUldNumber.getText().toString().length();
                        searchUld(mEtUldNumber.getText().toString());
                    } else {
                        ulds.clear();
                        uldAdapter.notifyDataSetChanged();
                    }
                }
            });

            //Uld??????????????????
            rcUld.setLayoutManager(new LinearLayoutManager(this));
            uldAdapter = new UldAdapter(ulds);
            rcUld.setAdapter(uldAdapter);
            uldAdapter.setOnItemClickListener((result, position) -> {
//                mEtUldNumber.setText(result);
                mLikePageBean = LikePageBeans.get(position);
                mTvUldnumber.setText(result);
                mEdtDeadWeight.setText(mLikePageBean.getUldWeight());
                ulds.clear();
                uldAdapter.notifyDataSetChanged();
                dismissPopWindows();
            });


            ivClose.setOnClickListener((v) -> {
                dismissPopWindows();
            });
            btnAll.setOnClickListener((v) -> {

            });
            windowAll.setOnDismissListener(() -> {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
                isPopWindow = false;
            });
        }
//        setDataPop1();
    }

    private void showPopWindowUld() {
        initPopupWindow();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        windowAll.setAnimationStyle(R.style.animTranslate);
        windowAll.showAtLocation(btnAll, Gravity.CENTER, 0, 0);
    }

    private void dismissPopWindows() {
        if (windowAll != null) {
            windowAll.dismiss();
            windowAll = null;
        }
    }


    private void initPopupWindowAdd() {
        if (windowAdd == null) {
            mPopAdd = getLayoutInflater().inflate(R.layout.popup_add_handcar, null);
            windowAdd = new PopupWindow(mPopAdd,
                    DisplayUtil.dp2Px(AddReceiveGoodActivity.this, 340),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // ??????popuwindow???????????????
            windowAdd.setOutsideTouchable(true);
            tvTitleAdd = mPopAdd.findViewById(R.id.tv_title_add);
            llSearchUld = mPopAdd.findViewById(R.id.ll_search_uld);
            etUldAirline = mPopAdd.findViewById(R.id.et_uld_airline);
            etUldNo = mPopAdd.findViewById(R.id.et_uld_number);
            etUldType = mPopAdd.findViewById(R.id.et_uld_type);
            rcUldType = mPopAdd.findViewById(R.id.rc_uld_type);
            rcAirline = mPopAdd.findViewById(R.id.rc_airline);
            etULDWeight = mPopAdd.findViewById(R.id.et_uld_weight);
            btnAllAdd = mPopAdd.findViewById(R.id.btn_submit_add);
            ivCloseAdd = mPopAdd.findViewById(R.id.iv_close_add);
//            ivAdd = mPopAdd.findViewById(R.id.iv_add);
            windowAdd.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            windowAdd.setFocusable(true);
            rcUldType.setLayoutManager(new LinearLayoutManager(this));
            uldTypeAdapter = new UldAdapter(uldTypes);
            rcUldType.setAdapter(uldTypeAdapter);
            rcAirline.setLayoutManager(new LinearLayoutManager(this));
            uldAirlineAdapter = new UldAdapter(airlineTwo);
            rcAirline.setAdapter(uldAirlineAdapter);
            uldTypeAdapter.setOnItemClickListener((result, position) -> {
                etUldType.setText(result);
            });
            uldAirlineAdapter.setOnItemClickListener((result, position) -> {
                String airlineTwo = result.substring(0, 2);//????????????
                etUldAirline.setText(airlineTwo);
            });

            etULDWeight.setOnTouchListener((v, event) -> {
                Tools.showSoftKeyboard(etULDWeight);
                return false;
            });
            ivCloseAdd.setOnClickListener((v) -> {
//                ulds.clear();
                uldTypes.clear();
//                uldAdapter.notifyDataSetChanged();
                uldTypeAdapter.notifyDataSetChanged();
                dismissPopWindowsAdd();
            });
            etUldType.setTransformationMethod(new TransInformation());
            etUldType.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!StringUtil.isEmpty(etUldType.getText().toString())) {
                        uldTypeCount = etUldType.getText().toString().length();
                        if (uldTypeCount > 0)
                            searchUldType(etUldType.getText().toString());
                    } else {
                        uldTypes.clear();
                        uldTypeAdapter.notifyDataSetChanged();
                    }
                }
            });
            etUldAirline.setTransformationMethod(new TransInformation());
            etUldAirline.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!StringUtil.isEmpty(etUldAirline.getText().toString())) {
                        airlineCount = etUldAirline.getText().toString().length();
                        if (airlineCount > 0)
                            searchAirline(etUldAirline.getText().toString());
                    } else {
                        airlineTwo.clear();
                        uldAirlineAdapter.notifyDataSetChanged();
                    }

                }
            });
            btnAllAdd.setOnClickListener((v) -> {
                LikePageBean mLikePageBeanNew = new LikePageBean();
                if (StringUtil.isEmpty(etUldNo.getText().toString()) || StringUtil.isEmpty(etUldType.getText().toString())
                        || StringUtil.isEmpty(etUldAirline.getText().toString()) || StringUtil.isEmpty(etULDWeight.getText().toString())
                        || etUldNo.getText().toString().length() < 5 || etUldAirline.getText().toString().length() < 2
                        || etUldType.getText().toString().length() < 3) {
                    ToastUtil.showToast("??????????????????ULD??????");
                    return;
                } else {
                    SaveOrUpdateEntity mSaveOrUpdateEntity = new SaveOrUpdateEntity();
                    mSaveOrUpdateEntity.setUldCode(etUldNo.getText().toString());
                    mSaveOrUpdateEntity.setUldType(etUldType.getText().toString());
                    mSaveOrUpdateEntity.setIata(etUldAirline.getText().toString());
                    mSaveOrUpdateEntity.setUldWeight(etULDWeight.getText().toString());

                    mLikePageBeanNew.setIata(etUldAirline.getText().toString());
                    mLikePageBeanNew.setUldCode(etUldNo.getText().toString());
                    mLikePageBeanNew.setUldType(etUldType.getText().toString());
                    mLikePageBeanNew.setUldWeight(etULDWeight.getText().toString());
                    mLikePageBean = mLikePageBeanNew;
                    addUld(mSaveOrUpdateEntity);
                }
                dismissPopWindowsAdd();
            });
            windowAdd.setOnDismissListener(() -> {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
                isPopWindow = false;
            });
        }
        setDataPop3(null);
    }

    private void dismissPopWindowsAdd() {
        if (windowAdd != null) {
            isShowAddUld = false;
            llSearchUld.setVisibility(View.GONE);
            etUldType.setText("");
            etUldNo.setText("");
            etUldAirline.setText("");
            etULDWeight.setText("");
            windowAdd.dismiss();
            windowAdd = null;
        }
    }

    private void searchAirline(String iata) {
        iata = iata.toUpperCase();
        List <FindAirlineAllBean> airLineBeans = new ArrayList <>();
        airLineBeans.clear();
        for (FindAirlineAllBean mFindAirlineAllBean : findAirlineAllBeans) {
            if (mFindAirlineAllBean.getIata().contains(iata)) {
                airLineBeans.add(mFindAirlineAllBean);
            }
        }
        airlineTwo.clear();
        if (airlineCount == 2) {
            if (airLineBeans.size() < 1)
                etUldAirline.setText("");
            airLineBeans.clear();
        }
        for (FindAirlineAllBean mFindAirlineAllBean : airLineBeans) {
            airlineTwo.add(mFindAirlineAllBean.getIata() + "-" + mFindAirlineAllBean.getShortname());
        }
        uldAirlineAdapter.notifyDataSetChanged();
    }

    private void setDataPop3(ScooterInfoListBean mScooterInfoListBean) {
        if (mScooterInfoListBean != null) {
        }
    }

    /**
     * ??????????????????uld
     */
    private void searchUld(String iata) {
        iata = iata.toUpperCase();
        mPresenter = new LikePagePresenter(this);
        BaseFilterEntity <FtRuntimeFlightScooter> entity = new BaseFilterEntity();
        FtRuntimeFlightScooter mFtRuntimeFlightScooter = new FtRuntimeFlightScooter();
        mFtRuntimeFlightScooter.setIata(iata);
        entity.setCurrent(1);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setFilter(mFtRuntimeFlightScooter);
        ((LikePagePresenter) mPresenter).likePage(entity);
    }

    /**
     * ????????????uld??????
     */
    private void searchUldType(String uldType) {
        uldType = uldType.toUpperCase();
        mPresenter = new ListByTypePresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        SearchULDEntity mSearch = new SearchULDEntity();
        mSearch.setName(uldType);
        mSearch.setType("1");
        entity.setFilter(mSearch);
        entity.setSize(50);
        entity.setCurrent(1);
        ((ListByTypePresenter) mPresenter).listByType(entity);
    }

    @Override
    public void likePageResult(List <LikePageBean> result) {
        if (uldCount == 10 && result.size() == 1) {//10???uld???????????????????????????????????????uld???
            ulds.clear();
            LikePageBeans.clear();
            uldAdapter.notifyDataSetChanged();
        } else {
            ulds.clear();
            LikePageBeans.clear();
            LikePageBeans.addAll(result);
            for (LikePageBean mLikePageBean : result) {
                ulds.add(mLikePageBean.getUldType() + mLikePageBean.getUldCode() + mLikePageBean.getIata());
            }
            uldAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void listByTypeResult(ListByTypeBean result) {
        if (uldTypeCount == 3) {//10???uld???????????????????????????????????????uld???
            if (result.getRecords().size() < 1)
                etUldType.setText("");
            uldTypes.clear();
            uldTypeList.clear();
            uldTypeAdapter.notifyDataSetChanged();
        } else {
            uldTypes.clear();
            uldTypeList.clear();
            uldTypeList.addAll(result.getRecords());
            for (ListByTypeBean.RecordsBean mListByTypeBean : result.getRecords()) {
                uldTypes.add(mListByTypeBean.getName());
            }
            uldTypeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void saveOrUpdateResult(String result) {

        if (mLikePageBean != null) {
            mTvUldnumber.setText(mLikePageBean.getUldType() + mLikePageBean.getUldCode() + mLikePageBean.getIata());
            mEdtDeadWeight.setText(mLikePageBean.getUldWeight());
        }
        ToastUtil.showToast(result);
    }

    @Override
    public void findAirlineAllResult(List <FindAirlineAllBean> result) {
        findAirlineAllBeans.clear();
        findAirlineAllBeans.addAll(result);
    }
}

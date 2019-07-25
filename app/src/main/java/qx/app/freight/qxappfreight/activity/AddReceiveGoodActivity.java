package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * TODO : 新增收货页面
 * Created by pr
 */
public class AddReceiveGoodActivity extends BaseActivity implements GetWeightContract.getWeightView, ScooterInfoListContract.scooterInfoListView, LikePageContract.likePageView, ListByTypeContract.listByTypeView, SaveOrUpdateContract.saveOrUpdateView, FindAirlineAllContract.findAirlineAllView {

    @BindView(R.id.sp_product)
    Spinner mSpProduct; //品名
    @BindView(R.id.ll_product)
    LinearLayout llProduct;
    @BindView(R.id.ll_overweight)
    LinearLayout llOverweight;
    @BindView(R.id.edt_overweight)
    TextView mEdtOverWeight;    //超重重量
    @BindView(R.id.tv_product_name)
    TextView tvProductName;    //品名
    @BindView(R.id.edt_dead_weight)
    EditText mEdtDeadWeight;   //ULD自重
    //    @BindView(R.id.et_uldnumber)
//    EditText mEtUldNumber;     //Uld号
    @BindView(R.id.tv_cooter_weight)
    TextView mTvCooterWeight;  //板车重量
    @BindView(R.id.tv_scooter)
    TextView mTvScooter;         //板车号
    @BindView(R.id.btn_takeweight)
    Button mBtnTakeWeight;      //重量
    @BindView(R.id.tv_weight)
    EditText mTvWeight;        //输入重量
    @BindView(R.id.edt_volume)
    EditText mEdtVolume;        //体积
    @BindView(R.id.edt_number)
    EditText mEdtNumber;        //件数
    @BindView(R.id.btn_commit)
    Button mBtnCommit;          //提交
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.iv_scan)
    ImageView mIvScan;          //扫一扫
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
    List<RcInfoOverweight> rcInfoOverweight; // 超重记录列表
    private MyAgentListBean mList;
    private int tag;
    private String wayBillId;
    private String taskTypeCode;
    private String id;
    private String scooterId;
    private String scooterType;
    //新增
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
    private List<String> ulds = new ArrayList<>();
    private List<String> uldTypes = new ArrayList<>();
    private List<String> airlineTwo = new ArrayList<>();
    private LikePageBean mLikePageBean = null;//当前选中的uld
    private List<LikePageBean> LikePageBeans = new ArrayList<>();//uld数据源
    private List<ListByTypeBean.RecordsBean> uldTypeList = new ArrayList<>();//uld类型数据源
    private boolean isShowAddUld = false; //true 为已经展开 uld新增。
    private int uldCount = 0;//uld输入框的字数
    private int uldTypeCount = 0;//uld输入框的字数
    private int airlineCount = 0;//uld输入框的字数
    private List<FindAirlineAllBean> findAirlineAllBeans = new ArrayList<>(); //所有航司数据
    //当前航段三字码
    private boolean isPopWindow = false;

    private PopupWindow windowAll;
    private View mPopAll;

    private Button btnAll;

    /**
     * @param context
     * @param waybillId
     * @param waybillCode //     * @param declareItemBean  品名列表 已取消
     * @param cargoCn     品名以逗号隔开
     */
    public static void startActivity(Activity context, String waybillId, String waybillCode, String cargoCn, MyAgentListBean declareItem, int tag, String wayBillId, String taskTypeCode, String id) {
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
        toolbar.setMainTitle(Color.WHITE, "新增");
        //点击清空
        toolbar.setRightTextViewImage(this, View.VISIBLE, Color.RED, "清空", R.mipmap.delete_all,
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
        //提交
        mBtnCommit.setOnClickListener(v -> {
            if ("".equals(mEdtNumber.getText().toString().trim())) {
                ToastUtil.showToast(this, "请输入件数");
            } else if ("".equals(mEdtNumber.getText().toString().trim())) {
                ToastUtil.showToast(this, "请输入重量");
            } else {
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
                if (!TextUtils.isEmpty(mTvScooter.getText()) || !scooterInfo.getScooterType().equals("2"))
                    showPopWindowUld();
                else
                    ToastUtil.showToast("板车号为空或者板车类型为平板车不能输入ULD号");
            } else
                ToastUtil.showToast("板车号为空或者板车类型为平板车不能输入ULD号");
        });

    }

    private void initView() {
        String content = "带<font color='#FF0000'>" + "*" + "</font>为必填项";
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
        //取重
        mBtnTakeWeight.setOnClickListener(v -> {
            mPresenter = new GetWeightPresenter(this);
            ((GetWeightPresenter) mPresenter).getWeight("pb1");
        });
        rcInfoOverweight = new ArrayList<>();
        llOverweight.setOnClickListener(v -> {
            showPopWindowList();
        });
        mIvScan.setOnClickListener(v -> ScanManagerActivity.startActivity(AddReceiveGoodActivity.this));
    }

    //激光扫码获取返回板车信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (result.getFunctionFlag().equals("AddReceiveGoodActivity")) {
            //板车号
            mScooterCode = result.getData();
            if (!TextUtils.isEmpty(mScooterCode)) {
                //板车号
                getNumberInfo(mScooterCode);
            }
        }
    }

    //普通扫码
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Constants.SCAN_RESULT == resultCode) {
            mScooterCode = data.getStringExtra(Constants.SACN_DATA);
            if (!"".equals(mScooterCode)) {
                //板车号
                getNumberInfo(mScooterCode);
            } else {
                ToastUtil.showToast(AddReceiveGoodActivity.this, "扫码数据为空请重新扫码");
            }
        }
    }

    //品名叠加
    private String setCargoCn(List<DeclareItem> data) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            str.append(data.get(i).getCargoCn() + ",");
        }
        return str.substring(0, str.length() - 1);
    }


    private void finishForResult() {
        //id
        mMyAgentListBean.setId(mList.getId());
        mMyAgentListBean.setScooterId(scooterId);
        mMyAgentListBean.setScooterType(scooterType);
        mMyAgentListBean.setAddOrderId(id);
        //运单id
        mMyAgentListBean.setWaybillId(waybillId);
        //
        mMyAgentListBean.setTaskTypeCode(taskTypeCode);

        mMyAgentListBean.setWaybillCode(waybillCode);
        //品名
        mMyAgentListBean.setCargoCn(tvProductName.getText().toString());
        //件数
        mMyAgentListBean.setNumber(Integer.valueOf(mEdtNumber.getText().toString().trim()));
        //体积
        if ("".equals(mEdtVolume.getText().toString())) {
            mMyAgentListBean.setVolume(0);
        } else {
            mMyAgentListBean.setVolume(Double.valueOf(mEdtVolume.getText().toString().trim()));
        }
        //重量
        if ("".equals(mTvWeight.getText().toString())) {
            mMyAgentListBean.setWeight(0);
        } else {
            mMyAgentListBean.setWeight(Double.valueOf(mTvWeight.getText().toString().trim()));
        }
        //板车自重
        mMyAgentListBean.setScooterWeight(mTvCooterWeight.getText().toString().trim());
        //判断ULD号是否为10位
        if (mTvUldnumber.getText().toString().length() == 10) {
            if (mLikePageBean != null) {
                //ULD号
                mMyAgentListBean.setUldCode(mLikePageBean.getUldCode());
                //ULD Id
                mMyAgentListBean.setUldId(mLikePageBean.getId());
                //ULD TYPE
                mMyAgentListBean.setUldType(mLikePageBean.getUldType());

                mMyAgentListBean.setIata(mLikePageBean.getIata());
                //ULD自重
                mMyAgentListBean.setUldWeight(Integer.valueOf(mEdtDeadWeight.getText().toString().trim()));
            } else {
                ToastUtil.showToast("ULD输入有误");
                return;
            }

        }
//        else {
//            mMyAgentListBean.setUldWeight(0);
//        }
        //板车号
        mMyAgentListBean.setScooterCode(mTvScooter.getText().toString().trim());


        mMyAgentListBean.setRcInfoOverweight(rcInfoOverweight);
        mPresenter = new ScooterInfoListPresenter(this);
        ((ScooterInfoListPresenter) mPresenter).addInfo(mMyAgentListBean);
    }

    //获取板车信息
    public void getNumberInfo(String mScooterCode) {
        mPresenter = new ScooterInfoListPresenter(this);
        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
        MyAgentListBean myAgentListBean = new MyAgentListBean();
        baseFilterEntity.setSize(10);
        baseFilterEntity.setCurrent(1);
        myAgentListBean.setScooterCode(mScooterCode);
        baseFilterEntity.setFilter(myAgentListBean);
        ((ScooterInfoListPresenter) mPresenter).ScooterInfoList(baseFilterEntity);
    }

    private void getAirlineData() {
        mPresenter = new FindAirlineAllPresenter(this);
        ((FindAirlineAllPresenter) mPresenter).findAirlineAll();
    }

    //取重
    @Override
    public void getWeightResult(String result) {
        if (!"".equals(result))
            mTvWeight.setText(result);
        else
            ToastUtil.showToast("未拿到取重数据，请重新点击取重");
    }

    private void finishAndToast() {
        ToastUtil.showToast("未获取到板车信息，请手动输入");
    }

    //根据扫一扫获取到的板车号查询获得数据
    @Override
    public void scooterInfoListResult(List<ScooterInfoListBean> scooterInfoListBeans) {
        if (scooterInfoListBeans != null && scooterInfoListBeans.size() > 0) {
            scooterInfo = scooterInfoListBeans.get(0);
            scooterId = scooterInfoListBeans.get(0).getId();
            scooterType = scooterInfoListBeans.get(0).getScooterType();
            if ("2".equals(scooterInfoListBeans.get(0).getScooterType())) {
                mTvScooter.setText(scooterInfoListBeans.get(0).getScooterCode());
                mTvCooterWeight.setText(scooterInfoListBeans.get(0).getScooterWeight() + "");
            } else {
                //板车号
                mTvScooter.setText(scooterInfoListBeans.get(0).getScooterCode());
                //板车重量
                mTvCooterWeight.setText(scooterInfoListBeans.get(0).getScooterWeight() + "");
            }
//            ((ScooterInfoListPresenter) mPresenter).exist(scooterInfoListBeans.get(0).getId());
        } else {
            finishAndToast();
        }
    }

    @Override
    public void existResult(MyAgentListBean existBean) {
        //如果返回为空就让用户输入
        int deadWeight = existBean.getUldWeight() == 0 ? 0 : existBean.getUldWeight();
        //uld号
//        mEtUldNumber.setText(existBean.getUldCode() == null ? "" : existBean.getUldCode());
        //uld重量
//        if (deadWeight == 0)
//            mEdtDeadWeight.setText("");
//        else
//            mEdtDeadWeight.setText(deadWeight + "");
    }

    //提交
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
        showProgessDialog("数据提交中……");
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
     * 弹出新增板车 输入ULD 框
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
            // 点击popuwindow外让其消失
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

            //Uld模糊查询关联
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
            // 点击popuwindow外让其消失
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
                String airlineTwo = result.substring(0, 2);//截取航司
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
                    ToastUtil.showToast("请填写完整的ULD信息");
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
        List<FindAirlineAllBean> airLineBeans = new ArrayList<>();
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
     * 模糊搜索整个uld
     */
    private void searchUld(String iata) {
        iata = iata.toUpperCase();
        mPresenter = new LikePagePresenter(this);
        BaseFilterEntity<FtRuntimeFlightScooter> entity = new BaseFilterEntity();
        FtRuntimeFlightScooter mFtRuntimeFlightScooter = new FtRuntimeFlightScooter();
        mFtRuntimeFlightScooter.setIata(iata);
        entity.setCurrent(1);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setFilter(mFtRuntimeFlightScooter);
        ((LikePagePresenter) mPresenter).likePage(entity);
    }

    /**
     * 模糊搜索uld类型
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
    public void likePageResult(List<LikePageBean> result) {
        if (uldCount == 10 && result.size() == 1) {//10位uld号已经输完，并且检测到了该uld号
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
        if (uldTypeCount == 3) {//10位uld号已经输完，并且检测到了该uld号
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
    public void findAirlineAllResult(List<FindAirlineAllBean> result) {
        findAirlineAllBeans.clear();
        findAirlineAllBeans.addAll(result);
    }
}

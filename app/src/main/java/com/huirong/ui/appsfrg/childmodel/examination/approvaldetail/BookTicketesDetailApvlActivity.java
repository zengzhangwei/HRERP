package com.huirong.ui.appsfrg.childmodel.examination.approvaldetail;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huirong.R;
import com.huirong.application.MyApplication;
import com.huirong.base.BaseActivity;
import com.huirong.common.MyException;
import com.huirong.dialog.Loading;
import com.huirong.helper.UserHelper;
import com.huirong.inject.ViewInject;
import com.huirong.model.MyApprovalModel;
import com.huirong.model.approvaldetailmodel.BookTicketsApvlModel;
import com.huirong.utils.PageUtil;


/**
 * 审批 订票详情
 * Created by sjy on 2016/12/2.
 */

public class BookTicketesDetailApvlActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;
    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;
    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;


    //申请人
    @ViewInject(id = R.id.tv_ApprovalPerson)
    TextView tv_ApprovalPerson;

    //部门
    @ViewInject(id = R.id.tv_approvaldept)
    TextView tv_approvaldept;

    //公司
    @ViewInject(id = R.id.tv_approvalCo)
    TextView tv_approvalCo;

    //申请时间
    @ViewInject(id = R.id.tv_approvalTime)
    TextView tv_approvalTime;



    //交通工具
    @ViewInject(id = R.id.tv_type)
    TextView tv_type;

    //出发地
    @ViewInject(id = R.id.tv_startPlace)
    TextView tv_startPlace;

    //目的地
    @ViewInject(id = R.id.tv_endPlace)
    TextView tv_endPlace;

    //订票事由
    @ViewInject(id = R.id.tv_reason, click = "ReasonExpended")
    TextView tv_reason;

    //开始时间
    @ViewInject(id = R.id.tv_startTime)
    TextView tv_startTime;

    //结束时间
    @ViewInject(id = R.id.tv_endTime)
    TextView tv_endTime;



    //未审批bottom
    @ViewInject(id = R.id.laytout_decide)
    LinearLayout laytout_decide;

    //驳回
    @ViewInject(id = R.id.btn_refulse, click = "forRefulse")
    Button btn_refulse;

    //批准
    @ViewInject(id = R.id.btn_commit, click = "toForCommit")
    Button btn_commit;

    //转交
    @ViewInject(id = R.id.btn_transfer, click = "forTransfer")
    Button btn_transfer;

    //审批bottom
    @ViewInject(id = R.id.laytout_copy)
    LinearLayout laytout_copy;

    //抄送
    @ViewInject(id = R.id.btn_copytp, click = "forCopyto")
    Button btn_copytp;

    //变量
    private MyApprovalModel myApprovalModel;
    private BookTicketsApvlModel model;
    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_booktickets_d2);
        tv_title.setText(getResources().getString(R.string.book_title_d));
        tv_right.setText("");

        Bundle bundle = this.getIntent().getExtras();
        myApprovalModel = (MyApprovalModel) bundle.getSerializable("MyApprovalModel");

        bottomType();
        //
        getDetailData();

        MyApplication.getInstance().addACT(this);
    }

    private void setShow() {
        tv_ApprovalPerson.setText(model.getEmployeeName());
        tv_approvaldept.setText(model.getDepartmentName());
        tv_approvalCo.setText(model.getStoreName());
        tv_approvalTime.setText(model.getApplicationCreateTime());

        tv_type.setText(model.getTraffic());
        tv_startPlace.setText(model.getStartAddress());
        tv_endPlace.setText(model.getEndAddress());
        tv_startTime.setText(model.getStartTime());
        tv_endTime.setText(model.getEndTime());
        tv_reason.setText(model.getReason());

    }

    private void bottomType() {
        //
        if (myApprovalModel.getApprovalStatus().contains("1")) {

            laytout_decide.setVisibility(View.GONE);
            laytout_copy.setVisibility(View.VISIBLE);

        } else {
            laytout_decide.setVisibility(View.VISIBLE);
            laytout_copy.setVisibility(View.GONE);
        }
    }

    private void getDetailData() {
        Loading.run(this, new Runnable() {
            @Override
            public void run() {

                //泛型
                try {
                    BookTicketsApvlModel model1 = new UserHelper<>(BookTicketsApvlModel.class).approvalDetailPost(BookTicketesDetailApvlActivity.this,
                            myApprovalModel.getApplicationID(),
                            myApprovalModel.getApplicationType());
                    sendMessage(POST_SUCCESS, model1);
                } catch (MyException e) {
                    e.printStackTrace();
                    sendMessage(POST_FAILED, e.getMessage());
                }
            }
        });
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case POST_SUCCESS:
                model = (BookTicketsApvlModel) msg.obj;
                setShow();
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
            default:
                break;
        }
    }

    //驳回
    public void forRefulse(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonDisagreeActivity.class, bundle);
    }

    //
    public void toForCommit(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonAgreeActivity.class, bundle);
    }

    //转交
    public void forTransfer(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonTransfertoActivity.class, bundle);
    }

    // 抄送
    public void forCopyto(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonCopytoCoActivity.class, bundle);
    }

    /**
     * back
     *
     * @param view
     */
    public void forBack(View view) {
        this.finish();
    }

    private boolean isExpend = false;

    public void ReasonExpended(View view) {
        if (!isExpend) {
            tv_reason.setMinLines(0);
            tv_reason.setMaxLines(Integer.MAX_VALUE);
            isExpend = true;
        } else {
            tv_reason.setLines(3);
            isExpend = false;
        }

    }

}

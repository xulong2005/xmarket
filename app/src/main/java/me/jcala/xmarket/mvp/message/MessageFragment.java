package me.jcala.xmarket.mvp.message;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import me.jcala.xmarket.R;
import me.jcala.xmarket.data.pojo.Message;
import me.jcala.xmarket.mvp.a_base.BaseFragment;
import me.jcala.xmarket.view.RecyclerCommonAdapter;

public class MessageFragment extends BaseFragment implements MessageView {

    @BindView(R.id.message_list)
    protected RecyclerView recyclerView;
    private Unbinder unbinder;
    protected MessagePresenter presenter;
//    BroadcastReceiver broadcastReceiver;
    //    public static final String ACTION_UPDATE_UI = "message.update.ui";
    private Realm realm;
    @Override
    protected int getLayoutResId() {
        return R.layout.message_fragment;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        unbinder= ButterKnife.bind(this,view);
        realm=Realm.getDefaultInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        presenter=new MessagePresenterImpl(getActivity(),this);
        // 动态注册广播
//        IntentFilter filter = new IntentFilter(ACTION_UPDATE_UI);
//        broadcastReceiver = new MessageBroadcastReceiver();
//        getActivity().registerReceiver(broadcastReceiver, filter);

        presenter.initView(realm);
    }


    @Override
    public void whenNeedUpdateMsgList(RecyclerCommonAdapter<?> adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void whenShowConfirmDialog(Message item) {
        new MaterialDialog.Builder(getActivity())
                    .title(R.string.message_dialog_title)
                    .content(R.string.message_dialog_content)
                    .positiveText(R.string.message_dialog_agree)
                    .negativeText(R.string.message_dialog_disagree)
                    .onPositive((MaterialDialog dialog,DialogAction which) ->{
                        presenter.confirmDeal(item);
                    })
                    .show();
    }

//    private class MessageBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            presenter.updateMessageList();
//        }
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        realm.close();
//        getActivity().unregisterReceiver(broadcastReceiver);
    }

}

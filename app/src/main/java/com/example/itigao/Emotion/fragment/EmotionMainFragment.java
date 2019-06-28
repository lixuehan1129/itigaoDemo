package com.example.itigao.Emotion.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.Broad.BroadRoomFragment;
import com.example.itigao.Emotion.adapter.HorizontalRecyclerviewAdapter;
import com.example.itigao.Emotion.adapter.NoHorizontalScrollerVPAdapter;
import com.example.itigao.Emotion.emotionkeyboardview.EmotionKeyboard;
import com.example.itigao.Emotion.emotionkeyboardview.NoHorizontalScrollerViewPager;
import com.example.itigao.Emotion.model.ImageModel;
import com.example.itigao.Emotion.utils.EmotionUtils;
import com.example.itigao.Emotion.utils.GlobalOnItemClickManagerUtils;
import com.example.itigao.R;
import com.example.itigao.Video.RoomFragment;
import com.example.itigao.ViewHelper.BaseFragment;
import com.ufreedom.uikit.FloatingText;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by zejian
 * Time  16/1/6 下午5:26
 * Email shinezejian@163.com
 * Description:表情主界面
 */
public class EmotionMainFragment extends BaseFragment {

    //是否绑定当前Bar的编辑框的flag
    public static final String BIND_TO_EDITTEXT="bind_to_edittext";
    //是否隐藏bar上的编辑框和发生按钮
    public static final String HIDE_BAR_EDITTEXT_AND_BTN="hide bar's editText and btn";

    //当前被选中底部tab
    private static final String CURRENT_POSITION_FLAG="CURRENT_POSITION_FLAG";
    private int CurrentPosition=0;
    //底部水平tab
    private RecyclerView recyclerview_horizontal;
    private HorizontalRecyclerviewAdapter horizontalRecyclerviewAdapter;
    //表情面板
    private EmotionKeyboard mEmotionKeyboard;

    private EditText editText;
    private TextView sendT;
    private ImageView zan,emotion,send;
    private LinearLayout linearLayout;

    private static BackDataListener mDataListener;


    //需要绑定的内容view
    private View contentView;

    //不可横向滚动的ViewPager
    private NoHorizontalScrollerViewPager viewPager;

    //是否绑定当前Bar的编辑框,默认true,即绑定。
    //false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
    private boolean isBindToBarEditText=true;

    //是否隐藏bar上的编辑框和发生按钮,默认不隐藏
    private boolean isHidenBarEditTextAndBtn=false;

    List<Fragment> fragments=new ArrayList<>();


    /**
     * 创建与Fragment对象关联的View视图时调用
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_emotion, container, false);
        isHidenBarEditTextAndBtn = args.getBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN);
        //获取判断绑定对象的参数
        isBindToBarEditText = args.getBoolean(EmotionMainFragment.BIND_TO_EDITTEXT);

        initView(rootView);

        mEmotionKeyboard = EmotionKeyboard.with(getActivity())
                .setEmotionView(rootView.findViewById(R.id.ll_emotion_layout))//绑定表情面板
                .bindToContent(contentView)//绑定内容view
                .bindToEditText(!isBindToBarEditText ? ((EditText) contentView) : ((EditText) rootView.findViewById(R.id.video_room_et)))//判断绑定那种EditView
                .bindToEmotionButton(rootView.findViewById(R.id.room_emotion))//绑定表情按钮
                .build();
        initDatas();
        //创建全局监听
        GlobalOnItemClickManagerUtils globalOnItemClickManager= GlobalOnItemClickManagerUtils.getInstance(getActivity());

        if(isBindToBarEditText){
            //绑定当前Bar的编辑框
            globalOnItemClickManager.attachToEditText(editText);

        }else{
            // false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
            globalOnItemClickManager.attachToEditText((EditText) contentView);
            mEmotionKeyboard.bindToEditText((EditText)contentView);
        }

        return rootView;


    }

    /**
     * 绑定内容view
     * @param contentView
     * @return
     */
    public void bindToContentView(View contentView){
        this.contentView = contentView;
    }

    /**
     * 初始化view控件
     */
    protected void initView(View rootView){
        viewPager= (NoHorizontalScrollerViewPager) rootView.findViewById(R.id.vp_emotionview_layout);
        recyclerview_horizontal= (RecyclerView) rootView.findViewById(R.id.recyclerview_horizontal);
        editText = (EditText) rootView.findViewById(R.id.video_room_et);
        sendT= (TextView) rootView.findViewById(R.id.room_sendT);
        linearLayout= (LinearLayout) rootView.findViewById(R.id.video_room_li);
        zan = (ImageView) rootView.findViewById(R.id.room_zan);
        emotion = (ImageView) rootView.findViewById(R.id.room_emotion);
        send = (ImageView) rootView.findViewById(R.id.room_send);

        RoomFragment.setOnDataListener(new OnDataListener());
        BroadRoomFragment.setOnDataListener(new OnDataListener());

        editText.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editText.getText().toString())){
                    send.setVisibility(View.VISIBLE);
                    sendT.setVisibility(View.INVISIBLE);
                }else {
                    send.setVisibility(View.INVISIBLE);
                    sendT.setVisibility(View.VISIBLE);
                }
            }
        });

        FloatingText floatingText = new FloatingText.FloatingTextBuilder(getActivity())
                .textColor(Color.RED) // 漂浮字体的颜色
                .textSize(25)   // 浮字体的大小
                .textContent("+1") // 浮字体的内容
                .offsetX(10) // FloatingText 相对其所贴附View的水平位移偏移量
                .offsetY(-10) // FloatingText 相对其所贴附View的垂直位移偏移量
                //.floatingAnimatorEffect(new CurvePathFloatingAnimator()) // 漂浮动画
                //.floatingPathEffect(new CurveFloatingPathEffect()) // 漂浮的路径
                .build();

        floatingText.attach2Window(); //将FloatingText贴附在Window上

        zan.setOnClickListener(v ->
                floatingText.startFloating(zan)
        );
    }

    //用于实现回调的类,实现的是处理回调的接口,并实现接口里面的函数
    class OnDataListener implements DataListener{
        //实现接口中处理数据的函数,只要右边的Fragment调用onData函数,这里就会收到传递的数据
        public void onData(long data) {
           // Tip.showTip(getActivity(), String.valueOf(data));
            if(data != 0){
                editText.setEnabled(true);
                sendT.setOnClickListener(view -> {
                    if(!TextUtils.isEmpty(editText.getText())){
                        //将输入法隐藏，mPasswordEditText 代表密码输入框
                        InputMethodManager imm =(InputMethodManager)getActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                        if(!isInterceptBackPress()){
                            mEmotionKeyboard.interceptBackPress();
                        }

                        // 发送聊天室消息
                        Conversation conv = JMessageClient.getChatRoomConversation(data);
                        if (null == conv) {
                            conv = Conversation.createChatRoomConversation(data);
                        }
                        String text = editText.getText().toString();
                        if(!text.isEmpty()){
                            final cn.jpush.im.android.api.model.Message msg = conv.createSendTextMessage(text);
                            msg.setOnSendCompleteCallback(new BasicCallback() {
                                @Override
                                public void gotResult(int responseCode, String responseMessage) {
                                    if (0 == responseCode) {
                                        System.out.println("MessageSent" + responseCode + responseMessage + msg);
                                    } else {
                                        System.out.println("MessageSent" + responseCode + responseMessage + "失败");
                                    }
                                }
                            });
                            JMessageClient.sendMessage(msg);
                            editText.setText("");
                            mDataListener.backData(text);
                        }
                    }else {
                        Toast.makeText(getActivity(),"内容不能为空",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    //创建注册回调的函数
    public static void setOnDataListener(BackDataListener dataListener){
        //将参数赋值给接口类型的成员变量
        mDataListener = dataListener;
    }

    /**
     * 数据操作,这里是测试数据，请自行更换数据
     */
    protected void initDatas(){
        replaceFragment();
        List<ImageModel> list = new ArrayList<>();
        for (int i=0 ; i<fragments.size(); i++){
            if(i==0){
                ImageModel model1=new ImageModel();
                model1.icon= getResources().getDrawable(R.drawable.ic_emotion);
                model1.flag="经典笑脸";
                model1.isSelected=true;
                list.add(model1);
            }else {
                ImageModel model = new ImageModel();
                model.icon = getResources().getDrawable(R.drawable.ic_plus);
                model.flag = "其他笑脸" + i;
                model.isSelected = false;
                list.add(model);
            }
        }

        //记录底部默认选中第一个
        CurrentPosition=0;
        SharePreferences.setInteger(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);

        //底部tab
        horizontalRecyclerviewAdapter = new HorizontalRecyclerviewAdapter(getActivity(),list);
        recyclerview_horizontal.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
        recyclerview_horizontal.setAdapter(horizontalRecyclerviewAdapter);
        recyclerview_horizontal.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        //初始化recyclerview_horizontal监听器
        horizontalRecyclerviewAdapter.setOnClickItemListener(new HorizontalRecyclerviewAdapter.OnClickItemListener() {
            @Override
            public void onItemClick(View view, int position, List<ImageModel> datas) {
                //获取先前被点击tab
                int oldPosition = SharePreferences.getInteger(getActivity(), CURRENT_POSITION_FLAG, 0);
                //修改背景颜色的标记
                datas.get(oldPosition).isSelected = false;
                //记录当前被选中tab下标
                CurrentPosition = position;
                datas.get(CurrentPosition).isSelected = true;
                SharePreferences.setInteger(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);
                //通知更新，这里我们选择性更新就行了
                horizontalRecyclerviewAdapter.notifyItemChanged(oldPosition);
                horizontalRecyclerviewAdapter.notifyItemChanged(CurrentPosition);
                //viewpager界面切换
                viewPager.setCurrentItem(position,false);
            }

            @Override
            public void onItemLongClick(View view, int position, List<ImageModel> datas) {
            }
        });

    }

    private void replaceFragment(){
        //创建fragment的工厂类
        FragmentFactory factory=FragmentFactory.getSingleFactoryInstance();
        //创建修改实例
        EmotiomComplateFragment f1= (EmotiomComplateFragment) factory.getFragment(EmotionUtils.EMOTION_CLASSIC_TYPE);
        fragments.add(f1);
        Bundle b=null;
        for (int i=0;i<1;i++){
            b = new Bundle();
            b.putString("Interge","Fragment-"+i);
            Fragment1 fg= Fragment1.newInstance(Fragment1.class,b);
            fragments.add(fg);
        }

        NoHorizontalScrollerVPAdapter adapter =new NoHorizontalScrollerVPAdapter(getActivity().getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
    }


    /**
     * 是否拦截返回键操作，如果此时表情布局未隐藏，先隐藏表情布局
     * @return true则隐藏表情布局，拦截返回键操作
     *         false 则不拦截返回键操作
     */
    public boolean isInterceptBackPress(){
        return mEmotionKeyboard.interceptBackPress();
    }

}



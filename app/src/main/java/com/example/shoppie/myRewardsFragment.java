package com.example.shoppie;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link myRewardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myRewardsFragment extends Fragment {
    private RecyclerView rewardsRecyclerView;
    private Dialog lodingDialog;
    public static myrewards_adapter myrewardsAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public myRewardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment myRewardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static myRewardsFragment newInstance(String param1, String param2) {
        myRewardsFragment fragment = new myRewardsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        lodingDialog=new Dialog(getContext());
        lodingDialog.setContentView(R.layout.loading_progress_dialog);
        lodingDialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.slider_background));
        lodingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lodingDialog.setCancelable(false);
        lodingDialog.show();
        View view= inflater.inflate(R.layout.fragment_my_rewards, container, false);
        rewardsRecyclerView=view.findViewById(R.id.myrewardsrecyclerview);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rewardsRecyclerView.setLayoutManager(layoutManager);
        if(DBqueries.rewardmodelList.size()==0)
        {
            DBqueries.loadRewards(getContext(),lodingDialog,true);
        }
        else
        {
            lodingDialog.dismiss();
        }

        myrewardsAdapter=new myrewards_adapter(DBqueries.rewardmodelList,false);
        rewardsRecyclerView.setAdapter(myrewardsAdapter);
        myrewardsAdapter.notifyDataSetChanged();

    }
}
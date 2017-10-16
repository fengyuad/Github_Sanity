package com.example.tomdong.sanity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.tomdong.sanity.dummy.DummyContent;
import com.example.tomdong.sanity.dummy.DummyContent.DummyItem;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BudgetFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ListView  mListView;
    private ImageView mImageView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BudgetFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BudgetFragment newInstance(int columnCount) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget_list, container, false);
        mListView=view.findViewById(R.id.my_budgets_listview);
        mImageView=view.findViewById(R.id.my_budgets_icon);


        ArrayList<Budget_card> list = new ArrayList<>();

        list.add(new Budget_card("Parking"));
        list.add(new Budget_card("Eating"));
        list.add(new Budget_card("Studying"));
        list.add(new Budget_card("Working"));
        list.add(new Budget_card("Skiing"));
        list.add(new Budget_card("Gaming"));
        list.add(new Budget_card("Travelling"));
        list.add(new Budget_card("pooping"));
        final CustomBudgetCardAdapter adapter = new CustomBudgetCardAdapter(getContext(),R.layout.fragment_budget, list);
        mListView.setAdapter(adapter);
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//            @Override
//            public void create(SwipeMenu menu) {
//
//                // create "delete" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(
//                        getContext());
//                // set item background
////                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
////                        0x3F, 0x25)));
//                // set item width
//                deleteItem.setWidth(170);
//                // set a icon
//                deleteItem.setIcon(R.drawable.close_red);
//
//                // add to menu
//                menu.addMenuItem(deleteItem);
//
//            }
//        };

// set creator
//        mListView.setMenuCreator(creator);
//
//        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
//                        adapter.remove(adapter.getItem(position));
//                        break;
//                }
//                // false : close the menu; true : not close the menu
//                return false;
//            }
//        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                startActivity(new Intent(getContext(),BudgetViewActivity.class));

            }
        });
//        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//
//            @Override
//            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//                 final int checkedCount=mListView.getCheckedItemCount();
//                mode.setTitle(checkedCount+"Selected");
//                adapter.toggleSelection(position);
//            }
//
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                mode.getMenuInflater().inflate(R.menu.menu_delete,menu);
//                return true;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                switch (item.getItemId())
//                {
//                    case R.id.delete:
//                        SparseBooleanArray selected = adapter.getSeectedIds();
//                        for(int i=(selected.size()-1);i>=0;i--)
//                        {
//                            if(selected.valueAt(i))
//                            {
//                                Budget_card selecteditem=adapter.getItem(selected.keyAt(i));
//                                adapter.remove(selecteditem);
//                            }
//                        }
//                        mode.finish();
//                        default:
//                            return false;
//                }
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//                adapter.removeSelection();
//            }
//
//
//        });
        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            recyclerView.setAdapter(new MyBudgetRecyclerViewAdapter(DummyContent.ITEMS, mListener));
//
//        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}

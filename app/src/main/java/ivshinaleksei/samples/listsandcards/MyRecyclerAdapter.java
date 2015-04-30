package ivshinaleksei.samples.listsandcards;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created on 29/04/2015.
 *
 * @author Aleksei_Ivshin
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    public static final String TAG = MyRecyclerAdapter.class.getSimpleName();

    private static final int NORMAL_CARD = 0;
    private static final int WIDE_CARD = 1;

    public static final Random RANDOM = new Random();

    private View mRootView;

    private final List<String> mItems;

    public MyRecyclerAdapter() {
        mItems = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            mItems.add("Item " + i);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case NORMAL_CARD:
                mRootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
                break;
            case WIDE_CARD:
                mRootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wide_card_view, parent, false);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return new MyViewHolder(mRootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        viewHolder.setContent(mItems.get(i));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (RANDOM.nextInt(2) == 0) {
            Log.d(TAG, "Normal card");
            return NORMAL_CARD;
        } else {
            Log.d(TAG, "Wide card");
            return WIDE_CARD;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mCardTextView;

        public MyViewHolder(View rootView) {
            super(rootView);
            mCardTextView = (TextView) rootView.findViewById(R.id.card_text_view);
        }

        public void setContent(String content) {
            mCardTextView.setText(content);
        }
    }
}

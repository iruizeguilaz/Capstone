package ivan.capstone.com.capstone.Adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ivan on 19/02/2016.
 */
public class SeriesAdapter  {
    private Cursor mCursor;

    public SeriesAdapter(Cursor cursor) {
        mCursor = cursor;
        }

}

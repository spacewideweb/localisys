package atpl.cc.localisys.adapter;



        import android.content.Context;
        import android.os.Parcelable;
        import android.support.v4.view.PagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;

        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;

        import atpl.cc.localisys.R;

/**
 * Created by android on 30/5/17.
 */

public class SlidingImage_Adapter extends PagerAdapter {

    ArrayList<String> IMAGES;


    /*private int[] IMAGES = {R.drawable.pic,R.drawable.pic_2,R.drawable.pic_3};*/
    private LayoutInflater inflater;
    Context context;


    public SlidingImage_Adapter(Context context, ArrayList<String> images) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.IMAGES = images;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.imageforview);

        Picasso.with(context).load(IMAGES.get(position)).into(imageView);
        Log.d("imgggg", IMAGES.get(position));

      //  imageView.setImageResource(IMAGES[position]);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
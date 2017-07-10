package mivadounou.projet2.uut.ucao.mivadounou.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.models.MenuRestau;


public class MenuRestauViewHolder extends RecyclerView.ViewHolder {

    private AVLoadingIndicatorView avLoadingIndicatorView;
    private TextView titleView;
    private TextView priceView;
    private ImageView starView;
    private ImageView menuImageView;
    private ImageView menuIconTypeImageView;
    private TextView numStarsView;

    public MenuRestauViewHolder(View itemView) {
        super(itemView);

        avLoadingIndicatorView = (AVLoadingIndicatorView) itemView.findViewById(R.id.avi);
        titleView = (TextView) itemView.findViewById(R.id.title_menu_restau);
        priceView = (TextView) itemView.findViewById(R.id.price_menu_restau);
        starView = (ImageView) itemView.findViewById(R.id.star);
        menuImageView = (ImageView) itemView.findViewById(R.id.menu_image);
        menuIconTypeImageView = (ImageView) itemView.findViewById(R.id.menu_icon_type);
        numStarsView = (TextView) itemView.findViewById(R.id.menu_restau_num_stars);
    }

    public void bindToPost(MenuRestau menuRestau, View.OnClickListener starClickListener) {
        titleView.setText(menuRestau.getTitle());
        priceView.setText(menuRestau.getRestauName() + " | " + "Prix : " + menuRestau.getPrice());
        numStarsView.setText(String.valueOf(menuRestau.getStarCount()));

        starView.setOnClickListener(starClickListener);
    }

    public void startAnim() {
        avLoadingIndicatorView.smoothToShow();
        // or avi.smoothToShow();
    }

    public void stopAnim() {
        avLoadingIndicatorView.smoothToHide();
        // or avi.smoothToHide();
    }

    public TextView getTitleView() {
        return titleView;
    }

    public void setTitleView(TextView titleView) {
        this.titleView = titleView;
    }

    public TextView getPriceView() {
        return priceView;
    }

    public void setPriceView(TextView priceView) {
        this.priceView = priceView;
    }

    public ImageView getStarView() {
        return starView;
    }

    public void setStarView(ImageView starView) {
        this.starView = starView;
    }

    public ImageView getMenuImageView() {
        return menuImageView;
    }

    public void setMenuImageView(ImageView menuImageView) {
        this.menuImageView = menuImageView;
    }

    public ImageView getMenuIconTypeImageView() {
        return menuIconTypeImageView;
    }

    public void setMenuIconTypeImageView(ImageView menuIconTypeImageView) {
        this.menuIconTypeImageView = menuIconTypeImageView;
    }

    public TextView getNumStarsView() {
        return numStarsView;
    }

    public void setNumStarsView(TextView numStarsView) {
        this.numStarsView = numStarsView;
    }
}

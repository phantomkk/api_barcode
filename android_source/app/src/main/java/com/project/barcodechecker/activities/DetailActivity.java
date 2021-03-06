package com.project.barcodechecker.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.barcodechecker.R;
import com.project.barcodechecker.api.APIServiceManager;
import com.project.barcodechecker.api.services.CommentService;
import com.project.barcodechecker.api.services.ProductService;
import com.project.barcodechecker.api.services.RatingService;
import com.project.barcodechecker.fragments.CommentFragment;
import com.project.barcodechecker.models.Comment;
import com.project.barcodechecker.models.Product;
import com.project.barcodechecker.models.Rating;
import com.project.barcodechecker.models.User;
import com.project.barcodechecker.utils.AppConst;
import com.project.barcodechecker.utils.CoreManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends BaseActivity implements CommentFragment.ButtonPostCommentClickListener {
    private TextView txtCode, txtName, txtPrice, txtCompany, txtContact, txtDescription;
    private RatingBar rbStar;
    private ImageView imgProduct;
    private Product product;
    private FrameLayout frameComments;
    private List<Comment> list;
    private CommentFragment commentFragment;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingView();
//        initToolbar();
        setFragmentComment();
        setToolbarTitle("Xem chi tiết sản phẩm");
        //get item
        Intent previousPage = getIntent();
        Bundle b = previousPage.getExtras();
        if (b != null) {
            product = (Product) (b.get(AppConst.PRODUCT_PARAM));
            setValues(product);
        }
        loadComments(product);
    }

    private void bindingView() {
        txtCode = (TextView) findViewById(R.id.txt_code_detail_atv);
        txtName = (TextView) findViewById(R.id.txt_name_detail_atv);
        txtPrice = (TextView) findViewById(R.id.txt_price_detail_atv);
        txtCompany = (TextView) findViewById(R.id.txt_company_detail_atv);
        txtDescription = (TextView) findViewById(R.id.txt_description_detail_atv);
        txtContact = (TextView) findViewById(R.id.txt_contact_detail_atv);
        imgProduct = (ImageView) findViewById(R.id.img_product_detail_atv);
        rbStar = (RatingBar) findViewById(R.id.rbar_star);
        frameComments = (FrameLayout) findViewById(R.id.frm_comment_detail_atv);
    }

    private void setFragmentComment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        commentFragment = new CommentFragment();
        transaction.replace(R.id.frm_comment_detail_atv, commentFragment);
        transaction.commit();
    }

    private void setValues(Product p) {
        if (p == null) {
            p = new Product();
            showMessage("An error occur!");
            Log.e("ERROR", "DetailActivity.setValues(*): Product null");
            logError(DetailActivity.class.getSimpleName(), "setValues", "Product null");
        }
        Picasso.with(this).load(p.getImgDefault()).into(imgProduct);
        txtCode.setText(p.getCode());
        txtName.setText(p.getName());
        txtPrice.setText(String.format(Locale.US, "%3f", p.getPrice()));
//        txtCompany.setText();
        txtDescription.setText(p.getDescription());
        txtContact.setText(p.getPhone());
        rbStar.setRating((float) p.getAverageRating());
        final User u = CoreManager.getUser(DetailActivity.this);
        if (u != null) {
            if (u.getRatingList() == null) {
            }
            if (u.getRatingList() != null) {
                if (getRating(u) != null) {

                }
            }
        }
        rbStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser){
                  Log.e("ERROR", "UP");
                if (u == null) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DetailActivity.this);
                    dialog.setMessage("Bạn cần phải đăng nhập để đánh giá sản phẩm này!")
                            .setPositiveButton("Đăng nhập", positiveListener)
                            .setNegativeButton("Hủy", null)
                            .show();
                } else {
                    Rating rate = getRating(u);
                    if (rate != null) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(DetailActivity.this);
                        dialog.setMessage("Bạn đã đánh giá sản phẩm này " + rate.getRating() + " sao.");
                        dialog.setPositiveButton("OK", null);
                        dialog.show();
                        rbStar.setRating((float) product.getAverageRating());
                    } else {
                        rate = new Rating();
                        rate.setProductID(product.getId());
                        rate.setUserID(u.getId());
                        rate.setRating(rating);
                        logError(DetailActivity.class.getSimpleName(), "setValues", "Tét rating" + rate.getRating() + " rating" +
                                "proID: " + rate.getProductID() + " userid " + rate.getUserID());
                        RatingService ratingService = APIServiceManager.getRatingService();
                        showLoading();
                        ratingService.postRating(rate).enqueue(new Callback<Rating>() {
                            @Override
                            public void onResponse(Call<Rating> call, Response<Rating> response) {
                                if (response.isSuccessful()) {
//                                    logError(DetailActivity.class.getSimpleName(), "setValues", "Post rating success" + response.body().getRating() + " rating" +
//                                            "proID: " + response.body().getProductID() + " userid " + response.body().getUserID());
                                    u.getRatingList().add(response.body());
                                    CoreManager.setUser(DetailActivity.this, u);
//                                    rbStar.setIsIndicator(true);
                                    showMessage("Đánh giá sản phẩm thành công.");
                                } else {
                                    logError(DetailActivity.class.getSimpleName(), "setValues", "Post rating onResponse but else" + response.code());
                                }
                                hideLoading();
                            }

                            @Override
                            public void onFailure(Call<Rating> call, Throwable t) {
                                logError(DetailActivity.class.getSimpleName(), "setValues", "OnFailure " + t.getMessage());
                                hideLoading();
                            }
                        });
                    }
                }
                //kiem tra user null
                //bat nguoi ta dang nhap de rating
                //user khong null
                //kiem tra user da rating chua

                //user chua rating
            }
        }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private Rating getRating(User user) {
        List<Rating> list = user.getRatingList();
        if (list == null) {
            return null;
        }
        for (Rating r : list) {
            if (r.getProductID() == product.getId()) {
                return r;
            }
        }
        return null;

    }

    private void loadComments(Product product) {
        if (product != null) {
            commentFragment.setProduct(product);
            ProductService productService = APIServiceManager.getPService();
            showLoading();
            productService.getProductComments(product.getId()).enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                    if (response.isSuccessful()) {
                        list = response.body();
                        commentFragment.setData(list);
                    } else {
                        logError(DetailActivity.class.getSimpleName(), "loadComments", "ELSE ");
                    }
                    hideLoading();
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {
                    hideLoading();
                    logError(DetailActivity.class.getSimpleName(), "loadComments", "Failure API " + t.getMessage());
                }
            });
        } else {
            logError(DetailActivity.class.getSimpleName(), "loadComments", "PRODUCT null");
        }
    }

    @Override
    public void postCmtClickListener(String content, final EditText edtCmt, TextView txtError) {
        if (content.length() == 0) {
            txtError.setVisibility(View.VISIBLE);
            return;
        } else {
            txtError.setVisibility(View.INVISIBLE);
        }
        User u = CoreManager.getUser(this);
        if (u == null) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Bạn cần phải đăng nhập để bình luận!")
                    .setPositiveButton("Đăng nhập", positiveListener)
                    .setNegativeButton("Hủy", null)
                    .show();
        } else {
            Date date = Calendar.getInstance().getTime();
            final Comment comment = new Comment();
            comment.setUserID(u.getId());
            comment.setComment(content);
            comment.setName(u.getName());
            comment.setUserAvatar(u.getAvatar());
            comment.setUserID(u.getId());
            comment.setProductID(product == null ? 1 : product.getId());
            comment.setDateCreated(new SimpleDateFormat(AppConst.DATE_AND_TIME_SQL, Locale.US).format(date));
            CommentService commentService = APIServiceManager.getCommentService();
            showLoading();
            commentService.postComment(comment).enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    if (response.isSuccessful()) {
                        commentFragment.updateData(response.body());
                    } else {
                        edtCmt.setText("EROR");
                        logError(DetailActivity.class.getSimpleName(), "postCmtClickListener", "ELSE API postComment");
                    }
                    edtCmt.setText("");
                    hideLoading();
                    Toast.makeText(DetailActivity.this, "Đăng bình luận thành công.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {
                    logError(DetailActivity.class.getSimpleName(), "postCmtClickListener", "Failue API " + t.getCause());
                    edtCmt.setText("");
                    hideLoading();
                }
            });
        }
    }

    final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            startActivity(new Intent(DetailActivity.this, LoginActivity.class));
        }
    };
}

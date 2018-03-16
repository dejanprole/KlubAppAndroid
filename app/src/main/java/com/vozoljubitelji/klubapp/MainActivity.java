package com.vozoljubitelji.klubapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import android.support.design.widget.FloatingActionButton;

import com.google.firebase.iid.FirebaseInstanceId;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Handler mHandler;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private View navHeader;
    private Adapter mAdapter;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private static final String urlNavHeaderBg = "http://klubljubiteljazeleznice-beograd.com/images/slider_drawer_background.jpg";
    private static final String urlProfileImg = "http://klubljubiteljazeleznice-beograd.com/images/logotip_black.png";
    private static final String urlInsertToken = "http://klubljubiteljazeleznice-beograd.com/android_app_klub/inserttoken.php";

    private String[] activityTitles;

    FragmentManager FM;
    FragmentTransaction FT;

    FloatingActionMenu fam;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_GALLERY = "gallery";
    private static final String TAG_PORTFOLIO = "portfolio";
    private static final String TAG_ABOUTUS = "aboutus";
    public static String CURRENT_TAG = TAG_HOME;

    private boolean shouldLoadHomeFragOnBackPress = true;

    public MainActivity() {
    }



    //For login
    SessionManagement session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyNoActionBarShadowTheme);
        super.onCreate(savedInstanceState);

        session = new SessionManagement(getApplicationContext());

        setContentView(R.layout.activity_main);

        //this is for sliding menu
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mHandler = new Handler(); //Dont know why this is commented

        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);
        //txtName = (TextView) navHeader.findViewById(R.id.name);
        //txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        //fab = (FloatingActionButton) findViewById(R.id.fab);

        //FCM TOKEN
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("TOKEN", "Token Value: " + refreshedToken);
        InsertData(refreshedToken);
        //FCM TOKEN



        ImageView fabIcon = new ImageView(this);
        //fabIcon.setImageResource(R.drawable.logo);
        fabIcon.findViewById(R.id.fab);

        final FloatingActionButton fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);


        //FloatingActionButton fab = new FloatingActionButton.Builder(this).setContentView(fabIcon).build();

        SubActionButton.Builder itemBulder = new SubActionButton.Builder(this);

        ImageView itemIconFB = new ImageView(this);
        itemIconFB.setImageResource(R.drawable.facebook);
        SubActionButton button1 = itemBulder.setContentView(itemIconFB).build();

        ImageView itemIconIN = new ImageView(this);
        itemIconIN.setImageResource(R.drawable.instagram);
        SubActionButton button2 = itemBulder.setContentView(itemIconIN).build();

        ImageView itemIconTW = new ImageView(this);
        itemIconTW.setImageResource(R.drawable.twitter);
        SubActionButton button3 = itemBulder.setContentView(itemIconTW).build();

        fam = new FloatingActionMenu.Builder(this).addSubActionView(button1,140,140).addSubActionView(button2,140,140).addSubActionView(button3,140,140).attachTo(fab).build();

        button1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://www.facebook.com/KlubLjubiteljaZelezniceBeograd/?ref=bookmarks"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                fam.close(true);

            }
        });

        button2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("http://www.instagram.com/vozicari/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                fam.close(true);

            }
        });

        button3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://twitter.com/KlubBeograd"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                fam.close(true);

            }
        });

        // load nav menu header data
        loadNavHeader();



        // initializing navigation menu
       // setUpNavigationView();


//
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;


            fam.close(true);


            loadHomeFragment();
        }

    }

    public void InsertData(final String token){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String TokenHolder = token ;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("token", TokenHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(urlInsertToken);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                    Log.i("INSERTTOKEN","Token nije uploadovan.");

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                //Toast.makeText(MainActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(token);
    }




    private void loadNavHeader() {
        // name, website
        //txtName.setText("Dejan Prole");
        //txtWebsite.setText("vozoljubitelji");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();


        // set toolbar title
        setToolbarTitle();


        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            fam.close(true);

            // show or hide the fab button
             toggleFab();
            return;
        }

        FM = getFragmentManager();
        FT = FM.beginTransaction();
        FT.replace(R.id.frame, new TabFragment()).commit();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 drawer.closeDrawers();

                if (item.getItemId()==R.id.nav_home)
                {
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,new MainFragment()).addToBackStack(null).commit();
                }

                if (item.getItemId()==R.id.nav_gallery)
                {
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,new GalleryFragment()).addToBackStack(null).commit();
                }

                if (item.getItemId()==R.id.nav_portfolio)
                {
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,new PortfolioFragment()).addToBackStack(null).commit();
                }

                if (item.getItemId()==R.id.nav_gallery_modeli)
                {
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,new GalleryModelsFragment()).addToBackStack(null).commit();
                }

                if (item.getItemId()==R.id.nav_gallery_klub)
                {
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,new GalleryClubFragment()).addToBackStack(null).commit();
                }

                if (item.getItemId()==R.id.nav_gallery_putovanja)
                {
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,new GalleryTravelFragment()).addToBackStack(null).commit();
                }

                if (item.getItemId()==R.id.nav_gallery_voznipark)
                {
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,new GalleryRollingStockFragment()).addToBackStack(null).commit();
                }

                return false;
            }
        });

        android.support.v7.widget.Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawer,toolbar,R.string.app_name,R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
//        Runnable mPendingRunnable = new Runnable() {
//            @Override
//            public void run() {
//                // update the main content by replacing fragments
//                //Fragment fragment = getHomeFragment();
//                //FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                // android.R.anim.fade_out);
//                //fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
//                //fragmentTransaction.commitAllowingStateLoss();
//                //fragmentTransaction.commit();
//
//
//
//
//
//            }
//        };

        // If mPendingRunnable is not null, then add to the message queue
//        if (mPendingRunnable != null) {
//            mHandler.post(mPendingRunnable);
//        }

        // show or hide the fab button
        toggleFab();



        //Closing drawer on item click
        drawer.closeDrawers();

        fam.close(true);

        // refresh toolbar menu
        invalidateOptionsMenu();
    }


//    private Fragment getHomeFragment() {
//        switch (navItemIndex) {
//            case 0:
//                MainFragment mainFragment = new MainFragment();
//                return mainFragment;
//            case 1:
//                GalleryFragment photosFragment = new GalleryFragment();
//                return photosFragment;
//            case 2:
//                PortfolioFragment portfolioFragment = new PortfolioFragment();
//                return portfolioFragment;
//            case 3:
//                AboutUsFragment aboutUsFragment = new AboutUsFragment();
//                return aboutUsFragment;
//
//
//            default:
//                return new MainFragment();
//        }
//    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }


//    private void setUpNavigationView() {
//        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//
//            // This method will trigger on item Click of navigation menu
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//
//                //Check to see which item was being clicked and perform appropriate action
//                switch (menuItem.getItemId()) {
//                    //Replacing the main content with ContentFragment Which is our Inbox View;
//                    case R.id.home:
//                        navItemIndex = 0;
//                        CURRENT_TAG = TAG_HOME;
//                        break;
//                    case R.id.nav_gallery:
//                        navItemIndex = 1;
//                        CURRENT_TAG = TAG_HOME;
//                        break;
//                    case R.id.nav_portfolio:
//                        navItemIndex = 2;
//                        CURRENT_TAG = TAG_PORTFOLIO;
//                        break;
//                    case R.id.nav_aboutus:
//                        navItemIndex = 3;
//                        CURRENT_TAG = TAG_ABOUTUS;
//                        break;
//
////                    case R.id.nav_about_us:
////                        // launch new intent instead of loading fragment
////                        startActivity(new Intent(MainActivity.this, AboutUsFragment.class));
////                        drawer.closeDrawers();
////                        return true;
//                    case R.id.nav_privacy_policy:
//                        // launch new intent instead of loading fragment
//                        startActivity(new Intent(MainActivity.this, PrivacyFragment.class));
//                        drawer.closeDrawers();
//                        return true;
//                    default:
//                        navItemIndex = 0;
//                }
//
//                //Checking if the item is in checked state or not, if not make it in checked state
//                if (menuItem.isChecked()) {
//                    menuItem.setChecked(false);
//                } else {
//                    menuItem.setChecked(true);
//                }
//                //menuItem.setChecked(true);
//
//                loadHomeFragment();
//
//                return true;
//            }
//        });


//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

//            @Override
//            public void onDrawerClosed(View drawerView) {
//                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
//                super.onDrawerClosed(drawerView);
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
//                super.onDrawerOpened(drawerView);
//            }
//
//
//
//
//        };

//        //Setting the actionbarToggle to drawer layout
//        drawer.addDrawerListener(actionBarDrawerToggle);
//
//        //calling sync state is necessary or else your hamburger icon wont show up
//        actionBarDrawerToggle.syncState();
//
        //}

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected

            if (session.isLoggedIn()) {
              getMenuInflater().inflate(R.menu.menu_logged_in, menu);
            } else {
                getMenuInflater().inflate(R.menu.main, menu);
            }



        // when fragment is notifications, load the menu created for notifications
//        if (navItemIndex == 3) {
//            getMenuInflater().inflate(R.menu.notifications, menu);
//        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            session.logoutUser();
            Toast.makeText(getApplicationContext(), "Izlogovali ste se", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.action_login) {
            //Toast.makeText(getApplicationContext(), "Ulogovali ste se", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.action_money) {
            Intent intent = new Intent(this, FinanceActivity.class);
            startActivity(intent);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {

        if (navItemIndex == 0)
            //fam.open(true);
        //else
            fam.close(true);
        //Toast.makeText(MainActivity.this, "Slider otvoren", Toast.LENGTH_SHORT).show();


    }

}




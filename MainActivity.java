package edu.temple.tue80717.stockinfo;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

     static String s ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        EditText et ;
        String full = "";

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            System.out.println("this");
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            //args.putString("Stock",  "Stock");
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_stock, container, false);
            et =    (EditText) rootView.findViewById(R.id.editText);


             ListView lv =  (ListView) rootView.findViewById(R.id.listView);
                    System.out.println("this");
            final WebView wv = (WebView) rootView.findViewById(R.id.webView);

         /*  String currentChars  = et.getText().toString();
            List<String> your_array_list = new ArrayList<String>();
            your_array_list.add("foo");
            your_array_list.add("bar");


            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this.getContext(),
                    android.R.layout.simple_list_item_1,
                    your_array_list );

            lv.setAdapter(arrayAdapter);       */

            Button b1  = (Button)  rootView.findViewById(R.id.button);
            Button b2  =  (Button) rootView.findViewById(R.id.button2);

            View.OnClickListener oc2 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    s = et.getText().toString();
                    System.out.println(s);

                    wv.post(new Runnable() {
                        @Override
                        public void run() {
                            wv.loadUrl("http://finance.yahoo.com/rss/headline?s=" + s.toUpperCase());
                        }
                    });


                }
            };
            b2.setOnClickListener(oc2);


            View.OnClickListener ocl =  new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    s = et.getText().toString();
                    System.out.println(s);

                      Thread t  = new Thread (new Runnable() {
            @Override
            public void run() {


                String urlStr = "http://finance.yahoo.com/webservice/v1/symbols/GOOG/quote?format=json&view=basic";
// Heres to add a check for stock names
                if(s.toUpperCase().equals("GOOG"))
                {
                    System.out.println("it was GOOG, wow!");
                    urlStr = "http://finance.yahoo.com/webservice/v1/symbols/"+ s.toUpperCase() + "/quote?format=json&view=basic";


                }
                String stockSymbol = "" ;
                String timeOfChange =  "" ;
                try {
                    URL url = new URL(urlStr);
                    java.net.URLConnection con = url.openConnection();
                    con.connect();
                    java.io.BufferedReader in =
                            new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));
                    String line;
                    String newFullData = "";

                    for (; (line = in.readLine()) != null; ) {

                        newFullData += line ;


                    }
                    System.out.println(full);
                    System.out.println(newFullData);
                    if( full.compareTo(newFullData) != 0 )
                    {
                        full = newFullData;

                        System.out.println("Data updated");

                         stockSymbol = full.substring(full.indexOf("symbol")+ 11 , full.indexOf("symbol")+ 15) ;
                        System.out.println(stockSymbol);

                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        timeOfChange =  sdf.format(cal.getTime());
                        System.out.println(timeOfChange);
                    }



                } catch (MalformedURLException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }


// Searches for the stock and parses JSON
                try {

                    URL url = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input="+s);
                    java.net.URLConnection con = url.openConnection();
                    con.connect();
                    java.io.BufferedReader in =
                            new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));
                    String line;
                    String newFullData = "";

                    for (; (line = in.readLine()) != null; ) {

                        newFullData += line ;


                    }
                    System.out.println(full);
                    System.out.println(newFullData);
                    if( full.compareTo(newFullData) != 0 )
                    {
                        System.out.print(full);
                        full = newFullData;

                        System.out.println("Stock Query  + "  +  newFullData);



            }
        }catch (Exception e )
                {

                }


            }}

                );

            t.start();
                }
            };
            b1.setOnClickListener(ocl);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}

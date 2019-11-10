package Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import Adapter.Home_adapter;
import Config.BaseURL;
import Model.Category_model;
import heights.megamart.R;
import util.Session_management;
import util.Utilities;



public class Task1 extends Fragment {

    private static String TAG = Task1.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private Session_management session_management;

    String count, point, date, task1_part_time;
    Boolean task1_part_done;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private TextView mLog, tvCount, tvPoints, tvDate;

    private Button show_add;
    String formattedDate;

    public Task1() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static Task1 newInstance(String param1, String param2) {
        Task1 fragment = new Task1();
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

    @Override
    public void onResume() {
        //86400000
        super.onResume();


        show_add.setEnabled(true);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c);

        Map<String, String> postData = new HashMap<>();
        postData.put("member_email", session_management.getUserDetails().get(BaseURL.KEY_EMAIL));
//            postData.put("anotherParam", anotherParam);
        HttpoPostTaskOne task = new HttpoPostTaskOne(postData);
        try {
            String result = task.execute("http://infocentroid.us/grocery_mlm/Api/get_task_one_details").get();
            JSONObject response = new JSONObject(result);
            JSONArray message = response.getJSONArray("message");
            JSONObject task_details = message.getJSONObject(0);
            count = task_details.getString("task1_count");


            date = task_details.getString("task1_date");
            point = task_details.getString("task1_point");

            task1_part_time = task_details.getString("task1_part_time");
            if(task1_part_time.equals("00:00:00"))
            {
                task1_part_time=new SimpleDateFormat("dd/MM/yyyy kk:mm:ss").format(Calendar.getInstance().getTime());
            }
            task1_part_done = Boolean.valueOf(task_details.getString("task1_part_done"));
            if (task1_part_done == true)
                Toast.makeText(getActivity(), "You have recieved today's 30 points", Toast.LENGTH_SHORT).show();

            Date ex = null;

            String ex_now = null;
            Date exn = null;
            try {
                ex = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss").parse(date+" "+task1_part_time);

                ex_now = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss").format(Calendar.getInstance().getTime());

                exn = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss").parse(ex_now);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long difff = exn.getTime()-ex.getTime();
            long mins = difff/60000;


            long current_timeDifference=0;
            try {
                Date task_one_part_time = new SimpleDateFormat("hh:mm:ss").parse(task1_part_time);
                long task1_part_time_ms = task_one_part_time.getTime();

                current_timeDifference = System.currentTimeMillis() - task1_part_time_ms;

                current_timeDifference = current_timeDifference/60000;//in minutes
            } catch (ParseException e) {
                e.printStackTrace();
            }


            tvPoints.setText("Coins :  " + point);
            tvCount.setText("Today's Clicks:  " + count);
            tvDate.setText("Date:  " + date);



            if (formattedDate.equals(date)) {

                //if(task1_part_done == true)
                if (task1_part_done==true) {
                    mLog.setText("You have already earned today's 30 points !");
                }
                else
                {
                    if(mins<1440000)
                    {
                        mLog.setText("Please check again later!");
                    }
                    else
                    {
                        show_add.setEnabled(true);
                        mLog.setText("Youtube video loaded !");

                    }
                }


            } else {
                date = formattedDate;
                count = "0";
                Calendar time = Calendar.getInstance();
                task1_part_time = new SimpleDateFormat("hh:mm:ss").format(time.getTime());
                task1_part_done=false;

                //sample ad unit id provided by google: ca-app-pub-3940256099942544/5224354917
                //set text to load youtube video loaded

            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_task1, container, false);
        mLog = (TextView) view.findViewById(R.id.textView3);
        mLog.append("Youtube channel loaded");

        session_management = new Session_management(getActivity());

        tvCount = (TextView) view.findViewById(R.id.count);
        tvPoints = (TextView) view.findViewById(R.id.point);
        tvDate = (TextView) view.findViewById(R.id.date);

        show_add = (Button) view.findViewById(R.id.show_add);

        show_add.setEnabled(true);
        show_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_add.setEnabled(true);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/channel/UClP0lpFwFWvjzNRJZuJqBnQ"));
                    startActivity(intent);


                if(point.equals(""))
                {
                    point="0";
                }
                count = String.valueOf(Integer.parseInt(count) + 1);
                if(task1_part_done==false)
                {
                    task1_part_done=true;
                    Calendar time = Calendar.getInstance();
                    task1_part_time = new SimpleDateFormat("kk:mm:ss").format(time.getTime());
                    mLog.append("You have earned 30 points");
                    point = String.valueOf(Integer.parseInt(point) + 30);
                }
                else
                {
                    mLog.append("You have already earned today's 30 points");
                }

                new AwardTask().execute(count, date, point, String.valueOf(task1_part_done), task1_part_time);
                Toast.makeText(getActivity(), "Points:  " + point, Toast.LENGTH_SHORT).show();


            }
        });

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c+c.getTime()/60000);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c);

        Map<String, String> postData = new HashMap<>();
        postData.put("member_email", session_management.getUserDetails().get(BaseURL.KEY_EMAIL));
//            postData.put("anotherParam", anotherParam);
        // HttpoPostTaskOne task = new HttpoPostTaskOne(postData);
        try {

            String result = new HttpoPostTaskOne(postData).execute("http://infocentroid.us/grocery_mlm/Api/get_task_one_details").get();
            JSONObject response = new JSONObject(result);
            JSONArray message = response.getJSONArray("message");
            JSONObject task_details = message.getJSONObject(0);
            count = task_details.getString("task1_count");
           date = task_details.getString("task1_date");
            point = task_details.getString("task1_point");

            task1_part_time = task_details.getString("task1_part_time");
            if(task1_part_time.equals("00:00:00"))
            {
                task1_part_time=new SimpleDateFormat("dd/MM/yyyy kk:mm:ss").format(Calendar.getInstance().getTime());
            }
            task1_part_done = Boolean.valueOf(task_details.getString("task1_part_done"));

            Date ex = null;

            String ex_now = null;
            Date exn = null;
            try {
                ex = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss").parse(date+" "+task1_part_time);

                ex_now = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss").format(Calendar.getInstance().getTime());

                exn = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss").parse(ex_now);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long difff = exn.getTime()-ex.getTime();
            long mins = difff/60000;
            System.out.println("Current time => " + c+c.getTime()/60000+"   "+mins);



            //tvPoints.setText("Points:  " + point);
            tvPoints.setText("Coins :  " + point);
            tvCount.setText("Today's Clicks :  " + count);
            tvDate.setText("Date :  " + date);
            // Toast.makeText(getActivity(), "Count:  "+count+" Points:  "+point+"  Date:  "+(formattedDate.equals(date)), Toast.LENGTH_SHORT).show();

            if (formattedDate.equals(date)) {

                if (Integer.parseInt(count) == 15) {
                    mLog.setText("You have reached today's limit!");
                }

                else if(Integer.parseInt(count)%50==0)
                {
                    //CHECK TIME DIFFERENCE
                    //if(mins<1440000)
                    if(mins<60)
                    {
                        mLog.setText("Please check again after: "+(60-mins)+" minutes");
                    }
                    else{
                        task1_part_done=false;


                    }
                }
                else {
                    task1_part_done=false;

                }
            } else {
                date = formattedDate;
                count = "0";
                Calendar time = Calendar.getInstance();
                task1_part_time = new SimpleDateFormat("hh:mm:ss").format(time.getTime());
                task1_part_done=false;



            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
/*
        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(true);
        MenuItem check = menu.findItem(R.id.action_change_password);
        check.setVisible(false);*/
    }




    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class AwardTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlParameters = "";
            try {
                urlParameters = "member_email=" + URLEncoder.encode(session_management.getUserDetails().get(BaseURL.KEY_EMAIL), "UTF-8") +
                        "&task1_count=" + URLEncoder.encode(strings[0], "UTF-8") +
                        "&task1_point=" + URLEncoder.encode(strings[2], "UTF-8") +
                        "&task1_date=" + URLEncoder.encode(strings[1], "UTF-8")+
                        "&task1_part_time="+URLEncoder.encode(strings[4], "UTF-8")+
                        "&task1_part_done="+URLEncoder.encode(strings[3], "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String result = Utilities.postParamsAndfindJSON("http://infocentroid.us/grocery_mlm/Api/task_one_details", urlParameters);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                super.onPostExecute(s);
                // Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        }
    }



    class HttpoPostTaskOne extends AsyncTask<String, Void, String> {

        JSONObject postData;

        public HttpoPostTaskOne(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }


        // This is a function that we are overriding from AsyncTask. It takes Strings as parameters because that is what we defined for the parameters of our async task
        @Override
        protected String doInBackground(String... params) {

            // Create data variable for sent values to server

            String response = null;

            String data = null;
            try {
                data = URLEncoder.encode("member_email", "UTF-8")
                        + "=" + URLEncoder.encode(postData.getString("member_email"), "UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                // This is getting the url from the string we passed in
                URL url = new URL(params[0]);

                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                // urlConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

                urlConnection.setRequestMethod("POST");


                // Send the post body
                if (data != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write(data);
                    writer.flush();
                }

                int statusCode = urlConnection.getResponseCode();


                if (statusCode == 200) {

                    //String response;


                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        result.append(line);
                    }
                    r.close();
                    response = result.toString();


                    // From here you can convert the string to JSON with whatever JSON parser you like to use
                    // After converting the string to JSON, I call my custom callback. You can follow this process too, or you can implement the onPostExecute(Result) method
                } else {
                    // Status code is not 200
                    // Do something to handle the error

                }

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return response;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {

            // Toast.makeText(getActivity(), "Result:"+ result.toString(), Toast.LENGTH_SHORT).show();

            Log.e("result: ", result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String response = jsonObject.getString("response");
                    JSONArray message = jsonObject.getJSONArray("message");
                    JSONObject task_details = message.getJSONObject(0);

                    String task_count = task_details.getString("task1_count");
                    String task_point = task_details.getString("task1_point");
                    String task_date = task_details.getString("task1_date");

                    final String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


                    Date ta_da = new SimpleDateFormat("dd/MM/yyyy").parse(task_date);
                    int year = ta_da.getYear();
                    int month = ta_da.getMonth();
                    int day = ta_da.getDay();

                    Calendar validDate = Calendar.getInstance();
                    validDate.set(year, month, day);

                    Calendar currentDate = Calendar.getInstance();

                    if (!currentDate.after(validDate)) {
                        show_add.setEnabled(true);

                    } else {
                        task_count = "0";
                        task_date = String.valueOf(validDate);


                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }


}

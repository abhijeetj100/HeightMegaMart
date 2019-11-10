package Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appnext.ads.fullscreen.RewardedVideo;
import com.appnext.base.Appnext;
import com.appnext.core.callbacks.OnAdClicked;
import com.appnext.core.callbacks.OnAdClosed;
import com.appnext.core.callbacks.OnAdError;
import com.appnext.core.callbacks.OnAdLoaded;
import com.appnext.core.callbacks.OnAdOpened;
import com.appnext.core.callbacks.OnVideoEnded;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import Config.BaseURL;
import heights.megamart.R;
import util.Session_management;
import util.Utilities;


public class Task2 extends Fragment {


    private static String TAG = Task2.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button next_app;
    private TextView mLog, tvCount, tvPoints, tvDate;
    private Session_management session_management;
    String formattedDate;
    String count, point, date;

    //Rewarded
    RewardedVideo rewarded_ad;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Task2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Task2.
     */
    // TODO: Rename and change types and number of parameters
    public static Task2 newInstance(String param1, String param2) {
        Task2 fragment = new Task2();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task2, container, false);

        Appnext.init(getActivity());

        //Rewarded
        rewarded_ad = new RewardedVideo(getActivity(), "d359317b-35c8-4bf6-a032-b913aff154ce");
        //Rewarded
        rewarded_ad.loadAd();

        // Get callback for ad loaded
        rewarded_ad.setOnAdLoadedCallback(new OnAdLoaded() {
            @Override
            public void adLoaded(String bannerID) {

            }
        });

// Get callback for ad opened
        rewarded_ad.setOnAdOpenedCallback(new OnAdOpened() {
            @Override
            public void adOpened() {

            }
        });
// Get callback for ad clicked
        rewarded_ad.setOnAdClickedCallback(new OnAdClicked() {
            @Override
            public void adClicked() {

            }
        });

// Get callback for ad closed
        rewarded_ad.setOnAdClosedCallback(new OnAdClosed() {
            @Override
            public void onAdClosed() {

            }
        });

// Get callback for ad error
        rewarded_ad.setOnAdErrorCallback(new OnAdError() {
            @Override
            public void adError(String error) {

            }
        });

// Get callback when the user saw the video until the end (video ended)
        rewarded_ad.setOnVideoEndedCallback(new OnVideoEnded() {
            @Override
            public void videoEnded() {

            }
        });

        session_management = new Session_management(getActivity());


        next_app = (Button) view.findViewById(R.id.next_app);
        mLog = (TextView) view.findViewById(R.id.textView3);
        tvCount = (TextView) view.findViewById(R.id.count);
        tvPoints = (TextView) view.findViewById(R.id.point);
        tvDate = (TextView) view.findViewById(R.id.date);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c + c.getTime() / 60000);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c);

        Map<String, String> postData = new HashMap<>();
        postData.put("user_email", session_management.getUserDetails().get(BaseURL.KEY_EMAIL));

        try {


            String result = new Task2.HttpoPostTaskOne(postData).execute("http://infocentroid.us/grocery_mlm/Api/task2Details").get();
            JSONObject response = null;
            response = new JSONObject(result);
            JSONObject data = response.getJSONObject("data");
            count = data.getString("task2_count");
            date = data.getString("task2_date");
            point = data.getString("task2_point");


            //tvPoints.setText("Points:  " + point);
            tvPoints.setText("Coins :  " + point);
            tvCount.setText("Today's Clicks :  " + count);
            tvDate.setText("Date :  " + date);

//            date = formattedDate;
//            count = String.valueOf(Integer.parseInt(count+1));
//            Calendar time = Calendar.getInstance();
//            task1_part_time = new SimpleDateFormat("hh:mm:ss").format(time.getTime());
//            task1_part_done=false;


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        next_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fullscreen
                if (rewarded_ad.isAdLoaded()) {
                    rewarded_ad.showAd();
                } else {
                    //continue...
                    Toast.makeText(getActivity(), "SORRY NO AD PRESENT !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                data = URLEncoder.encode("user_email", "UTF-8")
                        + "=" + URLEncoder.encode(postData.getString("user_email"), "UTF-8");

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
                    JSONObject data = jsonObject.getJSONObject("data");


                    String task_count = data.getString("task2_count");
                    String task_point = data.getString("task2_point");
                    String task_date = data.getString("task2_date");

                    final String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


                    Date ta_da = new SimpleDateFormat("dd/MM/yyyy").parse(task_date);
                    int year = ta_da.getYear();
                    int month = ta_da.getMonth();
                    int day = ta_da.getDay();

                    Calendar validDate = Calendar.getInstance();
                    validDate.set(year, month, day);

                    Calendar currentDate = Calendar.getInstance();

                    if (!currentDate.after(validDate)) {
                        next_app.setEnabled(true);

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

    class AwardTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlParameters = "";
            try {
                urlParameters = "user_email=" + URLEncoder.encode(session_management.getUserDetails().get(BaseURL.KEY_EMAIL), "UTF-8") +
                        "&task2_count=" + URLEncoder.encode(strings[0], "UTF-8") +
                        "&task2_point=" + URLEncoder.encode(strings[2], "UTF-8") +
                        "&task2_date=" + URLEncoder.encode(strings[1], "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String result = Utilities.postParamsAndfindJSON("http://infocentroid.us/grocery_mlm/Api/add_task2", urlParameters);

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


}

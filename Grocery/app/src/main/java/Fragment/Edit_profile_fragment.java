package Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import Config.BaseURL;
import heights.megamart.MainActivity;
import heights.megamart.R;
import util.Session_management;







public class Edit_profile_fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText et_pro_name,et_pro_phone,et_pro_email,et_pro_date,et_pro_sponser,et_kyc,et_bank_name,et_account_no,et_ifsc_code;
    Spinner spinner;
    LinearLayout bank_details;
    Button btn_pro_edit;
    String reg_type,user_id;
    HashMap<String, String> VhashMap;
    ArrayAdapter aa;

    String[] reg = {"Non-Earning", "Earning"};

    Session_management sessionManagement;

    String getName,getPhone,getemail,getdate,getsponser,getkyc,getbankname,getaccount,getifsc;
    String getName1,getPhone1,getemail1,getdate1,getsponser1,getkyc1,getbankname1,getaccount1,getifsc1;

    public Edit_profile_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        sessionManagement=new Session_management(getActivity());
        user_id = sessionManagement.getUserId();
        reg_type = sessionManagement.getUserType();
        getName1 = sessionManagement.getName();
        getPhone1 = sessionManagement.getPhone();
        getemail1 = sessionManagement.getEmail();
        getdate1 = sessionManagement.getDate();
        getsponser1 = sessionManagement.getSponser();
        getkyc1 = sessionManagement.getKyc();
        getbankname1 = sessionManagement.getBankname();
        getaccount1 = sessionManagement.getAccount();
        getifsc1 = sessionManagement.getIfsc();

        et_pro_name = (EditText)view.findViewById(R.id.et_pro_name);
        et_pro_phone= (EditText)view.findViewById(R.id.et_pro_phone);
        et_pro_email=(EditText)view.findViewById(R.id.et_pro_email);
        et_pro_date = (EditText)view.findViewById(R.id.et_pro_date);
        et_pro_sponser =(EditText)view.findViewById(R.id.et_pro_sponser);
        et_kyc =(EditText)view.findViewById(R.id.et_kyc);
        et_bank_name = (EditText)view.findViewById(R.id.et_bank_name);
        et_account_no = (EditText)view.findViewById(R.id.et_account_no);
        et_ifsc_code = (EditText)view.findViewById(R.id.et_ifsc_code);

        //et_city.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        et_ifsc_code.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        spinner = (Spinner)view.findViewById(R.id.spinner);
        bank_details = (LinearLayout)view.findViewById(R.id.bank_details);
        btn_pro_edit = (Button)view.findViewById(R.id.btn_pro_edit);

        et_pro_name.setText(getName1);
        et_pro_phone.setText(getPhone1);
        et_pro_email.setText(getemail1);
        et_pro_date.setText(getdate1);
        et_pro_sponser.setText(getsponser1);
        et_kyc.setText(getkyc1);
        et_bank_name.setText(getbankname1);
        et_account_no.setText(getaccount1);
        et_ifsc_code.setText(getifsc1);
        //  bank_details.setText();

        spinner.setOnItemSelectedListener(this);

        aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, reg);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        btn_pro_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getName = et_pro_name.getText().toString();
                getPhone = et_pro_phone.getText().toString();
                getaccount = et_account_no.getText().toString();
                getifsc = et_ifsc_code.getText().toString();
                getkyc = et_kyc.getText().toString();
                getdate = et_pro_date.getText().toString();
                getemail = et_pro_email.getText().toString();
                getsponser = et_pro_sponser.getText().toString();
                getbankname = et_bank_name.getText().toString();
//                sessionManagement.updateData(user_id,getName,getPhone,getdate,getsponser,reg_type,getbankname);

                new PostUpdate().execute();

            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem cart = menu.findItem(R.id.action_cart);
        cart.setVisible(false);
        MenuItem change_pass = menu.findItem(R.id.action_change_password);
        change_pass.setVisible(true);
        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_change_password:
                Fragment fm = new Change_password_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
                return false;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // spinner.setSelection(i);

        if (i == 0) {
            bank_details.setVisibility(View.INVISIBLE);
            reg_type=""+0;
        } else {
            reg_type=""+1;
            bank_details.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class PostUpdate extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.show();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(BaseURL.EDIT_PROFILE);
                JSONObject postDataParams = new JSONObject();

                postDataParams.put("member_mobile", getPhone);
                postDataParams.put("member_name", getName);
                postDataParams.put("member_email", getemail);
                postDataParams.put("member_Adhar", getName);
                postDataParams.put("member_bankifsc", getifsc);
                postDataParams.put("member_bankacno", getaccount);
                postDataParams.put("member_password", getName);
                postDataParams.put("member_sponser", getsponser);
                postDataParams.put("member_pan", getName);
                postDataParams.put("member_bankname", getbankname);
                postDataParams.put("member_id",user_id);
                postDataParams.put("reg_type",reg_type);


                /*String ss=VhashMap.get(reg_type);
                int spinnerPosition = aa.getPosition(ss);

                Log.e("pos",String.valueOf(spinnerPosition));

                spinner.setSelection(spinnerPosition);*/



                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        StringBuffer Ss = sb.append(line);
                        Log.e("Ss", Ss.toString());
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            /*Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();*/

            Log.e("PostUpdate_result", result.toString());
            if (result != null) {
                dialog.dismiss();

                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(result);
                    String responce = jsonObject.getString("responce");

                   /* String StatusCode_ = Sms.getString("StatusCode");
                    String Message_ = Sms.getString("Message");
                    Log.e(">>>>", Sms.toString() + " " + StatusCode_ + " " + Message_);*/

                    if (responce.equalsIgnoreCase("true")) {

                        sessionManagement.updateData(user_id,getName,getPhone,getdate,getsponser,reg_type,getbankname,getifsc,getaccount);

                        ((MainActivity) getActivity()).updateHeader();

                        Toast.makeText(getActivity(), getResources().getString(R.string.profile_updated), Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }
}












//*********************************************************




/*
public class Edit_profile_fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText et_pro_name,et_pro_phone,et_pro_email,et_pro_date,et_pro_sponser,et_kyc,et_bank_name,et_account_no,et_ifsc_code;
    Spinner spinner;
    LinearLayout bank_details;
    Button btn_pro_edit;
    String reg_type,UserId;

    String[] reg = {"Non-Earning", "Earning"};

    Session_management sessionManagement;

    String getName,getPhone,getemail,getdate,getsponser,getkyc,getbankname,getaccount,getifsc;

    public Edit_profile_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        UserId = AppPreference.getUserId(getActivity());

        et_pro_name = (EditText)view.findViewById(R.id.et_pro_name);
        et_pro_phone= (EditText)view.findViewById(R.id.et_pro_phone);
        et_pro_email=(EditText)view.findViewById(R.id.et_pro_email);
        et_pro_date = (EditText)view.findViewById(R.id.et_pro_date);
        et_pro_sponser =(EditText)view.findViewById(R.id.et_pro_sponser);
        et_kyc =(EditText)view.findViewById(R.id.et_kyc);
        et_bank_name = (EditText)view.findViewById(R.id.et_bank_name);
        et_account_no = (EditText)view.findViewById(R.id.et_account_no);
        et_ifsc_code = (EditText)view.findViewById(R.id.et_ifsc_code);
        spinner = (Spinner)view.findViewById(R.id.spinner);
        bank_details = (LinearLayout)view.findViewById(R.id.bank_details);
        btn_pro_edit = (Button)view.findViewById(R.id.btn_pro_edit);



        getName = et_pro_name.getText().toString();
        getPhone = et_pro_phone.getText().toString();
        getaccount = et_account_no.getText().toString();
        getifsc = et_ifsc_code.getText().toString();
        getkyc = et_kyc.getText().toString();
        getdate = et_pro_date.getText().toString();
        getemail = et_pro_email.getText().toString();
        getsponser = et_pro_sponser.getText().toString();
        getbankname = et_bank_name.getText().toString();


        et_pro_name.setText(getName);

        spinner.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, reg);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        btn_pro_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                new PostUpdate().execute();


            }
        });





        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();


    }










    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem cart = menu.findItem(R.id.action_cart);
        cart.setVisible(false);
        MenuItem change_pass = menu.findItem(R.id.action_change_password);
        change_pass.setVisible(true);
        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_change_password:
                Fragment fm = new Change_password_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
                return false;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            bank_details.setVisibility(View.INVISIBLE);
            reg_type=""+0;
        } else {
            reg_type=""+1;
            bank_details.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class PostUpdate extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.show();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(BaseURL.EDIT_PROFILE);
                JSONObject postDataParams = new JSONObject();

                postDataParams.put("member_mobile", getPhone);
                postDataParams.put("member_name", getName);
                postDataParams.put("member_email", getName);
                postDataParams.put("member_Adhar", getName);
                postDataParams.put("member_bankifsc", getName);
                postDataParams.put("member_bankacno", getName);
                postDataParams.put("member_password", getName);
                postDataParams.put("member_sponser", getName);
                postDataParams.put("member_pan", getName);
                postDataParams.put("member_bankname", getName);
                postDataParams.put("member_id",UserId);
                postDataParams.put("reg_type","1");





                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 */
/* milliseconds *//*
);
                conn.setConnectTimeout(15000 */
/* milliseconds *//*
);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        StringBuffer Ss = sb.append(line);
                        Log.e("Ss", Ss.toString());
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            */
/*Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();*//*


            Log.e("PostUpdate_result", result.toString());
            if (result != null) {
                dialog.dismiss();

                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(result);
                    String responce = jsonObject.getString("responce");

                   */
/* String StatusCode_ = Sms.getString("StatusCode");
                    String Message_ = Sms.getString("Message");
                    Log.e(">>>>", Sms.toString() + " " + StatusCode_ + " " + Message_);*//*


                    if (responce.equalsIgnoreCase("true")) {

                       // sessionManagement.updateData(UserId,getName,getPhone,getdate,getsponser);

                        ((MainActivity) getActivity()).updateHeader();

                        Toast.makeText(getActivity(), getResources().getString(R.string.profile_updated), Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }




}
*/




//***************************************************************************************************************


/*
public class Edit_profile_fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static String TAG = Edit_profile_fragment.class.getSimpleName();

    private EditText et_phone, et_name, et_email,et_date, et_sponser,et_house,et_kyc_id,et_ifsccode,et_account;
    private Button btn_update;
    LinearLayout bank_details;
    private TextView tv_phone, tv_name, tv_email, tv_house, tv_socity,btn_socity;
    private ImageView iv_profile;
    String reg_type;
    String type;
    private EditText et_sponser_id, et_kyc, et_bank_name, et_account_no, et_ifsc_code;

    //private Spinner sp_socity;

    private Spinner spin;
    String[] reg = {"Non-Earning", "Earning"};

    private String getsocity = "";
    private String filePath = "";
    private static final int GALLERY_REQUEST_CODE1 = 201;
    private Bitmap bitmap;
    private Uri imageuri;

    private Session_management sessionManagement;
    private Spinner spiType;

    public Edit_profile_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setHasOptionsMenu(true);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.edit_profile));

        sessionManagement = new Session_management(getActivity());

        et_name = (EditText) view.findViewById(R.id.et_pro_name);
        et_phone = (EditText) view.findViewById(R.id.et_pro_phone);
        et_email = (EditText) view.findViewById(R.id.et_pro_email);
       // et_pro = (EditText) view.findViewById(R.id.et_pro_date);
        et_name = (EditText) view.findViewById(R.id.et_pro_sponser);
        et_name = (EditText) view.findViewById(R.id.et_account);
        et_name = (EditText) view.findViewById(R.id.et_ifsccode);
        et_name = (EditText) view.findViewById(R.id.et_kyc_id);
        spiType = (Spinner) view.findViewById(R.id.spinner);
        iv_profile = (ImageView) view.findViewById(R.id.iv_pro_img);
        bank_details = (LinearLayout)view.findViewById(R.id.bank_details);
        et_sponser_id = (EditText)view.findViewById(R.id.et_sponser_id);

        */
/*et_house = (EditText) view.findViewById(R.id.et_pro_home);
        tv_house = (TextView) view.findViewById(R.id.tv_pro_home);
        tv_socity = (TextView) view.findViewById(R.id.tv_pro_socity);*//*

        btn_update = (Button) view.findViewById(R.id.btn_pro_edit);
        //btn_socity = (TextView) view.findViewById(R.id.btn_pro_socity);

      */
/*  spin = (Spinner)view.findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, reg);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
*//*

        String getemail = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
        String getimage = sessionManagement.getUserDetails().get(BaseURL.KEY_IMAGE);
        String getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        String getphone = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        String getDate = sessionManagement.getUserDetails().get(BaseURL.KEY_DATE);
        String getSponser = sessionManagement.getUserDetails().get(BaseURL.KEY_SPONSERID);
        String getKyc = sessionManagement.getUserDetails().get(BaseURL.KEY_KYC);
        String getAccount = sessionManagement.getUserDetails().get(BaseURL.KEY_ACCOUNT);
        String getIfsc = sessionManagement.getUserDetails().get(BaseURL.KEY_IFSC);
        String getpin = sessionManagement.getUserDetails().get(BaseURL.KEY_PINCODE);
        String gethouse = sessionManagement.getUserDetails().get(BaseURL.KEY_HOUSE);
        getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);
        String getsocity_name = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_NAME);

        et_name.setText(getname);
        et_phone.setText(getphone);
        et_date.setText(getDate);
        et_sponser.setText(getSponser);
        et_kyc_id.setText(getKyc);
        et_account.setText(getAccount);
        et_ifsccode.setText(getIfsc);

        */
/*if (!TextUtils.isEmpty(getsocity_name)) {
            btn_socity.setText(getsocity_name);
        }*//*


        if (!TextUtils.isEmpty(getimage)) {

            Glide.with(this)
                    .load(BaseURL.IMG_PROFILE_URL + getimage)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(iv_profile);
        }

        if (!TextUtils.isEmpty(getemail)){
            et_email.setText(getemail);
        }

        */
/*if (!TextUtils.isEmpty(gethouse)){
            et_house.setText(gethouse);
        }*//*


        btn_update.setOnClickListener(this);
        //btn_socity.setOnClickListener(this);
        iv_profile.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_pro_edit) {
            attemptEditProfile();
        } */
/*else if (id == R.id.btn_pro_socity) {

            String getpincode = et_pin.getText().toString();

            if (!TextUtils.isEmpty(getpincode)) {

                Bundle args = new Bundle();
                Fragment fm = new Socity_fragment();
                args.putString("pincode", getpincode);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }else{
                Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
            }

        } *//*
else if (id == R.id.iv_pro_img) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE1);
        }
    }

    private void attemptEditProfile() {

        tv_phone.setText(getResources().getString(R.string.et_login_phone_hint));
        tv_email.setText(getResources().getString(R.string.tv_login_email));
        tv_name.setText(getResources().getString(R.string.tv_reg_name_hint));

        */
/*tv_house.setText(getResources().getString(R.string.tv_reg_house));
        tv_socity.setText(getResources().getString(R.string.tv_reg_socity));*//*


        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_email.setTextColor(getResources().getColor(R.color.dark_gray));
        */
/*tv_house.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_socity.setTextColor(getResources().getColor(R.color.dark_gray));*//*


        String getphone = et_phone.getText().toString();
        String getname = et_name.getText().toString();
        String getemail = et_email.getText().toString();


        */
/*String gethouse = et_house.getText().toString();
        String getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);*//*


        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        } else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(R.string.phone_too_short));
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getemail)) {
            tv_email.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_email;
            cancel = true;
        }

        */
/*if (TextUtils.isEmpty(getsocity) && getsocity == null) {
            tv_socity.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = btn_socity;
            cancel = true;
        }*//*


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {

                String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                  //  new editProfile(user_id, getname, getphone,type).execute();
                }
            }
        }
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if ((requestCode == GALLERY_REQUEST_CODE1)) {
            if (resultCode == getActivity().RESULT_OK) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    //filePath = imgDecodableString;

                    Bitmap b = BitmapFactory.decodeFile(imgDecodableString);
                    Bitmap out = Bitmap.createScaledBitmap(b, 1200, 1024, false);

                    //getting image from gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);


                    File file = new File(imgDecodableString);
                    filePath = file.getAbsolutePath();
                    FileOutputStream fOut;
                    try {
                        fOut = new FileOutputStream(file);
                        out.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
                        fOut.flush();
                        fOut.close();
                        //b.recycle();
                        //out.recycle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (requestCode == GALLERY_REQUEST_CODE1) {

                        // Set the Image in ImageView after decoding the String
                        iv_profile.setImageBitmap(bitmap);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

      */
/*  type = spin.getSelectedItem().toString();
        spin.setSelected(Boolean.parseBoolean(reg_type));

        if (i == 0) {
            bank_details.setVisibility(View.INVISIBLE);
            reg_type=""+0;
        } else {
            reg_type=""+1;
            bank_details.setVisibility(View.VISIBLE);
        }*//*

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // asynctask for upload data with image or not image using HttpOk
    public class editProfile extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        JSONParser jsonParser;
        ArrayList<NameValuePair> nameValuePairs;
        boolean response;
        String error_string, success_msg;

        String getphone;
        String getname;
        String getsponser;
        //String getpin;
        String getdate;
        String gethouse;
        String getsocity;
        String getimage;

       */
/* public editProfile(String user_id, String name, String phone,String adhar,String ifsc,String account,String password,String sponser,String pan,
                           String bankname,String reg,String email) {*//*


        public editProfile(String user_id, String name, String phone,String reg) {

            nameValuePairs = new ArrayList<NameValuePair>();

        */
/*    nameValuePairs.add(new NameValuePair("member_id", user_id));
            nameValuePairs.add(new NameValuePair("member_name", name));
            nameValuePairs.add(new NameValuePair("member_mobile", phone));
            nameValuePairs.add(new NameValuePair("reg_type",reg));*//*


            nameValuePairs.add(new NameValuePair("member_mobile",reg));
            nameValuePairs.add(new NameValuePair("member_name",reg));
            nameValuePairs.add(new NameValuePair("member_email",reg));
            nameValuePairs.add(new NameValuePair("member_Adhar",reg));
            nameValuePairs.add(new NameValuePair("member_bankifsc",reg));
            nameValuePairs.add(new NameValuePair("member_bankacno",reg));
            nameValuePairs.add(new NameValuePair("member_password",reg));
            nameValuePairs.add(new NameValuePair("member_sponser",reg));
            nameValuePairs.add(new NameValuePair("member_pan",reg));
            nameValuePairs.add(new NameValuePair("member_bankname",reg));
            nameValuePairs.add(new NameValuePair("member_id",reg));
            nameValuePairs.add(new NameValuePair("reg_type",reg));


           */
/* nameValuePairs.add(new NameValuePair("member_Adhar",adhar));
            nameValuePairs.add(new NameValuePair("member_bankifsc",ifsc));
            nameValuePairs.add(new NameValuePair("member_bankacno",account));
            nameValuePairs.add(new NameValuePair("member_password",password));
            nameValuePairs.add(new NameValuePair("member_sponser",sponser));
            nameValuePairs.add(new NameValuePair("member_pan",pan));
            nameValuePairs.add(new NameValuePair("member_bankname",bankname));
            nameValuePairs.add(new NameValuePair("member_email",email));*//*


           // nameValuePairs.add(new NameValuePair("reg_type",reg_type));

           */
/* nameValuePairs.add(new NameValuePair("user_id", user_id));
            nameValuePairs.add(new NameValuePair("user_fullname", name));
            nameValuePairs.add(new NameValuePair("user_mobile", phone));
            nameValuePairs.add(new NameValuePair("pincode", pincode));
            nameValuePairs.add(new NameValuePair("socity_id", socity_id));
            nameValuePairs.add(new NameValuePair("house_no", house));*//*


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.uploading_profile_data), true);
            jsonParser = new JSONParser(getActivity());
        }

        @Override
        protected Void doInBackground(Void... params) {

            String json_responce = null;
            try {
                if (filePath == "") {
                    json_responce = jsonParser.execPostScriptJSON(BaseURL.EDIT_PROFILE, nameValuePairs);
                } else {
                    json_responce = jsonParser.execMultiPartPostScriptJSON(BaseURL.EDIT_PROFILE, nameValuePairs, "image/png", filePath, "image");
                }
                Log.e(TAG, json_responce + "," + filePath);

                JSONObject jObj = new JSONObject(json_responce);


               */
/* if (json_responce.equalsIgnoreCase("true")) {

                    Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }*//*



                if (jObj.getBoolean("responce")) {
                    response = true;
                    //success_msg = jObj.getString("data");
*/
/*
                    JSONObject obj = jObj.getJSONObject("data");

                    getphone = obj.getString("user_phone");
                    getname = obj.getString("user_fullname");
                    getdate = obj.getString("reg_date");
                    getsponser = obj.getString("sponsor_id");
                   // getpin = obj.getString("pincode");
                    gethouse = obj.getString("house_no");
                    getsocity = obj.getString("socity_id");
                    getimage = obj.getString("user_image");*//*


                }


                else {
                    response = false;
                    error_string = jObj.getString("error");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null) {
                progressDialog.hide();
                progressDialog.dismiss();
                progressDialog = null;
            }

            if (response) {

                sessionManagement.updateData(getname,getphone,getdate,getsponser,getsocity,getimage,gethouse);

                ((MainActivity) getActivity()).updateHeader();

                Toast.makeText(getActivity(), getResources().getString(R.string.profile_updated), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "" + error_string, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (progressDialog != null) {
                progressDialog.hide();
                progressDialog.dismiss();
                progressDialog = null;
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem cart = menu.findItem(R.id.action_cart);
        cart.setVisible(false);
        MenuItem change_pass = menu.findItem(R.id.action_change_password);
        change_pass.setVisible(true);
        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_change_password:
                Fragment fm = new Change_password_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
                return false;
        }
        return false;
    }

}
*/

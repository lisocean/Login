package mg.studio.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


public class Login extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /**
         * If the user just registered an account from Register.class,
         * the parcelable should be retrieved
         */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Retrieve the parcelable
            Feedback feedback = bundle.getParcelable("feedback");
            // Get the from the object
            TextView display = findViewById(R.id.display);
            display.setVisibility(View.VISIBLE);
 //           String prompt = userName.substring(0, 1).toUpperCase() + userName.substring(1) + " " + getString(R.string.account_created);
   //         display.setText(prompt);

        }

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);


        /**
         * Prepare the dialog to display when the login button is pressed
         */
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        /**
         * Use the SessionManager class to check whether
         * the user already logged in, is yest  then go to the MainActivity
         */
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }


    }

    /**
     *  Process the user input and log in if credentials are correct
     *  Disable the button while login is processing
     *  @param view from activity_login.xml
     */
    public void btnLogin(View view) {


        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Check for empty data in the form
        if (!email.isEmpty() && !password.isEmpty()) {

            // Avoid multiple clicks on the button
            loginButton.setClickable(false);

            //Todo : ensure the user has Internet connection

            // Display the progress Dialog


            //Todo: need to check weather the user has Internet before attempting checking the data
            // Start fetching the data from the Internet

            try {
                SharedPreferences sp = this.getBaseContext().getSharedPreferences("LoginConfig",Context.MODE_PRIVATE);
                String passwordBack = sp.getString(email, "");
                String name = sp.getString(password, "");
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if(password.equals(passwordBack)){
                    // Update the session
                    session.setLogin(true);
                    // Move the user to MainActivity and pass in the User name which was form the server
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.putExtra("feedback", "hello," + name);
                    startActivity(intent);
                }else {
                    // Allow the user to click the button
                    loginButton.setClickable(true);
                    Toast.makeText(getApplication(), "login Error, password error", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                System.out.println(e);
                loginButton.setClickable(true);
                Toast.makeText(getApplication(), "login Error, User isn't exist", Toast.LENGTH_SHORT).show();
            }


        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(),
                    R.string.enter_credentials, Toast.LENGTH_LONG)
                    .show();
        }
    }


    /**
     * Press the button register, go to Registration form
     *
     * @param view from the activity_login.xml
     */
    public void btnRegister(View view) {
        startActivity(new Intent(getApplicationContext(), Register.class));
        finish();
    }



    /**
     * Use the email and password provided to log the user in if the credentials are valid
     *
     */





//    /**
//     * Parsing the string response from the Server
//     * @param response
//     * @return
//     */
//    public int parsingResponse(String response) {
//
//        try {
//            JSONObject jObj = new JSONObject(response);
//            /**
//             * If the registration on the server was successful the return should be
//             * {"error":false}
//             * Else, an object for error message is added
//             * Example: {"error":true,"error_msg":"Invalid email format."}
//             * Success of the registration can be checked based on the
//             * object error, where true refers to the existence of an error
//             */
//            boolean error = jObj.getBoolean("error");
//
//            if (!error) {
//                //No error, return from the server was {"error":false}
//                JSONObject user = jObj.getJSONObject("user");
//                String email = user.getString("email");
//                feedback.setName(email);
//                return feedback.SUCCESS;
//            } else {
//                // The return contains error messages
//                String errorMsg = jObj.getString("error_msg");
//                Log.d("TAG", "errorMsg : " + errorMsg);
//                feedback.setError_message(errorMsg);
//                return feedback.FAIL;
//            }
//        } catch (JSONException e) {
//            feedback.setError_message(e.toString());
//            return feedback.FAIL;
//        }
//
//    }
}
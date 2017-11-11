package comkiolk.github.timerapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static comkiolk.github.timerapp.Constants.TAGS;

public class MainActivity extends AppCompatActivity {

    public static final String ENTER_4_DIGITS_NUMBER = "Enter 4 - digits number";
    public static final int SHOW_NUMBER_CYCLES = 500;
    EditText mTextForExecutor;
    TextView mFirstTextView;
    TextView mAsyncTaskTextView;
    TextView mExecutorTextView;
    Button mFirstThreadButton;
    Button mAsyncTaskButton;
    Button mExecutorButton;
    Handler mFirstHandler;
    Handler mSeconrHandler;
    ProgressBar mProgressBar;
    ExecutorService myExecutorService;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d(TAGS, "Start onCreate method of Thread: " + Thread.currentThread().getName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
    }

    private void initialization() {

        mFirstThreadButton = (Button) findViewById(R.id.thread_1_button);
        mAsyncTaskButton = (Button) findViewById(R.id.thread_async_task_button);
        mExecutorButton = (Button) findViewById(R.id.thread_executor_button);

        mAsyncTaskTextView = (TextView) findViewById(R.id.thread_async_task_text_view);
        mFirstTextView = (TextView) findViewById(R.id.thread_1_text_view);
        mExecutorTextView = (TextView) findViewById(R.id.thread_executor_text_view);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        myExecutorService = Executors.newSingleThreadExecutor();

        mTextForExecutor = (EditText) findViewById(R.id.input_number_for_executors_edit_text);

        initializationOfFirstHandler();
        initializationOfSecondHandler();

        final View.OnClickListener clickBtn = new View.OnClickListener() {

            @Override
            public void onClick(final View pView) {
                switch (pView.getId()) {
                    case R.id.thread_1_button:
                        cleanTextView(mFirstTextView);
                        final Thread firstThread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                final String inputNumber = "1234";
                                final int cnt = hardCalculationForWinInFirstStep(inputNumber);
                                mFirstHandler.sendEmptyMessage(cnt);
                            }
                        });
                        firstThread.setName("Thread with implementation");
                        firstThread.start();
                        break;
                    case R.id.thread_async_task_button:
                        cleanTextView(mAsyncTaskTextView);
                        new MyAsyncTask().execute(4);
                        break;
                    case R.id.thread_executor_button:
                        cleanTextView(mExecutorTextView);
                        final String inputNum = mTextForExecutor.getText().toString();
                        if (new RandomNumberGenerator().checkNumberForCorrectInput(inputNum, 4)) {

                            myExecutorService.submit(new Runnable() {

                                @Override
                                public void run() {
                                    Log.d(TAGS, "Start thread: " + Thread.currentThread().getName());
                                    final int cnt = hardCalculationForWinInFirstStep(inputNum);
                                    mSeconrHandler.sendEmptyMessage(cnt);
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, ENTER_4_DIGITS_NUMBER, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        mFirstThreadButton.setOnClickListener(clickBtn);
        mAsyncTaskButton.setOnClickListener(clickBtn);
        mExecutorButton.setOnClickListener(clickBtn);
    }

    private int hardCalculationForWinInFirstStep(final String pInputNumber) {
        String codedNumber = "";
        int cnt = 0;
        try {
            while (true) {
                codedNumber = new RandomNumberGenerator().generateRandomNumber(4);
                ++cnt;
                TimeUnit.MILLISECONDS.sleep(1);

                if (cnt % SHOW_NUMBER_CYCLES == 0) {
                    Log.d(TAGS, "Cycle happened: " + cnt + " times. In thread: " + Thread.currentThread().getName());
                }
                if (pInputNumber.equals(codedNumber)) {
                    return cnt;
                }
            }
        } catch (final InterruptedException pE) {
            pE.printStackTrace();
        }
        return cnt;
    }

    private void initializationOfSecondHandler() {
        mSeconrHandler = new Handler() {

            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);
                Log.d(TAGS, "Handler get message" + msg.what);
                mExecutorTextView.setText("" + mTextForExecutor.getText().toString() + ", You should win every" + msg.what + "'th time");
            }
        };
    }

    private void initializationOfFirstHandler() {
        mFirstHandler = new Handler() {

            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);
                Log.d(TAGS, "Handler get message" + msg.what);
                mFirstTextView.setText("1234, You should win every" + msg.what + "'th time");
            }
        };
    }

    private class MyAsyncTask extends AsyncTask<Integer, String, String> {

//

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAGS, "Start onPreExecute");
            mProgressBar.setVisibility(View.VISIBLE);
            mAsyncTaskButton.setEnabled(false);
        }

        @Override
        protected void onPostExecute(final String pS) {
            super.onPostExecute(pS);
            Log.d(TAGS, "Start onPostExecute");
            mProgressBar.setVisibility(View.INVISIBLE);
            mAsyncTaskButton.setEnabled(true);
            final String result = " 5678, You should win every" + pS + "'th time";
            mAsyncTaskTextView.setText(result);
        }

        @Override
        protected String doInBackground(final Integer... pIntegers) {
            final String inputNumber = "5678";
            final int result = hardCalculationForWinInFirstStep(inputNumber);
            return "" + result;
        }

    }

    public void cleanTextView(final TextView pTextView){
        pTextView.setText("");
    }

}

package com.example.wra1th.newsarticle;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView tvArticle;
    WebContentLoaderTask webContentLoaderTask;
    ProgressBar progressBar;
    Button retryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        // loadContentOfArticle();
        //loadContentFromWebPage();
        setClickListener();
        webContentLoaderTask = new WebContentLoaderTask();
        webContentLoaderTask.execute();
    }

    private void setClickListener() {
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webContentLoaderTask = new WebContentLoaderTask();
                webContentLoaderTask.execute();
            }
        });
    }



    private void loadContentFromWebPage() {

        String pageLink = "https://demo1738991.mockable.io/giant_atoms";

        //get content using getWebPageContent method
        String newsContent = getWebPageContent(pageLink);

        //finally load the news content in a textView

        tvArticle.setText(newsContent);
    }

    private void loadContentOfArticle() {
        tvArticle.setText("Giant Atom\n" +
                "\n" +
                "When it comes to quantum mechanics, I try to avoid writing about purely theoretical results. This is especially true of quantum computing, where, in the not-too-distant past, every researcher would put their names to papers describing a new way to make a quantum computer. Then people started playing with the real thing, and suddenly the theory-side held fewer attractions. But every now and again, one of these ideas makes me mash my hands on the keyboard.\n" +
                "Today, it's Rydberg atoms and how to make a quantum computer from them. It's a bit far-fetched, but given the detail of the calculations, it's probably something that will turn up in a couple of years. And when it turns up, it will not be like previous quantum computers, which started testing with one or two qubits. A Rydberg computer should start out at 10-12 qubits.\n" +
                "\n" +
                "Forbidden transition\n" +
                "To turn a Rydberg atom into a qubit (a qubit is a bit of quantum information), you choose two of these energetic states as logic one and logic zero. In this case, the researchers chose the 48th and 50th states because it is pretty much impossible for a Rydberg atom in the 50th state to lose some energy and decay into the 48th state. I won't go into how this works, but suffice it to say that going from the 50th to the 48th state involves changing more than just the electron's energy, so it can't be done by emitting or absorbing a single photon of light.\n" +
                "\n" +
                "A big problem with Rydberg states is that they love to give up their energy by emitting microwaves as they move down the stack using more favorable transitions. The researchers showed that this can be prevented by placing the atoms inside a capacitor. The conductive boundaries created by two metallic plates determine exactly which wavelengths of microwave can be emitted by the atom. By choosing the plate spacing correctly, the atom will find all routes from the 50th and 48th states blocked because it cannot emit the right frequency of radiation.\n" +
                "\n" +
                "The next problem is that Rydberg atoms don't sit still. Instead, they have to be trapped. In this case, the researchers think that they can accomplish this task using laser beams that have a donut profile—dark in the middle with a bright ring. Rydberg atoms are a bit like cockroaches: they avoid the light. So the laser (actually a pair of laser beams) will push the atoms into regularly spaced dark patches, resulting in a string of Rydberg atoms with a spacing that is controlled by the laser.\n" +
                "\n" +
                "With this, we have the tools to create qubits and to make sure that they don't run away. To compute something, the qubits need to be able to perform operations on each other, except the researchers don't plan to create discrete gates like a classical digital computer. Instead, they are thinking more in terms of analog computers. In this case, the researchers aim for the qubits to be continuously coupled to each other. If you arrange it right, the state of qubit one affects the state of qubit two, and vice versa.\n" +
                "\n" +
                "But you need to arrange it right: the coupling strength has to be under control so that it can be tuned from strong to weak.\n" +
                "\n" +
                "It turns out that the very nature of Rydberg atoms gives you this for free. The direct I-feel-your-charge force falls off with distance, while a less-direct coupling that depends on the spin of the electron depends on both the distance and a voltage applied to the capacitor. These two knobs are enough to complete an analog quantum computer based on trapped Rydberg atoms.\n" +
                "\n" +
                "Get a grip—it’s only theory\n" +
                "If what I described was all there was, this would be a nice idea but nothing more. Rydberg atoms are also relatively simple to model, however; once you know how a single Rydberg atom behaves, you can model a string of them as well. The researchers did this to check on things like whether the capacitor would stop the Rydberg atoms from decaying away, whether trapping would work, and whether the neighboring atoms could talk to each other in the way that the reseachers expected.\n" +
                "\n" +
                "Based on that model, it seems that not only is the idea worth pursuing, but it may even work. And it is not too far away. It is already possible to trap individual atoms in an optical lattice. It should be possible to use a combination of lasers and microwave sources to excite the trapped atoms to form the desired Rydberg states. The difficulty, as I see it, is that you have to do all of this in the confined space between two capacitor plates.\n" +
                "\n" +
                "I expect reports on trapped Rydberg atoms before summer and the first analog quantum computer experiments before the end of the year.\n" +
                "\n" +
                "Unlike most current attempts at quantum computing, this sort of device becomes useful even if there are fewer than 20 qubits total. That is because an analog quantum computer like this will mostly be used to understand physics problems, which are already difficult at comparatively small scale. Twenty qubits already takes you beyond what current classical computers can do and into the realm of usefulness.\n" +
                "\n" +
                "Physical Review X, 2018, DOI: 10.1103/PhysRevX.8.011032\n" +
                "\n" +
                "\n" +
                "source: https://arstechnica.com/science/2018/03/how-a-string-of-giant-atoms-might-bring-quantum-computers-to-physics-labs/");
    }


    private void bindView() {
        tvArticle = findViewById(R.id.tv_article);
        progressBar = findViewById(R.id.prog_bar);
        retryButton = findViewById(R.id.retry_button);
    }

    private String getWebPageContent(String pageLink) {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;

        try {
            //Thread.sleep(5000);
            URL urlObject = new URL(pageLink);
            connection = (HttpsURLConnection) urlObject.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(3000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();

            if (stream != null) {
                // Converts Stream to String.
                Reader reader = null;
                final int bufferSize = 1024;
                final char[] buffer = new char[bufferSize];
                final StringBuilder out = new StringBuilder();
                reader = new InputStreamReader(stream, "UTF-8");
                for (; ; ) {
                    int rsz = reader.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
                result = out.toString();
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    class WebContentLoaderTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            if(s != null) {
                tvArticle.setText(s);
                retryButton.setVisibility(View.GONE);
            }
            else{
                retryButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String pageLink = "https://demo1738991.mockable.io/giant_atoms";
            String result = getWebPageContent(pageLink);
            return result;
        }
    }
}

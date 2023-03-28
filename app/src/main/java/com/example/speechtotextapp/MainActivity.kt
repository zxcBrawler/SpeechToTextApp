package com.example.speechtotextapp
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private var speechRecognizer : SpeechRecognizer? = null
    private lateinit var inputVoice: Button
    private lateinit var text: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        speechToText()
    }

    private fun speechToText(){
        text = findViewById(R.id.speechToText)
        inputVoice = findViewById(R.id.inputVoice)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())
        inputVoice.setOnClickListener{
                speechRecognizer?.startListening(speechRecognizerIntent)
            Handler(Looper.getMainLooper()).postDelayed({
                speechRecognizer?.stopListening()
            }, 3000)
        }

        speechRecognizer?.setRecognitionListener(object : RecognitionListener{
            override fun onReadyForSpeech(params: Bundle?) {    }

            override fun onBeginningOfSpeech() {
                text.setText("Listening...")
            }
            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                text.setText(" ")
            }

            override fun onError(error: Int) {}

            override fun onResults(results: Bundle?) {
                val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                text.setText(data?.get(0))
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
    }
}
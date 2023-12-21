package com.mio.mio_ktx.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View.INVISIBLE
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mio.base.BaseActivity
import com.mio.base.BaseFragment
import com.mio.base.Tag.TAG
import com.mio.base.replaceFragment
import com.mio.base.view.BottomTab
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.ActivityMainBinding
import com.mio.mio_ktx.ui.mqtt.MqttHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    val fragments: List<BaseFragment<out ViewDataBinding>> by lazy {
        listOf(
            AFragment(),
            BFragment(),
        )
    }

    override fun initView() {
        showInitTag = true

        lifecycleScope.launch {
//            showLoading()
//            delay(1_200)
            replaceFragment(R.id.container, AFragment())
//            showContent()
        }
        mDataBinding.bt.setCheckedChangeListener(object : BottomTab.OnCheckChangeListener {
            override fun onChange(pos: Int) {
                Log.d(TAG, "onChange: $pos")
            }
        })

        mDataBinding.bt.visibility = INVISIBLE

        val startTime = System.currentTimeMillis()
        // 文字识别
//        testRec()
        // 人脸检测
//        testFaceRec()
        // 物体识别
//        testObjRec()

        val endTime = System.currentTimeMillis()

        testMqtt()

        val duration = endTime - startTime
        Log.d(TAG, "测试耗时: $duration")
    }

    /**
     * mqtt测试
     */
    private fun testMqtt() {
//        lifecycleScope.launch {
//            delay(500)
//            MqttHelper.init(this@MainActivity)
//
//        }
    }

    /**
     * 初始化数据
     */
    override fun initData() {
    }

    override fun initObserver() {
    }

    /**
     * 文字识别 可用
     */
    private fun testRec() {
        Log.d(TAG, "testRec: ")
        val image =
            InputImage.fromBitmap(BitmapFactory.decodeResource(resources, R.drawable.img_text), 0)

        val recognizer =
//            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) // 拉丁文
            TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())

        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val resultText = visionText.text
                for (block in visionText.textBlocks) {
                    val blockText = block.text
                    val blockCornerPoints = block.cornerPoints
                    val blockFrame = block.boundingBox
                    for (line in block.lines) {
                        val lineText = line.text
                        val lineCornerPoints = line.cornerPoints
                        val lineFrame = line.boundingBox
                        for (element in line.elements) {
                            val elementText = element.text
                            val elementCornerPoints = element.cornerPoints
                            val elementFrame = element.boundingBox
                            Log.d(TAG, "testRec: text: $elementText")
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
                Log.e(TAG, "testRec: $e")
            }
    }

    /**
     * 人脸检测 可用
     */
    private fun testFaceRec() {
        Log.d(TAG, "testFaceRec: ")
        // High-accuracy landmark detection and face classification
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

// Real-time contour detection
        val realTimeOpts = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()
        val image =
            InputImage.fromBitmap(BitmapFactory.decodeResource(resources, R.drawable.img_renqun), 0)

        val detector = FaceDetection.getClient()
        val result = detector.process(image)
            .addOnSuccessListener {
                for (face in it) {
                    val bounds = face.boundingBox
                    val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                    val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                    // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                    // nose available):
                    val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                    leftEar?.let {
                        val leftEarPos = leftEar.position
                    }

                    // If contour detection was enabled:
                    val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                    val upperLipBottomContour =
                        face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points

                    // If classification was enabled:
                    if (face.smilingProbability != null) {
                        val smileProb = face.smilingProbability
                    }
                    if (face.rightEyeOpenProbability != null) {
                        val rightEyeOpenProb = face.rightEyeOpenProbability
                    }

                    // If face tracking was enabled:
                    if (face.trackingId != null) {
                        val id = face.trackingId
                    }
                }
                Log.d(TAG, "testFaceRec: 检测到人脸个数:${it.size}")
            }
    }

    /**
     * 物体识别 不可用 会闪退
     */
    private fun testObjRec() {
        lifecycleScope.launch {
            val image =
                InputImage.fromBitmap(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.image_dog
                    ), 0
                )

            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            val result = labeler.process(image)
                .addOnSuccessListener { labels ->
                    // Task completed successfully
                    // ...
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        val index = label.index
                        Log.d(TAG, "testObjRec: text:$text confidence:$confidence index:$index")
                    }
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                    Log.e(TAG, "testObjRec: $e")
                }
        }
    }
}

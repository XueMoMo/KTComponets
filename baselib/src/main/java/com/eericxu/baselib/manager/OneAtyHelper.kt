package com.eericxu.baselib.manager

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import com.eericxu.baselib.BaseComponent
import com.eericxu.baselib.OneAty
import com.eericxu.baselib.ui.DialogCpt
import java.lang.Exception

/*单Activity 的实现帮助类*/
class OneAtyHelper(oneAty: OneAty) {
    private val cManager = ComponentManager(oneAty)
    private val mRoot = oneAty.getRoot()
    private val sWidth = mRoot.resources.displayMetrics.widthPixels.toFloat()
    private val sHeight = mRoot.resources.displayMetrics.heightPixels.toFloat()
    private val aty by lazy { oneAty as Activity }
    fun onCreateAty(savedInstanceState: Bundle?) {
        cManager.clear()
        initSensor()
    }

    private fun initSensor() {
        val manager = mRoot.context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        manager.registerListener(object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }

            override fun onSensorChanged(event: SensorEvent?) {
                event?.apply {
                    val rotationMatrix = FloatArray(16)
                    SensorManager.getRotationMatrixFromVector(
                            rotationMatrix, values)
                    // Remap coordinate system
                    val remappedRotationMatrix = FloatArray(16)
                    SensorManager.remapCoordinateSystem(rotationMatrix,
                            SensorManager.AXIS_X,
                            SensorManager.AXIS_Z,
                            remappedRotationMatrix)

                    // Convert to orientations
                    val orientations = FloatArray(3)
                    SensorManager.getOrientation(remappedRotationMatrix, orientations)
                    val abs = Math.abs(orientations[2])
                    onOrientationChange(abs > 45)
                }

            }
        }, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun onOrientationChange(isLandscan: Boolean) {
        val element = cManager.lastElement()
        element?.apply {
            if (screenOrientation == BaseComponent.ORIENTATION_SENSOR) {
                aty.requestedOrientation = if (isLandscan) BaseComponent.ORIENTATION_LANDSCAPE else BaseComponent.ORIENTATION_PORTRAIT
            }
        }
    }

    fun onStart() {

    }

    fun onResume() {

    }

    fun onPause() {}

    fun onStop() {}


    fun onRestart() {

    }


    fun onDestroy() {}
    /**
     * 回退栈
     * return  true 回退成功  false 回退失败*/
    fun back(): Boolean {
        if (cManager.canBack()) {
            remove()
            return true
        }
        return false
    }


    private var screenOrientation: Int = 1
    private fun screenIsPortrait(): Boolean {
        return screenOrientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun animLength(): Float {
        return if (screenIsPortrait()) sWidth else sHeight
    }

    fun defInAnim(): Animator? {
        return ObjectAnimator.ofFloat(null, View.TRANSLATION_X, animLength(), 0f).setDuration(200)
    }

    fun defOutAnim(): Animator? {
        return ObjectAnimator.ofFloat(null, View.TRANSLATION_X, 0f, animLength()).setDuration(200)
    }

    fun defLastOut(): Animator? {
        return ObjectAnimator.ofFloat(null, View.TRANSLATION_X, 0f, -animLength()).setDuration(200)
    }

    fun defLstShow(): Animator? {
        return ObjectAnimator.ofFloat(null, View.TRANSLATION_X, -animLength(), 0f).setDuration(200)
    }

    /**
     * 启动一个组件*/
    fun start(component: BaseComponent, anim: Animator? = defInAnim(), lastOutAnim: Animator? = defLastOut(), hideLast: Boolean = true) {
        cManager.start(component, anim, lastOutAnim, hideLast)
    }

    /**
     * 启动一个组件*/
    fun start(clazzName: String, map: MutableMap<String, Any>? = null, anim: Animator? = defInAnim(), lastOutAnim: Animator? = defLastOut(), hideLast: Boolean = true) {
        try {
            Class.forName(clazzName)?.apply {
                val constructor = constructors[0]
                val component =
                        if (map == null)
                            constructor.newInstance(mRoot.context)
                        else
                            constructor.newInstance(mRoot.context, map)
                start(component as BaseComponent, anim, lastOutAnim, hideLast)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 通过泛型启动 */
    inline fun <reified T : BaseComponent> startT(
            map: MutableMap<String, Any>? = null,
            anim: Animator? = defInAnim(),
            lastOutAnim: Animator? = defLastOut(),
            hideLast: Boolean = true) {
        start(T::class.java.name, map, anim, lastOutAnim, hideLast)
    }


    /**
     * 移除一个组件 不传时 将移除最后启动的那个组件*/
    fun remove(component: BaseComponent? = null, anim: Animator? = defOutAnim(), lastShowAnim: Animator? = defLstShow()) {
        val element = cManager.lastElement()

        if (element != null && element is DialogCpt)
            dismiss(element)
        else
            cManager.remove(component, anim, lastShowAnim)
    }

    fun onConfigurationChanged(newConfig: Configuration?) {
        newConfig?.apply {
            screenOrientation = orientation
            Log.i("OneAty:", "orientation:${newConfig.orientation}  w:$sWidth   h:$sHeight")
        }
    }

    fun show(dialogCpt: DialogCpt) {
        val inAnim = ObjectAnimator.ofFloat(null, View.ALPHA, 0f, 1f)
        inAnim.duration = 200
        cManager.start(dialogCpt, inAnim, null, false)
    }

    fun dismiss(dialogCpt: DialogCpt) {
        val outAnim = ObjectAnimator.ofFloat(null, View.ALPHA, 1f, 0f)
        outAnim.duration = 200
        cManager.remove(dialogCpt, outAnim, null)
    }


}
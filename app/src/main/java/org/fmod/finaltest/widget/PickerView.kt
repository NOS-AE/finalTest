package org.fmod.finaltest.widget

import android.content.Context
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import org.fmod.finaltest.R
import org.fmod.finaltest.util.toplevel.log
import java.lang.ref.WeakReference
import java.util.*

class PickerView {
    companion object {

        private lateinit var timePickerRef: WeakReference<TimePickerView>

        private lateinit var contextRef: WeakReference<Context>

        fun getTimePicker(context: Context, now: Calendar, callback: (Date)->Unit): TimePickerView {
            return when{
                !this::contextRef.isInitialized -> {
                    contextRef = WeakReference(context)
                    buildTimePicker(context, now, callback)
                }
                context != contextRef.get() -> {
                    //context改变，重构picker
                    log("context change, rebuild picker")
                    buildTimePicker(context, now, callback)
                }
                else -> {
                    //复用当前context的picker
                    timePickerRef.get() ?: buildTimePicker(context, now, callback)
                }
            }

        }

        private fun buildTimePicker(context: Context, now: Calendar, callback: (Date)->Unit): TimePickerView {
            val minDate = Calendar.getInstance().apply {
                add(Calendar.YEAR, -3)
                set(Calendar.MONTH, 0)
                set(Calendar.DAY_OF_YEAR, 1)
            }
            val maxDate = Calendar.getInstance()

            val res = TimePickerBuilder(context) { d, _ ->
                callback(d)
            }.apply {
                setDate(now)
                setCancelColor(context.getColor(R.color.colorTheme))
                setSubmitColor(context.getColor(R.color.colorTheme))
                setTitleText("选择交易日期")
                setRangDate(minDate, maxDate)
            }.build()
            timePickerRef = WeakReference(res)
            return res
        }
    }

}
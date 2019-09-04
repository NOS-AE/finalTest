package org.fmod.finaltest.widget

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.kind_item.view.*
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.R
import org.fmod.finaltest.bean.BigKind
import java.lang.ref.WeakReference

class KindView(layoutInflater: LayoutInflater, container: ViewGroup?) {

    companion object{

        val selectedIconColor by lazy {
            ContextCompat.getColorStateList(MyApp.appContext, R.color.white)
        }
        val defaultIconColor by lazy {
            ContextCompat.getColorStateList(MyApp.appContext, R.color.colorKindIcon)
        }
        val defaultBgColor by lazy {
            ContextCompat.getColorStateList(MyApp.appContext, R.color.colorKindBg)
        }

        lateinit var selectedView: WeakReference<View>
        lateinit var selectedKind: BigKind

    }

    val view: View = layoutInflater.inflate(R.layout.kind_item, container, false)

    @SuppressLint("CheckResult")
    fun bind(kind: BigKind, selectedCallback: (BigKind)->Unit){

        view.clicks()
            .filter {
                view != selectedView.get()
            }
            .subscribe {
                selectedKind.selected = false
                kind.selected = true
                selectedKind = kind

                view.run {
                    kind_icon.imageTintList = selectedIconColor
                    kind_icon.backgroundTintList = kind.bgColorRes
                }

                selectedView.get()?.run {
                    kind_icon.imageTintList = defaultIconColor
                    kind_icon.backgroundTintList = defaultBgColor
                }
                selectedView = WeakReference(view)
                selectedCallback(kind)
            }


        view.run {
            /*
                被选中的图标，icon和bg颜色为bean的值
                未选中的图标，icon和bg颜色为默认值
             */
            if(kind.selected) {
                kind_icon.backgroundTintList = kind.bgColorRes
                kind_icon.imageTintList = selectedIconColor
                selectedView = WeakReference(this)
            }
            else{
                kind_icon.backgroundTintList = defaultBgColor
                kind_icon.imageTintList = defaultIconColor
            }

            kind_icon.setImageResource(kind.iconResId)
            kind_name.text = kind.name
        }
    }

}
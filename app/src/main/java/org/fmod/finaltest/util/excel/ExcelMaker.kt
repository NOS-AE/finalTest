package org.fmod.finaltest.util.excel

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.apache.poi.hssf.usermodel.*
import org.fmod.finaltest.bean.DealItem
import org.fmod.finaltest.util.toplevel.log
import org.fmod.finaltest.util.toplevel.poiLog
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter
import kotlin.collections.ArrayList

/**
 * 一个Maker对应一个工作簿
 * */
class ExcelMaker {
    companion object {
        //日期格式
        const val pattern = "yyyy-MM-dd"

        //创建新实例
        fun newInstance() = ExcelMaker().apply {
            mWorkBook = HSSFWorkbook()
        }

    }

    private lateinit var mWorkBook: HSSFWorkbook
    private lateinit var mHeaderStyle: HSSFCellStyle
    private lateinit var mDataStyle: HSSFCellStyle

    fun createSheet(title: String, headers: List<String>, dataSet: List<DealItem>): ExcelMaker {
        createSheet(
            title,
            headers,
            dataSet,
            PredefinedExcel.createHeaderCellStyle(mWorkBook),
            PredefinedExcel.createDataCellStyle(mWorkBook)
        )
        return this
    }

    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     * @param title
     *          表格标题名
     * @param headers
     *          表格属性列名数组
     * @param dataSet
     *          需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *          javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param headerStyle
     *          自定义的header风格
     * @param dataStyle
     *          自定义的data风格
     */
    private fun createSheet(title: String, headers: List<String>, dataSet: List<DealItem>, headerStyle: HSSFCellStyle, dataStyle: HSSFCellStyle){

        mHeaderStyle = headerStyle
        mDataStyle = dataStyle
        //创建表格
        val sheet = mWorkBook.createSheet(title).apply {
            //设置表格默认字节宽度15字节
            defaultColumnWidth = 15 //sheet.autoSizeColumn(column)
        }
        //创建标题行
        createHeadersRow(headers, sheet)
        //创建数据行
        createDataRow(dataSet, sheet)
    }

    /**
     * 创建标题行
     */
    private fun createHeadersRow(headers: List<String>, sheet: HSSFSheet){
        val row = sheet.createRow(0)
        for(i in headers.indices){
            row.createCell(i).apply {
                setCellStyle(mHeaderStyle)
                setCellValue(headers[i])
            }
        }
    }

    /**
     * 创建数据行
     */
    private fun createDataRow(data: List<DealItem>, sheet: HSSFSheet){
        var row: HSSFRow
        val format = SimpleDateFormat(pattern, Locale.CHINA)
        for(i in data.indices){
            row = sheet.createRow(i+1)

            setCellValue(data[i].type, row, 0)
            setCellValue(format.format(data[i].dateNum), row, 1)
            setCellValue(if(data[i].isIncome) "收入" else "支出", row, 2)
            setCellValue(data[i].money, row, 3)
            setCellValue(data[i].remarks, row, 4)

        }
    }

    /**
     * 判断反射得到的方式是否getter
     * */
    private fun isGetter(method: Method) = method.name.substring(0,3) == "get"

    /**
     * 判断获取的value类型，并设置到cell上
     *
     * @param value
     *          通过反射getter到的属性
     * @param row
     *          需要设置的行
     * @param column
     *          属性所在列
     * */
    private fun setCellValue(value: Any, row: HSSFRow, column: Int){
        val cell = row.createCell(column)
        cell.setCellValue(HSSFRichTextString(value.toString()))
    }

    /**
     * 导出工作簿
     *
     * @param filePath
     *            本地文件路径
     * */
    fun export(filePath: String): Observable<String>{
        return Observable.create<String> {
            val fos = FileOutputStream(filePath)
            mWorkBook.write(fos)
            fos.close()
            it.onNext(filePath)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 创建标题行
     * test
     */
    /*private fun createHeadersRowTest(headers: List<String>){
        for(i in headers.indices){
            log("h:${headers[i]}, c:$i")
        }
    }

    /**
     * 创建数据行
     * 利用反射，根据javaBean属性先后顺序，调用getXxx()方法得到属性值
     * test
     */
    private fun <T: Any>createDataRowTest(data: ArrayList<T>){
        var methods: Array<Method>
        var value: Any
        for(i in 0 until data.size){
            //遍历所有方法，只使用getter
            methods = data[i].javaClass.declaredMethods
            for (column in 0 until methods.size){
                if(isGetter(methods[column])){
                    value = methods[column].invoke(data[i])
                    //测试数据
                    log("v:$value, r:$i, c:$column")
                }
            }
        }
    }*/
}
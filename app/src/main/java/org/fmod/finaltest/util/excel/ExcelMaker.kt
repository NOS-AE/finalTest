package org.fmod.finaltest.util.excel

import org.apache.poi.hssf.usermodel.*
import org.fmod.finaltest.util.toplevel.log
import org.fmod.finaltest.util.toplevel.poiLog
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Method

/**
 * 一个Maker对应一个工作簿
 * */
class ExcelMaker {
    companion object {
        //日期格式
        const val pattern = "yyyy-MM-dd"

        //创建新实例
        fun newInstance() = ExcelMaker()

        fun newInstance(workBook: HSSFWorkbook) = ExcelMaker().apply {
            mWorkBook = workBook
        }

    }

    lateinit var mWorkBook: HSSFWorkbook
    lateinit var mHeaderStyle: HSSFCellStyle
    lateinit var mDataStyle: HSSFCellStyle

    fun <T: Any>createSheet(title: String, headers: List<String>, dataSet: ArrayList<T>) {
        if(!this::mWorkBook.isInitialized){
            poiLog("mWorkBook hasn't been initialized")
            return
        }
        createSheet(title, headers, dataSet, PredefinedExcel.createHeaderCellStyle(mWorkBook), PredefinedExcel.createDataCellStyle(mWorkBook))
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
    fun <T: Any>createSheet(title: String, headers: List<String>, dataSet: ArrayList<T>, headerStyle: HSSFCellStyle, dataStyle: HSSFCellStyle){
        if(!this::mWorkBook.isInitialized){
            poiLog("mWorkBook hasn't been initialized")
            return
        }
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
    fun createHeadersRow(headers: List<String>, sheet: HSSFSheet){
        val row = sheet.createRow(0)
        for(i in 0 until headers.size){
            row.createCell(i).apply {
                setCellStyle(mHeaderStyle)
                setCellValue(headers[i])
            }
        }
    }

    /**
     * 创建数据行
     * 利用反射，根据javaBean属性先后顺序，调用getXxx()方法得到属性值
     */
    fun <T: Any>createDataRow(data: ArrayList<T>, sheet: HSSFSheet){
        var row: HSSFRow
        var methods: Array<Method>
        var value: Any
        for(i in 1..data.size){
            row = sheet.createRow(i)
            //遍历所有方法，只使用getter
            methods = data[i].javaClass.methods
            for (column in 0 until methods.size){
                if(isGetter(methods[column])){
                    value = methods[column].invoke(data[i])
                    setCellValue(value, row, column)
                }
            }
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
    fun export(filePath: String){
        try {
            val fos = FileOutputStream(filePath)
            mWorkBook.write(fos)
            fos.close()
        }catch (e: IOException){
            log("ExcelMaker export: ${e.message}")
        }

    }

    /**
     * 创建标题行
     * test
     */
    fun createHeadersRowTest(headers: List<String>){
        for(i in 0 until headers.size){
            log("h:${headers[i]}, c:$i")
        }
    }

    /**
     * 创建数据行
     * 利用反射，根据javaBean属性先后顺序，调用getXxx()方法得到属性值
     * test
     */
    fun <T: Any>createDataRowTest(data: ArrayList<T>){
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
    }
}
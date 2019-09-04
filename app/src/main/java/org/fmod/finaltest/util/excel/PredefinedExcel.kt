package org.fmod.finaltest.util.excel

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment

class PredefinedExcel {
    companion object{
        fun createHeaderCellStyle(workBook: HSSFWorkbook) = workBook.createCellStyle().apply {
            //天空蓝前景色填充
            fillForegroundColor = HSSFColor.HSSFColorPredefined.SKY_BLUE.index
            setFillPattern(FillPatternType.SOLID_FOREGROUND)
            //细边框
            setBorderBottom(BorderStyle.THIN)
            setBorderRight(BorderStyle.THIN)
            setBorderLeft(BorderStyle.THIN)
            setBorderTop(BorderStyle.THIN)
            //居中
            setAlignment(HorizontalAlignment.CENTER)
            setVerticalAlignment(VerticalAlignment.CENTER)
            //紫罗兰色字体，字高12，加粗
            setFont(workBook.createFont().apply {
                color = HSSFColor.HSSFColorPredefined.VIOLET.index
                fontHeightInPoints = 12
                bold = true
            })
        }

        fun createDataCellStyle(workBook: HSSFWorkbook) = workBook.createCellStyle().apply {
            //淡黄前景色填充
            fillForegroundColor = HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.index
            setFillPattern(FillPatternType.SOLID_FOREGROUND)
            //细边框
            setBorderBottom(BorderStyle.THIN)
            setBorderRight(BorderStyle.THIN)
            setBorderLeft(BorderStyle.THIN)
            setBorderTop(BorderStyle.THIN)
            //居中
            setAlignment(HorizontalAlignment.CENTER)
            setVerticalAlignment(VerticalAlignment.CENTER)
            //黑色字体，加粗
            setFont(workBook.createFont().apply {
                color = HSSFColor.HSSFColorPredefined.BLACK.index
                bold = true
            })
        }
    }
}
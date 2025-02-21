package com.example.beupdated.paymentsuccess.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.example.beupdated.R
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.paymentsuccess.domain.PaymentRepository
import com.example.beupdated.paymentsuccess.domain.Receipt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RepositoryImpl(private val context: Context) : PaymentRepository {
    override suspend fun generateReceipt(receipt: Receipt): Flow<CustomResult<String>> {
      return flow {
          val directory = File(
              context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
              "Receipts"
          ).apply {
              if (!exists()) mkdirs()
          }

          val file = File(directory, "${receipt.orderId}_receipt.pdf")

          try {
              val pdfDocument = PdfDocument().apply {
                  val pageInfo = PdfDocument.PageInfo.Builder(300, 800, 1).create()
                  val page = startPage(pageInfo)

                  val paint = Paint().apply {
                      textSize = 12f
                      typeface = Typeface.DEFAULT
                      color = Color.BLACK
                  }

                  val titlePaint = Paint().apply {
                      textSize = 16f
                      typeface = Typeface.DEFAULT_BOLD
                      color = Color.BLACK
                  }

                  val smallPaint = TextPaint().apply {
                      textSize = 10f
                      typeface = Typeface.DEFAULT
                      color = Color.GRAY
                  }

                  page.canvas.apply {
                      val logo = BitmapFactory.decodeResource(context.resources, R.drawable.beupdatedlogo)
                      if (logo != null) {
                          val scaledLogo = Bitmap.createScaledBitmap(logo, 60, 60, true)
                          drawBitmap(scaledLogo, 120f, 20f, null)
                          scaledLogo.recycle()
                          logo.recycle()
                      }

                      drawText("Order Receipt", 100f, 100f, titlePaint)

                      val startX = 50f
                      var currentY = 140f
                      val lineSpacing = 30f

                      val receiptDetails = listOf(
                          "Order ID: ${receipt.orderId}",
                          "Pick-Up Date: ${receipt.pickUpDate}",
                          "Status: ${receipt.status}"
                      )

                      receiptDetails.forEach { detail ->
                          drawText(detail, startX, currentY, paint)
                          currentY += lineSpacing
                      }

                      currentY += lineSpacing
                      drawText("Thank you for your purchase!", startX, currentY, paint)

                      currentY += lineSpacing * 1.5f
                      val terms = """
                    Important Notice:
                    Please note that your product must be retrieved within 1 month from your 
                    reservation date (${receipt.pickUpDate}). After this period, your reservation 
                    will become invalid and the product will no longer be available for retrieval. 
                    No refunds will be provided for unclaimed items after the expiration date.
                """.trimIndent()

                      val textWidth = pageInfo.pageWidth - (startX * 2)
                      val staticLayout = StaticLayout.Builder
                          .obtain(terms, 0, terms.length, smallPaint, textWidth.toInt())
                          .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                          .setLineSpacing(0f, 1f)
                          .setIncludePad(false)
                          .build()

                      save()
                      translate(startX, currentY)
                      staticLayout.draw(this)
                      restore()
                  }

                  finishPage(page)

                  val fos = FileOutputStream(file)
                  writeTo(fos)
                  fos.close()
              }

              pdfDocument.close()

              emit(CustomResult.Success(file.absolutePath))
          } catch (exception: Exception) {
              val errorMessage = when (exception) {
                  is IOException -> "Failed to write PDF file: ${exception.localizedMessage}"
                  is SecurityException -> "Permission denied: ${exception.localizedMessage}"
                  else -> "Unexpected error: ${exception.localizedMessage}"
              }
              emit(CustomResult.Failure(Exception(errorMessage, exception)))
          }
      }.flowOn(Dispatchers.IO)
    }
}
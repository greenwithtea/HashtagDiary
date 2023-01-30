package com.android.hashtagdiary

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_prev_result.*
import kotlinx.android.synthetic.main.activity_result.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class PrevResultActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb : SQLiteDatabase

    lateinit var tvLine1_prev : TextView  // date
    lateinit var tvLine2_prev : TextView  // sleep
    lateinit var tvLine3_prev : TextView  // weather
    lateinit var tvLine4_prev : TextView  // food
    lateinit var tvLine5_prev : TextView  // meet
    lateinit var tvLine6_prev : TextView  // mood
    lateinit var tvHashtag_prev : TextView  // hashtag

    ///
    // 이전의 위도, 경도를 받을 변수 선언
    var latitude : Double = 0.0
    var longtitude : Double = 0.0

    lateinit var map_View_prev : ConstraintLayout
    lateinit var mapView : MapView

    lateinit var btnBack_prev : Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prev_result)

        dbManager = DBManager(this, "diarybyday", null , 1)

        tvLine1_prev = findViewById(R.id.tvLine1_prev)
        tvLine2_prev = findViewById(R.id.tvLine2_prev)
        tvLine3_prev = findViewById(R.id.tvLine3_prev)
        tvLine4_prev = findViewById(R.id.tvLine4_prev)
        tvLine5_prev = findViewById(R.id.tvLine5_prev)
        tvLine6_prev = findViewById(R.id.tvLine6_prev)
        tvHashtag_prev = findViewById(R.id.tvHashTag_prev)

        btnBack_prev = findViewById(R.id.btnBack_prev)

        map_View_prev = findViewById(R.id.map_View_prev)

        sqlitedb = dbManager.readableDatabase
        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM diarybyday;", null)

        var datePrev :String = intent.getStringExtra("date").toString()
        var dateParam = LocalDate.parse(datePrev, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        var tvDate = dateParam.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))

        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals(datePrev)) {
                tvLine1_prev.text = "오늘은 ${tvDate},"
                tvLine2_prev.text = cursor.getString(1)
                tvLine3_prev.text = cursor.getString(2)
                tvLine4_prev.text = cursor.getString(3)
                tvLine5_prev.text = cursor.getString(4)
                tvLine6_prev.text = cursor.getString(5)
                tvHashtag_prev.text = cursor.getString(6)
                latitude = cursor.getDouble(7)
                longtitude = cursor.getDouble(8)
                break
            }
        }

        mapView = MapView(this)
        map_View_prev.addView(mapView)

        val uNowPosition = MapPoint.mapPointWithGeoCoord(latitude, longtitude)

        mapView.setMapCenterPoint(uNowPosition, true)
        mapView.setZoomLevel(1, true)

        // 현 위치에 마커 찍기
        val marker = MapPOIItem()
        marker.itemName = "현 위치"
        marker.mapPoint =uNowPosition
        marker.markerType = MapPOIItem.MarkerType.BluePin
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
        mapView.addPOIItem(marker)

        btnBack_prev.setOnClickListener {
            var intentNavi = Intent(this, NaviActivity::class.java)
            startActivity(intentNavi)
        }

    }
}
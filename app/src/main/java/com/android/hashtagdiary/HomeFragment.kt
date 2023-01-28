package com.android.hashtagdiary

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.LocalDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var btnRecord : Button

    lateinit var dbManager : DBManager
    lateinit var sqlitedb : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        btnRecord = view.findViewById(R.id.btnRecord)
        dbManager = DBManager(requireContext(), "diarybyday", null, 1)

        sqlitedb = dbManager.readableDatabase
        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT date FROM diarybyday;", null)
        var today = LocalDate.now().toString()

        var plag : String = "기록 안함"
        btnRecord.setOnClickListener {
            while (cursor.moveToNext()) {
                if (today.equals(cursor.getString(0).toString())) {
                    Toast.makeText(requireContext(), "이미 오늘의 일기가 기록되었습니다.", Toast.LENGTH_LONG).show()
                    plag = "기록 완료"
                    break
                }
                else {
                }
            }

            if (plag.equals("기록 안함")) {
                var intentRecord = Intent(getActivity(), RecordActivity::class.java)
                startActivity(intentRecord)
            }
            sqlitedb.close()
            cursor.close()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
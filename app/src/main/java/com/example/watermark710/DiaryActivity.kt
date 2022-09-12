package com.example.watermark710

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class DiaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)


        //데이터 불러오기
        val dataModelList = mutableListOf<DataModel>()

        //리스트뷰에 띄우기
        val listView = findViewById<ListView>(R.id.mainLV)
        val adapter_list = ListViewAdapter(dataModelList)
        listView.adapter = adapter_list


        val database = Firebase.database
        val myRef = database.getReference("diary")
        myRef.child(Firebase.auth.currentUser!!.uid).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dataModelList.clear()
                for(dataModel in snapshot.children){
                    Log.e("data스냅샷",dataModel.toString())
                    dataModelList.add(dataModel.getValue(DataModel::class.java)!!)
                }

                Log.e("데이터모델리스트 확인",dataModelList.toString())
                adapter_list.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })




        //다이얼로그띄우기
        val writeButton = findViewById<ImageView>(R.id.writeBtn)
        writeButton.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("커플 다이어리 다이얼로그")

            var dateText = ""
            //다이얼로그 띄움
            val maAlertDialog = mBuilder.show()
            //다이얼로그 내부 요소(날짜 선택 버튼) 이벤트처리
            maAlertDialog.findViewById<Button>(R.id.dateSelectBtn)?.setOnClickListener {
                val today = GregorianCalendar()
                val year:Int = today.get(Calendar.YEAR)
                val month:Int = today.get(Calendar.MONTH)
                val date:Int = today.get(Calendar.DATE)

                val dlg = DatePickerDialog(this,object:DatePickerDialog.OnDateSetListener{
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, date: Int) {
                        Log.e("check date","${year}/${month}/${date}")
                        maAlertDialog.findViewById<Button>(R.id.dateSelectBtn)?.setText("${year}/${month+1}/${date}")

                        dateText = "${year}/${month+1}/${date}"
                    }

                }, year,month,date)
                dlg.show()
            }

            val saveBtn = maAlertDialog.findViewById<Button>(R.id.saveBtn)
            saveBtn?.setOnClickListener {
                val database = Firebase.database


                val diaryText = maAlertDialog.findViewById<EditText>(R.id.diaryText)?.text.toString()
                val myRef = database.getReference("diary").child(Firebase.auth.currentUser!!.uid)

                val model = DataModel(dateText,diaryText)

                //myRef.setValue("hello, world")
                myRef.push().setValue(model)
                Toast.makeText(this,"저장!",Toast.LENGTH_SHORT).show()

                maAlertDialog.dismiss()

            }
        }


    }
}
package com.longnd.tracuudiemthi;

import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.R.bool;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.longnd.tracuudiemthi.adapters.MainListAdapter;
import com.longnd.tracuudiemthi.models.Thisinh;

public class MainActivity extends Activity {
	//Đường dẫn tìm thông tin thí sinh
	public static String URL_SEARCH_SBD = "http://long.nvttest.com/stile/pages/getdiem.jsp?sbd=";
	public static String URL_SEARCH_HOTEN = "http://long.nvttest.com/stile/pages/getdiem.jsp?hoten=";
	
	//Các đối tượng trên View
	ListView listView;
	EditText editText;
	Spinner spinner;
	Button button;
	
	//Chứa kết quả trả về
	Vector<Thisinh> thisinhs = new Vector<Thisinh>();	
	MainListAdapter adapter = null;
	
	//Lựa chọn tìm theo tên || số báo danh
	ArrayAdapter<String> adapterSpinner = null;
	Thisinh thisinh = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		//Liên kết với file xml
		listView = (ListView) findViewById(R.id.listView1);
		spinner = (Spinner) findViewById(R.id.spinner);
		editText = (EditText) findViewById(R.id.editText_keyword);
		button = (Button) findViewById(R.id.button_search);
		
		//
		adapter = new MainListAdapter(getApplicationContext(),
				R.layout.main_list_item, thisinhs);
		adapterSpinner = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.spinner_item_list, R.id.textView, new String[] {
						"Tìm kiếm theo số báo danh", "Tìm kiếm theo tên" });
		spinner.setAdapter(adapterSpinner);

		listView.setAdapter(adapter);
		
		//Bắt sự kiện lựa chọn spinner
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 0) {
					editText.setHint("Nhập số báo danh");
				} else {
					editText.setHint("Nhập họ tên thí sinh");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		//Bắt sự kiện nut xem
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new searchDiemThi().execute(editText.getText().toString());
			}
		});
	}
	
	//Tiến trình con, lấy thông tin trả về từ httpGet
	public class searchDiemThi extends AsyncTask<String, Void, Void> {
		private ProgressDialog dialog;
		private boolean flag = false;

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("Đang tải...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			String stringUrl = ((spinner.getSelectedItemPosition() == 0) ? MainActivity.URL_SEARCH_SBD
					: MainActivity.URL_SEARCH_HOTEN)
					+ params[0];
			try {
				//Thư viện
				Document document = Jsoup.connect(stringUrl).timeout(10000)
						.get();

				String res = document.text().trim();
				if (res.trim().contains("Khong co du lieu")) {
					flag = false;
					return null;
				}
				thisinh = new Thisinh();

				thisinh.setDiem(res);
				thisinhs.add(thisinh);
				flag = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			if (flag == false) {
				Toast.makeText(getApplicationContext(),
						"Không tìm thấy thông tin!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.action_bar) {
			Intent intent = new Intent(MainActivity.this, BarActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}

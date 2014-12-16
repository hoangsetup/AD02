/*
 * Activity chính
 */
package com.longnd.tracuudiemthi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	// Đường dẫn tìm thông tin thí sinh
	public static String URL_SEARCH_SBD = "http://long.nvttest.com/stile/pages/getdiem.jsp?sbd=";
	public static String URL_SEARCH_HOTEN = "http://long.nvttest.com/stile/pages/getdiem.jsp?hoten=";

	// Các đối tượng trên View
	TextView textViewKq;
	EditText editText;
	Spinner spinner;
	Button button;

	// Lựa chọn tìm theo tên || số báo danh
	ArrayAdapter<String> adapterSpinner = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_main);

		// Liên kết với file xml
		textViewKq = (TextView) findViewById(R.id.textView_kq);
		spinner = (Spinner) findViewById(R.id.spinner);
		editText = (EditText) findViewById(R.id.editText_keyword);
		button = (Button) findViewById(R.id.button_search);

		//
		adapterSpinner = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.spinner_item_list, R.id.textView, new String[] {
						"Tìm kiếm theo số báo danh", "Tìm kiếm theo tên" });
		spinner.setAdapter(adapterSpinner);

		// Bắt sự kiện lựa chọn 1 dòng của spinner
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
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

		// Bắt sự kiện nút xem được click
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(editText.getText())) {
					//nếu thông tin ô text bị trống thì thông báo lỗi
					editText.setError("Thông tin này không được bỏ trống!");
					return;
				}
				//Thực hiện tiến trình con lấy thông tin trên internet
				new searchDiemThi().execute(editText.getText().toString());
			}
		});
	}

	// Tiến trình con, lấy thông tin trả về từ internet
	public class searchDiemThi extends AsyncTask<String, Void, String> {
		//Dialog hiện lên trong quá trình chờ
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

		//Công việc chính của tiến trình
		@Override
		protected String doInBackground(String... params) {
			//Lựa chọn url theo item spinner được chọn
			//chọn dòng 0 thì tìm theo sbd, 1 theo họ tên
			String stringUrl = ((spinner.getSelectedItemPosition() == 0) ? MainActivity.URL_SEARCH_SBD
					: MainActivity.URL_SEARCH_HOTEN)
					+ params[0];
			try {
				// Thư viện kết nối và lấy data trả về từ request GET tới 1 url
				Document document = Jsoup.connect(stringUrl).timeout(10000)
						.get();
				//Lấy đoạn text trong thẻ body
				String res = document.text().trim();
				//Nếu không có dữ liệu trả về null
				if (res.trim().equals("Khong co du lieu")) {
					flag = false;
					return null;
				}//Nếu có dữ liệu
				flag = true;
				return res;
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// ẩn dialog
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			if (flag == false) {
				Toast.makeText(getApplicationContext(),
						"Không tìm thấy thông tin!", Toast.LENGTH_SHORT).show();
				textViewKq.setText("");
			}
			if (result != null) {
				//Hiện thị kết quả lên textView
				textViewKq.setText(result.replace(", ", "\n").trim()
						.replace("|", "\n").trim().replace(" Đ", "\nĐ"));
			}
		}
	}

	//Khởi tạo menu có nut để chuyển vào activity bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//Item xem biểu đồ được lựa chọn
		if (item.getItemId() == R.id.action_bar) {
			Intent intent = new Intent(MainActivity.this, BarActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}

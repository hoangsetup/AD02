/*
 * Activity thực hiện show biểu đồ
 */
package com.longnd.tracuudiemthi;

import java.util.ArrayList;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.longnd.tracuudiemthi.holographlibrary.Bar;
import com.longnd.tracuudiemthi.holographlibrary.BarGraph;

public class BarActivity extends Activity {
	// Đường dẫn lấy thông tin biểu đồ
	public static String URL_DIEMTONG = "http://long.nvttest.com/stile/pages/dulieubieudo.jsp?luachon=";
	// Danh sách cột trong biểu đồ
	private ArrayList<Bar> points = new ArrayList<Bar>();

	// Các đối tượng trên view
	// Biểu đồ
	private BarGraph barGraph;
	// Tên biểu đồ
	private TextView textViewTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bar);

		// Liên kết tới file xml
		barGraph = (BarGraph) findViewById(R.id.graph);
		textViewTitle = (TextView) findViewById(R.id.textView_title);

		// Khởi tạo hộp thoại lựa chọn tiêu chí vẽ bd ngay lúc khởi tạo
		Intent intent = new Intent(BarActivity.this, SubBarActivity.class);
		startActivityForResult(intent, 100);

		// Chạm| click vào tên biểu đổ để tạo biểu đổ mới
		textViewTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BarActivity.this,
						SubBarActivity.class);
				startActivityForResult(intent, 100);
			}
		});
	}

	// Bắt sự kiện khi hộp thoại lựa chọn bị đóng lại (SubActivity)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// Lấy thông tin từ hộp thoại gửi về
		if (resultCode != 111)// Bị đóng mà không phải nhấn nút xong
			return;
		// Hộp thoại được đóng với thông tin truyền trở lại
		String param = data.getStringExtra("res");
		int index = data.getIntExtra("int", 0);
		if (index == 0) {
			textViewTitle
					.setText("Biểu đồ: Phân bố tổng điểm theo số thí sinh");
		} else {
			textViewTitle.setText("Biểu đồ: Phân bố điểm môn "
					+ getResources().getStringArray(R.array.textHienthi)[index]
					+ " theo số thí sinh");
		}
		// Khởi chạy tiến trình lấy thông tin cho biểu đồ
		new getInforDiemtong().execute(URL_DIEMTONG + param);
	}

	// Tiến trình con lấy thôn tin biểu đồ
	public class getInforDiemtong extends AsyncTask<String, Void, String> {
		// dialog hiện lên trong quá trình lấy tin
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// Khởi tạo vào show dialog
			dialog = new ProgressDialog(BarActivity.this);
			dialog.setMessage("Đang tải...");
			dialog.setCancelable(false);
			dialog.show();
			//Clear bar
			points = new ArrayList<Bar>();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				// Lấy dữ liệu
				Document document = Jsoup.connect(arg0[0]).timeout(10000).get();
				String res = document.text().trim();
				return res;
			} catch (Exception ex) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			try {
				// Lấy dữ liệu thành công
				if (result != null && !result.equals("Khong co du lieu")) {
					// Tách string theo ký tự |
					String[] info = result.replace('|', '-').split("-");

					// Tạo màu ngẫu nhiên
					
					Random rand = new Random();
					for (String s : info) {
						int r = rand.nextInt(255);
						int g = rand.nextInt(255);
						int b = rand.nextInt(255);

						// Tạo đối tượng cột cho biểu đồ
						Bar bar = new Bar();
						// số người đạt điểm
						int songuoi = Integer.parseInt(s.split(",")[0].trim());
						// Điểm
						String diem = s.split(",")[1].trim();
						int randomColor = Color.rgb(r, g, b);
						// set màu cho cột
						bar.setColor(randomColor);

						bar.setValue(songuoi);
						bar.setName(diem);
						points.add(bar);
					}
					// Vẽ các cột lên biểu đồ
					barGraph.setBars(points);
				} else {
					// Hiện thông báo
					Toast.makeText(BarActivity.this, "Không có dữ liệu",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception ex) {
				Toast.makeText(BarActivity.this, ex.toString(),
						Toast.LENGTH_LONG).show();
				ex.printStackTrace();
			}
		}
	}
}

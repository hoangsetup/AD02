package com.longnd.tracuudiemthi;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class StatisticActivity extends Activity {
	public static String URL_DATA = "http://long.nvttest.com/stile/pages/dulieubieudo.jsp?luachon=";
	private int margins[] = { 50, 50, 50, 50 };

	private int[] arrsosinhvien = new int[1500];
	private float[] arrdiemthi = new float[1500];

	private static String title = "";
	
	LinearLayout layout;

	private XYSeriesRenderer BarRenderer, LineRenderer;	
	private XYMultipleSeriesRenderer multiRenderer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistic);
		layout = (LinearLayout) findViewById(R.id.chart);
		// Khởi tạo hộp thoại lựa chọn tiêu chí vẽ bd ngay lúc khởi tạo
		Intent intent = new Intent(StatisticActivity.this, SubBarActivity.class);
		startActivityForResult(intent, 100);
	}

	// Bắt sự kiện khi hộp thoại lựa chọn bị đóng lại (SubActivity)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// Lấy thông tin từ hộp thoại gửi về
		if (resultCode != 111)// Bị đóng mà không phải nhấn nút xong
		{
			Log.d("CC_trace", "111"); 
			return;
		}
		// Hộp thoại được đóng với thông tin truyền trở lại
		String param = data.getStringExtra("res");
		int index = data.getIntExtra("int", 0);
		if (index == 0) {
			title = "Biểu đồ: Phân bố tổng điểm theo số thí sinh";
		} else {
			title = "Biểu đồ: Phân bố điểm môn "
					+ getResources().getStringArray(R.array.textHienthi)[index]
					+ " theo số thí sinh";
		}
		// Khởi chạy tiến trình lấy thông tin cho biểu đồ
		arrdiemthi = new float[1500];
		arrsosinhvien = new int[1500];
		layout.removeAllViews();
		new drawChartThread().execute(URL_DATA + param);
	}

	public class GraphCombination {

		private Context context;

		public GraphCombination(Context context) {
			this.context = context;
		}

		public void drawChart() {
			XYSeries lowestTempSeries = new XYSeries("");

			int count = 0;
			for (int i : arrsosinhvien) {
				if (i != 0) {
					count++;
				}
			}

			for (int i = 0; i < count; i++) {
				lowestTempSeries.add(i, arrsosinhvien[i]);
			}

			XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

			dataset.addSeries(lowestTempSeries);
			dataset.addSeries(lowestTempSeries);

			setLowestTempBarRenderer();
			setLowestTempLineRenderer();
			setMultiRenderer();

			for (int i = 0; i < count; i++) {
				multiRenderer.addXTextLabel(i, arrdiemthi[i] + "");
			}

			multiRenderer.addSeriesRenderer(BarRenderer);
			multiRenderer.addSeriesRenderer(LineRenderer);

			
			GraphicalView view = ChartFactory.getCombinedXYChartView(context,
					dataset, multiRenderer, new String[] { BarChart.TYPE,
							LineChart.TYPE });
			
			// try {
			// layout = (LinearLayout) findViewById(R.id.chart);
			// layout.removeAllViewsInLayout();
			// } catch (Exception ex) {
			// ex.printStackTrace();
			// }
			layout.removeAllViews();
			layout.addView(view, 0, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));

		}

		private void setLowestTempBarRenderer() {

			BarRenderer = new XYSeriesRenderer();
			BarRenderer.setColor(Color.parseColor("#FF7700"));
			BarRenderer.setFillPoints(true);
			BarRenderer.setLineWidth(1);
			BarRenderer.setChartValuesTextAlign(Align.RIGHT);
			BarRenderer.setChartValuesTextSize(25f);
			BarRenderer.setDisplayChartValues(true);
		}

		private void setMultiRenderer() {

			multiRenderer = new XYMultipleSeriesRenderer();
			multiRenderer.setChartTitle(title);
			multiRenderer.setXTitle("Điểm số");
			multiRenderer.setYTitle("Số sinh viên");

			multiRenderer.setXAxisMin(-1);

			int c = 0, max = arrsosinhvien[0];
			for (int i : arrsosinhvien) {
				if (i != 0) {
					c++;
				}
				max = (i > max) ? i : max;
			}

			multiRenderer.setXAxisMax(c);
			multiRenderer.setYAxisMin(0);
			multiRenderer.setYAxisMax(max + (max / 2));

			multiRenderer.setLabelsTextSize(25f);
			multiRenderer.setLegendTextSize(20f);
			multiRenderer.setAxisTitleTextSize(25f);
			multiRenderer.setMargins(margins);
			multiRenderer.setChartTitleTextSize(30f);

			multiRenderer.setApplyBackgroundColor(true);
			multiRenderer.setBackgroundColor(Color.WHITE);
			multiRenderer.setYLabelsAlign(Align.RIGHT);
			multiRenderer.setBarSpacing(1);
			multiRenderer.setZoomButtonsVisible(false);
			multiRenderer.setPanEnabled(false);

			multiRenderer.setXLabels(0);

		}

		private void setLowestTempLineRenderer() {
			LineRenderer = new XYSeriesRenderer();
			LineRenderer.setColor(Color.parseColor("#0400FF"));
			LineRenderer.setFillPoints(true);
			LineRenderer.setLineWidth(4);
			LineRenderer.setChartValuesTextAlign(Align.CENTER);
			LineRenderer.setChartValuesTextSize(15f);
			LineRenderer.setDisplayChartValues(false);
		}
	}

	public class drawChartThread extends AsyncTask<String, Void, String> {
		// dialog hiện lên trong quá trình lấy tin
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// Khởi tạo vào show dialog
			dialog = new ProgressDialog(StatisticActivity.this);
			dialog.setMessage("Đang tải...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
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

					// Random rand = new Random();
					int i = 0;
					for (String s : info) {
						// số người đạt điểm
						int songuoi = Integer.parseInt(s.split(",")[0].trim());
						// Điểm
						String diem = s.split(",")[1].trim();
						arrdiemthi[i] = Float.parseFloat(diem);
						arrsosinhvien[i] = songuoi;
						i++;
					}

					(new GraphCombination(StatisticActivity.this)).drawChart();
				} else {
					Toast.makeText(StatisticActivity.this, "Không có dữ liệu",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception ex) {
				Toast.makeText(StatisticActivity.this, ex.toString(),
						Toast.LENGTH_LONG).show();
				ex.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.chart_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.action_bar) {
			// Khởi chạy activity BarActivity để tạo biểu đồ
			// Intent intent = new Intent(MainActivity.this, BarActivity.class);
			Intent intent = new Intent(StatisticActivity.this,
					SubBarActivity.class);
			startActivityForResult(intent, 100);
		}
		return super.onOptionsItemSelected(item);
	}
}

package com.longnd.tracuudiemthi;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;

public class BarActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bar);
		
		//Danh sách cột trong biểu đồ
		ArrayList<Bar> points = new ArrayList<Bar>();
		
		//Tạo màu ngẫu nhiên
		Random rand = new Random();
		for(int i = 0; i< 15; i++){
			int r = rand.nextInt(255);
			int g = rand.nextInt(255);
			int b = rand.nextInt(255);
			Bar bar = new Bar();
			int randomColor = Color.rgb(r,g,b);
			bar.setColor(randomColor);
			bar.setValue(rand.nextInt(i+1)*10);
			bar.setName("Name "+i);
			points.add(bar);
		}

		BarGraph barGraph = (BarGraph) findViewById(R.id.graph);
		barGraph.setBars(points);
	}
}

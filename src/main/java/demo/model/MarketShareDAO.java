package demo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MarketShareDAO {

	private static Random rand = new Random();

	public static List<MarketShare> filterYearLocation(int[] years, String[] locations) {
		List<MarketShare> list = new ArrayList<>();
		for (int i = 0, year = years.length; i < year; i++) {
			for (int j = 0, loc = locations.length; j < loc; j++) {
				Number num = (rand.nextInt(10) + 1) * 10000 + (rand.nextInt(10) + 1) * 1000;
				list.add(new MarketShare(years[i], locations[j], num));
			}
		}
		return list;
	}
}

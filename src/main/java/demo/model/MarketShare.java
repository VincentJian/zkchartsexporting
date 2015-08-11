package demo.model;

public class MarketShare {

	private int year;
	private String location;
	private Number share;

	public MarketShare() {
	}

	public MarketShare(int year, String location, Number share) {
		super();
		this.year = year;
		this.location = location;
		this.share = share;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Number getShare() {
		return share;
	}

	public void setShare(Number share) {
		this.share = share;
	}

}

/** 
 * ��Ʊʵ���� 
 * @ 2013.01.02 
 */  
public class StockData {  
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public double getAdjusted_close() {
		return adjusted_close;
	}
	public void setAdjusted_close(double adjusted_close) {
		this.adjusted_close = adjusted_close;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	private String date; // ��������  
    private double open = 0.0; // ���̼�  
    private double high = 0.0; // ��߼�  
    private double low = 0.0; // ��ͼ�  
    private double close = 0.0; // ���һ�ν��׼۸��൱�����̼�  
    private double adjusted_close = 0.0; // ���һ�ν��׼۸� (��������̼۵�����Ȩ�۸�)   
    private double volume = 0.0;// �ܽ�����  
    //setters and getters.....ʡ��  
    //toString method...Ϊ���Է�����ԼӸ�toString������ʡ��  
} 

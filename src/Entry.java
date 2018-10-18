//import java.util.*;
import org.joda.time.DateTime;
//import org.joda.time.Days;

public class Entry {
	
	private String id;
	private DateTime epicDate;
	private DateTime startEBRT;
	private DateTime endEBRT;
	
	Entry () {
	};

	public String getId() { return id;}
	public DateTime getEpic() {return epicDate;}
	public DateTime getStartEBRT() {return startEBRT;}
	public DateTime getEndEBRT() {return endEBRT;}
	
	public void setId(String i) {id = i;}
	public void setEpicDate(DateTime ep ) {epicDate = ep;}
	public void setStartEBRT(DateTime st) {startEBRT = st;}
	public void setEndEBRT(DateTime ed) {endEBRT = ed;}
	
}

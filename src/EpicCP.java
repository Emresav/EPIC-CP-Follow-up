import java.util.*;

public class EpicCP {
	
	private Date epicDate;
	private int followUp;
	
	EpicCP(Date ed, int fu) {
		epicDate = ed;
		followUp = fu;
	};
	
	public Date getEpicDate() {return epicDate;}
	public int getFollowUp() {return followUp;}
	
}

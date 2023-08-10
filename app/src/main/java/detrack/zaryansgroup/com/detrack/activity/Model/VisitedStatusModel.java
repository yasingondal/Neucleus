package detrack.zaryansgroup.com.detrack.activity.Model;

public class VisitedStatusModel {
    int StatusID;
    String VisitStatus;


    public VisitedStatusModel(int statusID, String visitStatus) {
        StatusID = statusID;
        VisitStatus = visitStatus;
    }


    public int getStatusID() {
        return StatusID;
    }

    public void setStatusID(int statusID) {
        StatusID = statusID;
    }

    public String getVisitStatus() {
        return VisitStatus;
    }

    public void setVisitStatus(String visitStatus) {
        VisitStatus = visitStatus;
    }
}




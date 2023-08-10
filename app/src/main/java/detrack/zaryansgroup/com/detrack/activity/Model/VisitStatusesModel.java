package detrack.zaryansgroup.com.detrack.activity.Model;

public class VisitStatusesModel {
    int StatusID;
    String VisitStatus;


    public VisitStatusesModel(int statusID, String visitStatus) {
        StatusID = statusID;
        VisitStatus = visitStatus;
    }

    public VisitStatusesModel() {

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

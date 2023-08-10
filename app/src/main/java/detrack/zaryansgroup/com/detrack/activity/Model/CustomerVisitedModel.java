package detrack.zaryansgroup.com.detrack.activity.Model;

public class CustomerVisitedModel {
    int id;
    String CustomerName;
    String RouteName;
    String VisitStatus;
    String VisitDate;
    String VisitTime;
    String latitude;
    String longitude;
    String imageName;

    int RouteID;
    int CustomerId;
    int SalesmanID;
    int StatusID;
    int CompanyID;
    int CompanySiteID;
    int isSync;



    public CustomerVisitedModel() {
    }

    public CustomerVisitedModel(int id, String customerName, String routeName, String visitStatus, String visitDate, String visitTime, String latitude, String longitude, String imageName, int routeID, int customerId, int salesmanID, int statusID, int companyID, int companySiteID, int isSync) {
        this.id = id;
        CustomerName = customerName;
        RouteName = routeName;
        VisitStatus = visitStatus;
        VisitDate = visitDate;
        VisitTime = visitTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageName = imageName;
        RouteID = routeID;
        CustomerId = customerId;
        SalesmanID = salesmanID;
        StatusID = statusID;
        CompanyID = companyID;
        CompanySiteID = companySiteID;
        this.isSync = isSync;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }

    public String getVisitStatus() {
        return VisitStatus;
    }

    public void setVisitStatus(String visitStatus) {
        VisitStatus = visitStatus;
    }

    public String getVisitDate() {
        return VisitDate;
    }

    public void setVisitDate(String visitDate) {
        VisitDate = visitDate;
    }

    public String getVisitTime() {
        return VisitTime;
    }

    public void setVisitTime(String visitTime) {
        VisitTime = visitTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getRouteID() {
        return RouteID;
    }

    public void setRouteID(int routeID) {
        RouteID = routeID;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public int getSalesmanID() {
        return SalesmanID;
    }

    public void setSalesmanID(int salesmanID) {
        SalesmanID = salesmanID;
    }

    public int getStatusID() {
        return StatusID;
    }

    public void setStatusID(int statusID) {
        StatusID = statusID;
    }

    public int getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(int companyID) {
        CompanyID = companyID;
    }

    public int getCompanySiteID() {
        return CompanySiteID;
    }

    public void setCompanySiteID(int companySiteID) {
        CompanySiteID = companySiteID;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }
}

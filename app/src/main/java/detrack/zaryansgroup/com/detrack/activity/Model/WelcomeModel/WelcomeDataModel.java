package detrack.zaryansgroup.com.detrack.activity.Model.WelcomeModel;


public class WelcomeDataModel {

    private int NumberOfAssignOrder;
    private int NumberOfBookedUnSyncOrder;
    private int NumberOfReceipt;
    private int NumberOfDeliveredUnSynecOrder;
    private int NumberOfReturnedOrders;
    private int NumberOfSyncOrder;
    private int NumberOfVisitRecords;

    public WelcomeDataModel(int numberOfAssignOrder, int numberOfBookedUnSyncOrder, int numberOfReceipt,
                            int numberOfDeliveredUnSynecOrder, int numberOfReturnedOrders, int numberOfSyncOrder, int numberOfVisitRecords) {
        NumberOfAssignOrder = numberOfAssignOrder;
        NumberOfBookedUnSyncOrder = numberOfBookedUnSyncOrder;
        NumberOfReceipt = numberOfReceipt;
        NumberOfDeliveredUnSynecOrder = numberOfDeliveredUnSynecOrder;
        NumberOfReturnedOrders = numberOfReturnedOrders;
        NumberOfSyncOrder = numberOfSyncOrder;
        NumberOfVisitRecords = numberOfVisitRecords;
    }


    public int getNumberOfVisitRecords() {
        return NumberOfVisitRecords;
    }

    public void setNumberOfVisitRecords(int numberOfVisitRecords) {
        NumberOfVisitRecords = numberOfVisitRecords;
    }

    public int getNumberOfAssignOrder() {
        return NumberOfAssignOrder;
    }

    public void setNumberOfAssignOrder(int numberOfAssignOrder) {
        NumberOfAssignOrder = numberOfAssignOrder;
    }

    public int getNumberOfBookedUnSyncOrder() {
        return NumberOfBookedUnSyncOrder;
    }

    public void setNumberOfBookedUnSyncOrder(int numberOfBookedUnSyncOrder) {
        NumberOfBookedUnSyncOrder = numberOfBookedUnSyncOrder;
    }

    public int getNumberOfReceipt() {
        return NumberOfReceipt;
    }

    public void setNumberOfReceipt(int numberOfReceipt) {
        NumberOfReceipt = numberOfReceipt;
    }

    public int getNumberOfDeliveredUnSynecOrder() {
        return NumberOfDeliveredUnSynecOrder;
    }

    public void setNumberOfDeliveredUnSynecOrder(int numberOfDeliveredUnSynecOrder) {
        NumberOfDeliveredUnSynecOrder = numberOfDeliveredUnSynecOrder;
    }

    public int getNumberOfReturnedOrders() {
        return NumberOfReturnedOrders;
    }

    public void setNumberOfReturnedOrders(int numberOfReturnedOrders) {
        NumberOfReturnedOrders = numberOfReturnedOrders;
    }

    public int getNumberOfSyncOrder() {
        return NumberOfSyncOrder;
    }

    public void setNumberOfSyncOrder(int numberOfSyncOrder) {
        NumberOfSyncOrder = numberOfSyncOrder;
    }
}

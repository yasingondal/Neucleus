package detrack.zaryansgroup.com.detrack.activity.SQLlite;

public class DBHelper {

     //                                 Table Info



    //TableCustomerPrice
    public static final String TBL_Customer_Price = "tblCustomerPrice";
    public static final String CustomerPrice_CustomerId = "CustomerId";
    public static final String CustomerPrice_ItemId = "ItemId";
    public static final String CustomerPrice_Price = "Price";


    //Table Visit Statuses
    public static final String TBL_Visited_Statuses = "tblVisitStatuses";
    public static final String Status_ID = "StatusID";
    public static final String Visit_Status = "VisitStatus";



    //TableVisited
    public static final String TBL_VisitedDetails = "tblVisitedDetails";
    public static final String vID = "visitId";
    public static final String vVisitDate = "visitedDate";
    public static final String vVisitTime = "visitedTime";
    public static final String vRouteID = "routeId";
    public static final String vCustomerId = "customerId";
    public static final String vSalesmanID = "salesmanId";
    public static final String vCompanyID = "companyId";
    public static final String vCompanySiteID = "companySiteId";
    public static final String vStatus = "visitedStatus";
    public static final String vIsSync = "IsSync";
    public static final String vRouteName = "routeName";
    public static final String vCustomerName = "CustomerName";
    public static final String vStatusName = "visitStatus";
    public static final String vLatitude = "latitude";
    public static final String vLongitude = "longitude";
    public static final String vImageName = "vImageName";



    // tbl for discount Policies
    public static final String TBL_DISCOUNT_POLICY = "tblDiscountPolicy";
    public static final String DISCOUNT_POLICY_ID = "DiscountPolicyId";
    public static final String DIS_TYPE_ID = "TypeId";
    public static final String DIS_TYPE = "Type";
    public static final String DISCOUNT_TYPE = "DiscountType";
    public static final String DIS_TARGET_VALUE = "TargetValue";
    public static final String DIS_TARGET_QTY = "TargetQty";
    public static final String DIS_DISCOUNT_PERCENTAGE = "DiscountPercentage";
    public static final String DIS_DISCOUNT_VALUE = "DiscountValue";
    public static final String DIS_ADDITIONAL_DISCOUNT_PERCENTAGE = "AdditionalDiscountPercentage";
    public static final String DIS_ADDITIONAL_DISCOUNT_VALUE = "AdditionalDiscountValue";
    public static final String DIS_STOP_OTHER_DISCOUNT = "StopOtherDiscount";
    public static final String DIS_IS_CLAIMABLE = "IsClaimable";
    public static final String DIS_START_DATE = "StartDate";
    public static final String DIS_END_DATE = "EndDate";
    public static final String DIS_GROUP_DIV_ID = "GroupDivId";
    public static final String DIS_DIV_ID = "DivId";
    public static final String DIS_ITEM_ID = "ItemId";


    // tbl for Bonus Policies BONUS

    public static final String TBL_BONUS_POLICY = "tblBonusPolicy";
    public static final String BONS_POLICY_ID = "BonusPolicesId";
    public static final String BONS_TYPE_ID = "TypeId";
    public static final String BONS_ITEM_ID = "ItemId";
    public static final String BONS_BONUS = "Bonus";
    public static final String BONS_TYPE = "Type";
    public static final String BONS_DISCOUNT_TYPE = "DiscountType";
    public static final String BONS_OUR_SHARE = "OurShare";
    public static final String BONS_TARGET_QTY = "TargetQty";
    public static final String BONS_INCENTIVE_ID = "IncentiveItemId";
    public static final String BONS_IS_CLAIMABLE = "IsClaimable";
    public static final String BONS_START_DATE = "StartDate";
    public static final String BONS_END_DATE = "EndDate";
    public static final String BONS_SUB_TYPE = "SubType";
    public static final String BONS_MULTI_ITEMS_NAMES = "MultiItemsName";
    public static final String BONS_MULTI_ITEMS_IDS = "MultiItemsIdes";


   //Table tblRoute
    public static final String TBL_Route = "tblRoute";
    public static final String ROUTE_ID = "RouteId";
    public static final String ROUTE_TITLE = "Title";
    public static final String ROUTE_CODE = "Code";
    public static final String ROUTE_COMPANY_ID = "CompanyID";
    public static final String ROUTE_COMPANY_SITE_ID = "CompanySiteID";
    public static final String ROUTE_DESCRIPTION = "Descript";



   //Table tblplnItem
    public static final String TBL_PLN_ITEMS = "tblplnItem";
    public static final String ITEM_ID = "Id";
    public static final String ITEM_TITLE = "Title";
    public static final String ITEM_CODE = "Code";
    public static final String ITEM_NAME = "Name";
    public static final String ITEM_IMAGE_NAME = "ImageName";
    public static final String ITEM_DETAIL = "ItemDetail";
    public static final String ITEM_PRICE_CORTON = "CostCtnPrice";
    public static final String ITEM_PRICE_PACK = "CostPackPrice";
    public static final String ITEM_PRICE_PIECES = "CostPiecePrice";
    public static final String ITEM_PRICE_WS_CORTON = "WSCtnPrice";
    public static final String ITEM_PRICE_WS_PACK = "WSPackPrice";
    public static final String ITEM_PRICE_WS_PIECES = "WSPiecePrice";
    public static final String ITEM_PRICE_RETAIL_CORTON = "RetailCtnPrice";
    public static final String ITEM_PRICE_RETAIL_PACK = "RetailPackPrice";
    public static final String ITEM_PRICE_RETAIL_PIECES = "RetailPiecePrice";
    public static final String ITEM_PRICE_DISPLAY = "DisplayPrice";
    public static final String ITEM_CTN_SIZE = "CtnSize";
    public static final String ITEM_TAX_CODE = "Taxcode";
    public static final String ITEM_GST_PER = "Gstper";
    public static final String ITEM_GST_VALUE = "Gstvalue";
    public static final String ITEM_PACK_SIZE = "PackSize";
    public static final String ITEM_EMPTY_BOTTLE = "EmptyBottle";
    public static final String ITEM_SKU = "ItemSku";


    public static final String ITEM_DIVISION_ID = "DivisionId";
    public static final String ITEM_PRICE_ID = "PriceId";
    public static final String ITEM_PIECE_SIZE = "PieceSize";
    public static final String ITEM_UNIT = "Unit";
    public static final String ITEM_ISST_PERSENT = "IsSTPercent";
    public static final String ITEM_ISST_VALUE = "IsSTValue";
    public static final String ITEM_AST_PERCENTAGE = "ASTPercentage";
    public static final String ITEM_AST_VALUE = "ASTValue";
    public static final String ITEM_IS_AST_PERCENT = "IsASTPercent";
    public static final String ITEM_UNIT_SIZE = "UnitSize";
    public static final String ITEM_SIZE = "Size";
    public static final String ITEM_BRAND_ID = "BrandId";




    //Table Customer
    public static final String TBL_REGISTER_CUSTOMER = "tblPlnCustomer";
    public static final String CUSTOMER_ID = "Id";
    public static final String CUSTOMER_TYPE_ID = "CustomerTypeId";
    public static final String CUSTOMER_NAME = "Name";
    public static final String CUSTOMER_SALES_MODE = "SalesMode";
    public static final String CUSTOMER_ImageName = "ImageName";
    public static final String C_ADDRESS = "Address";
    public static final String C_ADDRESS1 = "Address1";
    public static final String C_PHONE = "Phone";
    public static final String C_CELL = "Cell";
    public static final String C_City = "City";
    public static final String C_Country = "Country";
    public static final String C_LATITUDE = "Latitude";
    public static final String C_LONGITUDE = "Longitude";
    public static final String C_ISSAVE = "ISsave";   // for syncing purpose
    public static final String C_ISNEW = "ISNew";   // for new and old customer
    public static final String C_ROUTE_ID = "RouteID";
    public static final String c_CODE = "Code";//customer Code




    // Images Table
    public static final String TBL_IMAGES = "tblImages";
    public static final String IMG_ID = "_Id";
    public static final String IMG_NAME = "Name";
    public static final String IMG_TAG = "Tag";
    public static final String IMG_ORDER_ID = "OrderId";
    public static final String IMG_IS_SYNCED = "IsSynced";




    //Table tblPlnOrdConfirmMaster
    public static final String TBL_ORDER_CONFIRM_MASTER = "tblPlnOrdConfirmMaster";
    public static final String ORDER_CONFIRM_MASTER_ID = "OrderId";
    public static final String ORDER_CONFIRM_MASTER_SERVER_ID = "Server_OrderId";
    public static final String ORDER_CONFIRM_MASTER_From_Server = "FromServer";
    public static final String ORDER_EMPLOYEEID = "EmployeeId";
    public static final String ORDER_CUSTOMERID = "Customer_id";
    public static final String ORDER_VEHICLEID = "VehicleId";
    public static final String ORDER_NO = "OrderNo";
    public static final String ORDER_SERIAL_NO = "serialNo";
    public static final String ORDER_DATE_TIME = "OrderDateTime";
    public static final String ORDER_SALES_MODE = "SalesModel";
    public static final String ORDER_CASH_DEPOSITED_BANK_ID = "CashDepositedBankId";
    public static final String URGENT_ORDER_STATUS = "UrgentOrderStatus";
    public static final String ORDER_STATUS = "Status";
    public static final String ORDER_DESCRIPTION = "Description";
    public static final String ORDER_TOTAL_QTY = "TotalQuantity";
    public static final String ORDER_TOTAL_AMOUNT = "TotalAmount";
    public static final String ORDER_GROSS_AMOUNT = "TotalGrossAmount";
    public static final String ORDER_DISCOUNT = "Discount";
    public static final String ORDER_DISCOUNT_IN_PERCENTAGE = "DiscountInPercentage";
    public static final String ORDER_NetTotal = "NetTotal";
    public static final String ORDER_DELIVERY_DATE = "Delivery_date";
    public static final String ORDER_START_TIME = "Start_time";
    public static final String ORDER_END_TIME = "End_time";
    public static final String ORDER_DELIVERY_ADDRESS = "Delivery_Address";
    public static final String ORDER_REFUSED_REASON = "RefusedReason";
    public static final String ORDER_CANCELLED_REASON = "CancelledReason";
    public static final String ORDER_REJECTED_REASON = "RejectedReason";
    public static final String DELIVERY_TO_MOBILE = "Deliver_to_Mobile";
    public static final String ORDER_BY = "Orderby";
    public static final String ORDER_RECEIVED_BY = "Receivedby";
    public static final String ORDER_PODLAT = "PODLatitude";
    public static final String ORDER_PODLNG = "PODLongitude";
    public static final String ORDER_POBLAT = "POBLatitude";
    public static final String ORDER_POBLNG = "POBLongitude";
    public static final String ORDER_ROUTE_ID = "RouteId";
    public static final String ORDER_IS_REJECT = "IsReject";
    public static final String ORDER_CATEGORY_ID = "CategoryId";
    public static final String ORDER_NOTE = "Note";
    public static final String ORDER_LATITUDE = "latitude";
    public static final String ORDER_LONGITUDE = "longitude";
    public static final String ORDER_ISSAVE = "ISsave";   // for syncing purpose
    public static final String ORDER__ISNEW = "ISNew";   // for Taking new order purpose
    public static final String ORDER__ISREAD = "ISREAD";
    public static final String ORDER__NEW_UPDATE = "NewUpdate";

    public static final String ORDER_DISTRIBUTOR_ID = "DistributorId";
    public static final String ORDER_SUB_DISTRIBUTOR_ID = "SubDistributorId";



    //Table tblPlnOrdConfirmChild
    public static final String TBL_ORDER_CONFIRM_CHILD = "tblPlnOrdConfirmChild";
    public static final String ORDER_CONFIRM_CHILD_DETAIL_ID = "OrderDetailId";
    public static final String ORDER_CONFIRM_MASTER_DETAIL_ID = "MasterDetailId";
    public static final String ORDER_CONFIRM_CHILD_SERVER_DETAIL_ID = "Server_OrderDetailId";
    public static final String ITEM_DELIVERY_ID = "OrderId";
    public static final String ORDER_CONFIRM_CHILD_ITEM_ID = "ItemId";
    public static final String ORDER_CONFIRM_CHILD_ITEM_TAXCODE= "taxcode";
    public static final String ORDER_CONFIRM_CHILD_ITEM_NAME = "Item_name";
    public static final String ORDER_CONFIRM_CHILD_COST_CTN_PRICE = "CostCtnPrice";
    public static final String ORDER_CONFIRM_CHILD_WHOLESALE_CTN_PRICE = "WholesaleCtnPrice";
    public static final String ORDER_CONFIRM_CHILD_RETAIL_CTN_PRICE = "RetailCtnPrice";
    public static final String ORDER_CONFIRM_CHILD_COST_PACK_PRICE = "CostPackPrice";
    public static final String ORDER_CONFIRM_CHILD_WHOLESALE_PACK_PRICE = "WholesalePackPrice";
    public static final String ORDER_CONFIRM_CHILD_RETAIL_PACK_PRICE = "RetailPackPrice";
    public static final String ORDER_CONFIRM_CHILD_COST_PIECES_PRICE = "CostPiecesPrice";
    public static final String ORDER_CONFIRM_CHILD_WHOLESALE_PIECES_PRICE = "WholesalePiecesPrice";
    public static final String ORDER_CONFIRM_CHILD_RETAIL_PIECES_PRICE = "RetailPiecesPrice";
    public static final String ORDER_CONFIRM_CHILD_CTN_QUANTITY = "CtnQuantity";
    public static final String ORDER_CONFIRM_CHILD_PACK_QUANTITY = "PackQuantity";
    public static final String ORDER_CONFIRM_CHILD_PCS_QUANTITY = "PcsQuantity";
    public static final String ORDER_CONFIRM_CHILD_REJECT_CTN_QUANTITY = "RejectCtnQuantity";
    public static final String ORDER_CONFIRM_CHILD_REJECT_PACK_QUANTITY = "RejectPackQuantity";
    public static final String ORDER_CONFIRM_CHILD_REJECT_PCS_QUANTITY = "RejectPcsQuantity";
    public static final String ORDER_CONFIRM_CHILD_DELIVER_CTN_QUANTITY = "DeliverCtnQuantity";
    public static final String ORDER_CONFIRM_CHILD_DELIVER_PACK_QUANTITY = "DeliverPackQuantity";
    public static final String ORDER_CONFIRM_CHILD_DELIVER_PCS_QUANTITY = "DeliverPcsQuantity";
    public static final String ORDER_CONFIRM_CHILD_FOCTYPE = "FOCType";
    public static final String ORDER_CONFIRM_CHILD_FOCQTY = "FOCQty";
    public static final String ORDER_CONFIRM_CHILD_FOC_VALUE = "FOCValue";
    public static final String ORDER_CONFIRM_CHILD_FOC_PERCENTAGE = "FOCPercentage";
    public static final String ORDER_CONFIRM_CHILD_TOTAL_QUANTITY = "TotalQuantity";
    public static final String ORDER_CONFIRM_CHILD_TOTAL_COST = "TotalCost";
    public static final String ORDER_CONFIRM_CHILD_TOTAL_WHOLESALE = "TotalWholesale";
    public static final String ORDER_CONFIRM_CHILD_TOTAL_RETAIL = "TotalRetail";
    public static final String ORDER_CONFIRM_CHILD_NET_TOTAL_RETAIL = "NetTotalRetail";
    public static final String ORDER_CONFIRM_CHILD_REJECTED_QTY = "RejectedQty";
    public static final String ORDER_CONFIRM_CHILD_DELIVERED_QTY = "DeliveredQty";
    public static final String ORDER_CONFIRM_CHILD_ROUTE_ID = "RouteId";
    public static final String ORDER_CONFIRM_CHILD_CATEGORY_ID = "CategoryId";
    public static final String ITEM_ACTUAL_DELIVERD_Quantity = "Item_Actual_Deliverd_Quantity";  // for Updatting  delivered quantity purpsoe
    public static final String ITEM_RETURN_Quantity = "Item_Return_Quantity";
    public static final String ITEM_REJECT_RESSON = "Item_Reject_Resson";
    public static final String ORDER_CONFIRM_CHILD_DISPLAY_PRICE = "DisplayPrice";
    public static final String ORDER_CONFIRM_CHILD_GST_VALUE = "GstValue";
    public static final String ORDER_CONFIRM_CHILD_GST_PER = "GstPercentage";
    public static final String ORDER_CONFIRM_CHILD_ITEM_ISSAVE = "Item_ISsave";  // for syncing purpose
    public static final String ORDER_CONFIRM_CHILD_ITEM_ISNEW = "ISNew";   // for Taking new order purpose
    public static final String ORDER_CONFIRM_CHILD_ITEM_EMPTY_BOTTLE = "emptyBottle";
    public static final String ORDER_CONFIRM_CHILD_DISCOUNT = "Discount";


    //Columns for DiscountPolicy /Bonus Policy
    public static final String ORDER_CONFIRM_CHILD_DISCOUNT_POLICY_ID = "DiscountPolicyId";
    public static final String ORDER_CONFIRM_CHILD_DISCOUNT_PERCENTAGE = "DiscountPercentage";
    public static final String ORDER_CONFIRM_CHILD_DISCOUNT_POLICY_AMNT = "DiscountPolicyAmnt";
    public static final String ORDER_CONFIRM_CHILD_BONUS_POLICY_ID = "BonusPolicyId";
    public static final String ORDER_CONFIRM_CHILD_BONUS_QTY = "BonusQty";
    public static final String ORDER_CONFIRM_CHILD_BONUS_GST = "BonusGST";
    public static final String ORDER_CONFIRM_CHILD_BONUS_ITEM_ID = "BonusItemId";
    public static final String ORDER_CONFIRM_CHILD_BONUS_ITEM_NAME = "BonusItemName";


    //Distributors table
    public static final String TBL_DISTRIBUTORS = "tblDistributors";
    public static final String D_CONTACT_ID = "ContactId";
    public static final String D_TITLE = "Title";
    public static final String D_NAME = "Name";
    public static final String D_FATHER_NAME = "FatherName";
    public static final String D_ADDRESS = "Address";
    public static final String D_CITY = "City";
    public static final String D_COUNTRY = "Country";
    public static final String D_DELIVERY_ADDRESS = "DeliveryAddress";
    public static final String D_CONTACT_GROUP = "ContactGroup";
    public static final String D_PHONE = "Phone";
    public static final String D_PHONE2 = "Phone2";
    public static final String D_CELL = "Cell";
    public static final String D_FAX = "Fax";
    public static final String D_EMAIL = "Email";
    public static final String D_AREA_ID = "AreaId";
    public static final String D_DIVISION_ID = "DivisionId";
    public static final String D_ZONE_ID = "ZoneId";
    public static final String D_CREDIT_LIMIT = "CreditLimit";
    public static final String D_PERCENTAGEDISC1 = "PercentageDisc1";
    public static final String D_PERCENTAGEDISC2 = "PercentageDisc2";
    public static final String D_PERCENTAGEDISC3 = "PercentageDisc3";
    public static final String D_LONGITUDE = "Longitude";
    public static final String D_LATITUDE = "Latitude";
    public static final String D_ADDRESS1 = "Address1";
    public static final String D_ADDRESS2 = "Address2";
    public static final String D_ROUTE_ID = "RouteId";
    public static final String D_COMPANY_SITE_ID = "CompanySiteID";
    public static final String D_COMPANY_ID = "CompanyID";
    public static final String D_CONTACT_CODE = "ContactCode";
    public static final String D_CONTACT_TYPE = "ContactType";
    public static final String D_CONTACT_TYPE_ID = "ContactTypeId";
    public static final String D_SALES_MODE = "SalesMode";
    public static final String D_IMAGE_NAME = "ImageName";
    public static final String D_DISTRIBUTOR_ID = "DistributerId";
    public static final String D_IS_DISTRIBUTOR = "IsDistributor";





    // Table tblCustomerReceipts
    public static final String TBL_CUSTOMER_RECEIPTS = "tblCustomerReceipts";
    public static final String RECEIPT_ID = "ReceiptID";
    public static final String RECEIPT_SERVER_ID = "ServerReceiptID";
    public static final String RECEIPT_CUSTOMER_ID = "ReceiptCustomerID";
    public static final String RECEIPT_CUSTOMER_Name = "ReceiptCustomerName";
    public static final String RECEIPT_AMOUNT_PAID = "ReceiptAmountPaid";

    //Cash Deposited Bank Id
    public static final String RECEIPT_CASH_DEPOSITED_BANK_ID = "ReceiptCashDepositedBankId";
    public static final String RECEIPT_CASH_DEPOSITED_BANK_NAME = "ReceiptCashDepositedBankName";

    public static final String RECEIPT_BALANCE = "ReceiptBalance";
    public static final String RECEIPT_PREVIOUS_BALANCE = "ReceiptPreviousBalance";
    public static final String RECEIPT_REMARKS = "ReceiptRemarks";
    public static final String RECEIPT_DATE = "ReceiptDate";
    public static final String RECEIPT_IS_SYNC = "ReceiptStatus";


    //For Table TblCompanyWiseBankDetails by MY on 8 sep 2021
    public static final String TBL_COMPANY_WISE_BANKS = "TblCompanyWiseBankDetails";
    public static final String BANK_ID = "BankId";
    public static final String BANK_NAME = "BankName";
    public static final String BANK_ACCOUNT_NBR = "BankAccountNbr";
    public static final String BANK_ACCOUNT_TYPE = "BankAccountType";
    public static final String BANK_ADDRESS = "BankAddress";


    //                                Create Table


    //For Table TblCompanyWiseBankDetails
    public static final String CREATE_TABLE_TBL_COMPANY_WISE_BANKS = "CREATE TABLE IF NOT EXISTS " + TBL_COMPANY_WISE_BANKS + " ( " +
            " " + BANK_ID + " INTEGER , " + BANK_NAME + " TEXT , " + BANK_ACCOUNT_NBR + " TEXT , " + BANK_ACCOUNT_TYPE + " TEXT , " +
            " " + BANK_ADDRESS + " TEXT );";



    //  Table CustomerPrice
    public static final String CREATE_TABLE_TBL_CUSTOMER_PRICE = "CREATE TABLE IF NOT EXISTS " + TBL_Customer_Price + " ( " +
            " " + CustomerPrice_CustomerId + " INTEGER , " + CustomerPrice_ItemId + " INTEGER , " + CustomerPrice_Price + " TEXT );";



    //  Table Visited Statuses
    public static final String CREATE_TABLE_TBL_VISIST_STATUSES = "CREATE TABLE IF NOT EXISTS " + TBL_Visited_Statuses + " ( " +
            " " + Status_ID + " INTEGER , " + Visit_Status + " TEXT );";


 //  Table Visited
 public static final String CREATE_TABLE_TBL_VISITED_DETAILS = "CREATE TABLE IF NOT EXISTS " + TBL_VisitedDetails + " ( " +
         " " + vID + " INTEGER , " + vRouteID + " INTEGER , " + vCustomerId + " INTEGER , " + vCompanyID + " INTEGER , " + vCompanySiteID + " INTEGER , " +
         " " + vVisitDate + " TEXT , " + vVisitTime + " TEXT , " + vCustomerName + " TEXT" +
         ", " + vStatusName + " TEXT , " + vRouteName + " TEXT , " + vLatitude + " TEXT , " + vLongitude + " TEXT, " + vImageName + " TEXT, " + vSalesmanID + " INTEGER," +
         " " + vStatus + " INTEGER, " + vIsSync + " INTEGER );";




 //table Discount Policies
 public static final String CREATE_TABLE_TBL_DISCOUNT_POLICIES = "CREATE TABLE IF NOT EXISTS " + TBL_DISCOUNT_POLICY + " ( " +
            " " + DISCOUNT_POLICY_ID + " TEXT , " + DIS_TYPE_ID + " TEXT , " + DIS_TYPE + " TEXT , " + DISCOUNT_TYPE + " TEXT , " + DIS_TARGET_VALUE + " TEXT , " + DIS_TARGET_QTY + " TEXT , " +
            " " + DIS_DISCOUNT_PERCENTAGE + " TEXT , " + DIS_DISCOUNT_VALUE + " TEXT , " + DIS_ADDITIONAL_DISCOUNT_PERCENTAGE + " TEXT" +
            ", " + DIS_ADDITIONAL_DISCOUNT_VALUE + " TEXT , " + DIS_STOP_OTHER_DISCOUNT + " TEXT , " + DIS_IS_CLAIMABLE + " TEXT , " + DIS_START_DATE + " TEXT, " + DIS_END_DATE + " TEXT, " + DIS_GROUP_DIV_ID + " TEXT," +
            " " + DIS_DIV_ID + " TEXT, " + DIS_ITEM_ID + " TEXT );";


    //table Bonus Policies
    public static final String CREATE_TABLE_TBL_BONUS_POLICIES = "CREATE TABLE IF NOT EXISTS " + TBL_BONUS_POLICY + " ( " +
            " " + BONS_POLICY_ID + " TEXT , " + BONS_TYPE_ID + " TEXT , " + BONS_ITEM_ID + " TEXT , " + BONS_BONUS + " TEXT , " + BONS_TYPE + " TEXT , " + BONS_DISCOUNT_TYPE + " TEXT , " +
            " " + BONS_OUR_SHARE + " TEXT , " + BONS_TARGET_QTY + " TEXT , " + BONS_INCENTIVE_ID + " TEXT" +
            ", " + BONS_IS_CLAIMABLE + " TEXT , " + BONS_START_DATE + " TEXT , " + BONS_END_DATE + " TEXT , " + BONS_SUB_TYPE + " TEXT, " + BONS_MULTI_ITEMS_NAMES + " TEXT, " + BONS_MULTI_ITEMS_IDS + " TEXT );";




    //  Table RouteModel
    public static final String CREATE_TABLE_TBL_ROUTE = "CREATE TABLE IF NOT EXISTS " + TBL_Route + " ( " +
            " " + ROUTE_ID + " INTEGER , " + ROUTE_TITLE + " TEXT , " + ROUTE_CODE + " TEXT , " + ROUTE_COMPANY_ID + " TEXT , " +
            " " + ROUTE_DESCRIPTION + " TEXT , " + ROUTE_COMPANY_SITE_ID + " TEXT );";




    ///Table Item
    public static final String CREATE_TABLE_TBL_PLN_ITEM = "CREATE TABLE IF NOT EXISTS " + TBL_PLN_ITEMS + " ( " +
            " " + ITEM_ID + " TEXT , " + ITEM_PRICE_ID + " TEXT , " + ITEM_PIECE_SIZE + " TEXT , " + ITEM_UNIT + " TEXT , " + ITEM_ISST_PERSENT + " TEXT , " + ITEM_ISST_VALUE + " TEXT ," + ITEM_AST_PERCENTAGE + " TEXT ," + ITEM_AST_VALUE + " TEXT ," +
            " " + ITEM_IS_AST_PERCENT + " TEXT , " + ITEM_UNIT_SIZE + " TEXT , " + ITEM_SIZE + " TEXT , " + ITEM_BRAND_ID + " TEXT , " +
            " " + ITEM_DIVISION_ID + " INTEGER , " + ITEM_TITLE + " TEXT , " + ITEM_CODE + " TEXT , " + ITEM_NAME + " TEXT , " + ITEM_IMAGE_NAME + " TEXT , " + ITEM_DETAIL + " TEXT ," + ITEM_EMPTY_BOTTLE + " TEXT ," + ITEM_TAX_CODE + " TEXT ," +
            " " + ITEM_PRICE_CORTON + " REAL, " + ITEM_PRICE_PACK + " REAL, " + ITEM_GST_PER + " TEXT, " + ITEM_GST_VALUE+ " TEXT, " +
            " " + ITEM_PRICE_PIECES + " REAL , " + ITEM_PRICE_WS_CORTON + " REAL , " + ITEM_PRICE_WS_PACK + " REAL , " + ITEM_PRICE_WS_PIECES + " REAL , " +  ITEM_SKU + " TEXT , " +
            " " + ITEM_PRICE_RETAIL_CORTON + " REAL , " + ITEM_PRICE_RETAIL_PACK + " REAL , " + ITEM_PRICE_RETAIL_PIECES + " REAL , " + ITEM_PRICE_DISPLAY + " REAL , " + ITEM_CTN_SIZE + " TEXT , " + ITEM_PACK_SIZE + " TEXT );";


    ///Table Distributors
    public static final String CREATE_TABLE_DISTRIBUTORS = "CREATE TABLE IF NOT EXISTS " + TBL_DISTRIBUTORS + " ( " +
            " " + D_CONTACT_ID + " TEXT , " + D_TITLE + " TEXT , " + D_NAME + " TEXT , " + D_FATHER_NAME + " TEXT , " + D_ADDRESS + " TEXT , " + D_CITY + " TEXT ," + D_COUNTRY + " TEXT ," + D_DELIVERY_ADDRESS + " TEXT ," +
            " " + D_CONTACT_GROUP + " TEXT , " + D_PHONE + " TEXT , " + D_PHONE2 + " TEXT , " + D_CELL + " TEXT , " +
            " " + D_FAX + " INTEGER , " + D_EMAIL + " TEXT , " + D_AREA_ID + " TEXT , " + D_DIVISION_ID + " TEXT , " + D_ZONE_ID + " TEXT , " + D_CREDIT_LIMIT + " TEXT ," + D_PERCENTAGEDISC1 + " TEXT ," + D_PERCENTAGEDISC2 + " TEXT ," +
            " " + D_PERCENTAGEDISC3 + " REAL, " + D_LONGITUDE + " REAL, " + D_LATITUDE + " TEXT, " + D_ADDRESS1+ " TEXT, " +
            " " + D_ADDRESS2 + " REAL , " + D_ROUTE_ID + " REAL , " + D_COMPANY_SITE_ID + " REAL , " + D_COMPANY_ID + " REAL , " +
            " " + D_CONTACT_CODE + " REAL , " + D_CONTACT_TYPE + " REAL , " + D_CONTACT_TYPE_ID + " REAL , " + D_SALES_MODE + " REAL , " + D_IMAGE_NAME + " TEXT ," + D_IS_DISTRIBUTOR + " TEXT , " + D_DISTRIBUTOR_ID + " TEXT );";




    // Table Customer
    public static final String CREATE_REGISTER_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_REGISTER_CUSTOMER + "(" +
            " " + CUSTOMER_ID + " INTEGER, " + CUSTOMER_TYPE_ID + " INTEGER, " + CUSTOMER_NAME + " TEXT," + CUSTOMER_SALES_MODE + " TEXT," + CUSTOMER_ImageName + " TEXT, " + C_ADDRESS + " TEXT, " +
            " " + C_ADDRESS1 + " TEXT, " + C_PHONE + " TEXT, " + C_CELL + " TEXT, " + C_City + " TEXT," +
            " " + C_Country + " TEXT, " + C_LATITUDE + " TEXT, " + C_LONGITUDE + " TEXT, " + C_ISSAVE + " TEXT, " +
            " " + C_ISNEW + " TEXT, " + c_CODE + " TEXT, " + C_ROUTE_ID + " INTEGER);";
    // Image Table Creation
    public static final String CREATE_IMAGE_TABLE = "CREATE TABLE " + TBL_IMAGES + "(" + IMG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + IMG_NAME + " VARCHAR(100), " + IMG_TAG + " VARCHAR(50), " + IMG_ORDER_ID + " INTEGER, " + IMG_IS_SYNCED + " TINYINT)";




    // Actual Table tblPlnOrdConfirmMaster    // Assignment work By M.Y ( I have updated table with an extra Column CashDepositedBankID
    public static final String CREATE_ORDER_DELIVERY_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_ORDER_CONFIRM_MASTER + "(" +
            " " + ORDER_CONFIRM_MASTER_ID + " INTEGER , " + ORDER_CONFIRM_MASTER_SERVER_ID + " INTEGER, " + ORDER_EMPLOYEEID + " INTEGER, " +
            " " + ORDER_CUSTOMERID + " INTEGER, " + ORDER_VEHICLEID + " INTEGER, " + ORDER_NO + " TEXT, " + ORDER_SERIAL_NO + " TEXT, " + ORDER_DATE_TIME + " TEXT , " + ORDER_SALES_MODE + " TEXT , " + URGENT_ORDER_STATUS + " TEXT , " + ORDER_CASH_DEPOSITED_BANK_ID + " INTEGER ," +
            " " + ORDER_STATUS + " TEXT, " + ORDER_DESCRIPTION + " TEXT, " + ORDER_TOTAL_QTY + " INTEGER," + ORDER_CONFIRM_MASTER_From_Server + " TEXT," +
            " " + ORDER_DELIVERY_DATE + " TEXT, " + ORDER_START_TIME + " TEXT, " + ORDER_END_TIME + " TEXT, " + ORDER_DELIVERY_ADDRESS + " TEXT," +
            " " + ORDER_REFUSED_REASON + " TEXT ," + ORDER_CANCELLED_REASON + " TEXT ," + ORDER_REJECTED_REASON + " TEXT , " +
            " " + DELIVERY_TO_MOBILE + " TEXT, " + ORDER_BY + " TEXT, " + ORDER_ROUTE_ID + " TEXT , " + ORDER_IS_REJECT + " TEXT," +
            " " + ORDER_CATEGORY_ID + " TEXT , " + ORDER_RECEIVED_BY + " TEXT, " +
            " " + ORDER_PODLAT + " TEXT, " + ORDER_PODLNG + " TEXT, " + ORDER_POBLAT + " TEXT, " + ORDER_POBLNG + " TEXT, " + ORDER_NOTE + " TEXT, " +
            " " + ORDER_LATITUDE + " TEXT, " + ORDER_LONGITUDE + " TEXT," + ORDER_ISSAVE + " TEXT," + ORDER__ISNEW + " TEXT," + ORDER__ISREAD + " INTEGER," + ORDER_DISTRIBUTOR_ID + " INTEGER," + ORDER_SUB_DISTRIBUTOR_ID + " INTEGER," +
            " " + ORDER__NEW_UPDATE + " INTEGER," + ORDER_TOTAL_AMOUNT + " TEXT," + ORDER_GROSS_AMOUNT + " TEXT," + ORDER_DISCOUNT + " REAL," + ORDER_DISCOUNT_IN_PERCENTAGE + " TEXT,"+ORDER_CONFIRM_CHILD_GST_VALUE + " TEXT," + ORDER_NetTotal + " TEXT);";

    //  Table tblPlnOrdConfirmChild
    public static final String CREATE_ORDER_DELIVERY_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_ORDER_CONFIRM_CHILD + "(" +
            " " + ORDER_CONFIRM_CHILD_DETAIL_ID + " INTEGER  , " + ORDER_CONFIRM_MASTER_DETAIL_ID+" INTEGER,"+ ORDER_CONFIRM_CHILD_SERVER_DETAIL_ID + " INTEGER,"+ ORDER_CONFIRM_CHILD_DISCOUNT_POLICY_ID + " INTEGER, "  + ORDER_CONFIRM_CHILD_ITEM_EMPTY_BOTTLE + " INTEGER, "+ ITEM_DELIVERY_ID + " INTEGER, " + ORDER_CONFIRM_CHILD_ITEM_NAME + " TEXT, " + ORDER_CONFIRM_CHILD_ITEM_ID + " INTEGER, " +
            " " + ORDER_CONFIRM_CHILD_DISCOUNT_PERCENTAGE + " REAL  , " + ORDER_CONFIRM_CHILD_DISCOUNT_POLICY_AMNT+" REAL,"+ ORDER_CONFIRM_CHILD_BONUS_POLICY_ID + " INTEGER,"+ ORDER_CONFIRM_CHILD_BONUS_QTY + " INTEGER, "  + ORDER_CONFIRM_CHILD_BONUS_GST + " REAL, "+
            " " + ORDER_CONFIRM_CHILD_COST_CTN_PRICE + " REAL ," + ORDER_CONFIRM_CHILD_WHOLESALE_CTN_PRICE + " REAL , " + ORDER_CONFIRM_CHILD_RETAIL_CTN_PRICE + " REAL , " + ORDER_CONFIRM_CHILD_BONUS_ITEM_ID + " INTEGER, "  + ORDER_CONFIRM_CHILD_BONUS_ITEM_NAME + " REAL, "+
            " " + ORDER_CONFIRM_CHILD_COST_PACK_PRICE + " REAL ," + ORDER_CONFIRM_CHILD_WHOLESALE_PACK_PRICE + " REAL , " + ORDER_CONFIRM_CHILD_RETAIL_PACK_PRICE + " REAL , " +
            " " + ORDER_CONFIRM_CHILD_COST_PIECES_PRICE + " REAL ," + ORDER_CONFIRM_CHILD_WHOLESALE_PIECES_PRICE + " REAL , " + ORDER_CONFIRM_CHILD_RETAIL_PIECES_PRICE + " REAL , " +
            " " + ORDER_CONFIRM_CHILD_CTN_QUANTITY + " REAL , " + ORDER_CONFIRM_CHILD_PACK_QUANTITY + " REAL , " + ORDER_CONFIRM_CHILD_PCS_QUANTITY + " REAL , " + ORDER_CONFIRM_CHILD_ITEM_TAXCODE+ " TEXT , " +
            " " + ORDER_CONFIRM_CHILD_REJECT_CTN_QUANTITY + " REAL , " + ORDER_CONFIRM_CHILD_REJECT_PACK_QUANTITY + " REAL , " + ORDER_CONFIRM_CHILD_REJECT_PCS_QUANTITY + " REAL , " +
            " " + ORDER_CONFIRM_CHILD_DELIVER_CTN_QUANTITY + " REAL , " + ORDER_CONFIRM_CHILD_DELIVER_PACK_QUANTITY + " REAL , " + ORDER_CONFIRM_CHILD_DELIVER_PCS_QUANTITY + " REAL , " +
            " " + ORDER_CONFIRM_CHILD_FOCQTY + " REAL , " + ORDER_CONFIRM_CHILD_FOCTYPE + " TEXT, " +
            " " + ORDER_CONFIRM_CHILD_FOC_VALUE + " REAL , " + ORDER_CONFIRM_CHILD_FOC_PERCENTAGE + " REAL , " + ORDER_CONFIRM_CHILD_DISCOUNT + " REAL , " +
            " " + ORDER_CONFIRM_CHILD_TOTAL_QUANTITY + " REAL , " + ORDER_CONFIRM_CHILD_TOTAL_COST + " REAL , " + ORDER_CONFIRM_CHILD_TOTAL_WHOLESALE + " REAL , " + ORDER_CONFIRM_CHILD_TOTAL_RETAIL + " REAL , " +
            " " + ORDER_CONFIRM_CHILD_NET_TOTAL_RETAIL + " REAL , " +
            " " + ORDER_CONFIRM_CHILD_REJECTED_QTY + " REAL , " + ORDER_CONFIRM_CHILD_DELIVERED_QTY + " REAL , " + ORDER_CONFIRM_CHILD_ROUTE_ID + " INTEGER ," + ORDER_CONFIRM_CHILD_CATEGORY_ID + " INTEGER , " + ITEM_ACTUAL_DELIVERD_Quantity + " REAL , " + ITEM_RETURN_Quantity + " REAL , " +
            " " + ITEM_REJECT_RESSON + " TEXT," + ORDER_CONFIRM_CHILD_DISPLAY_PRICE + " REAL," + ORDER_CONFIRM_CHILD_GST_VALUE + " REAL," + ORDER_CONFIRM_CHILD_GST_PER + " REAL," + ORDER_CONFIRM_CHILD_ITEM_ISSAVE + " TEXT," + ORDER_CONFIRM_CHILD_ITEM_ISNEW + " TEXT);";

    //  Table TBL_CUSTOMER_RECEIPTS
    public static final String CREATE_TABLE_TBL_CUSTOMER_RECEIPT = "CREATE TABLE IF NOT EXISTS " + TBL_CUSTOMER_RECEIPTS + " ( " +
            " " + RECEIPT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + RECEIPT_SERVER_ID + " INTEGER , " + RECEIPT_CUSTOMER_ID + " INTEGER , " + RECEIPT_CUSTOMER_Name + " TEXT , " + RECEIPT_AMOUNT_PAID + " REAL , " + RECEIPT_CASH_DEPOSITED_BANK_ID + " INTEGER , " + RECEIPT_CASH_DEPOSITED_BANK_NAME + " TEXT , " + RECEIPT_BALANCE + " REAL , " + RECEIPT_PREVIOUS_BALANCE + " REAL , " +
            " " + RECEIPT_REMARKS + " TEXT , " + RECEIPT_DATE + " TEXT , " + RECEIPT_IS_SYNC + " INTEGER );";





//                                  Drop TAble
    ///Table RouteModel
    public static final String DROP_TBL_ROUTE = "DROP TABLE IF EXISTS " + TBL_Route;

    //For Tbl visit Statuses
    public static final String DROP_TBL_TBL_VISIT_STATUSES = "DROP TABLE IF EXISTS " + TBL_Visited_Statuses;

    //for discount policies table
    public static final String DROP_TBL_DISCOUNT_POLICIES = "DROP TABLE IF EXISTS " + TBL_DISCOUNT_POLICY;

    //for discount policies
    public static final String DROP_TBL_BONUS_POLICIES = "DROP TABLE IF EXISTS " + TBL_BONUS_POLICY;

//
    public static final String DROP_TBL_DISTRIBUTORS = "DROP TABLE IF EXISTS " + TBL_DISTRIBUTORS;


    ///Table Item
    public static final String DROP_TBL_ITEM = "DROP TABLE IF EXISTS " + TBL_PLN_ITEMS;

    //check table
    public static final String DROP_TBL_CHECK= "DROP TABLE IF EXISTS " + TBL_DISTRIBUTORS;

    //Table Customer
    public static final String DROP_REGISTER_CUSTOMER_TABLE = "DROP TABLE IF EXISTS " + TBL_REGISTER_CUSTOMER;
    // Table Images
    public static final String DROP_IMG_TABLE = "DROP TABLE IF EXISTS " + TBL_IMAGES;
    // Table tblPlnOrdConfirmMaster
    public static final String DROP_ORDER_DELIVERY_TABLE = "DROP TABLE IF EXISTS " + TBL_ORDER_CONFIRM_MASTER;
    // Table tblPlnOrdConfirmChild
    public static final String DROP_ORDER_DELIVERY_ITEMS_TABLE = "DROP TABLE IF EXISTS " + TBL_ORDER_CONFIRM_CHILD;
    // Table tblPlnOrdConfirmChild
    public static final String DROP_ORDER_CUSTOMER_RECEIPT_TABLE = "DROP TABLE IF EXISTS " + TBL_CUSTOMER_RECEIPTS;


    public static final String DROP_TABLE_TBL_COMPANY_WISE_BANKS = "DROP TABLE IF EXISTS " + TBL_COMPANY_WISE_BANKS;
    public static final String DROP_TABLE_TBL_VISITED_DETAILS = "DROP TABLE IF EXISTS " + TBL_VisitedDetails;

}


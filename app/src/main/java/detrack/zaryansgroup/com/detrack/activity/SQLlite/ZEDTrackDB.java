package detrack.zaryansgroup.com.detrack.activity.SQLlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Model.BankModels.CompanyWiseBanksModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CustomerPriceModel.CustomerPriceModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CustomerVisitedModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DailySummeryModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.DistributorModel.DistributorModel;
import detrack.zaryansgroup.com.detrack.activity.Model.ImagesModel;
import detrack.zaryansgroup.com.detrack.activity.Model.MapModel;
import detrack.zaryansgroup.com.detrack.activity.Model.ReceiptModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.Model.VisitStatusesModel;
import detrack.zaryansgroup.com.detrack.activity.activites.BonusPolicyWork.BonusPolicyModel;
import detrack.zaryansgroup.com.detrack.activity.activites.DiscountPolicyWork.DiscountPolicyModel;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import timber.log.Timber;

/**
 * Created by 6520 on 2/9/2016.
 */

@SuppressLint("Range")
public class ZEDTrackDB extends SQLiteOpenHelper {

    private static final int VERSION = Utility.DB_VERSION;

    private static final String DB_NAME = "ZEDTrackDB";
    Context context;

    public ZEDTrackDB(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DBHelper.CREATE_ORDER_DELIVERY_TABLE);
            db.execSQL(DBHelper.CREATE_ORDER_DELIVERY_ITEMS_TABLE);
            db.execSQL(DBHelper.CREATE_REGISTER_CUSTOMER_TABLE);
            db.execSQL(DBHelper.CREATE_IMAGE_TABLE);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_ROUTE);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_PLN_ITEM);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_CUSTOMER_RECEIPT);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_CUSTOMER_PRICE);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_VISITED_DETAILS);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_VISIST_STATUSES);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_DISCOUNT_POLICIES);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_BONUS_POLICIES);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_COMPANY_WISE_BANKS);

            db.execSQL(DBHelper.CREATE_TABLE_DISTRIBUTORS);


            Utility.logCatMsg("Table Created");
        } catch (Exception e) {
            Utility.logCatMsg("Table creation Error: " + e.getMessage());
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DBHelper.DROP_ORDER_DELIVERY_TABLE);
            db.execSQL(DBHelper.DROP_ORDER_DELIVERY_ITEMS_TABLE);
            db.execSQL(DBHelper.DROP_REGISTER_CUSTOMER_TABLE);
            db.execSQL(DBHelper.DROP_IMG_TABLE);
            db.execSQL(DBHelper.DROP_TBL_ROUTE);
            db.execSQL(DBHelper.DROP_TBL_ITEM);
            db.execSQL(DBHelper.DROP_ORDER_CUSTOMER_RECEIPT_TABLE);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_CUSTOMER_PRICE);
            db.execSQL(DBHelper.DROP_TABLE_TBL_COMPANY_WISE_BANKS);
            db.execSQL(DBHelper.DROP_TABLE_TBL_VISITED_DETAILS);
            db.execSQL(DBHelper.DROP_TBL_TBL_VISIT_STATUSES);
            db.execSQL(DBHelper.DROP_TBL_DISCOUNT_POLICIES);
            db.execSQL(DBHelper.DROP_TBL_BONUS_POLICIES);
            db.execSQL(DBHelper.DROP_TBL_CHECK);

            Utility.logCatMsg("Drooped");
            onCreate(db);
        } catch (Exception e) {
            Utility.logCatMsg("Table drop Error: " + e.getMessage());
        }
    }



    public void createPlanOrderTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(DBHelper.CREATE_ORDER_DELIVERY_TABLE);
            db.execSQL(DBHelper.CREATE_ORDER_DELIVERY_ITEMS_TABLE);
            Utility.logCatMsg("Plan Order Tables Created");
        } catch (Exception e) {
            Utility.logCatMsg("Table creation Error: " + e.getMessage());
        }
    }


    public void createRunTimeOrderTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(DBHelper.CREATE_REGISTER_CUSTOMER_TABLE);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_PLN_ITEM);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_ROUTE);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_COMPANY_WISE_BANKS);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_DISCOUNT_POLICIES);
            db.execSQL(DBHelper.CREATE_TABLE_TBL_BONUS_POLICIES);

            db.execSQL(DBHelper.CREATE_TABLE_DISTRIBUTORS);
            Utility.logCatMsg("RUN Time Order Tables Created");
        } catch (Exception e) {
            Utility.logCatMsg("Table creation Error: " + e.getMessage());
        }
    }


    public void dropPlanOrderTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DBHelper.DROP_ORDER_DELIVERY_TABLE);
        db.execSQL(DBHelper.DROP_ORDER_DELIVERY_ITEMS_TABLE);
        Utility.logCatMsg("PlanOrderTables Drooped");
    }

    public void dropRunTimeOrderTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DBHelper.DROP_REGISTER_CUSTOMER_TABLE);
        db.execSQL(DBHelper.DROP_TBL_ROUTE);
        db.execSQL(DBHelper.DROP_TBL_ITEM);
        db.execSQL(DBHelper.DROP_TABLE_TBL_COMPANY_WISE_BANKS);
        db.execSQL(DBHelper.DROP_TBL_DISCOUNT_POLICIES);
        db.execSQL(DBHelper.DROP_TBL_BONUS_POLICIES);

        db.execSQL(DBHelper.DROP_TBL_CHECK);
        Timber.d("all Tables including bank deleted");
        Utility.logCatMsg("RunTimeOrderTables Drooped");
    }


    public int insertImages(ImagesModel model) {
        int result = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DBHelper.IMG_NAME, model.getImage_name());
            values.put(DBHelper.IMG_ORDER_ID, model.getImage_order_id());
            values.put(DBHelper.IMG_TAG, model.getImage_tag());
            values.put(DBHelper.IMG_IS_SYNCED, model.getImag_is_synced());
            database.insert(DBHelper.TBL_IMAGES, null, values);
            Utility.logCatMsg("1 IMG Row Entered");
            result = 1;
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertOrderDelivery()" + e);
        }
        database.close();
        return result;
    }



    public int insertCompanyWiseBankDetails(ArrayList<CompanyWiseBanksModel> list) {
        int result = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            for (int i = 0; i < list.size(); i++) {
                ContentValues values = new ContentValues();
                CompanyWiseBanksModel model = list.get(i);
                //Inserting Values to Columns db...
                values.put(DBHelper.BANK_ID, model.getBankID());
                values.put(DBHelper.BANK_NAME, model.getBankName());
                values.put(DBHelper.BANK_ACCOUNT_NBR, model.getBankAccountNbr());
                values.put(DBHelper.BANK_ACCOUNT_TYPE, model.getBankAccountType());
                values.put(DBHelper.BANK_ADDRESS, model.getAddress());
                database.insert(DBHelper.TBL_COMPANY_WISE_BANKS, null, values);


            }

            Log.d("BankDatainserteddbfinal", "true");
            result = 1;
        } catch (Exception e) {
            Log.d("BankDatainserteddbfinal", "false");
            Utility.logCatMsg("Insertion Failed in method insertBanksCompanyWise()" + e);
        }
        database.close();
        return result;
    }

    public int insertVisitStatuses(ArrayList<VisitStatusesModel> list) {
        int result = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            for (int i = 0; i < list.size(); i++) {
                ContentValues values = new ContentValues();
                VisitStatusesModel model = list.get(i);
                values.put(DBHelper.Status_ID, model.getStatusID());
                values.put(DBHelper.Visit_Status, model.getVisitStatus());
                database.insert(DBHelper.TBL_Visited_Statuses, null, values);

            }

            result = 1;
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertVisitStatuses()" + e);
        }
        database.close();
        return result;
    }


    public int insertDiscountPolciies(ArrayList<DiscountPolicyModel> list) {
        int result = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            for (int i = 0; i < list.size(); i++) {

                ContentValues values = new ContentValues();
                DiscountPolicyModel model = list.get(i);
                values.put(DBHelper.DISCOUNT_POLICY_ID, model.getDiscountPolicyId());
                values.put(DBHelper.DIS_TYPE_ID, model.getTypeId());
                values.put(DBHelper.DIS_TYPE, model.getType());
                values.put(DBHelper.DISCOUNT_TYPE, model.getDiscountType());
                values.put(DBHelper.DIS_TARGET_VALUE, model.getTargetValue());
                values.put(DBHelper.DIS_TARGET_QTY, model.getTargetQty());
                values.put(DBHelper.DIS_DISCOUNT_PERCENTAGE, model.getDiscountPercentage());
                values.put(DBHelper.DIS_DISCOUNT_VALUE, model.getDiscountValue());
                values.put(DBHelper.DIS_ADDITIONAL_DISCOUNT_PERCENTAGE, model.getAdditionalDiscountPercentage());
                values.put(DBHelper.DIS_ADDITIONAL_DISCOUNT_VALUE, model.getAdditionalDiscountValue());
                values.put(DBHelper.DIS_STOP_OTHER_DISCOUNT, model.getStopOtherDiscount());
                values.put(DBHelper.DIS_IS_CLAIMABLE, model.getIsClaimable());
                values.put(DBHelper.DIS_START_DATE, model.getStartDate());
                values.put(DBHelper.DIS_END_DATE, model.getEndDate());
                values.put(DBHelper.DIS_GROUP_DIV_ID, model.getGroupDivId());
                values.put(DBHelper.DIS_DIV_ID, model.getDivId());
                values.put(DBHelper.DIS_ITEM_ID, model.getItemId());
                database.insert(DBHelper.TBL_DISCOUNT_POLICY, null, values);
            }

            result = 1;
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertVisitStatuses()" + e);
        }
        database.close();
        return result;
    }




    public int insertBonusPolicies(ArrayList<BonusPolicyModel> list) {
        int result = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            for (int i = 0; i < list.size(); i++) {

                ContentValues values = new ContentValues();
                BonusPolicyModel model = list.get(i);
                values.put(DBHelper.BONS_POLICY_ID, model.getBonusPolicesId());
                values.put(DBHelper.BONS_TYPE_ID, model.getTypeId());
                values.put(DBHelper.BONS_ITEM_ID, model.getItemId());
                values.put(DBHelper.BONS_BONUS, model.getBonus());
                values.put(DBHelper.BONS_TYPE, model.getType());
                values.put(DBHelper.BONS_DISCOUNT_TYPE, model.getDiscountType());
                values.put(DBHelper.BONS_OUR_SHARE, model.getOurShare());
                values.put(DBHelper.BONS_TARGET_QTY, model.getTargetQty());
                values.put(DBHelper.BONS_INCENTIVE_ID, model.getIncentiveItemId());
                values.put(DBHelper.BONS_IS_CLAIMABLE, model.getIsClaimable());
                values.put(DBHelper.BONS_START_DATE, model.getStartDate());
                values.put(DBHelper.BONS_END_DATE, model.getEndDate());
                values.put(DBHelper.DIS_START_DATE, model.getStartDate());
                values.put(DBHelper.DIS_END_DATE, model.getEndDate());
                values.put(DBHelper.BONS_SUB_TYPE, model.getSubType());
                values.put(DBHelper.BONS_MULTI_ITEMS_NAMES, model.getMultiItemsName());
                values.put(DBHelper.BONS_MULTI_ITEMS_IDS, model.getMultiItemsIdes());
                database.insert(DBHelper.TBL_BONUS_POLICY, null, values);

            }

            result = 1;
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertVisitStatuses()" + e);
        }
        database.close();
        return result;
    }



    public boolean insertCustomerReceipt(ReceiptModel model) {
        boolean insert = false;
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            ContentValues values = new ContentValues();
            // values.put(DBHelper.RECEIPT_ID, model.getId());
            values.put(DBHelper.RECEIPT_SERVER_ID, model.getServerID());
            values.put(DBHelper.RECEIPT_CUSTOMER_ID, model.getCustomerID());
            values.put(DBHelper.RECEIPT_CUSTOMER_Name, model.getCustomerName());
            values.put(DBHelper.RECEIPT_AMOUNT_PAID, model.getAmountPaid());

            //Cash Deposited Bank work..
            values.put(DBHelper.RECEIPT_CASH_DEPOSITED_BANK_ID, model.getCashDepositedBankId());
            values.put(DBHelper.RECEIPT_CASH_DEPOSITED_BANK_NAME, model.getCashDepositedBankName());

            values.put(DBHelper.RECEIPT_BALANCE, model.getBalance());
            values.put(DBHelper.RECEIPT_PREVIOUS_BALANCE, model.getPreviousBalnc());
            values.put(DBHelper.RECEIPT_REMARKS, model.getRemarks());
            values.put(DBHelper.RECEIPT_DATE, model.getDate());
            values.put(DBHelper.RECEIPT_IS_SYNC, model.getIsSync());
            database.insert(DBHelper.TBL_CUSTOMER_RECEIPTS, null, values);
            Utility.logCatMsg("1 CustomerReceipt Row Entered");
            insert = true;
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertCustomerReceipt()" + e);
        }
        database.close();
        return insert;
    }


    public List<DailySummeryModel> getReceipt(String customerName) {

        List<DailySummeryModel> modelList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Utility.logCatMsg("customerName " + customerName);
            String sql = "select * from " + DBHelper.TBL_CUSTOMER_RECEIPTS + " where " + DBHelper.RECEIPT_CUSTOMER_Name + " LIKE '" + customerName + "'";
            Log.d("receiptQuery", sql);
            Cursor cursor = database.rawQuery(sql, null);
            if (cursor != null) {
                cursor.moveToFirst();
                do {
                    DailySummeryModel model = new DailySummeryModel();
                    String CustomerName = getCustomerName(cursor.getInt(cursor.getColumnIndex(DBHelper.RECEIPT_CUSTOMER_ID)));
                    Log.d("receiptCustomerName : ", CustomerName);
                    model.setCustomerName(CustomerName);
                    model.setCustId(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_CUSTOMER_ID)));
                    model.setReceipt("");
                    model.setReceipt(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_AMOUNT_PAID)));
                    model.setGrossTotal(0);
                    model.setAmount(0);
                    model.setCash(0);
                    model.setCredit(0);
                    model.setItemQty(0);
                    model.setDisc("0");
                    modelList.add(model);
                    Utility.logCatMsg(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_DATE)));
                } while (cursor.moveToNext());
                return modelList;
            } else
                Utility.logCatMsg("Null cursor");
        } catch (Exception e) {
            Utility.logCatMsg("Error in getTotalReceipt " + e.getMessage());
            return modelList;
        }
        return modelList;
    }


    public int DeleteReceipt(int receiptId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + DBHelper.TBL_CUSTOMER_RECEIPTS + " WHERE " + DBHelper.RECEIPT_ID + " = " + receiptId);
            db.close();
            return 1;
        } catch (Exception e) {
            Utility.logCatMsg("Exception in DeleteReceipt Method" + e);
            return 0;
        }

    }

    public ArrayList<ReceiptModel> getCustomerReceipt(String date) {
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_CUSTOMER_RECEIPTS + " where " + DBHelper.RECEIPT_DATE + " like '" + date + "%'", null);
            if (cursor != null) {
                ArrayList<ReceiptModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    ReceiptModel model = new ReceiptModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.RECEIPT_ID)));
                    model.setServerID(cursor.getInt(cursor.getColumnIndex(DBHelper.RECEIPT_SERVER_ID)));
                    model.setCustomerID(cursor.getInt(cursor.getColumnIndex(DBHelper.RECEIPT_CUSTOMER_ID)));
                    model.setCustomerName(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_CUSTOMER_Name)));
                    model.setAmountPaid(cursor.getFloat(cursor.getColumnIndex(DBHelper.RECEIPT_AMOUNT_PAID)));
                    model.setBalance(cursor.getFloat(cursor.getColumnIndex(DBHelper.RECEIPT_BALANCE)));
                    model.setPreviousBalnc(cursor.getFloat(cursor.getColumnIndex(DBHelper.RECEIPT_PREVIOUS_BALANCE)));
                    model.setDate(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_REMARKS)));
                    model.setRemarks(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_DATE)));
                    list.add(model);
                }
                return list;
            } else
                Utility.logCatMsg("Null cursor");
        } catch (Exception e) {
            Utility.logCatMsg("Error in getImages " + e.getMessage());
            return null;
        }
        return null;
    }

    public float getTotalReceipt(String date) {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Utility.logCatMsg("date " + date);
            //Cursor cursor = database.rawQuery("select * from "+ DBHelper.TBL_CUSTOMER_RECEIPTS , null);
            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_CUSTOMER_RECEIPTS + " where " + DBHelper.RECEIPT_DATE + " LIKE '" + date + "%'", null);
            float result = 0;
            if (cursor != null) {

                while (cursor.moveToNext()) {
                    result = result + cursor.getFloat(cursor.getColumnIndex(DBHelper.RECEIPT_AMOUNT_PAID));
                    Utility.logCatMsg(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_DATE)));
                    Utility.logCatMsg("Result " + result);
                }
                return result;
            } else
                Utility.logCatMsg("Null cursor");
        } catch (Exception e) {
            Utility.logCatMsg("Error in getTotalReceipt " + e.getMessage());
            return 0;
        }
        return 0;
    }


    @SuppressLint("Range")
    public ArrayList<DailySummeryModel> getReceiptList(String date) {
        ArrayList<DailySummeryModel> list = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Utility.logCatMsg("date " + date);
            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_CUSTOMER_RECEIPTS + " where " + DBHelper.RECEIPT_DATE + " LIKE '" + date + "%'", null);
            Timber.d("select * from " + DBHelper.TBL_CUSTOMER_RECEIPTS + " where " + DBHelper.RECEIPT_DATE + " LIKE '" + date + "%'");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    DailySummeryModel model = new DailySummeryModel();
                    String CustomerName = getCustomerName(cursor.getInt(cursor.getColumnIndex(DBHelper.RECEIPT_CUSTOMER_ID)));
                    Log.d("receiptCustomerName : ", CustomerName);
                    model.setCustomerName(CustomerName);
                    model.setCustId(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_CUSTOMER_ID)));
                    model.setReceipt(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_AMOUNT_PAID)));
                    Log.d("ReciptAmount", cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_AMOUNT_PAID)));
                    model.setGrossTotal(0);
                    model.setAmount(0);
                    model.setCash(0);
                    model.setCredit(0);
                    model.setItemQty(0);
                    model.setDisc("0");
                    Utility.logCatMsg(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_DATE)));
                    list.add(model);
                }
                return list;
            } else
                Utility.logCatMsg("Null cursor");
        } catch (Exception e) {
            Utility.logCatMsg("Error in getTotalReceipt " + e.getMessage());
            return list;
        }
        return list;
    }

    @SuppressLint("Range")
    public ArrayList<ReceiptModel> getTodaysReceipts(String date) {
        ArrayList<ReceiptModel> list = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor;
        try {
            Utility.logCatMsg("SQLite code ");
            if (date.equals("")) {
                cursor = database.rawQuery("SELECT  * FROM " + DBHelper.TBL_CUSTOMER_RECEIPTS + "", null);
            } else {
                cursor = database.rawQuery("SELECT  * FROM " + DBHelper.TBL_CUSTOMER_RECEIPTS + " where " + DBHelper.RECEIPT_DATE + " like '%" + date + "%'", null);
            }

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    ReceiptModel model = new ReceiptModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.RECEIPT_ID)));
                    model.setServerID(cursor.getInt(cursor.getColumnIndex(DBHelper.RECEIPT_SERVER_ID)));
                    model.setCustomerID(cursor.getInt(cursor.getColumnIndex(DBHelper.RECEIPT_CUSTOMER_ID)));
                    model.setCustomerName(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_CUSTOMER_Name)));
                    model.setAmountPaid(cursor.getFloat(cursor.getColumnIndex(DBHelper.RECEIPT_AMOUNT_PAID)));

                    //For Bank Work
                    model.setCashDepositedBankName(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_CASH_DEPOSITED_BANK_NAME)));
                    model.setCashDepositedBankId(cursor.getInt(cursor.getColumnIndex(DBHelper.RECEIPT_CASH_DEPOSITED_BANK_ID)));

                    model.setBalance(cursor.getFloat(cursor.getColumnIndex(DBHelper.RECEIPT_BALANCE)));
                    model.setPreviousBalnc(cursor.getFloat(cursor.getColumnIndex(DBHelper.RECEIPT_PREVIOUS_BALANCE)));
                    model.setRemarks(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_REMARKS)));
                    model.setDate(cursor.getString(cursor.getColumnIndex(DBHelper.RECEIPT_DATE)));
                    model.setIsSync(cursor.getInt(cursor.getColumnIndex(DBHelper.RECEIPT_IS_SYNC)));
                    list.add(model);
                }
                Utility.logCatMsg("List Size " + list.size());
                return list;
            } else
                Utility.logCatMsg("Null cursor");
        } catch (Exception e) {
            Utility.logCatMsg("Error in getTotalReceipt " + e.getMessage());
            return list;
        }
        return list;
    }

    public int UpdateReceipt(ReceiptModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.RECEIPT_IS_SYNC, model.getIsSync());
        values.put(DBHelper.RECEIPT_SERVER_ID, model.getServerID());
        values.put(DBHelper.RECEIPT_BALANCE, model.getBalance());
        values.put(DBHelper.RECEIPT_PREVIOUS_BALANCE, model.getPreviousBalnc());
        try {
            return db.update(DBHelper.TBL_CUSTOMER_RECEIPTS, values, DBHelper.RECEIPT_ID + " = ?",
                    new String[]{String.valueOf(model.getId())});
        } catch (Exception e) {
            Utility.logCatMsg("Exception in UpdateReceipt Method in SQLite: " + e.getMessage());
            return 0;
        }
    }


    public ArrayList<ImagesModel> getImages(int orderID, String tag) {
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_IMAGES + " where " + DBHelper.IMG_ORDER_ID + " = '" + orderID + "' and " + DBHelper.IMG_TAG + " = '" + tag + "'", null);
            if (cursor != null) {
                ArrayList<ImagesModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    ImagesModel model = new ImagesModel();
                    model.setImage_id(cursor.getInt(cursor.getColumnIndex(DBHelper.IMG_ID)));
                    model.setImage_name(cursor.getString(cursor.getColumnIndex(DBHelper.IMG_NAME)));
                    model.setImage_tag(cursor.getString(cursor.getColumnIndex(DBHelper.IMG_TAG)));
                    model.setImage_order_id(cursor.getInt(cursor.getColumnIndex(DBHelper.IMG_ORDER_ID)));
                    model.setImag_is_synced(cursor.getString(cursor.getColumnIndex(DBHelper.IMG_IS_SYNCED)));
                    list.add(model);
                }
                return list;
            } else
                Utility.logCatMsg("Null cursor");
        } catch (Exception e) {
            Utility.logCatMsg("Error in getImages " + e.getMessage());
            return null;
        }
        return null;
    }


    public ArrayList<ImagesModel> getImagesNames(int IsSynced, int orderID) {
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_IMAGES + " where " + DBHelper.IMG_ORDER_ID + " = '" + orderID + "' and " + DBHelper.IMG_IS_SYNCED + " = '" + IsSynced + "'", null);
            Timber.d("select * from " + DBHelper.TBL_IMAGES + " where " + DBHelper.IMG_ORDER_ID + " = '" + orderID + "' and " + DBHelper.IMG_IS_SYNCED + " = '" + IsSynced + "'");
            if (cursor != null) {
                ArrayList<ImagesModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    ImagesModel model = new ImagesModel();
                    model.setImage_id(cursor.getInt(cursor.getColumnIndex(DBHelper.IMG_ID)));
                    model.setImage_name(cursor.getString(cursor.getColumnIndex(DBHelper.IMG_NAME)));
                    model.setImage_tag(cursor.getString(cursor.getColumnIndex(DBHelper.IMG_TAG)));
                    model.setImage_order_id(cursor.getInt(cursor.getColumnIndex(DBHelper.IMG_ORDER_ID)));
                    model.setImag_is_synced(cursor.getString(cursor.getColumnIndex(DBHelper.IMG_IS_SYNCED)));
                    list.add(model);
                }
                return list;
            } else
                Utility.logCatMsg("Null cursor");
        } catch (Exception e) {
            Utility.logCatMsg("Error in getImages " + e.getMessage());
            return null;
        }
        return null;
    }

    public ArrayList<ImagesModel> getImages(int orderID) {
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_IMAGES + " where " + DBHelper.IMG_ORDER_ID + " = '" + orderID + "'", null);
            if (cursor != null) {
                ArrayList<ImagesModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    ImagesModel model = new ImagesModel();
                    model.setImage_id(cursor.getInt(cursor.getColumnIndex(DBHelper.IMG_ID)));
                    model.setImage_name(cursor.getString(cursor.getColumnIndex(DBHelper.IMG_NAME)));
                    model.setImage_tag(cursor.getString(cursor.getColumnIndex(DBHelper.IMG_TAG)));
                    model.setImage_order_id(cursor.getInt(cursor.getColumnIndex(DBHelper.IMG_ORDER_ID)));
                    model.setImag_is_synced(cursor.getString(cursor.getColumnIndex(DBHelper.IMG_IS_SYNCED)));
                    list.add(model);
                }
                return list;
            } else
                Utility.logCatMsg("Null cursor");
        } catch (Exception e) {
            Utility.logCatMsg("Error in getImages " + e.getMessage());
            return null;
        }
        return null;
    }

    public int deleteImage(int id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + DBHelper.TBL_IMAGES + " WHERE " + DBHelper.IMG_ID + " = " + id); //delete  row in a table with the condition
            db.close();
            return 1;
        } catch (Exception e) {
            Utility.logCatMsg("Exception in deleteRunTimeOrder Method" + e.getMessage());
            return 0;

        }
    }


    public int insertOrderDelivery(ArrayList<DeliveryInfo> list, String ISNEW) {
        int result = 0;
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            for (int i = 0; i < list.size(); i++) {
                DeliveryInfo model = list.get(i);
                if (CheckOrderDeliveryExit(model.getServer_Delivery_id() + "", ISNEW)) {
                    Utility.logCatMsg("Order is already save");
                    if (SetOrderNewUpdate(model) == 1)
                        Utility.logCatMsg("Update Request has been set");
                    result = 0;
                } else {
                    ContentValues values = new ContentValues();
                    values.put(DBHelper.ORDER_CONFIRM_MASTER_ID, model.getServer_Delivery_id());

                    values.put(DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID, model.getServer_Delivery_id());
                    values.put(DBHelper.ORDER_EMPLOYEEID, model.getEmp_id());
                    values.put(DBHelper.ORDER_CUSTOMERID, model.getCustomer_id());
                    values.put(DBHelper.ORDER_VEHICLEID, model.getVehicle_id());

                    values.put(DBHelper.ORDER_STATUS, model.getDelivery_status());
                    values.put(DBHelper.ORDER_DESCRIPTION, model.getDelivery_description());
                    values.put(DBHelper.ORDER_TOTAL_QTY, model.getTotal_qty());
                    values.put(DBHelper.ORDER_DELIVERY_DATE, model.getDelivery_date());
                    values.put(DBHelper.ORDER_START_TIME, model.getDelivery_start_time());
                    values.put(DBHelper.ORDER_END_TIME, model.getDelivery_end_time());
                    values.put(DBHelper.ORDER_DELIVERY_ADDRESS, model.getDelivery_address());
                    values.put(DBHelper.DELIVERY_TO_MOBILE, model.getDelivery_to_mobile());
                    values.put(DBHelper.ORDER_BY, model.getDeliver_to_name());
                    values.put(DBHelper.ORDER_IS_REJECT, model.getIs_delivery_Reject());

                    values.put(DBHelper.ORDER_RECEIVED_BY, model.getReceivedBy());

                    //Bank work
                    values.put(DBHelper.ORDER_CASH_DEPOSITED_BANK_ID, model.getCashDespositedBankId());

                    values.put(DBHelper.ORDER_PODLAT, model.getPod_lat());
                    values.put(DBHelper.ORDER_PODLNG, model.getPod_lng());
                    values.put(DBHelper.ORDER_NOTE, model.getNote());
                    values.put(DBHelper.ORDER_LATITUDE, model.getDeliver_lat());
                    values.put(DBHelper.ORDER_LONGITUDE, model.getDeliver_lng());

                    values.put(DBHelper.ORDER_TOTAL_AMOUNT, model.getTotal_Bill());
                    values.put(DBHelper.ORDER_GROSS_AMOUNT, model.getGrossTotalBill());
                    values.put(DBHelper.ORDER_CONFIRM_CHILD_GST_VALUE, model.getGst());
                    values.put(DBHelper.ORDER_DISCOUNT_IN_PERCENTAGE, model.getPercentageDiscount());
                    values.put(DBHelper.ORDER_DISCOUNT, model.getDiscount());
                    values.put(DBHelper.ORDER_NetTotal, model.getNetTotal());

                    values.put(DBHelper.ORDER_ISSAVE, "0");
                    values.put(DBHelper.ORDER__ISNEW, ISNEW);
                    values.put(DBHelper.ORDER__ISREAD, 0);
                    values.put(DBHelper.ORDER__NEW_UPDATE, 0);

                    values.put(DBHelper.ORDER_NO, model.getOrderNumber());
                    values.put(DBHelper.ORDER_SERIAL_NO, model.getSerialNo());
                    values.put(DBHelper.ORDER_DATE_TIME, model.getDelivery_date());
                    values.put(DBHelper.ORDER_SALES_MODE, model.getSalemode());
                    values.put(DBHelper.ORDER_REFUSED_REASON, "");
                    values.put(DBHelper.ORDER_CANCELLED_REASON, "");

                    values.put(DBHelper.ORDER_REJECTED_REASON, model.getRejected_Reason());
                    values.put(DBHelper.ORDER_POBLAT, model.getPob_lat());
                    values.put(DBHelper.ORDER_POBLNG, model.getPob_lng());
                    values.put(DBHelper.ORDER_ROUTE_ID, model.getRoute());
                    values.put(DBHelper.ORDER_CATEGORY_ID, model.getCategoryId());

                    values.put(DBHelper.ORDER_CONFIRM_MASTER_From_Server, 1); //this fucnt is runing from server

                    database.insert(DBHelper.TBL_ORDER_CONFIRM_MASTER, null, values);
                    result = 1;
                }
            }
            Utility.logCatMsg(list.size() + " Info Rows Entered");
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertOrderDelivery()" + e);
        }
        database.close();
        return result;
    }

    public int insertOrderDeliveryItems(ArrayList<DeliveryItemModel> list, String ISNEW) {

        long rowEffected = 1;
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            for (int i = 0; i < list.size(); i++) {
                DeliveryItemModel model = list.get(i);
//                if (CheckOrderDeliveryExit(model.getOrder_item_id() + "", ISNEW)) {
//                    Log.d("itemExists", "true");
//                } else {
                ContentValues values = new ContentValues();
                values.put(DBHelper.ORDER_CONFIRM_CHILD_DETAIL_ID, model.getServer_Item_Id());
                values.put(DBHelper.ORDER_CONFIRM_MASTER_DETAIL_ID, 0);//this will zero when loading from server because we don't need sales
                // closing unless user made some orders from app, locally
                values.put(DBHelper.ORDER_CONFIRM_CHILD_SERVER_DETAIL_ID, model.getServer_Item_Id());
                values.put(DBHelper.ITEM_DELIVERY_ID, model.getOrder_item_id());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_NAME, model.getName());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_ID, model.getItem_id());

                Log.d("itemIDGet", String.valueOf(model.getOrder_item_id()) + "   " + ISNEW);

                values.put(DBHelper.ORDER_CONFIRM_CHILD_COST_CTN_PRICE, model.getCostCtnPrice());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_WHOLESALE_CTN_PRICE, model.getRetailPiecePrice());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_RETAIL_CTN_PRICE, model.getRetailCtnPrice());

                values.put(DBHelper.ORDER_CONFIRM_CHILD_COST_PACK_PRICE, model.getCostPackPrice());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_WHOLESALE_PACK_PRICE, model.getWSPackPrice());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_RETAIL_PACK_PRICE, model.getRetailPackPrice());

                values.put(DBHelper.ORDER_CONFIRM_CHILD_COST_PIECES_PRICE, model.getCostPiecePrice());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_WHOLESALE_PIECES_PRICE, model.getWSPiecePrice());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_RETAIL_PIECES_PRICE, model.getWSCtnPrice());

                values.put(DBHelper.ORDER_CONFIRM_CHILD_CTN_QUANTITY, model.getCtn_qty());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_PACK_QUANTITY, model.getPac_qty());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_PCS_QUANTITY, model.getPcs_qty());


                values.put(DBHelper.ORDER_CONFIRM_CHILD_REJECT_CTN_QUANTITY, "0");
                values.put(DBHelper.ORDER_CONFIRM_CHILD_REJECT_PACK_QUANTITY, "0");
                values.put(DBHelper.ORDER_CONFIRM_CHILD_REJECT_PCS_QUANTITY, "0");

                values.put(DBHelper.ORDER_CONFIRM_CHILD_DELIVER_CTN_QUANTITY, model.getCtn_qty());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_DELIVER_PACK_QUANTITY, model.getPac_qty());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_DELIVER_PCS_QUANTITY, model.getPcs_qty());

                values.put(DBHelper.ORDER_CONFIRM_CHILD_FOCQTY, model.getFoc_qty());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_FOC_VALUE, model.getFoc_value());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_FOC_PERCENTAGE, model.getFoc_percentage());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_DISCOUNT, model.getItem_discount());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_COST, model.getTotalCostPrice());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_WHOLESALE, model.getTotalwholeSalePrice());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_RETAIL, model.getTotalRetailPrice());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_NET_TOTAL_RETAIL, model.getNetTotalRetailPrice());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_ROUTE_ID, model.getRoute_id());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_CATEGORY_ID, model.getCategoryId());

                Utility.logCatMsg("Order Id in Sqllite " + DBHelper.ITEM_DELIVERY_ID + " " + model.getOrder_item_id());


                values.put(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_QUANTITY, model.getTotal_Quantity());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_REJECTED_QTY, model.getRejected_Quantity());
                values.put(DBHelper.ITEM_RETURN_Quantity, model.getReturn_Quantity());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_DELIVERED_QTY, model.getTotal_Quantity());
                values.put(DBHelper.ITEM_ACTUAL_DELIVERD_Quantity, model.getTotal_Quantity());
                values.put(DBHelper.ITEM_REJECT_RESSON, model.getRejectReason());

                values.put(DBHelper.ORDER_CONFIRM_CHILD_DISPLAY_PRICE, model.getDisplayPrice());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_GST_VALUE, model.getItemGstValue());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_GST_PER, model.getItemGstPer());
                values.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_ISSAVE, "0");

                values.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_ISNEW, ISNEW);

                values.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_TAXCODE, model.getTaxCode());

                rowEffected = database.insert(DBHelper.TBL_ORDER_CONFIRM_CHILD, null, values);
            }
            // }

            Utility.logCatMsg(list.size() + " Items Rows Entered ");
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertOrderDeliveryItems()");
        }
        database.close();
        return (int) rowEffected;
    }

    public ArrayList<DeliveryInfo> getSQLiteOrderDelivery(String status) {
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            //"SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER__ISNEW + " = 'True' and " + DBHelper.ORDER_STATUS + " = 'Inprogress' and " + DBHelper.ORDER_ISSAVE + "!= '2'";

            String sql = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER__ISNEW + " = 'True' and " + DBHelper.ORDER_STATUS + " = '" + status + "' and " + DBHelper.ORDER_ISSAVE + "!= '2'";
            Log.d("sqliteQuery", sql);
            Cursor cursor = database.rawQuery(sql, null);
            if (cursor != null) {
                ArrayList<DeliveryInfo> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    DeliveryInfo model = new DeliveryInfo();
                    model.setDelivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_ID)));
                    model.setServer_Delivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID)));
                    model.setDelivery_status(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_STATUS)));
                    model.setDelivery_description(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DESCRIPTION)));
                    model.setTotal_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_QTY)));
                    model.setDelivery_date(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DELIVERY_DATE)));
                    model.setDelivery_start_time(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_START_TIME)));
                    model.setDelivery_end_time(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_END_TIME)));
                    model.setDelivery_address(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DELIVERY_ADDRESS)));
                    model.setDelivery_to_mobile(cursor.getString(cursor.getColumnIndex(DBHelper.DELIVERY_TO_MOBILE)));
                    model.setDeliver_to_name(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_BY)));
                    // model.setAssign_to_TrackingNo(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_ASSIGN_TO_TRACKING_ID)));
                    model.setIs_delivery_Reject(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_IS_REJECT)));
                    model.setRejected_Reason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_REJECTED_REASON)));
                    model.setRefused_Reason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_REFUSED_REASON)));
                    model.setCancelledReason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CANCELLED_REASON)));
                    model.setReceivedBy(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_RECEIVED_BY)));
                    model.setPod_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_PODLAT)));
                    model.setPod_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_PODLNG)));
                    model.setNote(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_NOTE)));
                    model.setDeliver_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LATITUDE)));
                    model.setDeliver_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LONGITUDE)));
                    model.setIsPod_sync(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_ISSAVE)));
                    model.setIsOrderRead(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER__ISREAD)));
                    model.setIsNewUpdate(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER__NEW_UPDATE)));
                    model.setCustomer_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CUSTOMERID)));

                    //Bank work..
                    model.setCashDespositedBankId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CASH_DEPOSITED_BANK_ID)));

                    model.setTotal_Bill(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_AMOUNT)));
                    model.setGrossTotalBill(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_GROSS_AMOUNT)));
                    model.setPercentageDiscount(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DISCOUNT_IN_PERCENTAGE)));
                    model.setDiscount(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DISCOUNT)));
                    model.setNetTotal(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_NetTotal)));

                    model.setOrderNumber(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_NO)));
                    model.setFromserver(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_From_Server)));
                    list.add(model);

                }
                Utility.logCatMsg("SQLite list " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getSQLiteOrderDelivery(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public ArrayList<DeliveryInfo> getSQLiteNotificationOrderDelivery(String IsNew) {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select  * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER__ISNEW + " = '" + IsNew + "' ORDER BY " + DBHelper.ORDER_CONFIRM_MASTER_ID + " DESC LIMIT 1 ", null);
            if (cursor != null) {
                ArrayList<DeliveryInfo> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    DeliveryInfo model = new DeliveryInfo();
                    model.setDelivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_ID)));
                    model.setDelivery_status(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_STATUS)));
                    model.setDelivery_description(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DESCRIPTION)));
                    model.setTotal_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_QTY)));
                    model.setDelivery_date(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DELIVERY_DATE)));
                    model.setDelivery_start_time(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_START_TIME)));
                    model.setDelivery_end_time(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_END_TIME)));
                    model.setDelivery_address(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DELIVERY_ADDRESS)));
                    model.setDelivery_to_mobile(cursor.getString(cursor.getColumnIndex(DBHelper.DELIVERY_TO_MOBILE)));
                    model.setDeliver_to_name(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_BY)));
                    // model.setAssign_to_TrackingNo(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_ASSIGN_TO_TRACKING_ID)));
                    model.setIs_delivery_Reject(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_IS_REJECT)));
                    model.setRejected_Reason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_REJECTED_REASON)));
                    model.setRefused_Reason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_REFUSED_REASON)));
                    model.setCancelledReason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CANCELLED_REASON)));
                    model.setReceivedBy(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_RECEIVED_BY)));
                    model.setPod_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_PODLAT)));
                    model.setPod_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_PODLNG)));

                    model.setNote(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_NOTE)));
                    model.setDeliver_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LATITUDE)));
                    model.setDeliver_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LONGITUDE)));
                    model.setIsPod_sync(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_ISSAVE)));

                    model.setFromserver(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_From_Server)));

                    list.add(model);

                }
                Utility.logCatMsg("SQLite list " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getSQLiteOrderDelivery(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    //todo


    public ArrayList<DeliveryItemModel> getSQLiteOrderDeliveryItems(String delivery_id, String IsNew, boolean forclosing) {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Cursor cursor;
            if (forclosing) {
                cursor = database.rawQuery("select * from " + DBHelper.TBL_ORDER_CONFIRM_CHILD + " Where " + DBHelper.ORDER_CONFIRM_MASTER_DETAIL_ID + " = " + Integer.parseInt(delivery_id), null);
            } else {
                cursor = database.rawQuery("select * from " + DBHelper.TBL_ORDER_CONFIRM_CHILD + " Where " + DBHelper.ITEM_DELIVERY_ID + " = " + Integer.parseInt(delivery_id), null);

            }

            if (cursor != null) {
                ArrayList<DeliveryItemModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {

                    DeliveryItemModel model = new DeliveryItemModel();
                    model.setOrder_item_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_DETAIL_ID)));
                    model.setOrder_master_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_DETAIL_ID)));
                    model.setServer_Item_Id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_SERVER_DETAIL_ID)));
                    model.setName(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_ITEM_NAME)));
                    model.setItem_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_ITEM_ID)));
                    model.setPrice(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_COST_CTN_PRICE)));
                    model.setCtn_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_CTN_QUANTITY)));
                    model.setPac_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_PACK_QUANTITY)));
                    model.setPcs_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_PCS_QUANTITY)));
                    model.setReject_ctn_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_REJECT_CTN_QUANTITY)));
                    model.setReject_pac_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_REJECT_PACK_QUANTITY)));
                    model.setReject_pcs_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_REJECT_PCS_QUANTITY)));
                    model.setDeliver_ctn_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_DELIVER_CTN_QUANTITY)));
                    model.setDeliver_pac_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_DELIVER_PACK_QUANTITY)));
                    model.setDeliver_pcs_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_DELIVER_PCS_QUANTITY)));
                    model.setFocType(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_FOCTYPE)));
                    model.setFoc_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_FOCQTY)));
                    model.setFoc_value(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_FOC_VALUE)));
                    model.setFoc_percentage(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_FOC_PERCENTAGE)));
                    model.setItem_discount(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_DISCOUNT)));
                    Utility.logCatMsg("Item Discount " + model.getItem_discount());
                    model.setRoute_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_ROUTE_ID)));
                    model.setCategoryId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_CATEGORY_ID)));
                    Utility.logCatMsg("Category ID " + model.getCategoryId());
                    model.setDelivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_DELIVERY_ID)));
                    model.setTotal_Quantity(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_QUANTITY)));
                    model.setReturn_Quantity(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_RETURN_Quantity)));
                    model.setRejected_Quantity(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_REJECTED_QTY)));
                    model.setDelivered_Quantity(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_DELIVERED_QTY)));
                    model.setActualDeliverd_Quantity(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_ACTUAL_DELIVERD_Quantity)));
                    model.setRejectReason(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_REJECT_RESSON)));
                    model.setCostCtnPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_COST_CTN_PRICE)));
                    model.setRetailPiecePrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_WHOLESALE_CTN_PRICE)));
                    model.setRetailCtnPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_RETAIL_CTN_PRICE)));
                    model.setCostPackPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_COST_PACK_PRICE)));
                    model.setWSPackPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_WHOLESALE_PACK_PRICE)));
                    model.setRetailPackPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_RETAIL_PACK_PRICE)));
                    model.setCostPiecePrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_COST_PIECES_PRICE)));
                    model.setWSPiecePrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_WHOLESALE_PIECES_PRICE)));
                    model.setWSCtnPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_RETAIL_PIECES_PRICE)));
                    model.setTotalCostPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_COST)));
                    model.setTotalwholeSalePrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_WHOLESALE)));
                    model.setTotalRetailPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_RETAIL)));
                    model.setNetTotalRetailPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_NET_TOTAL_RETAIL)));

                    model.setDisplayPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_DISPLAY_PRICE)));

                    Timber.d("The Display Price is "+DBHelper.ORDER_CONFIRM_CHILD_DISPLAY_PRICE);
                    Timber.d("The Display1 Price is "+model.getDisplayPrice());


                    model.setItemGstValue(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_GST_VALUE)));
                    model.setItemGstPer(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_GST_PER)));
                    model.setEmptyBottles(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_ITEM_EMPTY_BOTTLE)));
                    model.setTaxCode(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_ITEM_TAXCODE)));

                    //for discount policy params
                    model.setDiscountPolicyId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_DISCOUNT_POLICY_ID)));
                    model.setDiscPercentage(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_DISCOUNT_PERCENTAGE)));
                    model.setDiscountPolicyValue(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_DISCOUNT_POLICY_AMNT)));


                    //for bonus policy
                    model.setBonusPolicyId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_BONUS_POLICY_ID)));
                    model.setBonusIncentiveItemId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_BONUS_ITEM_ID)));
                    model.setBonusItemName(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_BONUS_ITEM_NAME)));
                    model.setBonusQty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_BONUS_QTY)));
                    model.setBonusItemsGst(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_BONUS_GST)));

                    DeliveryItemModel itemModel = getCompanyItem(model.getItem_id());
                    model.setPackSize(itemModel.getPackSize());
                    model.setCtnSize(itemModel.getCtnSize());
                    list.add(model);

                }
                Utility.logCatMsg("SQLite delivery list size " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getSQLiteOrderDeliveryItems(): " + e.getMessage());
            return null;
        }
        database.close();

        return null;
    }

    public DeliveryInfo getSelectedSQLiteOrderDelivery(int delivery_id, String ISNEW) {
        SQLiteDatabase database = this.getReadableDatabase();
        DeliveryInfo model = new DeliveryInfo();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor;

//            if(ISNEW.equalsIgnoreCase("False"))
            cursor = database.rawQuery("select * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " Where " +
                    DBHelper.ORDER_CONFIRM_MASTER_ID + " = " + delivery_id + "", null);


//            else
//                cursor = database.rawQuery("select * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " Where " +
//                        DBHelper.ORDER_CONFIRM_MASTER_ID + " = " + delivery_id + "", null);


            if (cursor != null) {
                while (cursor.moveToNext()) {
//                    values.put(DBHelper.ORDER_TOTAL_QTY, Total_Quantity);
                    model.setIsUrgentOrderStatus(cursor.getString(cursor.getColumnIndex(DBHelper.URGENT_ORDER_STATUS)));
                    model.setDelivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_ID)));
                    model.setServer_Delivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID)));
                    model.setCustomer_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CUSTOMERID)));
                    model.setDelivery_status(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_STATUS)));
                    model.setDelivery_description(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DESCRIPTION)));

                    model.setTotal_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_QTY)));

                    model.setDelivery_date(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DELIVERY_DATE)));
                    model.setDelivery_start_time(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_START_TIME)));
                    model.setDelivery_end_time(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_END_TIME)));
                    model.setDelivery_address(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DELIVERY_ADDRESS)));
                    model.setDelivery_to_mobile(cursor.getString(cursor.getColumnIndex(DBHelper.DELIVERY_TO_MOBILE)));
                    model.setDeliver_to_name(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_BY)));
                    // model.setAssign_to_TrackingNo(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_TRACKING_NO)));
                    model.setIs_delivery_Reject(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_IS_REJECT)));
                    model.setRejected_Reason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_REJECTED_REASON)));
                    model.setRefusedReason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_REFUSED_REASON)));
                    model.setCancelledReason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CANCELLED_REASON)));
                    model.setSalemode(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_SALES_MODE)));

                    model.setReceivedBy(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_RECEIVED_BY)));
                    model.setPod_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_PODLAT)));
                    model.setPod_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_PODLNG)));
                    model.setIsPod_sync(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_ISSAVE)));

                    model.setNote(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_NOTE)));
                    model.setDeliver_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LATITUDE)));
                    model.setDeliver_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LONGITUDE)));

                    model.setTotal_Bill(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_AMOUNT)));
                    model.setGrossTotalBill(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_GROSS_AMOUNT)));
                    model.setPercentageDiscount(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DISCOUNT_IN_PERCENTAGE)));
                    model.setDiscount(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DISCOUNT)));
                    model.setGst(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_GST_VALUE)));
                    Log.d("gstvalue", "=" + model.getGst());

                    model.setNetTotal(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_NetTotal)));
                    // model.setCategoryId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CATEGORY_ID)));
                    model.setCategoryId(0);
                    model.setOrderNumber(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_NO)));
                    Log.d("getOrderNumber", model.getOrderNumber());
                    model.setSerialNo(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_SERIAL_NO)));
                    model.setPob_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_POBLAT)));
                    model.setPob_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_POBLNG)));
                    model.setRoute(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_ROUTE_ID)));
                    model.setIsSave(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_ISSAVE)));
                    model.setFromserver(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_From_Server)));

                    model.setTotal_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_QTY)));

                    model.setDistributorId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_DISTRIBUTOR_ID)));
                    model.setSubDistributorId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_SUB_DISTRIBUTOR_ID)));


                }
                return model;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getSQLiteBusinessCategories(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public ArrayList<DailySummeryModel> getTodaySaleSummery(String date, String designation) {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "";
        ArrayList<DailySummeryModel> list = new ArrayList<>();
        DailySummeryModel model;
        try {
            Utility.logCatMsg("SQLite code ");

//            if (designation.equals("Order Booker")) {
//                sql = "select * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " Where " + DBHelper.ORDER_DELIVERY_DATE + " like '%" + date + "%' and " + DBHelper.ORDER_STATUS + " = 'Booking' or " + DBHelper.ORDER_STATUS + " = 'Returned'";
//            } else {
//                sql = "select * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " Where " + DBHelper.ORDER_DELIVERY_DATE + " like '%" + date + "%' and " + DBHelper.ORDER_STATUS + " = 'Delivered' or " + DBHelper.ORDER_STATUS + " = 'Returned'";
//            }
            if (designation.equals("Order Booker")) {
                sql = "select * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " Where " + DBHelper.ORDER_DELIVERY_DATE + " like '%" + date + "%'";

            } else {
                sql = "select * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " Where " + DBHelper.ORDER_DELIVERY_DATE + " like '%" + date + "%'";

            }

            Timber.d("Sql Query is "+sql);
            Cursor cursor = database.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    model = new DailySummeryModel();
                    String CustomerName = getCustomerName(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CUSTOMERID)));
                    if (!CustomerName.equals("null"))
                        model.setCustomerName(CustomerName);
                    model.setCustId(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CUSTOMERID)));
                    model.setOrderId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_ID)));
                    model.setItemQty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_QTY)));

                    model.setCustomerSalesMode(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_SALES_MODE)));

                    model.setAmount(Float.valueOf(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_NetTotal))));

                    model.setGrossTotal(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_GROSS_AMOUNT)));

                    float itemWise = getItemTotalDisc(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_ID)));
                    float PercentageWise = cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_DISCOUNT));
                    float totalCalculated = (itemWise + PercentageWise);
                    model.setDisc(itemWise + "\n+\n" + PercentageWise + "\n=" + totalCalculated);
                    model.setCash(0);
                    model.setCredit(0);
                    model.setOrderStatus(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_STATUS)));
                    if (cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_SALES_MODE)).equals("Cash"))
                        model.setCash(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_NetTotal)));
                    else
                        model.setCredit(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_NetTotal)));


                    list.add(model);

                }
                database.close();
                cursor.close();
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");

        } catch (Exception e) {
            database.close();
            Utility.logCatMsg("Fetching Exception in getTodaySaleSummery(): " + e.getMessage());
            return null;
        }
        database.close();

        return null;
    }

    public int getItemTotalDisc(String orderId) {
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select Sum(" + DBHelper.ORDER_CONFIRM_CHILD_DISCOUNT + ") as Total from " + DBHelper.TBL_ORDER_CONFIRM_CHILD + " Where " + DBHelper.ITEM_DELIVERY_ID + " = " + orderId + "", null);
            int total = 0;
            if (cursor != null) {
                if (cursor.moveToNext())
                    total = cursor.getInt(cursor.getColumnIndex("Total"));
                Utility.logCatMsg("Item Total " + total);
                database.close();
                return total;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            database.close();
            Utility.logCatMsg("Fetching Exception in getItemTotalDisc(): " + e.getMessage());
            return 0;
        }
        database.close();
        return 0;
    }


    public int UpdateNote(DeliveryInfo deliveryInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.ORDER_NOTE, deliveryInfo.getNote());
        values.put(DBHelper.ORDER_ISSAVE, "1");
        try {
            return db.update(DBHelper.TBL_ORDER_CONFIRM_MASTER, values, DBHelper.ORDER_CONFIRM_MASTER_ID + " = ?",
                    new String[]{String.valueOf(deliveryInfo.getDelivery_id())});
        } catch (Exception e) {
            Utility.logCatMsg("Exception in UpdateNote Method in SQLite: " + e.getMessage());
            return 0;
        }
    }

    public int UpdateIsReadStatus(DeliveryInfo deliveryInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.ORDER__ISREAD, deliveryInfo.getIsOrderRead());
        values.put(DBHelper.ORDER__NEW_UPDATE, deliveryInfo.getIsNewUpdate());
        try {
            return db.update(DBHelper.TBL_ORDER_CONFIRM_MASTER, values, DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID + " = ?",
                    new String[]{String.valueOf(deliveryInfo.getServer_Delivery_id())});
        } catch (Exception e) {
            Utility.logCatMsg("Exception in UpdateIsReadStatus Method in SQLite: " + e.getMessage());
            return 0;
        }
    }

    public int SetOrderNewUpdate(DeliveryInfo deliveryInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.ORDER__NEW_UPDATE, 1);
        try {
            return db.update(DBHelper.TBL_ORDER_CONFIRM_MASTER, values, DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID + " = ?",
                    new String[]{String.valueOf(deliveryInfo.getServer_Delivery_id())});
        } catch (Exception e) {
            Utility.logCatMsg("Exception in SetOrderNewUpdate Method in SQLite: " + e.getMessage());
            return 0;
        }
    }

    public int UpdatePODLatLng(DeliveryInfo deliveryInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.ORDER_POBLAT, deliveryInfo.getPob_lat());
        values.put(DBHelper.ORDER_POBLNG, deliveryInfo.getPob_lng());
        values.put(DBHelper.ORDER_ISSAVE, "1");
        try {
            return db.update(DBHelper.TBL_ORDER_CONFIRM_MASTER, values, DBHelper.ORDER_CONFIRM_MASTER_ID + " = ?",
                    new String[]{String.valueOf(deliveryInfo.getDelivery_id())});
        } catch (Exception e) {
            Utility.logCatMsg("Exception in UpdateNote Method in SQLite: " + e.getMessage());
            return 0;
        }
    }

    public int UpdatePercentageBill(DeliveryInfo deliveryInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.ORDER_DISCOUNT, deliveryInfo.getDiscount());
        values.put(DBHelper.ORDER_DISCOUNT_IN_PERCENTAGE, deliveryInfo.getPercentageDiscount());
        values.put(DBHelper.ORDER_NetTotal, deliveryInfo.getNetTotal());
        values.put(DBHelper.ORDER_TOTAL_AMOUNT, deliveryInfo.getTotal_Bill());
        values.put(DBHelper.ORDER_GROSS_AMOUNT, deliveryInfo.getGrossTotalBill());
        values.put(DBHelper.ORDER_ISSAVE, "1");
        try {
            return db.update(DBHelper.TBL_ORDER_CONFIRM_MASTER, values, DBHelper.ORDER_CONFIRM_MASTER_ID + " = ?",
                    new String[]{String.valueOf(deliveryInfo.getDelivery_id())});
        } catch (Exception e) {
            Utility.logCatMsg("Exception in UpdatePercentageBill Method in SQLite: " + e.getMessage());
            return 0;
        }
    }

    public int UpdateRecivedBy(DeliveryInfo deliveryInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.ORDER_RECEIVED_BY, deliveryInfo.getReceivedBy());
        values.put(DBHelper.ORDER_ISSAVE, "1");
        try {
            return db.update(DBHelper.TBL_ORDER_CONFIRM_MASTER, values, DBHelper.ORDER_CONFIRM_MASTER_ID + " = ?",
                    new String[]{String.valueOf(deliveryInfo.getDelivery_id())});
        } catch (Exception e) {
            Utility.logCatMsg("Exception in UpdateRecivedBy Method in SQLite: " + e.getMessage());
            return 0;
        }
    }

    public int updateOrderQtyMasterStatusDelivered(String deliveryId, int newQty, Float newTotalAmount) {

        Log.d("updateParameter : ", newQty + "    " + newTotalAmount);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.ORDER_TOTAL_AMOUNT, String.valueOf(newTotalAmount));
        values.put(DBHelper.ORDER_TOTAL_QTY, newQty);
        Log.d("updateDelivery", values.toString());
        try {
            return db.update(DBHelper.TBL_ORDER_CONFIRM_MASTER, values, DBHelper.ORDER_CONFIRM_MASTER_ID + " =?", new String[]{deliveryId});
        } catch (Exception e) {
            Utility.logCatMsg("Exception in updateOrderQtyMasterStatusDelivered Method in SQLite: " + e.getMessage());
            return 0;
        }
    }


    public int UpdateItemQty(DeliveryItemModel ItemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBHelper.ORDER_CONFIRM_CHILD_REJECTED_QTY, ItemModel.getRejected_Quantity());
        Utility.logCatMsg("Updated Rejected Quantity in Sqlite " + ItemModel.getRejected_Quantity());

        values.put(DBHelper.ORDER_CONFIRM_CHILD_CTN_QUANTITY, ItemModel.getCtn_qty());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_PACK_QUANTITY, ItemModel.getPac_qty());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_PCS_QUANTITY, ItemModel.getPcs_qty());

        values.put(DBHelper.ORDER_CONFIRM_CHILD_REJECT_CTN_QUANTITY, ItemModel.getReject_ctn_qty());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_REJECT_PACK_QUANTITY, ItemModel.getReject_pac_qty());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_REJECT_PCS_QUANTITY, ItemModel.getReject_pcs_qty());

        values.put(DBHelper.ORDER_CONFIRM_CHILD_DELIVER_CTN_QUANTITY, ItemModel.getDeliver_ctn_qty());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_DELIVER_PACK_QUANTITY, ItemModel.getDeliver_pac_qty());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_DELIVER_PCS_QUANTITY, ItemModel.getDeliver_pcs_qty());

        values.put(DBHelper.ORDER_CONFIRM_CHILD_FOCQTY, ItemModel.getFoc_qty());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_FOC_VALUE, ItemModel.getFoc_value());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_FOC_PERCENTAGE, ItemModel.getFoc_percentage());

        values.put(DBHelper.ORDER_CONFIRM_CHILD_DISCOUNT, ItemModel.getItem_discount());

        values.put(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_COST, ItemModel.getTotalCostPrice());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_WHOLESALE, ItemModel.getTotalwholeSalePrice());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_RETAIL, ItemModel.getTotalRetailPrice());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_NET_TOTAL_RETAIL, ItemModel.getNetTotalRetailPrice());
        // aa
        //  Total_Quantity=Total_Quantity + model.getDelivered_Quantity();
        values.put(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_QUANTITY, ItemModel.getItemTotalActualQtyInPieces());

        values.put(DBHelper.ORDER_CONFIRM_CHILD_DELIVERED_QTY, ItemModel.getItemTotalDeliverQtyInPieces());
        values.put(DBHelper.ITEM_ACTUAL_DELIVERD_Quantity, ItemModel.getItemTotalActualQtyInPieces());
        values.put(DBHelper.ITEM_REJECT_RESSON, ItemModel.getRejectReason());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_DISPLAY_PRICE, ItemModel.getDisplayPrice());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_GST_VALUE, ItemModel.getItemGstValue());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_GST_PER, ItemModel.getItemGstPer());
        values.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_ISSAVE, "1");
        values.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_TAXCODE, ItemModel.getTaxCode());

        try {
            return db.update(DBHelper.TBL_ORDER_CONFIRM_CHILD, values, DBHelper.ORDER_CONFIRM_CHILD_DETAIL_ID + " = ?",
                    new String[]{String.valueOf(ItemModel.getOrder_item_id())});
        } catch (Exception e) {
            Utility.logCatMsg("Exception in UpdateItemQty Method in SQLite: " + e.getMessage());
            return 0;
        }
    }

    public int UpdateServerItemID(DeliveryItemModel ItemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.ORDER_CONFIRM_CHILD_SERVER_DETAIL_ID, ItemModel.getServer_Item_Id());
        Utility.logCatMsg("SQLite Code.. Server Item ID " + ItemModel.getServer_Item_Id() + " Sqlite Item ID " + ItemModel.getItem_id());

        try {
            return db.update(DBHelper.TBL_ORDER_CONFIRM_CHILD, values, DBHelper.ORDER_CONFIRM_CHILD_DETAIL_ID + " = ?",
                    new String[]{String.valueOf(ItemModel.getOrder_item_id())});
        } catch (Exception e) {
            Utility.logCatMsg("Exception in UpdateItemServerID Method in SQLite: " + e.getMessage());
            return 0;
        }
    }

    public int updateSaleMode(DeliveryInfo deliveryInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.ORDER_SALES_MODE, deliveryInfo.getSalemode());
        values.put(DBHelper.ORDER_ISSAVE, "1");
        try {
            return db.update(DBHelper.TBL_ORDER_CONFIRM_MASTER, values, DBHelper.ORDER_CONFIRM_MASTER_ID + " = ?",
                    new String[]{String.valueOf(deliveryInfo.getDelivery_id())});
        } catch (Exception e) {
            Utility.logCatMsg("Exception in updateSaleMode Method in SQLite: " + e.getMessage());
            return 0;
        }
    }

    public int UpdateOrderStatus(DeliveryInfo deliveryInfo) {

        Timber.d("UpdateOrderStatus is raning");

        int Count=0;
        Count++;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.ORDER_REJECTED_REASON, deliveryInfo.getRejected_Reason());
        values.put(DBHelper.ORDER_REFUSED_REASON, deliveryInfo.getRefused_Reason());
        values.put(DBHelper.ORDER_CANCELLED_REASON, deliveryInfo.getCancelledReason());
        values.put(DBHelper.ORDER_STATUS, deliveryInfo.getDelivery_status());
        values.put(DBHelper.ORDER_PODLAT, deliveryInfo.getPod_lat());
        values.put(DBHelper.ORDER_PODLNG, deliveryInfo.getPod_lng());
        values.put(DBHelper.ORDER_ISSAVE, "1");

        Timber.d("UpdateOrderStatus Count is "+Count);

        try {
            return db.update(DBHelper.TBL_ORDER_CONFIRM_MASTER, values, DBHelper.ORDER_CONFIRM_MASTER_ID + " = ?",
                    new String[]{String.valueOf(deliveryInfo.getDelivery_id())});
        } catch (Exception e) {
            Utility.logCatMsg("Exception in UpdateRejectOrder Method in SQLite: " + e.getMessage());
            return 0;
        }



    }

    public ArrayList<MapModel> getSQLiteOrderDeliveryMapInfo(String lat, String lng, String IsNew) {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_LONGITUDE + " like '" + lng + "%' and " + DBHelper.ORDER_LATITUDE + " like '" + lat + "%' and " + DBHelper.ORDER__ISNEW + " = '" + IsNew + "'", null);
            if (cursor != null) {
                ArrayList<MapModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    MapModel model = new MapModel();
                    model.setName(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_BY)));
                    model.setLat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LATITUDE)));
                    model.setLng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LONGITUDE)));
                    list.add(model);
                }
                Utility.logCatMsg("SQLite list " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getSQLiteOrderDeliveryMapInfo(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public MapModel getSQLiteOrderDeliveryMapLatLng(int id) {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            MapModel model = new MapModel();
            //String query = "select * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " Where " + DBHelper.ORDER_CONFIRM_MASTER_ID + " = " + id + " and " + DBHelper.ORDER__ISNEW + " = '" + IS_New + "'";
            String query = "select * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " Where " + DBHelper.ORDER_CONFIRM_MASTER_ID + " = " + id + "";
            Cursor cursor = database.rawQuery(query, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {

                    model.setName(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_BY)));
                    model.setLat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LATITUDE)));
                    model.setLng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LONGITUDE)));
                    model.setDelivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_ID)));

                }
                return model;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getSQLiteOrderDeliveryMapLatLng(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public ArrayList<MapModel> getSQLiteALLOrderDeliveryMapInfo(String IsNew) {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER__ISNEW + " = '" + IsNew + "'", null);
            if (cursor != null) {
                ArrayList<MapModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    MapModel model = new MapModel();
                    model.setName(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_BY)));
                    model.setLat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LATITUDE)));
                    model.setLng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LONGITUDE)));
                    model.setDelivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_ID)));
                    list.add(model);
                }
                Utility.logCatMsg("SQLite list " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getSQLiteALLOrderDeliveryMapInfo(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public int UpdateSynChanges(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();
        values1.put(DBHelper.ORDER_ISSAVE, "2");
        values2.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_ISSAVE, "2");
        try {
            db.update(DBHelper.TBL_ORDER_CONFIRM_MASTER, values1, DBHelper.ORDER_CONFIRM_MASTER_ID + " = ?",
                    new String[]{String.valueOf(id)});
            db.update(DBHelper.TBL_ORDER_CONFIRM_CHILD, values2, DBHelper.ITEM_DELIVERY_ID + " = ?",
                    new String[]{String.valueOf(id)});
            return 1;

        } catch (Exception e) {
            Utility.logCatMsg("Exception in UpdateSynChanges Method in SQLite: " + e.getMessage());
            return 0;
        }
    }

    public void insertCompanyCustomer(ArrayList<RegisterdCustomerModel> list, String IsNew) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            for (int i = 0; i < list.size(); i++) {
                RegisterdCustomerModel model = list.get(i);
                ContentValues values = new ContentValues();

                if (IsNew.trim().equals("False")) {
                    values.put(DBHelper.CUSTOMER_ID, model.getCustomer_id());
                } else {
                    values.put(DBHelper.CUSTOMER_ID, getMaxRegCustomerId());
                }

                values.put(DBHelper.CUSTOMER_NAME, model.getName());
                values.put(DBHelper.CUSTOMER_SALES_MODE, model.getSalesMode());
                values.put(DBHelper.CUSTOMER_TYPE_ID,model.getContactTypeId());
                values.put(DBHelper.CUSTOMER_ImageName, model.getImageName());
                values.put(DBHelper.C_ADDRESS, model.getAddress());
                values.put(DBHelper.C_ADDRESS1, model.getAddress1());
                values.put(DBHelper.C_PHONE, model.getPhone());
                values.put(DBHelper.C_CELL, model.getCell());
                values.put(DBHelper.C_LATITUDE, model.getLat());
                values.put(DBHelper.C_LONGITUDE, model.getLng());
                values.put(DBHelper.C_City, model.getCity());
                values.put(DBHelper.C_Country, model.getCountry());
                values.put(DBHelper.C_ISSAVE, "0");
                values.put(DBHelper.C_ISNEW, IsNew);
                values.put(DBHelper.C_ROUTE_ID, model.getRoute());
                values.put(DBHelper.c_CODE, model.getCode());
                database.insert(DBHelper.TBL_REGISTER_CUSTOMER, null, values);

            }
            Utility.logCatMsg(list.size() + " Company Customer Rows Entered ");
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertCompanyItems()");
        }
        database.close();
    }

    public String getRouteName(int routeId) {
        SQLiteDatabase database = this.getReadableDatabase();
        String routeTitle = "";
        String query = "select * from " + DBHelper.TBL_Route + " where " + DBHelper.ROUTE_ID + " = " + routeId;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            routeTitle = cursor.getString(cursor.getColumnIndex(DBHelper.ROUTE_TITLE));
        }
        cursor.close();
        return routeTitle;
    }

    public ArrayList<RegisterdCustomerModel> getSQLiteRegisterCustomerInfo(String routeId) {
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            String q = null;
            if (routeId == null) {
                q = "select * from " + DBHelper.TBL_REGISTER_CUSTOMER;

            } else {
                q = "select * from " + DBHelper.TBL_REGISTER_CUSTOMER + " Where " + DBHelper.C_ROUTE_ID + " = " + routeId;
                Timber.d("select * from " + DBHelper.TBL_REGISTER_CUSTOMER + " Where " + DBHelper.C_ROUTE_ID + " = " + routeId);
            }
            Cursor cursor = database.rawQuery(q, null);
            if (cursor != null) {
                ArrayList<RegisterdCustomerModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    RegisterdCustomerModel model = new RegisterdCustomerModel();
                    model.setCustomer_id(cursor.getInt(cursor.getColumnIndex(DBHelper.CUSTOMER_ID)));
                    model.setContactTypeId(cursor.getInt(cursor.getColumnIndex(DBHelper.CUSTOMER_TYPE_ID)));
                    model.setSalesMode(cursor.getString(cursor.getColumnIndex(DBHelper.CUSTOMER_SALES_MODE)));
                    model.setName(cursor.getString(cursor.getColumnIndex(DBHelper.CUSTOMER_NAME)));
                    model.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.C_ADDRESS)));
                    model.setAddress1(cursor.getString(cursor.getColumnIndex(DBHelper.C_ADDRESS1)));
                    model.setCell(cursor.getString(cursor.getColumnIndex(DBHelper.C_CELL)));
                    model.setPhone(cursor.getString(cursor.getColumnIndex(DBHelper.C_PHONE)));
                    model.setCity(cursor.getString(cursor.getColumnIndex(DBHelper.C_City)));
                    model.setCountry(cursor.getString(cursor.getColumnIndex(DBHelper.C_Country)));
                    model.setLat(cursor.getString(cursor.getColumnIndex(DBHelper.C_LATITUDE)));
                    model.setLng(cursor.getString(cursor.getColumnIndex(DBHelper.C_LONGITUDE)));
                    model.setImageName(cursor.getString(cursor.getColumnIndex(DBHelper.CUSTOMER_ImageName)));
                    model.setIsSave(cursor.getString(cursor.getColumnIndex(DBHelper.C_ISSAVE)));
                    model.setRoute(cursor.getInt(cursor.getColumnIndex(DBHelper.C_ROUTE_ID)));
                    model.setCode(cursor.getString(cursor.getColumnIndex(DBHelper.c_CODE)));
                    list.add(model);
                }
                Utility.logCatMsg("SQLite delivery list size " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getSQLiteOrderDeliveryItems(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }


    public ArrayList<CustomerVisitedModel> getAllVisitDetails() {

        SQLiteDatabase database = this.getReadableDatabase();
        String q = null;
        q = "select * from " + DBHelper.TBL_VisitedDetails;

        Timber.d("query : " + q);

        Cursor cursor = database.rawQuery(q, null);
        if (cursor != null) {
            ArrayList<CustomerVisitedModel> list = new ArrayList<>();
            while (cursor.moveToNext()) {

                CustomerVisitedModel model = new CustomerVisitedModel();
                model.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.vID)));
                model.setCustomerName(cursor.getString(cursor.getColumnIndex(DBHelper.vCustomerName)));
                model.setCustomerId(cursor.getInt(cursor.getColumnIndex(DBHelper.vCustomerId)));
                model.setRouteName(cursor.getString(cursor.getColumnIndex(DBHelper.vRouteName)));
                model.setRouteID(cursor.getInt(cursor.getColumnIndex(DBHelper.vRouteID)));
                model.setVisitStatus(cursor.getString(cursor.getColumnIndex(DBHelper.vStatusName)));
                model.setStatusID(cursor.getInt(cursor.getColumnIndex(DBHelper.vStatus)));
                model.setVisitDate(cursor.getString(cursor.getColumnIndex(DBHelper.vVisitDate)));
                model.setLatitude(cursor.getString(cursor.getColumnIndex(DBHelper.vLatitude)));
                model.setLongitude(cursor.getString(cursor.getColumnIndex(DBHelper.vLongitude)));
                model.setVisitTime(cursor.getString(cursor.getColumnIndex(DBHelper.vVisitTime)));
                model.setImageName(cursor.getString(cursor.getColumnIndex(DBHelper.vImageName)));
                model.setSalesmanID(cursor.getInt(cursor.getColumnIndex(DBHelper.vSalesmanID)));
                model.setIsSync(cursor.getInt(cursor.getColumnIndex(DBHelper.vIsSync)));
                list.add(model);
            }

            Utility.logCatMsg("SQLite delivery list size " + list.size());
            return list;
        } else {
            Utility.logCatMsg("*****NULL CURSOR******");
            database.close();
            return null;
        }

    }


    public ArrayList<RegisterdCustomerModel> getAllRegisterCustomers() {

        SQLiteDatabase database = this.getReadableDatabase();
        String q = null;
        q = "select * from " + DBHelper.TBL_REGISTER_CUSTOMER;

        Timber.d("Customer Query is "+q);

        Cursor cursor = database.rawQuery(q, null);
        if (cursor != null) {
            ArrayList<RegisterdCustomerModel> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                RegisterdCustomerModel model = new RegisterdCustomerModel();
                model.setCustomer_id(cursor.getInt(cursor.getColumnIndex(DBHelper.CUSTOMER_ID)));
                model.setSalesMode(cursor.getString(cursor.getColumnIndex(DBHelper.CUSTOMER_SALES_MODE)));
                model.setName(cursor.getString(cursor.getColumnIndex(DBHelper.CUSTOMER_NAME)));
                model.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.C_ADDRESS)));
                model.setAddress1(cursor.getString(cursor.getColumnIndex(DBHelper.C_ADDRESS1)));
                model.setCell(cursor.getString(cursor.getColumnIndex(DBHelper.C_CELL)));
                model.setPhone(cursor.getString(cursor.getColumnIndex(DBHelper.C_PHONE)));
                model.setCity(cursor.getString(cursor.getColumnIndex(DBHelper.C_City)));
                model.setCountry(cursor.getString(cursor.getColumnIndex(DBHelper.C_Country)));
                model.setLat(cursor.getString(cursor.getColumnIndex(DBHelper.C_LATITUDE)));
                model.setLng(cursor.getString(cursor.getColumnIndex(DBHelper.C_LONGITUDE)));
                model.setImageName(cursor.getString(cursor.getColumnIndex(DBHelper.CUSTOMER_ImageName)));
                model.setIsSave(cursor.getString(cursor.getColumnIndex(DBHelper.C_ISSAVE)));
                model.setRoute(cursor.getInt(cursor.getColumnIndex(DBHelper.C_ROUTE_ID)));
                model.setCode(cursor.getString(cursor.getColumnIndex(DBHelper.c_CODE)));
                list.add(model);
            }

            Utility.logCatMsg("SQLite delivery list size " + list.size());
            return list;
        } else {
            Utility.logCatMsg("*****NULL CURSOR******");
            database.close();
            return null;
        }

    }


    public int UpdateImagesStatusToSycned(ArrayList<ImagesModel> ImagesList) {
        int a = 0;

        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues Images = new ContentValues();
        for (int i = 0; i < ImagesList.size(); i++) {

            Images.put(DBHelper.IMG_IS_SYNCED, "1");
            database.update(DBHelper.TBL_IMAGES, Images, DBHelper.IMG_ID + "=" + ImagesList.get(i).getImage_id(), null);
            a = 1;
        }
        return a;

    }

    public ArrayList<DeliveryItemModel> getAllItems() {
        SQLiteDatabase database = this.getReadableDatabase();

        String q = null;
        q = "select * from " + DBHelper.TBL_PLN_ITEMS;
        Log.d("selectitem:", q + "");
        Cursor cursor = database.rawQuery(q, null);
        if (cursor != null) {
            ArrayList<DeliveryItemModel> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                DeliveryItemModel model = new DeliveryItemModel();
                model.setItem_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_ID)));
                model.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_TITLE)));
                model.setCode(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_CODE)));
                model.setName(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_NAME)));
                model.setItemDetail(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_DETAIL)));
                model.setCostCtnPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_CORTON)));
                model.setCostPackPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_PACK)));
                model.setCostPiecePrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_PIECES)));
                model.setRetailPiecePrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_WS_CORTON)));
                model.setWSPackPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_WS_PACK)));
                model.setWSPiecePrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_WS_PIECES)));
                model.setRetailCtnPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_RETAIL_CORTON)));
                model.setRetailPackPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_RETAIL_PACK)));
                model.setWSCtnPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_RETAIL_PIECES)));
                model.setDisplayPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_DISPLAY)));
                model.setPackSize(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_PACK_SIZE)));
                model.setCtnSize(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_CTN_SIZE)));
                model.setItemGstPer(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_GST_PER)));
                model.setItemGstValue(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_GST_VALUE)));
                model.setImageName(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_IMAGE_NAME)));
                model.setSKU(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_SKU)));
                model.setEmptyFlag(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_EMPTY_BOTTLE))));
                model.setTaxCode(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_TAX_CODE)));

                list.add(model);

            }


            Utility.logCatMsg("SQLite delivery list size " + list.size());
            return list;
        } else {
            Utility.logCatMsg("*****NULL CURSOR******");
            database.close();
            return null;
        }

    }


    public String getCustomerName(int Id) {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            String q = "select * from " + DBHelper.TBL_REGISTER_CUSTOMER + " Where " + DBHelper.CUSTOMER_ID + " = " + Id;

            Cursor cursor = database.rawQuery(q, null);
            if (cursor != null) {

                while (cursor.moveToNext())
                    return cursor.getString(cursor.getColumnIndex(DBHelper.CUSTOMER_NAME));
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getCustomerName(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }


    public Boolean hCheckIfCustomerVisited(int Id) {

        SQLiteDatabase database = this.getReadableDatabase();
        boolean hIsVisited = false;
        try {
            Utility.logCatMsg("SQLite code ");


            String q = "SELECT " + DBHelper.vCustomerId + " FROM " + DBHelper.TBL_VisitedDetails + " WHERE " + DBHelper.vCustomerId + " = " + Id;

            Cursor cursor = database.rawQuery(q, null);
            List<String> hCountList = new ArrayList<>();

            if (cursor != null) {

                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(DBHelper.vCustomerId));
                    hCountList.add(id);

                    if (!hCountList.isEmpty()) {
                        hIsVisited = true;
                        break;
                    }
                }
                database.close();
                return hIsVisited;
            } else {
                database.close();
                return hIsVisited;
            }
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getCustomerName(): " + e.getMessage());
            return hIsVisited;
        }
    }


    public Boolean rCheckIfCustomerVisitedByToday(int Id, String date) {

        SQLiteDatabase database = this.getReadableDatabase();
        boolean hIsVisited = false;
        try {
            Utility.logCatMsg("SQLite code ");

            String q = "SELECT " + DBHelper.vCustomerId + " FROM " + DBHelper.TBL_VisitedDetails + " WHERE " + DBHelper.vCustomerId + " = " + Id
                    + " and " + DBHelper.vVisitDate + " = '" + date+"'";

            Cursor cursor = database.rawQuery(q, null);
            List<String> hCountList = new ArrayList<>();

            if (cursor != null) {

                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(DBHelper.vCustomerId));
                    hCountList.add(id);

                    if (!hCountList.isEmpty()) {
                        hIsVisited = true;
                        break;
                    }
                }
                database.close();
                return hIsVisited;
            } else {
                database.close();
                return hIsVisited;
            }
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getCustomerName(): " + e.getMessage());
            return hIsVisited;
        }
    }


    public RegisterdCustomerModel getCustomerById(int id) {
        RegisterdCustomerModel model = new RegisterdCustomerModel();
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            String q = "select * from " + DBHelper.TBL_REGISTER_CUSTOMER + " Where " + DBHelper.CUSTOMER_ID + " = " + id;

            Cursor cursor = database.rawQuery(q, null);

            if (cursor != null) {
                cursor.moveToFirst();
                model.setCustomer_id(cursor.getInt(cursor.getColumnIndex(DBHelper.CUSTOMER_ID)));
                model.setName(cursor.getString(cursor.getColumnIndex(DBHelper.CUSTOMER_NAME)));
                model.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.C_ADDRESS)));
                model.setAddress1(cursor.getString(cursor.getColumnIndex(DBHelper.C_ADDRESS1)));
                model.setSalesMode(cursor.getString(cursor.getColumnIndex(DBHelper.CUSTOMER_SALES_MODE)));
                model.setCell(cursor.getString(cursor.getColumnIndex(DBHelper.C_CELL)));
                model.setPhone(cursor.getString(cursor.getColumnIndex(DBHelper.C_PHONE)));
                model.setCity(cursor.getString(cursor.getColumnIndex(DBHelper.C_City)));
                model.setCountry(cursor.getString(cursor.getColumnIndex(DBHelper.C_Country)));
                model.setLat(cursor.getString(cursor.getColumnIndex(DBHelper.C_LATITUDE)));
                model.setLng(cursor.getString(cursor.getColumnIndex(DBHelper.C_LONGITUDE)));
                model.setIsSave(cursor.getString(cursor.getColumnIndex(DBHelper.C_ISSAVE)));
                model.setRoute(cursor.getInt(cursor.getColumnIndex(DBHelper.C_ROUTE_ID)));
                model.setCode(cursor.getString(cursor.getColumnIndex(DBHelper.c_CODE)));

                return model;

            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getCustomerName(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public int getLastRowId(String tn) {
//        SELECT rowid from your_table_name order by ROWID DESC limit 1

        int lastId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT ROWID from " + tn + " order by ROWID DESC limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            lastId = (int) c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }
        return lastId;
    }


    public CustomerPriceModel getCustomerPriceByid(int customerid, int itemid) {
        SQLiteDatabase database = this.getReadableDatabase();
        CustomerPriceModel priceModel = new CustomerPriceModel();
        try {

            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_Customer_Price +
                    " Where " + DBHelper.CustomerPrice_CustomerId
                    + " = " + customerid + " and " + DBHelper.CustomerPrice_ItemId + " = '" + itemid + "'", null);

            Log.d("customerpricequery=>", "select * from " + DBHelper.TBL_Customer_Price +
                    " Where " + DBHelper.CustomerPrice_CustomerId
                    + " = " + customerid + " and " + DBHelper.CustomerPrice_ItemId + " = '" + itemid + "'");

            if (cursor != null) {
                cursor.moveToFirst();
                priceModel.setCustomerId(cursor.getInt(cursor.getColumnIndex(DBHelper.CustomerPrice_CustomerId)));
                priceModel.setItemId(cursor.getInt(cursor.getColumnIndex(DBHelper.CustomerPrice_ItemId)));
                priceModel.setPrice(cursor.getInt(cursor.getColumnIndex(DBHelper.CustomerPrice_Price)));
            }

        } catch (Exception e) {
            Utility.logCatMsg(" Failed in method getCustomerPriceByid()...ItemInfo = " + e.toString()
                    + "er=" + e.getMessage());
        }

        return priceModel;
    }


    public Boolean insertCustomerPrice(List<CustomerPriceModel> priceModelList) {
        SQLiteDatabase database = this.getWritableDatabase();
        boolean inserted = true;



        try {

            for (int i = 0; i < priceModelList.size(); i++)
            {

                CustomerPriceModel priceModel = priceModelList.get(i);
                ContentValues values = new ContentValues();
                values.put(DBHelper.CustomerPrice_CustomerId, priceModel.getCustomerId());
                values.put(DBHelper.CustomerPrice_ItemId, priceModel.getItemId());
                values.put(DBHelper.CustomerPrice_Price, priceModel.getPrice());
                long id = database.insert(DBHelper.TBL_Customer_Price, null, values);

            }
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertCustomerPrice()...error= " + e.toString()
                    + "er=" + e.getMessage());
            inserted = false;
            Log.d("dbCheck", "insert CustomerPrice insetion false " + inserted);
        }
        return inserted;
    }

    public void insertRunTimeOrderDetails(ArrayList<DeliveryItemModel> list, RegisterdCustomerModel RS_model, String isUrgent) {


        SQLiteDatabase database = this.getWritableDatabase();

        int count = getMaxItemId();
        int count2 = 0;
        int Total_Quantity = 0;
        int maxId = getMaxDeliverId() + 1;
        float gst = 0f;

        String dateTime = Utility.getDateForOrder()+" "+Utility.getTimeUpdated();


        Utility.logCatMsg("insetruntime lsize " + list.size());
        try {
            for (int i = 0; i < list.size(); i++) {
                DeliveryItemModel model = list.get(i);

                Timber.d("Old The NettTotal Retail Price is "+list.get(i).getNetTotalRetailPrice());

                float updatedNetTotal = list.get(i).getCtn_qty()*list.get(i).getRetailPiecePrice();

                list.get(i).setNetTotalRetailPrice(updatedNetTotal);

                Timber.d("New The NettTotal Retail Price is => "+updatedNetTotal);

                gst = gst + Math.abs(model.getItemGstValue());

                if (model.getSelectedValue() != null)
                    if (!model.getSelectedValue().equals("0")) {

                        ContentValues values = new ContentValues();

                        if (RS_model.getServerOrderId() != 0) {
                            values.put(DBHelper.ORDER_CONFIRM_CHILD_DETAIL_ID, model.getOrder_item_id());
                            values.put(DBHelper.ORDER_CONFIRM_MASTER_DETAIL_ID, RS_model.getServerOrderId());
                            values.put(DBHelper.ITEM_DELIVERY_ID, RS_model.getServerOrderId());

                        } else {
                            values.put(DBHelper.ORDER_CONFIRM_CHILD_DETAIL_ID, ++count);
                            values.put(DBHelper.ORDER_CONFIRM_MASTER_DETAIL_ID, maxId);
                            values.put(DBHelper.ITEM_DELIVERY_ID, getMaxDeliverId() + 1);

                        }


                        values.put(DBHelper.ORDER_CONFIRM_CHILD_SERVER_DETAIL_ID, model.getServer_Item_Id());
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_NAME, model.getName());
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_ID, model.getItem_id());

                        //For Discount Policy Id on each item...
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_DISCOUNT_POLICY_ID, model.getDiscountPolicyId());
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_DISCOUNT_POLICY_AMNT,model.getDiscountPolicyValue());
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_DISCOUNT_PERCENTAGE,model.getDiscPercentage());

                        //ForBonusPolicy
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_BONUS_POLICY_ID, model.getBonusPolicyId()); //bonusPolicyId
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_BONUS_ITEM_NAME,model.getBonusItemName()); //Extra
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_BONUS_ITEM_ID,model.getBonusIncentiveItemId()); //BonusitemId
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_BONUS_QTY,model.getBonusQty()); //bonusQty
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_BONUS_GST,model.getBonusItemsGst()); //gst done


                        values.put(DBHelper.ORDER_CONFIRM_CHILD_COST_CTN_PRICE, Math.abs(model.getCostCtnPrice()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_WHOLESALE_CTN_PRICE, Math.abs(model.getRetailPiecePrice()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_RETAIL_CTN_PRICE, Math.abs(model.getRetailCtnPrice()));

                        values.put(DBHelper.ORDER_CONFIRM_CHILD_COST_PACK_PRICE, Math.abs(model.getCostPackPrice()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_WHOLESALE_PACK_PRICE, Math.abs(model.getWSPackPrice()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_RETAIL_PACK_PRICE, Math.abs(model.getRetailPackPrice()));

                        values.put(DBHelper.ORDER_CONFIRM_CHILD_COST_PIECES_PRICE, Math.abs(model.getCostPiecePrice()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_WHOLESALE_PIECES_PRICE, Math.abs(model.getWSPiecePrice()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_RETAIL_PIECES_PRICE, Math.abs(model.getWSCtnPrice()));

                        values.put(DBHelper.ORDER_CONFIRM_CHILD_CTN_QUANTITY, Math.abs(model.getCtn_qty()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_PACK_QUANTITY, Math.abs(model.getPac_qty()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_PCS_QUANTITY, Math.abs(model.getPcs_qty()));


                        values.put(DBHelper.ORDER_CONFIRM_CHILD_REJECT_CTN_QUANTITY, "0");
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_REJECT_PACK_QUANTITY, "0");
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_REJECT_PCS_QUANTITY, "0");

                        values.put(DBHelper.ORDER_CONFIRM_CHILD_DELIVER_CTN_QUANTITY, Math.abs(model.getCtn_qty()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_DELIVER_PACK_QUANTITY, Math.abs(model.getPac_qty()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_DELIVER_PCS_QUANTITY, Math.abs(model.getPcs_qty()));

                        values.put(DBHelper.ORDER_CONFIRM_CHILD_FOCTYPE, model.getFocType());
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_FOCQTY, Math.abs(model.getFoc_qty()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_FOC_VALUE, Math.abs(model.getFoc_value()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_FOC_PERCENTAGE, Math.abs(model.getFoc_percentage()));

                        values.put(DBHelper.ORDER_CONFIRM_CHILD_DISCOUNT, Math.abs(model.getItem_discount()));
                        Utility.logCatMsg("Item Discount " + model.getItem_discount());

                        values.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_EMPTY_BOTTLE, Math.abs(model.getEmptyBottles()));
                        Log.d("addEmptyBottles", model.getTitle() + "   " + model.getEmptyBottles());

                        values.put(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_COST, Math.abs(model.getTotalCostPrice()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_WHOLESALE, Math.abs(model.getTotalwholeSalePrice()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_RETAIL, Math.abs(model.getTotalRetailPrice()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_NET_TOTAL_RETAIL, Math.abs(model.getNetTotalRetailPrice()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_ROUTE_ID, RS_model.getRoute());
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_CATEGORY_ID, 0);
                        Total_Quantity = Total_Quantity + model.getDelivered_Quantity();
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_TOTAL_QUANTITY, Math.abs(model.getItemTotalDeliverQtyInPieces()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_REJECTED_QTY, 0);
                        values.put(DBHelper.ITEM_RETURN_Quantity, 0);
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_DELIVERED_QTY, Math.abs(model.getItemTotalDeliverQtyInPieces()));
                        values.put(DBHelper.ITEM_ACTUAL_DELIVERD_Quantity, Math.abs(model.getItemTotalDeliverQtyInPieces()));
                        values.put(DBHelper.ITEM_REJECT_RESSON, "");
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_DISPLAY_PRICE, Math.abs(model.getDisplayPrice()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_GST_VALUE, Math.abs(model.getItemGstValue()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_GST_PER, Math.abs(model.getItemGstPer()));
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_ISSAVE, "1");
                        values.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_ISNEW, "True");

                        values.put(DBHelper.ORDER_CONFIRM_CHILD_ITEM_TAXCODE, model.getTaxCode());

                        long id = database.insert(DBHelper.TBL_ORDER_CONFIRM_CHILD, null, values);
                        count2++;
                    }
            }
            Utility.logCatMsg(count2 + " Items Rows Entered -insetruntime");
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertRunTimeOrderDetails()...ItemInfo = " + e.toString()
                    + "er=" + e.getMessage());
        }
        try {

            ContentValues values = new ContentValues();
            if (RS_model.getServerOrderId() != 0) {

                values.put(DBHelper.ORDER_CONFIRM_MASTER_ID, RS_model.getServerOrderId());
                values.put(DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID, RS_model.getServerOrderId());
                values.put(DBHelper.ORDER_CONFIRM_MASTER_From_Server, 1);
            } else {
                values.put(DBHelper.ORDER_CONFIRM_MASTER_ID, maxId);
                values.put(DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID, maxId);
                values.put(DBHelper.ORDER_CONFIRM_MASTER_From_Server, 0);
            }

            Utility.logCatMsg(RS_model.getDate().trim() + " " + Utility.getCurrentDate().trim());
            values.put(DBHelper.ORDER_STATUS, RS_model.getDeliveryStatus());
            values.put(DBHelper.ORDER_DESCRIPTION, "");


            //Altering for Positive..
            Log.d("totalqtydb", "=" + Math.abs(Total_Quantity));
            values.put(DBHelper.ORDER_TOTAL_QTY, Math.abs(Total_Quantity));
            //values.put(DBHelper.ORDER_TOTAL_QTY, Total_Quantity);



            Timber.d("The New DataTime Setting is "+dateTime);

            values.put(DBHelper.ORDER_DELIVERY_DATE, dateTime);
            values.put(DBHelper.ORDER_START_TIME, "");
            values.put(DBHelper.ORDER_END_TIME, "");
            values.put(DBHelper.ORDER_DELIVERY_ADDRESS, RS_model.getAddress() + " " + RS_model.getCity() + " " + RS_model.getCountry());
            values.put(DBHelper.DELIVERY_TO_MOBILE, RS_model.getCell());
            values.put(DBHelper.ORDER_BY, RS_model.getName());
            values.put(DBHelper.ORDER_IS_REJECT, "");
            values.put(DBHelper.ORDER_RECEIVED_BY, "");
            values.put(DBHelper.ORDER_CUSTOMERID, RS_model.getCustomer_id());
            values.put(DBHelper.ORDER_PODLAT, RS_model.getPodLat());
            values.put(DBHelper.ORDER_PODLNG, RS_model.getPodLng());
            values.put(DBHelper.ORDER_POBLAT, RS_model.getPobLat());
            values.put(DBHelper.ORDER_POBLNG, RS_model.getPobLng());
            values.put(DBHelper.ORDER_NOTE, "");
            values.put(DBHelper.ORDER_LATITUDE, RS_model.getLat());
            values.put(DBHelper.ORDER_LONGITUDE, RS_model.getLng());


            values.put(DBHelper.ORDER_DISTRIBUTOR_ID, RS_model.getDistributorId());
            values.put(DBHelper.ORDER_SUB_DISTRIBUTOR_ID, RS_model.getSubDistributorId());

            //Altering for Converting Negative Value to Positive
            values.put(DBHelper.ORDER_TOTAL_AMOUNT, Math.abs(Double.valueOf(RS_model.getTotalbill())));

            //Altering For Positive...
            values.put(DBHelper.ORDER_GROSS_AMOUNT, Math.abs(Double.valueOf(RS_model.getGrossTotal())));
            values.put(DBHelper.ORDER_DISCOUNT_IN_PERCENTAGE, RS_model.getPercentage_discount());
            if (RS_model.getDiscount() == null)
                values.put(DBHelper.ORDER_DISCOUNT, 0);
            else
                values.put(DBHelper.ORDER_DISCOUNT, RS_model.getDiscount());

            //Altering for Making Values Positive...
            values.put(DBHelper.ORDER_NetTotal, Math.abs(Double.valueOf(RS_model.getNetTotal())));
            values.put(DBHelper.ORDER_EMPLOYEEID, 0);
            values.put(DBHelper.ORDER_VEHICLEID, 0);
            values.put(DBHelper.ORDER_NO, RS_model.getOrderNumber());
            values.put(DBHelper.ORDER_SERIAL_NO, RS_model.getSerialNo());
            //  values.put(DBHelper. ORDER_DATE_TIME,"2016-08-22 14:45:06.293");
            values.put(DBHelper.ORDER_DATE_TIME, dateTime);


            values.put(DBHelper.ORDER_SALES_MODE, RS_model.getSalesMode());
            Timber.d("Sales Mode in db " + RS_model.getSalesMode().toString());

            //Deposited Bank id Assignment work
            values.put(DBHelper.ORDER_CASH_DEPOSITED_BANK_ID, RS_model.getCashDepositedBankId());
            values.put(DBHelper.URGENT_ORDER_STATUS, isUrgent);

            values.put(DBHelper.ORDER_REFUSED_REASON, "");
            values.put(DBHelper.ORDER_CANCELLED_REASON, "");
            values.put(DBHelper.ORDER_REJECTED_REASON, "");
            values.put(DBHelper.ORDER_ROUTE_ID, RS_model.getRoute());
            values.put(DBHelper.ORDER_CATEGORY_ID, 0);

            values.put(DBHelper.ORDER_ISSAVE, "1");
            values.put(DBHelper.ORDER__ISNEW, "True");
            values.put(DBHelper.ORDER__ISREAD, 1);
            values.put(DBHelper.ORDER__NEW_UPDATE, 0);

            Utility.logCatMsg("GST vale " + gst);

            values.put(DBHelper.ORDER_CONFIRM_CHILD_GST_VALUE, gst);

            //todo summary
            database.insert(DBHelper.TBL_ORDER_CONFIRM_MASTER, null, values);

            Utility.logCatMsg("1 User Info Row Entered");
            Toast.makeText(context, "Record Inserted Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertRunTimeOrderDetails()...userInfo");
        }

        database.close();
    }

    private int getMaxItemId() {
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select max( " + DBHelper.ORDER_CONFIRM_CHILD_DETAIL_ID + " ) from " + DBHelper.TBL_ORDER_CONFIRM_CHILD, null);
            if (cursor != null) {
                cursor.moveToFirst();
                Utility.logCatMsg("Max Item ID " + cursor.getInt(0) + "");
                int a = cursor.getInt(0);
                return a;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getMaxItemId(): " + e.getMessage());
            return -1;
        }
        database.close();
        return -1;
    }

    private int getMaxDeliverId() {
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select max( " + DBHelper.ORDER_CONFIRM_MASTER_ID + " ) from " + DBHelper.TBL_ORDER_CONFIRM_MASTER, null);
            if (cursor != null) {
                cursor.moveToFirst();
                Utility.logCatMsg("Max DeliverID " + cursor.getInt(0) + "");
                int a = cursor.getInt(0);
                return a;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getMaxItemId(): " + e.getMessage());
            return -1;
        }
        database.close();
        return -1;
    }

    private int getMaxRegCustomerId() {
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select max( " + DBHelper.CUSTOMER_ID + " ) from " + DBHelper.TBL_REGISTER_CUSTOMER, null);
            if (cursor != null) {
                cursor.moveToFirst();
                Utility.logCatMsg("Max CUSTOMER_ID " + cursor.getInt(0) + "");
                int a = cursor.getInt(0);
                return a;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getMaxRegCustomerId(): " + e.getMessage());
            return -1;
        }
        database.close();
        return -1;
    }


    public int deleteOrderItems(String delivery_id, String IsNew) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + DBHelper.TBL_ORDER_CONFIRM_CHILD + " WHERE " + DBHelper.ITEM_DELIVERY_ID + " = " + Integer.parseInt(delivery_id) + " and " + DBHelper.C_ISNEW + " = '" + IsNew + "'"); //delete  row in a table with the condition
            db.close();
            return 1;
        } catch (Exception e) {
            Utility.logCatMsg("Exception in deleteRunTimeOrder Method" + e);
            return 0;
        }

    }


    public boolean deleteOrder(String delivery_id, String IsNew) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " WHERE " + DBHelper.ORDER_CONFIRM_MASTER_ID + " = " + Integer.parseInt(delivery_id) + " and " + DBHelper.ORDER__ISNEW + " = '" + IsNew + "'"); //delete  row in a table with the condition
            db.execSQL("DELETE FROM " + DBHelper.TBL_ORDER_CONFIRM_CHILD + " WHERE " + DBHelper.ITEM_DELIVERY_ID + " = " + Integer.parseInt(delivery_id) + " and " + DBHelper.C_ISNEW + " = '" + IsNew + "'"); //delete  row in a table with the condition
            db.close();
            Log.d("orderDelted", "1");
            Log.d("orderDelted", "DELETE FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " WHERE " + DBHelper.ORDER_CONFIRM_MASTER_ID + " = " + Integer.parseInt(delivery_id) + " and " + DBHelper.ORDER__ISNEW + " = '" + IsNew + "'");
            Log.d("orderDelted", "DELETE FROM " + DBHelper.TBL_ORDER_CONFIRM_CHILD + " WHERE " + DBHelper.ITEM_DELIVERY_ID + " = " + Integer.parseInt(delivery_id) + " and " + DBHelper.C_ISNEW + " = '" + IsNew + "'");
            return true;
        } catch (Exception e) {
            Utility.logCatMsg("Exception in deleteRunTimeOrder Method" + e);
            return false;
        }

    }

    public void deletePlanOrderTableData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Delete  from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER__ISNEW + " = 'False'");
        db.execSQL("Delete  from " + DBHelper.TBL_ORDER_CONFIRM_CHILD + " where " + DBHelper.ORDER_CONFIRM_CHILD_ITEM_ISNEW + " = 'False'");
        Utility.logCatMsg("Delete data PlanOrderTables ");
    }

    public boolean CheckOrderDeliveryExit(String server_deliver_id, String IsNew) {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select " + DBHelper.ORDER_CONFIRM_MASTER_ID + "  from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID + " = '" + server_deliver_id + "'" + " and " + DBHelper.ORDER__ISNEW + " = '" + IsNew + "'", null);
            if (cursor != null) {

                cursor.moveToFirst();
                Utility.logCatMsg("Query Server Deliverd Id " + server_deliver_id + " Tag " + IsNew);

                if (cursor.getCount() > 0) {
                    //Utility.logCatMsg(" Deliverd Id  from table" + cursor.getInt(cursor.getColumnIndex(DBHelper.TBL_ORDER_CONFIRM_MASTER)));
                    return true;
                } else {
                    Utility.logCatMsg("Not found");
                    return false;
                }
            } else {
                Utility.logCatMsg("*****NULL CURSOR******");
                return false;
            }
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in CheckOrderDeliveryExit(): " + e.getMessage());
            return false;
        }

    }


    public ArrayList<RouteModel> getCompanyRoute() {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_Route, null);
            if (cursor != null) {
                ArrayList<RouteModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    RouteModel model = new RouteModel();
                    model.setRoute_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ROUTE_ID)));
                    model.setRoute_name(cursor.getString(cursor.getColumnIndex(DBHelper.ROUTE_TITLE)));
                    model.setRoute_code(cursor.getString(cursor.getColumnIndex(DBHelper.ROUTE_CODE)));
                    model.setRoute_description(cursor.getString(cursor.getColumnIndex(DBHelper.ROUTE_DESCRIPTION)));
                    list.add(model);
                }
                Utility.logCatMsg("SQLite Route list size " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getCompanyRoute(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }


    //Assignment work By Yaseen
    public ArrayList<CompanyWiseBanksModel> GetCompanyWiseBankDetails() {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("Select Distinct * from " + DBHelper.TBL_COMPANY_WISE_BANKS, null);
            if (cursor != null) {
                ArrayList<CompanyWiseBanksModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    CompanyWiseBanksModel model = new CompanyWiseBanksModel();
                    model.setBankID(cursor.getInt(cursor.getColumnIndex(DBHelper.BANK_ID)));
                    model.setBankName(cursor.getString(cursor.getColumnIndex(DBHelper.BANK_NAME)));
                    model.setBankAccountNbr(cursor.getString(cursor.getColumnIndex(DBHelper.BANK_ACCOUNT_NBR)));
                    model.setBankAccountType(cursor.getString(cursor.getColumnIndex(DBHelper.BANK_ACCOUNT_TYPE)));
                    model.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.BANK_ADDRESS)));
                    list.add(model);
                }
                Utility.logCatMsg("SQLite Route list size " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getCompanyRoute(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }






    public ArrayList<VisitStatusesModel> rGetVisitStatusList() {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("Select Distinct * from " + DBHelper.TBL_Visited_Statuses, null);
            if (cursor != null) {
                ArrayList<VisitStatusesModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    VisitStatusesModel model = new VisitStatusesModel();

                    model.setStatusID(cursor.getInt(cursor.getColumnIndex(DBHelper.Status_ID)));
                    model.setVisitStatus(cursor.getString(cursor.getColumnIndex(DBHelper.Visit_Status)));
                    list.add(model);
                }
                Utility.logCatMsg("SQLite Route list size " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getCompanyRoute(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }


    public void insertCompanyRoute(ArrayList<RouteModel> list) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            for (int i = 0; i < list.size(); i++) {
                RouteModel model = list.get(i);
                ContentValues values = new ContentValues();
                values.put(DBHelper.ROUTE_ID, model.getRoute_id());
                values.put(DBHelper.ROUTE_CODE, model.getRoute_code());
                values.put(DBHelper.ROUTE_TITLE, model.getRoute_name());
                values.put(DBHelper.ROUTE_DESCRIPTION, model.getRoute_description());

                Log.d("route_code=", model.getRoute_code());

                database.insert(DBHelper.TBL_Route, null, values);
            }
            Utility.logCatMsg(list.size() + " Route Rows Entered ");
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertCompanyRoute()");
        }
        database.close();
    }


    public ArrayList<DeliveryItemModel> getCompanyItem() {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select Distinct  *  from " + DBHelper.TBL_PLN_ITEMS, null);
            if (cursor != null) {
                ArrayList<DeliveryItemModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    DeliveryItemModel model = new DeliveryItemModel();
                    model.setItem_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_ID)));
                    model.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_TITLE)));
                    model.setCode(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_CODE)));
                    model.setName(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_NAME)));
                    model.setItemDetail(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_DETAIL)));
                    model.setCostCtnPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_CORTON)));
                    model.setCostPackPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_PACK)));
                    model.setCostPiecePrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_PIECES)));
                    model.setRetailPiecePrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_WS_CORTON)));
                    model.setWSPackPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_WS_PACK)));
                    model.setWSPiecePrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_WS_PIECES)));
                    model.setRetailCtnPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_RETAIL_CORTON)));
                    model.setRetailPackPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_RETAIL_PACK)));
                    model.setWSCtnPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_RETAIL_PIECES)));
                    model.setDisplayPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_DISPLAY)));
                    model.setPackSize(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_PACK_SIZE)));
                    model.setCtnSize(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_CTN_SIZE)));
                    model.setItemGstPer(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_GST_PER)));
                    model.setItemGstValue(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_GST_VALUE)));

                    //updated for discount policy
                    model.setDivisionId(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_DIVISION_ID)));

                    model.setBrandId(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_BRAND_ID)));

                    model.setEmptyFlag(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_EMPTY_BOTTLE))));
                    model.setTaxCode(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_TAX_CODE)));
                    model.setSKU(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_SKU)));
                    list.add(model);
                }
                Utility.logCatMsg("SQLite Company Item list size " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getCompanyItem(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public void insertCompanyItem(ArrayList<DeliveryItemModel> list) {
        Timber.d("insertCompanyItem is running");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            for (int i = 0; i < list.size(); i++) {

                DeliveryItemModel model = list.get(i);

                ContentValues values = new ContentValues();


                // More Perimeters for Discount Policy work
                values.put(DBHelper.ITEM_DIVISION_ID, model.getDivisionId());
                values.put(DBHelper.ITEM_PRICE_ID, model.getPriceId());
                values.put(DBHelper.ITEM_PIECE_SIZE, model.getPieceSize());
                values.put(DBHelper.ITEM_UNIT, model.getUnit());
                values.put(DBHelper.ITEM_ISST_PERSENT, model.getIsSTPercent());
                values.put(DBHelper.ITEM_ISST_VALUE, model.getIsSTValue());
                values.put(DBHelper.ITEM_AST_PERCENTAGE, model.getASTPercentage());
                values.put(DBHelper.ITEM_AST_VALUE, model.getASTValue());
                values.put(DBHelper.ITEM_IS_AST_PERCENT, model.getIsASTPercent());
                values.put(DBHelper.ITEM_UNIT_SIZE, model.getUnitSize());
                values.put(DBHelper.ITEM_SIZE, model.getSize());
                values.put(DBHelper.ITEM_BRAND_ID, model.getBrandId());
                // Ending here params for discount policy work


                values.put(DBHelper.ITEM_ID, model.getItem_id());
                values.put(DBHelper.ITEM_TITLE, model.getTitle());
                values.put(DBHelper.ITEM_CODE, model.getCode());
                values.put(DBHelper.ITEM_NAME, model.getName());
                values.put(DBHelper.ITEM_IMAGE_NAME, model.getImageName());
                values.put(DBHelper.ITEM_DETAIL, model.getItemDetail());
                values.put(DBHelper.ITEM_PRICE_CORTON, model.getCostCtnPrice());
                values.put(DBHelper.ITEM_PRICE_PACK, model.getCostPackPrice());
                values.put(DBHelper.ITEM_PRICE_PIECES, model.getCostPiecePrice());
                values.put(DBHelper.ITEM_PRICE_WS_CORTON, model.getRetailPiecePrice());
                values.put(DBHelper.ITEM_PRICE_WS_PACK, model.getWSPackPrice());
                values.put(DBHelper.ITEM_PRICE_WS_PIECES, model.getWSPiecePrice());
                values.put(DBHelper.ITEM_PRICE_RETAIL_CORTON, model.getRetailCtnPrice());
                values.put(DBHelper.ITEM_PRICE_RETAIL_PACK, model.getRetailPackPrice());
                values.put(DBHelper.ITEM_PRICE_RETAIL_PIECES, model.getWSCtnPrice());
                values.put(DBHelper.ITEM_PRICE_DISPLAY, model.getDisplayPrice());
                values.put(DBHelper.ITEM_PACK_SIZE, model.getPackSize());
                values.put(DBHelper.ITEM_CTN_SIZE, model.getCtnSize());
                values.put(DBHelper.ITEM_EMPTY_BOTTLE, String.valueOf(model.getEmptyFlag()));
                values.put(DBHelper.ITEM_TAX_CODE, model.getTaxCode());
                values.put(DBHelper.ITEM_GST_PER, model.getItemGstPer());
                values.put(DBHelper.ITEM_GST_VALUE, model.getItemGstValue());
                values.put(DBHelper.ITEM_SKU, model.getSKU());

                Log.d("emptyFlag", String.valueOf(model.getEmptyFlag()));
                database.insert(DBHelper.TBL_PLN_ITEMS, null, values);

            }
            Utility.logCatMsg(list.size() + " Company Item Rows Entered ");
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertCompanyItem()");
        }
        database.close();
    }

    public int getSQLiteOrderDeliveryID(Integer server_delivery_id, String ISNEW) {

        SQLiteDatabase database = this.getReadableDatabase();
        DeliveryInfo model = new DeliveryInfo();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " Where " + DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID + " = " + server_delivery_id + " and " + DBHelper.ORDER__ISNEW + " = '" + ISNEW + "'", null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    model.setDelivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_ID)));
                    Utility.logCatMsg("Order Confirm Master Id" + model.getDelivery_id());
                }
                return model.getDelivery_id();
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getSQLiteBusinessCategories(): " + e.getMessage());
            return 0;
        }
        database.close();
        return 0;
    }

    public DeliveryItemModel getCompanyItem(int id) {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_PLN_ITEMS + " where " + DBHelper.ITEM_ID + " = " + id, null);
            DeliveryItemModel model = new DeliveryItemModel();

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    model.setItem_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_ID)));
                    model.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_TITLE)));
                    model.setCode(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_CODE)));
                    model.setName(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_NAME)));
                    model.setItemDetail(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_DETAIL)));
                    model.setCostCtnPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_CORTON)));
                    model.setCostPackPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_PACK)));
                    model.setCostPiecePrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_PIECES)));
                    model.setRetailPiecePrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_WS_CORTON)));
                    model.setWSPackPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_WS_PACK)));
                    model.setWSPiecePrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_WS_PIECES)));
                    model.setRetailCtnPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_RETAIL_CORTON)));
                    model.setRetailPackPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_RETAIL_PACK)));
                    model.setWSCtnPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_RETAIL_PIECES)));
                    model.setDisplayPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_PRICE_DISPLAY)));
                    model.setPackSize(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_PACK_SIZE)));
                    model.setCtnSize(cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_CTN_SIZE)));
                    model.setTaxCode(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_TAX_CODE)));
//                    model.setEmptyFlag(cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_EMPTY_BOTTLE)));
//                    Log.d("emptyFlag", model.getEmptyFlag());
                }
                return model;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getCompanyItem(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public int NumberOfBookedUnSyncOrder(String date) {
        String countQuery = "";
        if (date.equals("")) {
            Log.d("previousHistory", "true");
            countQuery = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_ISSAVE + "= '1' and " + DBHelper.ORDER_STATUS + " = 'Booking'";
        } else {
            countQuery = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_DATE_TIME + " like '%" + date + "%' and " + DBHelper.ORDER_ISSAVE + "= '1' and " + DBHelper.ORDER_STATUS + " = 'Booking'";
        }
        Log.d("bookedUnsyncedQuery", countQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        Log.d("unsyncCounter", "unSync New Order Count " + cnt);
        Utility.logCatMsg("unSync New Order Count " + cursor.getCount());
        cursor.close();
        return cnt;
    }

    public int NumberOfDeliveredUnSynecOrder(String date) {
        String countQuery = "";
        if (date.equals("")) {
            countQuery = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_ISSAVE + "= '1' and " + DBHelper.ORDER_STATUS + " = 'Delivered'";
        } else {
            countQuery = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_DATE_TIME + " like '%" + date + "%' and " + DBHelper.ORDER_ISSAVE + "= '1' and " + DBHelper.ORDER_STATUS + " = 'Delivered'";
        }

        Log.d("deliveredUnsyncedQuery", countQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        Log.d("unsyncCounter", "unSync New Order Count " + cursor.getCount());
        Utility.logCatMsg("unSync New Order Count " + cursor.getCount());
        cursor.close();
        return cnt;
    }

    public int NumberOfReturnedOrders(String date) {

        String countQuery = "";
        if (date.equals("")) {
            countQuery = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_ISSAVE + "= '1' and " + DBHelper.ORDER_STATUS + " = 'Returned'";
        } else {
            countQuery = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_DATE_TIME + " like '%" + date + "%' and " + DBHelper.ORDER_ISSAVE + "= '1' and " + DBHelper.ORDER_STATUS + " = 'Returned'";
        }

        Log.d("returnedCounterQuery", countQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        Log.d("returnedCounter", "unSync New Order Count " + cursor.getCount());
        Utility.logCatMsg("returnedCounter " + cursor.getCount());
        cursor.close();
        return cnt;

    }


    public int NumberOfSyncOrder(String date) {
        String countQuery = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_DATE_TIME + " like '%" + date + "%' and " + DBHelper.ORDER_ISSAVE + "= '2'";
        Log.d("bookedSyncedQuery", countQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        Utility.logCatMsg("Sync New Order Count " + cursor.getCount());
        cursor.close();
        return cnt;
    }

    public int NumberOfVisitRecords(String date) {
        String countQuery = "SELECT  * FROM " + DBHelper.TBL_VisitedDetails + " where " + DBHelper.vVisitDate + " like '%" + date + "%' ";
        Timber.d("Visit Query is " + countQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        Utility.logCatMsg("Sync New Order Count " + cursor.getCount());
        cursor.close();
        return cnt;
    }

    public int NumberOfReceipt(String date) {
        String countQuery = "";
        if (date.equals("")) {
            countQuery = "SELECT  * FROM " + DBHelper.TBL_CUSTOMER_RECEIPTS;
        } else {
            countQuery = "SELECT  * FROM " + DBHelper.TBL_CUSTOMER_RECEIPTS + " where " + DBHelper.RECEIPT_DATE + " like '%" + date + "%'";

        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        Utility.logCatMsg("Receipt Count " + cursor.getCount());
        cursor.close();
        return cnt;
    }

    public int numberOfUnsyncReceipt(String date) {
        String countQuery = "SELECT  * FROM " + DBHelper.TBL_CUSTOMER_RECEIPTS + " where " + DBHelper.RECEIPT_DATE + " like '%" + date + "%' and " + DBHelper.RECEIPT_IS_SYNC + "!= 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        Utility.logCatMsg("Receipt Count " + cursor.getCount());
        cursor.close();
        return cnt;
    }

    public int NumberOfAssignOrder() {
        String countQuery = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER__ISNEW + " = 'True' and " + DBHelper.ORDER_STATUS + " = 'Inprogress' and " + DBHelper.ORDER_ISSAVE + "!= '2'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        Utility.logCatMsg("Assign Order Count " + cursor.getCount());
        cursor.close();
        return cnt;
    }


    public int rGetUnSyncVisits() {

        String countQuery = "SELECT  * FROM " + DBHelper.TBL_VisitedDetails + " where " + DBHelper.vIsSync + " != 1";
        Timber.d("Visists Query is "+countQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        Timber.d("Total Visits Cout is "+cnt);
        cursor.close();
        return cnt;
    }

    //Getting Booked order with delivered Status or Booking
    public ArrayList<DeliveryInfo> getOrderDelivery(String status, String date) {
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            String sql = "";
            if (date.equals("")) {
                Log.d("history", "true");
                sql = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_ISSAVE + "= '1' and " + DBHelper.ORDER_STATUS + " = '" + status + "'";
            } else {
                sql = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_DATE_TIME + " like '%" + date + "%' and " + DBHelper.ORDER_ISSAVE + "= '1' and " + DBHelper.ORDER_STATUS + " = '" + status + "'";
            }
            Cursor cursor = database.rawQuery(sql, null);
            if (cursor != null) {
                ArrayList<DeliveryInfo> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    DeliveryInfo model = new DeliveryInfo();
                    model.setCashDespositedBankId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CASH_DEPOSITED_BANK_ID)));
                    model.setDelivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_ID)));
                    model.setServer_Delivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID)));
                    model.setDelivery_status(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_STATUS)));
                    model.setDelivery_description(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DESCRIPTION)));
                    model.setTotal_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_QTY)));
                    model.setDelivery_date(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DELIVERY_DATE)));
                    model.setDelivery_start_time(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_START_TIME)));
                    model.setDelivery_end_time(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_END_TIME)));
                    model.setDelivery_address(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DELIVERY_ADDRESS)));
                    model.setDelivery_to_mobile(cursor.getString(cursor.getColumnIndex(DBHelper.DELIVERY_TO_MOBILE)));
                    model.setDeliver_to_name(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_BY)));
                    // model.setAssign_to_TrackingNo(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_ASSIGN_TO_TRACKING_ID)));
                    model.setIs_delivery_Reject(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_IS_REJECT)));
                    model.setRejected_Reason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_REJECTED_REASON)));
                    model.setRefused_Reason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_REFUSED_REASON)));
                    model.setCancelledReason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CANCELLED_REASON)));
                    model.setReceivedBy(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_RECEIVED_BY)));
                    model.setPod_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_PODLAT)));
                    model.setPod_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_PODLNG)));
                    model.setPob_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_POBLAT)));
                    model.setPob_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_POBLNG)));
                    model.setNote(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_NOTE)));
                    model.setDeliver_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LATITUDE)));
                    model.setDeliver_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LONGITUDE)));
                    model.setIsPod_sync(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_ISSAVE)));
                    model.setIsOrderRead(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER__ISREAD)));
                    model.setIsNewUpdate(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER__NEW_UPDATE)));
                    model.setCustomer_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CUSTOMERID)));
                    model.setTotal_Bill(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_AMOUNT)));
                    model.setGrossTotalBill(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_GROSS_AMOUNT)));
                    model.setPercentageDiscount(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DISCOUNT_IN_PERCENTAGE)));
                    model.setDiscount(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DISCOUNT)));
                    model.setNetTotal(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_NetTotal)));

                    model.setDistributorId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_DISTRIBUTOR_ID)));
                    model.setSubDistributorId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_SUB_DISTRIBUTOR_ID)));

                    model.setOrderNumber(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_NO)));
                    model.setFromserver(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_From_Server)));
                    model.setTotal_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_QTY)));


                    Log.d("deliveryId", model.getDelivery_id() + "");
                    list.add(model);

                }
                Utility.logCatMsg("SQLite list " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getSQLiteOrderDelivery(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public ArrayList<DeliveryInfo> getEmptyBottelsOrder(String status, String date) {
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            String sql = "";
            if (date.equals("")) {
                Log.d("history", "true");
                sql = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_STATUS + " = '" + status + "'";
            } else {
                sql = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_DATE_TIME + " like '%" + date + "%' and " + DBHelper.ORDER_STATUS + " = '" + status + "'";
            }
            Cursor cursor = database.rawQuery(sql, null);
            if (cursor != null) {
                ArrayList<DeliveryInfo> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    DeliveryInfo model = new DeliveryInfo();
                    model.setDelivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_ID)));
                    model.setServer_Delivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID)));
                    model.setDelivery_status(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_STATUS)));
                    model.setDelivery_description(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DESCRIPTION)));
                    model.setTotal_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_QTY)));
                    model.setDelivery_date(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DELIVERY_DATE)));
                    model.setDelivery_start_time(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_START_TIME)));
                    model.setDelivery_end_time(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_END_TIME)));
                    model.setDelivery_address(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DELIVERY_ADDRESS)));
                    model.setDelivery_to_mobile(cursor.getString(cursor.getColumnIndex(DBHelper.DELIVERY_TO_MOBILE)));
                    model.setDeliver_to_name(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_BY)));
                    // model.setAssign_to_TrackingNo(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_ASSIGN_TO_TRACKING_ID)));
                    model.setIs_delivery_Reject(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_IS_REJECT)));
                    model.setRejected_Reason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_REJECTED_REASON)));
                    model.setRefused_Reason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_REFUSED_REASON)));
                    model.setCancelledReason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CANCELLED_REASON)));
                    model.setReceivedBy(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_RECEIVED_BY)));
                    model.setPod_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_PODLAT)));
                    model.setPod_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_PODLNG)));
                    model.setPob_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_POBLAT)));
                    model.setPob_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_POBLNG)));
                    model.setNote(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_NOTE)));
                    model.setDeliver_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LATITUDE)));
                    model.setDeliver_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LONGITUDE)));
                    model.setIsPod_sync(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_ISSAVE)));
                    model.setIsOrderRead(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER__ISREAD)));
                    model.setIsNewUpdate(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER__NEW_UPDATE)));
                    model.setFromserver(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_From_Server)));
                    model.setTotal_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_QTY)));

                    Log.d("deliveryId", model.getDelivery_id() + "");
                    list.add(model);

                }
                Utility.logCatMsg("SQLite list " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getSQLiteOrderDelivery(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }


    //Getting order that are synced on server
    public ArrayList<DeliveryInfo> getOrderDeliveryStatusDeliveredAndSynced(String date) {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "";
        try {
            Utility.logCatMsg("SQLite code ");

            if (date.equals("")) {
                sql = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_ISSAVE + "= '2'";
            } else {
                sql = "SELECT  * FROM " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_DATE_TIME + " like '%" + date + "%' and " + DBHelper.ORDER_ISSAVE + "= '2'";
            }

            Cursor cursor = database.rawQuery(sql, null);
            if (cursor != null) {
                ArrayList<DeliveryInfo> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    DeliveryInfo model = new DeliveryInfo();
                    model.setDelivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_ID)));
                    model.setServer_Delivery_id(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID)));
                    model.setDelivery_status(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_STATUS)));
                    model.setDelivery_description(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DESCRIPTION)));
                    model.setTotal_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_QTY)));
                    model.setDelivery_date(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DELIVERY_DATE)));
                    model.setDelivery_start_time(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_START_TIME)));
                    model.setDelivery_end_time(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_END_TIME)));
                    model.setDelivery_address(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DELIVERY_ADDRESS)));
                    model.setDelivery_to_mobile(cursor.getString(cursor.getColumnIndex(DBHelper.DELIVERY_TO_MOBILE)));
                    model.setDeliver_to_name(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_BY)));
                    // model.setAssign_to_TrackingNo(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_ASSIGN_TO_TRACKING_ID)));
                    model.setIs_delivery_Reject(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_IS_REJECT)));
                    model.setRejected_Reason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_REJECTED_REASON)));
                    model.setRefused_Reason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_REFUSED_REASON)));
                    model.setCancelledReason(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CANCELLED_REASON)));
                    model.setReceivedBy(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_RECEIVED_BY)));
                    model.setPod_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_PODLAT)));
                    model.setPod_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_PODLNG)));
                    model.setNote(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_NOTE)));
                    model.setDeliver_lat(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LATITUDE)));
                    model.setDeliver_lng(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_LONGITUDE)));
                    model.setIsPod_sync(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_ISSAVE)));
                    model.setIsOrderRead(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER__ISREAD)));
                    model.setIsNewUpdate(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER__NEW_UPDATE)));
                    model.setFromserver(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_MASTER_From_Server)));
                    model.setTotal_qty(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_TOTAL_QTY)));

                    model.setDistributorId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_DISTRIBUTOR_ID)));
                    model.setSubDistributorId(cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_SUB_DISTRIBUTOR_ID)));

                    list.add(model);

                }
                Utility.logCatMsg("SQLite list " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getSQLiteOrderDelivery(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public CompanyWiseBanksModel getDelieveryOrderBankInfo(int cashDepositedBankId) {

        SQLiteDatabase database = this.getReadableDatabase();
        CompanyWiseBanksModel model = new CompanyWiseBanksModel();
        try {
            Cursor cursor;

            cursor = database.rawQuery("select * from " + DBHelper.TBL_COMPANY_WISE_BANKS + " Where " +
                    DBHelper.BANK_ID + " = " + cashDepositedBankId + "", null);



            if (cursor != null) {
                while (cursor.moveToNext()) {

                    model.setBankName(cursor.getString(cursor.getColumnIndex(DBHelper.BANK_NAME)));
                    model.setBankAccountNbr(cursor.getString(cursor.getColumnIndex(DBHelper.BANK_ACCOUNT_NBR)));
                    model.setBankAccountType(cursor.getString(cursor.getColumnIndex(DBHelper.BANK_ACCOUNT_TYPE)));
                    model.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.BANK_ADDRESS)));

                }
                return model;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in  getDelieveryOrderBankInfo(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;

    }


    public int CollectCashDepositedBankId(String delivery_id) {

        SQLiteDatabase database = this.getReadableDatabase();
        int id = 0;
        try {
            Cursor cursor;
            cursor = database.rawQuery("select " + DBHelper.ORDER_CASH_DEPOSITED_BANK_ID + " From " +
                    DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_CONFIRM_MASTER_ID + "=" + Integer.parseInt(delivery_id), null);


            if (cursor != null) {

                id = cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_CASH_DEPOSITED_BANK_ID));
                Log.d("returncheck1", String.valueOf(id));
                return id;

            }

        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in  getDelieveryOrderBankInfo(): " + e.getMessage());
            Log.d("returncheck2", String.valueOf(id));
            return id;

        }
        database.close();
        Log.d("returncheck3", String.valueOf(id));
        return id;


    }

    public List<DeliveryItemModel> grabOrderItems(int delivery_id) {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");
            Timber.d("Deleivery id %s", delivery_id);

            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_ORDER_CONFIRM_CHILD +
                    " Where " + DBHelper.ORDER_CONFIRM_MASTER_DETAIL_ID + " = " + delivery_id, null);

            if (cursor != null) {
                ArrayList<DeliveryItemModel> list = new ArrayList<>();

                while (cursor.moveToNext()) {
                    DeliveryItemModel model = new DeliveryItemModel();

                    model.setName(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_ITEM_NAME)));
                    model.setCtn_qty((int) cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_CTN_QUANTITY)));
                    model.setPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_WHOLESALE_CTN_PRICE)));
                    model.setTotalRetailPrice(cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_NET_TOTAL_RETAIL)));

                    Timber.d("ORDER_CONFIRM_CHILD_WHOLESALE_CTN_PRICE => "+cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_WHOLESALE_CTN_PRICE)));
                    Timber.d("ORDER_CONFIRM_CHILD_NET_TOTAL_RETAIL => "+cursor.getFloat(cursor.getColumnIndex(DBHelper.ORDER_CONFIRM_CHILD_NET_TOTAL_RETAIL)));

                    list.add(model);
                }
                return list;
            } else {
                Utility.logCatMsg("Null cursor");
                return null;
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error in getImages " + e.getMessage());
            Timber.d("Exception case is running" + e.getMessage());
            return null;
        }

    }

    public boolean rInsertCurrentCustomerVisit(CustomerVisitedModel model) {
        boolean insert = false;
        SQLiteDatabase database = this.getReadableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(DBHelper.vID, model.getId());
            values.put(DBHelper.vCustomerName, model.getCustomerName());
            values.put(DBHelper.vRouteName, model.getRouteName());
            values.put(DBHelper.vStatusName, model.getVisitStatus());
            values.put(DBHelper.vCustomerId, model.getCustomerId());
            values.put(DBHelper.vStatus, model.getStatusID());
            values.put(DBHelper.vCompanyID, model.getCompanyID());
            values.put(DBHelper.vCompanySiteID, model.getCompanySiteID());
            values.put(DBHelper.vVisitDate, model.getVisitDate());
            values.put(DBHelper.vVisitTime, model.getVisitTime());
            values.put(DBHelper.vSalesmanID, model.getSalesmanID());
            values.put(DBHelper.vRouteID, model.getRouteID());
            values.put(DBHelper.vIsSync, model.getIsSync());
            values.put(DBHelper.vImageName, model.getImageName());
            values.put(DBHelper.vLatitude, model.getLatitude());
            values.put(DBHelper.vLongitude, model.getLongitude());
            database.insert(DBHelper.TBL_VisitedDetails, null, values);
            insert = true;
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertCustomerReceipt()" + e);
        }
        database.close();
        return insert;

    }

    public boolean rUpdateVisitStatusToSync(int id) {
        boolean a = false;
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues ColStatus = new ContentValues();
        ColStatus.put(DBHelper.vIsSync, "1");
        database.update(DBHelper.TBL_VisitedDetails, ColStatus, DBHelper.vID + "=" + id, null);
        a = true;
        return a;
    }


    public String rGrabCustomerName(String customerId) {
        String cName = null;
        SQLiteDatabase database = this.getReadableDatabase();

        try {

            Cursor cursor = database.rawQuery("select  " + DBHelper.CUSTOMER_NAME + "  from " + DBHelper.TBL_REGISTER_CUSTOMER + " WHERE " + DBHelper.CUSTOMER_ID + " = " + customerId, null);
            if (cursor != null) {
                cursor.moveToFirst();
                cName = cursor.getString(0);
                return cName;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getMaxRegCustomerId(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public String rGrabRouteName(String routeId) {
        String rName = null;
        SQLiteDatabase database = this.getReadableDatabase();

        try {

            Cursor cursor = database.rawQuery("select  " + DBHelper.ROUTE_TITLE + "  from " + DBHelper.TBL_Route + " WHERE " + DBHelper.ROUTE_ID + " = " + routeId, null);
            if (cursor != null) {
                cursor.moveToFirst();
                rName = cursor.getString(0);
                return rName;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            return null;
        }
        database.close();
        return null;
    }


    public float checkIfSpecialPriceExists(int rCustomerId, int itemId) {
        float price = 0.0f;
        SQLiteDatabase database = this.getReadableDatabase();

        try {

            Cursor cursor = database.rawQuery("select Distinct  " + DBHelper.CustomerPrice_Price + "  from " + DBHelper.TBL_Customer_Price + " WHERE " + DBHelper.CustomerPrice_CustomerId + " = " + rCustomerId + " and "+ DBHelper.CustomerPrice_ItemId + " = "+itemId, null);
            if (cursor != null) {
                cursor.moveToFirst();
                price = cursor.getFloat(0);
                return price;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            return 0.0f;
        }
        database.close();
        return 0.0f;
    }

    public ArrayList<DiscountPolicyModel> getDiscountPolicyData(String currentDate, int typeId, String disPriority) {



        String discPriority = disPriority;
        String sql="";
        SQLiteDatabase database = this.getReadableDatabase();

        try {

            if(discPriority.equalsIgnoreCase("Customer"))
            {
                Timber.d("Customer is running and customer id is "+typeId);
                //todo Customer Query (done)
                 sql = "Select Distinct * from " + DBHelper.TBL_DISCOUNT_POLICY
                        + " WHERE " + "'"+currentDate+"'" + " BETWEEN " + DBHelper.DIS_START_DATE
                        + " and " + DBHelper.DIS_END_DATE +" and " + DBHelper.DIS_TYPE_ID + " = " + "'"+typeId+"'"
                        + " and " + DBHelper.DIS_TYPE + " = " +"'"+disPriority+"'"
                        + " Order by "+DBHelper.DIS_ITEM_ID+" DESC, "+DBHelper.DIS_DIV_ID +" DESC, "+DBHelper.DIS_GROUP_DIV_ID+" DESC";
            }
            else if(discPriority.equalsIgnoreCase("CustomerType"))
            {
                Timber.d("CustomerType is running id is "+typeId);
                //todo Customer Type (done)
                 sql = "Select Distinct * from " + DBHelper.TBL_DISCOUNT_POLICY
                        + " WHERE " + "'"+currentDate+"'" + " BETWEEN " + DBHelper.DIS_START_DATE
                        + " and " + DBHelper.DIS_END_DATE +" and " + DBHelper.DIS_TYPE_ID + " = " + "'"+typeId+"'"
                        + " and " + DBHelper.DIS_TYPE + " = " +"'"+disPriority+"'"
                        + " Order by "+DBHelper.DIS_ITEM_ID+" DESC, "+DBHelper.DIS_DIV_ID +" DESC, "+DBHelper.DIS_GROUP_DIV_ID+" DESC";
            }
            else if (discPriority.equalsIgnoreCase("Item"))
            {
                Timber.d("Item is running and id is "+typeId);
                //todo Item query (done)
                 sql = "Select Distinct * from " + DBHelper.TBL_DISCOUNT_POLICY
                        + " WHERE " + "'"+currentDate+"'" + " BETWEEN " + DBHelper.DIS_START_DATE
                        + " and " + DBHelper.DIS_END_DATE +" and " + DBHelper.DIS_TYPE_ID + " = " + "'"+typeId+"'"
                        + " and " + DBHelper.DIS_TYPE + " = " +"'"+disPriority+"'"
                        + " Order by "+DBHelper.DIS_ITEM_ID+" DESC, "+DBHelper.DIS_DIV_ID +" DESC, "+DBHelper.DIS_GROUP_DIV_ID+" DESC";
            }
            else if(discPriority.equalsIgnoreCase("Division"))
            {
                Timber.d("Division is running and div id is "+typeId);
                //todo Division Query (done)
                 sql = "Select Distinct * from " + DBHelper.TBL_DISCOUNT_POLICY
                        + " WHERE " + "'"+currentDate+"'" + " BETWEEN " + DBHelper.DIS_START_DATE
                        + " and " + DBHelper.DIS_END_DATE +" and " + DBHelper.DIS_TYPE_ID + " = " + "'"+typeId+"'"
                        + " and " + DBHelper.DIS_TYPE + " = " +"'"+disPriority+"'"
                        + " Order by "+DBHelper.DIS_ITEM_ID+" DESC, "+DBHelper.DIS_DIV_ID +" DESC, "+DBHelper.DIS_GROUP_DIV_ID+" DESC";
            }
            else
            {
                Timber.d("Brand is running and brand id is "+typeId);
                //todo Brand Query (done)
                 sql = "Select Distinct * from " + DBHelper.TBL_DISCOUNT_POLICY
                        + " WHERE " + "'"+currentDate+"'" + " BETWEEN " + DBHelper.DIS_START_DATE
                        + " and " + DBHelper.DIS_END_DATE +" and " + DBHelper.DIS_TYPE_ID + " = " + "'"+typeId+"'"
                        + " and " + DBHelper.DIS_TYPE + " = " +"'"+disPriority+"'"
                        + " Order by "+DBHelper.DIS_ITEM_ID+" DESC, "+DBHelper.DIS_DIV_ID +" DESC, "+DBHelper.DIS_GROUP_DIV_ID+" DESC";
            }



            Timber.d("Sql query is "+sql);

            Cursor cursor = database.rawQuery(sql, null);
            if (cursor != null) {
                ArrayList<DiscountPolicyModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    DiscountPolicyModel model = new DiscountPolicyModel();
                    model.setDiscountPolicyId((cursor.getInt(cursor.getColumnIndex(DBHelper.DISCOUNT_POLICY_ID))));
                    model.setTypeId((cursor.getInt(cursor.getColumnIndex(DBHelper.DIS_TYPE_ID))));
                    model.setType(cursor.getString(cursor.getColumnIndex(DBHelper.DIS_TYPE)));
                    model.setDiscountType(cursor.getString(cursor.getColumnIndex(DBHelper.DISCOUNT_TYPE)));
                    model.setTargetValue(cursor.getInt(cursor.getColumnIndex(DBHelper.DIS_TARGET_VALUE)));
                    model.setTargetQty(cursor.getInt(cursor.getColumnIndex(DBHelper.DIS_TARGET_QTY)));
                    model.setDiscountPercentage(cursor.getInt(cursor.getColumnIndex(DBHelper.DIS_DISCOUNT_PERCENTAGE)));
                    model.setDiscountPercentage(cursor.getInt(cursor.getColumnIndex(DBHelper.DIS_DISCOUNT_PERCENTAGE)));
                    model.setDiscountValue(cursor.getFloat(cursor.getColumnIndex(DBHelper.DIS_DISCOUNT_VALUE)));
                    model.setAdditionalDiscountPercentage(cursor.getFloat(cursor.getColumnIndex(DBHelper.DIS_ADDITIONAL_DISCOUNT_PERCENTAGE)));
                    model.setAdditionalDiscountValue(cursor.getFloat(cursor.getColumnIndex(DBHelper.DIS_ADDITIONAL_DISCOUNT_VALUE)));
                    model.setAdditionalDiscountValue(cursor.getFloat(cursor.getColumnIndex(DBHelper.DIS_ADDITIONAL_DISCOUNT_VALUE)));
                    model.setStartDate(cursor.getString(cursor.getColumnIndex(DBHelper.DIS_START_DATE)));
                    model.setEndDate(cursor.getString(cursor.getColumnIndex(DBHelper.DIS_END_DATE)));
                    model.setEndDate(cursor.getString(cursor.getColumnIndex(DBHelper.DIS_END_DATE)));
                    model.setItemId(cursor.getInt(cursor.getColumnIndex(DBHelper.DIS_ITEM_ID)));
                    model.setDivId(cursor.getInt(cursor.getColumnIndex(DBHelper.DIS_DIV_ID)));
                    model.setGroupDivId(cursor.getInt(cursor.getColumnIndex(DBHelper.DIS_GROUP_DIV_ID)));
                    list.add(model);

                }

                Timber.d("Data is added to the discountList");
                return list;
            } else
                Timber.d("Cursor is null");
        } catch (Exception e) {
           Timber.d("Exception is because of "+e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public ArrayList<BonusPolicyModel> getBonusPolicyData(String currentDate, int typeId, String disPriority) {


        String discPriority = disPriority;
        String sql="";
        SQLiteDatabase database = this.getReadableDatabase();

        try {

            if(discPriority.equalsIgnoreCase("Customer"))
            {
                Timber.d("CustomerBonus is running and customer id is "+typeId);
                //todo Customer Query (done)
                sql = "Select Distinct * from " + DBHelper.TBL_BONUS_POLICY
                        + " WHERE " + "'"+currentDate+"'" + " BETWEEN " + DBHelper.BONS_START_DATE
                        + " and " + DBHelper.BONS_END_DATE +" and " + DBHelper.BONS_TYPE_ID + " = " + "'"+typeId+"'"
                        + " and " + DBHelper.BONS_TYPE + " = " +"'"+disPriority+"'";
            }

            if(discPriority.equalsIgnoreCase("CustomerType"))
            {
                Timber.d("CustomerBonus is running and customer id is "+typeId);
                //todo Customer Query (done)
                sql = "Select Distinct * from " + DBHelper.TBL_BONUS_POLICY
                        + " WHERE " + "'"+currentDate+"'" + " BETWEEN " + DBHelper.BONS_START_DATE
                        + " and " + DBHelper.BONS_END_DATE +" and " + DBHelper.BONS_TYPE_ID + " = " + "'"+typeId+"'"
                        + " and " + DBHelper.BONS_TYPE + " = " +"'"+disPriority+"'";
            }




            else if (discPriority.equalsIgnoreCase("Item"))
            {
                Timber.d("ItemBonus is running and id is "+typeId);
                //todo Item query (done)
                sql = "Select Distinct * from " + DBHelper.TBL_BONUS_POLICY
                        + " WHERE " + "'"+currentDate+"'" + " BETWEEN " + DBHelper.BONS_START_DATE
                        + " and " + DBHelper.BONS_END_DATE +" and " + DBHelper.BONS_TYPE_ID + " = " + "'"+typeId+"'"
                        + " and " + DBHelper.BONS_TYPE + " = " +"'"+disPriority+"'";
            }
            else if(discPriority.equalsIgnoreCase("Division"))
            {
                Timber.d("DivisionBonus is running and div id is "+typeId);
                //todo Division Query (done)
                sql = "Select Distinct * from " + DBHelper.TBL_BONUS_POLICY
                        + " WHERE " + "'"+currentDate+"'" + " BETWEEN " + DBHelper.BONS_START_DATE
                        + " and " + DBHelper.BONS_END_DATE +" and " + DBHelper.BONS_TYPE_ID + " = " + "'"+typeId+"'"
                        + " and " + DBHelper.BONS_TYPE + " = " +"'"+disPriority+"'";
            }
            else
            {
                Timber.d("BrandBonus is running and brand id is "+typeId);
                //todo Brand Query (done)
                sql = "Select Distinct * from " + DBHelper.TBL_BONUS_POLICY
                        + " WHERE " + "'"+currentDate+"'" + " BETWEEN " + DBHelper.BONS_START_DATE
                        + " and " + DBHelper.BONS_END_DATE +" and " + DBHelper.BONS_TYPE_ID + " = " + "'"+typeId+"'"
                        + " and " + DBHelper.BONS_TYPE + " = " +"'"+disPriority+"'";
            }


            Timber.d("Sql query is "+sql);

            Cursor cursor = database.rawQuery(sql, null);
            if (cursor != null) {
                ArrayList<BonusPolicyModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {

                    BonusPolicyModel model = new BonusPolicyModel();
                    model.setBonusPolicesId((cursor.getInt(cursor.getColumnIndex(DBHelper.BONS_POLICY_ID))));
                    model.setTypeId((cursor.getInt(cursor.getColumnIndex(DBHelper.BONS_TYPE_ID))));
                    model.setType(cursor.getString(cursor.getColumnIndex(DBHelper.BONS_TYPE)));
                    model.setDiscountType(cursor.getString(cursor.getColumnIndex(DBHelper.BONS_DISCOUNT_TYPE)));
                    model.setBonus(cursor.getInt(cursor.getColumnIndex(DBHelper.BONS_BONUS)));
                    model.setTargetQty(cursor.getInt(cursor.getColumnIndex(DBHelper.BONS_TARGET_QTY)));
                    model.setIncentiveItemId(cursor.getInt(cursor.getColumnIndex(DBHelper.BONS_INCENTIVE_ID)));
                    model.setIsClaimable(cursor.getString(cursor.getColumnIndex(DBHelper.BONS_IS_CLAIMABLE)));
                    model.setMultiItemsName(cursor.getString(cursor.getColumnIndex(DBHelper.BONS_MULTI_ITEMS_NAMES)));
                    model.setMultiItemsIdes(cursor.getString(cursor.getColumnIndex(DBHelper.BONS_MULTI_ITEMS_IDS)));
                    model.setOurShare(cursor.getFloat(cursor.getColumnIndex(DBHelper.BONS_OUR_SHARE)));
                    model.setStartDate(cursor.getString(cursor.getColumnIndex(DBHelper.BONS_START_DATE)));
                    model.setEndDate(cursor.getString(cursor.getColumnIndex(DBHelper.BONS_END_DATE)));
                    model.setSubType(cursor.getString(cursor.getColumnIndex(DBHelper.BONS_SUB_TYPE)));
                    model.setItemId(cursor.getInt(cursor.getColumnIndex(DBHelper.BONS_ITEM_ID)));
                    list.add(model);

                }

                Timber.d("Data is added to the discountList");
                return list;
            } else
                Timber.d("Cursor is null");
        } catch (Exception e) {
            Timber.d("Exception is because of "+e.getMessage());
            return null;
        }
        database.close();
        return null;
    }


    public String getItemName(int incentiveItemId) {
        String ItemName = "";
        SQLiteDatabase database = this.getReadableDatabase();

        try {

            Cursor cursor = database.rawQuery("select  " + DBHelper.ITEM_TITLE + "  from " + DBHelper.TBL_PLN_ITEMS + " WHERE " + DBHelper.ITEM_ID + " = " + incentiveItemId, null);
            if (cursor != null) {
                cursor.moveToFirst();
                ItemName = cursor.getString(0);
                return ItemName;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getMaxRegCustomerId(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public float getItemgstPer(int itemId) {
        float gstper = 0.0f;
        SQLiteDatabase database = this.getReadableDatabase();

        try {

            Cursor cursor = database.rawQuery("select  " + DBHelper.ITEM_GST_PER + "  from " + DBHelper.TBL_PLN_ITEMS + " WHERE " + DBHelper.ITEM_ID + " = " + itemId, null);
            if (cursor != null) {
                cursor.moveToFirst();
                gstper = cursor.getFloat(0);
                return gstper;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getMaxRegCustomerId(): " + e.getMessage());
            return 0.0f;
        }
        database.close();
        return 0.0f;
    }

    public float getBonusRetail(int incentiveItemId) {

        float bonusRetailPrice = 0.0f;
        SQLiteDatabase database = this.getReadableDatabase();

        try {

            Cursor cursor = database.rawQuery("select  " + DBHelper.ITEM_PRICE_RETAIL_PIECES + "  from " + DBHelper.TBL_PLN_ITEMS + " WHERE " + DBHelper.ITEM_ID + " = " + incentiveItemId, null);
            if (cursor != null) {
                cursor.moveToFirst();
                bonusRetailPrice = cursor.getFloat(0);
                return bonusRetailPrice;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getMaxRegCustomerId(): " + e.getMessage());
            return 0.0f;
        }
        database.close();
        return 0.0f;
    }

    public String getTaxCode(int incentiveItemId) {
        String ItemTaxCode = "";
        SQLiteDatabase database = this.getReadableDatabase();

        try {

            Cursor cursor = database.rawQuery("select  " + DBHelper.ITEM_TITLE + "  from " + DBHelper.TBL_PLN_ITEMS + " WHERE " + DBHelper.ITEM_ID + " = " + incentiveItemId, null);
            if (cursor != null) {
                cursor.moveToFirst();
                ItemTaxCode = cursor.getString(0);
                return ItemTaxCode;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getMaxRegCustomerId(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public void insertDistributers(ArrayList<DistributorModel> feedDistributorList) {

        SQLiteDatabase database = this.getWritableDatabase();
        try {
            for (int i = 0; i < feedDistributorList.size(); i++) {
                DistributorModel model = feedDistributorList.get(i);
                ContentValues values = new ContentValues();


                values.put(DBHelper.D_CONTACT_ID, model.getContactId());
                values.put(DBHelper.D_TITLE, model.getTitle());
                values.put(DBHelper.D_NAME, model.getName());

                values.put(DBHelper.D_FATHER_NAME,model.getFatherName());
                values.put(DBHelper.D_ADDRESS, model.getAddress());
                values.put(DBHelper.D_CITY, model.getCity());

                values.put(DBHelper.D_COUNTRY, model.getCountry());
                values.put(DBHelper.D_DELIVERY_ADDRESS, model.getDeliveryAddress());
                values.put(DBHelper.D_CONTACT_GROUP, model.getContactGroup());

                values.put(DBHelper.D_PHONE, model.getPhone());
                values.put(DBHelper.D_PHONE2, model.getPhone2());
                values.put(DBHelper.D_CELL, model.getCell());

                values.put(DBHelper.D_FAX, model.getFax());
                values.put(DBHelper.D_EMAIL, model.getEmail());
                values.put(DBHelper.D_AREA_ID, model.getAreaId());

                values.put(DBHelper.D_DIVISION_ID, model.getDivisionId());
                values.put(DBHelper.D_ZONE_ID, model.getZoneId());
                values.put(DBHelper.D_CREDIT_LIMIT, model.getCreditLimit());

                values.put(DBHelper.D_PERCENTAGEDISC1, model.getPercentageDisc1());
                values.put(DBHelper.D_PERCENTAGEDISC2, model.getPercentageDisc2());
                values.put(DBHelper.D_PERCENTAGEDISC3, model.getPercentageDisc3());

                values.put(DBHelper.D_LONGITUDE, model.getLongitude());
                values.put(DBHelper.D_LATITUDE, model.getLatitude());
                values.put(DBHelper.D_ADDRESS1, model.getAddress1());

                values.put(DBHelper.D_ADDRESS2, model.getAddress2());
                values.put(DBHelper.D_ROUTE_ID, model.getRouteId());
                values.put(DBHelper.D_COMPANY_SITE_ID, model.getCompanySiteID());

                values.put(DBHelper.D_COMPANY_ID, model.getCompanyID());
                values.put(DBHelper.D_CONTACT_CODE, model.getContactCode());
                values.put(DBHelper.D_CONTACT_TYPE, model.getContactType());

                values.put(DBHelper.D_CONTACT_TYPE_ID, model.getContactTypeId());
                values.put(DBHelper.D_SALES_MODE, model.getSalesMode());
                values.put(DBHelper.D_IMAGE_NAME, model.getImageName());

                values.put(DBHelper.D_IS_DISTRIBUTOR, model.getIsDistributer());
                values.put(DBHelper.D_DISTRIBUTOR_ID, model.getDistributerId());

                database.insert(DBHelper.TBL_DISTRIBUTORS, null, values);

                Timber.d("Value inserted");

            }
            Utility.logCatMsg(feedDistributorList.size() + " Distributor Rows Inserted ");
        } catch (Exception e) {
            Utility.logCatMsg("Insertion Failed in method insertDistributers()"+e.getMessage());
        }
        database.close();

    }

    public ArrayList<DistributorModel> getDistributors() {

        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");



            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_DISTRIBUTORS + " WHERE " + DBHelper.D_IS_DISTRIBUTOR + " = " + "'true'" , null);
            if (cursor != null) {
                ArrayList<DistributorModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    DistributorModel model = new DistributorModel();

                    model.setContactId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_CONTACT_ID)));
                    model.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.D_TITLE)));
                    model.setName(cursor.getString(cursor.getColumnIndex(DBHelper.D_NAME)));
                    model.setFatherName(cursor.getString(cursor.getColumnIndex(DBHelper.D_FATHER_NAME)));
                    model.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.D_ADDRESS)));
                    model.setCity(cursor.getString(cursor.getColumnIndex(DBHelper.D_CITY)));
                    model.setCountry(cursor.getString(cursor.getColumnIndex(DBHelper.D_COUNTRY)));
                    model.setDeliveryAddress(cursor.getString(cursor.getColumnIndex(DBHelper.D_DELIVERY_ADDRESS)));
                    model.setContactGroup(cursor.getString(cursor.getColumnIndex(DBHelper.D_CONTACT_GROUP)));
                    model.setPhone(cursor.getString(cursor.getColumnIndex(DBHelper.D_PHONE)));
                    model.setPhone2(cursor.getString(cursor.getColumnIndex(DBHelper.D_PHONE2)));
                    model.setCell(cursor.getString(cursor.getColumnIndex(DBHelper.D_CELL)));
                    model.setFax(cursor.getString(cursor.getColumnIndex(DBHelper.D_FAX)));
                    model.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.D_EMAIL)));
                    model.setAreaId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_AREA_ID)));
                    model.setDivisionId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_DIVISION_ID)));
                    model.setZoneId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_ZONE_ID)));
                    model.setCreditLimit(cursor.getString(cursor.getColumnIndex(DBHelper.D_CREDIT_LIMIT)));
                    model.setPercentageDisc1(cursor.getString(cursor.getColumnIndex(DBHelper.D_PERCENTAGEDISC1)));
                    model.setPercentageDisc2(cursor.getString(cursor.getColumnIndex(DBHelper.D_PERCENTAGEDISC2)));
                    model.setPercentageDisc3(cursor.getString(cursor.getColumnIndex(DBHelper.D_PERCENTAGEDISC3)));
                    model.setLongitude(cursor.getString(cursor.getColumnIndex(DBHelper.D_LONGITUDE)));
                    model.setLatitude(cursor.getString(cursor.getColumnIndex(DBHelper.D_LATITUDE)));
                    model.setAddress1(cursor.getString(cursor.getColumnIndex(DBHelper.D_ADDRESS1)));
                    model.setAddress2(cursor.getString(cursor.getColumnIndex(DBHelper.D_ADDRESS2)));
                    model.setRouteId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_ROUTE_ID)));
                    model.setCompanySiteID(cursor.getInt(cursor.getColumnIndex(DBHelper.D_COMPANY_SITE_ID)));
                    model.setCompanyID(cursor.getInt(cursor.getColumnIndex(DBHelper.D_COMPANY_ID)));
                    model.setContactCode(cursor.getString(cursor.getColumnIndex(DBHelper.D_CONTACT_CODE)));
                    model.setContactType(cursor.getString(cursor.getColumnIndex(DBHelper.D_CONTACT_TYPE)));
                    model.setContactTypeId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_CONTACT_TYPE_ID)));
                    model.setSalesMode(cursor.getString(cursor.getColumnIndex(DBHelper.D_SALES_MODE)));
                    model.setImageName(cursor.getString(cursor.getColumnIndex(DBHelper.D_IMAGE_NAME)));
                    model.setDistributerId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_DISTRIBUTOR_ID)));
                    model.setIsDistributer(cursor.getString(cursor.getColumnIndex(DBHelper.D_IS_DISTRIBUTOR)));

                    list.add(model);
                }
                Utility.logCatMsg("SQLite Distributor list size " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getDistributors(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;

    }

    public ArrayList<DistributorModel> getsubDistributors(int contactId) {
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            Utility.logCatMsg("SQLite code ");




            Cursor cursor = database.rawQuery("select * from " + DBHelper.TBL_DISTRIBUTORS + " WHERE " + DBHelper.D_IS_DISTRIBUTOR + " = " + "'false'" + " and " + DBHelper.D_DISTRIBUTOR_ID + " = " +"'"+contactId+"'" , null);
            if (cursor != null) {
                ArrayList<DistributorModel> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    DistributorModel model = new DistributorModel();

                    model.setContactId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_CONTACT_ID)));
                    model.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.D_TITLE)));
                    model.setName(cursor.getString(cursor.getColumnIndex(DBHelper.D_NAME)));
                    model.setFatherName(cursor.getString(cursor.getColumnIndex(DBHelper.D_FATHER_NAME)));
                    model.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.D_ADDRESS)));
                    model.setCity(cursor.getString(cursor.getColumnIndex(DBHelper.D_CITY)));
                    model.setCountry(cursor.getString(cursor.getColumnIndex(DBHelper.D_COUNTRY)));
                    model.setDeliveryAddress(cursor.getString(cursor.getColumnIndex(DBHelper.D_DELIVERY_ADDRESS)));
                    model.setContactGroup(cursor.getString(cursor.getColumnIndex(DBHelper.D_CONTACT_GROUP)));
                    model.setPhone(cursor.getString(cursor.getColumnIndex(DBHelper.D_PHONE)));
                    model.setPhone2(cursor.getString(cursor.getColumnIndex(DBHelper.D_PHONE2)));
                    model.setCell(cursor.getString(cursor.getColumnIndex(DBHelper.D_CELL)));
                    model.setFax(cursor.getString(cursor.getColumnIndex(DBHelper.D_FAX)));
                    model.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.D_EMAIL)));
                    model.setAreaId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_AREA_ID)));
                    model.setDivisionId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_DIVISION_ID)));
                    model.setZoneId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_ZONE_ID)));
                    model.setCreditLimit(cursor.getString(cursor.getColumnIndex(DBHelper.D_CREDIT_LIMIT)));
                    model.setPercentageDisc1(cursor.getString(cursor.getColumnIndex(DBHelper.D_PERCENTAGEDISC1)));
                    model.setPercentageDisc2(cursor.getString(cursor.getColumnIndex(DBHelper.D_PERCENTAGEDISC2)));
                    model.setPercentageDisc3(cursor.getString(cursor.getColumnIndex(DBHelper.D_PERCENTAGEDISC3)));
                    model.setLongitude(cursor.getString(cursor.getColumnIndex(DBHelper.D_LONGITUDE)));
                    model.setLatitude(cursor.getString(cursor.getColumnIndex(DBHelper.D_LATITUDE)));
                    model.setAddress1(cursor.getString(cursor.getColumnIndex(DBHelper.D_ADDRESS1)));
                    model.setAddress2(cursor.getString(cursor.getColumnIndex(DBHelper.D_ADDRESS2)));
                    model.setRouteId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_ROUTE_ID)));
                    model.setCompanySiteID(cursor.getInt(cursor.getColumnIndex(DBHelper.D_COMPANY_SITE_ID)));
                    model.setCompanyID(cursor.getInt(cursor.getColumnIndex(DBHelper.D_COMPANY_ID)));
                    model.setContactCode(cursor.getString(cursor.getColumnIndex(DBHelper.D_CONTACT_CODE)));
                    model.setContactType(cursor.getString(cursor.getColumnIndex(DBHelper.D_CONTACT_TYPE)));
                    model.setContactTypeId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_CONTACT_TYPE_ID)));
                    model.setSalesMode(cursor.getString(cursor.getColumnIndex(DBHelper.D_SALES_MODE)));
                    model.setImageName(cursor.getString(cursor.getColumnIndex(DBHelper.D_IMAGE_NAME)));
                    model.setDistributerId(cursor.getInt(cursor.getColumnIndex(DBHelper.D_DISTRIBUTOR_ID)));
                    model.setIsDistributer(cursor.getString(cursor.getColumnIndex(DBHelper.D_IS_DISTRIBUTOR)));

                    list.add(model);
                }
                Utility.logCatMsg("SQLite Distributor list size " + list.size());
                return list;
            } else
                Utility.logCatMsg("*****NULL CURSOR******");
        } catch (Exception e) {
            Utility.logCatMsg("Fetching Exception in getDistributors(): " + e.getMessage());
            return null;
        }
        database.close();
        return null;
    }

    public String getDistributorName(int subDistributorId) {

        SQLiteDatabase database = this.getReadableDatabase();
        String distName = "";
        String query = "select * from " + DBHelper.TBL_DISTRIBUTORS + " where " + DBHelper.D_CONTACT_ID + " = " + subDistributorId;
        Timber.d(" getDistributorName query is "+query);
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            distName = cursor.getString(cursor.getColumnIndex(DBHelper.D_NAME));
        }
        cursor.close();
        return distName;

    }


    public String getSubDistributorName(int subDistributorId) {

        SQLiteDatabase database = this.getReadableDatabase();
        String subdistName = "";
        String query = "select * from " + DBHelper.TBL_DISTRIBUTORS + " where " + DBHelper.D_CONTACT_ID + " = " + subDistributorId;
        Timber.d(" getSubDistributorName query is "+query);
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            subdistName = cursor.getString(cursor.getColumnIndex(DBHelper.D_NAME));
        }
        cursor.close();
        return subdistName;
    }

    public int GetDistributerId(int server_delivery_id) {

        SQLiteDatabase database = this.getReadableDatabase();
        int distId = 0;
        String query = "select * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID + " = " + server_delivery_id;

        Timber.d(" getDistributorName query is "+query);
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            distId = cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_DISTRIBUTOR_ID));
            Timber.d("the DistId inside is => "+distId);
        }
        cursor.close();
        return distId;

    }

    public int GetSubDistributerId(int server_delivery_id) {

        SQLiteDatabase database = this.getReadableDatabase();
        int subdistId = 0;
        String query = "select * from " + DBHelper.TBL_ORDER_CONFIRM_MASTER + " where " + DBHelper.ORDER_CONFIRM_MASTER_SERVER_ID + " = " + server_delivery_id;


        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            subdistId = cursor.getInt(cursor.getColumnIndex(DBHelper.ORDER_SUB_DISTRIBUTOR_ID));
        }
        cursor.close();
        return subdistId;

    }

    public String getItemDetail(int item_id) {
        SQLiteDatabase database = this.getReadableDatabase();
        String itemDetail = "";

        String query = "select  " + DBHelper.ITEM_DETAIL + "  from " + DBHelper.TBL_PLN_ITEMS + " WHERE " + DBHelper.ITEM_ID + " = " + item_id;


        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            itemDetail = cursor.getString(0);
        }
        cursor.close();
        return itemDetail;
    }
}

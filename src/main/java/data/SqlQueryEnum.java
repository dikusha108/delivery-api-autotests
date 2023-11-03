package data;

public enum SqlQueryEnum {
    uid("13072", ""),
    foreign_uid("0", ""),
    corr_id("CORR_ID", "EXECUTE PROCEDURE L3_CAPI_B2B_USERINFO(" + uid.getParam() + ", null, null)"),
    us_id("US_ID", "EXECUTE PROCEDURE L3_CAPI_B2B_USERINFO(" + uid.getParam() + ", null, null)"),
    foreign_us_id("US_ID", "EXECUTE PROCEDURE L3_CAPI_B2B_USERINFO(" + foreign_uid.getParam() + ", null, null)"),
    cnt_id("MAX", "SELECT MAX(CNT_ID) FROM CONTRACT c WHERE CORR_ID = %s AND CNTT_ID = 1 AND CNT_ISACTIVE = 1"),
    foreign_corr_id("CORR_ID", "EXECUTE PROCEDURE L3_CAPI_B2B_USERINFO(" + foreign_uid.getParam() + ", null, null)"),
    foreign_cnt_id("MAX", "SELECT MAX(CNT_ID) FROM CONTRACT c WHERE CORR_ID = %s AND CNTT_ID = 1 AND CNT_ISACTIVE = 1"),
    contact_person("MAX", "select MAX(CORR_ID_OUT) from L3_CAPI_CORR_PERSONS(%s)"),
    foreign_contact_person("MAX", "select MAX(CORR_ID_OUT) from L3_CAPI_CORR_PERSONS(%s)"),
    material_id_1("MAT_ID", "SELECT FIRST 1 m2.MAT_ID FROM MATQTYSUM m JOIN MATERIAL m2 " +
            "ON m.MQS_ID = m2.MQS_ID WHERE (m.mqs_storesaleqty - (m.mqs_hardsalereserveqty + " +
            "m.mqs_hardsalereservezakazqty + m.mqs_moitmploadsaleqty)) > 10000 " +
            "AND m2.STO_ID_DEFAULT = 12 AND m2.MAT_ID != 75083 ORDER BY RAND()"),
    material_id_2("MAT_ID", "SELECT FIRST 1 m2.MAT_ID FROM MATQTYSUM m JOIN MATERIAL m2 " +
            "ON m.MQS_ID = m2.MQS_ID WHERE (m.mqs_storesaleqty - (m.mqs_hardsalereserveqty + " +
            "m.mqs_hardsalereservezakazqty + m.mqs_moitmploadsaleqty)) > 8000 " +
            "AND (m.mqs_storesaleqty - (m.mqs_hardsalereserveqty + m.mqs_hardsalereservezakazqty + " +
            "m.mqs_moitmploadsaleqty)) < 10000 AND m2.STO_ID_DEFAULT = 12 ORDER BY RAND()"),
    material_id_3("MAT_ID", "SELECT FIRST 1 m2.MAT_ID FROM MATQTYSUM m JOIN MATERIAL m2 " +
            "ON m.MQS_ID = m2.MQS_ID WHERE (m.mqs_storesaleqty - (m.mqs_hardsalereserveqty + " +
            "m.mqs_hardsalereservezakazqty + m.mqs_moitmploadsaleqty)) > 6000 " +
            "AND (m.mqs_storesaleqty - (m.mqs_hardsalereserveqty + m.mqs_hardsalereservezakazqty + " +
            "m.mqs_moitmploadsaleqty)) < 8000 AND m2.STO_ID_DEFAULT = 12 ORDER BY RAND()"),
    material_id_4("MAT_ID", "SELECT FIRST 1 MAT_ID FROM MATREM m WHERE STO_ID = 12 AND MR_CURQTY = 0 ORDER BY RAND()"),
    material_id_5("MAT_ID", "SELECT FIRST 1 m2.MAT_ID FROM MATQTYSUM m JOIN MATERIAL m2 " +
            "ON m.MQS_ID = m2.MQS_ID WHERE (m.mqs_storesaleqty - (m.mqs_hardsalereserveqty + " +
            "m.mqs_hardsalereservezakazqty + m.mqs_moitmploadsaleqty)) > 3 " +
            "AND (m.mqs_storesaleqty - (m.mqs_hardsalereserveqty + m.mqs_hardsalereservezakazqty + " +
            "m.mqs_moitmploadsaleqty)) < 5 AND m2.STO_ID_DEFAULT = 12 ORDER BY RAND()"),
    correspondent_name("CORR_NAME2", "SELECT CORR_NAME2 FROM CORR WHERE CORR_ID = %s"),
    correspondent_name_abbr("CORR_NAME2", "SELECT CORR_NAME2 FROM CORR WHERE CORR_ID = %s"),
    correspondent_inn("CORR_INN", "SELECT CORR_INN FROM CORR WHERE CORR_ID = %s"),
    correspondent_kpp("CORR_KPP", "SELECT CORR_KPP FROM CORR WHERE CORR_ID = %s"),
    email("APP_FORM_EMAIL", "EXECUTE PROCEDURE L3_CAPI_B2B_USERINFO(" + uid.getParam() + ", null, null)"),
    foreign_delivery_address("CRM_ADDR_ID_RET", "SELECT FIRST 1 CRM_ADDR_ID_RET from L3_CAPI_CORR_ADDR_LIST(%s)"),
    delivery_address("CRM_ADDR_ID_RET", "select FIRST 1 CRM_ADDR_ID_RET from L3_CAPI_CORR_ADDR_LIST(%s);"),
    packed_invoice_id("DA_ID", "SELECT FIRST 1 l3.da_id\n" +
            "FROM l3_docacc_status l3 \n" +
            "JOIN DOCACC da ON da.DA_ID = l3.DA_ID \n" +
            "JOIN matopitem moi ON moi.DA_Id = da.DA_ID\n" +
            "WHERE DAK_ID = 11 and CORR_ID_DEBIT = %s and moi.MOI_QTY <10\n" +
            "GROUP BY l3.da_id \n" +
            "HAVING MAX(l3.da_status) = 100 and COUNT(moi.MOI_ID) < 5 \n" +
            "ORDER BY RAND();");

    private final String param;
    private final String query;

    SqlQueryEnum(String param, String query) {
        this.param = param;
        this.query = query;
    }

    public String getParam() {
        return param;
    }

    public String getQuery() {
        return query;
    }
}
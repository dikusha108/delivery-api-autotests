package model.customers;

public class PostAgreement {
    private String system;
    private String user_id;
    private String corr_id_holding;
    private Object sync_if_needed;

    public PostAgreement(String system, String user_id, String corr_id_holding) {
        this.system = system;
        this.user_id = user_id;
        this.corr_id_holding = corr_id_holding;
    }

    public PostAgreement(String system, String user_id, String corr_id_holding, Object sync_if_needed) {
        this.system = system;
        this.user_id = user_id;
        this.corr_id_holding = corr_id_holding;
        this.sync_if_needed = sync_if_needed;
    }

    public PostAgreement(){}

    public String getSystem() {
        return system;
    }

    public String getUserId() {
        return user_id;
    }

    public String getCorrIdHolding() {
        return corr_id_holding;
    }

    public void setSyncIfNeeded(Object sync_if_needed) {
        this.sync_if_needed = sync_if_needed;
    }

    public void setCorrIdHolding(String corr_id_holding) {
        this.corr_id_holding = corr_id_holding;
    }
}

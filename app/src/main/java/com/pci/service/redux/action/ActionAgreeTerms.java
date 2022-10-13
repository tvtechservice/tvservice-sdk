package com.pci.service.redux.action;

import java.util.HashMap;

import com.pci.service.redux.core.Action;

public class ActionAgreeTerms extends Action {

    private static final String KEY_ADID = "KEY_ADID";
    private static final String KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER";
    private static final String KEY_IS_ADID_USE_AGREED = "KEY_IS_ADID_USE_AGREED";
    private static final String KEY_IS_AD_PUSH_AGREED = "KEY_IS_AD_PUSH_AGREED";

    public ActionAgreeTerms(final String adid, final String phoneNumber, final boolean isAdidUseAgreed, final boolean isAdPushAgreed) {
        super(ActionType.PCI_AGREE_TERMS, new HashMap<String, Object>() {{
            this.put(KEY_ADID, adid);
            this.put(KEY_PHONE_NUMBER, phoneNumber);
            this.put(KEY_IS_ADID_USE_AGREED, isAdidUseAgreed);
            this.put(KEY_IS_AD_PUSH_AGREED, isAdPushAgreed);
        }});
    }

    public String getAdid() {
        return (String) getPayload().get(KEY_ADID);
    }

    public String getPhoneNumber() {
        return (String) getPayload().get(KEY_PHONE_NUMBER);
    }

    public boolean isAdidUseAgreed() {
        return (Boolean) getPayload().get(KEY_IS_ADID_USE_AGREED);
    }

    public boolean isAdPushAgreed() {
        return (Boolean) getPayload().get(KEY_IS_AD_PUSH_AGREED);
    }
}

package com.flatstack.android.utils.network;

import com.flatstack.android.utils.StringsUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class CommonError {

    private Error error;

    public String getMessage() {
        String message = error.error == null ? "" : (error.error + '\n');
        if (error.validations != null) {
            for (String key : error.validations.keySet()) {
                message += key + " " + StringsUtils.listToString(error.validations.get(key)) + '\n';
            }
        }
        return message;
    }

    public Map<String, List<String>> getValidations() {
        return error.validations;
    }

    public static class Error {
        private String error;
        private Map<String, List<String>> validations;
    }

}

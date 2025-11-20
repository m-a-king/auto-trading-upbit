package com.making.auto_trading_with_upbit_api.client.util;

import com.making.auto_trading_with_upbit_api.constants.ApiConstants;
import lombok.experimental.UtilityClass;
import org.springframework.util.MultiValueMap;

@UtilityClass
public class QueryStringBuilder {

    public String build(final MultiValueMap<String, String> params) {
        if (params == null || params.isEmpty()) {
            return ApiConstants.EMPTY_STRING;
        }

        final StringBuilder queryString = new StringBuilder();

        params.forEach((key, values) -> {
            for (final String value : values) {
                if (!queryString.isEmpty()) {
                    queryString.append("&");
                }

                if (values.size() > 1) {
                    queryString.append(key).append("[]=").append(value);
                } else {
                    queryString.append(key).append("=").append(value);
                }
            }
        });

        return queryString.toString();
    }
}

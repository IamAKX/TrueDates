package com.neosao.truedates.model.options;

import java.util.ArrayList;

public class ZodiacSigns {
    private String id;
    private String sign;

    public ZodiacSigns(String id, String sign) {
        this.id = id;
        this.sign = sign;
    }

    public String getId() {
        return id;
    }

    public String getSign() {
        return sign;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

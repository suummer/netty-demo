package com.skyline.demo.nettydemo.ws.common.util.json;

import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

public class JsonSerializerProvider extends DefaultSerializerProvider {

    private static final long serialVersionUID = -2891437067035919790L;

    public JsonSerializerProvider() {
        super();
    }

    public JsonSerializerProvider(JsonSerializerProvider src) {
        super(src);
    }

    protected JsonSerializerProvider(SerializerProvider src,
                                     SerializationConfig config, SerializerFactory f) {
        super(src, config, f);
    }

    @Override
    public DefaultSerializerProvider copy() {
        if (getClass() != JsonSerializerProvider.class) {
            return super.copy();
        }
        return new JsonSerializerProvider(this);
    }

    @Override
    public JsonSerializerProvider createInstance(
            SerializationConfig config, SerializerFactory jsf) {
        return new JsonSerializerProvider(this, config, jsf);
    }

}

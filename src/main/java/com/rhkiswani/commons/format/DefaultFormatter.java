package com.rhkiswani.commons.format;

import com.rhkiswani.commons.format.exception.FormatException;
import com.rhkiswani.commons.lang.Arrays;

public abstract class DefaultFormatter<IN, OUT> implements Formatter<IN, OUT> {

    protected abstract OUT formatVal(IN in, Object... params);

    @Override
    public OUT format(IN in, Object... params) throws FormatException {
        if (in == null){
            return null;
        }
        if (params.length > 0){
            try {
                Arrays.replace(params, null, "");
                return formatVal(in, params);
            } catch (Throwable t ){
                throw new FormatException(t);
            }

        }
        return formatVal(in);
    }

}

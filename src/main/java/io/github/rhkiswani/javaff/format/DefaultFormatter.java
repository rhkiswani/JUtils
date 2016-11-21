package io.github.rhkiswani.javaff.format;

import io.github.rhkiswani.javaff.format.exception.FormatException;
import io.github.rhkiswani.javaff.lang.ArraysHelper;

public abstract class DefaultFormatter<IN, OUT> implements Formatter<IN, OUT> {

    protected abstract OUT formatVal(IN in, Object... params);

    @Override
    public OUT format(IN in, Object... params) throws FormatException {
        if (in == null){
            return null;
        }
        if (params.length > 0){
            try {
                ArraysHelper.replace(params, null, "");
                return formatVal(in, params);
            } catch (Throwable t ){
                throw new FormatException(t);
            }

        }
        return formatVal(in);
    }

}

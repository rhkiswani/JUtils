/*
 * Copyright 2016 Mohamed Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.rhkiswani.javaff.log;

import io.github.rhkiswani.javaff.detector.ApiDetectorUtil;
import io.github.rhkiswani.javaff.factory.AbstractFactory;

/**
 * @author Mohamed Kiswani
 * @since 0.0.1
 * @see io.github.rhkiswani.javaff.factory.AbstractFactory
 */
public class LogFactory extends AbstractFactory<Log>{
    private static LogFactory instance = new LogFactory();

    private LogFactory(){
    }

    public static LogFactory instance(){
        return instance;
    }

    @Override
    protected Log getDefault(Class targetClazz) {
        if (ApiDetectorUtil.isSlf4jAvailable()){
            return new Slf4jLog(targetClazz);
        }
        return new DefaultLog(targetClazz.getClass());
    }

    public static Log getLogger(Class aClass) {
        return new LogWrapper(instance().create(aClass));
    }
}

/*
 * Copyright (C) 2019 The JackKnife Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.cache_generator.apt;

import com.example.cache_generator.annotation.handler.AnnotationHandler;
import com.example.cache_generator.annotation.handler.ThreeLevelCacheHandler;
import com.example.cache_generator.writer.JavaWriter;
import com.google.auto.service.AutoService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes(
        {
                "com.example.cache_generator.annotation.ThreeLevelCache"
        })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CacheProcessor extends AbstractProcessor {

    private List<AnnotationHandler> mHandlers = new ArrayList<>();
    private JavaWriter mWriter;
    private Map<String, List<Element>> mElementsMap = new HashMap<>();
    private Filer mFiler;
    private Messager mMessager;

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void log(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.mFiler = processingEnv.getFiler();
        this.mMessager = processingEnv.getMessager();
        registerHandler(new ThreeLevelCacheHandler());

        mWriter = new JavaWriter(processingEnv);
    }

    protected void registerHandler(AnnotationHandler handler) {
        mHandlers.add(handler);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (AnnotationHandler handler : mHandlers) {
            handler.attachProcessingEnvironment(processingEnv);
            mElementsMap.putAll(handler.handleAnnotation(roundEnv));
        }
        mWriter.generate(mElementsMap);
        return true;    //处理完成了，return true就好
    }
}
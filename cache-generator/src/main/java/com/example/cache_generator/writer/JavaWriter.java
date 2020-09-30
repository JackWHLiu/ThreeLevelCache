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

package com.example.cache_generator.writer;

import com.example.cache_generator.annotation.ThreeLevelCache;
import com.example.cache_generator.cache.Cache;
import com.example.cache_generator.cache.CacheContext;
import com.example.cache_generator.cache.CacheFactory;
import com.example.cache_generator.priority.CachePriority;
import com.example.cache_generator.priority.DefaultCachePriority;
import com.example.cache_generator.provider.CacheProvider;
import com.example.cache_generator.provider.DatabaseProvider;
import com.example.cache_generator.provider.MemoryProvider;
import com.example.cache_generator.provider.NetworkProvider;
import com.example.cache_generator.source.CacheSource;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;

public class JavaWriter implements AbstractWriter {

    private ProcessingEnvironment mProcessingEnv;

    private Messager mMessager;

    private Filer mFiler;

    public JavaWriter(ProcessingEnvironment env) {
        this.mProcessingEnv = env;
        this.mMessager = env.getMessager();
        this.mFiler = mProcessingEnv.getFiler();
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void log(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

    @Override
    public void generate(Map<String, List<Element>> map) {
        for (Map.Entry<String, List<Element>> entry : map.entrySet()) {
            List<Element> elements = entry.getValue();
            for (Element element : elements) {
                ThreeLevelCache threeLevelCache = element.getAnnotation(ThreeLevelCache.class);
                if (threeLevelCache != null) {
                    handleAnnotation(threeLevelCache, element);
                }
            }
        }
    }

    private String getPackageName(Element element) {
        return mProcessingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();
    }

    public MethodSpec createCacheSource(ClassName tableClassName) {
//        CachePriority priority = new DefaultCachePriority();
//        CacheContext cacheContext = new CacheContext();
//        return priority.selectSource(cacheContext, Person.class);
        return MethodSpec.methodBuilder("createCacheSource")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("$T priority = new $T()", CachePriority.class, DefaultCachePriority.class)
                .addStatement("$T cacheContext = new $T()", CacheContext.class, CacheContext.class)
                .addStatement("return priority.selectSource(cacheContext, $T.class)", tableClassName)
                .returns( ParameterizedTypeName.get(ClassName.get(CacheSource.class), ClassName.get(String.class), tableClassName))
                .build();
    }

    public MethodSpec createCacheProvider(ClassName tableClassName) {
//        DatabaseProvider<Person> databaseProvider = new DatabaseProvider<>();
//        NetworkProvider<Person> networkProvider = new NetworkProvider<>(databaseProvider);
//        return new MemoryProvider<>(networkProvider);
        return MethodSpec.methodBuilder("createCacheProvider")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("$T<$T> databaseProvider = new $T<>()", DatabaseProvider.class,
                        tableClassName, DatabaseProvider.class)
                .addStatement("$T<$T> networkProvider = new $T<>(databaseProvider)",
                        NetworkProvider.class, tableClassName, NetworkProvider.class)
                .addStatement("return new $T<>(networkProvider)", MemoryProvider.class)
                .returns( ParameterizedTypeName.get(ClassName.get(CacheProvider.class), ClassName.get(String.class), tableClassName))
                .build();
    }

    public MethodSpec constructor() {
//        mFactory = new PersonCacheFactory();
//        mSource = mFactory.createCacheSource();
//        mProvider = mFactory.createCacheProvider();
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("mFactory = new Factory()")
                .addStatement("mSource = mFactory.createCacheSource()")
                .addStatement("mProvider = mFactory.createCacheProvider()")
                .build();
    }


    public FieldSpec cacheFactory(String packageName, TypeSpec typeSpec) {
        return FieldSpec.builder(ClassName.bestGuess("Factory"), "mFactory", Modifier.PRIVATE)
                .build();
    }

    private FieldSpec cacheProvider(ClassName tableClassName) {
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(CacheProvider.class),
                ClassName.get(String.class), tableClassName);
        return FieldSpec.builder(parameterizedTypeName, "mProvider", Modifier.PRIVATE)
                .build();
    }

    private FieldSpec cacheSource(ClassName tableClassName) {
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(CacheSource.class),
                ClassName.get(String.class), tableClassName);
        return FieldSpec.builder(parameterizedTypeName, "mSource", Modifier.PRIVATE)
                .build();
    }

    private MethodSpec add(ClassName tableClassName) {
        return MethodSpec.methodBuilder("add")
                .addParameter(tableClassName, "value")
                .addAnnotation(Override.class)
                .addCode("if (mProvider != null) {\t\n")
                .addStatement("\tmProvider.add(value)")
                .addCode("}\t\n")
                .addStatement("super.add(value)")
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

    private MethodSpec addAll(ClassName tableClassName) {
        return MethodSpec.methodBuilder("addAll")
                .addParameter(ParameterizedTypeName.get(ClassName.get(List.class), tableClassName), "values")
                .addAnnotation(Override.class)
                .addCode("if (mProvider != null) {\t\n")
                .addStatement("\tmProvider.addAll(values)")
                .addCode("}\t\n")
                .addStatement("super.addAll(values)")
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

    private MethodSpec remove(ClassName tableClassName) {
        return MethodSpec.methodBuilder("remove")
                .addParameter(tableClassName, "value")
                .addAnnotation(Override.class)
                .addCode("if (mProvider != null) {\t\n")
                .addStatement("\tmProvider.remove(value)")
                .addCode("}\t\n")
                .addStatement("super.remove(value)")
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

    private MethodSpec removeAll(ClassName tableClassName) {
        return MethodSpec.methodBuilder("removeAll")
                .addParameter(ParameterizedTypeName.get(ClassName.get(List.class), tableClassName), "values")
                .addAnnotation(Override.class)
                .addCode("if (mProvider != null) {\t\n")
                .addStatement("\tmProvider.removeAll(values)")
                .addCode("}\t\n")
                .addStatement("super.removeAll(values)")
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

    private MethodSpec save(ClassName tableClassName) {
        return MethodSpec.methodBuilder("save")
                .addParameter(tableClassName, "value")
                .addAnnotation(Override.class)
                .addCode("if (mProvider != null) {\t\n")
                .addStatement("\tmProvider.save(value)")
                .addCode("}\t\n")
                .addStatement("super.save(value)")
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

    private MethodSpec saveAll(ClassName tableClassName) {
        return MethodSpec.methodBuilder("saveAll")
                .addParameter(ParameterizedTypeName.get(ClassName.get(List.class), tableClassName), "values")
                .addAnnotation(Override.class)
                .addCode("if (mProvider != null) {\t\n")
                .addStatement("\tmProvider.saveAll(values)")
                .addCode("}\t\n")
                .addStatement("super.saveAll(values)")
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

    private MethodSpec fetch(ClassName tableClassName) {
        return MethodSpec.methodBuilder("fetch")
                .addAnnotation(Override.class)
                .addCode("if (mSource != null) {\t\n")
                .addStatement("\treturn mSource.fetch()")
                .addCode("}\t\n")
                .addStatement("return super.fetch()")
                .addModifiers(Modifier.PUBLIC)
                .returns(tableClassName)
                .build();
    }

    private MethodSpec fetchAll(ClassName tableClassName) {
        return MethodSpec.methodBuilder("fetchAll")
                .addAnnotation(Override.class)
                .addCode("if (mSource != null) {\t\n")
                .addStatement("\treturn mSource.fetchAll()")
                .addCode("}\t\n")
                .addStatement("return super.fetchAll()")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(List.class), tableClassName))
                .build();
    }

    public Iterable<MethodSpec> methods(MethodSpec... methodSpecs) {
        return Arrays.asList(methodSpecs);
    }

    public Iterable<FieldSpec> fields(FieldSpec... fieldSpecs) {
        return Arrays.asList(fieldSpecs);
    }

    private void handleAnnotation(ThreeLevelCache threeLevelCache, Element element) {
        String packageName = getPackageName(element);
        String tableName = element.getSimpleName().toString();
        ClassName tableClassName = ClassName.get(packageName, tableName);
        TypeSpec factory = TypeSpec.classBuilder("Factory")
                .addMethods(methods(createCacheProvider(tableClassName), createCacheSource(tableClassName)))
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(CacheFactory.class),
                        ClassName.get(packageName, tableName)))
                .addModifiers(Modifier.STATIC)
                .build();
        TypeSpec tableCache = TypeSpec.classBuilder(tableName+"Cache")
                .superclass(ParameterizedTypeName.get(ClassName.get(Cache.class),
                        ClassName.get(String.class), ClassName.get(packageName, tableName)))
                .addFields(fields(cacheFactory(packageName+"."+tableName+"Cache", factory),
                        cacheSource(tableClassName), cacheProvider(tableClassName)))
                .addMethods(methods(constructor(),
                        add(tableClassName), addAll(tableClassName),
                        remove(tableClassName), removeAll(tableClassName),
                        save(tableClassName), saveAll(tableClassName),
                        fetch(tableClassName), fetchAll(tableClassName)
                        ))
                .addModifiers(Modifier.PUBLIC)
                .addType(factory)
                .build();
        JavaFile javaFile = JavaFile.builder(packageName, tableCache)
                .addFileComment("These codes are generated by Dora automatically. Do not modify!")
                .build();
        try {
            javaFile.writeTo(mProcessingEnv.getFiler());
        } catch (IOException e) {
            log(e.getMessage());
        }
    }

//    private void handleAnnotation(Difference difference, Element element) {
//        String proxyName = difference.proxyName();
//        TypeElement typeElement = (TypeElement) element;
//        TypeVariableName c = TypeVariableName.get("C", IDifference.class);
//        TypeVariableName d = TypeVariableName.get("D", IDifference.class);
//        MethodSpec.Builder newDecoratorMtdBuilder = MethodSpec.methodBuilder("newDecorator");
//        newDecoratorMtdBuilder.addModifiers(Modifier.PUBLIC);
//        newDecoratorMtdBuilder.addTypeVariable(c);
//        newDecoratorMtdBuilder.addTypeVariable(d);
//        newDecoratorMtdBuilder.addParameter(c, "component");
//        newDecoratorMtdBuilder.addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), c), "componentClazz");
//        MethodSpec.Builder getDecoratorClassMtdBuilder = MethodSpec.methodBuilder("getDecoratorClass");
//        getDecoratorClassMtdBuilder.addModifiers(Modifier.PUBLIC)
//                .returns(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(IDifference.class)));
//        boolean needReturnNull = false;
//        List<? extends AnnotationMirror> annotationMirrors = typeElement.getAnnotationMirrors();
//        for (AnnotationMirror annotationMirror : annotationMirrors) {
//            DeclaredType annotationType = annotationMirror.getAnnotationType();
//            Element ae = annotationType.asElement();
//            Flavor flavor = ae.getAnnotation(Flavor.class);
//            String s = ae.getSimpleName().toString();
//            if (flavor == null) {
//                continue;
//            }
//            if (proxyName.equalsIgnoreCase(s)) {
//                List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
//                for (TypeMirror typeMirror : interfaces) {
//                    Types types = mProcessingEnv.getTypeUtils();
//                    Element e = types.asElement(typeMirror);
//                    DifferenceInterface differenceInterface = e.getAnnotation(DifferenceInterface.class);
//                    if (differenceInterface != null) {
//                        newDecoratorMtdBuilder.addCode("try {\n  $T constructor = getDecoratorClass().getConstructor(componentClazz);\n", Constructor.class);
//                        newDecoratorMtdBuilder.addStatement("  constructor.setAccessible(true)");
//                        newDecoratorMtdBuilder.addStatement("  return (D) constructor.newInstance(component)");
//                        newDecoratorMtdBuilder.addCode("} catch($T e) {\n  e.printStackTrace();\n}\n", Exception.class);
//                        getDecoratorClassMtdBuilder.addStatement("return $T.class", ClassName
//                                .bestGuess(differenceInterface.packageName()+"."+differenceInterface.moduleName() + s));
//                        needReturnNull = true;
//                    }
//                }
//            }
//        }
//        if (!needReturnNull) {
//            getDecoratorClassMtdBuilder.addStatement("return null");
//        }
//        newDecoratorMtdBuilder.addStatement("return null");
//        newDecoratorMtdBuilder.returns(d);
//        String packageName = MultiFlavors.getPackageName(mProcessingEnv, element);
//        String className = typeElement.getSimpleName().toString();
//        className += "$Factory";
//        TypeSpec typeSpec = TypeSpec.classBuilder(className)
//                .addSuperinterface(DecoratorFactory.class)
//                .addModifiers(Modifier.PUBLIC)
//                .addMethod(newDecoratorMtdBuilder.build())
//                .addMethod(getDecoratorClassMtdBuilder.build())
//                .build();
//        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
//                .addFileComment("These codes are generated by JackKnife automatically. Do not modify!")
//                .build();
//        try {
//            javaFile.writeTo(mProcessingEnv.getFiler());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void handleAnnotation(Wrapper wrapper, Element element) {
//        String flavorName = wrapper.flavorName();
//        Types types = mProcessingEnv.getTypeUtils();
//        TypeElement typeElement = (TypeElement) element;
//        List<? extends AnnotationMirror> annotationMirrors = typeElement.getAnnotationMirrors();
//        for (AnnotationMirror annotationMirror : annotationMirrors) {
//            DeclaredType annotationType = annotationMirror.getAnnotationType();
//            Element ae = annotationType.asElement();
//            Flavor flavor = ae.getAnnotation(Flavor.class);
//            if (flavor == null) {
//                continue;
//            }
//            List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
//            for (TypeMirror typeMirror:interfaces) {
//                Element interfaceElement = types.asElement(typeMirror);
//                DifferenceInterface differenceInterface = interfaceElement.getAnnotation(DifferenceInterface.class);
//                if (differenceInterface != null) {
//                    String packageName = differenceInterface.packageName();
//                    String moduleName = differenceInterface.moduleName();
//                    String s = ae.getSimpleName().toString();
//                    if (s.equalsIgnoreCase(flavorName)) {
//                        MethodSpec methodSpec = MethodSpec.constructorBuilder()
//                                .addModifiers(Modifier.PUBLIC)
//                                .addParameter(ClassName.bestGuess(interfaceElement.toString()), "base")
//                                .addStatement("super(base)")
//                                .build();
//                        TypeSpec typeSpec = TypeSpec.classBuilder(moduleName + s)
//                                .addModifiers(Modifier.PUBLIC)
//                                .superclass(ClassName.bestGuess(typeElement.toString()))
//                                .addMethod(methodSpec)
//                                .build();
//                        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
//                                .addFileComment("These codes are generated by JackKnife automatically. Do not modify!")
//                                .build();
//                        try {
//                            javaFile.writeTo(mProcessingEnv.getFiler());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
//    }
}

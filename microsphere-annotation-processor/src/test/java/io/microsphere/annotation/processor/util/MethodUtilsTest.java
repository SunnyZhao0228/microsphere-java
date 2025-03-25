/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.microsphere.annotation.processor.util;

import io.microsphere.annotation.processor.AbstractAnnotationProcessingTest;
import io.microsphere.annotation.processor.TestService;
import io.microsphere.annotation.processor.model.Model;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static io.microsphere.annotation.processor.util.MethodUtils.findAllDeclaredMethods;
import static io.microsphere.annotation.processor.util.MethodUtils.findMethod;
import static io.microsphere.annotation.processor.util.MethodUtils.findPublicNonStaticMethods;
import static io.microsphere.annotation.processor.util.MethodUtils.getAllDeclaredMethods;
import static io.microsphere.annotation.processor.util.MethodUtils.getDeclaredMethods;
import static io.microsphere.annotation.processor.util.MethodUtils.getMethodName;
import static io.microsphere.annotation.processor.util.MethodUtils.getMethodParameterTypes;
import static io.microsphere.annotation.processor.util.MethodUtils.getOverrideMethod;
import static io.microsphere.annotation.processor.util.MethodUtils.getReturnType;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link MethodUtils} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @since 1.0.0
 */
public class MethodUtilsTest extends AbstractAnnotationProcessingTest {

    private TypeElement testTypeElement;

    @Test
    public void testDeclaredMethods() {
        TypeElement type = getTypeElement(Model.class);
        List<ExecutableElement> methods = getDeclaredMethods(type);
        assertEquals(12, methods.size());

        methods = getAllDeclaredMethods(type);
        assertTrue(methods.size() >= 33);

        assertTrue(getAllDeclaredMethods((TypeElement) null).isEmpty());
        assertTrue(getAllDeclaredMethods((TypeMirror) null).isEmpty());
    }

    private List<? extends ExecutableElement> doFindAllDeclaredMethods() {
        return findAllDeclaredMethods(testTypeElement, Object.class);
    }

    @Test
    public void testFindAllDeclaredMethods() {
        List<? extends ExecutableElement> methods = doFindAllDeclaredMethods();
        assertEquals(14, methods.size());
    }

    @Test
    public void testFindPublicNonStaticMethods() {
        List<? extends ExecutableElement> methods = findPublicNonStaticMethods(testTypeElement, Object.class);
        assertEquals(14, methods.size());

        methods = findPublicNonStaticMethods(testTypeElement.asType(), Object.class);
        assertEquals(14, methods.size());
    }

    @Test
    public void testIsMethod() {
        List<? extends ExecutableElement> methods = findPublicNonStaticMethods(testTypeElement, Object.class);
        assertEquals(14, methods.stream().map(MethodUtils::isMethod).count());
    }

    @Test
    public void testIsPublicNonStaticMethod() {
        List<? extends ExecutableElement> methods = findPublicNonStaticMethods(testTypeElement, Object.class);
        assertEquals(14, methods.stream().map(MethodUtils::isPublicNonStaticMethod).count());
    }

    @Test
    public void testFindMethod() {
        TypeElement type = getTypeElement(Model.class);
        // Test methods from java.lang.Object
        // Object#toString()
        String methodName = "toString";
        ExecutableElement method = findMethod(type.asType(), methodName);
        assertEquals(method.getSimpleName().toString(), methodName);

        // Object#hashCode()
        methodName = "hashCode";
        method = findMethod(type.asType(), methodName);
        assertEquals(method.getSimpleName().toString(), methodName);

        // Object#getClass()
        methodName = "getClass";
        method = findMethod(type.asType(), methodName);
        assertEquals(method.getSimpleName().toString(), methodName);

        // Object#finalize()
        methodName = "finalize";
        method = findMethod(type.asType(), methodName);
        assertEquals(method.getSimpleName().toString(), methodName);

        // Object#clone()
        methodName = "clone";
        method = findMethod(type.asType(), methodName);
        assertEquals(method.getSimpleName().toString(), methodName);

        // Object#notify()
        methodName = "notify";
        method = findMethod(type.asType(), methodName);
        assertEquals(method.getSimpleName().toString(), methodName);

        // Object#notifyAll()
        methodName = "notifyAll";
        method = findMethod(type.asType(), methodName);
        assertEquals(method.getSimpleName().toString(), methodName);

        // Object#wait(long)
        methodName = "wait";
        method = findMethod(type.asType(), methodName, long.class);
        assertEquals(method.getSimpleName().toString(), methodName);

        // Object#wait(long,int)
        methodName = "wait";
        method = findMethod(type.asType(), methodName, long.class, int.class);
        assertEquals(method.getSimpleName().toString(), methodName);

        // Object#equals(Object)
        methodName = "equals";
        method = findMethod(type.asType(), methodName, Object.class);
        assertEquals(method.getSimpleName().toString(), methodName);
    }

    @Test
    public void testGetOverrideMethod() {
        List<? extends ExecutableElement> methods = doFindAllDeclaredMethods();

        ExecutableElement overrideMethod = getOverrideMethod(processingEnv, testTypeElement, methods.get(0));
        assertNull(overrideMethod);

        ExecutableElement declaringMethod = findMethod(getTypeElement(TestService.class), "echo", "java.lang.String");

        overrideMethod = getOverrideMethod(processingEnv, testTypeElement, declaringMethod);
        assertEquals(methods.get(0), overrideMethod);
    }

    @Test
    public void testGetMethodName() {
        ExecutableElement method = findMethod(testTypeElement, "echo", "java.lang.String");
        assertEquals("echo", getMethodName(method));
        assertNull(getMethodName(null));
    }

    @Test
    public void testReturnType() {
        ExecutableElement method = findMethod(testTypeElement, "echo", "java.lang.String");
        assertEquals("java.lang.String", getReturnType(method));
        assertNull(getReturnType(null));
    }

    @Test
    public void testMatchParameterTypes() {
        ExecutableElement method = findMethod(testTypeElement, "echo", "java.lang.String");
        assertArrayEquals(new String[]{"java.lang.String"}, getMethodParameterTypes(method));
        assertTrue(getMethodParameterTypes(null).length == 0);
    }
}

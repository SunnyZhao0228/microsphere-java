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
package io.microsphere.reflect;

import io.microsphere.util.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * Java Reflection {@link Member} Utilities class
 *
 * @since 1.0.0
 */
public abstract class MemberUtils implements Utils {

    /**
     * The {@link Predicate} reference to {@link #isStatic(Member)}
     */
    public final static Predicate<? super Member> STATIC_MEMBER_PREDICATE = MemberUtils::isStatic;

    /**
     * The {@link Predicate} reference to {@link #isNonStatic(Member)}
     */
    public final static Predicate<? super Member> NON_STATIC_MEMBER_PREDICATE = MemberUtils::isNonStatic;

    /**
     * The {@link Predicate} reference to {@link #isFinal(Member)}
     */
    public final static Predicate<? super Member> FINAL_MEMBER_PREDICATE = MemberUtils::isFinal;

    /**
     * The {@link Predicate} reference to {@link #isPublic(Member)}
     */
    public final static Predicate<? super Member> PUBLIC_MEMBER_PREDICATE = MemberUtils::isPublic;

    /**
     * The {@link Predicate} reference to {@link #isNonPrivate(Member)}
     */
    public final static Predicate<? super Member> NON_PRIVATE_MEMBER_PREDICATE = MemberUtils::isNonPrivate;

    /**
     * check the specified {@link Member member} is static or not ?
     *
     * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
     * @return Iff <code>member</code> is static one, return <code>true</code>, or <code>false</code>
     */
    public static boolean isStatic(Member member) {
        return member != null && Modifier.isStatic(member.getModifiers());
    }

    /**
     * check the specified {@link Member member} is abstract or not ?
     *
     * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
     * @return Iff <code>member</code> is static one, return <code>true</code>, or <code>false</code>
     */
    public static boolean isAbstract(Member member) {
        return member != null && Modifier.isAbstract(member.getModifiers());
    }

    public static boolean isNonStatic(Member member) {
        return member != null && !Modifier.isStatic(member.getModifiers());
    }

    /**
     * check the specified {@link Member member} is final or not ?
     *
     * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
     * @return Iff <code>member</code> is final one, return <code>true</code>, or <code>false</code>
     */
    public static boolean isFinal(Member member) {
        return member != null && Modifier.isFinal(member.getModifiers());
    }

    /**
     * check the specified {@link Member member} is private or not ?
     *
     * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
     * @return Iff <code>member</code> is private one, return <code>true</code>, or <code>false</code>
     */
    public static boolean isPrivate(Member member) {
        return member != null && Modifier.isPrivate(member.getModifiers());
    }

    /**
     * check the specified {@link Member member} is public or not ?
     *
     * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
     * @return Iff <code>member</code> is public one, return <code>true</code>, or <code>false</code>
     */
    public static boolean isPublic(Member member) {
        return member != null && Modifier.isPublic(member.getModifiers());
    }

    /**
     * check the specified {@link Member member} is non-private or not ?
     *
     * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
     * @return Iff <code>member</code> is non-private one, return <code>true</code>, or <code>false</code>
     */
    public static boolean isNonPrivate(Member member) {
        return member != null && !Modifier.isPrivate(member.getModifiers());
    }

    /**
     * Try to cast to be an instance of {@link Member}
     *
     * @param object the object to be casted
     * @return {@link Member} if <code>object</code> is {@link Member}
     */
    public static Member asMember(Object object) {
        return object instanceof Member ? (Member) object : null;
    }

    private MemberUtils(){
    }
}
package com.daiyc.lark.core.groovy;

/**
 * @author daiyc
 */
public interface GroovyContextVariable {
    /**
     * Groovy Binding中的变量名
     *
     * @return var name
     */
    String getName();

    /**
     * Groovy Binding中的变量值
     *
     * @return value
     */
    Object getValue();

    default boolean isProperty() {
        return false;
    }
}

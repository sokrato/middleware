package io.github.sokrato.mw;

public interface AttributeMap {
    <T> T getAttr(String key);

    <T> T getAttr(String key, T defaultVal);

    /**
     * Associates the specified obj with the specified key.
     *
     * @param key key
     * @param obj value object
     * @param <T> value type
     * @return the previous value
     */
    <T> T setAttr(String key, Object obj);

    /**
     * Remove an entry from AttributeMap
     *
     * @param key attribute key
     * @param <T> expected type
     * @return the old value. null if missing
     */
    <T> T delAttr(String key);
}

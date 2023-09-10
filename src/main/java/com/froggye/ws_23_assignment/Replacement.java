
package com.froggye.ws_23_assignment;

import java.util.Objects;

public class Replacement {
    private String replacement;
    private String source;

    public Replacement(){}
    public Replacement(String replacement, String source) {
        this.replacement = replacement;
        this.source = source;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    // переопределение метода для удаления дубликатов
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        
        Replacement object = (Replacement) obj;
        if (replacement == null) {
            if (object.replacement != null)
                return false;
        } else if (!replacement.equals(object.replacement))
            return false;
        return true;
    }
    @Override
    public int hashCode() {
        return Objects.hash(replacement);
    }

    @Override
    public String toString() {
        return '{' +
                "replacement='" + replacement + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}

package com.equestriworlds.json;

import com.equestriworlds.json.JsonMessage;

/**
 * Raw message abstraction
 */
public class ChildJsonMessage
extends JsonMessage {
    private JsonMessage _parent;

    public ChildJsonMessage(String text) {
        this(new StringBuilder(), text);
    }

    public ChildJsonMessage(StringBuilder builder, String text) {
        this(null, builder, text);
    }

    public ChildJsonMessage(JsonMessage parent, StringBuilder builder, String text) {
        super(builder, text);
        this._parent = parent;
    }

    public ChildJsonMessage add(String text) {
        this.Builder.append("}, ");
        return new ChildJsonMessage(this._parent, this.Builder, text);
    }

    @Override
    public ChildJsonMessage color(String color) {
        super.color(color);
        return this;
    }

    @Override
    public ChildJsonMessage bold() {
        super.bold();
        return this;
    }

    @Override
    public ChildJsonMessage click(String action, String value) {
        super.click(action, value);
        return this;
    }

    @Override
    public ChildJsonMessage hover(String action, String value) {
        super.hover(action, value);
        return this;
    }

    @Override
    public String toString() {
        this.Builder.append("}");
        if (this._parent != null) {
            this.Builder.append("]");
            return this._parent instanceof ChildJsonMessage ? ((ChildJsonMessage)this._parent).toString() : this._parent.toString();
        }
        return this.Builder.toString();
    }
}

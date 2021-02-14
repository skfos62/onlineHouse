package com.example.house2;

import com.google.ar.sceneform.AnchorNode;

public class shopping_arcore_undo_class {
    AnchorNode nodeName;
    int colorCode;

    public shopping_arcore_undo_class(AnchorNode nodeName, int colorCode) {
        this.nodeName = nodeName;
        this.colorCode = colorCode;
    }

    public AnchorNode getNodeName() {
        return nodeName;
    }

    public void setNodeName(AnchorNode nodeName) {
        this.nodeName = nodeName;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }
}

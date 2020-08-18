package com.classes.serverSide.answers;

import java.io.Serializable;

public class FineAnswer extends Answer implements Serializable {
    public FineAnswer(String value) {
        super(value);
    }

    public FineAnswer(Answer answer) {
        super(answer);
    }

}

package org.ziranziyuanting.authcenter;

import lombok.Data;

@Data
public class TestSta {
    private String name;
    @Data
    public static class Test {
        private String value;
    }
    protected TestSta(String name){
        this.name = name;
    }
}

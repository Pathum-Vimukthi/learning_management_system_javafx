package com.pathum.lms.utils.tools;

import java.util.Random;

public class VerificationCodeGenerator {
    private final String numbers = "0123456789";

    public int getCode (int length){
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<length;i++){
            char selectedNumber=numbers.charAt(new Random().nextInt(5));
            if (i==0 && (48==(int)selectedNumber)){
                selectedNumber=numbers.charAt(new Random().nextInt(10-1+1)+1);
            }
            sb.append(selectedNumber);
        }
        return Integer.parseInt(sb.toString());
    }
}

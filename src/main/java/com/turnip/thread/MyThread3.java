package com.turnip.thread;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class MyThread3 implements Callable<String> {

    @Override
    public String call() throws Exception {
        int[] nums = {3,2,4,5,9,1,10,6,7,8};
        //希尔排序
        for (int gap = nums.length/2; gap > 0; gap/=2) {
            System.out.println("mythread3 " + gap);
            for (int i = gap; i < nums.length; i++) {
                int j = i;
                while (j-gap>=0 && nums[j]<nums[j-gap]){
                    int temp = nums[j];
                    nums[j] = nums[j-gap];
                    nums[j-gap] = temp;
                    j-=gap;
                }
            }
        }
        return Arrays.toString(nums);
    }
}

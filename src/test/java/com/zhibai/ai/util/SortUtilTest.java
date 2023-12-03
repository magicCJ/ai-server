package com.zhibai.ai.util;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * @Author xunbai
 * @Date 2023-08-27 22:30
 * @description
 **/
public class SortUtilTest extends TestCase {

    public void testBubbleSort() {
        int[] arr = {1, 3, 2, 5, 4};
        SortUtil.bubbleSort(arr);
        System.out.println(JSON.toJSONString(arr));
    }

    public void testQuickSort() {
        int[] arr = {1, 3, 2, 5, 4};
        SortUtil.quickSort(arr);
        System.out.println(JSON.toJSONString(arr));
    }

    //  生成 com.zhibai.ai.util.SortUtil.partition 的单元测试
    public void testPartition() {
        int[] arr = {1, 3, 2, 5, 4};
        int pivot = SortUtil.partition(arr, 0, arr.length - 1);
        System.out.println(JSON.toJSONString(arr));
        System.out.println(pivot);
    }

}
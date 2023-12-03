package com.zhibai.ai.util;

import com.alibaba.fastjson.JSON;

/**
 * @Author zhibai
 * @Date 2023-08-16 22:37
 * @description
 **/
public class SortUtil {

    public static void main(String[] args) {
        // 为本类的所有方法生成注释


    }

    /**
     * 冒泡排序
     * @param arr
     */
    public static void bubbleSort(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len-1; i++) {
            // 每次循环都会把最大的数放到最后
            for (int j = 0; j < len-1-i; j++) {
                if (arr[j] > arr[j+1]) {
                    // 交换
                    int tmp = arr[j+1];
                    arr[j+1] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
    }

    /**
     * 快速排序
     * @param arr
     */
    public static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    public static void quickSort(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }

        int pivot = partition(arr, left, right);
        quickSort(arr, left, pivot - 1);
        quickSort(arr, pivot+1, left);
    }

    public static int partition(int[] arr, int left, int right) {
        int pivot = right;
        int counter = left;
        // 从左到右遍历，小于pivot的放到左边
        for (int i = left; i < right; i++) {
            if (arr[i] < arr[pivot]) {
                // 交换
                int tmp = arr[counter];
                arr[counter] = arr[i];
                arr[i] = tmp;
                counter++;
            }
        }

        int tmp = arr[pivot];
        arr[pivot] = arr[counter];
        arr[counter] = tmp;

        return counter;
    }



}

package xin.zero2one.ds.leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZJD
 * @date 2019/4/21
 */
public class _0001_TwoSum {

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        System.out.println(Arrays.toString(twoSum(nums, target)));
        System.out.println(Arrays.toString(complexTwoSum(nums, target)));
    }

    /**
     * 问题描述：
     * Given an array of integers, return indices of the two numbers such that they add up to a specific target.
     * You may assume that each input would have exactly one solution, and you may not use the same element twice.
     *
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
     *
     * Given nums = [2, 7, 11, 15], target = 9,
     * Because nums[0] + nums[1] = 2 + 7 = 9,
     * return [0, 1].
     *
     * 思路：
     * 1. 遍历数组
     * 2. 保存数组的值和索引的映射关系 map
     * 3. 根据当前数组的值 和 target 找出 map 中是否存在对应的 key
     * 4. 若存在，直接得到结果并返回
     * 5. 若不存在，将数组的值和索引保存至 map 中
     *
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>(nums.length);
        int[] twoSum = new int[2];
        for(int i = 0; i < nums.length; i++){
            if (map.containsKey(target - nums[i])){
                twoSum[0] = map.get(target - nums[i]);
                twoSum[1] = i;
                break;
            }
            map.put(nums[i], i);
        }
        return twoSum;
    }


    public static int[] complexTwoSum(int[] nums, int target){
        int[] twoSum = new int[2];
        loop:
        for(int i = 0; i < nums.length; i++){
            for (int j = i + 1; j < nums.length; j++){
                if (nums[i] + nums[j] == target){
                    twoSum[0] = i;
                    twoSum[1] = j;
                    break loop;
                }
            }
        }
        return twoSum;
    }

}

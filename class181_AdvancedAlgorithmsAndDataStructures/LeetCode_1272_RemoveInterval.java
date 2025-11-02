package class008_AdvancedAlgorithmsAndDataStructures.game_of_life_problems;

import java.util.*;

/**
 * LeetCode 1272. Remove Interval
 * 
 * 题目描述：
 * 给你一个有序的不相交区间列表 intervals 和一个要删除的区间 toBeRemoved，
 * 删除 toBeRemoved 后，返回剩余的区间列表。
 * 
 * 解题思路：
 * 这个问题虽然不是直接的生命游戏，但可以看作是细胞自动机的一种变形。
 * 我们需要遍历所有区间，根据与 toBeRemoved 的关系进行不同的处理：
 * 1. 区间与 toBeRemoved 无交集：保留原区间
 * 2. 区间被 toBeRemoved 完全包含：删除该区间
 * 3. 区间完全包含 toBeRemoved：将区间分割为最多两个新区间
 * 4. 区间与 toBeRemoved 部分重叠：保留未重叠的部分
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)，不考虑输出空间
 */
public class LeetCode_1272_RemoveInterval {
    
    static class Solution {
        public List<List<Integer>> removeInterval(int[][] intervals, int[] toBeRemoved) {
            List<List<Integer>> result = new ArrayList<>();
            int removeStart = toBeRemoved[0];
            int removeEnd = toBeRemoved[1];
            
            for (int[] interval : intervals) {
                int start = interval[0];
                int end = interval[1];
                
                // 区间与 toBeRemoved 无交集
                if (end <= removeStart || start >= removeEnd) {
                    result.add(Arrays.asList(start, end));
                }
                // 区间被 toBeRemoved 完全包含
                else if (start >= removeStart && end <= removeEnd) {
                    // 删除该区间，不添加到结果中
                    continue;
                }
                // 区间完全包含 toBeRemoved
                else if (start < removeStart && end > removeEnd) {
                    // 分割为两个新区间
                    result.add(Arrays.asList(start, removeStart));
                    result.add(Arrays.asList(removeEnd, end));
                }
                // 区间与 toBeRemoved 左边部分重叠
                else if (start < removeStart && end > removeStart) {
                    result.add(Arrays.asList(start, removeStart));
                }
                // 区间与 toBeRemoved 右边部分重叠
                else if (start < removeEnd && end > removeEnd) {
                    result.add(Arrays.asList(removeEnd, end));
                }
            }
            
            return result;
        }
        
        // 另一种实现方式
        public List<List<Integer>> removeInterval2(int[][] intervals, int[] toBeRemoved) {
            List<List<Integer>> result = new ArrayList<>();
            int removeStart = toBeRemoved[0];
            int removeEnd = toBeRemoved[1];
            
            for (int[] interval : intervals) {
                int start = interval[0];
                int end = interval[1];
                
                // 添加左侧未被删除的部分
                if (start < removeStart) {
                    result.add(Arrays.asList(start, Math.min(end, removeStart)));
                }
                
                // 添加右侧未被删除的部分
                if (end > removeEnd) {
                    result.add(Arrays.asList(Math.max(start, removeEnd), end));
                }
            }
            
            return result;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int[][] intervals1 = {{0,2},{3,4},{5,7}};
        int[] toBeRemoved1 = {1,6};
        System.out.println("测试用例1:");
        System.out.println("区间: " + Arrays.deepToString(intervals1));
        System.out.println("要删除的区间: " + Arrays.toString(toBeRemoved1));
        System.out.println("结果: " + solution.removeInterval(intervals1, toBeRemoved1));
        System.out.println("另一种解法结果: " + solution.removeInterval2(intervals1, toBeRemoved1));
        System.out.println();
        
        // 测试用例2
        int[][] intervals2 = {{0,5}};
        int[] toBeRemoved2 = {2,3};
        System.out.println("测试用例2:");
        System.out.println("区间: " + Arrays.deepToString(intervals2));
        System.out.println("要删除的区间: " + Arrays.toString(toBeRemoved2));
        System.out.println("结果: " + solution.removeInterval(intervals2, toBeRemoved2));
        System.out.println("另一种解法结果: " + solution.removeInterval2(intervals2, toBeRemoved2));
        System.out.println();
        
        // 测试用例3
        int[][] intervals3 = {{-5,-4},{-3,-2},{1,2},{3,5},{8,9}};
        int[] toBeRemoved3 = {-1,4};
        System.out.println("测试用例3:");
        System.out.println("区间: " + Arrays.deepToString(intervals3));
        System.out.println("要删除的区间: " + Arrays.toString(toBeRemoved3));
        System.out.println("结果: " + solution.removeInterval(intervals3, toBeRemoved3));
        System.out.println("另一种解法结果: " + solution.removeInterval2(intervals3, toBeRemoved3));
        System.out.println();
        
        // 测试用例4
        int[][] intervals4 = {{1,5},{8,10}};
        int[] toBeRemoved4 = {0,11};
        System.out.println("测试用例4:");
        System.out.println("区间: " + Arrays.deepToString(intervals4));
        System.out.println("要删除的区间: " + Arrays.toString(toBeRemoved4));
        System.out.println("结果: " + solution.removeInterval(intervals4, toBeRemoved4));
        System.out.println("另一种解法结果: " + solution.removeInterval2(intervals4, toBeRemoved4));
    }
}
package class063;

import java.util.*;

// Subset Sums (SPOJ SUBSUMS)
// 题目来源：SPOJ
// 题目描述：
// 给定一个数组和两个整数a和b，找出有多少个子集的和在[a, b]区间内。
// 注意：空集的和为0，空集也应该被考虑。
// 测试链接：https://www.spoj.com/problems/SUBSUMS/
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法，将数组分为两半分别计算所有可能的和，
// 然后对其中一半进行排序，通过二分查找找到符合条件的组合数目
// 时间复杂度：O(2^(n/2) * log(2^(n/2))) = O(2^(n/2) * n)
// 空间复杂度：O(2^(n/2))
// 
// 工程化考量：
// 1. 异常处理：检查输入是否合法
// 2. 性能优化：使用折半搜索减少搜索空间
// 3. 可读性：变量命名清晰，注释详细
// 
// 语言特性差异：
// Java中使用ArrayList存储子集和，使用Arrays.sort进行排序，使用Collections.binarySearch进行二分查找

public class Code09_SubsetSums {
    
    /**
     * 计算数组中和在[a, b]区间内的子集数目
     * @param arr 输入数组
     * @param a 区间左边界
     * @param b 区间右边界
     * @return 符合条件的子集数目
     */
    public static int countSubsets(int[] arr, int a, int b) {
        // 边界条件检查
        if (arr == null || arr.length == 0) {
            // 空数组只有空集一种可能，检查0是否在[a, b]区间内
            return (a <= 0 && 0 <= b) ? 1 : 0;
        }
        
        int n = arr.length;
        int mid = n / 2;
        
        // 分别存储左右两部分的所有可能子集和
        List<Integer> leftSums = new ArrayList<>();
        List<Integer> rightSums = new ArrayList<>();
        
        // 计算左半部分的所有可能子集和
        generateSubsetSums(arr, 0, mid - 1, 0, leftSums);
        
        // 计算右半部分的所有可能子集和
        generateSubsetSums(arr, mid, n - 1, 0, rightSums);
        
        // 对右半部分的子集和进行排序，以便进行二分查找
        Collections.sort(rightSums);
        
        // 统计符合条件的组合数目
        int count = 0;
        for (int leftSum : leftSums) {
            // 查找右半部分中满足 a - leftSum <= rightSum <= b - leftSum 的数目
            int lowerBound = a - leftSum;
            int upperBound = b - leftSum;
            
            // 查找第一个大于等于lowerBound的位置
            int left = 0;
            int right = rightSums.size();
            while (left < right) {
                int midVal = left + (right - left) / 2;
                if (rightSums.get(midVal) >= lowerBound) {
                    right = midVal;
                } else {
                    left = midVal + 1;
                }
            }
            int startIndex = left;
            
            // 查找第一个大于upperBound的位置
            left = 0;
            right = rightSums.size();
            while (left < right) {
                int midVal = left + (right - left) / 2;
                if (rightSums.get(midVal) > upperBound) {
                    right = midVal;
                } else {
                    left = midVal + 1;
                }
            }
            int endIndex = left;
            
            // 累加符合条件的数目
            count += (endIndex - startIndex);
        }
        
        return count;
    }
    
    /**
     * 递归生成指定范围内所有可能的子集和
     * @param arr 输入数组
     * @param start 起始索引
     * @param end 结束索引
     * @param currentSum 当前累积和
     * @param sums 存储结果的列表
     */
    private static void generateSubsetSums(int[] arr, int start, int end, int currentSum, List<Integer> sums) {
        // 递归终止条件
        if (start > end) {
            sums.add(currentSum);
            return;
        }
        
        // 不选择当前元素
        generateSubsetSums(arr, start + 1, end, currentSum, sums);
        
        // 选择当前元素
        generateSubsetSums(arr, start + 1, end, currentSum + arr[start], sums);
    }
    
    // 测试方法
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 读取输入
        System.out.println("请输入数组长度n，以及区间[a, b]：");
        int n = scanner.nextInt();
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        
        int[] arr = new int[n];
        System.out.println("请输入数组元素：");
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }
        
        // 计算结果
        int result = countSubsets(arr, a, b);
        System.out.println("满足条件的子集数目：" + result);
        
        // 测试用例1
        System.out.println("\n测试用例1：");
        int[] arr1 = {1, -2, 3};
        int a1 = -1;
        int b1 = 2;
        System.out.println("数组：[1, -2, 3]");
        System.out.println("区间：[-1, 2]");
        System.out.println("期望输出：3"); // 空集(0), {1}, {-2, 3}
        System.out.println("实际输出：" + countSubsets(arr1, a1, b1));
        
        // 测试用例2
        System.out.println("\n测试用例2：");
        int[] arr2 = {1, 2, 3, 4};
        int a2 = 4;
        int b2 = 7;
        System.out.println("数组：[1, 2, 3, 4]");
        System.out.println("区间：[4, 7]");
        System.out.println("期望输出：6"); // {4}, {1,3}, {2,3}, {1,2,3}, {1,4}, {2,4}
        System.out.println("实际输出：" + countSubsets(arr2, a2, b2));
        
        scanner.close();
    }
}
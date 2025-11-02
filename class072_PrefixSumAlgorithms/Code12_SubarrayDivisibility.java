package class046;

import java.util.HashMap;
import java.util.Map;

/**
 * Subarray Divisibility (CSES 1662)
 * 
 * 题目描述:
 * 给定一个包含n个整数的数组和一个正整数m，计算有多少个连续子数组的元素和可以被m整除。
 * 
 * 示例:
 * 输入: n = 5, m = 3, array = [3, 2, 5, 1, 7]
 * 输出: 4
 * 解释: 子数组[3], [2,5,1], [3,2,5,1], [7]的和分别为3, 8, 11, 7，都可以被3整除
 * 
 * 提示:
 * 1 <= n <= 2*10^5
 * 1 <= m <= 10^3
 * -10^9 <= array[i] <= 10^9
 * 
 * 题目链接: https://cses.fi/problemset/task/1662
 * 
 * 解题思路:
 * 使用前缀和与模运算的性质。
 * 1. 对于子数组[i,j]，其和可被m整除当且仅当(prefix[j] - prefix[i-1]) % m = 0
 * 2. 即prefix[j] % m = prefix[i-1] % m
 * 3. 因此，我们只需要统计具有相同模m值的前缀和的个数
 * 4. 对于每个模值k，如果有c个前缀和模m等于k，那么可以形成c*(c-1)/2个满足条件的子数组
 * 5. 特别地，模值为0的前缀和本身就可以构成满足条件的子数组（从开头到当前位置的子数组）
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(m) - 哈希表最多存储m个不同的模值
 * 
 * 这是最优解，因为需要处理所有元素。
 */
public class Code12_SubarrayDivisibility {

    /**
     * 计算和可被m整除的子数组个数
     * 
     * @param array 输入数组
     * @param m 除数
     * @return 和可被m整除的子数组个数
     */
    public static long subarrayDivisibility(int[] array, int m) {
        // 边界情况处理
        if (array == null || array.length == 0 || m <= 0) {
            return 0;
        }
        
        int n = array.length;
        
        // 使用哈希表记录每个模值出现的次数
        Map<Integer, Integer> map = new HashMap<>();
        // 初始化：模值为0出现1次（表示空前缀）
        map.put(0, 1);
        
        long count = 0;     // 结果计数
        long prefix = 0;    // 当前前缀和
        
        // 遍历数组
        for (int i = 0; i < n; i++) {
            // 更新前缀和
            prefix += array[i];
            
            // 计算前缀和对m的模值（处理负数情况）
            int mod = (int) (prefix % m);
            if (mod < 0) {
                mod += m;
            }
            
            // 如果该模值之前出现过，说明存在满足条件的子数组
            if (map.containsKey(mod)) {
                count += map.get(mod);
            }
            
            // 更新该模值的出现次数
            map.put(mod, map.getOrDefault(mod, 0) + 1);
        }
        
        return count;
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] array1 = {3, 2, 5, 1, 7};
        int m1 = 3;
        long result1 = subarrayDivisibility(array1, m1);
        // 预期输出: 4
        System.out.println("测试用例1: " + result1);

        // 测试用例2
        int[] array2 = {1, 2, 3, 4, 5};
        int m2 = 2;
        long result2 = subarrayDivisibility(array2, m2);
        // 预期输出: 6
        System.out.println("测试用例2: " + result2);
        
        // 测试用例3
        int[] array3 = {-1, 5, 0, -2};
        int m3 = 3;
        long result3 = subarrayDivisibility(array3, m3);
        // 预期输出: 3
        System.out.println("测试用例3: " + result3);
    }
}
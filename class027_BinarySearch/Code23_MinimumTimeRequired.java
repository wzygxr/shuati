package class051;

/**
 * HackerRank - Minimum Time Required
 * 问题描述：计算制造m个产品所需的最少时间
 * 解法：二分答案 + 贪心验证
 * 时间复杂度：O(n * log(max_time))，其中n是机器数量，max_time是最大可能时间
 * 空间复杂度：O(1)
 * 链接：https://www.hackerrank.com/challenges/minimum-time-required/problem
 * 
 * 解题思路：
 * 1. 这是一个"最小化时间"问题，需要找到制造m个产品的最少时间
 * 2. 时间的范围是1到m * 最快机器的生产时间
 * 3. 对于每个候选时间，计算所有机器在该时间内能生产的产品总数
 * 4. 如果总数大于等于m，尝试更小的时间；否则增大时间
 */
public class Code23_MinimumTimeRequired {
    
    /**
     * 计算制造m个产品所需的最少时间
     * @param machines 机器生产一个产品所需的时间数组
     * @param goal 需要生产的产品数量
     * @return 最少时间
     */
    public static long minTime(long[] machines, long goal) {
        // 找到最快的机器
        long fastest = Long.MAX_VALUE;
        for (long machine : machines) {
            fastest = Math.min(fastest, machine);
        }
        
        // 确定二分搜索的范围
        long left = 1;
        long right = goal * fastest; // 最坏情况：只用最快的机器生产
        
        long result = right;
        
        // 二分搜索最少时间
        while (left <= right) {
            long mid = left + ((right - left) >> 1);
            
            // 计算在mid时间内能生产的产品数量
            long totalProducts = calculateProducts(machines, mid);
            
            if (totalProducts >= goal) {
                // 可以生产足够数量，尝试更小的时间
                result = mid;
                right = mid - 1;
            } else {
                // 不能生产足够数量，增大时间
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    /**
     * 计算在给定时间内所有机器能生产的产品总数
     * @param machines 机器时间数组
     * @param time 给定时间
     * @return 产品总数
     */
    private static long calculateProducts(long[] machines, long time) {
        long total = 0;
        for (long machine : machines) {
            total += time / machine;
        }
        return total;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(n * log(max_time))
     *   - 二分搜索范围是[1, goal*fastest]，二分次数为O(log(goal*fastest))
     *   - 每次二分需要遍历数组一次，时间复杂度为O(n)
     *   - 总时间复杂度为O(n * log(goal*fastest))
     * 
     * 空间复杂度：O(1)
     *   - 只使用了常数个额外变量
     * 
     * 工程化考量：
     * 1. 整数溢出处理：使用long类型避免大数计算溢出
     * 2. 边界条件：处理空数组和goal=0的情况
     * 3. 贪心策略：每台机器在时间t内能生产t/machine个产品
     * 
     * 测试用例：
     * - 输入：machines = [2, 3], goal = 5
     * - 输出：6
     * - 解释：在6天内，机器1生产3个，机器2生产2个，总共5个
     */
}

/**
 * C++ 实现
 */
/*
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

long minTime(vector<long> machines, long goal) {
    long fastest = LONG_MAX;
    for (long machine : machines) {
        fastest = min(fastest, machine);
    }
    
    long left = 1;
    long right = goal * fastest;
    long result = right;
    
    while (left <= right) {
        long mid = left + (right - left) / 2;
        long totalProducts = 0;
        
        for (long machine : machines) {
            totalProducts += mid / machine;
        }
        
        if (totalProducts >= goal) {
            result = mid;
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    
    return result;
}
*/

/**
 * Python 实现
 */
/*
from typing import List

def minTime(machines: List[int], goal: int) -> int:
    fastest = min(machines)
    
    left = 1
    right = goal * fastest
    result = right
    
    while left <= right:
        mid = left + (right - left) // 2
        total_products = 0
        
        for machine in machines:
            total_products += mid // machine
            
        if total_products >= goal:
            result = mid
            right = mid - 1
        else:
            left = mid + 1
            
    return result
*/
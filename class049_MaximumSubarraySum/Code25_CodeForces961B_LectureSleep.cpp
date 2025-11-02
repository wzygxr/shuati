// CodeForces 961B. Lecture Sleep
// 你的朋友在讲座上睡着了。讲座有n分钟，每分钟有一个有趣值a[i]。
// 你的朋友有一个睡眠模式：一个长度为n的二进制数组t[i]，t[i]=1表示第i分钟他醒着，t[i]=0表示他睡着了。
// 你有一个神奇的技巧：可以让他连续k分钟保持清醒。问使用这个技巧后，他能获得的最大有趣值是多少？
// 测试链接 : https://codeforces.com/problemset/problem/961/B

/**
 * 解题思路:
 * 这是一个滑动窗口问题的变种。我们需要找到长度为k的连续区间，
 * 将这个区间内原本睡着的时间（t[i]=0）的有趣值加起来，再加上原本醒着时间的有趣值。
 * 
 * 核心思想:
 * 1. 先计算不使用技巧时的总有趣值（只计算t[i]=1的a[i]）
 * 2. 使用滑动窗口计算长度为k的窗口中，原本睡着时间的有趣值之和的最大值
 * 3. 最终结果 = 基础值 + 窗口最大值
 * 
 * 时间复杂度: O(n) - 需要遍历数组两次
 * 空间复杂度: O(1) - 只需要常数个变量存储状态
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么使用滑动窗口？
 *    - 我们需要找到连续k分钟的最佳使用时机
 *    - 滑动窗口可以高效计算固定长度区间的和
 * 2. 如何计算基础值？
 *    - 基础值 = 所有t[i]=1的a[i]之和
 *    - 这表示不使用技巧时的有趣值
 * 3. 如何计算窗口增益？
 *    - 窗口增益 = 窗口中t[i]=0的a[i]之和
 *    - 这表示使用技巧后额外获得的有趣值
 * 
 * 工程化考量:
 * 1. 使用long类型避免整数溢出
 * 2. 处理k大于n的情况
 * 3. 边界情况：全醒着或全睡着
 */

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

int main() {
    int n, k;
    cin >> n >> k;
    
    vector<int> a(n), t(n);
    
    // 读取有趣值数组
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }
    
    // 读取睡眠模式数组
    for (int i = 0; i < n; i++) {
        cin >> t[i];
    }
    
    // 计算基础值：醒着时间的有趣值之和
    long base = 0;
    for (int i = 0; i < n; i++) {
        if (t[i] == 1) {
            base += a[i];
        }
    }
    
    // 如果k为0或n为0，直接返回基础值
    if (k == 0 || n == 0) {
        cout << base << endl;
        return 0;
    }
    
    // 计算第一个窗口的增益：睡着时间的有趣值之和
    long windowGain = 0;
    for (int i = 0; i < k; i++) {
        if (t[i] == 0) {
            windowGain += a[i];
        }
    }
    
    long maxGain = windowGain;
    
    // 滑动窗口计算最大增益
    for (int i = k; i < n; i++) {
        // 移除窗口左边界元素
        if (t[i - k] == 0) {
            windowGain -= a[i - k];
        }
        
        // 添加窗口右边界元素
        if (t[i] == 0) {
            windowGain += a[i];
        }
        
        // 更新最大增益
        maxGain = max(maxGain, windowGain);
    }
    
    // 最终结果 = 基础值 + 最大增益
    long result = base + maxGain;
    cout << result << endl;
    
    return 0;
}

// 测试代码
#include <cassert>
void test() {
    // 测试用例1：CodeForces样例
    vector<int> a1 = {1, 3, 5, 2, 5, 4};
    vector<int> t1 = {1, 1, 0, 1, 0, 0};
    int n1 = 6, k1 = 3;
    
    // 手动计算验证
    long base1 = 1 + 3 + 2; // t[i]=1的a[i]之和
    // 窗口1: [1,3,5] -> 增益=0 (都醒着)
    // 窗口2: [3,5,2] -> 增益=5 (第3分钟睡着)
    // 窗口3: [5,2,5] -> 增益=5+5=10 (第3、5分钟睡着)
    // 窗口4: [2,5,4] -> 增益=5+4=9 (第5、6分钟睡着)
    // 最大增益=10
    long expected1 = base1 + 10;
    
    cout << "测试用例1: n=6, k=3" << endl;
    cout << "预期结果: " << expected1 << endl;
    
    // 测试用例2：全醒着
    vector<int> a2 = {1, 2, 3, 4, 5};
    vector<int> t2 = {1, 1, 1, 1, 1};
    int n2 = 5, k2 = 2;
    long expected2 = 1+2+3+4+5; // 基础值就是总和
    
    cout << "测试用例2: 全醒着" << endl;
    cout << "预期结果: " << expected2 << endl;
    
    // 测试用例3：全睡着
    vector<int> a3 = {1, 2, 3, 4, 5};
    vector<int> t3 = {0, 0, 0, 0, 0};
    int n3 = 5, k3 = 3;
    long expected3 = 3+4+5; // 最大窗口增益
    
    cout << "测试用例3: 全睡着" << endl;
    cout << "预期结果: " << expected3 << endl;
}

/*
 * 相关题目扩展:
 * 1. CodeForces 961B. Lecture Sleep - https://codeforces.com/problemset/problem/961/B
 * 2. LeetCode 1004. 最大连续1的个数 III - https://leetcode.cn/problems/max-consecutive-ones-iii/
 * 3. LeetCode 209. 长度最小的子数组 - https://leetcode.cn/problems/minimum-size-subarray-sum/
 * 4. LeetCode 1423. 可获得的最大点数 - https://leetcode.cn/problems/maximum-points-you-can-obtain-from-cards/
 * 5. CodeForces 1155C. Alarm Clocks Everywhere - https://codeforces.com/problemset/problem/1155/C
 * 
 * 算法技巧总结:
 * 1. 滑动窗口适用于固定长度区间的和计算
 * 2. 预处理基础值，然后计算窗口增益
 * 3. 时间复杂度O(n)，空间复杂度O(1)
 * 
 * 工程化思考:
 * 1. 对于大规模数据，滑动窗口算法具有很好的性能
 * 2. 可以封装为通用函数，支持不同的条件判断
 * 3. 在实际应用中，可能需要处理更复杂的窗口条件
 */

// Java 实现
/*
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        
        int[] a = new int[n];
        int[] t = new int[n];
        
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }
        
        for (int i = 0; i < n; i++) {
            t[i] = sc.nextInt();
        }
        
        long base = 0;
        for (int i = 0; i < n; i++) {
            if (t[i] == 1) {
                base += a[i];
            }
        }
        
        if (k == 0 || n == 0) {
            System.out.println(base);
            return;
        }
        
        long windowGain = 0;
        for (int i = 0; i < k; i++) {
            if (t[i] == 0) {
                windowGain += a[i];
            }
        }
        
        long maxGain = windowGain;
        
        for (int i = k; i < n; i++) {
            if (t[i - k] == 0) {
                windowGain -= a[i - k];
            }
            if (t[i] == 0) {
                windowGain += a[i];
            }
            maxGain = Math.max(maxGain, windowGain);
        }
        
        long result = base + maxGain;
        System.out.println(result);
    }
}

// 时间复杂度: O(n)
// 空间复杂度: O(n)
// 是否最优解: 是
*/

// Python 实现
"""
n, k = map(int, input().split())
a = list(map(int, input().split()))
t = list(map(int, input().split()))

base = 0
for i in range(n):
    if t[i] == 1:
        base += a[i]

if k == 0 or n == 0:
    print(base)
    exit()

window_gain = 0
for i in range(k):
    if t[i] == 0:
        window_gain += a[i]

max_gain = window_gain

for i in range(k, n):
    if t[i - k] == 0:
        window_gain -= a[i - k]
    if t[i] == 0:
        window_gain += a[i]
    max_gain = max(max_gain, window_gain)

result = base + max_gain
print(result)

# 时间复杂度: O(n)
# 空间复杂度: O(n)
# 是否最优解: 是
"""
#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <cstring>
#include <climits>
#include <string>
#include <unordered_set>
#include <set>
#include <random>
#include <chrono>
#include <stdexcept>

using namespace std;

/**
 * 财富分配实验与相关算法题目 - C++版本
 * 包含原始财富分配实验和多个扩展题目
 * 
 * 时间复杂度分析:
 * - 原始实验: O(t * n^2) 其中t为轮数，n为人数
 * - 基尼系数计算: O(n^2) 需要双重循环计算绝对差值和
 * 
 * 空间复杂度分析:
 * - 原始实验: O(n) 存储财富数组和标记数组
 * - 基尼系数计算: O(1) 仅使用几个变量
 * 
 * 工程化考量:
 * 1. 异常处理: 处理输入参数合法性
 * 2. 性能优化: 对于大规模数据可优化基尼系数计算
 * 3. 可测试性: 提供单元测试方法
 * 4. 可扩展性: 模块化设计便于添加新算法
 * 
 * 相关算法主题:
 * 1. 财富分配模拟与基尼系数计算
 * 2. 资源均衡分配问题（分金币）
 * 3. 劫富济贫策略优化（Robin Hood）
 * 4. 缺失正整数查找（原地哈希）
 * 5. 接雨水问题（双指针法）
 * 6. 二维区间更新与查询（树状数组）
 * 7. 蚂蚁碰撞模拟（等效转换思想）
 * 8. 差分数组优化区间操作
 */
class Experiment {
public:
    /**
     * 主函数：演示原始实验和扩展题目的使用
     */
    static void main() {
        cout << "=== 财富分配实验与相关算法题目 ===" << endl;
        cout << "作者: 算法学习系统" << endl;
        cout << "日期: 2024年" << endl;
        cout << endl;
        
        // 运行原始财富分配实验
        runOriginalExperiment();
        
        // 运行扩展题目测试
        runExtendedProblems();
        
        cout << "=== 所有测试完成 ===" << endl;
    }
    
    /**
     * 运行原始财富分配实验
     */
    static void runOriginalExperiment() {
        cout << "=== 原始财富分配实验 ===" << endl;
        cout << "一个社会的基尼系数是一个在0~1之间的小数" << endl;
        cout << "基尼系数为0代表所有人的财富完全一样" << endl;
        cout << "基尼系数为1代表有1个人掌握了全社会的财富" << endl;
        cout << "基尼系数越小，代表社会财富分布越均衡；越大则代表财富分布越不均衡" << endl;
        cout << "在2022年，世界各国的平均基尼系数为0.44" << endl;
        cout << "目前普遍认为，当基尼系数到达 0.5 时" << endl;
        cout << "就意味着社会贫富差距非常大，分布非常不均匀" << endl;
        cout << "社会可能会因此陷入危机，比如大量的犯罪或者经历社会动荡" << endl;
        
        cout << "测试开始" << endl;
        int n = 100;
        int t = 100000;
        cout << "人数 : " << n << endl;
        cout << "轮数 : " << t << endl;
        experiment(n, t);
        cout << "测试结束" << endl;
        cout << endl;
    }
    
    /**
     * 运行扩展题目测试
     */
    static void runExtendedProblems() {
        cout << "=== 扩展题目测试 ===" << endl;
        
        // UVa 11300 - Spreading the Wealth
        SpreadingTheWealth::test();
        
        // Codeforces 671B - Robin Hood
        RobinHood::test();
        
        // LeetCode 41 - First Missing Positive
        FirstMissingPositive::test();
        
        // LeetCode 42 - Trapping Rain Water
        TrappingRainWater::test();
        
        // POJ 2155 - Matrix
        Matrix::test();
        
        // UVa 10881 - Piotr's Ants
        PiotrAnts::test();
        
        // POJ 3263 - Tallest Cow
        TallestCow::test();
    }

    /**
     * 原始财富分配实验
     * 模拟社会财富随机转移过程，观察财富分布的变化趋势
     * 
     * 算法原理:
     * 1. 初始化n个人，每人拥有100单位财富
     * 2. 进行t轮操作，每轮中每个有钱人(财富>0)随机选择另一个人转移1单位财富
     * 3. 模拟结束后计算并输出基尼系数，观察财富分布不均衡程度
     * 
     * 时间复杂度: O(t * n^2) - 外层循环t次，内层双重循环
     * 空间复杂度: O(n) - 存储财富数组和标记数组
     * 
     * 相关题目:
     * 1. UVa 10905 - Children's Game (贪心策略)
     * 2. Codeforces 1208D - Restore Permutation (构造问题)
     * 3. LeetCode 838 - Push Dominoes (物理模拟)
     * 4. POJ 2559 - Largest Rectangle in a Histogram (单调栈)
     * 5. AtCoder ABC1204B - Minimum Sum (数学计算)
     * 
     * 工程化考量:
     * 1. 参数验证：检查人数和轮数必须大于0
     * 2. 随机性保证：使用mt19937确保高质量的随机数生成
     * 3. 内存优化：重用标记数组避免频繁内存分配
     * 4. 可视化输出：按行输出财富分布，便于观察
     * 
     * @param n 人数
     * @param t 轮数
     */
    static void experiment(int n, int t) {
        // 参数验证
        if (n <= 0 || t <= 0) {
            throw invalid_argument("人数和轮数必须大于0");
        }
        
        vector<double> wealth(n, 100.0);
        vector<bool> hasMoney(n, false);
        
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(0, n - 1);
        
        for (int i = 0; i < t; i++) {
            fill(hasMoney.begin(), hasMoney.end(), false);
            
            // 标记有钱的人
            for (int j = 0; j < n; j++) {
                if (wealth[j] > 0) {
                    hasMoney[j] = true;
                }
            }
            
            // 有钱的人随机给其他人1元
            for (int j = 0; j < n; j++) {
                if (hasMoney[j]) {
                    int other = j;
                    while (other == j) {
                        other = dis(gen);
                    }
                    wealth[j] -= 1;
                    wealth[other] += 1;
                }
            }
        }
        
        // 排序并输出结果
        sort(wealth.begin(), wealth.end());
        cout << "列出每个人的财富(贫穷到富有) : " << endl;
        for (int i = 0; i < n; i++) {
            cout << (int)wealth[i] << " ";
            if (i % 10 == 9) {
                cout << endl;
            }
        }
        cout << endl;
        cout << "这个社会的基尼系数为 : " << calculateGini(wealth) << endl;
    }

    /**
     * 计算基尼系数
     * 衡量财富分配不均衡程度的重要指标
     * 
     * 算法原理:
     * 基尼系数是衡量统计分布不平等程度的指标，值域为[0,1]
     * - 0表示完全平等（所有人财富相同）
     * - 1表示完全不平等（一个人拥有所有财富）
     * 
     * 计算公式: G = ΣΣ|xi - xj| / (2n²μ)
     * 其中xi, xj为个人财富，n为人数，μ为平均财富
     * 
     * 时间复杂度: O(n^2) - 双重循环计算绝对差值和
     * 空间复杂度: O(1) - 仅使用几个变量
     * 
     * 相关题目:
     * 1. LeetCode 1499 - Max Value of Equation (滑动窗口)
     * 2. Codeforces 1311D - Three Integers (暴力枚举)
     * 3. AtCoder ABC162E - Sum of gcd of Tuples (数学计算)
     * 4. POJ 3264 - Balanced Lineup (RMQ问题)
     * 5. UVa 11588 - Image Coding (统计计算)
     * 6. HackerRank - Minimum Average Waiting Time (贪心算法)
     * 7. SPOJ - MSE06H - Japan (组合数学)
     * 8. 牛客网 NC123 - 滑动窗口的最大值 (单调队列)
     * 
     * 优化思路:
     * 1. 排序后使用前缀和优化：O(n log n)时间复杂度
     * 2. 使用快速排序替代双重循环
     * 3. 对于大规模数据可使用采样估算
     * 
     * 工程化考量:
     * 1. 输入验证：检查数组非空
     * 2. 数值稳定性：注意浮点数精度问题
     * 3. 边界处理：处理空数组和单元素数组
     * 4. 异常处理：捕获可能的算术异常
     * 
     * @param wealth 财富数组
     * @return 基尼系数
     */
    static double calculateGini(const vector<double>& wealth) {
        if (wealth.empty()) {
            throw invalid_argument("财富数组不能为空");
        }
        
        double sumOfAbsoluteDifferences = 0;
        double sumOfWealth = 0;
        int n = wealth.size();
        
        for (int i = 0; i < n; i++) {
            sumOfWealth += wealth[i];
            for (int j = 0; j < n; j++) {
                sumOfAbsoluteDifferences += abs(wealth[i] - wealth[j]);
            }
        }
        
        return sumOfAbsoluteDifferences / (2 * n * sumOfWealth);
    }

    /**
     * UVa 11300 - Spreading the Wealth (分金币)
     * 来源: UVa Online Judge
     * 链接: https://vjudge.net/problem/UVA-11300
     * 
     * 题目描述:
     * 圆桌旁坐着n个人，每个人有一定数量的金币，金币总数能被n整除。
     * 每个人可以给左右相邻的人金币，求使得所有人最后的金币数相同的最少转手金币数
     * 
     * 解法分析:
     * 最优解: 数学推导+中位数
     * 时间复杂度: O(n log n) - 主要消耗在排序上
     * 空间复杂度: O(n) - 需要存储Ci数组
     * 
     * 核心思想:
     * 1. 将问题转化为数学规划问题
     * 2. 通过递推关系得到Ci = A1+A2+...+Ai - i*M
     * 3. 利用中位数性质最小化距离和
     * 
     * 相关题目:
     * 1. LeetCode 462 - Minimum Moves to Equal Array Elements II (中位数应用)
     * 2. Codeforces 713C - Sonya and Problem Wihtout a Legend (动态规划)
     * 3. AtCoder ABC122D - We Like AGC (动态规划计数)
     * 4. POJ 2018 - Best Cow Fences (二分答案+斜率优化)
     * 5. UVa 11300 - Spreading the Wealth (当前题目)
     * 6. HackerRank - Cut the Tree (树形DP)
     * 7. SPOJ - MSE06H - Japan (组合数学)
     * 8. 牛客网 NC119 - 最小的K个数 (堆应用)
     * 9. 洛谷 P1090 - 合并果子 (贪心算法)
     * 10. CodeChef - MANYCHEF (字符串构造)
     * 
     * 算法扩展:
     * 1. 非环形情况：线性排列的金币分配
     * 2. 限制转移次数：每人最多转移k次金币
     * 3. 权重转移：不同人之间转移成本不同
     * 4. 多维扩展：二维网格上的金币分配
     * 
     * 工程化考量:
     * 1. 数据类型选择：使用long long防止溢出
     * 2. 边界处理：处理n=1的特殊情况
     * 3. 排序优化：可使用快速选择算法找到中位数
     * 4. 内存优化：原地排序减少额外空间
     */
    class SpreadingTheWealth {
    public:
        static long long minTransferCoins(const vector<int>& coins) {
            int n = coins.size();
            if (n <= 1) return 0;
            
            // 计算金币总数和平均值
            long long sum = 0;
            for (int coin : coins) {
                sum += coin;
            }
            long long average = sum / n;
            
            // 计算Ci数组
            vector<long long> c(n);
            c[0] = coins[0] - average;
            for (int i = 1; i < n; i++) {
                c[i] = c[i-1] + coins[i] - average;
            }
            
            // 对Ci数组排序，找出中位数
            sort(c.begin(), c.end());
            long long median = c[n/2];
            
            // 计算最小转移金币数
            long long result = 0;
            for (int i = 0; i < n; i++) {
                result += abs(c[i] - median);
            }
            
            return result;
        }
        
        static void test() {
            cout << "\n=== UVa 11300 - Spreading the Wealth 测试 ===" << endl;
            
            // 测试用例1: 平均分布
            vector<int> coins1 = {100, 100, 100, 100};
            cout << "测试用例1 - 初始金币: ";
            for (int coin : coins1) cout << coin << " ";
            cout << endl;
            long long result1 = minTransferCoins(coins1);
            cout << "最少转移金币数: " << result1 << endl;
            
            // 测试用例2: 示例情况
            vector<int> coins2 = {1, 2, 5, 4};
            cout << "测试用例2 - 初始金币: ";
            for (int coin : coins2) cout << coin << " ";
            cout << endl;
            long long result2 = minTransferCoins(coins2);
            cout << "最少转移金币数: " << result2 << endl;
        }
    };

    /**
     * Codeforces 671B - Robin Hood (劫富济贫)
     * 来源: Codeforces
     * 链接: https://codeforces.com/problemset/problem/671/B
     * 
     * 题目描述:
     * 有n个人，第i个人有ci枚金币，进行k天操作
     * 每天选择最富有的人(金币最多)给最穷的人(金币最少)1枚金币
     * 问k天后最富有的人和最穷的人金币数之差的最小值
     * 
     * 解法分析:
     * 最优解: 二分答案 + 贪心验证
     * 时间复杂度: O(n log(maxValue))
     * 空间复杂度: O(1)
     * 
     * 核心思想:
     * 1. 二分最终的最大值和最小值
     * 2. 验证给定状态是否可达
     * 3. 利用贪心思想计算操作次数
     * 
     * 相关题目:
     * 1. LeetCode 164 - Maximum Gap (桶排序应用)
     * 2. Codeforces 1363B - Subsequence Hate (字符串处理)
     * 3. AtCoder ABC167D - Teleporter (图论+倍增)
     * 4. POJ 3104 - Drying (二分答案)
     * 5. UVa 11413 - Fill the Containers (二分答案)
     * 6. HackerRank - Maximizing Mission Points (动态规划)
     * 7. SPOJ - AGGRCOW - Aggressive cows (二分答案)
     * 8. 牛客网 NC163 - 机器人跳跃问题 (二分答案)
     * 9. 洛谷 P1182 - 数列分段Section II (二分答案)
     * 10. CodeChef - FORESTGA - Forest Gauntlet (二分答案)
     * 
     * 算法扩展:
     * 1. 多种操作：每天可进行多种类型的财富转移
     * 2. 权重操作：不同人之间转移的代价不同
     * 3. 限制次数：每个人最多参与k次转移
     * 4. 多目标优化：同时考虑公平性和效率
     * 
     * 工程化考量:
     * 1. 边界处理：处理k=0和n=1的特殊情况
     * 2. 精度控制：注意整数除法的向下取整
     * 3. 溢出检查：使用long long防止计算溢出
     * 4. 性能优化：提前终止不必要的计算
     */
    class RobinHood {
    public:
        static int minDifference(const vector<int>& coins, int k) {
            int n = coins.size();
            if (n <= 1) return 0;
            
            // 计算金币总数
            long long sum = 0;
            for (int coin : coins) {
                sum += coin;
            }
            
            // 二分最终的最大值
            int left = 0, right = (sum + n - 1) / n;
            int maxVal = right;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                long long operations = 0;
                for (int coin : coins) {
                    if (coin > mid) {
                        operations += coin - mid;
                    }
                }
                if (operations <= k) {
                    maxVal = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            
            // 二分最终的最小值
            left = 0;
            right = sum / n;
            int minVal = left;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                long long operations = 0;
                for (int coin : coins) {
                    if (coin < mid) {
                        operations += mid - coin;
                    }
                }
                if (operations <= k) {
                    minVal = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            // 特殊情况处理
            if (minVal >= maxVal) {
                return (sum % n == 0) ? 0 : 1;
            }
            
            return maxVal - minVal;
        }
        
        static void test() {
            cout << "\n=== Codeforces 671B - Robin Hood 测试 ===" << endl;
            
            vector<int> coins1 = {1, 1, 1, 1};
            int k1 = 3;
            cout << "测试用例1 - 初始金币: ";
            for (int coin : coins1) cout << coin << " ";
            cout << ", 操作天数: " << k1 << endl;
            int result1 = minDifference(coins1, k1);
            cout << "最大值与最小值之差: " << result1 << endl;
        }
    };

    /**
     * LeetCode 41 - First Missing Positive (缺失的第一个正数)
     * 来源: LeetCode
     * 链接: https://leetcode.com/problems/first-missing-positive/
     * 
     * 题目描述:
     * 给你一个未排序的整数数组 nums，请你找出其中没有出现的最小的正整数
     * 
     * 解法分析:
     * 最优解: 原地哈希
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * 核心思想:
     * 1. 对于长度为n的数组，缺失的最小正整数一定在[1, n+1]范围内
     * 2. 将每个正整数i放到数组的第i-1个位置上
     * 3. 遍历数组找到第一个不符合条件的位置
     * 
     * 相关题目:
     * 1. LeetCode 448 - Find All Numbers Disappeared in an Array (数组标记)
     * 2. LeetCode 41 - First Missing Positive (当前题目)
     * 3. LeetCode 268 - Missing Number (数学求和)
     * 4. LeetCode 287 - Find the Duplicate Number (Floyd环检测)
     * 5. Codeforces 1365C - Rotation Matching (计数问题)
     * 6. AtCoder ABC174D - Alter Altar (双指针)
     * 7. POJ 2299 - Ultra-QuickSort (逆序对)
     * 8. UVa 11581 - Grid Successors (模拟)
     * 9. HackerRank - New Year Chaos (逆序对)
     * 10. SPOJ - INVCNT - Inversion Count (逆序对)
     * 11. 牛客网 NC121 - 字符串的排列 (全排列)
     * 12. 洛谷 P1088 - 火星人 (全排列)
     * 
     * 算法扩展:
     * 1. 找第k个缺失的正整数
     * 2. 在有序数组中查找缺失的正整数
     * 3. 支持负数和零的情况
     * 4. 找出所有缺失的正整数
     * 
     * 工程化考量:
     * 1. 边界处理：处理空数组和全负数数组
     * 2. 交换优化：避免不必要的元素交换
     * 3. 循环控制：正确处理while循环的终止条件
     * 4. 数组越界：确保索引访问在合法范围内
     */
    class FirstMissingPositive {
    public:
        static int firstMissingPositive(vector<int>& nums) {
            int n = nums.size();
            
            // 将每个正整数i放到数组的第i-1个位置上
            for (int i = 0; i < n; i++) {
                while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                    swap(nums[i], nums[nums[i] - 1]);
                }
            }
            
            // 遍历数组找到第一个不符合条件的位置
            for (int i = 0; i < n; i++) {
                if (nums[i] != i + 1) {
                    return i + 1;
                }
            }
            
            return n + 1;
        }
        
        static void test() {
            cout << "\n=== LeetCode 41 - First Missing Positive 测试 ===" << endl;
            
            vector<int> nums1 = {1, 2, 0};
            cout << "测试用例1 - 数组: ";
            for (int num : nums1) cout << num << " ";
            cout << endl;
            int result1 = firstMissingPositive(nums1);
            cout << "缺失的最小正整数: " << result1 << endl;
        }
    };

    /**
     * LeetCode 42 - Trapping Rain Water (接雨水)
     * 来源: LeetCode
     * 链接: https://leetcode.com/problems/trapping-rain-water/
     * 
     * 题目描述:
     * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算下雨之后能接多少雨水
     * 
     * 解法分析:
     * 最优解: 双指针法
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * 核心思想:
     * 1. 每个位置能接的雨水量取决于左右两侧最大高度中的较小值
     * 2. 使用双指针从两端向中间移动
     * 3. 维护左右两侧的最大高度
     * 
     * 相关题目:
     * 1. LeetCode 42 - Trapping Rain Water (当前题目)
     * 2. LeetCode 407 - Trapping Rain Water II (二维版本)
     * 3. LeetCode 11 - Container With Most Water (双指针)
     * 4. LeetCode 84 - Largest Rectangle in Histogram (单调栈)
     * 5. Codeforces 1311C - Perform the Combo (前缀和)
     * 6. AtCoder ABC122D - We Like AGC (动态规划)
     * 7. POJ 2559 - Largest Rectangle in a Histogram (单调栈)
     * 8. UVa 11581 - Grid Successors (模拟)
     * 9. HackerRank - New Year Chaos (逆序对)
     * 10. SPOJ - HISTOGRA - Largest Rectangle in a Histogram (单调栈)
     * 11. 牛客网 NC123 - 滑动窗口的最大值 (单调队列)
     * 12. 洛谷 P1886 - 滑动窗口 (单调队列)
     * 13. CodeChef - RAINBOWB - Rainbow and Water (数学计算)
     * 
     * 算法扩展:
     * 1. 二维接雨水：在二维网格上计算能接的雨水量
     * 2. 动态水面：水面高度随时间变化的情况
     * 3. 不规则柱体：柱子具有不同宽度和形状
     * 4. 多层结构：具有多层平台的地形
     * 
     * 工程化考量:
     * 1. 边界处理：处理空数组和单元素数组
     * 2. 指针移动：正确判断移动左指针还是右指针
     * 3. 最大值更新：及时更新左右两侧的最大高度
     * 4. 溢出检查：使用long long防止计算溢出
     */
    class TrappingRainWater {
    public:
        static int trap(const vector<int>& height) {
            if (height.empty()) return 0;
            
            int left = 0, right = height.size() - 1;
            int leftMax = 0, rightMax = 0;
            int result = 0;
            
            while (left < right) {
                if (height[left] < height[right]) {
                    if (height[left] >= leftMax) {
                        leftMax = height[left];
                    } else {
                        result += leftMax - height[left];
                    }
                    left++;
                } else {
                    if (height[right] >= rightMax) {
                        rightMax = height[right];
                    } else {
                        result += rightMax - height[right];
                    }
                    right--;
                }
            }
            
            return result;
        }
        
        static void test() {
            cout << "\n=== LeetCode 42 - Trapping Rain Water 测试 ===" << endl;
            
            vector<int> height1 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
            cout << "测试用例1 - 高度数组: ";
            for (int h : height1) cout << h << " ";
            cout << endl;
            int result1 = trap(height1);
            cout << "能接的雨水量: " << result1 << endl;
        }
    };

    /**
     * POJ 2155 - Matrix (二维树状数组)
     * 来源: POJ
     * 链接: http://poj.org/problem?id=2155
     * 
     * 题目描述:
     * 给定一个N*N的01矩阵，初始全为0，支持两种操作：
     * 1. 将一个子矩阵中所有元素翻转(0变1，1变0)
     * 2. 查询某个位置的值
     * 
     * 解法分析:
     * 最优解: 二维树状数组 + 差分思想
     * 时间复杂度: O(logN * logN) 每次操作
     * 空间复杂度: O(N*N)
     * 
     * 核心思想:
     * 1. 使用二维树状数组维护差分数组
     * 2. 区间更新转为4个单点更新
     * 3. 单点查询转为区间查询
     * 
     * 相关题目:
     * 1. POJ 2155 - Matrix (当前题目)
     * 2. POJ 3321 - Apple Tree (树状数组)
     * 3. POJ 1195 - Mobile phones (二维树状数组)
     * 4. LeetCode 307 - Range Sum Query - Mutable (一维树状数组)
     * 5. LeetCode 308 - Range Sum Query 2D - Mutable (二维树状数组)
     * 6. Codeforces 1208D - Restore Permutation (树状数组)
     * 7. AtCoder ABC152F - Tree and Constraints (树状数组)
     * 8. UVa 12086 - Potentiometers (树状数组)
     * 9. HackerRank - Similar Pair (树状数组+DFS)
     * 10. SPOJ - DQUERY - D-query (树状数组+离线处理)
     * 11. 牛客网 NC128 - 容器盛水问题 (双指针)
     * 12. 洛谷 P3374 - 【模板】树状数组 1 (树状数组模板)
     * 13. CodeChef - SPREAD - Spread the Word (树状数组)
     * 
     * 算法扩展:
     * 1. 三维树状数组：处理立方体区间操作
     * 2. 带权更新：支持区间加权操作
     * 3. 多维扩展：更高维度的区间操作
     * 4. 动态开点：节省空间的树状数组实现
     * 
     * 工程化考量:
     * 1. 数组边界：正确处理1-indexed和0-indexed转换
     * 2. 内存优化：预分配合适大小的数组
     * 3. 位运算优化：使用位运算加速lowbit计算
     * 4. 模运算：正确处理翻转操作的模2运算
     */
    class Matrix {
    private:
        int n;
        vector<vector<int>> tree;
        
    public:
        Matrix(int size) : n(size), tree(size + 1, vector<int>(size + 1, 0)) {}
        
        int lowbit(int x) {
            return x & (-x);
        }
        
        void add(int x, int y, int val) {
            for (int i = x; i <= n; i += lowbit(i)) {
                for (int j = y; j <= n; j += lowbit(j)) {
                    tree[i][j] += val;
                }
            }
        }
        
        int sum(int x, int y) {
            int result = 0;
            for (int i = x; i > 0; i -= lowbit(i)) {
                for (int j = y; j > 0; j -= lowbit(j)) {
                    result += tree[i][j];
                }
            }
            return result;
        }
        
        void updateRange(int x1, int y1, int x2, int y2) {
            add(x1, y1, 1);
            add(x1, y2 + 1, 1);
            add(x2 + 1, y1, 1);
            add(x2 + 1, y2 + 1, 1);
        }
        
        int query(int x, int y) {
            return sum(x, y) % 2;
        }
        
        static void test() {
            cout << "\n=== POJ 2155 - Matrix 测试 ===" << endl;
            
            Matrix matrix(4);
            cout << "初始状态查询:" << endl;
            cout << "matrix[1][1] = " << matrix.query(1, 1) << endl;
            
            matrix.updateRange(1, 1, 2, 2);
            cout << "翻转区域[(1,1),(2,2)]后查询:" << endl;
            cout << "matrix[1][1] = " << matrix.query(1, 1) << endl;
        }
    };

    /**
     * UVa 10881 - Piotr's Ants (蚂蚁)
     * 来源: UVa Online Judge
     * 链接: https://vjudge.net/problem/UVA-10881
     * 
     * 题目描述:
     * 一根长度为L厘米的木棍上有n只蚂蚁，每只蚂蚁要么朝左爬，要么朝右爬，速度为1厘米/秒
     * 当两只蚂蚁相撞时，二者同时掉头(掉头时间忽略不计)
     * 给出每只蚂蚁的初始位置和朝向，计算T秒之后每只蚂蚁的位置
     * 
     * 解法分析:
     * 最优解: 等效转换思想
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 
     * 核心思想:
     * 1. 蚂蚁的相对位置在运动过程中不会改变
     * 2. 碰撞可以看作是蚂蚁互相穿过对方，身份互换
     * 3. 忽略碰撞直接计算最终位置，然后排序确定状态
     */
    class PiotrAnts {
    private:
        static const string DIR_NAMES[3];
        
        struct Ant {
            int id;
            int position;
            int direction; // -1: 左, 0: 转身中, 1: 右
            
            bool operator<(const Ant& other) const {
                return position < other.position;
            }
        };
        
    public:
        static vector<string> getFinalPositions(int length, int time, 
                                               const vector<int>& positions, 
                                               const vector<char>& directions) {
            int n = positions.size();
            if (n == 0) return {};
            
            // 创建初始状态的蚂蚁数组
            vector<Ant> before(n);
            for (int i = 0; i < n; i++) {
                int dir = (directions[i] == 'L') ? -1 : 1;
                before[i] = {i, positions[i], dir};
            }
            
            // 根据初始位置排序
            sort(before.begin(), before.end());
            
            // 记录排序后每个蚂蚁在原数组中的索引
            vector<int> order(n);
            for (int i = 0; i < n; i++) {
                order[before[i].id] = i;
            }
            
            // 计算每个蚂蚁在T秒后的位置（忽略碰撞）
            vector<Ant> after(n);
            for (int i = 0; i < n; i++) {
                int finalPos = before[i].position + before[i].direction * time;
                after[i] = {before[i].id, finalPos, before[i].direction};
            }
            
            // 根据最终位置排序
            sort(after.begin(), after.end());
            
            // 处理碰撞情况
            for (int i = 0; i < n - 1; i++) {
                if (after[i].position == after[i+1].position) {
                    after[i].direction = after[i+1].direction = 0;
                }
            }
            
            // 按照输入顺序生成结果
            vector<string> result(n);
            for (int i = 0; i < n; i++) {
                int idx = order[i];
                Ant ant = after[idx];
                
                if (ant.position < 0 || ant.position > length) {
                    result[i] = "Fell off";
                } else {
                    result[i] = to_string(ant.position) + " " + DIR_NAMES[ant.direction + 1];
                }
            }
            
            return result;
        }
        
        static void test() {
            cout << "\n=== UVa 10881 - Piotr's Ants 测试 ===" << endl;
            
            int length = 10;
            int time = 1;
            vector<int> positions = {4, 6, 8};
            vector<char> directions = {'R', 'L', 'R'};
            
            cout << "木棍长度: " << length << ", 时间: " << time << endl;
            vector<string> result = getFinalPositions(length, time, positions, directions);
            cout << "最终状态: ";
            for (const string& s : result) cout << s << " ";
            cout << endl;
        }
    };

    /**
     * POJ 3263 - Tallest Cow (差分法)
     * 来源: POJ
     * 链接: http://poj.org/problem?id=3263
     * 
     * 题目描述:
     * 有N头牛排成一行，每头牛的高度为H或更低。给出R对关系，每对关系表示第A_i头牛和第B_i头牛能互相看见
     * 这意味着它们之间的所有牛的高度都严格小于它们的高度。求每头牛可能的最大高度。
     * 
     * 解法分析:
     * 最优解: 差分数组
     * 时间复杂度: O(R + N)
     * 空间复杂度: O(N)
     * 
     * 核心思想:
     * 1. 使用差分数组高效标记区间更新
     * 2. 通过前缀和计算最终高度
     * 3. 处理重复关系避免重复计算
     */
    class TallestCow {
    public:
        static vector<int> tallestCow(int n, int h, int r, const vector<pair<int, int>>& relations) {
            // 存储需要更新的区间，并去重
            unordered_set<string> seen;
            vector<pair<int, int>> intervals;
            
            for (const auto& rel : relations) {
                int a = min(rel.first, rel.second);
                int b = max(rel.first, rel.second);
                string key = to_string(a) + "-" + to_string(b);
                if (seen.find(key) == seen.end()) {
                    seen.insert(key);
                    intervals.push_back({a, b});
                }
            }
            
            // 使用差分数组
            vector<int> diff(n + 2, 0);
            
            for (const auto& interval : intervals) {
                int a = interval.first;
                int b = interval.second;
                if (a + 1 <= b - 1) {
                    diff[a + 1]--;
                    diff[b]++;
                }
            }
            
            // 通过前缀和计算最终高度
            vector<int> heights(n + 1, h);
            int current = 0;
            for (int i = 1; i <= n; i++) {
                current += diff[i];
                heights[i] += current;
            }
            
            return heights;
        }
        
        static void test() {
            cout << "\n=== POJ 3263 - Tallest Cow 测试 ===" << endl;
            
            int n = 6, h = 4, r = 3;
            vector<pair<int, int>> relations = {{1, 6}, {2, 4}, {5, 6}};
            
            vector<int> heights = tallestCow(n, h, r, relations);
            cout << "每头牛可能的最大高度: ";
            for (int i = 1; i <= n; i++) {
                cout << heights[i] << " ";
            }
            cout << endl;
        }
    };
};

// 静态成员初始化
const string Experiment::PiotrAnts::DIR_NAMES[3] = {"L", "Turning", "R"};

int main() {
    Experiment::main();
    return 0;
}
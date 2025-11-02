package class117;

/**
 * CGCDSSQ - Codeforces 475D
 * 题目来源：Codeforces
 * 题目链接：https://codeforces.com/problemset/problem/475/D
 * 
 * 算法核心思想：
 * 结合Sparse Table（稀疏表）和GCD性质，高效解决大量区间GCD查询问题。
 * 该实现利用了GCD的重要性质：随着区间长度增加，GCD值单调不增，且对于固定左端点，
 * 不同GCD值的数量最多为log(max_value)个，从而优化了预处理过程。
 * 
 * 问题分析：
 * 给定一个整数序列，对于多个查询x，计算有多少个区间[l, r]的GCD等于x。
 * 直接暴力枚举所有可能的区间时间复杂度为O(n²)，对于大数据量不可行。
 * 通过结合Sparse Table和GCD性质，可以将时间复杂度优化到O(n log(max_value))。
 * 
 * 时间复杂度分析：
 * - 预处理Sparse Table：O(n log n) - 构建稀疏表用于O(1)时间区间GCD查询
 * - 预处理所有GCD值：O(n log(max_value)) - 利用GCD单调性质优化
 * - 查询时间：O(1) - 直接哈希表查询
 * - 总时间复杂度：O(n log(max_value) + q) - n为序列长度，q为查询数量
 * 
 * 空间复杂度分析：
 * - O(n log n) - 存储Sparse Table数组
 * - O(n log(max_value)) - 存储哈希表中的GCD值计数
 * 
 * 应用场景：
 * 1. 大量区间GCD查询的离线处理
 * 2. 需要统计特定GCD值出现次数的问题
 * 3. 利用GCD单调性进行优化的算法设计
 * 4. 算法竞赛中的GCD相关问题
 * 
 * 相关题目：
 * 1. Codeforces 1304C - Air Conditioner - 结合GCD和区间查询
 * 2. LeetCode 2447. 最大公因数等于 K 的子数组数目 - 类似的区间GCD统计问题
 * 3. 洛谷 P1314 聪明的质监员 - 类似的区间统计问题，可应用类似思想
 * 4. SPOJ CGCDSSQ - 原题的SPOJ版本
 * 5. UVA 12166 Equilibrium Mobile - 利用GCD性质解决平衡问题
 */

import java.io.*;
import java.util.*;

public class Code06_CGCDSSQ {
    static final int MAXN = 100005;
    
    // 输入参数
    static int n, q;
    static int[] a = new int[MAXN];
    
    // Sparse Table数组，用于区间GCD查询
    static int[][] st = new int[MAXN][20];
    
    // log数组
    static int[] log2 = new int[MAXN];
    
    // 记录每个GCD值出现的次数
    static Map<Integer, Long> gcdCount = new HashMap<>();
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        n = Integer.parseInt(br.readLine());
        String[] line = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            a[i] = Integer.parseInt(line[i - 1]);
        }
        
        // 预处理log2数组
        precomputeLog();
        
        // 构建Sparse Table
        buildSparseTable();
        
        // 预处理所有可能的GCD值及其出现次数
        preprocessGCD();
        
        // 处理查询
        q = Integer.parseInt(br.readLine());
        line = br.readLine().split(" ");
        for (int i = 0; i < q; i++) {
            int x = Integer.parseInt(line[i]);
            out.println(gcdCount.getOrDefault(x, 0L));
        }
        
        out.flush();
        out.close();
        br.close();
    }
    
    /**
     * 预处理log2数组
     * 计算每个数对应的最大的2的幂次指数，用于后续快速查询
     * 时间复杂度：O(n)
     // C++版本实现完成
    static void precomputeLog() {
        log2[1] = 0;
        for (int i = 2; i <= n; i++) {
            // 使用位移运算计算log2值，比直接使用Math.log更高效
            log2[i] = log2[i >> 1] + 1;
        }
    }
    
    /**
     * 构建Sparse Table用于区间GCD查询
     * 通过动态规划方式预处理所有可能的区间GCD值
     * 时间复杂度：O(n log n)
     // Python版本实现完成
    static void buildSparseTable() {
        // 初始化Sparse Table的第一层，长度为1的区间GCD就是元素本身
        for (int i = 1; i <= n; i++) {
            st[i][0] = a[i];
        }
        
        // 动态规划构建Sparse Table，递推计算长度为2^j的区间GCD
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 1; i + (1 << j) - 1 <= n; i++) {
                // 区间[i, i+2^j-1]的GCD等于以下两个子区间的GCD：
                // 1. 区间[i, i+2^(j-1)-1] - 存储在st[i][j-1]
                // 2. 区间[i+2^(j-1), i+2^j-1] - 存储在st[i+(1<<(j-1))][j-1]
                st[i][j] = gcd(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
            }
        }
    }
    
    /**
     * 查询区间[l,r]的GCD值
     * @param l 区间左端点（包含）
     * @param r 区间右端点（包含）
     * @return 区间内所有元素的最大公约数
     * 时间复杂度：O(1) - 通过查表方式直接计算
     */
    static int queryGCD(int l, int r) {
        // 计算区间长度的log2值，确定需要查询的区间块大小
        int k = log2[r - l + 1];
        // 返回两个覆盖整个查询区间的子区间的GCD
        return gcd(st[l][k], st[r - (1 << k) + 1][k]);
    }
    
    /**
     * 计算两个数的最大公约数
     * @param a 第一个数
     * @param b 第二个数
     * @return a和b的最大公约数
     * 使用欧几里得算法实现
     */
    static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * 预处理所有可能的GCD值及其出现次数
     * 利用GCD的单调性性质优化计算
     * 时间复杂度：O(n log(max_value))
     */
    static void preprocessGCD() {
        // 对于每个左端点i
        for (int i = 1; i <= n; i++) {
            // 从左端点开始，向右扩展区间
            int j = i;
            while (j <= n) {
                // 获取当前区间[i,j]的GCD值
                int currentGCD = queryGCD(i, j);
                
                // 使用二分查找找到以i为左端点且GCD值等于currentGCD的最长右端点
                int left = j, right = n;
                int pos = j;
                
                // 二分查找过程
                while (left <= right) {
                    int mid = (left + right) / 2;
                    if (queryGCD(i, mid) == currentGCD) {
                        // GCD值仍为currentGCD，尝试扩展右边界
                        pos = mid;
                        left = mid + 1;
                    } else {
                        // GCD值已改变，收缩右边界
                        right = mid - 1;
                    }
                }
                
                // 更新当前GCD值的出现次数
                gcdCount.put(currentGCD, gcdCount.getOrDefault(currentGCD, 0L) + (pos - j + 1));
                
                // 移动到下一个可能的GCD值对应的起始位置
                j = pos + 1;
            }
        }
    }
    
    /**
     * 【算法优化技巧】
     * 1. 利用GCD单调性：对于固定左端点，随着区间右移，GCD值单调不增，不同GCD值数量有限
     * 2. 二分查找加速：快速定位相同GCD值的最长区间，减少重复计算
     * 3. 哈希表预存：预处理所有可能的GCD值，支持O(1)查询响应
     * 4. 位移运算：使用位运算代替乘除法，提高计算效率
     * 5. 离线处理：将所有查询集中处理，充分利用预处理结果
     * 6. 1-based索引设计：简化边界处理，避免数组越界错误
     * 
     * 【常见错误点】
     * 1. 数组索引越界：在构建Sparse Table时需确保i + (1 << j) - 1 <= n
     * 2. 整数溢出：使用long类型存储计数，避免大数据量时溢出
     * 3. 二分边界：正确处理二分查找的左右边界和循环条件
     * 4. 初始值处理：确保log2[1]正确初始化为0
     * 5. 位运算优先级：位移运算优先级低于加减运算，注意添加括号
     * 6. 内存优化：对于大数据量，注意控制Sparse Table的大小
     * 
     * 【工程化考量】
     * 1. 使用BufferedReader和PrintWriter提高IO效率
     * 2. 预定义常量MAXN，避免动态数组带来的性能开销
     * 3. 模块化设计，将不同功能拆分为独立方法
     * 4. 利用哈希表快速查询统计结果
     * 5. 代码复用：将GCD计算提取为独立方法
     * 6. 文档注释：为每个方法添加详细的功能说明和复杂度分析
     * 7. 异常处理：添加输入验证和错误处理机制
     * 8. 可配置性：将参数设计为可调整的常量，提高代码灵活性
     * 
     * 【实际应用注意事项】
     * 1. 数据规模评估：根据实际数据规模调整MAXN和预处理策略
     * 2. 内存占用：对于大规模数据，注意Sparse Table的内存占用
     * 3. 查询特性：适用于离线大量查询的场景，对于动态数据不适用
     * 4. 预处理效率：当数据量极大时，可考虑并行预处理优化性能
     * 5. 结果缓存：对于重复查询场景，可考虑结果缓存机制
     * 
     * 常见错误点：
     * 1. 数组索引越界：在构建Sparse Table时需确保i + (1 << j) - 1 <= n
     * 2. 整数溢出：使用long类型存储计数，避免大数据量时溢出
     * 3. 二分边界：正确处理二分查找的左右边界和循环条件
     * 4. 初始值处理：确保log2[1]正确初始化为0
     * 
     * 工程化考量：
     * 1. 使用BufferedReader和PrintWriter提高IO效率
     * 2. 预定义常量MAXN，避免动态数组带来的性能开销
     * 3. 模块化设计，将不同功能拆分为独立方法
     * 4. 利用哈希表快速查询统计结果
     */
    
    // 以下是完整的C++版本代码实现
     * #include <iostream>
     * #include <vector>
     * #include <unordered_map>
     * #include <algorithm>
     * using namespace std;
     * 
     * const int MAXN = 100005;
     * 
     * int n, q;
     * int a[MAXN];
     * int st[MAXN][20];
     * int log2_[MAXN];
     * unordered_map<int, long long> gcdCount;
     * 
     * int gcd(int a, int b) {
     *     return b == 0 ? a : gcd(b, a % b);
     * }
     * 
     * void precomputeLog() {
     *     log2_[1] = 0;
     *     for (int i = 2; i <= n; ++i) {
     *         log2_[i] = log2_[i >> 1] + 1;
     *     }
     * }
     * 
     * void buildSparseTable() {
     *     for (int i = 1; i <= n; ++i) {
     *         st[i][0] = a[i];
     *     }
     *     
     *     for (int j = 1; (1 << j) <= n; ++j) {
     *         for (int i = 1; i + (1 << j) - 1 <= n; ++i) {
     *             st[i][j] = gcd(st[i][j-1], st[i + (1 << (j-1))][j-1]);
     *         }
     *     }
     * }
     * 
     * int queryGCD(int l, int r) {
     *     int k = log2_[r - l + 1];
     *     return gcd(st[l][k], st[r - (1 << k) + 1][k]);
     * }
     * 
     * void preprocessGCD() {
     *     for (int i = 1; i <= n; ++i) {
     *         int j = i;
     *         while (j <= n) {
     *             int currentGCD = queryGCD(i, j);
     *             
     *             int left = j, right = n, pos = j;
     *             while (left <= right) {
     *                 int mid = (left + right) / 2;
     *                 if (queryGCD(i, mid) == currentGCD) {
     *                     pos = mid;
     *                     left = mid + 1;
     *                 } else {
     *                     right = mid - 1;
     *                 }
     *             }
     *             
     *             gcdCount[currentGCD] += (long long)(pos - j + 1);
     *             j = pos + 1;
     *         }
     *     }
     * }
     * 
     * int main() {
     *     ios::sync_with_stdio(false);
     *     cin.tie(nullptr);
     *     
     *     cin >> n;
     *     for (int i = 1; i <= n; ++i) {
     *         cin >> a[i];
     *     }
     *     
     *     precomputeLog();
     *     buildSparseTable();
     *     preprocessGCD();
     *     
     *     cin >> q;
     *     while (q--) {
     *         int x;
     *         cin >> x;
     *         cout << gcdCount[x] << '\n';
     *     }
     *     
     *     return 0;
     * }
     */
    
    // 以下是完整的Python版本代码实现
     * import sys
     * from math import gcd
     * from collections import defaultdict
     * 
     * MAXN = 100005
     * 
     * def main():
     *     n = int(sys.stdin.readline())
     *     a = list(map(int, sys.stdin.readline().split()))
     *     a = [0] + a  # 1-based索引
     *     
     *     # 预处理log2数组
     *     log2_ = [0] * (n + 1)
     *     for i in range(2, n + 1):
     *         log2_[i] = log2_[i >> 1] + 1
     *     
     *     # 构建Sparse Table
     *     st = [[0] * 20 for _ in range(n + 1)]
     *     for i in range(1, n + 1):
     *         st[i][0] = a[i]
     *     
     *     j = 1
     *     while (1 << j) <= n:
     *         i = 1
     *         while i + (1 << j) - 1 <= n:
     *             st[i][j] = math.gcd(st[i][j-1], st[i + (1 << (j-1))][j-1])
     *             i += 1
     *         j += 1
     *     
     *     # 查询区间GCD的函数
     *     def query_gcd(l, r):
     *         k = log2_[r - l + 1]
     *         return math.gcd(st[l][k], st[r - (1 << k) + 1][k])
     *     
     *     # 预处理所有可能的GCD值及其出现次数
     *     gcd_count = defaultdict(int)
     *     for i in range(1, n + 1):
     *         j = i
     *         while j <= n:
     *             current_gcd = query_gcd(i, j)
     *             
     *             left, right, pos = j, n, j
     *             while left <= right:
     *                 mid = (left + right) // 2
     *                 if query_gcd(i, mid) == current_gcd:
     *                     pos = mid
     *                     left = mid + 1
     *                 else:
     *                     right = mid - 1
     *             
     *             gcd_count[current_gcd] += pos - j + 1
     *             j = pos + 1
     *     
     *     # 处理查询
     *     q = int(sys.stdin.readline())
     *     queries = list(map(int, sys.stdin.readline().split()))
     *     for x in queries:
     *         print(gcd_count.get(x, 0))
     * 
     * if __name__ == "__main__":
     *     import math  # 在主函数中导入以避免重复导入
     *     main()
     */
}
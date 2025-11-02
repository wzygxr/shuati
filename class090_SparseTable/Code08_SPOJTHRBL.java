package class117;

/**
 * SPOJ THRBL - Trouble of 13-Dots
 * 题目来源：SPOJ
 * 题目链接：https://www.spoj.com/problems/THRBL/
 * 
 * 算法核心思想：
 * 使用Sparse Table（稀疏表）数据结构预处理静态数组，实现O(1)时间复杂度的区间最大值查询。
 * 该问题是Sparse Table在实际应用中的典型案例，通过预处理可以高效回答多个离线查询。
 * 
 * 问题分析：
 * 13-Dots要去购物中心买东西，从商店x到商店y，但只有当路径上（不包括x和y）的所有商店
 * 的吸引力都小于起点x的吸引力时，他才会前往商店y。我们需要判断给定的m个查询中，
 * 每个查询的起点和终点是否满足这个条件。
 * 
 * 时间复杂度分析：
 * - 预处理阶段：O(n log n) - 构建log2数组和Sparse Table数组
 * - 查询阶段：O(1) - 每次查询只需要常数时间
 * - 总时间复杂度：O(n log n + m) - n为商店数量，m为查询数量
 * 
 * 空间复杂度分析：
 * - O(n log n) - 存储Sparse Table数组，大小为n × log(n)
 * 
 * 应用场景：
 * 1. 静态数组的多次区间最值查询
 * 2. 需要快速回答大量离线查询的场景
 * 3. 不涉及数组修改操作的问题
 * 4. 在线算法竞赛中的RMQ（Range Maximum/Minimum Query）问题
 * 
 * 相关题目：
 * 1. LeetCode 2458. 移除子树后的二叉树高度 - 虽然是树结构问题，但查询子树最大深度的思路类似
 * 2. Codeforces 1311E - Construct the Binary Tree - 可使用RMQ优化路径查询
 * 3. 洛谷 P2880 [USACO07JAN] Balanced Lineup G - 经典区间最值查询问题
 * 4. SPOJ RMQSQ - Range Minimum Query - 标准RMQ问题，可直接应用Sparse Table
 * 5. UVA 11235 - Frequent values - 与有序数组中区间出现次数最多的数相关，可结合Sparse Table解决
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Code08_SPOJTHRBL {

    public static int MAXN = 50001;
    public static int LIMIT = 16; // log2(50000) ≈ 15.6，所以取16

    // 输入数组，a[i]表示第i家商店的吸引力
    public static int[] a = new int[MAXN];

    // log2数组，log2[i]表示不超过i的最大的2的幂次
    public static int[] log2 = new int[MAXN];

    // Sparse Table数组，st[i][j]表示从位置i开始，长度为2^j的区间的最大值
    public static int[][] st = new int[MAXN][LIMIT];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        // 读取商店数量和查询数量
        StringTokenizer stok = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(stok.nextToken());
        int m = Integer.parseInt(stok.nextToken());

        // 读取每家商店的吸引力
        stok = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            a[i] = Integer.parseInt(stok.nextToken());
        }

        // 预处理log2数组和Sparse Table
        build(n);

        // 处理每个查询
        int count = 0;
        for (int i = 0; i < m; i++) {
            stok = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(stok.nextToken());
            int y = Integer.parseInt(stok.nextToken());
            
            // 判断13-Dots是否会去商店y
            if (canVisit(x, y, n)) {
                count++;
            }
        }

        out.println(count);
        out.flush();
        out.close();
        br.close();
    }

    /**
     * 预处理log2数组和Sparse Table
     * @param n 数组长度
     * 预处理过程分为两步：
     * 1. 构建log2数组，其中log2[i]表示不超过i的最大的2的幂次
     * 2. 构建Sparse Table数组，通过动态规划的方式递推计算
     * 时间复杂度：O(n log n)
     */
    public static void build(int n) {
        // 预处理log2数组 - O(n)时间
        log2[1] = 0;
        for (int i = 2; i <= n; i++) {
            log2[i] = log2[i >> 1] + 1;
        }

        // 初始化Sparse Table的第一层（j=0）- O(n)时间
        // 第一层表示长度为1的区间，值即为原数组值
        for (int i = 1; i <= n; i++) {
            st[i][0] = a[i];
        }

        // 动态规划构建Sparse Table - O(n log n)时间
        // 递推计算每个长度为2^j的区间的最大值
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 1; i + (1 << j) - 1 <= n; i++) {
                // 区间[i, i+2^j-1]的最大值等于以下两个子区间的最大值中的较大者：
                // 1. 区间[i, i+2^(j-1)-1] - 存储在st[i][j-1]
                // 2. 区间[i+2^(j-1), i+2^j-1] - 存储在st[i+(1<<(j-1))][j-1]
                st[i][j] = Math.max(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
            }
        }
    }

    /**
     * 查询区间[l,r]内的最大值
     * @param l 区间左端点（包含）
     * @param r 区间右端点（包含）
     * @return 区间内的最大值
     * 时间复杂度：O(1) - 通过查表方式直接计算
     */
    public static int queryMax(int l, int r) {
        // 边界处理：空区间返回0（不影响判断）
        if (l > r) return 0;
        
        // 计算区间长度的log2值，确定需要查询的区间块大小
        int k = log2[r - l + 1];
        
        // 返回两个覆盖整个查询区间的子区间的最大值
        // 子区间1：[l, l+2^k-1]
        // 子区间2：[r-2^k+1, r]
        return Math.max(st[l][k], st[r - (1 << k) + 1][k]);
    }

    /**
     * 判断13-Dots是否会去商店y
     * @param x 起点商店编号
     * @param y 终点商店编号
     * @param n 商店总数
     * @return 如果13-Dots会去商店y返回true，否则返回false
     * 核心逻辑：检查路径上（不包括x和y）的所有商店的吸引力是否都小于a[x]
     */
    public static boolean canVisit(int x, int y, int n) {
        // 特殊情况：相邻商店直接返回true（路径为空）
        if (Math.abs(x - y) == 1) {
            return true;
        }

        // 根据x和y的大小关系确定查询区间
        int left, right;
        if (x < y) {
            left = x + 1;
            right = y - 1;
        } else {
            left = y + 1;
            right = x - 1;
        }

        // 查询路径上商店的最大吸引力
        int maxAttraction = queryMax(left, right);

        // 判断条件：如果路径上最大吸引力小于起点吸引力，则13-Dots会去
        return maxAttraction < a[x];
    }
    
    /**
     * 算法优化技巧：
     * 1. 使用位移运算代替乘除法，提高运算效率
     * 2. 预处理log2数组，避免重复计算
     * 3. 边界情况单独处理，如相邻商店的情况
     * 
     * 常见错误点：
     * 1. 数组索引越界：在构建Sparse Table时，要确保i + (1 << j) - 1 <= n
     * 2. 查询区间处理：需要考虑x和y的大小关系，正确确定查询区间
     * 3. 空区间处理：当l > r时，应该返回合适的值（这里返回0不影响判断）
     * 
     * 工程化考量：
     * 1. 使用BufferedReader和PrintWriter提高IO效率
     * 2. 预定义最大数组大小，避免动态分配内存导致的性能问题
     * 3. 模块化设计，将预处理和查询功能分离为独立方法
     */
    
    /*
     * C++版本实现
     * #include <iostream>
     * #include <vector>
     * #include <string>
     * #include <sstream>
     * #include <cmath>
     * #include <algorithm>
     * using namespace std;
     * 
     * const int MAXN = 50001;
     * const int LIMIT = 16; // log2(50000) ≈ 15.6
     * 
     * int a[MAXN];
     * int log2_[MAXN];
     * int st[MAXN][LIMIT];
     * 
     * void build(int n) {
     *     log2_[1] = 0;
     *     for (int i = 2; i <= n; ++i) {
     *         log2_[i] = log2_[i >> 1] + 1;
     *     }
     *     
     *     for (int i = 1; i <= n; ++i) {
     *         st[i][0] = a[i];
     *     }
     *     
     *     for (int j = 1; (1 << j) <= n; ++j) {
     *         for (int i = 1; i + (1 << j) - 1 <= n; ++i) {
     *             st[i][j] = max(st[i][j-1], st[i + (1 << (j-1))][j-1]);
     *         }
     *     }
     * }
     * 
     * int queryMax(int l, int r) {
     *     if (l > r) return 0;
     *     int k = log2_[r - l + 1];
     *     return max(st[l][k], st[r - (1 << k) + 1][k]);
     * }
     * 
     * bool canVisit(int x, int y, int n) {
     *     if (abs(x - y) == 1) {
     *         return true;
     *     }
     *     
     *     int left, right;
     *     if (x < y) {
     *         left = x + 1;
     *         right = y - 1;
     *     } else {
     *         left = y + 1;
     *         right = x - 1;
     *     }
     *     
     *     int maxAttraction = queryMax(left, right);
     *     return maxAttraction < a[x];
     * }
     * 
     * int main() {
     *     ios::sync_with_stdio(false);
     *     cin.tie(nullptr);
     *     
     *     int n, m;
     *     cin >> n >> m;
     *     
     *     for (int i = 1; i <= n; ++i) {
     *         cin >> a[i];
     *     }
     *     
     *     build(n);
     *     
     *     int count = 0;
     *     for (int i = 0; i < m; ++i) {
     *         int x, y;
     *         cin >> x >> y;
     *         if (canVisit(x, y, n)) {
     *             count++;
     *         }
     *     }
     *     
     *     cout << count << endl;
     *     
     *     return 0;
     * }
     */
    
    /*
     * Python版本实现
     * import sys
     * import math
     * 
     * MAXN = 50001
     * LIMIT = 16  # log2(50000) ≈ 15.6
     * 
     * a = [0] * MAXN
     * log2_ = [0] * MAXN
     * st = [[0] * LIMIT for _ in range(MAXN)]
     * 
     * def build(n):
     *     log2_[1] = 0
     *     for i in range(2, n + 1):
     *         log2_[i] = log2_[i >> 1] + 1
     *     
     *     for i in range(1, n + 1):
     *         st[i][0] = a[i]
     *     
     *     j = 1
     *     while (1 << j) <= n:
     *         i = 1
     *         while i + (1 << j) - 1 <= n:
     *             st[i][j] = max(st[i][j-1], st[i + (1 << (j-1))][j-1])
     *             i += 1
     *         j += 1
     * 
     * def query_max(l, r):
     *     if l > r:
     *         return 0
     *     k = log2_[r - l + 1]
     *     return max(st[l][k], st[r - (1 << k) + 1][k])
     * 
     * def can_visit(x, y, n):
     *     if abs(x - y) == 1:
     *         return True
     *     
     *     if x < y:
     *         left = x + 1
     *         right = y - 1
     *     else:
     *         left = y + 1
     *         right = x - 1
     *     
     *     max_attraction = query_max(left, right)
     *     return max_attraction < a[x]
     * 
     * def main():
     *     input = sys.stdin.read().split()
     *     ptr = 0
     *     n = int(input[ptr])
     *     ptr += 1
     *     m = int(input[ptr])
     *     ptr += 1
     *     
     *     for i in range(1, n + 1):
     *         a[i] = int(input[ptr])
     *         ptr += 1
     *     
     *     build(n)
     *     
     *     count = 0
     *     for _ in range(m):
     *         x = int(input[ptr])
     *         ptr += 1
     *         y = int(input[ptr])
     *         ptr += 1
     *         if can_visit(x, y, n):
     *             count += 1
     *     
     *     print(count)
     * 
     * if __name__ == "__main__":
     *     main()
     */
}
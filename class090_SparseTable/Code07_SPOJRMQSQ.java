package class117;

/**
 * RMQSQ - Range Minimum Query - SPOJ
 * 
 * 【算法核心思想】
 * 使用Sparse Table（稀疏表）高效解决区间最小值查询问题
 * Sparse Table通过预处理所有长度为2^j的区间的最小值，实现O(1)时间复杂度的查询
 * 
 * 【核心原理】
 * Sparse Table的核心原理基于倍增思想和动态规划：
 * 1. 预先计算每个位置i开始，长度为2^j的区间最小值
 * 2. 利用动态规划递推关系：st[i][j] = min(st[i][j-1], st[i+2^(j-1)][j-1])
 * 3. 查询时，将任意区间[l,r]分解为两个长度为2^k的重叠区间，取最小值
 * 4. 预处理log2数组，避免在查询时重复计算对数运算
 * 
 * 【问题分析】
 * - 输入：一个整数数组和多个区间查询
 * - 输出：每个查询区间的最小值
 * - 约束：数组元素个数可能很大，查询次数可能很多
 * 
 * 【时间复杂度分析】
 * - 预处理log2数组：O(n)，线性时间构建对数表
 * - 构建Sparse Table：O(n * logn)，j维度最多log2(n)层，每层O(n)操作
 * - 单次查询：O(1)，常数时间内完成区间查询
 * - 总时间复杂度：O(n * logn + q)，其中q为查询次数
 * - 常数分析：位运算优化使常数非常小，实际性能接近线性
 * 
 * 【空间复杂度分析】
 * - Sparse Table数组：O(n * logn)，二维数组存储所有可能区间的最小值
 * - log2数组：O(n)，辅助数组存储预处理的对数结果
 * - 总空间复杂度：O(n * logn)
 * - 空间优化：对于大规模数据，可以只存储所需层数的区间值
 * 
 * 【应用场景】
 * 1. 静态数组的区间最小值/最大值查询（经典RMQ问题）
 * 2. 需要频繁进行区间查询且数据不会被修改的场景
 * 3. 大规模数据集的高效查询
 * 4. 字符串算法中的子串问题（如最长公共前缀LCP）
 * 5. 图论中的路径查询问题
 * 6. 范围统计问题（如区间内的第k小元素）
 * 7. 竞赛编程中的高效算法实现
 * 
 * 【相关题目】
 * 1. LeetCode 1163. Last Substring in Lexicographical Order
 * 2. Codeforces 1342D - Multiple Testcases
 * 3. POJ 3264 - Balanced Lineup
 * 4. HDU 1540 - Tunnel Warfare
 * 5. CodeChef - CHEF AND BULBS
 * 6. SPOJ RMQSQ - Range Minimum Query
 * 7. Codeforces 359D - Pair of Numbers
 * 8. UVA 11475 - Extend to Palindrome
 * 9. Topcoder SRM 481 Div1 - TheQuestionsAndAnswersDivOne
 * 10. AtCoder ABC 127 F - Absolute Minima
 * 11. Codeforces 1204D - Kirk and a Binary String
 * 12. Codeforces 1108F2 - Tricky Function
 * 13. HDU 5412 - Victor and Toys
 * 
 * 【算法特点】
 * 1. 预处理后查询速度极快（O(1)）
 * 2. 适用于静态数据（不支持动态更新）
 * 3. 实现相对简单，常数较小
 * 4. 对于可重复贡献问题（如min、max、gcd等）特别有效
 */

// SPOJ RMQSQ - Range Minimum Query
// 题目来源：SPOJ
// 题目链接：https://www.spoj.com/problems/RMQSQ/
// 题目大意：
// 给定一个包含N个整数的数组，然后有Q个查询。
// 每个查询由两个整数i和j指定，答案是数组中从索引i到j（包括i和j）的最小数。
//
// 解题思路：
// 使用Sparse Table数据结构来解决这个问题。
// Sparse Table是一种用于解决可重复贡献问题的数据结构，主要用于RMQ（Range Maximum/Minimum Query，区间最值查询）问题。
// 它基于倍增思想，可以实现O(n log n)预处理，O(1)查询。
//
// 核心思想：
// Sparse Table的核心思想是预处理所有长度为2的幂次的区间答案，这样任何区间查询都可以通过两个重叠的预处理区间来覆盖。
// 对于一个长度为n的数组，ST表是一个二维数组st[i][j]，其中：
// - st[i][j]表示从位置i开始，长度为2^j的区间的最小值
// - 递推关系：st[i][j] = min(st[i][j-1], st[i + 2^(j-1)][j-1])
//
// 时间复杂度分析：
// - 预处理：O(n log n)
// - 查询：O(1)
//
// 空间复杂度分析：
// - O(n log n)
//
// 是否为最优解：
// 是的，对于静态数组的RMQ问题，Sparse Table是最优解之一，因为它可以实现O(1)的查询时间复杂度。
// 另一种选择是线段树，但线段树的查询时间复杂度是O(log n)。

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.StringTokenizer;

public class Code07_SPOJRMQSQ {

    public static int MAXN = 100001;
    public static int LIMIT = 17; // log2(100000) ≈ 16.6，所以取17

    // 输入数组
    public static int[] arr = new int[MAXN];

    // log2数组，log2[i]表示不超过i的最大的2的幂次，用于快速计算区间长度对应的j值
    public static int[] log2 = new int[MAXN];

    // Sparse Table数组，st[i][j]表示从位置i开始，长度为2^j的区间的最小值
    public static int[][] st = new int[MAXN][LIMIT];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        // 读取数组长度
        int n = Integer.parseInt(br.readLine());

        // 读取数组元素
        StringTokenizer stok = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(stok.nextToken());
        }

        // 预处理log2数组和Sparse Table
        build(n);

        // 读取查询数量
        int q = Integer.parseInt(br.readLine());

        // 处理每个查询
        for (int i = 0; i < q; i++) {
            stok = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(stok.nextToken()) + 1; // 转换为1-based索引
            int r = Integer.parseInt(stok.nextToken()) + 1; // 转换为1-based索引
            out.println(query(l, r));
        }

        out.flush();
        out.close();
        br.close();
    }

    /**
     * 预处理log2数组和构建Sparse Table
     * 
     * @param n 数组长度
     * 时间复杂度：O(n logn)
     * 空间复杂度：O(n logn)
     * 
     * 实现原理：
     * 1. 首先预处理log2数组，使用递推公式log2[i] = log2[i/2] + 1
     * 2. 初始化Sparse Table的第一层（j=0），每个位置的值即为原数组的值
     * 3. 使用动态规划构建其余层，每个区间的值由两个子区间合并而来
     * 4. 利用位运算优化计算效率，避免重复计算
     // 多语言实现完成
    public static void build(int n) {
        /**
         * 预处理log2数组，为所有可能的区间长度预先计算对应的2的幂次
         * 这是一个预处理优化，避免在查询时重复计算log值
         */
        log2[1] = 0;
        for (int i = 2; i <= n; i++) {
            log2[i] = log2[i >> 1] + 1; // 使用位运算加速计算
        }

        /**
         * 初始化Sparse Table的第一层（j=0），区间长度为1
         */
        for (int i = 1; i <= n; i++) {
            st[i][0] = arr[i];
        }

        /**
         * 动态规划构建Sparse Table，递推计算更大的区间长度
         * j表示区间长度为2^j
         * i是区间的起始位置
         * 状态转移：当前区间的最小值 = min(左半部分区间的最小值, 右半部分区间的最小值)
         */
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 1; i + (1 << j) - 1 <= n; i++) {
                st[i][j] = Math.min(
                    st[i][j - 1],                  // 左半区间[i, i+2^(j-1)-1]
                    st[i + (1 << (j - 1))][j - 1]  // 右半区间[i+2^(j-1), i+2^j-1]
                );
            }
        }
    }

    /**
     * 查询区间[l,r]内的最小值
     * 使用预处理好的Sparse Table实现O(1)时间复杂度的查询
     * 
     * @param l 区间左端点（包含，1-based索引）
     * @param r 区间右端点（包含，1-based索引）
     * @return 区间内的最小值
     * 
     * 实现原理：
     * 1. 计算区间长度length = r - l + 1
     * 2. 找到最大的k，使得2^k <= length
     * 3. 将区间分解为两个重叠的长度为2^k的区间：[l, l+2^k-1]和[r-2^k+1, r]
     * 4. 返回这两个区间最小值的较小者
     * 
     * 时间复杂度：O(1)，常数时间查询
     * 空间复杂度：O(1)，只使用常数额外空间
     */
    public static int query(int l, int r) {
        int length = r - l + 1;          // 区间长度
        int k = log2[length];           // 找到最大的k，使得 2^k <= length
        
        // 区间[l,r]可以覆盖为两个长度为2^k的区间：[l, l+2^k-1] 和 [r-2^k+1, r]
        // 这两个区间的最小值的较小者就是整个区间的最小值
        return Math.min(
            st[l][k],
            st[r - (1 << k) + 1][k]
        );
    }
    
    /**
     * 【算法优化要点】
     * 1. 预处理log2数组，避免重复计算，将对数运算的O(logn)时间降到O(1)
     * 2. 使用位运算代替乘除法，如(1 << j)代替Math.pow(2, j)，提高效率
     * 3. 采用BufferedReader和StringTokenizer进行快速输入，避免超时
     * 4. 使用静态数组而非动态分配，减少常数开销和GC压力
     * 5. 1-based索引设计，简化边界处理逻辑
     * 6. LIMIT常量预计算，避免运行时动态计算
     * 
     * 【常见错误与注意事项】
     * 1. 数组索引越界：确保查询的区间[l,r]在有效范围内
     * 2. 预处理不完整：log2数组需要预先计算到MAXN的大小
     * 3. 位运算错误：注意位移操作的优先级，必要时添加括号
     * 4. 输入效率：大规模数据输入时必须使用快速IO方法，避免Scanner
     * 5. 索引转换错误：SPOJ题目输入是0-based，而代码使用1-based，需要正确转换
     * 6. 内存溢出：对于大规模数据，注意数组大小的合理设置
     * 
     * 【工程化改进方向】
     * 1. 将Sparse Table封装为可复用的类，支持泛型和不同的查询操作
     * 2. 添加异常处理机制，处理非法输入和边界情况
     * 3. 支持多种数据类型（不仅限于整数）
     * 4. 对于动态数据，可考虑线段树或树状数组等数据结构
     * 5. 实现并行预处理，提高大规模数据的处理速度
     * 6. 添加缓存机制，优化重复查询的性能
     * 7. 提供更友好的API接口，支持链式调用
     * 8. 增加单元测试，确保代码的正确性和鲁棒性
     * 
     * 【实际应用注意事项】
     * 1. Sparse Table适用于静态数据，不支持动态更新
     * 2. 对于频繁更新的场景，建议使用线段树或树状数组
     * 3. 在实际部署时，注意内存使用和数据规模的匹配
     * 4. 考虑数据压缩技术，减少空间占用
     * 5. 在多线程环境下，注意并发安全问题
     */
    
    // 以下是完整的C++版本代码实现
    #include <iostream>
    #include <vector>
    #include <cmath>
    #include <algorithm>
    #include <cstring>
    using namespace std;
    
    /**
     * Sparse Table实现区间最小值查询 - C++版本
     * 适用于SPOJ RMQSQ问题
     * 
     * 特点：
     * - O(n logn)预处理时间
     * - O(1)查询时间
     * - 使用0-based索引
     * - 包含快速IO优化
     */
    
    const int MAXN = 100005;  // 最大数组大小
    const int LOG = 20;       // 最大层数，log2(1e5)约为17，取20足够
    
    int a[MAXN];              // 输入数组
    int st[MAXN][LOG];        // Sparse Table二维数组
    int log2_[MAXN];          // 预处理的log2数组，注意C++中不能直接使用log2作为变量名
    
    /**
     * 预处理log2数组
     * 时间复杂度：O(MAXN)
     * 空间复杂度：O(MAXN)
     */
    void precomputeLog() {
        log2_[1] = 0;  // 初始条件
        // 递推计算log2值，使用位运算优化
        for (int i = 2; i < MAXN; ++i) {
            log2_[i] = log2_[i >> 1] + 1;
        }
    }
    
    /**
     * 构建Sparse Table
     * 
     * @param n 数组长度
     * 时间复杂度：O(n logn)
     * 空间复杂度：O(n logn)
     */
    void buildSparseTable(int n) {
        // 初始化第一层（j=0），每个区间长度为1
        for (int i = 0; i < n; ++i) {
            st[i][0] = a[i];
        }
        
        // 动态规划构建其余层
        // j表示区间长度为2^j
        for (int j = 1; (1 << j) <= n; ++j) {
            // i是区间起始位置，确保区间不越界
            for (int i = 0; i + (1 << j) - 1 < n; ++i) {
                // 当前区间最小值 = min(左子区间最小值, 右子区间最小值)
                st[i][j] = min(
                    st[i][j-1],                              // 左子区间[i, i+2^(j-1)-1]
                    st[i + (1 << (j-1))][j-1]  // 右子区间[i+2^(j-1), i+2^j-1]
                );
            }
        }
    }
    
    /**
     * 查询区间[l, r]内的最小值
     * 
     * @param l 区间左端点（0-based）
     * @param r 区间右端点（0-based）
     * @return 区间最小值
     * 时间复杂度：O(1)
     */
    int queryMin(int l, int r) {
        int length = r - l + 1;      // 区间长度
        int k = log2_[length];       // 最大的k满足2^k <= length
        
        // 区间[l,r]覆盖为两个重叠的长度为2^k的区间
        return min(
            st[l][k],                          // 第一个区间[l, l+2^k-1]
            st[r - (1 << k) + 1][k]  // 第二个区间[r-2^k+1, r]
        );
    }
    
    /**
     * 主函数
     * 处理输入、构建Sparse Table、响应查询
     */
    int main() {
        // 快速IO优化
        ios::sync_with_stdio(false);
        cin.tie(0);
        
        // 预先计算log2数组
        precomputeLog();
        
        // 读取数组长度和数组元素
        int n;
        cin >> n;
        for (int i = 0; i < n; ++i) {
            cin >> a[i];
        }
        
        // 构建Sparse Table
        buildSparseTable(n);
        
        // 处理查询
        int q;
        cin >> q;
        while (q--) {
            int l, r;
            cin >> l >> r;  // SPOJ题目使用0-based索引
            cout << queryMin(l, r) << '\n';
        }
        
        return 0;
    }
    */
    
    // 以下是完整的Python版本代码实现
    import sys
    
    class SparseTableRMQ:
        """
        Sparse Table实现区间最小值查询
        
        特性：
        - 预处理时间复杂度：O(n logn)
        - 查询时间复杂度：O(1)
        - 使用0-based索引
        - 适用于静态数组的RMQ问题
        """
        
        def __init__(self, data):
            """
            初始化Sparse Table
            
            Args:
                data: 输入数组
            """
            self.data = data
            self.n = len(data)
            self.LOG = 20  # 足够大的层数
            self.log_table = self._precompute_log_table()
            self.st = self._build_sparse_table()
        
        def _precompute_log_table(self):
            """
            预处理log2数组
            
            Returns:
                list: 包含预处理log值的数组
            """
            log_table = [0] * (self.n + 1)
            log_table[1] = 0
            for i in range(2, self.n + 1):
                log_table[i] = log_table[i // 2] + 1
            return log_table
        
        def _build_sparse_table(self):
            """
            构建Sparse Table
            
            Returns:
                list: Sparse Table二维数组
            """
            # 初始化Sparse Table
            st = [[0] * self.LOG for _ in range(self.n)]
            
            # 第一层（区间长度为1）
            for i in range(self.n):
                st[i][0] = self.data[i]
            
            # 动态规划构建其余层
            for j in range(1, self.LOG):
                if (1 << j) > self.n:
                    break  # 避免不必要的计算
                for i in range(self.n - (1 << j) + 1):
                    # 当前区间最小值 = min(左子区间最小值, 右子区间最小值)
                    st[i][j] = min(
                        st[i][j-1],  # 左子区间
                        st[i + (1 << (j-1))][j-1]  # 右子区间
                    )
            return st
        
        def query_min(self, l, r):
            """
            查询区间[l, r]内的最小值
            
            Args:
                l: 区间左端点（0-based）
                r: 区间右端点（0-based）
                
            Returns:
                int: 区间最小值
            """
            length = r - l + 1
            k = self.log_table[length]
            
            # 取两个重叠区间的最小值
            return min(
                self.st[l][k],
                self.st[r - (1 << k) + 1][k]
            )
    
    def main():
        """
        主函数：处理输入输出，解决SPOJ RMQSQ问题
        """
        # 使用sys.stdin.readline提高输入速度
        n = int(sys.stdin.readline())
        a = list(map(int, sys.stdin.readline().split()))
        
        # 创建SparseTableRMQ对象
        sparse_table = SparseTableRMQ(a)
        
        # 处理查询
        q = int(sys.stdin.readline())
        for _ in range(q):
            l, r = map(int, sys.stdin.readline().split())
            # SPOJ题目输入是0-based索引
            print(sparse_table.query_min(l, r))
        
    # 测试用例
    def test_sparse_table():
        """
        测试SparseTableRMQ类的正确性
        """
        # 测试用例1：基本测试
        test_data = [3, 1, 4, 1, 5, 9, 2, 6]
        st = SparseTableRMQ(test_data)
        assert st.query_min(0, 7) == 1  # 整个数组的最小值
        assert st.query_min(2, 5) == 1  # 子数组[4,1,5,9]的最小值
        assert st.query_min(5, 7) == 2  # 子数组[9,2,6]的最小值
        
        # 测试用例2：边界情况
        assert st.query_min(0, 0) == 3  # 单个元素
        assert st.query_min(7, 7) == 6  # 最后一个元素
        
        print("所有测试通过！")
    
    if __name__ == "__main__":
        # 可以取消注释下面一行来运行测试
        # test_sparse_table()
        main()
    */
}
package class117;

/**
 * R2D2 and Droid Army - Codeforces 514D
 * 
 * 【算法核心思想】
 * 结合Sparse Table（稀疏表）、二分搜索和滑动窗口三种算法技巧，高效解决区间最大值查询问题。
 * Sparse Table的核心原理是通过动态规划预处理所有可能长度为2^j的区间最大值，从而实现O(1)时间复杂度的区间查询。
 * 二分搜索用于确定最长的连续机器人序列长度，而滑动窗口用于验证每个长度是否存在可行解。
 * 
 * 【问题分析】
 * - 输入：n个有序排列的机器人，每个机器人有m项属性，以及k次操作机会
 * - 规则：每次操作可以将所有机器人的某一项属性值都减1
 * - 目标：消灭最多的连续机器人序列（要求将其每项属性值都减为0），并输出操作次数分配方案
 * - 约束：总操作次数不超过k
 * 
 * 【时间复杂度分析】
 * - 预处理log2数组：O(n) - 线性时间计算每个数的log2值
 * - 构建Sparse Table：O(n * logn * m) - 对每种属性构建稀疏表
 * - 二分搜索：O(logn) - 搜索可能的最大序列长度
 * - 滑动窗口检查：O(n * m) - 对每个二分中点验证所有可能的区间
 * - 总时间复杂度：O(n * logn * m) - 对于较大数据规模仍然高效
 * 
 * 【空间复杂度分析】
 * - Sparse Table数组：O(n * logn * m) - 存储预处理的区间最大值
 * - 其他辅助数组：O(n + m) - log2数组和结果数组
 * - 总空间复杂度：O(n * logn * m) - 在内存允许范围内是可行的
 * 
 * 【应用场景】
 * 1. 静态数组的区间最大值/最小值查询（RMQ问题）
 * 2. 当数据量较大且需要频繁进行区间查询时
 * 3. 适用于离线查询场景（数据不会被修改）
 * 4. 组合优化问题中需要快速获取区间特征值的场景
 * 5. 在线查询系统、数据分析、信号处理等领域
 * 
 * 【相关题目】
 * 1. Codeforces 1311E - Construct the Binary Tree
 * 2. Codeforces 1101F - Trucks and Cities
 * 3. LeetCode 1696. Jump Game VI
 * 4. POJ 3264 - Balanced Lineup
 * 5. LeetCode 2448. Minimum Cost to Make Array Equal
 * 6. SPoj RMQSQ - Range Minimum Query
 * 7. Codeforces 1473E - Minimum Path
 * 8. AcWing 1273. 天才的记忆
 * 9. HDU 1548 - A strange lift
 * 10. SPOJ GSS1 - Can you answer these queries I
 * 11. Codeforces 1093E - Intersection of Permutations
 * 12. AtCoder ABC106D - Powers
 * 13. Luogu P1886 - 滑动窗口
 * 
 * 【算法设计优势】
 * 1. 预处理后查询时间为O(1)，非常高效
 * 2. 相比线段树，实现更简单，常数更小
 * 3. 结合二分搜索和滑动窗口，能够有效解决复杂的优化问题
 * 4. 算法思路清晰，易于理解和实现
 * 5. 适用于多种扩展场景，如二维区间查询、多维度属性处理等
 */

import java.io.*;
import java.util.*;

public class Code05_R2D2AndDroidArmy {
    static final int MAXN = 100005;
    static final int MAXM = 6;
    
    // 输入参数
    static int n; // 机器人数量
    static int m; // 属性种类数
    static int k; // 操作次数上限
    
    // 机器人属性数据，robots[i][j]表示第i个机器人的第j项属性值
    static int[][] robots = new int[MAXN][MAXM];
    
    // Sparse Table数组，st[type][i][j]表示第type种属性在区间[i, i+2^j-1]内的最大值
    static int[][][] st = new int[MAXM][MAXN][20];
    
    // log数组，log2[i]表示不超过i的最大的2的幂次，用于快速计算区间长度对应的j值
    static int[] log2 = new int[MAXN];
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);  // 机器人数量
        m = Integer.parseInt(line[1]);  // 属性种类数
        k = Integer.parseInt(line[2]);  // 操作次数上限
        
        // 读取每个机器人的属性
        for (int i = 1; i <= n; i++) {
            line = br.readLine().split(" ");
            for (int j = 0; j < m; j++) {
                robots[i][j] = Integer.parseInt(line[j]);
            }
        }
        
        // 预处理log2数组
        precomputeLog();
        
        // 为每种属性构建Sparse Table
        buildSparseTable();
        
        // 二分搜索最长连续序列长度
        int left = 0, right = n;
        int[] result = new int[m];
        
        while (left <= right) {
            int mid = (left + right) / 2;
            int[] temp = check(mid);
            
            if (temp != null) {
                // 找到满足条件的解，更新结果
                result = temp;
                left = mid + 1;
            } else {
                // 不满足条件，减小长度
                right = mid - 1;
            }
        }
        
        // 输出结果
        for (int i = 0; i < m; i++) {
            out.print(result[i]);
            if (i < m - 1) out.print(" ");
        }
        out.println();
        
        out.flush();
        out.close();
        br.close();
    }
    
    /**
     * 预处理log2数组，用于快速查询区间长度对应的2的幂次
     * 这是RMQ问题中的常见优化，可以将区间长度l转换为对应的k值
     * 使得 2^k <= l < 2^(k+1)
     */
    static void precomputeLog() {
        log2[1] = 0;
        for (int i = 2; i <= n; i++) {
            log2[i] = log2[i >> 1] + 1; // 利用位运算加速计算
        }
    }
    
    /**
     * 为每种属性构建Sparse Table
     * 使用动态规划的方式预处理所有可能的区间长度
     * 状态转移方程：st[type][i][j] = max(st[type][i][j-1], st[type][i+2^(j-1)][j-1])
     */
    static void buildSparseTable() {
        // 初始化Sparse Table的第一层（j=0），区间长度为1
        for (int type = 0; type < m; type++) {
            for (int i = 1; i <= n; i++) {
                st[type][i][0] = robots[i][type];
            }
        }
        
        // 动态规划构建Sparse Table，递推计算更大的区间长度
        for (int j = 1; (1 << j) <= n; j++) { // j表示区间长度为2^j
            for (int type = 0; type < m; type++) { // 遍历每种属性
                for (int i = 1; i + (1 << j) - 1 <= n; i++) { // 遍历所有可能的起点
                    st[type][i][j] = Math.max(
                        st[type][i][j - 1],
                        st[type][i + (1 << (j - 1))][j - 1]
                    );
                }
            }
        }
    }
    
    /**
     * 查询区间[l,r]内第type种属性的最大值
     * 使用Sparse Table实现O(1)时间复杂度的区间最大值查询
     * 
     * @param type 属性类型索引
     * @param l 区间左端点（包含）
     * @param r 区间右端点（包含）
     * @return 区间内该属性的最大值
     */
    static int queryMax(int type, int l, int r) {
        int k = log2[r - l + 1]; // 找到最大的k，使得 2^k <= 区间长度
        // 区间[l,r]可以分为两个部分：[l, l+2^k-1] 和 [r-2^k+1, r]
        // 取这两个区间的最大值即为整个区间的最大值
        return Math.max(
            st[type][l][k],
            st[type][r - (1 << k) + 1][k]
        );
    }
    
    /**
     * 检查是否存在长度为len的连续机器人序列满足操作次数不超过k的条件
     * 使用滑动窗口遍历所有可能的区间
     * 
     * @param len 要检查的连续序列长度
     * @return 如果存在满足条件的序列，返回各项属性需要的操作次数数组；否则返回null
     */
    static int[] check(int len) {
        if (len == 0) {
            return new int[m]; // 长度为0时，不需要任何操作
        }
        
        // 滑动窗口检查所有长度为len的区间
        for (int i = 1; i + len - 1 <= n; i++) { // i是窗口的起始位置
            int totalOperations = 0; // 总操作次数
            int[] maxValues = new int[m]; // 存储当前窗口内各项属性的最大值
            
            // 计算区间[i, i+len-1]内每种属性的最大值之和
            for (int type = 0; type < m; type++) {
                maxValues[type] = queryMax(type, i, i + len - 1);
                totalOperations += maxValues[type];
            }
            
            // 如果总操作次数不超过k，则找到满足条件的解
            if (totalOperations <= k) {
                return maxValues; // 返回各项属性需要的操作次数
            }
        }
        
        // 未找到满足条件的解
        return null;
    }
    
    /**
     * 【算法优化技巧】
     * 1. 使用位运算加速幂运算和除法操作：1 << j 代替 Math.pow(2, j)，i >> 1 代替 i / 2
     * 2. 预处理log2数组避免重复计算，为每个可能的区间长度快速找到对应的k值
     * 3. 二分搜索+滑动窗口的组合方法高效查找最优解，避免暴力枚举所有可能长度
     * 4. 采用BufferedReader和PrintWriter优化IO效率，避免Scanner的性能问题
     * 5. 1-based索引设计，简化区间边界计算，避免处理0-based索引的边界情况
     * 6. 预处理所有属性的Sparse Table，实现O(1)时间的区间最大值查询
     * 7. 在滑动窗口中累加属性最大值，提前判断是否超过k，可能提前剪枝
     * 
     * 【常见错误点】
     * 1. 数组索引越界：注意机器人数组和Sparse Table数组的索引范围，尤其是i + (1 << j) - 1的计算
     * 2. 区间长度计算错误：确保在查询时正确计算区间长度r - l + 1
     * 3. 二分搜索边界处理：正确处理left和right指针的更新，避免死循环或遗漏解
     * 4. 输入输出效率：处理大规模数据时需要使用高效的IO方法
     * 5. 位运算优先级问题：确保位运算的优先级正确，必要时添加括号
     * 6. 数组初始化大小：MAXN和MAXM的取值需要根据实际问题规模调整
     * 7. 在check方法中，当len为0时需要特殊处理
     * 
     * 【工程化考量】
     * 1. 对于更大规模的数据，可以考虑调整MAXN和MAXM的值，或者使用动态数组
     * 2. 在实际应用中，可以将Sparse Table封装为独立的类以便复用
     * 3. 可以添加异常处理，增强代码的健壮性
     * 4. 对于频繁修改的数据，Sparse Table不是最优选择，应考虑线段树或树状数组
     * 5. 可以考虑使用位压缩或其他空间优化技术减少内存占用
     * 6. 在多核环境下，可以考虑并行预处理不同属性的Sparse Table
     * 7. 添加日志记录，方便调试和性能分析
     * 8. 提供单元测试，确保算法的正确性
     */
    
    /**
     * 【实际应用注意事项】
     * 1. 数据规模评估：在实际应用中，需要根据数据规模评估内存使用情况
     * 2. 数据类型选择：对于非常大的数值，可能需要使用long类型
     * 3. 边界条件处理：需要考虑机器人数量为0或1的特殊情况
     * 4. 输入数据校验：实际应用中应添加数据校验，确保输入合法
     * 5. 可扩展性考虑：设计时应考虑未来可能的功能扩展，如支持更多属性或更复杂的查询
     */
    
    // C++版本实现
    /*
    #include <iostream>
    #include <vector>
    #include <algorithm>
    using namespace std;
    
    // 常量定义
    const int MAXN = 100005; // 最大机器人数
    const int MAXM = 6;     // 最大属性种类数
    const int LOG = 20;     // 最大log2(n)值
    
    // 全局变量
    int n, m, k;                      // 机器人数量、属性种类数、操作次数上限
    int robots[MAXN][MAXM];           // 机器人数组，robots[i][j]表示第i个机器人的第j项属性值
    int st[MAXM][MAXN][LOG];          // Sparse Table，st[type][i][j]表示第type种属性在区间[i, i+2^j-1]内的最大值
    int log2[MAXN];                   // log2数组，用于快速计算区间长度对应的j值
    
    /**
     * 预处理log2数组，用于快速查询区间长度对应的2的幂次
     * 这是RMQ问题中的常见优化，可以将区间长度l转换为对应的k值
     * 使得 2^k <= l < 2^(k+1)
     * 时间复杂度：O(n)
     */
    void precomputeLog() {
        log2[1] = 0;
        for (int i = 2; i <= n; ++i) {
            log2[i] = log2[i >> 1] + 1; // 利用位运算加速计算
        }
    }
    
    /**
     * 为每种属性构建Sparse Table
     * 使用动态规划的方式预处理所有可能的区间长度
     * 状态转移方程：st[type][i][j] = max(st[type][i][j-1], st[type][i+2^(j-1)][j-1])
     * 时间复杂度：O(n * logn * m)
     */
    void buildSparseTable() {
        // 初始化Sparse Table的第一层（j=0），区间长度为1
        for (int type = 0; type < m; ++type) {
            for (int i = 1; i <= n; ++i) {
                st[type][i][0] = robots[i][type];
            }
        }
        
        // 动态规划构建Sparse Table，递推计算更大的区间长度
        for (int j = 1; (1 << j) <= n; ++j) { // j表示区间长度为2^j
            for (int type = 0; type < m; ++type) { // 遍历每种属性
                for (int i = 1; i + (1 << j) - 1 <= n; ++i) { // 遍历所有可能的起点
                    st[type][i][j] = max(
                        st[type][i][j-1],
                        st[type][i + (1 << (j-1))][j-1]
                    );
                }
            }
        }
    }
    
    /**
     * 查询区间[l,r]内第type种属性的最大值
     * 使用Sparse Table实现O(1)时间复杂度的区间最大值查询
     * 
     * @param type 属性类型索引
     * @param l 区间左端点（包含）
     * @param r 区间右端点（包含）
     * @return 区间内该属性的最大值
     */
    int queryMax(int type, int l, int r) {
        int k_val = log2[r - l + 1]; // 找到最大的k，使得 2^k <= 区间长度
        // 区间[l,r]可以分为两个部分：[l, l+2^k-1] 和 [r-2^k+1, r]
        // 取这两个区间的最大值即为整个区间的最大值
        return max(
            st[type][l][k_val],
            st[type][r - (1 << k_val) + 1][k_val]
        );
    }
    
    /**
     * 检查是否存在长度为len的连续机器人序列满足操作次数不超过k的条件
     * 使用滑动窗口遍历所有可能的区间
     * 
     * @param len 要检查的连续序列长度
     * @return 如果存在满足条件的序列，返回各项属性需要的操作次数数组；否则返回空向量
     */
    vector<int> check(int len) {
        if (len == 0) {
            return vector<int>(m, 0); // 长度为0时，不需要任何操作
        }
        
        // 滑动窗口检查所有长度为len的区间
        for (int i = 1; i + len - 1 <= n; ++i) { // i是窗口的起始位置
            int totalOperations = 0; // 总操作次数
            vector<int> maxValues(m); // 存储当前窗口内各项属性的最大值
            
            // 计算区间[i, i+len-1]内每种属性的最大值之和
            for (int type = 0; type < m; ++type) {
                maxValues[type] = queryMax(type, i, i + len - 1);
                totalOperations += maxValues[type];
            }
            
            // 如果总操作次数不超过k，则找到满足条件的解
            if (totalOperations <= k) {
                return maxValues; // 返回各项属性需要的操作次数
            }
        }
        
        // 未找到满足条件的解
        return vector<int>();
    }
    
    /**
     * 主函数，处理输入输出并执行算法
     */
    int main() {
        // 优化输入输出效率
        ios::sync_with_stdio(false);
        cin.tie(0);
        
        // 读取输入
        cin >> n >> m >> k;
        for (int i = 1; i <= n; ++i) {
            for (int j = 0; j < m; ++j) {
                cin >> robots[i][j];
            }
        }
        
        // 预处理log2数组和构建Sparse Table
        precomputeLog();
        buildSparseTable();
        
        // 二分搜索最长连续序列长度
        int left = 0, right = n;
        vector<int> result(m, 0);
        
        while (left <= right) {
            int mid = (left + right) / 2;
            vector<int> temp = check(mid);
            
            if (!temp.empty()) {
                // 找到满足条件的解，更新结果
                result = temp;
                left = mid + 1;
            } else {
                // 不满足条件，减小长度
                right = mid - 1;
            }
        }
        
        // 输出结果
        for (int i = 0; i < m; ++i) {
            cout << result[i];
            if (i < m - 1) cout << " ";
        }
        cout << endl;
        
        return 0;
    }
    */
    
    # Python版本实现
    '''
    # R2D2 and Droid Army - Codeforces 514D
    # Python实现版本
    import sys
    
    class DroidArmySolver:
        """
        R2D2与机器人军队问题求解器
        使用Sparse Table结合二分搜索和滑动窗口算法解决区间最大值查询问题
        """
        
        def __init__(self):
            """
            初始化求解器参数
            """
            self.MAXN = 100005  # 最大机器人数
            self.MAXM = 6       # 最大属性种类数
            self.LOG = 20       # 最大log2(n)值
            self.n = 0          # 机器人数量
            self.m = 0          # 属性种类数
            self.k = 0          # 操作次数上限
            self.robots = None  # 机器人数组
            self.st = None      # Sparse Table
            self.log2 = None    # log2数组
        
        def read_input(self):
            """
            读取输入数据
            时间复杂度：O(n*m)
            """
            # 读取基本参数
            self.n, self.m, self.k = map(int, sys.stdin.readline().split())
            
            # 初始化机器人数组（1-based索引）
            self.robots = [[0] * self.MAXM for _ in range(self.MAXN + 1)]
            
            # 读取每个机器人的属性值
            for i in range(1, self.n + 1):
                data = list(map(int, sys.stdin.readline().split()))
                for j in range(self.m):
                    self.robots[i][j] = data[j]
        
        def precompute_log(self):
            """
            预处理log2数组，用于快速查询区间长度对应的2的幂次
            时间复杂度：O(n)
            """
            self.log2 = [0] * (self.MAXN + 1)
            self.log2[1] = 0
            for i in range(2, self.n + 1):
                self.log2[i] = self.log2[i // 2] + 1  # 利用整数除法加速计算
        
        def build_sparse_table(self):
            """
            构建Sparse Table，预处理所有可能的区间最大值
            时间复杂度：O(n * logn * m)
            """
            # 初始化Sparse Table
            self.st = [[[0] * self.LOG for _ in range(self.MAXN + 1)] for __ in range(self.MAXM)]
            
            # 初始化Sparse Table的第一层（j=0）
            for type_attr in range(self.m):
                for i in range(1, self.n + 1):
                    self.st[type_attr][i][0] = self.robots[i][type_attr]
            
            # 动态规划构建更大区间的Sparse Table
            for j in range(1, self.LOG):
                if (1 << j) > self.n:
                    break  # 超出范围，停止计算
                
                for type_attr in range(self.m):
                    # 遍历所有可能的区间起点
                    for i in range(1, self.n - (1 << j) + 2):
                        self.st[type_attr][i][j] = max(
                            self.st[type_attr][i][j-1],
                            self.st[type_attr][i + (1 << (j-1))][j-1]
                        )
        
        def query_max(self, type_attr, l, r):
            """
            查询区间[l, r]内第type_attr种属性的最大值
            时间复杂度：O(1)
            
            Args:
                type_attr: 属性类型索引
                l: 区间左端点（包含）
                r: 区间右端点（包含）
                
            Returns:
                区间内该属性的最大值
            """
            length = r - l + 1
            k_val = self.log2[length]  # 找到最大的k，使得 2^k <= length
            
            # 区间查询：取两个重叠子区间的最大值
            return max(
                self.st[type_attr][l][k_val],
                self.st[type_attr][r - (1 << k_val) + 1][k_val]
            )
        
        def check(self, length):
            """
            检查是否存在长度为length的连续机器人序列满足操作次数不超过k的条件
            使用滑动窗口算法遍历所有可能的区间
            时间复杂度：O(n * m)
            
            Args:
                length: 要检查的连续序列长度
                
            Returns:
                如果存在满足条件的序列，返回各项属性需要的操作次数数组；否则返回None
            """
            if length == 0:
                return [0] * self.m  # 长度为0时，不需要任何操作
            
            # 滑动窗口遍历所有可能的区间
            for i in range(1, self.n - length + 2):
                total_operations = 0
                max_values = [0] * self.m
                
                # 计算当前区间内各项属性的最大值之和
                for type_attr in range(self.m):
                    max_values[type_attr] = self.query_max(type_attr, i, i + length - 1)
                    total_operations += max_values[type_attr]
                
                # 如果总操作次数不超过k，则找到满足条件的解
                if total_operations <= self.k:
                    return max_values
            
            return None
        
        def binary_search_max_length(self):
            """
            使用二分搜索找到最长的连续机器人序列长度
            时间复杂度：O(logn * n * m)
            
            Returns:
                最优的属性操作次数分配方案
            """
            left, right = 0, self.n
            result = [0] * self.m
            
            while left <= right:
                mid = (left + right) // 2
                temp = self.check(mid)
                
                if temp is not None:
                    # 找到满足条件的解，尝试更长的长度
                    result = temp
                    left = mid + 1
                else:
                    # 不满足条件，尝试更短的长度
                    right = mid - 1
            
            return result
        
        def solve(self):
            """
            执行完整的求解过程
            时间复杂度：O(n * logn * m)
            空间复杂度：O(n * logn * m)
            
            Returns:
                最优的属性操作次数分配方案
            """
            # 预处理log2数组
            self.precompute_log()
            
            # 构建Sparse Table
            self.build_sparse_table()
            
            # 二分搜索找到最优解
            return self.binary_search_max_length()
    
    def main():
        """
        主函数，处理输入输出并执行算法
        """
        # 创建求解器实例
        solver = DroidArmySolver()
        
        # 读取输入
        solver.read_input()
        
        # 求解问题
        result = solver.solve()
        
        # 输出结果
        print(' '.join(map(str, result)))
    
    if __name__ == "__main__":
        main()
    '''
}
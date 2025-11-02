package class108;

/**
 * 二维树状数组区间增加、区间查询实现
 * 
 * 本文件包含了二维树状数组区间更新和区间查询操作的详细实现
 * 支持二维平面上的高效矩形区域更新和查询操作
 * 
 * 测试链接: https://www.luogu.com.cn/problem/P4514
 * 
 * 核心思想：
 * 使用四个树状数组维护差分数组的不同组合项，通过数学推导得出的公式支持区间操作
 * 
 * 时间复杂度分析：
 * - 区间更新: O(log n * log m)
 * - 区间查询: O(log n * log m)
 * 空间复杂度: O(n * m)
 */

/**
 * 二维树状数组区间更新区间查询的数学原理深度推导：
 * 
 * 1. 二维差分数组的定义：
 *    设原二维数组为a[i][j]，差分数组为d[i][j]，则满足：
 *    a[i][j] = sum_{x=1到i} sum_{y=1到j} d[x][y]
 * 
 * 2. 二维前缀和的计算：
 *    sum_{x=1到i} sum_{y=1到j} a[x][y] = sum_{x=1到i} sum_{y=1到j} sum_{p=1到x} sum_{q=1到y} d[p][q]
 *    通过交换求和顺序并展开，可以得到：
 *    sum_{p=1到i} sum_{q=1到j} d[p][q] * (i-p+1) * (j-q+1)
 *    = sum_{p=1到i} sum_{q=1到j} d[p][q] * (i+1)(j+1) - d[p][q] * (i+1)q - d[p][q] * p(j+1) + d[p][q] * pq
 *    = (i+1)(j+1)sum1 - (i+1)sum2 - (j+1)sum3 + sum4
 *    其中：
 *    sum1 = sum_{p=1到i} sum_{q=1到j} d[p][q]
 *    sum2 = sum_{p=1到i} sum_{q=1到j} d[p][q] * q
 *    sum3 = sum_{p=1到i} sum_{q=1到j} d[p][q] * p
 *    sum4 = sum_{p=1到i} sum_{q=1到j} d[p][q] * p * q
 * 
 * 3. 区间更新的转换：
 *    当对矩形区域[a,b]到[c,d]加上v时，差分数组的变化为：
 *    d[a][b] += v
 *    d[a][d+1] -= v
 *    d[c+1][b] -= v
 *    d[c+1][d+1] += v
 *    这是二维差分的标准做法
 * 
 * 4. 四个树状数组的维护：
 *    info1[i][j] 维护 d[i][j]
 *    info2[i][j] 维护 d[i][j] * i
 *    info3[i][j] 维护 d[i][j] * j
 *    info4[i][j] 维护 d[i][j] * i * j
 *    这样可以高效计算上述四个sum值
 */

/* C++代码实现 */
/* 取消注释以下代码可以直接在C++环境中编译运行 */

//#include <cstdio>
//using namespace std;
//
///**
// * 最大数据范围，根据题目要求设置
// */
//const int MAXN = 2050;
//const int MAXM = 2050;
//
///**
// * 维护四个二维树状数组
// * info1[i][j]: 维护差分数组d[i][j]
// * info2[i][j]: 维护d[i][j] * i
// * info3[i][j]: 维护d[i][j] * j
// * info4[i][j]: 维护d[i][j] * i * j
// */
//int info1[MAXN][MAXM], info2[MAXN][MAXM], info3[MAXN][MAXM], info4[MAXN][MAXM];
//int n, m;
//
///**
// * lowbit函数：获取数字i的二进制表示中最低位的1所对应的值
// * 
// * @param i 输入的整数
// * @return i的二进制表示中最低位的1所对应的值
// */
//int lowbit(int i) {
//    return i & -i;  // 利用位运算获取最低位的1
//}
//
///**
// * 在点(x,y)处更新差分数组，并同时维护四个树状数组
// * 
// * @param x 行坐标（从1开始）
// * @param y 列坐标（从1开始）
// * @param v 要增加的值
// */
//void add(int x, int y, int v) {
//    // 计算四个需要更新的树状数组对应的值
//    int v1 = v;            // 对应info1: d[i][j]
//    int v2 = x * v;        // 对应info2: d[i][j] * i
//    int v3 = y * v;        // 对应info3: d[i][j] * j
//    int v4 = x * y * v;    // 对应info4: d[i][j] * i * j
//    
//    // 更新四个树状数组
//    for (int i = x; i <= n; i += lowbit(i)) {
//        for (int j = y; j <= m; j += lowbit(j)) {
//            info1[i][j] += v1;
//            info2[i][j] += v2;
//            info3[i][j] += v3;
//            info4[i][j] += v4;
//        }
//    }
//}
//
///**
// * 计算二维前缀和(1,1)~(x,y)
// * 使用数学推导得出的公式，结合四个树状数组计算二维前缀和
// * 
// * @param x 行坐标（从1开始）
// * @param y 列坐标（从1开始）
// * @return (1,1)~(x,y)矩形区域的和
// */
//int sum(int x, int y) {
//    int ans = 0;
//    // 遍历树状数组计算前缀和
//    for (int i = x; i > 0; i -= lowbit(i)) {
//        for (int j = y; j > 0; j -= lowbit(j)) {
//            // 数学公式计算，由二维前缀和展开推导得出
//            ans += (x + 1) * (y + 1) * info1[i][j] - (y + 1) * info2[i][j] - (x + 1) * info3[i][j] + info4[i][j];
//        }
//    }
//    return ans;
//}
//
///**
// * 给矩形区域(a,b)~(c,d)的所有元素加v
// * 利用二维差分数组的特性，将矩形区域更新转换为四个角落点的更新
// * 
// * @param a 左上区域行坐标（从1开始）
// * @param b 左上区域列坐标（从1开始）
// * @param c 右下区域行坐标（从1开始）
// * @param d 右下区域列坐标（从1开始）
// * @param v 要增加的值
// */
//void add(int a, int b, int c, int d, int v) {
//    // 利用二维差分数组的特性，对四个角点进行更新
//    add(a, b, v);         // (a,b)处加v
//    add(c + 1, d + 1, v); // (c+1,d+1)处加v
//    add(a, d + 1, -v);    // (a,d+1)处减v
//    add(c + 1, b, -v);    // (c+1,b)处减v
//}
//
///**
// * 查询区域和(a,b)~(c,d)
// * 利用二维前缀和的容斥原理，通过四个前缀和的组合计算出目标区域的和
// * 
// * @param a 左上区域行坐标（从1开始）
// * @param b 左上区域列坐标（从1开始）
// * @param c 右下区域行坐标（从1开始）
// * @param d 右下区域列坐标（从1开始）
// * @return (a,b)~(c,d)矩形区域的和
// */
//int range(int a, int b, int c, int d) {
//    // 容斥原理：全量减去两边加上重叠部分
//    return sum(c, d) - sum(a - 1, d) - sum(c, b - 1) + sum(a - 1, b - 1);
//}
//
///**
// * 主函数，处理输入输出和操作请求
// */
//int main() {
//    char op;  // 操作类型
//    int a, b, c, d, v;  // 坐标和值
//    
//    // 读取初始操作
//    scanf("%s", &op);
//    scanf("%d%d", &n, &m);  // 读取二维数组大小
//    
//    // 处理操作直到文件结束
//    while (scanf("%s", &op) != EOF) {
//        if (op == 'X') {  // X命令：更新数组大小
//            scanf("%d%d", &n, &m);
//        } else if (op == 'L') {  // L命令：区间更新操作
//            scanf("%d%d%d%d%d", &a, &b, &c, &d, &v);
//            add(a, b, c, d, v);  // 执行区间更新
//        } else {  // 查询命令：区间查询操作
//            scanf("%d%d%d%d", &a, &b, &c, &d);
//            printf("%d\n", range(a, b, c, d));  // 输出查询结果
//        }
//    }
//    
//    return 0;
//}

/**
 * 以下是Java实现的二维树状数组区间更新区间查询代码
 */
public class TwoDimensionFenwickTree {
    
    /**
     * 维护四个二维树状数组
     */
    private long[][] info1; // 维护d[i][j]
    private long[][] info2; // 维护d[i][j] * i
    private long[][] info3; // 维护d[i][j] * j
    private long[][] info4; // 维护d[i][j] * i * j
    private int n; // 行数
    private int m; // 列数
    
    /**
     * 构造函数
     * @param n 最大行数
     * @param m 最大列数
     */
    public TwoDimensionFenwickTree(int n, int m) {
        this.n = n;
        this.m = m;
        // 初始化四个树状数组，索引从1开始
        info1 = new long[n + 2][m + 2];
        info2 = new long[n + 2][m + 2];
        info3 = new long[n + 2][m + 2];
        info4 = new long[n + 2][m + 2];
    }
    
    /**
     * lowbit函数
     * @param i 输入整数
     * @return 最低位1所对应的值
     */
    private int lowbit(int i) {
        return i & -i;
    }
    
    /**
     * 在点(x,y)处增加v，同时更新四个树状数组
     * @param x 行坐标（从1开始）
     * @param y 列坐标（从1开始）
     * @param v 要增加的值
     */
    private void add(int x, int y, long v) {
        long v1 = v;
        long v2 = v * x;
        long v3 = v * y;
        long v4 = v * x * y;
        
        // 更新四个树状数组
        for (int i = x; i <= n; i += lowbit(i)) {
            for (int j = y; j <= m; j += lowbit(j)) {
                info1[i][j] += v1;
                info2[i][j] += v2;
                info3[i][j] += v3;
                info4[i][j] += v4;
            }
        }
    }
    
    /**
     * 计算前缀和(1,1)~(x,y)
     * @param x 行坐标（从1开始）
     * @param y 列坐标（从1开始）
     * @return 前缀和结果
     */
    private long sum(int x, int y) {
        long ans = 0;
        for (int i = x; i > 0; i -= lowbit(i)) {
            for (int j = y; j > 0; j -= lowbit(j)) {
                // 应用数学公式
                ans += (x + 1) * (y + 1) * info1[i][j] - (y + 1) * info2[i][j] - (x + 1) * info3[i][j] + info4[i][j];
            }
        }
        return ans;
    }
    
    /**
     * 对矩形区域(a,b)~(c,d)的所有元素加v
     * @param a 左上区域行坐标（从1开始）
     * @param b 左上区域列坐标（从1开始）
     * @param c 右下区域行坐标（从1开始）
     * @param d 右下区域列坐标（从1开始）
     * @param v 要增加的值
     */
    public void rangeAdd(int a, int b, int c, int d, long v) {
        add(a, b, v);
        add(a, d + 1, -v);
        add(c + 1, b, -v);
        add(c + 1, d + 1, v);
    }
    
    /**
     * 查询矩形区域(a,b)~(c,d)的和
     * @param a 左上区域行坐标（从1开始）
     * @param b 左上区域列坐标（从1开始）
     * @param c 右下区域行坐标（从1开始）
     * @param d 右下区域列坐标（从1开始）
     * @return 区域和
     */
    public long rangeQuery(int a, int b, int c, int d) {
        return sum(c, d) - sum(a - 1, d) - sum(c, b - 1) + sum(a - 1, b - 1);
    }
    
    /**
     * 设置矩阵大小
     * @param n 新的行数
     * @param m 新的列数
     */
    public void setSize(int n, int m) {
        this.n = n;
        this.m = m;
        // 重置树状数组
        info1 = new long[n + 2][m + 2];
        info2 = new long[n + 2][m + 2];
        info3 = new long[n + 2][m + 2];
        info4 = new long[n + 2][m + 2];
    }
    
    /**
     * 主方法，用于处理输入输出
     */
    public static void main(String[] args) {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        String op = sc.next();
        int n = sc.nextInt();
        int m = sc.nextInt();
        
        TwoDimensionFenwickTree ft = new TwoDimensionFenwickTree(n, m);
        
        while (sc.hasNext()) {
            op = sc.next();
            if (op.equals("X")) {
                n = sc.nextInt();
                m = sc.nextInt();
                ft.setSize(n, m);
            } else if (op.equals("L")) {
                int a = sc.nextInt();
                int b = sc.nextInt();
                int c = sc.nextInt();
                int d = sc.nextInt();
                long v = sc.nextLong();
                ft.rangeAdd(a, b, c, d, v);
            } else {
                int a = sc.nextInt();
                int b = sc.nextInt();
                int c = sc.nextInt();
                int d = sc.nextInt();
                System.out.println(ft.rangeQuery(a, b, c, d));
            }
        }
        sc.close();
    }
}

/**
 * 以下是Python实现的二维树状数组区间更新区间查询代码
 * 
 * class TwoDimensionFenwickTree:
 *     def __init__(self, n, m):
 *         self.n = n
 *         self.m = m
 *         # 初始化四个树状数组，索引从1开始
 *         self.info1 = [[0] * (m + 2) for _ in range(n + 2)]  # 维护d[i][j]
 *         self.info2 = [[0] * (m + 2) for _ in range(n + 2)]  # 维护d[i][j] * i
 *         self.info3 = [[0] * (m + 2) for _ in range(n + 2)]  # 维护d[i][j] * j
 *         self.info4 = [[0] * (m + 2) for _ in range(n + 2)]  # 维护d[i][j] * i * j
 *     
 *     def _lowbit(self, x):
 *         # lowbit函数
 *         return x & -x
 *     
 *     def _add(self, x, y, v):
 *         # 在点(x,y)处增加v，同时更新四个树状数组
 *         v1 = v
 *         v2 = v * x
 *         v3 = v * y
 *         v4 = v * x * y
 *         
 *         i = x
 *         while i <= self.n:
 *             j = y
 *             while j <= self.m:
 *                 self.info1[i][j] += v1
 *                 self.info2[i][j] += v2
 *                 self.info3[i][j] += v3
 *                 self.info4[i][j] += v4
 *                 j += self._lowbit(j)
 *             i += self._lowbit(i)
 *     
 *     def _sum(self, x, y):
 *         # 计算前缀和(1,1)~(x,y)
 *         ans = 0
 *         i = x
 *         while i > 0:
 *             j = y
 *             while j > 0:
 *                 # 应用数学公式
 *                 ans += (x + 1) * (y + 1) * self.info1[i][j] \
 *                        - (y + 1) * self.info2[i][j] \
 *                        - (x + 1) * self.info3[i][j] \
 *                        + self.info4[i][j]
 *                 j -= self._lowbit(j)
 *             i -= self._lowbit(i)
 *         return ans
 *     
 *     def range_add(self, a, b, c, d, v):
 *         # 对矩形区域(a,b)~(c,d)的所有元素加v
 *         self._add(a, b, v)
 *         self._add(a, d + 1, -v)
 *         self._add(c + 1, b, -v)
 *         self._add(c + 1, d + 1, v)
 *     
 *     def range_query(self, a, b, c, d):
 *         # 查询矩形区域(a,b)~(c,d)的和
 *         return (self._sum(c, d) - 
 *                 self._sum(a - 1, d) - 
 *                 self._sum(c, b - 1) + 
 *                 self._sum(a - 1, b - 1))
 *     
 *     def set_size(self, n, m):
 *         # 设置矩阵大小
 *         self.n = n
 *         self.m = m
 *         # 重置树状数组
 *         self.info1 = [[0] * (m + 2) for _ in range(n + 2)]
 *         self.info2 = [[0] * (m + 2) for _ in range(n + 2)]
 *         self.info3 = [[0] * (m + 2) for _ in range(n + 2)]
 *         self.info4 = [[0] * (m + 2) for _ in range(n + 2)]
 * 
 * # 主函数
 * def main():
 *     import sys
 *     input = sys.stdin.read().split()
 *     ptr = 0
 *     
 *     op = input[ptr]
 *     ptr += 1
 *     n = int(input[ptr])
 *     ptr += 1
 *     m = int(input[ptr])
 *     ptr += 1
 *     
 *     ft = TwoDimensionFenwickTree(n, m)
 *     
 *     while ptr < len(input):
 *         op = input[ptr]
 *         ptr += 1
 *         
 *         if op == 'X':
 *             n = int(input[ptr])
 *             ptr += 1
 *             m = int(input[ptr])
 *             ptr += 1
 *             ft.set_size(n, m)
 *         elif op == 'L':
 *             a = int(input[ptr])
 *             ptr += 1
 *             b = int(input[ptr])
 *             ptr += 1
 *             c = int(input[ptr])
 *             ptr += 1
 *             d = int(input[ptr])
 *             ptr += 1
 *             v = int(input[ptr])
 *             ptr += 1
 *             ft.range_add(a, b, c, d, v)
 *         else:
 *             a = int(input[ptr])
 *             ptr += 1
 *             b = int(input[ptr])
 *             ptr += 1
 *             c = int(input[ptr])
 *             ptr += 1
 *             d = int(input[ptr])
 *             ptr += 1
 *             print(ft.range_query(a, b, c, d))
 * 
 * if __name__ == '__main__':
 *     main()
 */

/**
 * 二维树状数组区间更新区间查询的高级分析与工程实践：
 * 
 * 1. 三维及以上扩展：
 *    - 对于三维情况，需要8个树状数组维护不同的乘积项
 *    - 每增加一个维度，需要维护的树状数组数量翻倍
 *    - 高维情况下时间复杂度为O(log^k n)，其中k为维度
 *    - 实际应用中二维已经是较为常用的情况，三维及以上使用较少
 * 
 * 2. 数据类型溢出问题：
 *    - 对于大数据量，必须使用long类型或long long类型
 *    - 二维情况下数值会累积更快，尤其需要注意溢出
 *    - 在Java中使用long，C++中使用long long，Python自动处理大数
 * 
 * 3. 性能优化技巧：
 *    - 缓存热点区域的查询结果
 *    - 使用快速IO方法处理大规模输入
 *    - 预分配足够空间，避免动态扩容
 *    - 对于稀疏矩阵，考虑使用哈希表实现
 * 
 * 4. 内存优化策略：
 *    - 对于大规模数据，可以考虑使用离散化技术
 *    - 对于特定问题，可以根据实际数据范围优化数组大小
 *    - 避免在栈上分配大型数组，可能导致栈溢出
 * 
 * 5. 实际应用场景：
 *    - 图像处理中的区域滤镜和效果处理
 *    - 二维统计数据的动态更新和查询
 *    - 游戏开发中的区域影响计算
 *    - 地理信息系统中的空间分析
 * 
 * 6. 与线段树的对比：
 *    - 二维树状数组实现更简单，常数更小
 *    - 线段树可以支持更复杂的区间操作（如区间最大值）
 *    - 树状数组在实际应用中通常更快，但功能相对受限
 * 
 * 7. 调试技巧：
 *    - 使用小例子测试各个操作的正确性
 *    - 打印中间状态以验证树状数组的更新是否正确
 *    - 对比一维情况，逐步扩展到二维
 *    - 确保数学公式的正确性，这是最容易出错的地方
 */

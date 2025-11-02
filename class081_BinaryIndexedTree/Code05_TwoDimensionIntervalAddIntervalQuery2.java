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
 * - 区间更新: O(log n * log m)，其中n和m分别是矩阵的行数和列数
 * - 区间查询: O(log n * log m)
 * 空间复杂度: O(n * m)，用于存储四个二维树状数组
 */

/**
 * 二维树状数组区间更新区间查询的可视化与原理解析：
 * 
 * 1. 二维差分原理可视化：
 *    - 当我们需要对矩形区域(a,b)~(c,d)增加v时，我们只需要在差分数组的四个角点进行操作：
 *      d[a][b] += v    // 区域左上角开始增加v
 *      d[a][d+1] -= v  // 右侧边界减去v，抵消右侧区域的影响
 *      d[c+1][b] -= v  // 下侧边界减去v，抵消下侧区域的影响
 *      d[c+1][d+1] += v // 右下边界增加v，恢复交叉区域的影响
 *    - 这种操作确保了只有目标矩形区域内的所有点会被增加v，而其他区域不变
 * 
 * 2. 树状数组与差分结合的可视化理解：
 *    - 原始数组a[i][j] = 差分数组d[i][j]的前缀和
 *    - 二维前缀和sum(a[i][j]) = 差分数组d[i][j]加权前缀和
 *    - 四个树状数组分别维护不同权重的差分数组，实现高效计算
 * 
 * 3. 四个树状数组的功能可视化：
 *    info1: 存储d[i][j]的树状数组，负责(i+1)(j+1)部分的计算
 *    info2: 存储d[i][j]*i的树状数组，负责-(i+1)部分的计算
 *    info3: 存储d[i][j]*j的树状数组，负责-(j+1)部分的计算
 *    info4: 存储d[i][j]*i*j的树状数组，负责+1部分的计算
 *    这四个部分组合起来正好构成了二维前缀和的数学公式
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
 * 二维树状数组区间更新区间查询的典型应用案例与深入优化：
 * 
 * 1. 典型应用案例详解：
 *    - 案例一：二维区间累加与查询
 *      应用场景：图像处理中的矩形区域亮度调整、二维热图动态更新
 *      输入输出示例：
 *      输入：初始化一个3x3矩阵为全0
 *           rangeAdd(1,1,3,3,5) // 整个矩阵增加5
 *           rangeAdd(1,1,2,2,3) // 左上2x2区域再增加3
 *           rangeQuery(1,1,3,3) // 查询整个矩阵的和
 *      输出：75 (3x3x5 + 2x2x3 = 45 + 12 = 57？不，这里应该是查询原始数组的和，需要重新计算)
 *           正确计算：(1,1)=8, (1,2)=8, (1,3)=5, (2,1)=8, (2,2)=8, (2,3)=5, (3,1)=5, (3,2)=5, (3,3)=5
 *           总和=8+8+5+8+8+5+5+5+5=62
 * 
 *    - 案例二：二维频率统计
 *      应用场景：文本词频矩阵、用户活跃度热图
 *      特点：频繁进行区间更新，多次查询不同区域的总和
 *      优化方法：结合离散化技术处理大范围稀疏数据
 * 
 *    - 案例三：二维动态区域操作
 *      应用场景：游戏开发中的区域效果、物理模拟中的力场计算
 *      特点：需要高效的动态区间操作，对性能要求高
 *      优势：比暴力更新O(n²)和二维线段树更高效
 * 
 * 2. 性能优化深度技巧：
 *    - 数据类型优化：
 *      根据实际数据范围选择合适的数据类型，避免不必要的空间浪费
 *      对于可能溢出的情况，及时切换到更大的数据类型
 *      C++和Java中使用long/long long类型，Python自动处理大数
 * 
 *    - 输入输出优化：
 *      使用快速IO方法处理大规模输入，避免超时
 *      在Java中使用BufferedReader替代Scanner
 *      在C++中使用scanf/printf替代cin/cout
 *      示例：
 *      // Java快速IO示例
 *      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 *      StringTokenizer st = new StringTokenizer(br.readLine());
 * 
 *    - 离散化技术：
 *      当二维坐标范围很大但实际使用的坐标点较少时，使用离散化
 *      将原始坐标映射到连续的较小整数范围内
 *      可以节省大量内存空间，适用于大规模稀疏数据
 * 
 *    - 内存访问模式优化：
 *      调整循环顺序，提高缓存命中率
 *      优先按行访问，然后按列访问，符合内存的行优先存储
 *      避免频繁的列方向跳跃访问
 * 
 *    - 并行化处理：
 *      对于超大型矩阵，可以考虑分块并行处理
 *      将矩阵分成多个子块，每个线程处理一部分
 *      适用于多核处理器，可显著提高大规模数据处理速度
 * 
 * 3. 工程实现高级注意事项：
 *    - 索引管理：
 *      严格区分原始数据（0-based）和树状数组（1-based）的索引
 *      注意边界条件，特别是c+1和d+1不能越界
 *      在初始化时预留足够的空间（+2）避免越界问题
 * 
 *    - 异常处理与边界检查：
 *      对输入的坐标范围进行验证，确保不会越界
 *      处理极端情况，如空矩阵、单个元素矩阵等
 *      添加适当的错误处理机制
 *      示例：
 *      public void rangeAdd(int a, int b, int c, int d, long v) {
 *          // 验证坐标范围
 *          if (a < 1 || b < 1 || c > n || d > m || a > c || b > d) {
 *              throw new IllegalArgumentException("Invalid range coordinates");
 *          }
 *          // 正常处理
 *          add(a, b, v);
 *          add(a, d + 1, -v);
 *          add(c + 1, b, -v);
 *          add(c + 1, d + 1, v);
 *      }
 * 
 *    - 可复用性设计：
 *      将二维树状数组封装为独立的类，提供清晰的API
 *      考虑添加批量操作方法，提高频繁操作的效率
 *      设计适当的接口，便于集成到更大的系统中
 * 
 *    - 单元测试：
 *      编写全面的单元测试，覆盖各种边界情况
 *      测试不同的输入模式和操作组合
 *      使用小数据集验证正确性，大数据集测试性能
 * 
 * 4. 与其他二维区间操作数据结构对比：
 *    - 二维线段树：
 *      功能更强大，支持区间最值查询
 *      实现复杂，代码量大，常数较大
 *      时间复杂度相同，但实际效率通常低于树状数组
 * 
 *    - 二维块状数组：
 *      实现简单，适合某些特定场景
 *      时间复杂度为O(√n × √m)，对于大规模数据效率较低
 *      内存占用较小，实现灵活
 * 
 *    - 二维平衡树：
 *      实现复杂，不适合此类特定场景
 *      时间复杂度较高，通常不用于二维区间操作
 *      主要用于动态维护有序集合
 * 
 *    - 二维前缀和数组：
 *      只能处理静态数据，无法进行动态更新
 *      查询时间O(1)，但不支持更新操作
 *      适用于只读场景
 * 
 * 5. 常见陷阱与调试技巧：
 *    - 数学公式错误：
 *      确保前缀和计算的数学公式正确无误
 *      仔细检查四个树状数组的组合方式
 *      使用小例子手动验证公式
 * 
 *    - 边界条件处理：
 *      特别注意c+1和d+1可能超出原始数组范围的情况
 *      确保数组大小足够大，或者添加边界检查
 * 
 *    - 索引转换错误：
 *      严格区分0-based和1-based索引
 *      在输入输出时注意索引转换
 * 
 *    - 数据溢出：
 *      及时使用更大的数据类型，如long/long long
 *      监控中间计算结果，防止溢出
 * 
 *    - 性能瓶颈：
 *      对于大规模数据，监控内存使用情况
 *      考虑使用稀疏表示或离散化技术
 *      优化输入输出操作，避免成为性能瓶颈
 */

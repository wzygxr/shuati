/*
 * POJ 2155 Matrix - 二维树状数组区间取反单点查询问题
 * 题目链接: http://poj.org/problem?id=2155
 * 
 * 题目描述:
 * 给定一个 N×N 的矩阵，初始时所有元素都为 0。
 * 有两种操作：
 * 1. "C x1 y1 x2 y2"：将左上角为 (x1,y1)、右下角为 (x2,y2) 的子矩阵中的每个元素取反（0变1，1变0）
 * 2. "Q x y"：查询位置 (x,y) 的值
 * 
 * 解题思路深度分析:
 * 
 * 这个问题是二维树状数组的经典应用 - 区间更新（取反）、单点查询的场景。
 * 
 * 1. 取反操作的数学表示：
 *    由于每次取反就是将元素值增加1然后模2（0+1=1, 1+1=2≡0 mod2），
 *    所以我们可以将取反操作转化为对区间内每个元素加1，最后查询时模2。
 * 
 * 2. 二维差分数组的应用：
 *    要实现区间加1、单点查询，可以使用二维差分数组：
 *    - 对于矩形区域(x1,y1)到(x2,y2)的加1操作，只需要在差分数组的四个角进行更新：
 *      d[x1][y1] += 1
 *      d[x1][y2+1] -= 1
 *      d[x2+1][y1] -= 1
 *      d[x2+1][y2+1] += 1
 *    - 这四个点的更新可以保证，当计算原数组某点的值（差分数组前缀和）时，
 *      只有在(x1,y1)到(x2,y2)矩形内的点才会被加1
 * 
 * 3. 树状数组优化：
 *    二维树状数组用来高效维护二维差分数组，支持：
 *    - 单点更新操作 O(log n * log n)
 *    - 二维前缀和查询 O(log n * log n)
 * 
 * 4. 单点查询的实现：
 *    查询位置(x,y)的值实际上是查询差分数组从(1,1)到(x,y)的二维前缀和对2取模的结果
 *    这是因为每次更新的影响会通过差分数组传播到所有受影响的位置
 * 
 * 时间复杂度：
 * - 区间更新: O(log n * log n) - 四次单点更新操作
 * - 单点查询: O(log n * log n) - 一次二维前缀和查询
 * 空间复杂度: O(n * n)
 * 
 * 与线段树对比：
 * - 树状数组实现更简洁，常数更小
 * - 线段树虽然功能更强大，但在此问题中树状数组已足够且效率更高
 */

class POJ2155_Matrix {
private:
    int tree[1001][1001];  // 二维树状数组
    int n;
    
    /**
     * lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
     * 例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
     * 
     * @param i 输入数字
     * @return 最低位的1所代表的数值
     */
    int lowbit(int i) {
        return i & -i;
    }
    
public:
    /**
     * 二维树状数组初始化
     * 
     * @param n 矩阵大小
     */
    POJ2155_Matrix(int n) {
        this->n = n;
        // 初始化二维树状数组
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                tree[i][j] = 0;
            }
        }
    }
    
    /**
     * 二维树状数组单点增加操作
     * 
     * @param x x坐标（从1开始）
     * @param y y坐标（从1开始）
     * @param v 增加的值
     */
    void add(int x, int y, int v) {
        for (int i = x; i <= n; i += lowbit(i)) {
            for (int j = y; j <= n; j += lowbit(j)) {
                tree[i][j] += v;
            }
        }
    }
    
    /**
     * 二维树状数组前缀和查询：计算从(1,1)到(x,y)的矩形区域内所有元素的和
     * 
     * @param x x坐标（从1开始）
     * @param y y坐标（从1开始）
     * @return 前缀和
     */
    int sum(int x, int y) {
        int ans = 0;
        for (int i = x; i > 0; i -= lowbit(i)) {
            for (int j = y; j > 0; j -= lowbit(j)) {
                ans += tree[i][j];
            }
        }
        return ans;
    }
    
    /**
     * 区间更新操作：将左上角为(x1,y1)、右下角为(x2,y2)的子矩阵中的每个元素取反
     * 
     * @param x1 左上角x坐标
     * @param y1 左上角y坐标
     * @param x2 右下角x坐标
     * @param y2 右下角y坐标
     */
    void update(int x1, int y1, int x2, int y2) {
        add(x1, y1, 1);
        add(x1, y2 + 1, -1);
        add(x2 + 1, y1, -1);
        add(x2 + 1, y2 + 1, 1);
    }
    
    /**
     * 单点查询操作：查询位置(x,y)的值
     * 
     * @param x x坐标
     * @param y y坐标
     * @return 位置(x,y)的值
     */
    int query(int x, int y) {
        return sum(x, y) % 2;
    }
};

/*
 * 完整主函数实现
 * 处理POJ 2155的输入输出格式
 */
#include <iostream>
using namespace std;

int main() {
    int T;  // 测试用例数
    cin >> T;
    while (T--) {
        int N, Q;  // N:矩阵大小, Q:操作数
        cin >> N >> Q;
        
        POJ2155_Matrix matrix(N);
        
        while (Q--) {
            char op;  // 操作类型
            cin >> op;
            
            if (op == 'C') {  // 区间更新操作
                int x1, y1, x2, y2;
                cin >> x1 >> y1 >> x2 >> y2;
                matrix.update(x1, y1, x2, y2);
            } else if (op == 'Q') {  // 单点查询操作
                int x, y;
                cin >> x >> y;
                cout << matrix.query(x, y) << endl;
            }
        }
        
        if (T > 0) cout << endl;  // 不同测试用例之间输出一个空行
    }
    return 0;
}

/*
 * 以下是Java实现的POJ2155代码
 * 
 * import java.io.*;
 * import java.util.*;
 * 
 * public class Main {
 *     static class FenwickTree2D {
 *         private int[][] tree;
 *         private int n;
 *         
 *         public FenwickTree2D(int n) {
 *             this.n = n;
 *             tree = new int[n + 2][n + 2];  // 索引从1开始，预留额外空间避免越界
 *         }
 *         
 *         private int lowbit(int x) {
 *             return x & -x;
 *         }
 *         
 *         public void add(int x, int y, int val) {
 *             for (int i = x; i <= n; i += lowbit(i)) {
 *                 for (int j = y; j <= n; j += lowbit(j)) {
 *                     tree[i][j] += val;
 *                 }
 *             }
 *         }
 *         
 *         public int sum(int x, int y) {
 *             int ans = 0;
 *             for (int i = x; i > 0; i -= lowbit(i)) {
 *                 for (int j = y; j > 0; j -= lowbit(j)) {
 *                     ans += tree[i][j];
 *                 }
 *             }
 *             return ans;
 *         }
 *         
 *         public void update(int x1, int y1, int x2, int y2) {
 *             add(x1, y1, 1);
 *             add(x1, y2 + 1, -1);
 *             add(x2 + 1, y1, -1);
 *             add(x2 + 1, y2 + 1, 1);
 *         }
 *         
 *         public int query(int x, int y) {
 *             return sum(x, y) % 2;
 *         }
 *     }
 *     
 *     public static void main(String[] args) throws IOException {
 *         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 *         PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
 *         int T = Integer.parseInt(br.readLine());
 *         
 *         while (T-- > 0) {
 *             StringTokenizer st = new StringTokenizer(br.readLine());
 *             int N = Integer.parseInt(st.nextToken());
 *             int Q = Integer.parseInt(st.nextToken());
 *             
 *             FenwickTree2D ft = new FenwickTree2D(N);
 *             
 *             while (Q-- > 0) {
 *                 st = new StringTokenizer(br.readLine());
 *                 char op = st.nextToken().charAt(0);
 *                 
 *                 if (op == 'C') {
 *                     int x1 = Integer.parseInt(st.nextToken());
 *                     int y1 = Integer.parseInt(st.nextToken());
 *                     int x2 = Integer.parseInt(st.nextToken());
 *                     int y2 = Integer.parseInt(st.nextToken());
 *                     ft.update(x1, y1, x2, y2);
 *                 } else {
 *                     int x = Integer.parseInt(st.nextToken());
 *                     int y = Integer.parseInt(st.nextToken());
 *                     pw.println(ft.query(x, y));
 *                 }
 *             }
 *             
 *             if (T > 0) {
 *                 pw.println();
 *             }
 *         }
 *         
 *         pw.flush();
 *         br.close();
 *         pw.close();
 *     }
 * }
 */

/*
 * 以下是Python实现的POJ2155代码
 * 
 * import sys
 * 
 * class FenwickTree2D:
 *     def __init__(self, n):
 *         self.n = n
 *         # 索引从1开始，预留额外空间
 *         self.tree = [[0] * (n + 2) for _ in range(n + 2)]
 *     
 *     def _lowbit(self, x):
 *         return x & -x
 *     
 *     def add(self, x, y, val):
 *         i = x
 *         while i <= self.n:
 *             j = y
 *             while j <= self.n:
 *                 self.tree[i][j] += val
 *                 j += self._lowbit(j)
 *             i += self._lowbit(i)
 *     
 *     def sum(self, x, y):
 *         ans = 0
 *         i = x
 *         while i > 0:
 *             j = y
 *             while j > 0:
 *                 ans += self.tree[i][j]
 *                 j -= self._lowbit(j)
 *             i -= self._lowbit(i)
 *         return ans
 *     
 *     def update(self, x1, y1, x2, y2):
 *         self.add(x1, y1, 1)
 *         self.add(x1, y2 + 1, -1)
 *         self.add(x2 + 1, y1, -1)
 *         self.add(x2 + 1, y2 + 1, 1)
 *     
 *     def query(self, x, y):
 *         return self.sum(x, y) % 2
 * 
 * def main():
 *     input = sys.stdin.read().split()
 *     ptr = 0
 *     T = int(input[ptr])
 *     ptr += 1
 *     
 *     for _ in range(T):
 *         N = int(input[ptr])
 *         ptr += 1
 *         Q = int(input[ptr])
 *         ptr += 1
 *         
 *         ft = FenwickTree2D(N)
 *         
 *         for __ in range(Q):
 *             op = input[ptr]
 *             ptr += 1
 *             
 *             if op == 'C':
 *                 x1 = int(input[ptr])
 *                 ptr += 1
 *                 y1 = int(input[ptr])
 *                 ptr += 1
 *                 x2 = int(input[ptr])
 *                 ptr += 1
 *                 y2 = int(input[ptr])
 *                 ptr += 1
 *                 ft.update(x1, y1, x2, y2)
 *             else:
 *                 x = int(input[ptr])
 *                 ptr += 1
 *                 y = int(input[ptr])
 *                 ptr += 1
 *                 print(ft.query(x, y))
 *         
 *         if _ < T - 1:
 *             print()
 * 
 * if __name__ == '__main__':
 *     main()
 */

/*
 * 二维树状数组在区间更新单点查询场景中的应用与扩展：
 * 
 * 1. 类似问题扩展：
 *    - POJ 3468 A Simple Problem with Integers（一维情况）
 *    - 二维区间异或、区间加法等操作都可以用类似方法处理
 *    - 图像处理中的区域操作，如图像反转、亮度调整等
 * 
 * 2. 二进制性质的应用：
 *    - 本题中利用模2运算来实现取反效果
 *    - 类似地，可以利用不同模数实现其他周期性操作
 *    - 二进制位操作在树状数组中起到核心作用
 * 
 * 3. 实现细节与优化：
 *    - 数组大小：通常需要比最大索引大1或2，避免边界检查
 *    - 输入输出优化：对于大规模数据，使用快速IO方法
 *    - 内存优化：对于稀疏矩阵，可以考虑使用哈希表存储
 *    - 性能考虑：在C++中使用数组比vector更快，在Java中可以用BufferedReader加速
 * 
 * 4. 教学价值：
 *    - 是理解二维差分数组与树状数组结合的绝佳示例
 *    - 展示了如何将复杂的区间操作转换为简单的点操作
 *    - 体现了数学抽象在算法设计中的重要性
 * 
 * 5. 实际应用场景：
 *    - 二维网格统计（如地图标记、热力图更新）
 *    - 游戏开发中的区域效果（如范围伤害、增益效果）
 *    - 数据库中的二维范围更新操作
 *    - 计算机图形学中的区域着色和变换
 */
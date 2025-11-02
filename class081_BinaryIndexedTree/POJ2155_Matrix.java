package class108;

import java.io.*;
import java.util.*;

/**
 * POJ 2155 Matrix
 * 题目链接: http://poj.org/problem?id=2155
 * 
 * 题目描述:
 * 给定一个 N×N 的矩阵，初始时所有元素都为 0。
 * 有两种操作：
 * 1. "C x1 y1 x2 y2"：将左上角为 (x1,y1)、右下角为 (x2,y2) 的子矩阵中的每个元素取反（0变1，1变0）
 * 2. "Q x y"：查询位置 (x,y) 的值
 * 
 * 解题思路:
 * 使用二维树状数组 + 差分思想来解决这个问题。
 * 对于区间更新、单点查询的问题，可以使用二维差分数组配合二维树状数组：
 * 1. 对于更新操作，我们只需要在差分数组的四个角上进行更新：
 *    - 在 (x1, y1) 处 +1
 *    - 在 (x1, y2+1) 处 -1
 *    - 在 (x2+1, y1) 处 -1
 *    - 在 (x2+1, y2+1) 处 +1
 * 2. 对于查询操作，查询 (x,y) 点的值就是差分数组 (1,1) 到 (x,y) 的二维前缀和对 2 取模
 * 
 * 时间复杂度：
 * - 区间更新: O(log n * log n)
 * - 单点查询: O(log n * log n)
 * 空间复杂度: O(n * n)
 */

public class POJ2155_Matrix {
    // 二维树状数组
    private int[][] tree;
    private int n;
    
    /**
     * lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
     * 例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
     * 
     * @param i 输入数字
     * @return 最低位的1所代表的数值
     */
    private int lowbit(int i) {
        return i & -i;
    }
    
    /**
     * 二维树状数组初始化
     * 
     * @param n 矩阵大小
     */
    public POJ2155_Matrix(int n) {
        this.n = n;
        this.tree = new int[n + 1][n + 1];
    }
    
    /**
     * 二维树状数组单点增加操作
     * 
     * @param x x坐标（从1开始）
     * @param y y坐标（从1开始）
     * @param v 增加的值
     */
    private void add(int x, int y, int v) {
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
    private int sum(int x, int y) {
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
    public void update(int x1, int y1, int x2, int y2) {
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
    public int query(int x, int y) {
        return sum(x, y) % 2;
    }
    
    /**
     * 主函数：处理输入输出和调用相关操作
     * 注意：POJ的输入输出格式比较特殊，需要严格按照题目要求
     */
    public static void main(String[] args) throws IOException {
        // 使用高效的IO处理方式
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取测试用例数量
        in.nextToken();
        int testCases = (int) in.nval;
        
        for (int t = 0; t < testCases; t++) {
            if (t > 0) out.println(); // 每个测试用例之间输出一个空行
            
            // 读取矩阵大小和操作数量
            in.nextToken();
            int n = (int) in.nval;
            in.nextToken();
            int operations = (int) in.nval;
            
            // 初始化二维树状数组
            POJ2155_Matrix matrix = new POJ2155_Matrix(n);
            
            // 处理操作
            for (int i = 0; i < operations; i++) {
                in.nextToken();
                String op = in.sval;
                
                if (op.equals("C")) {
                    // 区间更新操作
                    in.nextToken(); int x1 = (int) in.nval;
                    in.nextToken(); int y1 = (int) in.nval;
                    in.nextToken(); int x2 = (int) in.nval;
                    in.nextToken(); int y2 = (int) in.nval;
                    matrix.update(x1, y1, x2, y2);
                } else {
                    // 单点查询操作
                    in.nextToken(); int x = (int) in.nval;
                    in.nextToken(); int y = (int) in.nval;
                    out.println(matrix.query(x, y));
                }
            }
        }
        
        // 刷新输出缓冲区并关闭IO流
        out.flush();
        out.close();
        br.close();
    }
}
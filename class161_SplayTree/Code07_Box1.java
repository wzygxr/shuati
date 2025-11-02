package class153;

/**
 * Box - Splay树实现盒子包含关系问题，Java版本
 * 
 * 【题目来源】HDU 2475
 * 【题目链接】http://acm.hdu.edu.cn/showproblem.php?pid=2475
 * 【题目大意】
 * 有n个盒子，每个盒子可能包含在另一个盒子中，支持以下操作：
 * 1. MOVE x y: 将盒子x移动到盒子y中（y为0表示移到最外层）
 * 2. QUERY x: 查询盒子x在哪一个盒子中（0表示在最外层）
 * 
 * 【数据范围】
 * 1 <= N <= 50000
 * 1 <= M <= 100000
 * 
 * 【算法分析】
 * 使用Splay树维护森林结构，每个Splay树表示一个包含关系树
 * 通过Splay操作优化频繁访问节点的访问速度
 * 
 * 【时间复杂度】
 * - 所有操作均摊时间复杂度为O(log n)
 * - 单次操作最坏情况可能达到O(n)
 * 
 * 【空间复杂度】O(n)
 * 
 * 【实现特点】
 * - 使用数组模拟节点结构，避免对象创建开销
 * - 维护包含关系的parent数组
 * - 使用辅助栈优化Splay操作
 */

import java.io.*;
import java.util.*;

/**
 * Splay树实现Box问题
 * 支持盒子的移动和查询操作
 * 
 * 【核心思想】
 * 1. 使用Splay树维护森林结构，每个Splay树表示一个包含关系树
 * 2. 通过parent数组维护盒子间的包含关系
 * 3. 利用Splay操作将访问的节点移动到根附近优化后续访问
 * 
 * 【应用场景】
 * - 动态维护树形结构的包含关系
 * - 需要频繁查询节点父节点的问题
 * - 算法竞赛中的数据结构问题
 */

public class Code07_Box1 {
    public static int MAXN = 50010;
    
    // Splay树节点相关数组
    public static int[] father = new int[MAXN];   // 父节点
    public static int[] left = new int[MAXN];     // 左子节点
    public static int[] right = new int[MAXN];    // 右子节点
    public static int[] parent = new int[MAXN];   // 包含关系中的父盒子
    
    public static int[] stack = new int[MAXN];    // 辅助栈
    public static int top = 0;
    
    /**
     * 【方向判断】确定节点i是其父节点的左子节点还是右子节点
     * 时间复杂度: O(1)
     * 
     * @param i 需要判断的节点索引
     * @return 1表示右子节点，0表示左子节点
     */
    // 判断节点i是其父节点的左儿子还是右儿子
    public static int lr(int i) {
        return right[father[i]] == i ? 1 : 0;
    }
    
    /**
     * 【核心旋转操作】将节点i旋转至其父节点的位置
     * 这是Splay树维护平衡的基本操作
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * @param i 需要旋转的节点索引
     */
    // 旋转操作
    public static void rotate(int i) {
        int f = father[i], g = father[f], soni = lr(i), sonf = lr(f);
        
        // 【旋转逻辑】根据当前节点是左子还是右子执行不同的旋转操作
        if (soni == 1) {       // 右子节点，执行右旋
            right[f] = left[i];
            if (right[f] != 0) father[right[f]] = f;
            left[i] = f;
        } else {               // 左子节点，执行左旋
            left[f] = right[i];
            if (left[f] != 0) father[left[f]] = f;
            right[i] = f;
        }
        
        // 更新祖父节点的子节点指针
        if (g != 0) {
            if (sonf == 1) right[g] = i;
            else left[g] = i;
        }
        
        // 更新父指针
        father[f] = i;
        father[i] = g;
    }
    
    /**
     * 【核心伸展操作】将节点i旋转到根节点
     * 这是Splay树的核心操作，通过一系列旋转使被访问节点移动到树的顶部
     * 时间复杂度: 均摊O(log n)，最坏情况O(n)
     * 空间复杂度: O(1)
     * 
     * @param i 需要旋转的节点索引
     */
    // Splay操作，将节点i旋转到根
    public static void splay(int i) {
        // 使用辅助栈收集路径上的所有节点
        top = 0;
        stack[++top] = i;
        int j = i;
        while (father[j] != 0) {
            stack[++top] = father[j];
            j = father[j];
        }
        
        // 从根到目标节点依次下传懒标记
        while (top > 0) {
            down(stack[top--]);
        }
        
        // 执行Splay操作
        int f = father[i], g = father[f];
        while (f != 0) {
            // 【旋转策略】根据Zig-Zig和Zig-Zag情况选择不同的旋转顺序
            if (g != 0) {
                if (lr(i) == lr(f)) rotate(f);  // Zig-Zig情况
                else rotate(i);                // Zig-Zag情况
            }
            rotate(i);
            f = father[i];
            g = father[f];
        }
    }
    
    /**
     * 【懒标记下传】将懒标记传播到子节点
     * 在这个题目中，down操作为空
     * 
     * @param i 需要下传懒标记的节点
     */
    // 下传操作
    public static void down(int i) {
        // 在这个题目中，down操作为空
    }
    
    /**
     * 【查找根节点】查找节点i所在树的根节点
     * 时间复杂度: 均摊O(log n)
     * 
     * @param i 要查找根节点的节点索引
     * @return 节点i所在树的根节点索引
     */
    // 查找节点i的根节点
    public static int findRoot(int i) {
        // 将节点i旋转到根
        splay(i);
        
        // 找到最左边的节点（即根节点）
        int cur = i;
        while (left[cur] != 0) {
            cur = left[cur];
        }
        
        // 将根节点旋转到根（优化后续访问）
        splay(cur);
        
        return cur;
    }
    
    /**
     * 【移动操作】将盒子x移动到盒子y中
     * 时间复杂度: 均摊O(log n)
     * 
     * @param x 要移动的盒子编号
     * @param y 目标盒子编号（0表示移到最外层）
     */
    // 移动操作：将盒子x移动到盒子y中
    public static void move(int x, int y) {
        // 先将x从原来的包含关系中分离
        // 将盒子x旋转到根
        splay(x);
        
        // 断开x与其左子树的连接
        if (left[x] != 0) {
            father[left[x]] = 0;
        }
        left[x] = 0;
        
        // 如果y不为0，将x连接到y的最右路径
        if (y != 0) {
            // 将盒子y旋转到根
            splay(y);
            
            // 找到y的最右节点
            int cur = y;
            while (right[cur] != 0) {
                cur = right[cur];
            }
            
            // 将最右节点旋转到根
            splay(cur);
            
            // 将x连接为最右节点的右子节点
            right[cur] = x;
            father[x] = cur;
        }
        
        // 更新包含关系
        parent[x] = y;
    }
    
    /**
     * 【查询操作】查询盒子x的直接外层盒子
     * 时间复杂度: O(1)
     * 
     * @param x 要查询的盒子编号
     * @return 盒子x的直接外层盒子编号（0表示在最外层）
     */
    // 查询操作：查询盒子x的直接外层盒子
    public static int query(int x) {
        return parent[x];
    }
    
    /**
     * 【主函数】处理输入输出和操作调用
     * 【输入输出优化】使用BufferedReader和PrintWriter提高读取效率
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 【IO优化】使用BufferedReader和PrintWriter提高读取效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String line;
        boolean first = true;
        
        // 处理多组测试数据
        while ((line = br.readLine()) != null) {
            if (!first) {
                out.println();
            }
            first = false;
            
            // 读取盒子数量
            int n = Integer.parseInt(line.trim());
            
            // 【初始化】清空所有数组
            for (int i = 1; i <= n; i++) {
                father[i] = 0;
                left[i] = 0;
                right[i] = 0;
                parent[i] = 0;
            }
            
            // 读取初始状态并构建包含关系
            String[] parts = br.readLine().split(" ");
            for (int i = 1; i <= n; i++) {
                int p = Integer.parseInt(parts[i-1]);
                if (p != 0) {
                    // 如果盒子i包含在盒子p中，则执行移动操作
                    move(i, p);
                }
            }
            
            // 处理操作
            while (true) {
                line = br.readLine().trim();
                if (line.equals("")) break;
                
                parts = line.split(" ");
                if (parts[0].equals("M")) {
                    // MOVE操作：将盒子x移动到盒子y中
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    move(x, y);
                } else {
                    // QUERY操作：查询盒子x的直接外层盒子
                    int x = Integer.parseInt(parts[1]);
                    out.println(query(x));
                }
            }
        }
        
        // 【工程化考量】确保所有输出都被刷新并关闭资源
        out.flush();
        out.close();
        br.close();
    }
}
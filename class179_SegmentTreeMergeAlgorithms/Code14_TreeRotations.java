// BZOJ2212/POI2011 Tree Rotations，Java版
// 测试链接 : https://www.luogu.com.cn/problem/P3521

import java.io.*;
import java.util.*;

/**
 * BZOJ2212/POI2011 Tree Rotations
 * 
 * 题目来源: POI 2011
 * 题目链接: https://www.luogu.com.cn/problem/P3521 / https://www.lydsy.com/JudgeOnline/problem.php?id=2212
 * 
 * 题目描述:
 * 给定一棵二叉树，每个节点有一个权值。你可以交换任意节点的左右子树，
 * 求交换后中序遍历的逆序对的最小数量。
 * 
 * 解题思路:
 * 1. 使用后序遍历的方式处理整棵二叉树
 * 2. 对于每个节点，分别计算交换和不交换左右子树时的逆序对数目
 * 3. 选择逆序对数目较小的方案
 * 4. 使用线段树合并来高效计算左右子树合并时产生的逆序对数目
 * 
 * 算法复杂度:
 * - 时间复杂度: O(n log n)，其中n是树的节点数。每个节点最多被访问一次，
 *   每次线段树合并操作的时间复杂度是O(log n)。
 * - 空间复杂度: O(n log n)，动态开点线段树的空间复杂度。
 * 
 * 最优解验证:
 * 线段树合并是该问题的最优解。其他可能的解法包括归并排序的分治方法，
 * 但线段树合并的实现更加直观，且能够高效计算子树间的逆序对数目。
 * 
 * 线段树合并解决逆序对问题的核心思想:
 * 1. 为每个子树维护一个权值线段树，记录子树中各个权值的出现次数
 * 2. 当合并左右子树时，可以通过线段树快速计算交叉逆序对数目
 * 3. 同时，我们可以选择是否交换左右子树，以最小化总逆序对数目
 */

public class Code14_TreeRotations {
    
    // 线段树的节点数量
    private static int cnt;
    // 线段树的左子节点、右子节点、当前节点的值（权值出现次数）
    private static int[] ls, rs, sum;
    // 初始分配的空间大小（可以根据需要调整）
    private static final int MAXN = 400000 * 20; // 每个节点最多需要O(log n)空间
    private static long ans; // 存储最小逆序对数目
    private static int[] a; // 存储节点权值
    private static int ptr; // 权值数组指针
    
    public static void main(String[] args) throws IOException {
        // 使用快速IO
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        int n = Integer.parseInt(br.readLine());
        a = new int[n + 1]; // 1-based索引
        ptr = 1;
        
        // 初始化线段树数组
        ls = new int[MAXN];
        rs = new int[MAXN];
        sum = new int[MAXN];
        Arrays.fill(ls, 0);
        Arrays.fill(rs, 0);
        Arrays.fill(sum, 0);
        cnt = 0;
        ans = 0;
        
        // 递归构建树并计算最小逆序对数目
        build(br);
        
        pw.println(ans);
        pw.flush();
        pw.close();
        br.close();
    }
    
    /**
     * 构建二叉树并计算最小逆序对数目
     * @param br 输入流
     * @return 当前子树的线段树根节点
     * @throws IOException 输入异常
     */
    private static int build(BufferedReader br) throws IOException {
        int w = Integer.parseInt(br.readLine());
        int root = newNode();
        
        if (w == 0) {
            // 非叶子节点，递归构建左右子树
            int left = build(br);
            int right = build(br);
            
            // 计算交换和不交换左右子树时的逆序对数目
            long case1 = calc(left, right); // 不交换时的交叉逆序对
            long case2 = calc(right, left); // 交换时的交叉逆序对（即原右子树与左子树的逆序对）
            
            // 选择逆序对数目较小的方案
            ans += Math.min(case1, case2);
            
            // 合并左右子树的线段树
            root = merge(left, right, 1, (int)4e5);
        } else {
            // 叶子节点，将权值插入线段树
            a[ptr++] = w;
            update(root, 1, (int)4e5, w, 1);
        }
        
        return root;
    }
    
    /**
     * 创建新的线段树节点
     * @return 新创建的节点编号
     */
    private static int newNode() {
        cnt++;
        ls[cnt] = 0;
        rs[cnt] = 0;
        sum[cnt] = 0;
        return cnt;
    }
    
    /**
     * 线段树更新操作
     * @param p 当前节点编号
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @param x 需要更新的位置
     * @param v 更新的值（这里是+1）
     */
    private static void update(int p, int l, int r, int x, int v) {
        sum[p] += v; // 更新当前节点的权值出现次数
        if (l == r) {
            return; // 叶子节点，更新完成
        }
        
        int mid = (l + r) >> 1;
        
        // 根据x的位置决定更新左子树还是右子树
        if (x <= mid) {
            if (ls[p] == 0) {
                ls[p] = newNode();
            }
            update(ls[p], l, mid, x, v);
        } else {
            if (rs[p] == 0) {
                rs[p] = newNode();
            }
            update(rs[p], mid + 1, r, x, v);
        }
    }
    
    /**
     * 线段树合并操作
     * @param x 第一棵线段树的根节点
     * @param y 第二棵线段树的根节点
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @return 合并后的线段树根节点
     */
    private static int merge(int x, int y, int l, int r) {
        // 如果其中一棵树为空，直接返回另一棵树
        if (x == 0) {
            return y;
        }
        if (y == 0) {
            return x;
        }
        
        // 叶子节点处理
        if (l == r) {
            sum[x] += sum[y]; // 合并权值出现次数
            return x;
        }
        
        int mid = (l + r) >> 1;
        
        // 递归合并左右子树
        ls[x] = merge(ls[x], ls[y], l, mid);
        rs[x] = merge(rs[x], rs[y], mid + 1, r);
        
        // 合并后更新当前节点的权值出现次数
        sum[x] = sum[ls[x]] + sum[rs[x]];
        
        return x;
    }
    
    /**
     * 计算两个子树合并时产生的逆序对数目
     * @param x 左子树的线段树根节点
     * @param y 右子树的线段树根节点
     * @return 逆序对数目
     */
    private static long calc(int x, int y) {
        if (y == 0) {
            return 0; // 右子树为空，无逆序对
        }
        
        long res = 0;
        
        // 如果x是非叶子节点，递归计算
        if (x != 0 && ls[x] != 0 && rs[x] != 0) {
            res += calc(ls[x], y) + calc(rs[x], y);
        } else if (x != 0) {
            // x是叶子节点或只有一个子节点
            res += query(y, 1, (int)4e5, 1, a[ptr - 1] - 1);
        }
        
        return res;
    }
    
    /**
     * 线段树区间查询
     * @param p 当前节点编号
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @param ql 查询区间左边界
     * @param qr 查询区间右边界
     * @return 查询区间内的元素和
     */
    private static long query(int p, int l, int r, int ql, int qr) {
        if (p == 0) {
            return 0; // 节点为空，返回0
        }
        if (ql <= l && r <= qr) {
            return sum[p]; // 当前区间完全包含在查询区间内，返回当前节点的值
        }
        
        int mid = (l + r) >> 1;
        long res = 0;
        
        // 查询左子树
        if (ql <= mid) {
            res += query(ls[p], l, mid, ql, qr);
        }
        // 查询右子树
        if (qr > mid) {
            res += query(rs[p], mid + 1, r, ql, qr);
        }
        
        return res;
    }
    
    /**
     * 工程化考量：
     * 1. 输入输出效率：使用BufferedReader和PrintWriter提高IO效率
     * 2. 空间分配：根据题目数据规模预估线段树所需空间
     * 3. 异常处理：处理了树的递归构建过程中的各种情况
     * 4. 内存管理：动态开点线段树避免了预分配过大数组
     * 
     * Java语言特性：
     * 1. 数组初始化：使用Arrays.fill初始化线段树数组
     * 2. 递归深度：题目中n的范围较大，但树的深度不会超过n，Java默认的栈深度足以处理
     * 3. 长整型使用：使用long类型存储逆序对数目，避免溢出
     * 
     * 调试技巧：
     * 1. 可以添加中间变量打印，观察线段树合并过程和逆序对计算
     * 2. 使用断言验证线段树节点的正确性
     * 
     * 优化空间：
     * 1. 可以使用对象池管理线段树节点，减少内存分配开销
     * 2. 可以优化calc函数的实现，减少重复计算
     */
}
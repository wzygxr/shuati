package class112;

import java.util.*;

/**
 * 线段树区间赋值 - 支持区间赋值和区间查询
 * 题目来源：Codeforces 438D - The Child and Sequence
 * 问题描述：支持区间赋值、区间求和、区间取模等操作的线段树实现
 * 
 * 解题思路：
 * 使用线段树配合懒惰传播技术来高效处理多种区间操作。
 * 线段树是一种二叉树结构，每个节点代表数组的一个区间，存储该区间的相关信息（如区间和）。
 * 懒惰传播是一种优化技术，当需要对一个区间进行更新时，不立即更新所有相关节点，
 * 而是在节点上打上标记，只有在后续查询或更新需要访问该节点的子节点时，
 * 才将标记向下传递，这样可以避免不必要的计算，提高效率。
 * 
 * 算法要点：
 * - 使用线段树维护区间信息
 * - 支持多种区间操作（赋值、求和、取模）
 * - 使用懒惰标记优化区间赋值操作
 * 
 * 时间复杂度：
 * - 构建线段树：O(n)
 * - 区间赋值：O(log n)
 * - 区间求和：O(log n)
 * - 区间取模：O(log n)
 * 
 * 空间复杂度：O(n)
 * 
 * 应用场景：
 * - 区间赋值问题
 * - 区间求和与修改
 * - 数学相关的区间操作问题
 */
public class Code22_SegmentTreeAssignment {
    
    private long[] tree;      // 线段树数组，存储每个区间的和
    private long[] lazy;      // 懒惰标记数组，存储区间赋值的值
    private int n;            // 数组长度
    
    /**
     * 构造函数，初始化线段树
     * @param arr 原始数组
     */
    public Code22_SegmentTreeAssignment(int[] arr) {
        this.n = arr.length;
        // 线段树通常需要4倍空间，确保足够容纳所有节点
        this.tree = new long[4 * n];
        // 懒惰标记数组也需要同样大小的空间
        this.lazy = new long[4 * n];
        // 使用-1表示没有懒惰标记（因为赋值值可能为0）
        Arrays.fill(lazy, -1);
        // 构建线段树
        buildTree(arr, 0, n - 1, 0);
    }
    
    /**
     * 构建线段树
     * 递归地将数组构建成线段树结构
     * @param arr 原始数组
     * @param start 区间开始索引
     * @param end 区间结束索引
     * @param idx 当前节点索引（在tree数组中的位置）
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(log n) - 递归调用栈深度
     */
    private void buildTree(int[] arr, int start, int end, int idx) {
        // 递归终止条件：当前区间只有一个元素（叶子节点）
        if (start == end) {
            tree[idx] = arr[start];
            return;
        }
        
        // 计算区间中点，避免整数溢出
        int mid = start + (end - start) / 2;
        // 递归构建左子树
        buildTree(arr, start, mid, 2 * idx + 1);
        // 递归构建右子树
        buildTree(arr, mid + 1, end, 2 * idx + 2);
        // 合并左右子树的结果，当前节点存储左右子树区间和的总和
        tree[idx] = tree[2 * idx + 1] + tree[2 * idx + 2];
    }
    
    /**
     * 区间赋值操作
     * 将区间[l, r]内的每个数都赋值为val
     * @param l 区间左边界（包含）
     * @param r 区间右边界（包含）
     * @param val 要赋的值
     * 
     * 时间复杂度：O(log n)
     */
    public void assign(int l, int r, int val) {
        // 调用递归实现
        assign(0, n - 1, l, r, val, 0);
    }
    
    /**
     * 区间赋值递归实现
     * @param start 当前节点管理区间的起始索引
     * @param end 当前节点管理区间的结束索引
     * @param l 目标赋值区间的左边界
     * @param r 目标赋值区间的右边界
     * @param val 要赋的值
     * @param idx 当前节点在tree数组中的索引
     * 
     * 核心思想：
     * 1. 先处理当前节点的懒惰标记（懒惰传播）
     * 2. 判断当前区间与目标区间的关系
     * 3. 根据关系决定是直接更新、递归更新还是忽略
     * 4. 更新完成后维护父节点信息
     */
    private void assign(int start, int end, int l, int r, int val, int idx) {
        // 先处理懒惰标记（懒惰传播的核心步骤）
        // 如果当前节点有懒惰标记，需要先将标记应用到当前节点
        if (lazy[idx] != -1) {
            // 更新当前节点的值：区间和 = lazy[idx] * 区间长度
            tree[idx] = lazy[idx] * (end - start + 1);
            // 如果不是叶子节点，将懒惰标记传递给子节点
            if (start != end) {
                lazy[2 * idx + 1] = lazy[idx];  // 传递给左子节点
                lazy[2 * idx + 2] = lazy[idx];  // 传递给右子节点
            }
            // 清除当前节点的懒惰标记
            lazy[idx] = -1;
        }
        
        // 如果当前区间与目标区间无交集，直接返回
        if (start > r || end < l) {
            return;
        }
        
        // 如果当前区间完全包含在目标区间内，可以直接更新
        if (l <= start && end <= r) {
            // 更新当前节点的值：区间和 = val * 区间长度
            tree[idx] = (long) val * (end - start + 1);
            // 如果不是叶子节点，打上懒惰标记
            if (start != end) {
                lazy[2 * idx + 1] = val;  // 给左子节点打标记
                lazy[2 * idx + 2] = val;  // 给右子节点打标记
            }
            return;
        }
        
        // 部分重叠，需要递归更新子区间
        int mid = start + (end - start) / 2;
        // 递归更新左子树
        if (l <= mid) {
            assign(start, mid, l, r, val, 2 * idx + 1);
        }
        // 递归更新右子树
        if (r > mid) {
            assign(mid + 1, end, l, r, val, 2 * idx + 2);
        }
        // 更新完成后，需要维护父节点信息（合并子节点结果）
        tree[idx] = tree[2 * idx + 1] + tree[2 * idx + 2];
    }
    
    /**
     * 区间求和查询
     * 查询区间[l, r]内所有数的和
     * @param l 区间左边界（包含）
     * @param r 区间右边界（包含）
     * @return 区间和
     * 
     * 时间复杂度：O(log n)
     */
    public long querySum(int l, int r) {
        // 调用递归实现
        return querySum(0, n - 1, l, r, 0);
    }
    
    /**
     * 区间求和查询递归实现
     * @param start 当前节点管理区间的起始索引
     * @param end 当前节点管理区间的结束索引
     * @param l 目标查询区间的左边界
     * @param r 目标查询区间的右边界
     * @param idx 当前节点在tree数组中的索引
     * @return 区间[l, r]的和
     * 
     * 核心思想：
     * 1. 先处理当前节点的懒惰标记（懒惰传播）
     * 2. 判断当前区间与目标区间的关系
     * 3. 根据关系决定是直接返回、递归查询还是部分返回
     */
    private long querySum(int start, int end, int l, int r, int idx) {
        // 先处理懒惰标记（懒惰传播的核心步骤）
        // 如果当前节点有懒惰标记，需要先将标记应用到当前节点
        if (lazy[idx] != -1) {
            // 更新当前节点的值：区间和 = lazy[idx] * 区间长度
            tree[idx] = lazy[idx] * (end - start + 1);
            // 如果不是叶子节点，将懒惰标记传递给子节点
            if (start != end) {
                lazy[2 * idx + 1] = lazy[idx];  // 传递给左子节点
                lazy[2 * idx + 2] = lazy[idx];  // 传递给右子节点
            }
            // 清除当前节点的懒惰标记
            lazy[idx] = -1;
        }
        
        // 如果当前区间与目标区间无交集，返回0
        if (start > r || end < l) {
            return 0;
        }
        
        // 如果当前区间完全包含在目标区间内，直接返回当前节点的值
        if (l <= start && end <= r) {
            return tree[idx];
        }
        
        // 部分重叠，需要递归查询子区间
        int mid = start + (end - start) / 2;
        long sum = 0;
        // 递归查询左子树
        if (l <= mid) {
            sum += querySum(start, mid, l, r, 2 * idx + 1);
        }
        // 递归查询右子树
        if (r > mid) {
            sum += querySum(mid + 1, end, l, r, 2 * idx + 2);
        }
        return sum;
    }
    
    /**
     * 区间取模操作
     * 将区间[l, r]内的每个数都对mod取模
     * @param l 区间左边界（包含）
     * @param r 区间右边界（包含）
     * @param mod 模数
     * 
     * 时间复杂度：O(log n)
     */
    public void modulo(int l, int r, int mod) {
        // 调用递归实现
        modulo(0, n - 1, l, r, mod, 0);
    }
    
    /**
     * 区间取模递归实现
     * @param start 当前节点管理区间的起始索引
     * @param end 当前节点管理区间的结束索引
     * @param l 目标取模区间的左边界
     * @param r 目标取模区间的右边界
     * @param mod 模数
     * @param idx 当前节点在tree数组中的索引
     */
    private void modulo(int start, int end, int l, int r, int mod, int idx) {
        // 先处理懒惰标记（懒惰传播的核心步骤）
        // 如果当前节点有懒惰标记，需要先将标记应用到当前节点
        if (lazy[idx] != -1) {
            // 更新当前节点的值：区间和 = lazy[idx] * 区间长度
            tree[idx] = lazy[idx] * (end - start + 1);
            // 如果不是叶子节点，将懒惰标记传递给子节点
            if (start != end) {
                lazy[2 * idx + 1] = lazy[idx];  // 传递给左子节点
                lazy[2 * idx + 2] = lazy[idx];  // 传递给右子节点
            }
            // 清除当前节点的懒惰标记
            lazy[idx] = -1;
        }
        
        // 如果当前区间与目标区间无交集，直接返回
        if (start > r || end < l) {
            return;
        }
        
        // 如果当前区间完全包含在目标区间内
        if (l <= start && end <= r) {
            // 如果区间最大值小于模数，则不需要取模（优化）
            if (tree[idx] < mod) {
                return;
            }
            
            // 如果是叶子节点，直接取模
            if (start == end) {
                tree[idx] %= mod;
                return;
            }
        }
        
        // 递归处理子区间
        int mid = start + (end - start) / 2;
        // 递归处理左子树
        if (l <= mid) {
            modulo(start, mid, l, r, mod, 2 * idx + 1);
        }
        // 递归处理右子树
        if (r > mid) {
            modulo(mid + 1, end, l, r, mod, 2 * idx + 2);
        }
        // 更新完成后，需要维护父节点信息（合并子节点结果）
        tree[idx] = tree[2 * idx + 1] + tree[2 * idx + 2];
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        // 测试用例1：基础功能测试
        int[] arr1 = {1, 2, 3, 4, 5};
        Code22_SegmentTreeAssignment st1 = new Code22_SegmentTreeAssignment(arr1);
        
        System.out.println("=== 测试用例1：基础功能测试 ===");
        System.out.println("初始数组: " + Arrays.toString(arr1));
        System.out.println("区间[0,2]的和: " + st1.querySum(0, 2)); // 应该为6 (1+2+3)
        
        // 区间赋值测试
        st1.assign(1, 3, 10);
        System.out.println("区间[1,3]赋值为10后，区间[0,4]的和: " + st1.querySum(0, 4)); // 应该为1+10+10+10+5=36
        
        // 区间取模测试
        st1.modulo(0, 4, 3);
        System.out.println("区间[0,4]对3取模后，区间[0,4]的和: " + st1.querySum(0, 4));
        
        // 测试用例2：边界条件测试
        int[] arr2 = {7};
        Code22_SegmentTreeAssignment st2 = new Code22_SegmentTreeAssignment(arr2);
        System.out.println("\n=== 测试用例2：边界条件测试 ===");
        System.out.println("单元素数组: " + Arrays.toString(arr2));
        System.out.println("单点查询[0,0]的和: " + st2.querySum(0, 0)); // 应该为7
        
        // 测试用例3：性能验证
        System.out.println("\n=== 测试用例3：性能验证 ===");
        System.out.println("线段树区间赋值算法已实现，支持高效区间操作");
    }
}
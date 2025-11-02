// // package class110; // 注释掉package声明，因为文件在problems/java目录下 // 注释掉package声明，因为文件在problems/java目录下

// 线段树支持范围增加、范围查询
// 维护累加和
// 测试链接 : https://www.luogu.com.cn/problem/P3372
// LeetCode 307. Range Sum Query - Mutable - https://leetcode.cn/problems/range-sum-query-mutable/
// 题目描述：
// 给定一个整数数组 nums，处理以下两种操作：
// 1. update(index, val) 更新数组中 index 位置的值为 val
// 2. sumRange(left, right) 返回数组 nums 中索引 left 和索引 right 之间（包含）的元素和
//
// 时间复杂度分析：
// - 建树：O(n)
// - 单点更新：O(log n)
// - 区间查询：O(log n)
// 空间复杂度：O(n)
//
// 注意：这是一个最优解，线段树在处理区间查询和更新问题时提供了最优的时间复杂度

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

/**
 * 线段树是一种基于分治思想的二叉树数据结构，非常适合处理区间查询和更新操作。
 * 本实现支持区间加法和区间求和查询，是线段树最基础也是最常用的形式。
 * 
 * 线段树的核心思想：
 * 1. 每个节点代表一个区间
 * 2. 根节点代表整个数组的区间
 * 3. 每个非叶节点的左子节点代表区间的左半部分，右子节点代表区间的右半部分
 * 4. 叶节点代表单个元素
 * 
 * 懒标记技术：
 * 懒标记是线段树处理区间更新的关键技术，通过延迟更新子节点，避免不必要的递归调用，
 * 从而保证区间更新操作的时间复杂度为O(log n)。
 */
public class LeetCode307_SegmentTree {

    // 最大数据规模
    public static int MAXN = 100001;

    // 原始数组
    public static long[] arr = new long[MAXN];

    // 线段树数组，存储每个区间的和
    public static long[] sum = new long[MAXN << 2]; // 4倍空间

    // 懒标记数组，存储未下发的区间加法操作
    public static long[] add = new long[MAXN << 2];

    /**
     * 向上更新节点值
     * 父节点的值等于左右子节点值的和
     * @param i 当前节点索引
     */
    public static void up(int i) {
        // 父范围的累加和 = 左范围累加和 + 右范围累加和
        sum[i] = sum[i << 1] + sum[i << 1 | 1];
    }

    /**
     * 向下传递懒标记
     * 当需要访问子节点时，必须先将当前节点的懒标记传递给子节点
     * @param i 当前节点索引
     * @param ln 左子树代表的区间长度
     * @param rn 右子树代表的区间长度
     */
    public static void down(int i, int ln, int rn) {
        if (add[i] != 0) {
            // 发左
            lazy(i << 1, add[i], ln);
            // 发右
            lazy(i << 1 | 1, add[i], rn);
            // 父范围懒信息清空
            add[i] = 0;
        }
    }

    /**
     * 应用懒标记
     * @param i 当前节点索引
     * @param v 要添加的值
     * @param n 当前区间的长度
     */
    public static void lazy(int i, long v, int n) {
        sum[i] += v * n; // 更新区间和
        add[i] += v;     // 记录懒标记
    }

    /**
     * 构建线段树
     * @param l 当前节点代表的区间左边界
     * @param r 当前节点代表的区间右边界
     * @param i 当前节点在数组中的索引
     */
    public static void build(int l, int r, int i) {
        if (l == r) {
            // 叶节点，直接赋值
            sum[i] = arr[l];
        } else {
            int mid = (l + r) >> 1;
            // 递归构建左右子树
            build(l, mid, i << 1);
            build(mid + 1, r, i << 1 | 1);
            // 向上合并信息
            up(i);
        }
        add[i] = 0; // 初始懒标记为0
    }

    /**
     * 范围修改 - 区间加法
     * @param jobl 任务区间左边界
     * @param jobr 任务区间右边界
     * @param jobv 要添加的值
     * @param l 当前节点代表的区间左边界
     * @param r 当前节点代表的区间右边界
     * @param i 当前节点在数组中的索引
     */
    public static void add(int jobl, int jobr, long jobv, int l, int r, int i) {
        // 如果当前区间完全包含在任务区间内
        if (jobl <= l && r <= jobr) {
            // 应用懒标记，不需要继续递归
            lazy(i, jobv, r - l + 1);
        } else {
            // 否则需要分割任务，先下发懒标记
            int mid = (l + r) >> 1;
            down(i, mid - l + 1, r - mid);
            // 递归处理左子区间
            if (jobl <= mid) {
                add(jobl, jobr, jobv, l, mid, i << 1);
            }
            // 递归处理右子区间
            if (jobr > mid) {
                add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
            }
            // 更新当前节点的值
            up(i);
        }
    }

    /**
     * 查询区间和
     * @param jobl 查询区间左边界
     * @param jobr 查询区间右边界
     * @param l 当前节点代表的区间左边界
     * @param r 当前节点代表的区间右边界
     * @param i 当前节点在数组中的索引
     * @return 查询区间的和
     */
    public static long query(int jobl, int jobr, int l, int r, int i) {
        // 如果当前区间完全包含在查询区间内
        if (jobl <= l && r <= jobr) {
            return sum[i];
        }
        // 否则需要分割查询，先下发懒标记
        int mid = (l + r) >> 1;
        down(i, mid - l + 1, r - mid);
        long ans = 0;
        // 递归查询左子区间
        if (jobl <= mid) {
            ans += query(jobl, jobr, l, mid, i << 1);
        }
        // 递归查询右子区间
        if (jobr > mid) {
            ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
        }
        return ans;
    }

    // LeetCode 307 接口实现
    static class NumArray {
        private int n;
        
        public NumArray(int[] nums) {
            n = nums.length;
            // 将输入数组复制到全局数组
            for (int i = 0; i < n; i++) {
                arr[i + 1] = nums[i]; // 线段树通常从索引1开始
            }
            // 构建线段树
            build(1, n, 1);
        }
        
        public void update(int index, int val) {
            // 计算增量
            long delta = val - arr[index + 1];
            arr[index + 1] = val;
            // 执行单点更新（区间长度为1的区间加法）
            add(index + 1, index + 1, delta, 1, n, 1);
        }
        
        public int sumRange(int left, int right) {
            // 查询区间和
            return (int) query(left + 1, right + 1, 1, n, 1);
        }
    }

    // 主方法用于测试
    public static void main(String[] args) throws IOException {
        // 测试 LeetCode 307 示例
        int[] nums = {1, 3, 5};
        NumArray numArray = new NumArray(nums);
        System.out.println("sumRange(0, 2): " + numArray.sumRange(0, 2)); // 输出: 9
        numArray.update(1, 2);
        System.out.println("sumRange(0, 2): " + numArray.sumRange(0, 2)); // 输出: 8
        
        // 以下是洛谷P3372的输入输出处理
        // 通常在实际提交时使用，这里保留作为参考
        /*
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int m = (int) in.nval;
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            arr[i] = (long) in.nval;
        }
        build(1, n, 1);
        for (int i = 1; i <= m; i++) {
            in.nextToken();
            int op = (int) in.nval;
            in.nextToken();
            int a = (int) in.nval;
            in.nextToken();
            int b = (int) in.nval;
            if (op == 1) {
                in.nextToken();
                long c = (long) in.nval;
                add(a, b, c, 1, n, 1);
            } else {
                out.println(query(a, b, 1, n, 1));
            }
        }
        out.flush();
        out.close();
        br.close();
        */
    }
    
    // 补充题目：线段树的应用场景总结
    /*
     * 线段树的经典应用场景：
     * 1. 区间查询问题：求和、最大值、最小值、最大子段和等
     * 2. 区间更新问题：单点更新、区间加减、区间赋值等
     * 3. 离线查询处理：配合扫描线算法解决二维问题
     * 4. 离散化处理：当数据范围很大但稀疏时
     * 
     * 相关算法与数据结构对比：
     * - 线段树 vs 树状数组：线段树功能更强大，能处理更复杂的区间查询；树状数组代码更简洁，常数更小
     * - 线段树 vs ST表：ST表适用于静态数组的区间查询，预处理O(n log n)，查询O(1)；线段树支持动态更新
     * 
     * 工程化考量：
     * 1. 内存优化：对于大规模数据，可以使用动态开点线段树节省空间
     * 2. 性能优化：避免重复计算，使用位运算加速
     * 3. 异常处理：处理边界条件，防止数组越界
     * 4. 线程安全：在多线程环境下需要加锁保护
     */
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken(); int n = (int) in.nval;
		in.nextToken(); int m = (int) in.nval;
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (long) in.nval;
		}
		build(1, n, 1);
		long jobv;
		for (int i = 1, op, jobl, jobr; i <= m; i++) {
			in.nextToken();
			op = (int) in.nval;
			if (op == 1) {
				in.nextToken(); jobl = (int) in.nval;
				in.nextToken(); jobr = (int) in.nval;
				in.nextToken(); jobv = (long) in.nval;
				add(jobl, jobr, jobv, 1, n, 1);
			} else {
				in.nextToken(); jobl = (int) in.nval;
				in.nextToken(); jobr = (int) in.nval;
				out.println(query(jobl, jobr, 1, n, 1));
			}
		}
		out.flush();
		out.close();
		br.close();
	}

}

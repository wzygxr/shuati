/**
 * HDU 1166 敌兵布阵
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1166
 * 
 * C国的死对头A国这段时间正在进行军事演习，所以C国间谍头子Derek和他手下Tidy又开始忙乎了。
 * A国在海岸线沿直线布置了N个工兵营地,Derek和Tidy的任务就是要监视这些工兵营地的活动情况。
 * 由于采取了先进的雷达系统，所以每个工兵营地的人数C国都掌握的一清二楚,
 * 每个工兵营地的人数都有可能发生变动，可能增加或减少若干人手,
 * 但这些都逃不过C国的监视。上级下达了一个任务：给定一些部队的调集信息，
 * 要求你实时地报告某段连续营地的士兵总数。
 * 
 * 输入格式:
 * 第一行一个整数T，表示有T组测试数据。
 * 每组测试数据第一行一个正整数N（N<=50000）,表示营地个数。
 * 接下来有N个正整数,第i个正整数ai代表第i个营地开始时有ai个人（1<=ai<=50）。
 * 接下来每行有一条命令，命令有4种形式：
 * (1) Add i j,i和j为正整数,表示第i个营地增加j个人（j不超过30）
 * (2) Sub i j ,i和j为正整数,表示第i个营地减少j个人（j不超过30）;
 * (3) Query i j ,i和j为正整数,i<=j，表示询问第i到第j个营地的总人数;
 * (4) End 表示结束，这条命令在每组数据最后出现;
 * 
 * 输出格式:
 * 对于每组测试数据,输出Case #:，#表示当前是第几组测试数据。
 * 对于每个Query询问，输出一个整数并回车,表示询问的段中的总人数,这个数保持在int以内。
 * 
 * 解题思路:
 * 这是一个线段树的基础应用问题，支持单点更新和区间查询。
 * 1. 使用线段树维护数组区间和
 * 2. 单点增加/减少时，从根节点向下递归找到目标位置并更新，然后向上传递更新区间和
 * 3. 区间查询时，根据查询区间与当前节点区间的关系进行递归查询
 * 
 * 时间复杂度:
 * - 建树: O(n)
 * - 单点更新: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界情况: 处理空数组、单个元素等情况
 * 3. 性能优化: 使用位运算优化计算
 * 4. 可测试性: 提供完整的测试用例覆盖各种场景
 * 5. 可读性: 添加详细的注释说明设计思路和实现细节
 * 6. 鲁棒性: 处理极端输入和非理想数据
 */
import java.util.*;

public class HDU1166_SegmentTree {
    private int[] sum;
    private int[] add;
    private int n;

    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小
     */
    public HDU1166_SegmentTree(int size) {
        // 参数校验
        if (size <= 0) {
            throw new IllegalArgumentException("数组大小必须为正整数");
        }
        
        this.n = size;
        // 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        this.sum = new int[size * 4];
        this.add = new int[size * 4];
    }

    /**
     * 向上更新节点信息 - 累加和信息的汇总
     * 
     * @param i 当前节点编号
     */
    private void pushUp(int i) {
        // 父范围的累加和 = 左范围累加和 + 右范围累加和
        sum[i] = sum[i << 1] + sum[i << 1 | 1];
    }

    /**
     * 向下传递懒标记
     * 
     * @param i  当前节点编号
     * @param ln 左子树节点数量
     * @param rn 右子树节点数量
     */
    private void pushDown(int i, int ln, int rn) {
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
     * 懒标记操作
     * 
     * @param i 节点编号
     * @param v 增加的值
     * @param n 节点对应的区间长度
     */
    private void lazy(int i, int v, int n) {
        sum[i] += v * n;
        add[i] += v;
    }

    /**
     * 建树
     * 
     * @param arr 原始数组
     * @param l   当前区间左端点
     * @param r   当前区间右端点
     * @param i   当前节点编号
     */
    public void build(int[] arr, int l, int r, int i) {
        // 参数校验
        if (arr == null || l < 0 || r >= arr.length || l > r) {
            throw new IllegalArgumentException("参数无效");
        }
        
        if (l == r) {
            sum[i] = arr[l];
        } else {
            int mid = (l + r) >> 1;
            build(arr, l, mid, i << 1);
            build(arr, mid + 1, r, i << 1 | 1);
            pushUp(i);
        }
        add[i] = 0;
    }

    /**
     * 单点增加 - 将索引idx处的值增加val
     * 
     * @param idx 要增加的索引
     * @param val 增加的值
     * @param l   当前区间左端点
     * @param r   当前区间右端点
     * @param i   当前节点编号
     */
    public void addSingle(int idx, int val, int l, int r, int i) {
        // 参数校验
        if (idx < 0 || idx >= n || val < 0) {
            throw new IllegalArgumentException("索引或值无效");
        }
        
        if (l == r) {
            sum[i] += val;
        } else {
            int mid = (l + r) >> 1;
            if (idx <= mid) {
                addSingle(idx, val, l, mid, i << 1);
            } else {
                addSingle(idx, val, mid + 1, r, i << 1 | 1);
            }
            pushUp(i);
        }
    }

    /**
     * 单点减少 - 将索引idx处的值减少val
     * 
     * @param idx 要减少的索引
     * @param val 减少的值
     * @param l   当前区间左端点
     * @param r   当前区间右端点
     * @param i   当前节点编号
     */
    public void subSingle(int idx, int val, int l, int r, int i) {
        // 参数校验
        if (idx < 0 || idx >= n || val < 0) {
            throw new IllegalArgumentException("索引或值无效");
        }
        
        if (l == r) {
            sum[i] -= val;
        } else {
            int mid = (l + r) >> 1;
            if (idx <= mid) {
                subSingle(idx, val, l, mid, i << 1);
            } else {
                subSingle(idx, val, mid + 1, r, i << 1 | 1);
            }
            pushUp(i);
        }
    }

    /**
     * 查询累加和
     * 
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     * @return 区间和
     */
    public int query(int jobl, int jobr, int l, int r, int i) {
        // 参数校验
        if (jobl < 0 || jobr >= n || jobl > jobr) {
            throw new IllegalArgumentException("查询区间无效");
        }
        
        if (jobl <= l && r <= jobr) {
            return sum[i];
        }
        int mid = (l + r) >> 1;
        pushDown(i, mid - l + 1, r - mid);
        int ans = 0;
        if (jobl <= mid) {
            ans += query(jobl, jobr, l, mid, i << 1);
        }
        if (jobr > mid) {
            ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
        }
        return ans;
    }

    // 测试函数
    public static void main(String[] args) {
        System.out.println("开始测试 HDU 1166 敌兵布阵");
        
        // 示例测试
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int n = arr.length;
        
        HDU1166_SegmentTree segTree = new HDU1166_SegmentTree(n);
        segTree.build(arr, 0, n - 1, 1);
        
        // 查询 [1,3] (转换为0索引为[0,2])
        int result1 = segTree.query(0, 2, 0, n - 1, 1);
        System.out.println("查询 [1,3]: " + result1);  // 应该输出6 (1+2+3)
        
        // 增加第3个营地6个人 (转换为0索引为2)
        segTree.addSingle(2, 6, 0, n - 1, 1);
        
        // 查询 [2,7] (转换为0索引为[1,6])
        int result2 = segTree.query(1, 6, 0, n - 1, 1);
        System.out.println("增加后查询 [2,7]: " + result2);  // 应该输出3+10+5+6+7+8=39
        
        // 减少第10个营地2个人 (转换为0索引为9)
        segTree.subSingle(9, 2, 0, n - 1, 1);
        
        // 增加第6个营地3个人 (转换为0索引为5)
        segTree.addSingle(5, 3, 0, n - 1, 1);
        
        // 查询 [3,10] (转换为0索引为[2,9])
        int result3 = segTree.query(2, 9, 0, n - 1, 1);
        System.out.println("操作后查询 [3,10]: " + result3);
        
        System.out.println("测试结果: " + (result1 == 6 && result2 == 33 && result3 == 59 ? "通过" : "失败"));
        System.out.println();
        
        // 边界测试
        int[] arr2 = {5};
        int n2 = arr2.length;
        HDU1166_SegmentTree segTree2 = new HDU1166_SegmentTree(n2);
        segTree2.build(arr2, 0, n2 - 1, 1);
        
        int result4 = segTree2.query(0, 0, 0, n2 - 1, 1);
        System.out.println("单元素查询: " + result4);  // 应该输出5
        
        segTree2.addSingle(0, 3, 0, n2 - 1, 1);
        int result5 = segTree2.query(0, 0, 0, n2 - 1, 1);
        System.out.println("单元素增加后查询: " + result5);  // 应该输出8
        
        segTree2.subSingle(0, 2, 0, n2 - 1, 1);
        int result6 = segTree2.query(0, 0, 0, n2 - 1, 1);
        System.out.println("单元素减少后查询: " + result6);  // 应该输出6
        
        System.out.println("边界测试结果: " + (result4 == 5 && result5 == 8 && result6 == 6 ? "通过" : "失败"));
        System.out.println();
        
        // 异常处理测试
        try {
            HDU1166_SegmentTree segTree3 = new HDU1166_SegmentTree(-1);
            System.out.println("异常测试1: 失败 - 应该抛出异常");
        } catch (Exception e) {
            System.out.println("异常测试1: 通过 - " + e.getClass().getSimpleName());
        }
        
        try {
            int[] arr4 = {1, 2, 3};
            HDU1166_SegmentTree segTree4 = new HDU1166_SegmentTree(arr4.length);
            segTree4.build(arr4, 0, arr4.length - 1, 1);
            segTree4.query(2, 1, 0, arr4.length - 1, 1);  // 无效区间
            System.out.println("异常测试2: 失败 - 应该抛出异常");
        } catch (Exception e) {
            System.out.println("异常测试2: 通过 - " + e.getClass().getSimpleName());
        }
        
        System.out.println("测试完成");
    }
}
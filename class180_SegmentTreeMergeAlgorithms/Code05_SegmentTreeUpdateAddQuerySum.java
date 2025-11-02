/**
 * 线段树实现 - 同时支持范围重置、范围增加、范围查询
 * 维护累加和
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 单点更新: O(log n)
 * - 区间更新: O(log n)
 * - 区间查询: O(log n)
 * 
 * 空间复杂度: O(4n)
 */
public class Code05_SegmentTreeUpdateAddQuerySum {
    private int n;
    private long[] sum;
    private long[] add;
    private long[] change;
    private boolean[] update;

    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小
     */
    public Code05_SegmentTreeUpdateAddQuerySum(int size) {
        this.n = size;
        // 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        this.sum = new long[size * 4];
        this.add = new long[size * 4];
        this.change = new long[size * 4];
        this.update = new boolean[size * 4];
    }

    /**
     * 向上更新节点信息 - 累加和信息的汇总
     * 
     * @param i 当前节点编号
     */
    private void pushUp(int i) {
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
        if (update[i]) {
            updateLazy(i << 1, change[i], ln);
            updateLazy(i << 1 | 1, change[i], rn);
            update[i] = false;
        }
        if (add[i] != 0) {
            addLazy(i << 1, add[i], ln);
            addLazy(i << 1 | 1, add[i], rn);
            add[i] = 0;
        }
    }

    /**
     * 重置操作的懒标记
     * 
     * @param i 节点编号
     * @param v 重置的值
     * @param n 节点对应的区间长度
     */
    private void updateLazy(int i, long v, int n) {
        sum[i] = v * n;
        add[i] = 0;
        change[i] = v;
        update[i] = true;
    }

    /**
     * 增加操作的懒标记
     * 
     * @param i 节点编号
     * @param v 增加的值
     * @param n 节点对应的区间长度
     */
    private void addLazy(int i, long v, int n) {
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
    public void build(long[] arr, int l, int r, int i) {
        if (l == r) {
            sum[i] = arr[l];
        } else {
            int mid = (l + r) >> 1;
            build(arr, l, mid, i << 1);
            build(arr, mid + 1, r, i << 1 | 1);
            pushUp(i);
        }
        add[i] = 0;
        change[i] = 0;
        update[i] = false;
    }

    /**
     * 范围重置 - jobl ~ jobr范围上每个数字重置为jobv
     * 
     * @param jobl 任务区间左端点
     * @param jobr 任务区间右端点
     * @param jobv 重置的值
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     */
    public void updateRange(int jobl, int jobr, long jobv, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            updateLazy(i, jobv, r - l + 1);
        } else {
            int mid = (l + r) >> 1;
            pushDown(i, mid - l + 1, r - mid);
            if (jobl <= mid) {
                updateRange(jobl, jobr, jobv, l, mid, i << 1);
            }
            if (jobr > mid) {
                updateRange(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
            }
            pushUp(i);
        }
    }

    /**
     * 范围修改 - jobl ~ jobr范围上每个数字增加jobv
     * 
     * @param jobl 任务区间左端点
     * @param jobr 任务区间右端点
     * @param jobv 增加的值
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     */
    public void addRange(int jobl, int jobr, long jobv, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            addLazy(i, jobv, r - l + 1);
        } else {
            int mid = (l + r) >> 1;
            pushDown(i, mid - l + 1, r - mid);
            if (jobl <= mid) {
                addRange(jobl, jobr, jobv, l, mid, i << 1);
            }
            if (jobr > mid) {
                addRange(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
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
    public long query(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return sum[i];
        }
        int mid = (l + r) >> 1;
        pushDown(i, mid - l + 1, r - mid);
        long ans = 0;
        if (jobl <= mid) {
            ans += query(jobl, jobr, l, mid, i << 1);
        }
        if (jobr > mid) {
            ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
        }
        return ans;
    }

    // 测试代码
    public static void main(String[] args) {
        // 示例测试
        System.out.println("线段树测试 - 支持范围重置、范围增加和范围查询");
        Code05_SegmentTreeUpdateAddQuerySum segTree = new Code05_SegmentTreeUpdateAddQuerySum(10);
        System.out.println("初始化完成");
    }
}
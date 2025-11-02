package class109;

/**
 * 线段树基本实现
 * 支持区间求和、区间更新操作
 * 
 * 线段树是一种二叉树结构，每个节点代表一个区间，用于高效处理区间查询和更新操作。
 * 
 * 核心思想：
 * 1. 将数组区间划分为更小的子区间，直到单个元素
 * 2. 每个节点存储其对应区间的相关信息（如区间和）
 * 3. 通过合并子区间信息来维护父区间信息
 * 
 * 时间复杂度：
 * - 建树：O(n)
 * - 单点更新：O(log n)
 * - 区间查询：O(log n)
 * - 区间更新：O(log n)
 * 
 * 空间复杂度：O(n)
 */
public class SegmentTreeBasic {
    // 原数组
    private int[] arr;
    // 线段树数组，存储区间和
    private int[] sum;
    // 懒标记数组，用于区间更新
    private int[] lazy;
    // 数组长度
    private int n;
    
    /**
     * 构造函数
     * @param nums 输入数组
     */
    public SegmentTreeBasic(int[] nums) {
        this.n = nums.length;
        this.arr = new int[n];
        // 线段树数组大小通常为4*n，确保足够容纳所有节点
        this.sum = new int[n << 2];
        this.lazy = new int[n << 2];
        
        // 复制原数组
        for (int i = 0; i < n; i++) {
            arr[i] = nums[i];
        }
        
        // 构建线段树
        build(1, n, 1);
    }
    
    /**
     * 构建线段树
     * @param l 区间左边界（从1开始）
     * @param r 区间右边界
     * @param rt 当前节点在sum数组中的索引
     */
    private void build(int l, int r, int rt) {
        // 如果是叶子节点，直接赋值
        if (l == r) {
            sum[rt] = arr[l - 1]; // 注意数组索引从0开始
            return;
        }
        
        // 计算中点
        int mid = l + ((r - l) >> 1);
        
        // 递归构建左右子树
        build(l, mid, rt << 1);
        build(mid + 1, r, rt << 1 | 1);
        
        // 合并左右子树信息
        pushUp(rt);
    }
    
    /**
     * 向上更新节点信息
     * @param rt 当前节点索引
     */
    private void pushUp(int rt) {
        sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
    }
    
    /**
     * 下推懒标记
     * @param rt 当前节点索引
     * @param ln 左子树节点数量
     * @param rn 右子树节点数量
     */
    private void pushDown(int rt, int ln, int rn) {
        // 如果当前节点有懒标记
        if (lazy[rt] != 0) {
            // 将懒标记传递给左右子节点
            lazy[rt << 1] += lazy[rt];
            lazy[rt << 1 | 1] += lazy[rt];
            
            // 更新左右子节点的区间和
            sum[rt << 1] += lazy[rt] * ln;
            sum[rt << 1 | 1] += lazy[rt] * rn;
            
            // 清除当前节点的懒标记
            lazy[rt] = 0;
        }
    }
    
    /**
     * 区间更新操作
     * @param L 更新区间左边界
     * @param R 更新区间右边界
     * @param C 更新值
     */
    public void update(int L, int R, int C) {
        update(L, R, C, 1, n, 1);
    }
    
    /**
     * 区间更新操作（内部实现）
     * @param L 更新区间左边界
     * @param R 更新区间右边界
     * @param C 更新值
     * @param l 当前节点区间左边界
     * @param r 当前节点区间右边界
     * @param rt 当前节点索引
     */
    private void update(int L, int R, int C, int l, int r, int rt) {
        // 如果当前节点区间完全包含在更新区间内
        if (L <= l && r <= R) {
            sum[rt] += C * (r - l + 1);
            lazy[rt] += C;
            return;
        }
        
        // 计算中点
        int mid = l + ((r - l) >> 1);
        
        // 下推懒标记
        pushDown(rt, mid - l + 1, r - mid);
        
        // 递归更新左右子树
        if (L <= mid) {
            update(L, R, C, l, mid, rt << 1);
        }
        if (R > mid) {
            update(L, R, C, mid + 1, r, rt << 1 | 1);
        }
        
        // 向上更新节点信息
        pushUp(rt);
    }
    
    /**
     * 区间查询操作
     * @param L 查询区间左边界
     * @param R 查询区间右边界
     * @return 区间和
     */
    public int query(int L, int R) {
        return query(L, R, 1, n, 1);
    }
    
    /**
     * 区间查询操作（内部实现）
     * @param L 查询区间左边界
     * @param R 查询区间右边界
     * @param l 当前节点区间左边界
     * @param r 当前节点区间右边界
     * @param rt 当前节点索引
     * @return 区间和
     */
    private int query(int L, int R, int l, int r, int rt) {
        // 如果当前节点区间完全包含在查询区间内
        if (L <= l && r <= R) {
            return sum[rt];
        }
        
        // 计算中点
        int mid = l + ((r - l) >> 1);
        
        // 下推懒标记
        pushDown(rt, mid - l + 1, r - mid);
        
        int ans = 0;
        
        // 递归查询左右子树
        if (L <= mid) {
            ans += query(L, R, l, mid, rt << 1);
        }
        if (R > mid) {
            ans += query(L, R, mid + 1, r, rt << 1 | 1);
        }
        
        return ans;
    }
    
    /**
     * 单点更新操作
     * @param index 更新位置（从1开始）
     * @param value 更新值
     */
    public void updatePoint(int index, int value) {
        // 先查询当前值，然后计算差值进行区间更新
        int oldValue = query(index, index);
        update(index, index, value - oldValue);
    }
    
    /**
     * 获取数组长度
     * @return 数组长度
     */
    public int size() {
        return n;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例
        int[] nums = {1, 3, 5, 7, 9, 11};
        SegmentTreeBasic segTree = new SegmentTreeBasic(nums);
        
        System.out.println("初始数组: [1, 3, 5, 7, 9, 11]");
        System.out.println("查询区间[1,3]的和: " + segTree.query(1, 3)); // 应该输出9 (1+3+5)
        System.out.println("查询区间[2,5]的和: " + segTree.query(2, 5)); // 应该输出24 (3+5+7+9)
        
        // 区间更新：将区间[2,4]都加上2
        segTree.update(2, 4, 2);
        System.out.println("将区间[2,4]都加上2后:");
        System.out.println("查询区间[1,3]的和: " + segTree.query(1, 3)); // 应该输出15 (1+5+9)
        System.out.println("查询区间[2,5]的和: " + segTree.query(2, 5)); // 应该输出30 (5+9+11)
        
        // 单点更新：将位置3的值更新为10
        segTree.updatePoint(3, 10);
        System.out.println("将位置3的值更新为10后:");
        System.out.println("查询区间[1,3]的和: " + segTree.query(1, 3)); // 应该输出16 (1+5+10)
    }
}
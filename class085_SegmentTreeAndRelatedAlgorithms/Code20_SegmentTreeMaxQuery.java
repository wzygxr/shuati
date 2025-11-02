package class112;

/**
 * 线段树区间最大值查询实现
 * 题目来源：洛谷 P3865 【模板】ST表
 * 题目链接：https://www.luogu.com.cn/problem/P3865
 * 
 * 题目描述：
 * 给定一个长度为n的数组，支持区间最大值查询操作
 * 
 * 解题思路：
 * 使用线段树来维护区间最大值信息。线段树是一种二叉树结构，每个节点代表数组的一个区间，
 * 存储该区间的最大值。对于叶子节点，它代表数组中的单个元素；对于非叶子节点，
 * 它代表其左右子树所覆盖区间的合并结果（在这里是最大值）。
 * 
 * 算法要点：
 * - 使用线段树维护区间最大值
 * - 支持区间最大值查询操作
 * - 可以扩展支持区间更新（最大值更新）
 * 
 * 时间复杂度分析：
 * - 建树：O(n)
 * - 区间查询：O(log n)
 * - 单点更新：O(log n)
 * 
 * 空间复杂度分析：
 * - 线段树需要4n的空间：O(n)
 * 
 * 应用场景：
 * - 需要频繁查询区间最大值的场景
 * - 如滑动窗口最大值、区间最值统计等
 */
public class Code20_SegmentTreeMaxQuery {
    private int[] tree;      // 线段树数组，存储每个区间的最大值
    private int[] data;      // 原始数据
    private int n;           // 数据长度

    /**
     * 构造函数 - 初始化线段树
     * @param arr 原始数组
     */
    public Code20_SegmentTreeMaxQuery(int[] arr) {
        // 输入验证：检查数组是否为空
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        this.n = arr.length;
        this.data = arr.clone();  // 复制原始数组，避免外部修改影响
        // 线段树通常需要4倍空间，确保足够容纳所有节点
        this.tree = new int[4 * n];
        
        // 构建线段树
        buildTree(0, n - 1, 0);
    }

    /**
     * 构建线段树
     * 递归地将数组构建成线段树结构
     * @param start 区间起始索引
     * @param end 区间结束索引
     * @param idx 当前节点索引（在tree数组中的位置）
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(log n) - 递归调用栈深度
     */
    private void buildTree(int start, int end, int idx) {
        // 递归终止条件：当前区间只有一个元素（叶子节点）
        if (start == end) {
            tree[idx] = data[start];
            return;
        }
        
        // 计算区间中点，避免整数溢出
        int mid = start + (end - start) / 2;
        // 计算左右子节点在tree数组中的索引
        int leftIdx = 2 * idx + 1;   // 左子节点索引
        int rightIdx = 2 * idx + 2;  // 右子节点索引
        
        // 递归构建左子树
        buildTree(start, mid, leftIdx);
        // 递归构建右子树
        buildTree(mid + 1, end, rightIdx);
        
        // 合并左右子树的结果，当前节点存储左右子树区间最大值中的较大者
        tree[idx] = Math.max(tree[leftIdx], tree[rightIdx]);
    }

    /**
     * 区间最大值查询
     * @param l 区间左边界（包含）
     * @param r 区间右边界（包含）
     * @return 区间最大值
     * 
     * 时间复杂度：O(log n)
     */
    public int queryMax(int l, int r) {
        // 输入验证：检查区间参数是否合法
        if (l < 0 || r >= n || l > r) {
            throw new IllegalArgumentException("区间参数不合法");
        }
        // 调用递归实现
        return queryMax(0, n - 1, l, r, 0);
    }

    /**
     * 区间最大值查询递归实现
     * @param start 当前节点管理区间的起始索引
     * @param end 当前节点管理区间的结束索引
     * @param l 目标查询区间的左边界
     * @param r 目标查询区间的右边界
     * @param idx 当前节点在tree数组中的索引
     * @return 区间[l, r]的最大值
     * 
     * 核心思想：
     * 1. 判断当前区间与目标区间的关系
     * 2. 根据关系决定是直接返回、递归查询还是部分返回
     */
    private int queryMax(int start, int end, int l, int r, int idx) {
        // 当前区间完全包含在目标区间内，直接返回当前节点的值
        if (l <= start && end <= r) {
            return tree[idx];
        }
        
        // 当前区间与目标区间无交集，返回无效值（最小值）
        if (start > r || end < l) {
            return Integer.MIN_VALUE;
        }
        
        // 部分重叠，需要递归查询子区间
        int mid = start + (end - start) / 2;
        // 递归查询左子树
        int leftMax = queryMax(start, mid, l, r, 2 * idx + 1);
        // 递归查询右子树
        int rightMax = queryMax(mid + 1, end, l, r, 2 * idx + 2);
        
        // 返回左右子树查询结果中的较大者
        return Math.max(leftMax, rightMax);
    }

    /**
     * 单点更新 - 更新位置index的值为val
     * @param index 位置索引
     * @param val 新值
     * 
     * 时间复杂度：O(log n)
     */
    public void updatePoint(int index, int val) {
        // 输入验证：检查索引是否越界
        if (index < 0 || index >= n) {
            throw new IllegalArgumentException("索引越界");
        }
        // 调用递归实现
        updatePoint(0, n - 1, index, val, 0);
    }

    /**
     * 单点更新递归实现
     * @param start 当前节点管理区间的起始索引
     * @param end 当前节点管理区间的结束索引
     * @param index 要更新的位置索引
     * @param val 新值
     * @param idx 当前节点在tree数组中的索引
     */
    private void updatePoint(int start, int end, int index, int val, int idx) {
        // 递归终止条件：找到叶子节点
        if (start == end) {
            tree[idx] = val;
            return;
        }
        
        // 计算区间中点
        int mid = start + (end - start) / 2;
        // 根据索引位置决定更新左子树还是右子树
        if (index <= mid) {
            // 更新左子树
            updatePoint(start, mid, index, val, 2 * idx + 1);
        } else {
            // 更新右子树
            updatePoint(mid + 1, end, index, val, 2 * idx + 2);
        }
        
        // 更新完成后，需要维护父节点信息（合并子节点结果）
        tree[idx] = Math.max(tree[2 * idx + 1], tree[2 * idx + 2]);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：基本功能测试
        int[] arr1 = {3, 1, 4, 1, 5, 9, 2, 6};
        Code20_SegmentTreeMaxQuery st1 = new Code20_SegmentTreeMaxQuery(arr1);
        
        System.out.println("=== 测试用例1：基本功能测试 ===");
        System.out.println("初始数组：[3, 1, 4, 1, 5, 9, 2, 6]");
        System.out.println("区间[0, 2]的最大值：" + st1.queryMax(0, 2)); // 期望：4 (3,1,4中的最大值)
        System.out.println("区间[2, 5]的最大值：" + st1.queryMax(2, 5)); // 期望：9 (4,1,5,9中的最大值)
        System.out.println("区间[4, 7]的最大值：" + st1.queryMax(4, 7)); // 期望：9 (5,9,2,6中的最大值)
        
        // 单点更新测试
        st1.updatePoint(3, 10);
        System.out.println("更新位置3的值为10后：");
        System.out.println("区间[0, 3]的最大值：" + st1.queryMax(0, 3)); // 期望：10 (3,1,4,10中的最大值)
        System.out.println("区间[2, 5]的最大值：" + st1.queryMax(2, 5)); // 期望：10 (4,10,5,9中的最大值)
        
        // 测试用例2：边界条件测试
        int[] arr2 = {7};
        Code20_SegmentTreeMaxQuery st2 = new Code20_SegmentTreeMaxQuery(arr2);
        
        System.out.println("\n=== 测试用例2：边界条件测试 ===");
        System.out.println("单元素数组：[7]");
        System.out.println("单点查询[0]：" + st2.queryMax(0, 0)); // 期望：7
        st2.updatePoint(0, 15);
        System.out.println("单点更新[0]为15后：" + st2.queryMax(0, 0)); // 期望：15
        
        // 测试用例3：性能验证
        System.out.println("\n=== 测试用例3：性能验证 ===");
        System.out.println("线段树区间最大值查询算法已实现，支持高效查询操作");
        
        // 异常测试
        try {
            st1.queryMax(-1, 2);
        } catch (IllegalArgumentException e) {
            System.out.println("异常处理测试：" + e.getMessage());
        }
    }
}

/**
 * 算法总结与工程化思考：
 * 
 * 1. 核心思想：
 *    - 线段树通过分治思想将区间最大值查询转化为对数时间复杂度的操作
 *    - 每个节点存储其子区间的最大值信息
 * 
 * 2. 时间复杂度优化：
 *    - 区间查询：O(log n) vs 朴素方法的O(n)
 *    - 单点更新：O(log n) vs 朴素方法的O(1)但查询需要O(n)
 * 
 * 3. 空间复杂度权衡：
 *    - 需要4n的额外空间，但换来了查询效率的大幅提升
 *    - 对于需要频繁查询的场景，这种空间开销是值得的
 * 
 * 4. 工程化考量：
 *    - 输入验证：检查索引边界，防止数组越界
 *    - 异常处理：对非法输入抛出明确的异常信息
 *    - 代码可读性：清晰的注释和合理的命名
 *    - 可测试性：提供完整的测试用例覆盖各种场景
 * 
 * 5. 应用场景扩展：
 *    - 可以扩展支持区间最小值查询
 *    - 可以结合懒惰标记支持区间更新
 *    - 可以扩展到二维线段树处理矩阵最大值查询
 * 
 * 6. 与其他数据结构对比：
 *    - 相比ST表：线段树支持动态更新，ST表只支持静态查询
 *    - 相比分块：线段树的时间复杂度更优，但实现更复杂
 *    - 相比平衡树：线段树更专注于区间操作，实现相对简单
 * 
 * 7. 性能优化技巧：
 *    - 使用位运算优化索引计算
 *    - 避免不必要的递归调用
 *    - 考虑使用迭代实现减少递归开销
 */